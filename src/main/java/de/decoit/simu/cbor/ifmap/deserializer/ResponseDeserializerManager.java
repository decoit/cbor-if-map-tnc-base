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
package de.decoit.simu.cbor.ifmap.deserializer;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import static de.decoit.simu.cbor.ifmap.deserializer.AbstractDeserializerManager.getTopLevelElement;
import de.decoit.simu.cbor.ifmap.deserializer.response.EndSessionResultDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.response.ErrorResultDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.response.NewSessionResultDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.response.PollResultDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.response.PublishReceivedDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.response.PurgePublisherReceivedDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.response.RenewSessionResultDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.response.SearchResultDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.response.SubscribeReceivedDeserializer;
import de.decoit.simu.cbor.ifmap.enums.IfMapValidationType;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.response.CBORResponse;
import de.decoit.simu.cbor.ifmap.response.model.AbstractResult;
import de.decoit.simu.cbor.ifmap.util.DictionaryHelper;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;


/**
 * Central class for managing response deserializers.
 * Users of this library should never use the methods provided by this class directly. Use the public interface
 * class {@link CBORDeserializer} instead.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class ResponseDeserializerManager extends AbstractDeserializerManager {
	/**
	 * Deserialize a response object structure from the specified data items.
	 * The attributes and nested tags arrays may be empty but never null.
	 *
	 * @param namespace CBOR data item representing the element namespace
	 * @param cborName CBOR data item representing the element name
	 * @param attributes CBOR array data item containing the element's attributes
	 * @param nestedTags CBOR array data item containing the element's nested tags
	 * @return The deserialized object
	 * @throws CBORDeserializationException if deserialization failed
	 */
	public static CBORResponse deserialize(final DataItem namespace, 
										   final DataItem cborName, 
										   final Array attributes, 
										   final Array nestedTags) throws CBORDeserializationException {
		DictionarySimpleElement requestEntry =  getTopLevelElement(namespace, cborName);
		
		IfMapValidationType validation = null;
		
		// Get list of all attribute data items
		List<DataItem> attributesDataItems = attributes.getDataItems();

		// Iterate over the data items in steps of 2
		for(int i=0; i<attributesDataItems.size(); i=i+2) {
			// Get name and value data items
			DataItem attrName = attributesDataItems.get(i);
			DataItem attrValue = attributesDataItems.get(i+1);

			String attrNameStr = RequestDeserializerManager.getAttributeXmlName(attrName, requestEntry);

			// Process the attribute value
			switch(attrNameStr) {
				case "validation":
					validation = IfMapValidationType.fromXmlName(RequestDeserializerManager.getAttributeEnumValueXmlName(attrName, attrValue, requestEntry));
					break;
			}
		}
		
		// Get list of all nested tags data items
		List<DataItem> nestedTagsDataItems = nestedTags.getDataItems();

		DataItem ntNamespace = nestedTagsDataItems.get(0);
		DataItem ntName = nestedTagsDataItems.get(1);
		DataItem ntAttributes = nestedTagsDataItems.get(2);
		DataItem ntNestedTags = nestedTagsDataItems.get(3);
		
		if(ntAttributes.getMajorType() != MajorType.ARRAY) {
			throw new CBORDeserializationException("Invalid major type for data item in attributes array position: " + ntAttributes.getMajorType());
		}

		if(ntNestedTags.getMajorType() != MajorType.ARRAY) {
			throw new CBORDeserializationException("Invalid major type for data item in nested tags array position: " + ntNestedTags.getMajorType());
		}
		
		AbstractResult result = processResult(ntNamespace, ntName, (Array) ntAttributes, (Array) ntNestedTags, requestEntry);
		
		CBORResponse rv = new CBORResponse(result);
		rv.setValidation(validation);
		
		return rv;
	}
	
	
	/**
	 * Process the response result contained inside the response.
	 * 
	 * @param namespace CBOR data item representing the element namespace
	 * @param cborName CBOR data item representing the element name
	 * @param attributes CBOR array data item containing the element's attributes
	 * @param nestedTags CBOR array data item containing the element's nested tags
	 * @param parentEntry Dictionary entry of the response element
	 * @return The deserialized result object
	 * @throws CBORDeserializationException if deserialization failed
	 */
	private static AbstractResult processResult(final DataItem namespace, 
												final DataItem cborName, 
												final Array attributes, 
												final Array nestedTags,
												final DictionarySimpleElement parentEntry) throws CBORDeserializationException {
		if(!isSimpleValueNull(namespace)) {
			throw new CBORDeserializationException("Unexpected nested element with namespace found inside 'response' element");
		}
		
		try {
			String resultName = getNestedTagXmlName(cborName, parentEntry);
			DictionarySimpleElement resultEntry = DictionaryHelper.findNestedElement(resultName, parentEntry);

			AbstractResult rv;

			switch(resultName) {
				case "errorResult":
					rv = ErrorResultDeserializer.getInstance().deserialize(attributes, nestedTags, resultEntry);
					break;
				case "publishReceived":
					rv = PublishReceivedDeserializer.getInstance().deserialize(attributes, nestedTags, resultEntry);
					break;
				case "searchResult":
					rv = SearchResultDeserializer.getInstance().deserialize(attributes, nestedTags, resultEntry);
					break;
				case "subscribeReceived":
					rv = SubscribeReceivedDeserializer.getInstance().deserialize(attributes, nestedTags, resultEntry);
					break;
				case "pollResult":
					rv = PollResultDeserializer.getInstance().deserialize(attributes, nestedTags, resultEntry);
					break;
				case "purgePublisherReceived":
					rv = PurgePublisherReceivedDeserializer.getInstance().deserialize(attributes, nestedTags, resultEntry);
					break;
				case "newSessionResult":
					rv = NewSessionResultDeserializer.getInstance().deserialize(attributes, nestedTags, resultEntry);
					break;
				case "endSessionResult":
					rv = EndSessionResultDeserializer.getInstance().deserialize(attributes, nestedTags, resultEntry);
					break;
				case "renewSessionResult":
					rv = RenewSessionResultDeserializer.getInstance().deserialize(attributes, nestedTags, resultEntry);
					break;
				default:
					throw new CBORDeserializationException("Unknown result type: " + resultName);
			}

			return rv;
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORDeserializationException("Deserialization failed, see nested exception for details", ex);
		}
	}
}
