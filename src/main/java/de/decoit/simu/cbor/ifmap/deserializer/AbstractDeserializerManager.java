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

import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.DoublePrecisionFloat;
import co.nstant.in.cbor.model.HalfPrecisionFloat;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.NegativeInteger;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.SinglePrecisionFloat;
import co.nstant.in.cbor.model.Special;
import co.nstant.in.cbor.model.SpecialType;
import co.nstant.in.cbor.model.Tag;
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.enums.CBORTags;
import de.decoit.simu.cbor.ifmap.util.TimestampHelper;
import de.decoit.simu.cbor.xml.dictionary.DictionaryComplexElement;
import de.decoit.simu.cbor.xml.dictionary.DictionaryEnumValueAttribute;
import de.decoit.simu.cbor.xml.dictionary.DictionaryEnumValueElement;
import de.decoit.simu.cbor.xml.dictionary.DictionaryNamespace;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleAttribute;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;



/**
 * Abstract base class for all deserializer classes.
 * Defines several methods to decode data items of specific types.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public abstract class AbstractDeserializerManager {
	/**
	 * Extract the BigInteger value from a UnsignedInteger data item.
	 * If the data item is null and the required flag is set to true, an
	 * exception will be raised. If the required flag is set to false, the
	 * method will return null.
	 *
	 * @param di Data item to process, may be null if required=false
	 * @param required May di paramter be null or not
	 * @return The extracted BigInteger object
	 * @throws CBORDeserializationException if deserialization failed
	 */
	public static BigInteger processUnsignedIntegerItem(DataItem di, boolean required) throws CBORDeserializationException {
		if(di != null) {
			if(di.getMajorType() == MajorType.UNSIGNED_INTEGER) {
				UnsignedInteger us = (UnsignedInteger) di;
				return us.getValue();
			}
			else {
				throw new CBORDeserializationException("Invalid major type for UnsignedInteger item: " + di.getMajorType().toString());
			}
		}
		else {
			if(required) {
				throw new CBORDeserializationException("Null pointer passed for required item");
			}
			else {
				return null;
			}
		}
	}


	/**
	 * Extract the Double value from any kind of floating point data item.
	 * If the data item is null and the required flag is set to true, an
	 * exception will be raised. If the required flag is set to false, the
	 * method will return null.
	 *
	 * @param di Data item to process, may be null if required=false
	 * @param required May di paramter be null or not
	 * @return The extracted Double object
	 * @throws CBORDeserializationException if deserialization failed
	 */
	public static Double processFloatingPointItem(DataItem di, boolean required) throws CBORDeserializationException {
		if(di != null) {
			if(di.getMajorType() == MajorType.SPECIAL) {
				Special special = (Special) di;
				if(special.getSpecialType() == SpecialType.IEEE_754_DOUBLE_PRECISION_FLOAT) {
					DoublePrecisionFloat dpf = (DoublePrecisionFloat) special;
					return dpf.getValue();
				}
				else if(special.getSpecialType() == SpecialType.IEEE_754_SINGLE_PRECISION_FLOAT) {
					SinglePrecisionFloat spf = (SinglePrecisionFloat) special;
					return (double) spf.getValue();
				}
				else if(special.getSpecialType() == SpecialType.IEEE_754_HALF_PRECISION_FLOAT) {
					HalfPrecisionFloat hpf = (HalfPrecisionFloat) special;
					return (double) hpf.getValue();
				}
				else {
					throw new CBORDeserializationException("Invalid special type for floating point item: " + special.getSpecialType().toString());
				}
			}
			else {
				throw new CBORDeserializationException("Invalid major type for floating point item: " + di.getMajorType().toString());
			}
		}
		else {
			if(required) {
				throw new CBORDeserializationException("Null pointer passed for required item");
			}
			else {
				return null;
			}
		}
	}


	/**
	 * Extract the string value from a UnicodeString data item.
	 * If the data item is null and the required flag is set to true, an
	 * exception will be raised. If the required flag is set to false, the
	 * method will return null.
	 *
	 * @param di Data item to process, may be null if required=false
	 * @param required May di paramter be null or not
	 * @return The extracted string
	 * @throws CBORDeserializationException if deserialization failed
	 */
	public static String processUnicodeStringItem(DataItem di, boolean required) throws CBORDeserializationException {
		if(di != null) {
			if(di.getMajorType() == MajorType.UNICODE_STRING) {
				UnicodeString us = (UnicodeString) di;
				return us.getString();
			}
			else {
				throw new CBORDeserializationException("Invalid major type for UnicodeString item: " + di.getMajorType().toString());
			}
		}
		else {
			if(required) {
				throw new CBORDeserializationException("Null pointer passed for required item");
			}
			else {
				return null;
			}
		}
	}


	/**
	 * Extract the byte array from a ByteString data item.
	 * If the data item is null and the required flag is set to true, an
	 * exception will be raised. If the required flag is set to false, the
	 * method will return null.
	 *
	 * @param di Data item to process, may be null if required=false
	 * @param required May di paramter be null or not
	 * @return The extracted byte array
	 * @throws CBORDeserializationException if deserialization failed
	 */
	public static byte[] processByteStringItem(DataItem di, boolean required) throws CBORDeserializationException {
		if(di != null) {
			if(di.getMajorType() == MajorType.BYTE_STRING) {
				ByteString bs = (ByteString) di;
				return bs.getBytes();
			}
			else {
				throw new CBORDeserializationException("Invalid major type for ByteString item: " + di.getMajorType().toString());
			}
		}
		else {
			if(required) {
				throw new CBORDeserializationException("Null pointer passed for required item");
			}
			else {
				return null;
			}
		}
	}


	/**
	 * Extract the ZonedDateTime value from a timestamp data item.
	 * If the data item is null and the required flag is set to true, an
	 * exception will be raised. If the required flag is set to false, the
	 * method will return null.
	 *
	 * @param di Data item to process, may be null if required=false
	 * @param required May di paramter be null or not
	 * @return The extracted ZonedDateTime object
	 * @throws CBORDeserializationException if deserialization failed
	 */
	public static ZonedDateTime processZonedDateTimeItem(DataItem di, boolean required) throws CBORDeserializationException {
		if(di != null) {
			Tag diTag = null;
			if(di.hasTag()) {
				diTag = di.getTag();
			}

			if(di.getMajorType() == MajorType.UNICODE_STRING) {
				if(diTag == null || CBORTags.fromTagNumber(diTag.getValue()) != CBORTags.DATE_TIME_STRING) {
					log.warn("Data item was not tagged with DATE_TIME_STRING (0L), outcome of deserialization unclear!");
				}

				UnicodeString us = (UnicodeString) di;
				return TimestampHelper.fromXsdDateTime(us.getString());
			}
			else if(di.getMajorType() == MajorType.UNSIGNED_INTEGER) {
				if(diTag == null || CBORTags.fromTagNumber(diTag.getValue()) != CBORTags.DATE_TIME_EPOCH) {
					log.warn("Data item was not tagged with DATE_TIME_EPOCH (1L), outcome of deserialization unclear!");
				}

				UnsignedInteger ui = (UnsignedInteger) di;
				return TimestampHelper.fromEpochTime(ui.getValue().longValueExact());
			}
			else if(di.getMajorType() == MajorType.NEGATIVE_INTEGER) {
				if(diTag == null || CBORTags.fromTagNumber(diTag.getValue()) != CBORTags.DATE_TIME_EPOCH) {
					log.warn("Data item was not tagged with DATE_TIME_EPOCH (1L), outcome of deserialization unclear!");
				}

				NegativeInteger ni = (NegativeInteger) di;
				return TimestampHelper.fromEpochTime(ni.getValue().longValueExact());
			}
			else {
				throw new CBORDeserializationException("Invalid major type for ZonedDateTime item: " + di.getMajorType().toString());
			}
		}
		else {
			if(required) {
				throw new CBORDeserializationException("Null pointer passed for required item");
			}
			else {
				return null;
			}
		}
	}


	/**
	 * Extract the InetAddress value from a ByteString data item.
	 * If the data item is null and the required flag is set to true, an
	 * exception will be raised. If the required flag is set to false, the
	 * method will return null.
	 *
	 * @param di Data item to process, may be null if required=false
	 * @param required May di paramter be null or not
	 * @return The extracted InetAddress object
	 * @throws CBORDeserializationException if deserialization failed
	 */
	public static InetAddress processInetAddressItem(DataItem di, boolean required) throws CBORDeserializationException {
		if(di != null) {
			if(di.getMajorType() == MajorType.BYTE_STRING) {
				try {
					ByteString bs = (ByteString) di;
					return InetAddress.getByAddress(bs.getBytes());
				}
				catch(UnknownHostException ex) {
					throw new CBORDeserializationException("Unable to decode IP address", ex);
				}
			}
			else {
				throw new CBORDeserializationException("Invalid major type for InetAddress item: " + di.getMajorType().toString());
			}
		}
		else {
			if(required) {
				throw new CBORDeserializationException("Null pointer passed for required item");
			}
			else {
				return null;
			}
		}
	}


	/**
	 * Check if a data item is of simple type 'null'.
	 * This means that major type must be SPECIAL, special type must be SIMPLE_VALUE and
	 * simple value type must be NULL.
	 *
	 * @param di Data item to test
	 * @return true if the above criteria match, false otherwise
	 * @throws CBORDeserializationException if passed data item was a null reference
	 */
	public static boolean isSimpleValueNull(DataItem di) throws CBORDeserializationException {
		if(di != null) {
			if(di.getMajorType() == MajorType.SPECIAL) {
				Special specDi = (Special) di;
				if(specDi.getSpecialType() == SpecialType.SIMPLE_VALUE) {
					SimpleValue simpDi = (SimpleValue) specDi;
					return (simpDi.getSimpleValueType() == SimpleValueType.NULL);
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			 throw new CBORDeserializationException("Null pointer passed for simple value 'null' check");
		}
	}


	/**
	 * Try to get a dictionary entry for the specified combination of namespace and element.
	 * For both parameters first a reverse lookup is performed. If that does not return an entry,
	 * the provided data item is checked for its type. If it is UnicodeString, an additional forward lookup
	 * if performed to find an entry. This method returns null if either namespace or element cannot be
	 * resolved as dictionary entries.
	 *
	 * @param namespace CBOR data item specifying the XML namespace
	 * @param elementName CBOR data item specifying the XML element name
	 * @return The resolved dictionary entry or null
	 */
	public static DictionarySimpleElement getTopLevelElement(DataItem namespace, DataItem elementName) {
		DictionaryNamespace nsEntry = DictionaryProvider.getInstance().reverseLookupNamespace(namespace);

		// If reverse lookup did not work, try forward lookup if namespace data item is of type UnicodeString
		if(nsEntry == null) {
			if(namespace.getMajorType() == MajorType.UNICODE_STRING) {
				UnicodeString us = (UnicodeString) namespace;
				String nsString = us.getString();

				nsEntry = DictionaryProvider.getInstance().lookupNamespace(nsString);

				// If namespace cannot be resolved, element name cannot be looked up. Return null.
				if(nsEntry == null) {
					return null;
				}
			}
			// If reverse lookup failed and namespace is not of type UnicodeString, return null
			else {
				return null;
			}
		}

		DictionarySimpleElement elementEntry = nsEntry.reverseLookupElement(elementName);

		// If reverse lookup did not work, try forward lookup if element name data item is of type UnicodeString
		if(elementEntry == null) {
			if(elementName.getMajorType() == MajorType.UNICODE_STRING) {
				UnicodeString us = (UnicodeString) elementName;
				String enString = us.getString();

				elementEntry = nsEntry.lookupElement(enString);
			}
		}

		return elementEntry;
	}
	
	
	/**
	 * Get the XML name of a CBOR encoded namespace.
	 * The method first checks if the namespace can be resolved using the default dictionary. 
	 * If resolving fails, it is checked if the data item if of type UnicodeString. If it is, 
	 * the method suggests that this is the XML name and returns the String value of the data item.
	 *
	 * @param namespace  CBOR data item specifying the namespace
	 * @return The XML name of the attribute
	 * @throws CBORDeserializationException if the namespace cannot be resolved and is not of type UnicodeString
	 */
	public static String getNamespaceXmlName(DataItem namespace) throws CBORDeserializationException {
		DictionaryNamespace nsEntry = DictionaryProvider.getInstance().reverseLookupNamespace(namespace);
		
		if(nsEntry != null) {
			return nsEntry.getXmlName();
		}
		
		if(namespace.getMajorType() == MajorType.UNICODE_STRING) {
			UnicodeString us = (UnicodeString) namespace;

			return us.getString();
		}
		
		throw new CBORDeserializationException("Non-UnicodeString attribute name found without dictionary entry");
	}
	
	
	public static String getTopLevelElementXmlName(DataItem namespace, DataItem element) throws CBORDeserializationException {
		DictionaryNamespace nsEntry = DictionaryProvider.getInstance().reverseLookupNamespace(namespace);
		
		if(nsEntry != null) {
			DictionarySimpleElement elementEntry = nsEntry.reverseLookupElement(element);
			
			if(elementEntry != null) {
				return elementEntry.getXmlName();
			}
		}
		
		if(element.getMajorType() == MajorType.UNICODE_STRING) {
			UnicodeString us = (UnicodeString) element;

			return us.getString();
		}
		
		throw new CBORDeserializationException("Non-UnicodeString attribute name found without dictionary entry");
	}


	/**
	 * Get the XML name of a CBOR encoded attribute name.
	 * The method first checks if fromElement is not null and if the attribute can be resolved
	 * using that provided dictionary entry. If both fails, it is checked if the data item if of
	 * type UnicodeString. If it is, the method suggests that this is the XML name and returns the
	 * String value of the data item.
	 *
	 * @param attributeName CBOR data item specifying the attribute name
	 * @param fromElement Dictionary item for attribute name lookup, may be null
	 * @return The XML name of the attribute
	 * @throws CBORDeserializationException if the attribute cannot be resolved and is not of type UnicodeString
	 */
	public static String getAttributeXmlName(DataItem attributeName, DictionarySimpleElement fromElement) throws CBORDeserializationException {
		if(fromElement != null) {
			DictionarySimpleAttribute attrEntry = fromElement.reverseLookupAttribute(attributeName);

			if(attrEntry != null) {
				return attrEntry.getXmlName();
			}
		}

		if(attributeName.getMajorType() == MajorType.UNICODE_STRING) {
			UnicodeString us = (UnicodeString) attributeName;

			return us.getString();
		}

		throw new CBORDeserializationException("Non-UnicodeString attribute name found without dictionary entry");
	}


	/**
	 * Get the XML name of a CBOR encoded nested tag name.
	 * The method first checks if fromElement is not null and if the nested tag can be resolved
	 * using that provided dictionary entry. If both fails, it is checked if the data item if of
	 * type UnicodeString. If it is, the method suggests that this is the XML name and returns the
	 * String value of the data item.
	 *
	 * @param nestedTag CBOR data item specifying the nested tag name
	 * @param fromElement Dictionary item for nested tag name lookup, may be null
	 * @return The XML name of the nested tag
	 * @throws CBORDeserializationException if the nested tag cannot be resolved and is not of type UnicodeString
	 */
	public static String getNestedTagXmlName(DataItem nestedTag, DictionarySimpleElement fromElement) throws CBORDeserializationException {
		if(fromElement instanceof DictionaryComplexElement) {
			DictionaryComplexElement complexFromElement = (DictionaryComplexElement) fromElement;
			DictionarySimpleElement nestedTagEntry = complexFromElement.reverseLookupNestedElement(nestedTag);

			if(nestedTagEntry != null) {
				return nestedTagEntry.getXmlName();
			}
		}

		if(nestedTag.getMajorType() == MajorType.UNICODE_STRING) {
			UnicodeString us = (UnicodeString) nestedTag;

			return us.getString();
		}

		throw new CBORDeserializationException("Non-UnicodeString nested tag name found without dictionary entry");
	}


	/**
	 * Get the XML name of a CBOR encoded attribute enum value.
	 * The method first checks if fromElement is not null and if the attribute can be resolved
	 * using that provided dictionary entry. Then it is checked if the enum value can be resolved using
	 * that attribute entry. If all these steps fail, it is checked if the enum value data item if of
	 * type UnicodeString. If it is, the method suggests that this is the XML name and returns the
	 * String value of the enum value data item.
	 *
	 * @param attributeName CBOR data item specifying the attribute name
	 * @param enumValue CBOR data item specifying the enum value
	 * @param fromElement Dictionary item for attribute name lookup, may be null
	 * @return The XML name of the enum value
	 * @throws CBORDeserializationException if the enum value cannot be resolved and is not of type UnicodeString
	 */
	public static String getAttributeEnumValueXmlName(DataItem attributeName, DataItem enumValue, DictionarySimpleElement fromElement) throws CBORDeserializationException {
		if(fromElement != null) {
			DictionarySimpleAttribute attrEntry = fromElement.reverseLookupAttribute(attributeName);

			if(attrEntry instanceof DictionaryEnumValueAttribute) {
				DictionaryEnumValueAttribute enumAttrEntry = (DictionaryEnumValueAttribute) attrEntry;
				String enumValueName = enumAttrEntry.reverseLookupEnumValue(enumValue);

				if(enumValueName != null) {
					return enumValueName;
				}
			}
		}

		if(enumValue.getMajorType() == MajorType.UNICODE_STRING) {
			UnicodeString us = (UnicodeString) enumValue;

			return us.getString();
		}

		throw new CBORDeserializationException("Non-UnicodeString enum value found without dictionary entry");
	}
	
	
	/**
	 * Get the XML name of a CBOR encoded nested tag enum value.
	 * The method first checks if fromElement is not null and if the nested tag can be resolved
	 * using that provided dictionary entry. Then it is checked if the enum value can be resolved using
	 * that nested tag entry. If any of these steps fail, it is checked if the enum value data item if of
	 * type UnicodeString. If it is, the method suggests that this is the XML name and returns the
	 * String value of the enum value data item.
	 *
	 * @param nestedTag CBOR data item specifying the nested tag name
	 * @param enumValue CBOR data item specifying the enum value
	 * @param fromElement Dictionary item for nested tag name lookup, may be null
	 * @return The XML name of the enum value
	 * @throws CBORDeserializationException if the enum value cannot be resolved and is not of type UnicodeString
	 */
	public static String getNestedTagEnumValueXmlName(DataItem nestedTag, DataItem enumValue, DictionarySimpleElement fromElement) throws CBORDeserializationException {
		if(fromElement instanceof DictionaryComplexElement) {
			DictionaryComplexElement complexFromElement = (DictionaryComplexElement) fromElement;
			DictionarySimpleElement nestedTagEntry = complexFromElement.reverseLookupNestedElement(nestedTag);
			
			if(nestedTagEntry instanceof DictionaryEnumValueElement) {
				DictionaryEnumValueElement enumAttrEntry = (DictionaryEnumValueElement) nestedTagEntry;
				String enumValueName = enumAttrEntry.reverseLookupEnumValue(enumValue);

				if(enumValueName != null) {
					return enumValueName;
				}
			}
		}

		if(enumValue.getMajorType() == MajorType.UNICODE_STRING) {
			UnicodeString us = (UnicodeString) enumValue;

			return us.getString();
		}

		throw new CBORDeserializationException("Non-UnicodeString enum value found without dictionary entry");
	}
	
	
	/**
	 * Get the XML name of a CBOR encoded element enum value.
	 * The method first checks if fromElement is not null and if the enum value can be resolved using
	 * that element entry. If any of these steps fail, it is checked if the enum value data item if of
	 * type UnicodeString. If it is, the method suggests that this is the XML name and returns the
	 * String value of the enum value data item.
	 * 
	 * @param enumValue CBOR data item specifying the enum value
	 * @param fromElement Dictionary item for enum value lookup, may be null
	 * @return The XML name of the enum value
	 * @throws CBORDeserializationException if the enum value cannot be resolved and is not of type UnicodeString
	 */
	public static String getElementEnumValueXmlName(DataItem enumValue, DictionarySimpleElement fromElement) throws CBORDeserializationException {
		if(fromElement != null) {
			if(fromElement instanceof DictionaryEnumValueElement) {
				DictionaryEnumValueElement enumElementEntry = (DictionaryEnumValueElement) fromElement;
				String enumValueName = enumElementEntry.reverseLookupEnumValue(enumValue);
				
				if(enumValueName != null) {
					return enumValueName;
				}
			}
		}
		
		if(enumValue.getMajorType() == MajorType.UNICODE_STRING) {
			UnicodeString us = (UnicodeString) enumValue;

			return us.getString();
		}

		throw new CBORDeserializationException("Non-UnicodeString enum value found without dictionary entry");
	}
}
