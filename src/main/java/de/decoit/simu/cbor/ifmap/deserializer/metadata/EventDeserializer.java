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
import de.decoit.simu.cbor.ifmap.enums.IfMapEventType;
import de.decoit.simu.cbor.ifmap.enums.IfMapSignificance;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBOREvent;
import de.decoit.simu.cbor.ifmap.util.TimestampHelper;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * The singleton instance of this class may be used to deserialize metadata of type {@link CBOREvent}.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class EventDeserializer implements VendorMetadataDeserializer<CBOREvent> {
	private static EventDeserializer instance;


	/**
	 * Get the singleton instance of this deserializer.
	 *
	 * @return Deserializer instance
	 */
	public static EventDeserializer getInstance() {
		if(instance == null) {
			instance = new EventDeserializer();
		}

		return instance;
	}


	/**
	 * Private constructor, this is a singleton class.
	 */
	private EventDeserializer() {}
	
	
	@Override
	public CBOREvent deserialize(final Array attributes, 
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
		String name = null;
		ZonedDateTime discoveredTime = null;
		String discovererId = null;
		Integer magnitude = null;
		Integer confidence = null;
		IfMapSignificance significance = null;
		IfMapEventType type = null;
		String otherTypeDefinition = null;
		String information = null;
		String vulnerabilityUri = null;
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
				case CBOREvent.IFMAP_PUBLISHER_ID:
					publisherId = MetadataDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
				case CBOREvent.IFMAP_TIMESTAMP:
					timestampDi = attrValue;
					break;
				case CBOREvent.IFMAP_TIMESTAMP_FRACTION:
					timestampFractionDi = attrValue;
			}
		}
		
		// Get list of all nested tags data items
		List<DataItem> nestedTagsDataItems = nestedTags.getDataItems();

		// Iterate over the data items in steps of 4
		for(int i=0; i<nestedTagsDataItems.size(); i=i+4) {
			// Get namespace, name and nested tag/value data items (index i and i+1)
			// The attributes array is ignore because a IF-MAP 'event' has no attributes on nested tags
			// No further nested elements are expected, only a value should be present (index i+3)
			DataItem ntNamespace = nestedTagsDataItems.get(i);
			DataItem ntName = nestedTagsDataItems.get(i+1);
			DataItem ntNestedValue = nestedTagsDataItems.get(i+3);

			// The namespace should be of simple type NULL, no namespace is expected to be found here
			if(!MetadataDeserializerManager.isSimpleValueNull(ntNamespace)) {
				throw new CBORDeserializationException("Unexpected nested element with namespace found inside 'event' element");
			}

			String nestedTagName = MetadataDeserializerManager.getNestedTagXmlName(ntName, elementDictEntry);

			// Process the nested element value
			switch(nestedTagName) {
				case CBOREvent.NAME:
					name = MetadataDeserializerManager.processUnicodeStringItem(ntNestedValue, true);
					break;
				case CBOREvent.MAGNITUDE:
					magnitude = MetadataDeserializerManager.processUnsignedIntegerItem(ntNestedValue, true).intValueExact();
					break;
				case CBOREvent.CONFIDENCE:
					confidence = MetadataDeserializerManager.processUnsignedIntegerItem(ntNestedValue, true).intValueExact();
					break;
				case CBOREvent.SIGNIFICANCE:
					significance = IfMapSignificance.fromXmlName(MetadataDeserializerManager.getNestedTagEnumValueXmlName(ntName, ntNestedValue, elementDictEntry));
					break;
				case CBOREvent.DISCOVERED_TIME:
					discoveredTime = TimestampHelper.fromEpochTimeDataItem(ntNestedValue, null);
					break;
				case CBOREvent.DISCOVERER_ID:
					discovererId = MetadataDeserializerManager.processUnicodeStringItem(ntNestedValue, true);
					break;
				case CBOREvent.TYPE:
					type = IfMapEventType.fromXmlName(MetadataDeserializerManager.getNestedTagEnumValueXmlName(ntName, ntNestedValue, elementDictEntry));
					break;
				case CBOREvent.OTHER_TYPE_DEFINITION:
					otherTypeDefinition = MetadataDeserializerManager.processUnicodeStringItem(ntNestedValue, true);
					break;
				case CBOREvent.INFORMATION:
					information = MetadataDeserializerManager.processUnicodeStringItem(ntNestedValue, true);
					break;
				case CBOREvent.VULNERABILITY_URI:
					vulnerabilityUri = MetadataDeserializerManager.processUnicodeStringItem(ntNestedValue, true);
					break;
			}
		}

		if(timestampDi != null) {
			timestamp = TimestampHelper.fromEpochTimeDataItem(timestampDi, timestampFractionDi);
		}

		// Build return value object
		CBOREvent rv;
		if(publisherId != null && timestamp != null) {
			rv = new CBOREvent(publisherId, timestamp, name, discoveredTime, discovererId, magnitude, confidence, significance);
		}
		else {
			rv = new CBOREvent(name, discoveredTime, discovererId, magnitude, confidence, significance);
		}
		rv.setType(type, otherTypeDefinition);
		rv.setInformation(information);
		rv.setVulnerabilityUri(vulnerabilityUri);

		return rv;
	}
}
