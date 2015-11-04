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
package de.decoit.simu.cbor.ifmap.deserializer.request;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import de.decoit.simu.cbor.ifmap.attributegroup.SessionAttributeGroup;
import de.decoit.simu.cbor.ifmap.attributegroup.ValidationAttributeGroup;
import de.decoit.simu.cbor.ifmap.deserializer.ExtendedIdentifierDeserializerManager;
import de.decoit.simu.cbor.ifmap.deserializer.IdentifierDeserializerManager;
import de.decoit.simu.cbor.ifmap.deserializer.MetadataDeserializerManager;
import de.decoit.simu.cbor.ifmap.deserializer.RequestDeserializerManager;
import de.decoit.simu.cbor.ifmap.enums.CBORTags;
import de.decoit.simu.cbor.ifmap.enums.IfMapLifetime;
import de.decoit.simu.cbor.ifmap.enums.IfMapValidationType;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.ifmap.identifier.extended.AbstractExtendedIdentifier;
import de.decoit.simu.cbor.ifmap.metadata.AbstractMetadata;
import de.decoit.simu.cbor.ifmap.request.CBORPublishRequest;
import de.decoit.simu.cbor.ifmap.request.model.publish.AbstractUpdateNotify;
import de.decoit.simu.cbor.ifmap.request.model.publish.CBORPublishDelete;
import de.decoit.simu.cbor.ifmap.request.model.publish.CBORPublishNotify;
import de.decoit.simu.cbor.ifmap.request.model.publish.CBORPublishUpdate;
import de.decoit.simu.cbor.ifmap.util.DictionaryHelper;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;


/**
 * The singleton instance of this class may be used to deserialize requests of type {@link CBORPublishRequest}.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class PublishDeserializer implements InternalRequestDeserializer<CBORPublishRequest> {
	private static PublishDeserializer instance;


	/**
	 * Get the singleton instance of this deserializer.
	 *
	 * @return Deserializer instance
	 */
	public static PublishDeserializer getInstance() {
		if(instance == null) {
			instance = new PublishDeserializer();
		}

		return instance;
	}


	/**
	 * Private constructor, this is a singleton class.
	 */
	private PublishDeserializer() {}
	
	
	@Override
	public CBORPublishRequest deserialize(final Array attributes, 
										  final Array nestedTags, 
										  final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		if(log.isDebugEnabled()) {
			log.debug("Attributes array: " + attributes);
			log.debug("Nested tags array: " + nestedTags);
			log.debug("Dictionary entry: " + elementDictEntry);
		}
		
		// Initially define the required variables to build the target object
		String sessionId = null;
		IfMapValidationType validation = null;

		// Get list of all attribute data items
		List<DataItem> attributesDataItems = attributes.getDataItems();

		// Iterate over the data items in steps of 2
		for(int i=0; i<attributesDataItems.size(); i=i+2) {
			// Get name and value data items
			DataItem attrName = attributesDataItems.get(i);
			DataItem attrValue = attributesDataItems.get(i+1);

			String attrNameStr = RequestDeserializerManager.getAttributeXmlName(attrName, elementDictEntry);

			// Process the attribute value
			switch(attrNameStr) {
				case SessionAttributeGroup.SESSION_ID:
					sessionId = RequestDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
				case ValidationAttributeGroup.VALIDATION:
					validation = IfMapValidationType.fromXmlName(RequestDeserializerManager.getAttributeEnumValueXmlName(attrName, attrValue, elementDictEntry));
					break;
			}
		}
		
		CBORPublishRequest rv = new CBORPublishRequest(sessionId);
		rv.setValidation(validation);
		
		// Get list of all nested tags data items
		List<DataItem> nestedTagsDataItems = nestedTags.getDataItems();

		// Iterate over the data items in steps of 4
		for(int i=0; i<nestedTagsDataItems.size(); i=i+4) {
			// Get namespace, name and nested tag/value data items (index i and i+1)
			DataItem ntNamespace = nestedTagsDataItems.get(i);
			DataItem ntName = nestedTagsDataItems.get(i+1);
			DataItem ntAttributes = nestedTagsDataItems.get(i+2);
			DataItem ntNestedTags = nestedTagsDataItems.get(i+3);

			// The namespace should be of simple type NULL, no namespace is expected to be found here
			if(!RequestDeserializerManager.isSimpleValueNull(ntNamespace)) {
				throw new CBORDeserializationException("Unexpected nested element with namespace found inside 'publish' element");
			}

			String nestedTagName = RequestDeserializerManager.getNestedTagXmlName(ntName, elementDictEntry);

			// Process the nested element value
			try {
				DictionarySimpleElement pubTypeEntry = DictionaryHelper.findNestedElement(nestedTagName, elementDictEntry);
				
				switch(nestedTagName) {
					case CBORPublishUpdate.XML_NAME:
						rv.addPublishType(processPublishUpdate((Array) ntAttributes, (Array) ntNestedTags, pubTypeEntry));
						break;
					case CBORPublishNotify.XML_NAME:
						rv.addPublishType(processPublishNotify((Array) ntAttributes, (Array) ntNestedTags, pubTypeEntry));
						break;
					case CBORPublishDelete.XML_NAME:
						rv.addPublishType(processPublishDelete((Array) ntAttributes, (Array) ntNestedTags, pubTypeEntry));
						break;
				}
			}
			catch(DictionaryPathException ex) {
				throw new CBORDeserializationException("Deserialization failed, see nested exception for details", ex);
			}
		}
		
		return rv;
	}
	
	
	/**
	 * Process the update element contained inside the publish request.
	 * 
	 * @param attributes CBOR array data item containing the element's attributes
	 * @param nestedTags CBOR array data item containing the element's nested tags
	 * @param parentEntry Dictionary entry of the update element
	 * @return The deserialized update object
	 * @throws CBORDeserializationException if deserialization failed
	 */
	private CBORPublishUpdate processPublishUpdate(final Array attributes, 
												   final Array nestedTags, 
												   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		if(log.isDebugEnabled()) {
			log.debug("processPublishUpdate():");
			log.debug("Attributes array: " + attributes);
			log.debug("Nested tags array: " + nestedTags);
			log.debug("Dictionary entry: " + elementDictEntry);
		}
		
		IfMapLifetime lifetime = null;
		
		// Get list of all attribute data items
		List<DataItem> attributesDataItems = attributes.getDataItems();

		// Iterate over the data items in steps of 2
		for(int i=0; i<attributesDataItems.size(); i=i+2) {
			// Get name and value data items
			DataItem attrName = attributesDataItems.get(i);
			DataItem attrValue = attributesDataItems.get(i+1);

			String attrNameStr = RequestDeserializerManager.getAttributeXmlName(attrName, elementDictEntry);

			// Process the attribute value
			switch(attrNameStr) {
				case CBORPublishUpdate.LIFETIME:
					lifetime = IfMapLifetime.fromXmlName(RequestDeserializerManager.getAttributeEnumValueXmlName(attrName, attrValue, elementDictEntry));
					break;
			}
		}
		
		// This array will hold the identifiers after deserialization
		AbstractIdentifier[] identifiers = {null, null};
		List<AbstractMetadata> metadata = new ArrayList<>();
		
		processUpdateNotifyNestedTags(nestedTags, elementDictEntry, identifiers, metadata);
		
		CBORPublishUpdate rv = new CBORPublishUpdate(identifiers[0], identifiers[1]);
		rv.setLiftime(lifetime);
		
		metadata.stream().forEach((m) -> {
			rv.addMetadata(m);
		});
		
		return rv;
	}
	
	
	/**
	 * Process the notify element contained inside the publish request.
	 * 
	 * @param attributes CBOR array data item containing the element's attributes
	 * @param nestedTags CBOR array data item containing the element's nested tags
	 * @param parentEntry Dictionary entry of the notify element
	 * @return The deserialized notify object
	 * @throws CBORDeserializationException if deserialization failed
	 */
	private CBORPublishNotify processPublishNotify(final Array attributes, 
												   final Array nestedTags, 
												   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		if(log.isDebugEnabled()) {
			log.debug("processPublishNotify():");
			log.debug("Attributes array: " + attributes);
			log.debug("Nested tags array: " + nestedTags);
			log.debug("Dictionary entry: " + elementDictEntry);
		}
		
		IfMapLifetime lifetime = null;
		
		// Get list of all attribute data items
		List<DataItem> attributesDataItems = attributes.getDataItems();

		// Iterate over the data items in steps of 2
		for(int i=0; i<attributesDataItems.size(); i=i+2) {
			// Get name and value data items
			DataItem attrName = attributesDataItems.get(i);
			DataItem attrValue = attributesDataItems.get(i+1);

			String attrNameStr = RequestDeserializerManager.getAttributeXmlName(attrName, elementDictEntry);

			// Process the attribute value
			switch(attrNameStr) {
				case CBORPublishNotify.LIFETIME:
					lifetime = IfMapLifetime.fromXmlName(RequestDeserializerManager.getAttributeEnumValueXmlName(attrName, attrValue, elementDictEntry));
					break;
			}
		}
		
		// This array will hold the identifiers after deserialization
		AbstractIdentifier[] identifiers = {null, null};
		List<AbstractMetadata> metadata = new ArrayList<>();
		
		processUpdateNotifyNestedTags(nestedTags, elementDictEntry, identifiers, metadata);
		
		CBORPublishNotify rv = new CBORPublishNotify(identifiers[0], identifiers[1]);
		rv.setLiftime(lifetime);
		
		metadata.stream().forEach((m) -> {
			rv.addMetadata(m);
		});
		
		return rv;
	}
	
	
	/**
	 * Process the nested tags contained inside the update or notify element of a publish request.
	 * 
	 * @param nestedTags CBOR array data item containing the element's nested tags
	 * @param elementDictEntry Dictionary entry of the update/notify element
	 * @param identifiers The contained identifiers will be stored in this array
	 * @param metadata The contained metadata will be stored in this list
	 * @throws CBORDeserializationException if deserialization failed
	 */
	private void processUpdateNotifyNestedTags(final Array nestedTags, 
											   final DictionarySimpleElement elementDictEntry,
											   final AbstractIdentifier[] identifiers,
											   final List<AbstractMetadata> metadata) throws CBORDeserializationException {
		int identifierIndex = 0;
		
		// Get list of all nested tags data items
		List<DataItem> nestedTagsDataItems = nestedTags.getDataItems();

		// Iterate over the data items in steps of 4
		for(int i=0; i<nestedTagsDataItems.size(); i=i+4) {
			// Get namespace, name and nested tag/value data items (index i and i+1)
			DataItem ntNamespace = nestedTagsDataItems.get(i);
			DataItem ntName = nestedTagsDataItems.get(i+1);
			DataItem ntAttributes = nestedTagsDataItems.get(i+2);
			DataItem ntNestedTags = nestedTagsDataItems.get(i+3);

			if(ntAttributes.getMajorType() != MajorType.ARRAY) {
				throw new CBORDeserializationException("Invalid major type for data item in attributes array position: " + ntAttributes.getMajorType());
			}
			
			if(ntNestedTags.getMajorType() != MajorType.ARRAY) {
				throw new CBORDeserializationException("Invalid major type for data item in nested tags array position: " + ntNestedTags.getMajorType());
			}
			
			// If name space is simple value NULL, then we found the metadata element
			if(RequestDeserializerManager.isSimpleValueNull(ntNamespace)) {
				String ntXmlName = RequestDeserializerManager.getNestedTagXmlName(ntName, elementDictEntry);
				
				if(AbstractUpdateNotify.METADATA.equals(ntXmlName)) {
					Array metadataArray = (Array) ntNestedTags;
					
					// Get list of all nested tags data items
					List<DataItem> metadataDataItems = metadataArray.getDataItems();

					// Iterate over the data items in steps of 4
					for(int j=0; j<metadataDataItems.size(); j=j+4) {
						DataItem metadataNamespace = metadataDataItems.get(j);
						DataItem metadataName = metadataDataItems.get(j+1);
						DataItem metadataAttributes = metadataDataItems.get(j+2);
						DataItem metadataNestedTags = metadataDataItems.get(j+3);
						
						if(metadataAttributes.getMajorType() != MajorType.ARRAY) {
							throw new CBORDeserializationException("Invalid major type for data item in attributes array position: " + metadataAttributes.getMajorType());
						}
						
						String namespace = RequestDeserializerManager.getNamespaceXmlName(metadataNamespace);
						String elementName = RequestDeserializerManager.getTopLevelElementXmlName(metadataNamespace, metadataName);
						
						Class<? extends AbstractMetadata> targetClass = MetadataDeserializerManager.resolveTargetClass(namespace, elementName);
						
						metadata.add(MetadataDeserializerManager.deserialize(metadataNamespace, metadataName, 
																	  (Array) metadataAttributes, metadataNestedTags,
																	  targetClass));
					}
				}
				else {
					throw new CBORDeserializationException("Found unknown nested tag inside publish/update element: " + ntXmlName);
				}
			}
			else {
				String namespace = RequestDeserializerManager.getNamespaceXmlName(ntNamespace);
				String elementName = RequestDeserializerManager.getTopLevelElementXmlName(ntNamespace, ntName);

				// Determine if target element is an extended identifier or not
				if(ntName.hasTag() && ntName.getTag().equals(CBORTags.IF_MAP_EXTENDED_IDENTIFIER.getTagDataItem())) {
					Class<? extends AbstractExtendedIdentifier> targetClass = ExtendedIdentifierDeserializerManager.resolveTargetClass(namespace, elementName);
					
					identifiers[identifierIndex] = ExtendedIdentifierDeserializerManager.deserialize(ntNamespace, ntName, (Array) ntAttributes,
							(Array) ntNestedTags, targetClass);
				}
				else {
					Class<? extends AbstractIdentifier> targetClass = IdentifierDeserializerManager.resolveTargetClass(namespace, elementName);
					
					identifiers[identifierIndex] = IdentifierDeserializerManager.deserialize(ntNamespace, ntName, (Array) ntAttributes,
							(Array) ntNestedTags, targetClass);
				}

				identifierIndex++;
			}
		}
	}
	
	
	/**
	 * Process the delete element contained inside the publish request.
	 * 
	 * @param attributes CBOR array data item containing the element's attributes
	 * @param nestedTags CBOR array data item containing the element's nested tags
	 * @param parentEntry Dictionary entry of the delete element
	 * @return The deserialized delete object
	 * @throws CBORDeserializationException if deserialization failed
	 */
	private CBORPublishDelete processPublishDelete(final Array attributes, 
												   final Array nestedTags, 
												   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		if(log.isDebugEnabled()) {
			log.debug("processPublishDelete():");
			log.debug("Attributes array: " + attributes);
			log.debug("Nested tags array: " + nestedTags);
			log.debug("Dictionary entry: " + elementDictEntry);
		}
		
		String filter = null;
		
		// Get list of all attribute data items
		List<DataItem> attributesDataItems = attributes.getDataItems();

		// Iterate over the data items in steps of 2
		for(int i=0; i<attributesDataItems.size(); i=i+2) {
			// Get name and value data items
			DataItem attrName = attributesDataItems.get(i);
			DataItem attrValue = attributesDataItems.get(i+1);

			String attrNameStr = RequestDeserializerManager.getAttributeXmlName(attrName, elementDictEntry);

			// Process the attribute value
			switch(attrNameStr) {
				case CBORPublishDelete.FILTER:
					filter = RequestDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
			}
		}
		
		// This array will hold the identifiers after deserialization
		AbstractIdentifier[] identifiers = {null, null};
		int identifierIndex = 0;
		
		// Get list of all nested tags data items
		List<DataItem> nestedTagsDataItems = nestedTags.getDataItems();

		// Iterate over the data items in steps of 4
		for(int i=0; i<nestedTagsDataItems.size(); i=i+4) {
			// Get namespace, name and nested tag/value data items (index i and i+1)
			// No further nested elements are expected, only a value should be present (index i+3)
			DataItem ntNamespace = nestedTagsDataItems.get(i);
			DataItem ntName = nestedTagsDataItems.get(i+1);
			DataItem ntAttributes = nestedTagsDataItems.get(i+2);
			DataItem ntNestedTags = nestedTagsDataItems.get(i+3);

			// The namespace should NOT be of simple type NULL
			if(RequestDeserializerManager.isSimpleValueNull(ntNamespace)) {
				throw new CBORDeserializationException("Unexpected nested element without namespace found inside 'publish/delete' element");
			}
			
			if(ntAttributes.getMajorType() != MajorType.ARRAY) {
				throw new CBORDeserializationException("Invalid major type for data item in attributes array position: " + ntAttributes.getMajorType());
			}
			
			if(ntNestedTags.getMajorType() != MajorType.ARRAY) {
				throw new CBORDeserializationException("Invalid major type for data item in nested tags array position: " + ntNestedTags.getMajorType());
			}

			String namespace = RequestDeserializerManager.getNamespaceXmlName(ntNamespace);
			String elementName = RequestDeserializerManager.getTopLevelElementXmlName(ntNamespace, ntName);

			// Determine if target element is an extended identifier or not
			if(ntName.hasTag() && ntName.getTag().equals(CBORTags.IF_MAP_EXTENDED_IDENTIFIER.getTagDataItem())) {
				Class<? extends AbstractExtendedIdentifier> targetClass = ExtendedIdentifierDeserializerManager.resolveTargetClass(namespace, elementName);
				identifiers[identifierIndex] = ExtendedIdentifierDeserializerManager.deserialize(ntNamespace, ntName, (Array) ntAttributes,
						(Array) ntNestedTags, targetClass);
			}
			else {
				Class<? extends AbstractIdentifier> targetClass = IdentifierDeserializerManager.resolveTargetClass(namespace, elementName);
				identifiers[identifierIndex] = IdentifierDeserializerManager.deserialize(ntNamespace, ntName, (Array) ntAttributes,
						(Array) ntNestedTags, targetClass);
			}
			
			identifierIndex++;
		}
		
		CBORPublishDelete rv = new CBORPublishDelete(identifiers[0], identifiers[1]);
		rv.setFilter(filter);
		
		return rv;
	}
}
