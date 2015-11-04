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
package de.decoit.simu.cbor.ifmap.deserializer.identifier;

import co.nstant.in.cbor.model.Array;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;



/**
 * This interface is used to specify identifier deserializers that are used internally.
 * A developer extending this library MUST NOT use this interface. Use the {@link VendorIdentifierDeserializer} interface
 * for your extended identifiers instead.
 *
 * @author Thomas Rix (rix@decoit.de)
 *
 * @param <T> Identifier class to be deserialized by the implementing class
 */
public interface InternalIdentifierDeserializer<T extends AbstractIdentifier> {
	/**
	 * Deserialize an object of type T from the specified CBOR data items.
	 * The attributes and nested tags arrays may be empty but never null. If the dictionary entry is null, it will be 
	 * assumed that the data was serialized using the string fallback method.
	 * 
	 * @param attributes CBOR array data item containing the element's attributes
	 * @param nestedTags CBOR array data item containing the element's nested tags
	 * @param elementDictEntry Dictionary entry for the target element
	 * @return The deserialized identifer object
	 * @throws CBORDeserializationException if deserialization failed
	 */
	public T deserialize(final Array attributes, final Array nestedTags, final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException;
}
