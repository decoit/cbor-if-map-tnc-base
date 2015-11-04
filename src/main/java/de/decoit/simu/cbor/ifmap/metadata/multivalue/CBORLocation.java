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
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.util.DictionaryHelper;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.ifmap.util.TimestampHelper;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of the IF-MAP location metadata.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class CBORLocation extends AbstractMultiValueMetadata {
	public static final String XML_NAME = "location";
	public static final String DISCOVERED_TIME = "discovered-time";
	public static final String DISCOVERER_ID = "discoverer-id";
	public static final String LOCATION_INFORMATION = "location-information";

	@Getter
	private final ZonedDateTime discoveredTime;
	@Getter
	private final String discovererId;
	private final Set<LocationInformation> locationInformation;


	/**
	 * Create a new server-capability metadata.
	 * This constructor should be used for metadata that is sent from the SERVER to the CLIENT. It includes
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamp is stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * Attribute for locationInformation is initialized with an empty set. This set MUST be filled with
	 * at least one valid item, otherwise the serialization process will not be successful.
	 *
	 * @param ifMapPublisherId IF-MAP client publisher ID
	 * @param ifMapTimestamp Timestamp for the metadata
	 * @param discoveredTime Time the behavior was discovered
	 * @param discovererId ID of the client that discovered the behavior
	 */
	public CBORLocation(String ifMapPublisherId, ZonedDateTime ifMapTimestamp, ZonedDateTime discoveredTime, String discovererId) {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME, ifMapPublisherId, ifMapTimestamp);

		if(discoveredTime == null) {
			throw new IllegalArgumentException("Discovered time must not be null");
		}

		if(StringUtils.isBlank(discovererId)) {
			throw new IllegalArgumentException("Discoverer ID must not be blank");
		}

		this.discoveredTime = TimestampHelper.toUTC(discoveredTime);
		this.discovererId = discovererId;
		this.locationInformation = new HashSet<>();
	}


	/**
	 * Create a new server-capability metadata.
	 * This constructor should be used for metadata that is sent from the CLIENT to the SERVER. It does not include
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamp is stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * Attribute for locationInformation is initialized with an empty set. This set MUST be filled with
	 * at least one valid item, otherwise the serialization process will not be successful.
	 *
	 * @param discoveredTime Time the behavior was discovered
	 * @param discovererId ID of the client that discovered the behavior
	 */
	public CBORLocation(ZonedDateTime discoveredTime, String discovererId) {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME);

		if(discoveredTime == null) {
			throw new IllegalArgumentException("Discovered time must not be null");
		}

		if(StringUtils.isBlank(discovererId)) {
			throw new IllegalArgumentException("Discoverer ID must not be blank");
		}

		this.discoveredTime = TimestampHelper.toUTC(discoveredTime);
		this.discovererId = discovererId;
		this.locationInformation = new HashSet<>();
	}


	/**
	 * Returns an immutable view of the localtion information set.
	 *
	 * @return Immutable set view
	 */
	public Set<LocationInformation> getLocationInformation() {
		return Collections.unmodifiableSet(this.locationInformation);
	}


	/**
	 * Add a new location information to this metadata.
	 *
	 * @param locationType Location type
	 * @param locationValue Location value
	 */
	public void addLocationInformation(String locationType, String locationValue) {
		LocationInformation locInfo = new LocationInformation(locationType, locationValue);

		this.locationInformation.add(locInfo);
	}


	/**
	 * Remove an existing location information from this metadata.
	 *
	 * @param locationType Location type
	 * @param locationValue Location value
	 */
	public void removeLocationInformation(String locationType, String locationValue) {
		LocationInformation locInfo = new LocationInformation(locationType, locationValue);

		this.locationInformation.remove(locInfo);
	}


	@Override
	protected void serializeNestedElements(final ArrayBuilder<?> builder) throws CBORSerializationException {
		if(this.locationInformation.isEmpty()) {
			throw new CBORSerializationException("Location information set must not be empty");
		}

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
				DataItem cborName = this.getNestedElementNameMapping(CBORLocation.DISCOVERED_TIME, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(TimestampHelper.toEpochTimeDataItem(this.discoveredTime));
			}


			// Serialize 'discoverer-id'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBORLocation.DISCOVERER_ID, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.discovererId));
			}


			// Serialize 'location-information'
			for(LocationInformation l : this.locationInformation) {
				l.cborSerialize(builder, elementEntry);
			};
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}


	@EqualsAndHashCode
	@ToString
	public class LocationInformation {
		public static final String XML_NAME = "location-information";
		public static final String TYPE = "type";
		public static final String VALUE = "value";

		@Getter
		private final String type;
		@Getter
		private final String value;


		/**
		 * Create a new location information object with the specified type and value.
		 *
		 * @param type Location information type
		 * @param value Location information value
		 */
		public LocationInformation(String type, String value) {
			if(StringUtils.isBlank(type)) {
				throw new IllegalArgumentException("Location information type must not be blank");
			}

			if(StringUtils.isBlank(value)) {
				throw new IllegalArgumentException("Location information value must not be blank");
			}

			this.type = type;
			this.value = value;
		}


		private void cborSerialize(final ArrayBuilder<?> builder, final DictionarySimpleElement parentElementEntry) throws CBORSerializationException {
			try {
				DataItem cborName = getNestedElementNameMapping(LocationInformation.XML_NAME, parentElementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);

				// Get dictionary entry for this element
				DictionarySimpleElement elementEntry = DictionaryHelper.findNestedElement(LocationInformation.XML_NAME, parentElementEntry);

				ArrayBuilder<?> attrBuilder = builder.addArray();
				serializeAttributes(attrBuilder, elementEntry);

				builder.addArray();
			}
			catch(DictionaryPathException | RuntimeException ex) {
				throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
			}
		}


		private void serializeAttributes(final ArrayBuilder<?> builder, DictionarySimpleElement elementEntry) throws CBORSerializationException {
			// Serialize 'type'
			{
				DataItem cborName = getAttributeNameMapping(LocationInformation.TYPE, elementEntry);

				builder.add(cborName);
				builder.add(new UnicodeString(this.type));
			}


			// Serialize 'value'
			{
				DataItem cborName = getAttributeNameMapping(LocationInformation.VALUE, elementEntry);

				builder.add(cborName);
				builder.add(new UnicodeString(this.value));
			}
		}
	}
}
