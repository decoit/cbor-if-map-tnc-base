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
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORLocation;
import de.decoit.simu.cbor.ifmap.util.DictionaryHelper;
import de.decoit.simu.cbor.ifmap.util.TimestampHelper;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * The singleton instance of this class may be used to deserialize metadata of type {@link CBORLocation}.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class LocationDeserializer implements VendorMetadataDeserializer<CBORLocation> {
	private static LocationDeserializer instance;


	/**
	 * Get the singleton instance of this deserializer.
	 *
	 * @return Deserializer instance
	 */
	public static LocationDeserializer getInstance() {
		if(instance == null) {
			instance = new LocationDeserializer();
		}

		return instance;
	}


	/**
	 * Private constructor, this is a singleton class.
	 */
	private LocationDeserializer() {}
	
	
	@Override
	public CBORLocation deserialize(final Array attributes, 
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
		ZonedDateTime discoveredTime = null;
		String discovererId = null;
		DataItem timestampDi = null;
		DataItem timestampFractionDi = null;

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
				case CBORLocation.IFMAP_PUBLISHER_ID:
					publisherId = MetadataDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
				case CBORLocation.IFMAP_TIMESTAMP:
					timestampDi = attrValue;
					break;
				case CBORLocation.IFMAP_TIMESTAMP_FRACTION:
					timestampFractionDi = attrValue;
			}
		}
		
		// Get list of all nested tags data items
		List<DataItem> nestedTagsDataItems = nestedTags.getDataItems();
		List<DataItem> locationInformationDataItems = new ArrayList<>();

		// Iterate over the data items in steps of 4
		for(int i=0; i<nestedTagsDataItems.size(); i=i+4) {
			// Get namespace, name and nested tag/value data items (index i and i+1)
			// No further nested elements are expected, only a value should be present (index i+3)
			DataItem ntNamespace = nestedTagsDataItems.get(i);
			DataItem ntName = nestedTagsDataItems.get(i+1);
			DataItem ntAttributes = nestedTagsDataItems.get(i+2);
			DataItem ntNestedValue = nestedTagsDataItems.get(i+3);

			// The namespace should be of simple type NULL, no namespace is expected to be found here
			if(!MetadataDeserializerManager.isSimpleValueNull(ntNamespace)) {
				throw new CBORDeserializationException("Unexpected nested element with namespace found inside 'location' element");
			}

			String nestedTagName = MetadataDeserializerManager.getNestedTagXmlName(ntName, elementDictEntry);

			// Process the nested element value
			switch(nestedTagName) {
				case CBORLocation.DISCOVERED_TIME:
					discoveredTime = TimestampHelper.fromEpochTimeDataItem(ntNestedValue, null);
					break;
				case CBORLocation.DISCOVERER_ID:
					discovererId = MetadataDeserializerManager.processUnicodeStringItem(ntNestedValue, true);
					break;
				case CBORLocation.LOCATION_INFORMATION:
					locationInformationDataItems.add(ntAttributes);
			}
		}

		if(timestampDi != null) {
			timestamp = TimestampHelper.fromEpochTimeDataItem(timestampDi, timestampFractionDi);
		}

		// Build return value object
		CBORLocation rv;
		if(publisherId != null && timestamp != null) {
			rv = new CBORLocation(publisherId, timestamp, discoveredTime, discovererId);
		}
		else {
			rv = new CBORLocation(discoveredTime, discovererId);
		}
		
		// Iterate over the location information data items
		for(DataItem di : locationInformationDataItems) {
			Array arrayDi = (Array) di;

			if(log.isDebugEnabled()) {
				log.debug("Array data item: " + arrayDi.toString());
			}

			String locationType = null;
			String locationValue = null;

			// Iterate over the attribute data item list in steps of 2
			for(int j=0; j<arrayDi.getDataItems().size(); j=j+2) {
				DataItem attrName = arrayDi.getDataItems().get(j);
				DataItem attrValue = arrayDi.getDataItems().get(j+1);

				if(log.isDebugEnabled()) {
					log.debug("Attribute name data item: " + attrName.toString());
					log.debug("Attribute value data item: " + attrValue.toString());
				}
				
				try {
					DictionarySimpleElement ntEntry = DictionaryHelper.findNestedElement(CBORLocation.LOCATION_INFORMATION, elementDictEntry);
					String attrNameStr = MetadataDeserializerManager.getAttributeXmlName(attrName, ntEntry);

					switch(attrNameStr) {
						case CBORLocation.LocationInformation.TYPE:
							locationType = MetadataDeserializerManager.processUnicodeStringItem(attrValue, true);
							break;
						case CBORLocation.LocationInformation.VALUE:
							locationValue = MetadataDeserializerManager.processUnicodeStringItem(attrValue, true);
							break;
					}
				}
				catch(DictionaryPathException ex) {
					throw new CBORDeserializationException("Deserialization failed, see nested exception for details", ex);
				}
			}

			rv.addLocationInformation(locationType, locationValue);
		}

		return rv;
	}
}
