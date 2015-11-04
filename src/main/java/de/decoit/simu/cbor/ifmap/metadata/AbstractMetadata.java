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
package de.decoit.simu.cbor.ifmap.metadata;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnicodeString;
import de.decoit.simu.cbor.ifmap.AbstractElementBase;
import de.decoit.simu.cbor.ifmap.enums.IfMapCardinality;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
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
 * Abstract base class for all metadata classes.
 * It defines basic attributes that any metadata must contain. However, new metadata
 * classes MUST NOT extend this class. Use the base classes with defined cardinality,
 * {@link AbstractSingleValueMetadata} and {@link AbstractMultiValueMetadata} instead.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public abstract class AbstractMetadata extends AbstractElementBase {
	public static final String IFMAP_PUBLISHER_ID = "ifmap-publisher-id";
	public static final String IFMAP_TIMESTAMP = "ifmap-timestamp";
	public static final String IFMAP_TIMESTAMP_FRACTION = "ifmap-timestamp-fraction";
	public static final String IFMAP_CARDINALITY = "ifmap-cardinality";

	@Getter
	protected final String ifMapPublisherId;
	@Getter
	protected final ZonedDateTime ifMapTimestamp;
	@Getter
	protected final IfMapCardinality ifMapCardinality;


	/**
	 * Create a new metadata object with the specified information.
	 * This constructor should be used for metadata that is sent from the SERVER to the CLIENT. It includes
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 *
	 * @param namespace
	 * @param elementName Metadata element name
	 * @param ifMapPublisherId IF-MAP client publisher ID
	 * @param ifMapTimestamp Timestamp for the metadata
	 * @param ifMapCardinality Cardinality value
	 */
	public AbstractMetadata(String namespace, String elementName, String ifMapPublisherId, ZonedDateTime ifMapTimestamp, IfMapCardinality ifMapCardinality) {
		super(namespace, elementName);

		if(StringUtils.isBlank(ifMapPublisherId)) {
			throw new IllegalArgumentException("IF-MAP publisher ID must not be blank");
		}

		if(ifMapTimestamp == null) {
			throw new IllegalArgumentException("IF-MAP timestamp must not be null");
		}

		if(ifMapCardinality == null) {
			throw new IllegalArgumentException("IF-MAP cardinality must not be null");
		}

		this.ifMapPublisherId = ifMapPublisherId;
		this.ifMapTimestamp = TimestampHelper.toUTC(ifMapTimestamp);
		this.ifMapCardinality = ifMapCardinality;
	}


	/**
	 * Create a new metadata object with the specified information.
	 * This constructor should be used for metadata that is sent from the CLIENT to the SERVER. It does not include
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 *
	 * @param namespace
	 * @param elementName Metadata element name
	 * @param ifMapCardinality Cardinality value
	 */
	public AbstractMetadata(String namespace, String elementName, IfMapCardinality ifMapCardinality) {
		super(namespace, elementName);

		if(ifMapCardinality == null) {
			throw new IllegalArgumentException("IF-MAP cardinality must not be null");
		}

		this.ifMapPublisherId = null;
		this.ifMapTimestamp = null;
		this.ifMapCardinality = ifMapCardinality;
	}


	@Override
	protected void serializeAttributes(final ArrayBuilder<?> builder) throws CBORSerializationException {
		try {
			// Build dictionary path to this element
			StringBuilder dictPathSb = new StringBuilder("<");
			dictPathSb.append(this.namespace);
			dictPathSb.append(">");
			dictPathSb.append(this.elementName);

			// Get dictionary entry for this element
			DictionarySimpleElement elementEntry = DictionaryProvider.getInstance().findElementByPath(dictPathSb.toString());

			// Serialize 'ifmap-publisher-id'
			if(this.ifMapPublisherId != null) {
				DataItem cborName = this.getAttributeNameMapping(AbstractMetadata.IFMAP_PUBLISHER_ID, elementEntry);

				builder.add(cborName);
				builder.add(new UnicodeString(this.ifMapPublisherId));
			}


			// Serialize 'ifmap-timestamp' and 'ifmap-timestamp-fraction'
			if(this.ifMapTimestamp != null) {
				DataItem timestampCborName = this.getAttributeNameMapping(AbstractMetadata.IFMAP_TIMESTAMP, elementEntry);
				DataItem timestampFractionCborName = this.getAttributeNameMapping(AbstractMetadata.IFMAP_TIMESTAMP_FRACTION, elementEntry);

				builder.add(timestampCborName);
				builder.add(TimestampHelper.toEpochTimeDataItem(this.ifMapTimestamp));

				builder.add(timestampFractionCborName);
				builder.add(TimestampHelper.toIfMapTimestampFractionDataItem(this.ifMapTimestamp));
			}


			// Serialize 'ifmap-cardinality'
			{
				DataItem cborName = this.getAttributeNameMapping(AbstractMetadata.IFMAP_CARDINALITY, elementEntry);
				DataItem cborValue = this.getAttributeEnumValueMapping(AbstractMetadata.IFMAP_CARDINALITY, this.ifMapCardinality.getXmlName(), elementEntry);

				builder.add(cborName);
				builder.add(cborValue);
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
