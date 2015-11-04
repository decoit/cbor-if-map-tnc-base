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
import de.decoit.simu.cbor.ifmap.attributegroup.SessionAttributeGroup;
import de.decoit.simu.cbor.ifmap.deserializer.ResponseDeserializerManager;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.response.model.CBORNewSessionResult;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * The singleton instance of this class may be used to deserialize response results of type {@link CBORNewSessionResult}.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class NewSessionResultDeserializer implements InternalResultDeserializer<CBORNewSessionResult> {
	private static NewSessionResultDeserializer instance;


	/**
	 * Get the singleton instance of this deserializer.
	 *
	 * @return Deserializer instance
	 */
	public static NewSessionResultDeserializer getInstance() {
		if(instance == null) {
			instance = new NewSessionResultDeserializer();
		}

		return instance;
	}


	/**
	 * Private constructor, this is a singleton class.
	 */
	private NewSessionResultDeserializer() {}
	
	
	@Override
	public CBORNewSessionResult deserialize(final Array attributes, 
											final Array nestedTags, 
											final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		if(log.isDebugEnabled()) {
			log.debug("Attributes array: " + attributes);
			log.debug("Nested tags array: " + nestedTags);
			log.debug("Dictionary entry: " + elementDictEntry);
		}
		
		String sessionId = null;
		String publisherId = null;
		Integer maxPollResultSize = null;
		
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
				case SessionAttributeGroup.SESSION_ID:
					sessionId = ResponseDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
				case CBORNewSessionResult.IFMAP_PUBLISHER_ID:
					publisherId = ResponseDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
				case CBORNewSessionResult.MAX_POLL_RESULT_SIZE:
					maxPollResultSize = ResponseDeserializerManager.processUnsignedIntegerItem(attrValue, true).intValueExact();
					break;
			}
		}
		
		CBORNewSessionResult rv = new CBORNewSessionResult(sessionId, publisherId);
		rv.setMaxPollResultSize(maxPollResultSize);
		
		return rv;
	}
}
