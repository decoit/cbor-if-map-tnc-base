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
 * Java representation of the IF-MAP device-characteristic metadata.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class CBORDeviceCharacteristic extends AbstractMultiValueMetadata {
	public static final String XML_NAME = "device-characteristic";
	public static final String DISCOVERED_TIME = "discovered-time";
	public static final String DISCOVERER_ID = "discoverer-id";
	public static final String DISCOVERY_METHOD = "discovery-method";
	public static final String MANUFACTURER = "manufacturer";
	public static final String MODEL = "model";
	public static final String OS = "os";
	public static final String OS_VERSION = "os-version";
	public static final String DEVICE_TYPE = "device-type";

	@Getter
	private final ZonedDateTime discoveredTime;
	@Getter
	private final String discovererId;
	@Getter
	private final String discoveryMethod;
	@Getter
	private String manufacturer;
	@Getter
	private String model;
	@Getter
	private String os;
	@Getter
	private String osVersion;
	@Getter
	private String deviceType;


	/**
	 * Create a new device-characteristic metadata.
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
	 * @param discoveredTime Time the charcteristic was discovered
	 * @param discovererId ID of the client that discovered the charcteristic
	 * @param discoveryMethod Used method of charcteristic discovery
	 */
	public CBORDeviceCharacteristic(String ifMapPublisherId, ZonedDateTime ifMapTimestamp, ZonedDateTime discoveredTime, String discovererId, String discoveryMethod) {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME, ifMapPublisherId, ifMapTimestamp);

		if(discoveredTime == null) {
			throw new IllegalArgumentException("Discovered time must not be null");
		}

		if(StringUtils.isBlank(discovererId)) {
			throw new IllegalArgumentException("Discoverer ID must not be blank");
		}

		if(StringUtils.isBlank(discoveryMethod)) {
			throw new IllegalArgumentException("Discovery method must not be blank");
		}

		this.discoveredTime = TimestampHelper.toUTC(discoveredTime);
		this.discovererId = discovererId;
		this.discoveryMethod = discoveryMethod;

		this.manufacturer = null;
		this.model = null;
		this.os = null;
		this.osVersion = null;
		this.deviceType = null;
	}


	/**
	 * Create a new device-characteristic metadata.
	 * This constructor should be used for metadata that is sent from the CLIENT to the SERVER. It does not include
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamps are stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * Magnitude must be given as a number with range from 0 to 100 with higher numbers meaning
	 * higher magnitude.
	 *
	 * @param discoveredTime Time the charcteristic was discovered
	 * @param discovererId ID of the client that discovered the charcteristic
	 * @param discoveryMethod Used method of charcteristic discovery
	 */
	public CBORDeviceCharacteristic(ZonedDateTime discoveredTime, String discovererId, String discoveryMethod) {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME);

		if(discoveredTime == null) {
			throw new IllegalArgumentException("Discovered time must not be null");
		}

		if(StringUtils.isBlank(discovererId)) {
			throw new IllegalArgumentException("Discoverer ID must not be blank");
		}

		if(StringUtils.isBlank(discoveryMethod)) {
			throw new IllegalArgumentException("Discovery method must not be blank");
		}

		this.discoveredTime = TimestampHelper.toUTC(discoveredTime);
		this.discovererId = discovererId;
		this.discoveryMethod = discoveryMethod;

		this.manufacturer = null;
		this.model = null;
		this.os = null;
		this.osVersion = null;
		this.deviceType = null;
	}


	/**
	 * Set the manufacturer for this metadata.
	 * The manufacturer may be null to remove this value from the metadata. If not null,
	 * the manufacturer MUST NOT be an empty string or whitespace only.
	 *
	 * @param manufacturer Manufacturer name
	 */
	public void setManufacturer(String manufacturer) {
		if(StringUtils.isWhitespace(manufacturer)) {
			throw new IllegalArgumentException("Manufacturer must not be empty or whitespace only");
		}

		this.manufacturer = manufacturer;
	}


	/**
	 * Set the model for this metadata.
	 * The model may be null to remove this value from the metadata. If not null,
	 * the model MUST NOT be an empty string or whitespace only.
	 *
	 * @param model Model name
	 */
	public void setModel(String model) {
		if(StringUtils.isWhitespace(model)) {
			throw new IllegalArgumentException("Model must not be empty or whitespace only");
		}

		this.model = model;
	}


	/**
	 * Set the operating system for this metadata.
	 * The operating system may be null to remove this value from the metadata. If not null,
	 * the operating system MUST NOT be an empty string or whitespace only.
	 *
	 * @param os Operating system name
	 */
	public void setOs(String os) {
		if(StringUtils.isWhitespace(os)) {
			throw new IllegalArgumentException("OS must not be empty or whitespace only");
		}

		this.os = os;
	}


	/**
	 * Set the operating system version for this metadata.
	 * The operating system version may be null to remove this value from the metadata. If not null,
	 * the operating system version MUST NOT be an empty string or whitespace only.
	 *
	 * @param osVersion Operating system version
	 */
	public void setOsVersion(String osVersion) {
		if(StringUtils.isWhitespace(osVersion)) {
			throw new IllegalArgumentException("OS version must not be empty or whitespace only");
		}

		this.osVersion = osVersion;
	}


	/**
	 * Set the device type for this metadata.
	 * The device type may be null to remove this value from the metadata. If not null,
	 * the device type MUST NOT be an empty string or whitespace only.
	 *
	 * @param deviceType Device type
	 */
	public void setDeviceType(String deviceType) {
		if(StringUtils.isWhitespace(deviceType)) {
			throw new IllegalArgumentException("Device type must not be empty or whitespace only");
		}

		this.deviceType = deviceType;
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
				DataItem cborName = this.getNestedElementNameMapping(CBORDeviceCharacteristic.DISCOVERED_TIME, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(TimestampHelper.toEpochTimeDataItem(this.discoveredTime));
			}


			// Serialize 'discoverer-id'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBORDeviceCharacteristic.DISCOVERER_ID, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.discovererId));
			}


			// Serialize 'discovery-method'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBORDeviceCharacteristic.DISCOVERY_METHOD, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.discoveryMethod));
			}


			// Serialize 'manufacturer'
			if(this.manufacturer != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORDeviceCharacteristic.MANUFACTURER, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.manufacturer));
			}


			// Serialize 'model'
			if(this.model != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORDeviceCharacteristic.MODEL, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);;
				builder.addArray();
				builder.add(new UnicodeString(this.model));
			}


			// Serialize 'os'
			if(this.os != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORDeviceCharacteristic.OS, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.os));
			}


			// Serialize 'os-version'
			if(this.osVersion != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORDeviceCharacteristic.OS_VERSION, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.osVersion));
			}


			// Serialize 'device-type'
			if(this.deviceType != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORDeviceCharacteristic.DEVICE_TYPE, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.deviceType));
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
