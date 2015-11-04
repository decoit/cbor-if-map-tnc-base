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
import de.decoit.simu.cbor.ifmap.attributegroup.SearchTypeAttributeGroup;
import de.decoit.simu.cbor.ifmap.attributegroup.SessionAttributeGroup;
import de.decoit.simu.cbor.ifmap.attributegroup.ValidationAttributeGroup;
import de.decoit.simu.cbor.ifmap.deserializer.ExtendedIdentifierDeserializerManager;
import de.decoit.simu.cbor.ifmap.deserializer.IdentifierDeserializerManager;
import de.decoit.simu.cbor.ifmap.deserializer.RequestDeserializerManager;
import de.decoit.simu.cbor.ifmap.enums.CBORTags;
import de.decoit.simu.cbor.ifmap.enums.IfMapValidationType;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.ifmap.identifier.extended.AbstractExtendedIdentifier;
import de.decoit.simu.cbor.ifmap.request.CBORSubscribeRequest;
import de.decoit.simu.cbor.ifmap.request.model.search.CBORSubscribeDelete;
import de.decoit.simu.cbor.ifmap.request.model.search.CBORSubscribeUpdate;
import de.decoit.simu.cbor.ifmap.util.DictionaryHelper;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * The singleton instance of this class may be used to deserialize requests of type {@link CBORSubscribeRequest}.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class SubscribeDeserializer implements InternalRequestDeserializer<CBORSubscribeRequest> {
	private static SubscribeDeserializer instance;


	/**
	 * Get the singleton instance of this deserializer.
	 *
	 * @return Deserializer instance
	 */
	public static SubscribeDeserializer getInstance() {
		if(instance == null) {
			instance = new SubscribeDeserializer();
		}

		return instance;
	}


	/**
	 * Private constructor, this is a singleton class.
	 */
	private SubscribeDeserializer() {}
	
	
	@Override
	public CBORSubscribeRequest deserialize(final Array attributes, 
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
		
		CBORSubscribeRequest rv = new CBORSubscribeRequest(sessionId);
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
				throw new CBORDeserializationException("Unexpected nested element with namespace found inside 'subscribe' element");
			}

			String nestedTagName = RequestDeserializerManager.getNestedTagXmlName(ntName, elementDictEntry);

			// Process the nested element value
			try {
				DictionarySimpleElement pubTypeEntry = DictionaryHelper.findNestedElement(nestedTagName, elementDictEntry);
				
				switch(nestedTagName) {
					case CBORSubscribeUpdate.XML_NAME:
						rv.addSubscribeType(processSubscribeUpdate((Array) ntAttributes, (Array) ntNestedTags, pubTypeEntry));
						break;
					case CBORSubscribeDelete.XML_NAME:
						rv.addSubscribeType(processSubscribeDelete((Array) ntAttributes, (Array) ntNestedTags, pubTypeEntry));
						break;
				}
			}
			catch(DictionaryPathException ex) {
				throw new CBORDeserializationException("Deserialization failed, see nested exception for details", ex);
			}
		}
		
		return rv;
	}
	
	
	private CBORSubscribeUpdate processSubscribeUpdate(final Array attributes, 
													   final Array nestedTags, 
													   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		if(log.isDebugEnabled()) {
			log.debug("processPublishUpdate():");
			log.debug("Attributes array: " + attributes);
			log.debug("Nested tags array: " + nestedTags);
			log.debug("Dictionary entry: " + elementDictEntry);
		}
		
		String name = null;
		AbstractIdentifier identifier = null;
		String matchLinks = null;
		Integer maxDepth = null;
		Integer maxSize = null;
		String resultFilter = null;
		String terminalIdentifierType = null;
		
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
				case CBORSubscribeUpdate.NAME:
					name = RequestDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
				case SearchTypeAttributeGroup.MATCH_LINKS:
					matchLinks = RequestDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
				case SearchTypeAttributeGroup.MAX_DEPTH:
					maxDepth = RequestDeserializerManager.processUnsignedIntegerItem(attrValue, true).intValueExact();
					break;
				case SearchTypeAttributeGroup.MAX_SIZE:
					maxSize = RequestDeserializerManager.processUnsignedIntegerItem(attrValue, true).intValueExact();
					break;
				case SearchTypeAttributeGroup.RESULT_FILTER:
					resultFilter = RequestDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
				case SearchTypeAttributeGroup.TERMINAL_IDENTIFIER_TYPE:
					terminalIdentifierType = RequestDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
			}
		}
		
		// Get list of all nested tags data items
		List<DataItem> nestedTagsDataItems = nestedTags.getDataItems();

		DataItem ntNamespace = nestedTagsDataItems.get(0);
		DataItem ntName = nestedTagsDataItems.get(1);
		DataItem ntAttributes = nestedTagsDataItems.get(2);
		DataItem ntNestedTags = nestedTagsDataItems.get(3);

		// The namespace should be of simple type NULL, no namespace is expected to be found here
		if(RequestDeserializerManager.isSimpleValueNull(ntNamespace)) {
			throw new CBORDeserializationException("Unexpected nested element without namespace found inside 'search' element");
		}

		// Process the nested element value
		try {
			String namespace = RequestDeserializerManager.getNamespaceXmlName(ntNamespace);
			String elementName = RequestDeserializerManager.getTopLevelElementXmlName(ntNamespace, ntName);

			// Determine if target element is an extended identifier or not
			if(ntName.hasTag() && ntName.getTag().equals(CBORTags.IF_MAP_EXTENDED_IDENTIFIER.getTagDataItem())) {
				Class<? extends AbstractExtendedIdentifier> targetClass = ExtendedIdentifierDeserializerManager.resolveTargetClass(namespace, elementName);

				identifier = ExtendedIdentifierDeserializerManager.deserialize(ntNamespace, ntName, (Array) ntAttributes,
						(Array) ntNestedTags, targetClass);
			}
			else {
				Class<? extends AbstractIdentifier> targetClass = IdentifierDeserializerManager.resolveTargetClass(namespace, elementName);

				identifier = IdentifierDeserializerManager.deserialize(ntNamespace, ntName, (Array) ntAttributes,
						(Array) ntNestedTags, targetClass);
			}
		}
		catch(Exception ex) {
			throw new CBORDeserializationException("Deserialization failed, see nested exception for details", ex);
		}
		
		CBORSubscribeUpdate rv = new CBORSubscribeUpdate(name, identifier);
		rv.getSearchTypeAttributes().setMatchLinks(matchLinks);
		rv.getSearchTypeAttributes().setMaxDepth(maxDepth);
		rv.getSearchTypeAttributes().setMaxSize(maxSize);
		rv.getSearchTypeAttributes().setResultFilter(resultFilter);
		rv.getSearchTypeAttributes().setTerminalIdentifierType(terminalIdentifierType);
		
		return rv;
	}
	
	
	private CBORSubscribeDelete processSubscribeDelete(final Array attributes, 
													   final Array nestedTags, 
													   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		if(log.isDebugEnabled()) {
			log.debug("processPublishDelete():");
			log.debug("Attributes array: " + attributes);
			log.debug("Nested tags array: " + nestedTags);
			log.debug("Dictionary entry: " + elementDictEntry);
		}
		
		String name = null;
		
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
				case CBORSubscribeDelete.NAME:
					name = RequestDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
			}
		}
		
		CBORSubscribeDelete rv = new CBORSubscribeDelete(name);
		
		return rv;
	}
}
