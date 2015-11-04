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
import de.decoit.simu.cbor.ifmap.enums.IfMapIpAddressType;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of an IF-MAP ip-address identifier.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORIpAddress extends AbstractIdentifier {
	public static final String XML_NAME = "ip-address";
	public static final String VALUE = "value";
	public static final String ADMINISTRATIVE_DOMAIN = "administrative-domain";
	public static final String TYPE = "type";

	@Getter
	private InetAddress value;
	@Getter
	private IfMapIpAddressType type;
	@Getter
	private String administrativeDomain;


	/**
	 * Create a new ip-address identifier that represents the provided IP address.
	 * IP address type (IPv4 or IPv6) will be determined by checking the
	 * type of the provided InetAddress object.
	 *
	 * @param value IP address to represent
	 */
	public CBORIpAddress(InetAddress value) {
		super(IfMapNamespaces.IFMAP, XML_NAME);

		this.processInetAddress(value);

		this.administrativeDomain = null;
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
				DataItem cborName = this.getAttributeNameMapping(CBORIpAddress.VALUE, elementEntry);

				DataItem valueDi = new ByteString(this.value.getAddress());
				if(this.type == IfMapIpAddressType.IPV4) {
					valueDi.setTag(CBORTags.IPV4_ADDRESS.getTagDataItem());
				}
				else {
					valueDi.setTag(CBORTags.IPV6_ADDRESS.getTagDataItem());
				}

				builder.add(cborName);
				builder.add(valueDi);
			}


			// Serialize 'type'
			{
				DataItem cborName = this.getAttributeNameMapping(CBORIpAddress.TYPE, elementEntry);
				DataItem cborValue = this.getAttributeEnumValueMapping(CBORIpAddress.TYPE, this.type.getXmlName(), elementEntry);

				builder.add(cborName);
				builder.add(cborValue);
			}


			// Serialize 'administrative-domain'
			if(this.administrativeDomain != null) {
				DataItem cborName = this.getAttributeNameMapping(CBORIpAddress.ADMINISTRATIVE_DOMAIN, elementEntry);

				builder.add(cborName);
				builder.add(this.administrativeDomain);
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}


	private void processInetAddress(InetAddress value) {
		if(value == null) {
			throw new IllegalArgumentException("value parameter must not be null");
		}

		IfMapIpAddressType localType;
		if(value instanceof Inet4Address) {
			localType = IfMapIpAddressType.IPV4;
		}
		else if(value instanceof Inet6Address) {
			localType = IfMapIpAddressType.IPV6;
		}
		else {
			throw new IllegalArgumentException("Unkown type for value parameter: " + value.getClass().getName());
		}

		this.value = value;
		this.type = localType;
	}
}
