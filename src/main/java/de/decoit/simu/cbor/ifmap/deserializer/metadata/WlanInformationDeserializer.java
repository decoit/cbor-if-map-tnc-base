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
package de.decoit.simu.cbor.ifmap.deserializer.metadata;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import de.decoit.simu.cbor.ifmap.deserializer.MetadataDeserializerManager;
import de.decoit.simu.cbor.ifmap.deserializer.vendor.VendorMetadataDeserializer;
import de.decoit.simu.cbor.ifmap.enums.IfMapWlanSecurityType;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORWlanInformation;
import de.decoit.simu.cbor.ifmap.util.DictionaryHelper;
import de.decoit.simu.cbor.ifmap.util.TimestampHelper;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * The singleton instance of this class may be used to deserialize metadata of type {@link CBORWlanInformation}.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class WlanInformationDeserializer implements VendorMetadataDeserializer<CBORWlanInformation> {
	private static WlanInformationDeserializer instance;


	/**
	 * Get the singleton instance of this deserializer.
	 *
	 * @return Deserializer instance
	 */
	public static WlanInformationDeserializer getInstance() {
		if(instance == null) {
			instance = new WlanInformationDeserializer();
		}

		return instance;
	}


	/**
	 * Private constructor, this is a singleton class.
	 */
	private WlanInformationDeserializer() {}
	
	
	@Override
	public CBORWlanInformation deserialize(final Array attributes, 
										   final DataItem nestedDataItem,
										   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		if(log.isDebugEnabled()) {
			log.debug("Attributes array: " + attributes);
			log.debug("Nested data item: " + nestedDataItem);
			log.debug("Dictionary entry: " + elementDictEntry);
		}
		
		if(nestedDataItem.getMajorType() != MajorType.ARRAY) {
			throw new CBORDeserializationException("Expected nested Array, found " + nestedDataItem.getMajorType());
		}
		Array nestedTags = (Array) nestedDataItem;
		
		// Initially define the required variables to build the target object
		String publisherId = null;
		ZonedDateTime timestamp = null;
		String ssid = null;
		WlanSecurityTypeContainer groupSecurity = null;
		DataItem timestampDi = null;
		DataItem timestampFractionDi = null;
		
		// These lists will hold data items for different security types
		List<DataItem> groupSecurityDataItems = new ArrayList<>();
		List<DataItem> managementSecurityDataItems = new ArrayList<>();
		List<DataItem> unicastSecurityDataItems = new ArrayList<>();

		// Get list of all attribute data items
		List<DataItem> attributesDataItems = attributes.getDataItems();

		// Iterate over the data items in steps of 2
		for(int i=0; i<attributesDataItems.size(); i=i+2) {
			// Get name and value data items
			DataItem attrName = attributesDataItems.get(i);
			DataItem attrValue = attributesDataItems.get(i+1);

			String attrNameStr = MetadataDeserializerManager.getAttributeXmlName(attrName, elementDictEntry);

			// Process the attribute value
			switch(attrNameStr) {
				case CBORWlanInformation.IFMAP_PUBLISHER_ID:
					publisherId = MetadataDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
				case CBORWlanInformation.IFMAP_TIMESTAMP:
					timestampDi = attrValue;
					break;
				case CBORWlanInformation.IFMAP_TIMESTAMP_FRACTION:
					timestampFractionDi = attrValue;
			}
		}
		
		if(timestampDi != null) {
			timestamp = TimestampHelper.fromEpochTimeDataItem(timestampDi, timestampFractionDi);
		}
		
		// Get list of all nested tags data items
		List<DataItem> nestedTagsDataItems = nestedTags.getDataItems();

		// Iterate over the data items in steps of 4
		for(int i=0; i<nestedTagsDataItems.size(); i=i+4) {
			// Get namespace, name and nested tag/value data items (index i and i+1)
			// Attributes array is required for wlan-security type elements (i+2)
			// No further nested elements are expected, only a value should be present (index i+3)
			DataItem ntNamespace = nestedTagsDataItems.get(i);
			DataItem ntName = nestedTagsDataItems.get(i+1);
			DataItem ntAttributes = nestedTagsDataItems.get(i+2);
			DataItem ntNestedValue = nestedTagsDataItems.get(i+3);

			// The namespace should be of simple type NULL, no namespace is expected to be found here
			if(!MetadataDeserializerManager.isSimpleValueNull(ntNamespace)) {
				throw new CBORDeserializationException("Unexpected nested element with namespace found inside 'wlan-information' element");
			}

			String nestedTagName = MetadataDeserializerManager.getNestedTagXmlName(ntName, elementDictEntry);

			// Process the nested element value
			switch(nestedTagName) {
				case CBORWlanInformation.SSID:
					ssid = MetadataDeserializerManager.processUnicodeStringItem(ntNestedValue, true);
					break;
				case CBORWlanInformation.SSID_GROUP_SECURITY:
					groupSecurityDataItems.add(ntNamespace);
					groupSecurityDataItems.add(ntName);
					groupSecurityDataItems.add(ntAttributes);
					groupSecurityDataItems.add(ntNestedValue);
					break;
				case CBORWlanInformation.SSID_MANAGEMENT_SECURITY:
					managementSecurityDataItems.add(ntNamespace);
					managementSecurityDataItems.add(ntName);
					managementSecurityDataItems.add(ntAttributes);
					managementSecurityDataItems.add(ntNestedValue);
					break;
				case CBORWlanInformation.SSID_UNICAST_SECURITY:
					unicastSecurityDataItems.add(ntNamespace);
					unicastSecurityDataItems.add(ntName);
					unicastSecurityDataItems.add(ntAttributes);
					unicastSecurityDataItems.add(ntNestedValue);
					break;
			}
		}

		// Process 'ssid-group-security'
		{
			DataItem ntNamespace = groupSecurityDataItems.get(0);
			Array ntAttributes = (Array) groupSecurityDataItems.get(2);
			DataItem ntValue = groupSecurityDataItems.get(3);

			// The namespace should be of simple type NULL, no namespace is expected to be found here
			if(!MetadataDeserializerManager.isSimpleValueNull(ntNamespace)) {
				throw new CBORDeserializationException("Unexpected nested element with namespace found inside 'wlan-information' element");
			}
			
			try {
				DictionarySimpleElement nestedTagEntry = DictionaryHelper.findNestedElement(CBORWlanInformation.SSID_GROUP_SECURITY, elementDictEntry);

				groupSecurity = processWlanSecurityType(ntAttributes, ntValue, nestedTagEntry);
			}
			catch(DictionaryPathException ex) {
				throw new CBORDeserializationException("Deserialization failed, see nested exception for details", ex);
			}
		}

		
		// Build return value object
		CBORWlanInformation rv;
		if(publisherId != null && timestamp != null) {
			rv = new CBORWlanInformation(publisherId, timestamp, groupSecurity.getWlanSecurityType(), groupSecurity.getOtherTypeDefinition());
		}
		else {
			rv = new CBORWlanInformation(groupSecurity.getWlanSecurityType(), groupSecurity.getOtherTypeDefinition());
		}
		rv.setSsid(ssid);
		
		
		// Process 'ssid-management-security' in steps of 4
		for(int i=0; i<managementSecurityDataItems.size(); i=i+4) {
			DataItem ntNamespace = managementSecurityDataItems.get(i+0);
			Array ntAttributes = (Array) managementSecurityDataItems.get(i+2);
			DataItem ntValue = managementSecurityDataItems.get(i+3);

			// The namespace should be of simple type NULL, no namespace is expected to be found here
			if(!MetadataDeserializerManager.isSimpleValueNull(ntNamespace)) {
				throw new CBORDeserializationException("Unexpected nested element with namespace found inside 'wlan-information' element");
			}
			
			try {
				DictionarySimpleElement nestedTagEntry = DictionaryHelper.findNestedElement(CBORWlanInformation.SSID_MANAGEMENT_SECURITY, elementDictEntry);

				WlanSecurityTypeContainer secType = processWlanSecurityType(ntAttributes, ntValue, nestedTagEntry);
				rv.addSsidManagementSecurity(secType.getWlanSecurityType(), secType.getOtherTypeDefinition());
			}
			catch(DictionaryPathException ex) {
				throw new CBORDeserializationException("Deserialization failed, see nested exception for details", ex);
			}
		}

		// Process 'ssid-unicast-security' in steps of 4
		for(int i=0; i<unicastSecurityDataItems.size(); i=i+4) {
			DataItem ntNamespace = unicastSecurityDataItems.get(i+0);
			Array ntAttributes = (Array) unicastSecurityDataItems.get(i+2);
			DataItem ntValue = unicastSecurityDataItems.get(i+3);

			// The namespace should be of simple type NULL, no namespace is expected to be found here
			if(!MetadataDeserializerManager.isSimpleValueNull(ntNamespace)) {
				throw new CBORDeserializationException("Unexpected nested element with namespace found inside 'wlan-information' element");
			}
			
			try {
				DictionarySimpleElement nestedTagEntry = DictionaryHelper.findNestedElement(CBORWlanInformation.SSID_UNICAST_SECURITY, elementDictEntry);

				WlanSecurityTypeContainer secType = processWlanSecurityType(ntAttributes, ntValue, nestedTagEntry);
				rv.addSsidUnicastSecurity(secType.getWlanSecurityType(), secType.getOtherTypeDefinition());
			}
			catch(DictionaryPathException ex) {
				throw new CBORDeserializationException("Deserialization failed, see nested exception for details", ex);
			}
		}
		
		return rv;
	}
	
	
	/**
	 * Process data items defining a WLAN security type into an internal container object.
	 * 
	 * @param attributes CBOR array of attribute data items
	 * @param value CBOR data item of the element's value
	 * @param elementEntry Dictionary entry of the element
	 * @return Internal container object containing the processed data
	 * @throws CBORDeserializationException if processing the data fails
	 */
	private WlanSecurityTypeContainer processWlanSecurityType(final Array attributes, final DataItem value,
															  final DictionarySimpleElement elementEntry) throws CBORDeserializationException {
		WlanSecurityTypeContainer rv = new WlanSecurityTypeContainer();
		
		rv.setWlanSecurityType(IfMapWlanSecurityType.fromXmlName(MetadataDeserializerManager.getElementEnumValueXmlName(value, elementEntry)));
		
		// Get list of all attribute data items
		List<DataItem> attributesDataItems = attributes.getDataItems();
		
		for(int j=0; j<attributesDataItems.size(); j=j+2) {
			DataItem attrName = attributesDataItems.get(j);
			DataItem attrValue = attributesDataItems.get(j+1);

			String attrNameStr = MetadataDeserializerManager.getAttributeXmlName(attrName, elementEntry);

			switch(attrNameStr) {
				case CBORWlanInformation.WlanSecurityType.OTHER_TYPE_DEFINITION:
					rv.setOtherTypeDefinition(MetadataDeserializerManager.processUnicodeStringItem(attrValue, true));
					break;
			}
		}
		
		return rv;
	}
	
	
	/**
	 * Internal container class for processed WLAN security type elements.
	 */
	@Getter
	@Setter
	@EqualsAndHashCode
	@ToString
	private class WlanSecurityTypeContainer {
		private IfMapWlanSecurityType wlanSecurityType = null;
		private String otherTypeDefinition = null;
	}
}
