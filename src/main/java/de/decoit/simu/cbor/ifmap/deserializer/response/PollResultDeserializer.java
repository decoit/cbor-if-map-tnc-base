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
package de.decoit.simu.cbor.ifmap.deserializer.response;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import de.decoit.simu.cbor.ifmap.deserializer.ExtendedIdentifierDeserializerManager;
import de.decoit.simu.cbor.ifmap.deserializer.IdentifierDeserializerManager;
import de.decoit.simu.cbor.ifmap.deserializer.MetadataDeserializerManager;
import de.decoit.simu.cbor.ifmap.deserializer.RequestDeserializerManager;
import de.decoit.simu.cbor.ifmap.deserializer.ResponseDeserializerManager;
import de.decoit.simu.cbor.ifmap.enums.CBORTags;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.ifmap.identifier.extended.AbstractExtendedIdentifier;
import de.decoit.simu.cbor.ifmap.metadata.AbstractMetadata;
import de.decoit.simu.cbor.ifmap.response.model.CBORErrorResult;
import de.decoit.simu.cbor.ifmap.response.model.CBORPollResult;
import de.decoit.simu.cbor.ifmap.response.model.search.DeletePollSearchResult;
import de.decoit.simu.cbor.ifmap.response.model.search.NotifyPollSearchResult;
import de.decoit.simu.cbor.ifmap.response.model.search.SearchPollSearchResult;
import de.decoit.simu.cbor.ifmap.response.model.search.SearchResultItem;
import de.decoit.simu.cbor.ifmap.response.model.search.UpdatePollSearchResult;
import de.decoit.simu.cbor.ifmap.util.DictionaryHelper;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * The singleton instance of this class may be used to deserialize response results of type {@link CBORPollResult}.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class PollResultDeserializer implements InternalResultDeserializer<CBORPollResult> {
	private static PollResultDeserializer instance;


	/**
	 * Get the singleton instance of this deserializer.
	 *
	 * @return Deserializer instance
	 */
	public static PollResultDeserializer getInstance() {
		if(instance == null) {
			instance = new PollResultDeserializer();
		}

		return instance;
	}


	/**
	 * Private constructor, this is a singleton class.
	 */
	private PollResultDeserializer() {}
	
	
	@Override
	public CBORPollResult deserialize(final Array attributes, 
									  final Array nestedTags, 
									  final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		if(log.isDebugEnabled()) {
			log.debug("Attributes array: " + attributes);
			log.debug("Nested tags array: " + nestedTags);
			log.debug("Dictionary entry: " + elementDictEntry);
		}
		
		CBORPollResult rv = new CBORPollResult();
		
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
			if(!ResponseDeserializerManager.isSimpleValueNull(ntNamespace)) {
				throw new CBORDeserializationException("Unexpected nested element with namespace found inside 'searchResult' element");
			}

			String nestedTagName = ResponseDeserializerManager.getNestedTagXmlName(ntName, elementDictEntry);

			// Process the nested element value
			try {
				DictionarySimpleElement resultItemEntry = DictionaryHelper.findNestedElement(nestedTagName, elementDictEntry);
				
				switch(nestedTagName) {
					case CBORErrorResult.XML_NAME:
						rv.addPollResult(processErrorResult((Array) ntAttributes, (Array) ntNestedTags, resultItemEntry));
						break;
					case DeletePollSearchResult.XML_NAME:
						rv.addPollResult(processDeleteResult((Array) ntAttributes, (Array) ntNestedTags, resultItemEntry));
						break;
					case NotifyPollSearchResult.XML_NAME:
						rv.addPollResult(processNotifyResult((Array) ntAttributes, (Array) ntNestedTags, resultItemEntry));
						break;
					case SearchPollSearchResult.XML_NAME:
						rv.addPollResult(processSearchResult((Array) ntAttributes, (Array) ntNestedTags, resultItemEntry));
						break;
					case UpdatePollSearchResult.XML_NAME:
						rv.addPollResult(processUpdateResult((Array) ntAttributes, (Array) ntNestedTags, resultItemEntry));
						break;
				}
			}
			catch(DictionaryPathException ex) {
				throw new CBORDeserializationException("Deserialization failed, see nested exception for details", ex);
			}
		}
		
		return rv;
	}
	
	
	private CBORErrorResult processErrorResult(final Array attributes, 
											   final Array nestedTags, 
											   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		return ErrorResultDeserializer.getInstance().deserialize(attributes, nestedTags, elementDictEntry);
	}
	
	
	private DeletePollSearchResult processDeleteResult(final Array attributes, 
													   final Array nestedTags, 
													   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		String name = processResultAttributes(attributes, elementDictEntry);
		
		DeletePollSearchResult rv = new DeletePollSearchResult();
		rv.setName(name);
		
		processResultNestedTags(nestedTags, elementDictEntry).stream().forEach((sri) -> {
			rv.addSearchResultItem(sri);
		});
		
		return rv;
	}
	
	
	private NotifyPollSearchResult processNotifyResult(final Array attributes, 
													   final Array nestedTags, 
													   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		String name = processResultAttributes(attributes, elementDictEntry);
		
		NotifyPollSearchResult rv = new NotifyPollSearchResult();
		rv.setName(name);
		
		processResultNestedTags(nestedTags, elementDictEntry).stream().forEach((sri) -> {
			rv.addSearchResultItem(sri);
		});
		
		return rv;
	}
	
	
	private SearchPollSearchResult processSearchResult(final Array attributes, 
													   final Array nestedTags, 
													   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		String name = processResultAttributes(attributes, elementDictEntry);
		
		SearchPollSearchResult rv = new SearchPollSearchResult();
		rv.setName(name);
		
		processResultNestedTags(nestedTags, elementDictEntry).stream().forEach((sri) -> {
			rv.addSearchResultItem(sri);
		});
		
		return rv;
	}
	
	
	private UpdatePollSearchResult processUpdateResult(final Array attributes, 
													   final Array nestedTags, 
													   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		String name = processResultAttributes(attributes, elementDictEntry);
		
		UpdatePollSearchResult rv = new UpdatePollSearchResult();
		rv.setName(name);
		
		processResultNestedTags(nestedTags, elementDictEntry).stream().forEach((sri) -> {
			rv.addSearchResultItem(sri);
		});
		
		return rv;
	}
	
	
	private String processResultAttributes(final Array attributes, 
										   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		String name = null;
		
		// Get list of all attribute data items
		List<DataItem> attributesDataItems = attributes.getDataItems();

		// Iterate over the data items in steps of 2
		for(int i=0; i<attributesDataItems.size(); i=i+2) {
			// Get name and value data items
			DataItem attrName = attributesDataItems.get(i);
			DataItem attrValue = attributesDataItems.get(i+1);

			String attrNameStr = ResponseDeserializerManager.getAttributeXmlName(attrName, elementDictEntry);

			// Process the attribute value
			switch(attrNameStr) {
				case "name":
					name = ResponseDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
			}
		}
		
		return name;
	}
	
	
	private List<SearchResultItem> processResultNestedTags(final Array nestedTags, 
														   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		List<SearchResultItem> rvList = new ArrayList<>();
		
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
			if(!ResponseDeserializerManager.isSimpleValueNull(ntNamespace)) {
				throw new CBORDeserializationException("Unexpected nested element with namespace found inside 'deleteResult/notifyResult/searchResult/updateResult' element");
			}

			String nestedTagName = ResponseDeserializerManager.getNestedTagXmlName(ntName, elementDictEntry);

			// Process the nested element value
			try {
				DictionarySimpleElement resultItemEntry = DictionaryHelper.findNestedElement(nestedTagName, elementDictEntry);
				
				switch(nestedTagName) {
					case "resultItem":
						rvList.add(processResultItem((Array) ntAttributes, (Array) ntNestedTags, resultItemEntry));
						break;
				}
			}
			catch(DictionaryPathException ex) {
				throw new CBORDeserializationException("Deserialization failed, see nested exception for details", ex);
			}
		}
		
		return rvList;
	}
	
	
	private SearchResultItem processResultItem(final Array attributes, 
											   final Array nestedTags, 
											   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		if(log.isDebugEnabled()) {
			log.debug("processResultItem():");
			log.debug("Attributes array: " + attributes);
			log.debug("Nested tags array: " + nestedTags);
			log.debug("Dictionary entry: " + elementDictEntry);
		}
		
		AbstractIdentifier[] identifiers = {null, null};
		List<AbstractMetadata> metadata = new ArrayList<>();
		
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
				
				if("metadata".equals(ntXmlName)) {
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
		
		SearchResultItem rv = new SearchResultItem(identifiers[0], identifiers[1]);
		metadata.stream().forEach((m) -> {
			rv.addMetadata(m);
		});
		
		return rv;
	}
}
