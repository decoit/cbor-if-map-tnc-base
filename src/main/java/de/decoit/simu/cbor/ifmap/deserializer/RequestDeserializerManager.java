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
import co.nstant.in.cbor.model.UnicodeString;
import de.decoit.simu.cbor.ifmap.deserializer.request.EndSessionDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.request.NewSessionDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.request.PollDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.request.PublishDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.request.PurgePublisherDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.request.RenewSessionDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.request.SearchDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.request.SubscribeDeserializer;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.request.AbstractRequest;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import lombok.extern.slf4j.Slf4j;

/**
 * Central class for managing request deserializers.
 * Users of this library should never use the methods provided by this class directly. Use the public interface
 * class {@link CBORDeserializer} instead.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class RequestDeserializerManager extends AbstractDeserializerManager {
	/**
	 * Deserialize a request object structure from the specified data items.
	 * The attributes and nested tags arrays may be empty but never null.
	 *
	 * @param namespace CBOR data item representing the element namespace
	 * @param cborName CBOR data item representing the element name
	 * @param attributes CBOR array data item containing the element's attributes
	 * @param nestedTags CBOR array data item containing the element's nested tags
	 * @return The deserialized object
	 * @throws CBORDeserializationException if deserialization failed
	 */
	public static AbstractRequest deserialize(final DataItem namespace, 
											  final DataItem cborName, 
											  final Array attributes, 
											  final Array nestedTags) throws CBORDeserializationException {
		DictionarySimpleElement requestEntry =  getTopLevelElement(namespace, cborName);
		String reqXmlName;
		
		if(requestEntry != null) {
			reqXmlName = requestEntry.getXmlName();
		}
		else {
			if(cborName.getMajorType() == MajorType.UNICODE_STRING) {
				UnicodeString us = (UnicodeString) cborName;
				reqXmlName = us.getString();
			}
			else {
				throw new CBORDeserializationException("Non-UnicodeString request name found without dictionary entry");
			}
		}
		
		try {
			switch(reqXmlName) {
				case "publish":
					return PublishDeserializer.getInstance().deserialize(attributes, nestedTags, requestEntry);
				case "search":
					return SearchDeserializer.getInstance().deserialize(attributes, nestedTags, requestEntry);
				case "subscribe":
					return SubscribeDeserializer.getInstance().deserialize(attributes, nestedTags, requestEntry);
				case "poll":
					return PollDeserializer.getInstance().deserialize(attributes, nestedTags, requestEntry);
				case "purgePublisher":
					return PurgePublisherDeserializer.getInstance().deserialize(attributes, nestedTags, requestEntry);
				case "newSession":
					return NewSessionDeserializer.getInstance().deserialize(attributes, nestedTags, requestEntry);
				case "endSession":
					return EndSessionDeserializer.getInstance().deserialize(attributes, nestedTags, requestEntry);
				case "renewSession":
					return RenewSessionDeserializer.getInstance().deserialize(attributes, nestedTags, requestEntry);
				default:
					throw new CBORDeserializationException("Unknown request type: " + reqXmlName);
			}
		}
		catch(RuntimeException ex) {
			throw new CBORDeserializationException("Deserialization failed, see nested exception for details", ex);
		}
	}
}
