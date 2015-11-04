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
import de.decoit.simu.cbor.ifmap.deserializer.ResponseDeserializerManager;
import de.decoit.simu.cbor.ifmap.enums.IfMapErrorCode;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.response.model.CBORErrorResult;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * The singleton instance of this class may be used to deserialize response results of type {@link CBORErrorResult}.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class ErrorResultDeserializer implements InternalResultDeserializer<CBORErrorResult> {
	private static ErrorResultDeserializer instance;


	/**
	 * Get the singleton instance of this deserializer.
	 *
	 * @return Deserializer instance
	 */
	public static ErrorResultDeserializer getInstance() {
		if(instance == null) {
			instance = new ErrorResultDeserializer();
		}

		return instance;
	}


	/**
	 * Private constructor, this is a singleton class.
	 */
	private ErrorResultDeserializer() {}
	
	
	@Override
	public CBORErrorResult deserialize(final Array attributes, 
									   final Array nestedTags, 
									   final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		if(log.isDebugEnabled()) {
			log.debug("Attributes array: " + attributes);
			log.debug("Nested tags array: " + nestedTags);
			log.debug("Dictionary entry: " + elementDictEntry);
		}
		
		String name = null;
		IfMapErrorCode errorCode = null;
		String errorString = null;
		
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
				case CBORErrorResult.NAME:
					name = ResponseDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
				case CBORErrorResult.ERROR_CODE:
					errorCode = IfMapErrorCode.fromXmlName(ResponseDeserializerManager.getAttributeEnumValueXmlName(attrName, attrValue, elementDictEntry));
					break;
			}
		}
		
		// Get list of all nested tags data items
		List<DataItem> nestedTagsDataItems = nestedTags.getDataItems();

		// Iterate over the data items in steps of 4
		for(int i=0; i<nestedTagsDataItems.size(); i=i+4) {
			// Get namespace, name and nested tag/value data items (index i and i+1)
			DataItem ntNamespace = nestedTagsDataItems.get(i);
			DataItem ntName = nestedTagsDataItems.get(i+1);
			DataItem ntNestedTags = nestedTagsDataItems.get(i+3);

			// The namespace should be of simple type NULL, no namespace is expected to be found here
			if(!ResponseDeserializerManager.isSimpleValueNull(ntNamespace)) {
				throw new CBORDeserializationException("Unexpected nested element with namespace found inside 'errorResult' element");
			}

			String nestedTagName = ResponseDeserializerManager.getNestedTagXmlName(ntName, elementDictEntry);

			// Process the nested element value
			switch(nestedTagName) {
				case CBORErrorResult.ERROR_STRING:
					errorString = ResponseDeserializerManager.processUnicodeStringItem(ntNestedTags, true);
					break;
			}
		}
		
		CBORErrorResult rv = new CBORErrorResult(errorCode);
		rv.setName(name);
		rv.setErrorString(errorString);
		
		return rv;
	}
}
