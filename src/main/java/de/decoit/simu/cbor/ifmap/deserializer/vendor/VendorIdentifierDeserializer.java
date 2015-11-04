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
package de.decoit.simu.cbor.ifmap.deserializer.vendor;

import co.nstant.in.cbor.model.Array;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.identifier.extended.AbstractExtendedIdentifier;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;



/**
 * This interface must be implemented by deserializer classes for vendor extended identifier.
 * Classes that implement this interface can be registered at the {@link ExtendedIdentifierDeserializer}
 * to be used for deserialization of a specific identifier class.
 *
 * @author Thomas Rix (rix@decoit.de)
 * @param <T> Identifier class to be deserialized by the implementing class
 */
public interface VendorIdentifierDeserializer<T extends AbstractExtendedIdentifier> {
	/**
	 * Build an identifier object of the implemented type from the provided CBOR data items.
	 * 
	 * @param attributes CBOR array of attribute data items
	 * @param nestedTags CBOR array of nested tag data items
	 * @param elementDictEntry Dictionary entry of the element to be built
	 * @return Deserialized identifier object
	 * @throws CBORDeserializationException if deserialization fails
	 */
	public T deserialize(final Array attributes, final Array nestedTags, final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException;
}
