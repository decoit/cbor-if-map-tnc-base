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
package de.decoit.simu.cbor.ifmap;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.UnicodeString;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.util.DictionaryHelper;
import de.decoit.simu.cbor.xml.dictionary.DictionaryComplexElement;
import de.decoit.simu.cbor.xml.dictionary.DictionaryEnumValueAttribute;
import de.decoit.simu.cbor.xml.dictionary.DictionaryEnumValueElement;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleAttribute;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode
@ToString
@Slf4j
public abstract class AbstractNestedElementBase {
	protected boolean nestedElementProvideParentBuilder = false;
	@Getter
	protected final String elementName;


	public AbstractNestedElementBase(final String elementName) {
		if(StringUtils.isBlank(elementName)) {
			throw new IllegalArgumentException("Element name must not be blank");
		}

		this.elementName = elementName;
	}


	public void cborSerialize(final ArrayBuilder<?> builder, final DictionarySimpleElement parentElementEntry) throws CBORSerializationException {
		try {
			DictionarySimpleElement elementEntry = DictionaryHelper.findNestedElement(this.elementName, parentElementEntry);

			serializeNamespaceAndName(builder, parentElementEntry);

			// Add attributes array
			ArrayBuilder attrBuilder = builder.addArray();
			this.serializeAttributes(attrBuilder, elementEntry);
			attrBuilder.end();

			// Add nested elements array
			if(nestedElementProvideParentBuilder) {
				this.serializeNestedElements(builder, elementEntry);
			}
			else {
				ArrayBuilder elementsBuilder = builder.addArray();
				this.serializeNestedElements(elementsBuilder, elementEntry);
				elementsBuilder.end();
			}
		}
		catch(CBORSerializationException ex) {
			throw ex;
		}
		catch(Exception ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}


	/**
	 * Serialize namespace and element name into the specified ArrayBuilder.
	 *
	 * @param builder ArrayBuilder instance to serialize into
	 * @param parentElementEntry Dictionary entry of parent element
	 * @throws CBORSerializationException
	 */
	protected void serializeNamespaceAndName(final ArrayBuilder<?> builder, final DictionarySimpleElement parentElementEntry) throws CBORSerializationException {
		try {
			DataItem namespaceCborName = new SimpleValue(SimpleValueType.NULL);
			DataItem elementCborName;

			elementCborName = this.getNestedElementNameMapping(this.elementName, parentElementEntry);

			builder.add(namespaceCborName);
			builder.add(elementCborName);
		}
		catch(Exception ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}


	/**
	 * Serialize attributes into the specified ArrayBuilder.
	 * The ArrayBuilder instance MUST build be the actual attribute array,
	 * no parent builder should be provided here.<br>
	 * By default this is a no-op method and needs to be overridden by extending classes.
	 *
	 * @param builder Attributes ArrayBuilder instance
	 * @param elementEntry Dictionary entry of parent element
	 * @throws CBORSerializationException
	 */
	protected void serializeAttributes(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {};


	/**
	 * Serialize nested elements into the specified ArrayBuilder.
	 * The ArrayBuilder instance MUST build be the actual nested elements array,
	 * no parent builder should be provided here.<br>
	 * By default this is a no-op method and needs to be overridden by extending classes.
	 *
	 * @param builder Nested elements ArrayBuilder instance
	 * @param elementEntry Dictionary entry of parent element
	 * @throws CBORSerializationException
	 */
	protected void serializeNestedElements(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {};


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
			else if(fromAttribute != null) {
				log.warn("Tried to read enum value from non-enum value dictionary entry. Dictionary corrupted?");
				log.warn("Element: " + fromElement.getXmlName() + "; Attribute: " + attributeName);
			}
		}

		log.info("No dictionary entry found for attribute enum value: " + enumValue);
		log.info("Using fallback mapping: UnicodeString(" + enumValue + ")");

		return new UnicodeString(enumValue);
	}


	/**
	 * Get the CBOR mapping of the specified nested element from the dictionary.
	 * If no entry is found, this method returns a default UnicodeString mapping using the specified XML name.
	 * Log entries with log level WARN are created when no entry was found.
	 *
	 * @param nestedElementName XML name of the nested element
	 * @param fromElement Element dictionary entry to read the mapping from
	 * @return DataItem with CBOR mapping
	 */
	protected final DataItem getNestedElementNameMapping(final String nestedElementName, final DictionarySimpleElement fromElement) {
		if(fromElement instanceof DictionaryComplexElement) {
			DictionaryComplexElement complexFromElement = (DictionaryComplexElement) fromElement;
			DictionarySimpleElement nestedElement = complexFromElement.lookupNestedElement(nestedElementName);

			if(nestedElement != null) {
				return nestedElement.getCborName();
			}
		}

		log.info("No dictionary entry found for nested element name: " + nestedElementName);
		log.info("Using fallback mapping: UnicodeString(" + nestedElementName + ")");

		return new UnicodeString(nestedElementName);
	}


	/**
	 * Get the CBOR mapping of the specified element enum value from the dictionary.
	 * If no entry is found, this method returns a default UnicodeString mapping using the specified XML name.
	 * Log entries with log level WARN are created when no entry was found.
	 *
	 * @param enumValue XML name of the enum value
	 * @param fromElement Element dictionary entry to read the mapping from
	 * @return DataItem with CBOR mapping
	 */
	protected final DataItem getElementEnumValueMapping(final String enumValue, final DictionarySimpleElement fromElement) {
		if(fromElement instanceof DictionaryEnumValueElement) {
			DictionaryEnumValueElement enumValueFromElement = (DictionaryEnumValueElement) fromElement;

			DataItem rv = enumValueFromElement.lookupEnumValue(enumValue);

			if(rv != null) {
				return rv;
			}
		}
		else if(fromElement != null) {
			log.warn("Tried to read enum value from non-enum value dictionary entry. Dictionary corrupted?");
			log.warn("Element: " + fromElement.getXmlName());
		}

		log.info("No dictionary entry found for element enum value: " + enumValue);
		log.info("Using fallback mapping: UnicodeString(" + enumValue + ")");

		return new UnicodeString(enumValue);
	}


	/**
	 * Get the CBOR mapping of the specified element enum value from the dictionary.
	 * If no entry is found, this method returns a default UnicodeString mapping using the specified XML name.
	 * Log entries with log level WARN are created when no entry was found.
	 *
	 * @param nestedElementName XML name of the nested element the enum value belogs to
	 * @param enumValue XML name of the enum value
	 * @param fromElement Element dictionary entry to read the mapping from
	 * @return DataItem with CBOR mapping
	 */
	protected final DataItem getNestedElementEnumValueMapping(final String nestedElementName, final String enumValue, final DictionarySimpleElement fromElement) {
		if(fromElement instanceof DictionaryComplexElement) {
			DictionaryComplexElement complexFromElement = (DictionaryComplexElement) fromElement;
			DictionarySimpleElement nestedElement = complexFromElement.lookupNestedElement(nestedElementName);

			if(nestedElement instanceof DictionaryEnumValueElement) {
				DictionaryEnumValueElement enumValueNestedElement = (DictionaryEnumValueElement) nestedElement;

				DataItem rv = enumValueNestedElement.lookupEnumValue(enumValue);

				if(rv != null) {
					return rv;
				}
			}
			else if(nestedElement != null) {
				log.warn("Tried to read enum value from non-enum value dictionary entry. Dictionary corrupted?");
				log.warn("Element: " + nestedElement.getXmlName());
			}
		}

		log.info("No dictionary entry found for element enum value: " + enumValue);
		log.info("Using fallback mapping: UnicodeString(" + enumValue + ")");

		return new UnicodeString(enumValue);
	}
}
