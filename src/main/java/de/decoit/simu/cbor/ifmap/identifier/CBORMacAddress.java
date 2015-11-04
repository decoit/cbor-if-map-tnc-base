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
package de.decoit.simu.cbor.ifmap.identifier;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import de.decoit.simu.cbor.ifmap.enums.CBORTags;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import javax.xml.bind.DatatypeConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of an IF-MAP mac-address identifier.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORMacAddress extends AbstractIdentifier {
	public static final String XML_NAME = "mac-address";
	public static final String VALUE = "value";
	public static final String ADMINISTRATIVE_DOMAIN = "administrative-domain";

	@Getter
	private byte[] value;
	@Getter
	private String administrativeDomain;


	/**
	 * Create a new mac-address identifier representing the provided MAC address.
	 * Bytes of the MAC address will be treated as network byte order (big endian).
	 *
	 * @param value Raw bytes of MAC address
	 */
	public CBORMacAddress(byte[] value) {
		super(IfMapNamespaces.IFMAP, XML_NAME);

		this.processByteArray(value);
	}


	/**
	 * Create a new mac-address identifier representing the provided MAC address.
	 * Bytes of the MAC address will be treated as network byte order (big endian).
	 * The string must represent a valid MAC address and must use one of the
	 * following formats:<br>
	 * - 00-80-41-ae-fd-7e<br>
	 * - 00:80:41:ae:fd:7e<br>
	 * - 0080.41ae.fd7e<br>
	 * - 008041aefd7e
	 *
	 * @param value String representation of MAC address
	 */
	public CBORMacAddress(String value) {
		super(IfMapNamespaces.IFMAP, XML_NAME);

		byte[] address = this.processStringRepresentation(value);
		this.processByteArray(address);
	}


	/**
	 * Set an administrative-domain for this ip-address identifier.
	 * The administrative-domain may be null to remove this value from the metadata. If not null,
	 * the administrative-domain MUST NOT be an empty string or whitespace only.
	 *
	 * @param administrativeDomain administrative-domain value, may be null
	 */
	public void setAdministrativeDomain(String administrativeDomain) {
		if(StringUtils.isWhitespace(administrativeDomain)) {
			throw new IllegalArgumentException("Administrative domain must not be empty or whitespace only");
		}

		this.administrativeDomain = administrativeDomain;
	}


	@Override
	protected void serializeAttributes(final ArrayBuilder<?> builder) throws CBORSerializationException {
		try {
			// Build dictionary path to this element
			StringBuilder dictPathSb = new StringBuilder("<");
			dictPathSb.append(this.namespace);
			dictPathSb.append(">");
			dictPathSb.append(this.elementName);

			// Get dictionary entry for this element
			DictionarySimpleElement elementEntry = DictionaryProvider.getInstance().findElementByPath(dictPathSb.toString());

			// Serialize 'value'
			{
				DataItem cborName = this.getAttributeNameMapping(CBORMacAddress.VALUE, elementEntry);

				DataItem valueDi = new ByteString(this.value);
				valueDi.setTag(CBORTags.MAC_ADDRESS.getTagDataItem());

				builder.add(cborName);
				builder.add(valueDi);
			}


			// Serialize 'administrative-domain'
			if(this.administrativeDomain != null) {
				DataItem cborName = this.getAttributeNameMapping(CBORMacAddress.ADMINISTRATIVE_DOMAIN, elementEntry);

				builder.add(cborName);
				builder.add(this.administrativeDomain);
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}


	/**
	 * Parse the string representation of a MAC address into a byte array.
	 *
	 * @param value String representation of MAC address
	 * @return Raw bytes of MAC address
	 */
	private byte[] processStringRepresentation(String value) {
		if(StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("Value string may not be blank");
		}

		// Remove seprators and leading and trailing whitespaces
		String strippedAddress;
		if(value.contains("-")) {
			strippedAddress = StringUtils.replace(value, "-", "");
		}
		else if(value.contains(":")) {
			strippedAddress = StringUtils.replace(value, ":", "");
		}
		else if(value.contains(".")) {
			strippedAddress = StringUtils.replace(value, ".", "");
		}
		else {
			strippedAddress = value;
		}

		byte[] address = DatatypeConverter.parseHexBinary(strippedAddress);

		if(address.length != 6) {
			throw new IllegalArgumentException("Invalid number of bytes for MAC address: " + address.length);
		}

		return address;
	}


	/**
	 * Validate and store the raw MAC address bytes.
	 *
	 * @param value Raw bytes of MAC address
	 */
	private void processByteArray(byte[] value) {
		if(value == null) {
			throw new IllegalArgumentException("Value byte array may not be null");
		}

		if(value.length != 6) {
			throw new IllegalArgumentException("Invalid number of bytes for MAC address: " + value.length);
		}

		this.value = value;
	}
}
