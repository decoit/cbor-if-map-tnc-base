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
package de.decoit.simu.cbor.ifmap.attributegroup;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnicodeString;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.xml.dictionary.DictionaryEnumValueAttribute;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleAttribute;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;




/**
 * Abstract base class for all IF-MAP attribute group definitions.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode
@ToString
@Slf4j
public abstract class AbstractAttributeGroup {
	/**
	 * Serialize the values stored in this class into the parent element's attributes array.
	 * If the provided dictionary entry for the parent element is null or does not contain one or more
	 * attribute mappings, the attribute names will be used as unicode strings in CBOR.
	 *
	 * @param builder The parent element's attributes array builder
	 * @param elementEntry Dictionary entry of the parent element
	 * @throws CBORSerializationException if serialization fails for some reason
	 */
	public abstract void serializeAttributeGroup(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException;


	/**
	 * Get the CBOR mapping of the specified attribute from the dictionary.
	 * If no entry is found, this method returns a default UnicodeString mapping using the specified XML name.
	 * Log entries with log level WARN are created when no entry was found.
	 *
	 * @param attributeName XML name of the attribute
	 * @param fromElement Element dictionary entry to read the mapping from
	 * @return DataItem with CBOR mapping
	 */
	protected final DataItem getAttributeNameMapping(final String attributeName, final DictionarySimpleElement fromElement) {
		if(fromElement != null) {
			DictionarySimpleAttribute attributeEntry = fromElement.lookupAttribute(attributeName);

			if(attributeEntry != null) {
				return attributeEntry.getCborName();
			}
		}

		log.info("No dictionary entry found for attribute name: " + attributeName);
		log.info("Using fallback mapping: UnicodeString(" + attributeName + ")");

		return new UnicodeString(attributeName);
	}


	/**
	 * Get the CBOR mapping of the specified attribute enum value from the dictionary.
	 * If no entry is found, this method returns a default UnicodeString mapping using the specified XML name.
	 * Log entries with log level WARN are created when no entry was found.
	 *
	 * @param attributeName XML name of the attribute the enum value belogs to
	 * @param enumValue XML name of the enum value
	 * @param fromElement Element dictionary entry to read the mapping from
	 * @return DataItem with CBOR mapping
	 */
	protected final DataItem getAttributeEnumValueMapping(final String attributeName, final String enumValue, final DictionarySimpleElement fromElement) {
		if(fromElement != null) {
			DictionarySimpleAttribute fromAttribute = fromElement.lookupAttribute(attributeName);

			if(fromAttribute instanceof DictionaryEnumValueAttribute) {
				DictionaryEnumValueAttribute enumAttribute = (DictionaryEnumValueAttribute) fromAttribute;

				DataItem rv = enumAttribute.lookupEnumValue(enumValue);

				if(rv != null) {
					return rv;
				}
			}
		}

		log.info("No dictionary entry found for attribute enum value: " + enumValue);
		log.info("Using fallback mapping: UnicodeString(" + enumValue + ")");

		return new UnicodeString(enumValue);
	}
}
