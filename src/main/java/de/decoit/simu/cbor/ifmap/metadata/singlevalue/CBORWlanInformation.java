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
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.UnicodeString;
import de.decoit.simu.cbor.ifmap.enums.IfMapWlanSecurityType;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.util.DictionaryHelper;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of the IF-MAP wlan-information metadata.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORWlanInformation extends AbstractSingleValueMetadata {
	public static final String XML_NAME = "wlan-information";
	public static final String SSID = "ssid";
	public static final String SSID_UNICAST_SECURITY = "ssid-unicast-security";
	public static final String SSID_GROUP_SECURITY = "ssid-group-security";
	public static final String SSID_MANAGEMENT_SECURITY = "ssid-management-security";

	@Getter
	private String ssid;
	@Getter
	private WlanSecurityType ssidGroupSecurity;
	private final Set<WlanSecurityType> ssidUnicastSecurity;
	private final Set<WlanSecurityType> ssidManagementSecurity;


	/**
	 * Create a new wlan-security metadata with the specified publisher ID and timestamp.
	 * This constructor should be used for metadata that is sent from the SERVER to the CLIENT. It includes
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamp is stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * Attributes for ssid-management-security and ssid-unicast-security are initialized with empty
	 * sets. Each of these sets MUST be filled with at least one valid item, otherwise the
	 * serialization process will not be successful.
	 *
	 * @param ifMapPublisherId IF-MAP client publisher ID
	 * @param ifMapTimestamp Timestamp for the metadata
	 * @param ssidGroupSecurityType
	 */
	public CBORWlanInformation(String ifMapPublisherId, ZonedDateTime ifMapTimestamp, IfMapWlanSecurityType ssidGroupSecurityType) {
		this(ifMapPublisherId, ifMapTimestamp, ssidGroupSecurityType, null);
	}


	/**
	 * Create a new wlan-security metadata with the specified publisher ID and timestamp.
	 * This constructor should be used for metadata that is sent from the SERVER to the CLIENT. It includes
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamp is stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * Attributes for ssid-management-security and ssid-unicast-security are initialized with empty
	 * sets. Each of these sets MUST be filled with at least one valid item, otherwise the
	 * serialization process will not be successful.
	 *
	 * @param ifMapPublisherId IF-MAP client publisher ID
	 * @param ifMapTimestamp Timestamp for the metadata
	 * @param ssidGroupSecurityType
	 * @param ssidGroupSecurityOtherTypeDefinition
	 */
	public CBORWlanInformation(String ifMapPublisherId, ZonedDateTime ifMapTimestamp, IfMapWlanSecurityType ssidGroupSecurityType, String ssidGroupSecurityOtherTypeDefinition) {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME, ifMapPublisherId, ifMapTimestamp);

		this.ssid = null;
		this.ssidGroupSecurity = new WlanSecurityType(ssidGroupSecurityType, ssidGroupSecurityOtherTypeDefinition);
		this.ssidUnicastSecurity = new HashSet<>();
		this.ssidManagementSecurity = new HashSet<>();
	}


	/**
	 * Create a new wlan-security metadata with the specified publisher ID and timestamp.
	 * This constructor should be used for metadata that is sent from the CLIENT to the SERVER. It does not include
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamp is stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * Attributes for ssid-management-security and ssid-unicast-security are initialized with empty
	 * sets. Each of these sets MUST be filled with at least one valid item, otherwise the
	 * serialization process will not be successful.
	 *
	 * @param ssidGroupSecurityType
	 */
	public CBORWlanInformation(IfMapWlanSecurityType ssidGroupSecurityType) {
		this(ssidGroupSecurityType, null);
	}


	/**
	 * Create a new wlan-security metadata with the specified publisher ID and timestamp.
	 * This constructor should be used for metadata that is sent from the CLIENT to the SERVER. It does not include
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamp is stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * Attributes for ssid-management-security and ssid-unicast-security are initialized with empty
	 * sets. Each of these sets MUST be filled with at least one valid item, otherwise the
	 * serialization process will not be successful.
	 *
	 * @param ssidGroupSecurityType
	 * @param ssidGroupSecurityOtherTypeDefinition
	 */
	public CBORWlanInformation(IfMapWlanSecurityType ssidGroupSecurityType, String ssidGroupSecurityOtherTypeDefinition) {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME);

		this.ssid = null;
		this.ssidGroupSecurity = new WlanSecurityType(ssidGroupSecurityType, ssidGroupSecurityOtherTypeDefinition);
		this.ssidUnicastSecurity = new HashSet<>();
		this.ssidManagementSecurity = new HashSet<>();
	}


	/**
	 * Set the SSID for this metadata.
	 * The SSID may be null to remove this value from the metadata. If not null,
	 * the SSID MUST NOT be an empty string or whitespace only.
	 *
	 * @param ssid SSID for this metadata
	 */
	public void setSsid(String ssid) {
		if(StringUtils.isWhitespace(ssid)) {
			throw new IllegalArgumentException("SSID must not be empty or whitespace only");
		}

		this.ssid = ssid;
	}


	/**
	 * Returns an immutable view of the ssid-unicast-securtity set.
	 *
	 * @return Immutable set view
	 */
	public Set<WlanSecurityType> getSsidUnicastSecurity() {
		return Collections.unmodifiableSet(this.ssidUnicastSecurity);
	}


	/**
	 * Add a new SSID unicast security type to this metadata.
	 * The other-type-definition parameter is required to be not blank if type is IfMapWlanSecurityType.OTHER.
	 * For all other types the other-type-definition value will be discarded and replaced by null.
	 *
	 * @param wlanSecurityType Security type
	 * @param otherTypeDefinition Other type definition if applicable, may be null
	 */
	public void addSsidUnicastSecurity(IfMapWlanSecurityType wlanSecurityType, String otherTypeDefinition) {
		WlanSecurityType secType = new WlanSecurityType(wlanSecurityType, otherTypeDefinition);

		this.ssidUnicastSecurity.add(secType);
	}


	/**
	 * Remove an existing SSID unicast security type from this metadata.
	 * The other-type-definition parameter is required to be not blank if type is IfMapWlanSecurityType.OTHER.
	 * For all other types the other-type-definition value will be discarded and replaced by null.
	 *
	 * @param wlanSecurityType Security type
	 * @param otherTypeDefinition Other type definition if applicable, may be null
	 */
	public void removeSsidUnicastSecurity(IfMapWlanSecurityType wlanSecurityType, String otherTypeDefinition) {
		WlanSecurityType secType = new WlanSecurityType(wlanSecurityType, otherTypeDefinition);

		this.ssidUnicastSecurity.remove(secType);
	}


	public boolean hasSsidUnicastSecurity(IfMapWlanSecurityType wlanSecurityType, String otherTypeDefinition) {
		WlanSecurityType secType = new WlanSecurityType(wlanSecurityType, otherTypeDefinition);

		return this.ssidUnicastSecurity.contains(secType);
	}


	/**
	 * Returns an immutable view of the ssid-management-securtity set.
	 *
	 * @return Immutable set view
	 */
	public Set<WlanSecurityType> getSsidManagementSecurity() {
		return Collections.unmodifiableSet(this.ssidManagementSecurity);
	}


	/**
	 * Add a new SSID management security type to this metadata.
	 * The other-type-definition parameter is required to be not blank if type is IfMapWlanSecurityType.OTHER.
	 * For all other types the other-type-definition value will be discarded and replaced by null.
	 *
	 * @param wlanSecurityType Security type
	 * @param otherTypeDefinition Other type definition if applicable, may be null
	 */
	public void addSsidManagementSecurity(IfMapWlanSecurityType wlanSecurityType, String otherTypeDefinition) {
		WlanSecurityType secType = new WlanSecurityType(wlanSecurityType, otherTypeDefinition);

		this.ssidManagementSecurity.add(secType);
	}


	/**
	 * Remove an existing SSID management security type from this metadata.
	 * The other-type-definition parameter is required to be not blank if type is IfMapWlanSecurityType.OTHER.
	 * For all other types the other-type-definition value will be discarded and replaced by null.
	 *
	 * @param wlanSecurityType Security type
	 * @param otherTypeDefinition Other type definition if applicable, may be null
	 */
	public void removeSsidManagementSecurity(IfMapWlanSecurityType wlanSecurityType, String otherTypeDefinition) {
		WlanSecurityType secType = new WlanSecurityType(wlanSecurityType, otherTypeDefinition);

		this.ssidManagementSecurity.remove(secType);
	}


	public boolean hasSsidManagementSecurity(IfMapWlanSecurityType wlanSecurityType, String otherTypeDefinition) {
		WlanSecurityType secType = new WlanSecurityType(wlanSecurityType, otherTypeDefinition);

		return this.ssidManagementSecurity.contains(secType);
	}


	@Override
	protected void serializeNestedElements(final ArrayBuilder<?> builder) throws CBORSerializationException {
		if(this.ssidUnicastSecurity.isEmpty()) {
			throw new CBORSerializationException("SSID unicast security set must not be empty");
		}

		if(this.ssidManagementSecurity.isEmpty()) {
			throw new CBORSerializationException("SSID management security set must not be empty");
		}

		try {
			// Build dictionary path to this element
			StringBuilder dictPathSb = new StringBuilder("<");
			dictPathSb.append(this.namespace);
			dictPathSb.append(">");
			dictPathSb.append(this.elementName);

			// Get dictionary entry for this element
			DictionarySimpleElement elementEntry = DictionaryProvider.getInstance().findElementByPath(dictPathSb.toString());

			// Serialize 'ssid'
			if(this.ssid != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORWlanInformation.SSID, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.ssid));
			}


			// Serialize 'ssid-unicast-security'
			{
				for(WlanSecurityType wst : this.ssidUnicastSecurity) {
					wst.cborSerialize(builder, CBORWlanInformation.SSID_UNICAST_SECURITY, elementEntry);
				}
			}


			// Serialize 'ssid-group-security'
			{
				this.ssidGroupSecurity.cborSerialize(builder, CBORWlanInformation.SSID_GROUP_SECURITY, elementEntry);
			}


			// Serialize 'ssid-management-security'
			{
				for(WlanSecurityType wst : this.ssidManagementSecurity) {
					wst.cborSerialize(builder, CBORWlanInformation.SSID_MANAGEMENT_SECURITY, elementEntry);
				}
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}


	@ToString
	public class WlanSecurityType {
		public static final String OTHER_TYPE_DEFINITION = "other-type-definition";

		@Getter
		private final IfMapWlanSecurityType wlanSecurityType;
		@Getter
		private final String otherTypeDefinition;


		/**
		 * Create a new WLAN security object with the specified type value.
		 * The other-type-definition parameter is required to be not blank if type is IfMapWlanSecurityType.OTHER.
		 * For all other types the other-type-definition value will be discarded and replaced by null.
		 *
		 * @param wlanSecurityType Security type
		 * @param otherTypeDefinition Other type definition if applicable, may be null
		 */
		public WlanSecurityType(IfMapWlanSecurityType wlanSecurityType, String otherTypeDefinition) {
			if(wlanSecurityType == null) {
				throw new IllegalArgumentException("wlanSecurityType parameter must not be null");
			}

			if(wlanSecurityType == IfMapWlanSecurityType.OTHER && StringUtils.isBlank(otherTypeDefinition)) {
				throw new IllegalArgumentException("other-type-definition must not be blank if type is OTHER");
			}
			// No other type definition is required if type is different from OTHER
			else if(wlanSecurityType != IfMapWlanSecurityType.OTHER) {
				otherTypeDefinition = null;
			}

			this.wlanSecurityType = wlanSecurityType;
			this.otherTypeDefinition = otherTypeDefinition;
		}


		private void cborSerialize(final ArrayBuilder<?> builder, final String elementName, final DictionarySimpleElement parentElementEntry) throws CBORSerializationException {
			try {
				DataItem cborName = getNestedElementNameMapping(elementName, parentElementEntry);
				DataItem cborValue = getNestedElementEnumValueMapping(elementName, this.wlanSecurityType.getXmlName(), parentElementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);

				DictionarySimpleElement elementEntry = DictionaryHelper.findNestedElement(elementName, parentElementEntry);

				ArrayBuilder<?> attrBuilder = builder.addArray();
				serializeAttributes(attrBuilder, elementEntry);

				builder.add(cborValue);
			}
			catch(DictionaryPathException | RuntimeException ex) {
				throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
			}
		}


		private void serializeAttributes(final ArrayBuilder<?> builder, DictionarySimpleElement elementEntry) {
			if(this.otherTypeDefinition != null) {
				DataItem cborName = getAttributeNameMapping(WlanSecurityType.OTHER_TYPE_DEFINITION, elementEntry);

				builder.add(cborName);
				builder.add(new UnicodeString(this.otherTypeDefinition));
			}
		}


		// hashCode() cannot be generated by Lombok because we need to use the ordinal of the IfMapWlanSecurityType enum for calculation
		// Not using the ordinal makes order of the elements in the HashSet<> not deterministic and thus tests would fail some times due to
		// different sorting of the elements in CBOR output.
		@Override
		public int hashCode() {
			int hash = 7;
			hash = 53 * hash + this.wlanSecurityType.ordinal();
			hash = 53 * hash + Objects.hashCode(this.otherTypeDefinition);
			return hash;
		}


		// Since we have to write our own hashCode() the same is true for equals()
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final WlanSecurityType other = (WlanSecurityType) obj;
			if (this.wlanSecurityType != other.wlanSecurityType) {
				return false;
			}
			if (!Objects.equals(this.otherTypeDefinition, other.otherTypeDefinition)) {
				return false;
			}
			return true;
		}
	}
}
