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
package de.decoit.simu.cbor.ifmap.metadata.singlevalue;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
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



/**
 * Java representation of the IF-MAP client-time operational metadata.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORClientTime extends AbstractSingleValueMetadata {
	public static final String XML_NAME = "client-time";
	public static final String CURRENT_TIME = "current-time";

	@Getter
	private final ZonedDateTime currentTime;


	/**
	 * Create a new client-time metadata.
	 * This constructor should be used for metadata that is sent from the SERVER to the CLIENT. It includes
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The current time and timestamp are stored with UTC timezone. If a current time or timestamp
	 * with another timezone are passed into the constructor, they will be converted to UTC.
	 *
	 * @param ifMapPublisherId IF-MAP client publisher ID
	 * @param ifMapTimestamp Timestamp for the metadata
	 * @param currentTime Value for the current-time atrribute
	 */
	public CBORClientTime(String ifMapPublisherId, ZonedDateTime ifMapTimestamp, ZonedDateTime currentTime) {
		super(IfMapNamespaces.IFMAP_OPEARATIONAL_METADATA, XML_NAME, ifMapPublisherId, ifMapTimestamp);

		if(currentTime == null) {
			throw new IllegalArgumentException("Current time must not be null");
		}

		this.currentTime = TimestampHelper.toUTC(currentTime);
	}


	/**
	 * Create a new client-time metadata.
	 * This constructor should be used for metadata that is sent from the CLIENT to the SERVER. It does not include
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The current time and timestamp are stored with UTC timezone. If a current time or timestamp
	 * with another timezone are passed into the constructor, they will be converted to UTC.
	 *
	 * @param currentTime Value for the current-time atrribute
	 */
	public CBORClientTime(ZonedDateTime currentTime) {
		super(IfMapNamespaces.IFMAP_OPEARATIONAL_METADATA, XML_NAME);

		if(currentTime == null) {
			throw new IllegalArgumentException("Current time must not be null");
		}

		this.currentTime = TimestampHelper.toUTC(currentTime);
	}


	@Override
	protected void serializeAttributes(final ArrayBuilder<?> builder) throws CBORSerializationException {
		// Serialize attributes of parent class
		super.serializeAttributes(builder);

		try {
			// Build dictionary path to this element
			StringBuilder dictPathSb = new StringBuilder("<");
			dictPathSb.append(this.namespace);
			dictPathSb.append(">");
			dictPathSb.append(this.elementName);

			// Get dictionary entry for this element
			DictionarySimpleElement elementEntry = DictionaryProvider.getInstance().findElementByPath(dictPathSb.toString());

			// Serialize 'current-time'
			{
				DataItem cborName = this.getAttributeNameMapping(CBORClientTime.CURRENT_TIME, elementEntry);

				builder.add(cborName);
				builder.add(TimestampHelper.toEpochTimeDataItem(this.currentTime));
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
