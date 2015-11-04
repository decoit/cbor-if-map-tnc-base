/* 
 * Copyright 2015 DECOIT GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.decoit.simu.cbor.ifmap.metadata.multivalue;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;
import de.decoit.simu.cbor.ifmap.enums.IfMapSignificance;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.ifmap.util.TimestampHelper;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.time.ZonedDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of the IF-MAP unexpected-behavior metadata.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class CBORUnexpectedBehavior extends AbstractMultiValueMetadata {
	public static final String XML_NAME = "unexpected-behavior";
	public static final String DISCOVERED_TIME = "discovered-time";
	public static final String DISCOVERER_ID = "discoverer-id";
	public static final String MAGNITUDE = "magnitude";
	public static final String CONFIDENCE = "confidence";
	public static final String SIGNIFICANCE = "significance";
	public static final String INFORMATION = "information";
	public static final String TYPE = "type";

	@Getter
	private final ZonedDateTime discoveredTime;
	@Getter
	private final String discovererId;
	@Getter
	private String information;
	@Getter
	private final Integer magnitude;
	@Getter
	private Integer confidence;
	@Getter
	private final IfMapSignificance significance;
	@Getter
	private String type;


	/**
	 * Create a new unexpected-behavior metadata.
	 * This constructor should be used for metadata that is sent from the SERVER to the CLIENT. It includes
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamps are stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * Magnitude must be given as a number with range from 0 to 100 with higher numbers meaning
	 * higher magnitude.
	 *
	 * @param ifMapPublisherId IF-MAP client publisher ID
	 * @param ifMapTimestamp Timestamp for the metadata
	 * @param discoveredTime Time the behavior was discovered
	 * @param discovererId ID of the client that discovered the behavior
	 * @param magnitude The behavior's magnitude, value from 0 to 100
	 * @param significance Significance of the behavior
	 */
	public CBORUnexpectedBehavior(String ifMapPublisherId, ZonedDateTime ifMapTimestamp, ZonedDateTime discoveredTime, String discovererId, int magnitude, IfMapSignificance significance) {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME, ifMapPublisherId, ifMapTimestamp);

		if(discoveredTime == null) {
			throw new IllegalArgumentException("Discovered time must not be null");
		}

		if(StringUtils.isBlank(discovererId)) {
			throw new IllegalArgumentException("Discoverer ID must not be blank");
		}

		if(magnitude < 0 || magnitude > 100) {
			throw new IllegalArgumentException("Magnitude must have a value from 0 to 100");
		}

		if(significance == null) {
			throw new IllegalArgumentException("Significance time must not be null");
		}

		this.discoveredTime = TimestampHelper.toUTC(discoveredTime);
		this.discovererId = discovererId;
		this.magnitude = magnitude;
		this.significance = significance;

		this.information = null;
		this.confidence = null;
		this.type = null;
	}


	/**
	 * Create a new unexpected-behavior metadata.
	 * This constructor should be used for metadata that is sent from the CLIENT to the SERVER. It does not include
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamps are stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * Magnitude must be given as a number with range from 0 to 100 with higher numbers meaning
	 * higher magnitude.
	 *
	 * @param discoveredTime Time the behavior was discovered
	 * @param discovererId ID of the client that discovered the behavior
	 * @param magnitude The behavior's magnitude, value from 0 to 100
	 * @param significance Significance of the behavior
	 */
	public CBORUnexpectedBehavior(ZonedDateTime discoveredTime, String discovererId, int magnitude, IfMapSignificance significance) {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME);

		if(discoveredTime == null) {
			throw new IllegalArgumentException("Discovered time must not be null");
		}

		if(StringUtils.isBlank(discovererId)) {
			throw new IllegalArgumentException("Discoverer ID must not be blank");
		}

		if(magnitude < 0 || magnitude > 100) {
			throw new IllegalArgumentException("Magnitude must have a value from 0 to 100");
		}

		if(significance == null) {
			throw new IllegalArgumentException("Significance time must not be null");
		}

		this.discoveredTime = TimestampHelper.toUTC(discoveredTime);
		this.discovererId = discovererId;
		this.magnitude = magnitude;
		this.significance = significance;

		this.information = null;
		this.confidence = null;
		this.type = null;
	}


	/**
	 * Set the information string for this metadata.
	 * The information may be null to remove this value from the metadata. If not null,
	 * the information MUST NOT be an empty string or whitespace only.
	 *
	 * @param information Information string
	 */
	public void setInformation(String information) {
		if(StringUtils.isWhitespace(information)) {
			throw new IllegalArgumentException("Information must not be whitespace only");
		}

		this.information = information;
	}


	/**
	 * Set the type string for this metadata.
	 * The type may be null to remove this value from the metadata. If not null,
	 * the type MUST NOT be an empty string or whitespace only.
	 *
	 * @param type Type string
	 */
	public void setType(String type) {
		if(StringUtils.isWhitespace(type)) {
			throw new IllegalArgumentException("Type must not be whitespace only");
		}

		this.type = type;
	}


	/**
	 * Set the confidence value for this metadata.
	 * The confidence may be null to remove this value from the metadata. If not null,
	 * the confidence MUST be a number with range from 0 to 100.
	 *
	 * @param confidence Confidence value
	 */
	public void setConfidence(Integer confidence) {
		if(confidence != null && (confidence < 0 || confidence > 100)) {
			throw new IllegalArgumentException("Confidence must have a value from 0 to 100");
		}

		this.confidence = confidence;
	}


	@Override
	protected void serializeNestedElements(final ArrayBuilder<?> builder) throws CBORSerializationException {
		try {
			// Build dictionary path to this element
			StringBuilder dictPathSb = new StringBuilder("<");
			dictPathSb.append(this.namespace);
			dictPathSb.append(">");
			dictPathSb.append(this.elementName);

			// Get dictionary entry for this element
			DictionarySimpleElement elementEntry = DictionaryProvider.getInstance().findElementByPath(dictPathSb.toString());

			// Serialize 'discovered-time'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBORUnexpectedBehavior.DISCOVERED_TIME, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(TimestampHelper.toEpochTimeDataItem(this.discoveredTime));
			}


			// Serialize 'discoverer-id'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBORUnexpectedBehavior.DISCOVERER_ID, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.discovererId));
			}


			// Serialize 'magnitude'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBORUnexpectedBehavior.MAGNITUDE, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnsignedInteger(this.magnitude));
			}


			// Serialize 'significance'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBORUnexpectedBehavior.SIGNIFICANCE, elementEntry);
				DataItem cborValue = this.getNestedElementEnumValueMapping(CBORUnexpectedBehavior.SIGNIFICANCE, this.significance.getXmlName(), elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(cborValue);
			}


			// Serialize 'confidence'
			if(this.confidence != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORUnexpectedBehavior.CONFIDENCE, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnsignedInteger(this.confidence));
			}


			// Serialize 'type'
			if(this.type != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORUnexpectedBehavior.TYPE, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.type));
			}


			// Serialize 'information'
			if(this.information != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORUnexpectedBehavior.INFORMATION, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.information));
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
