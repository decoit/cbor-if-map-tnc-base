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
import de.decoit.simu.cbor.ifmap.enums.IfMapEventType;
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
 * Java representation of the IF-MAP event metadata.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class CBOREvent extends AbstractMultiValueMetadata {
	public static final String XML_NAME = "event";
	public static final String DISCOVERED_TIME = "discovered-time";
	public static final String DISCOVERER_ID = "discoverer-id";
	public static final String NAME = "name";
	public static final String MAGNITUDE = "magnitude";
	public static final String CONFIDENCE = "confidence";
	public static final String SIGNIFICANCE = "significance";
	public static final String INFORMATION = "information";
	public static final String TYPE = "type";
	public static final String OTHER_TYPE_DEFINITION = "other-type-definition";
	public static final String VULNERABILITY_URI = "vulnerability-uri";

	@Getter
	private final ZonedDateTime discoveredTime;
	@Getter
	private final String discovererId;
	@Getter
	private final String name;
	@Getter
	private final Integer magnitude;
	@Getter
	private final Integer confidence;
	@Getter
	private final IfMapSignificance significance;
	@Getter
	private String information;
	@Getter
	private IfMapEventType type;
	@Getter
	private String otherTypeDefinition;
	@Getter
	private String vulnerabilityUri;


	/**
	 * Create a new event metadata.
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
	 * @param name Event name
	 * @param discoveredTime Time the behavior was discovered
	 * @param discovererId ID of the client that discovered the behavior
	 * @param magnitude The event's magnitude, value from 0 to 100
	 * @param confidence Confidence of the detecting client
	 * @param significance Significance of the event
	 */
	public CBOREvent(String ifMapPublisherId, ZonedDateTime ifMapTimestamp, String name, ZonedDateTime discoveredTime, String discovererId, int magnitude, int confidence, IfMapSignificance significance) {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME, ifMapPublisherId, ifMapTimestamp);

		if(discoveredTime == null) {
			throw new IllegalArgumentException("Discovered time must not be null");
		}

		if(StringUtils.isBlank(discovererId)) {
			throw new IllegalArgumentException("Discoverer ID must not be blank");
		}

		if(StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Name must not be blank");
		}

		if(magnitude < 0 || magnitude > 100) {
			throw new IllegalArgumentException("Magnitude must have a value from 0 to 100");
		}

		if(confidence < 0 || confidence > 100) {
			throw new IllegalArgumentException("Confidence must have a value from 0 to 100");
		}

		if(significance == null) {
			throw new IllegalArgumentException("Significance time must not be null");
		}

		this.discoveredTime = TimestampHelper.toUTC(discoveredTime);
		this.discovererId = discovererId;
		this.name = name;
		this.magnitude = magnitude;
		this.confidence = confidence;
		this.significance = significance;

		this.information = null;
		this.type = null;
		this.otherTypeDefinition = null;
		this.vulnerabilityUri = null;
	}


	/**
	 * Create a new event metadata.
	 * This constructor should be used for metadata that is sent from the CLIENT to the SERVER. It does not include
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamps are stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * Magnitude must be given as a number with range from 0 to 100 with higher numbers meaning
	 * higher magnitude.
	 *
	 * @param name Event name
	 * @param discoveredTime Time the behavior was discovered
	 * @param discovererId ID of the client that discovered the behavior
	 * @param magnitude The event's magnitude, value from 0 to 100
	 * @param confidence Confidence of the detecting client
	 * @param significance Significance of the event
	 */
	public CBOREvent(String name, ZonedDateTime discoveredTime, String discovererId, int magnitude, int confidence, IfMapSignificance significance) {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME);

		if(discoveredTime == null) {
			throw new IllegalArgumentException("Discovered time must not be null");
		}

		if(StringUtils.isBlank(discovererId)) {
			throw new IllegalArgumentException("Discoverer ID must not be blank");
		}

		if(StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Name must not be blank");
		}

		if(magnitude < 0 || magnitude > 100) {
			throw new IllegalArgumentException("Magnitude must have a value from 0 to 100");
		}

		if(confidence < 0 || confidence > 100) {
			throw new IllegalArgumentException("Confidence must have a value from 0 to 100");
		}

		if(significance == null) {
			throw new IllegalArgumentException("Significance time must not be null");
		}

		this.discoveredTime = TimestampHelper.toUTC(discoveredTime);
		this.discovererId = discovererId;
		this.name = name;
		this.magnitude = magnitude;
		this.confidence = confidence;
		this.significance = significance;

		this.information = null;
		this.type = null;
		this.otherTypeDefinition = null;
		this.vulnerabilityUri = null;
	}


	/**
	 * Set an information for this metadata.
	 * The information may be null to remove this value from the metadata. If not null,
	 * the information MUST NOT be an empty string or whitespace only.
	 *
	 * @param information administrative-domain value, may be null
	 */
	public void setInformation(String information) {
		if(StringUtils.isWhitespace(information)) {
			throw new IllegalArgumentException("Information must not be empty or whitespace only");
		}

		this.information = information;
	}


	/**
	 * Set the event type.
	 * This method will raise an exception if called with IfMapEventType.OTHER.
	 *
	 * @param type Event type
	 */
	public void setType(IfMapEventType type) {
		this.setType(type, null);
	}


	/**
	 * Set the event type and other-type-definition.
	 * The other-type-definition parameter is required to be not blank if type is IfMapEventType.OTHER.
	 * For all other types the other-type-definition value will be discarded and replaced by null.
	 *
	 * @param type Event type
	 * @param otherTypeDefinition Other type definition if applicable, may be null
	 */
	public void setType(IfMapEventType type, String otherTypeDefinition) {
		if(type == null) {
			throw new IllegalArgumentException("type parameter must not be null");
		}

		if(type == IfMapEventType.OTHER && StringUtils.isBlank(otherTypeDefinition)) {
			throw new IllegalArgumentException("other-type-definition must not be blank if type is OTHER");
		}
		// No other type definition is required if type is different from OTHER
		else if(type != IfMapEventType.OTHER) {
			otherTypeDefinition = null;
		}

		this.type = type;
		this.otherTypeDefinition = otherTypeDefinition;
	}


	/**
	 * Set an vulnerability URI for this metadata.
	 * The vulnerability URI may be null to remove this value from the metadata. If not null,
	 * the vulnerability URI MUST NOT be an empty string or whitespace only.
	 *
	 * @param vulnerabilityUri Vulnerability URI, may be null
	 */
	public void setVulnerabilityUri(String vulnerabilityUri) {
		if(StringUtils.isWhitespace(vulnerabilityUri)) {
			throw new IllegalArgumentException("Vulnerability URI must not be empty or whitespace only");
		}

		this.vulnerabilityUri = vulnerabilityUri;
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

			// Serialize 'name'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBOREvent.NAME, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.name));
			}


			// Serialize 'discovered-time'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBOREvent.DISCOVERED_TIME, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(TimestampHelper.toEpochTimeDataItem(this.discoveredTime));
			}


			// Serialize 'discoverer-id'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBOREvent.DISCOVERER_ID, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.discovererId));
			}


			// Serialize 'magnitude'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBOREvent.MAGNITUDE, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnsignedInteger(this.magnitude));
			}


			// Serialize 'confidence'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBOREvent.CONFIDENCE, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnsignedInteger(this.confidence));
			}


			// Serialize 'significance'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBOREvent.SIGNIFICANCE, elementEntry);
				DataItem cborValue = this.getNestedElementEnumValueMapping(CBOREvent.SIGNIFICANCE, this.significance.getXmlName(), elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(cborValue);
			}


			// Serialize 'type'
			if(this.type != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBOREvent.TYPE, elementEntry);
				DataItem cborValue = this.getNestedElementEnumValueMapping(CBOREvent.TYPE, this.type.getXmlName(), elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(cborValue);
			}


			// Serialize 'other-type-definition'
			if(this.otherTypeDefinition != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBOREvent.OTHER_TYPE_DEFINITION, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.otherTypeDefinition));
			}


			// Serialize 'information'
			if(this.information != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBOREvent.INFORMATION, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.information));
			}


			// Serialize 'vulnerability-uri'
			if(this.vulnerabilityUri != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBOREvent.VULNERABILITY_URI, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.vulnerabilityUri));
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
