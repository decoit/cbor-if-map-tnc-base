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
package de.decoit.simu.cbor.ifmap.metadata.multivalue;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.time.ZonedDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of the IF-MAP layer2-information metadata.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class CBORLayer2Information extends AbstractMultiValueMetadata {
	public static final String XML_NAME = "layer2-information";
	public static final String VLAN = "vlan";
	public static final String VLAN_NAME = "vlan-name";
	public static final String PORT = "port";
	public static final String ADMINISTRATIVE_DOMAIN = "administrative-domain";

	@Getter
	private Integer vlan;
	@Getter
	private String vlanName;
	@Getter
	private Integer port;
	@Getter
	private String administrativeDomain;


	/**
	 * Create a new layer2-information metadata.
	 * This constructor should be used for metadata that is sent from the SERVER to the CLIENT. It includes
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamps are stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.
	 *
	 * @param ifMapPublisherId IF-MAP client publisher ID
	 * @param ifMapTimestamp Timestamp for the metadata
	 */
	public CBORLayer2Information(String ifMapPublisherId, ZonedDateTime ifMapTimestamp) {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME, ifMapPublisherId, ifMapTimestamp);

		this.vlan = null;
		this.vlanName = null;
		this.port = null;
		this.administrativeDomain = null;
	}


	/**
	 * Create a new layer2-information metadata.
	 * This constructor should be used for metadata that is sent from the CLIENT to the SERVER. It does not include
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamps are stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.
	 */
	public CBORLayer2Information() {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME);

		this.vlan = null;
		this.vlanName = null;
		this.port = null;
		this.administrativeDomain = null;
	}


	/**
	 * Set a new VLAN ID for this metadata.
	 * The VLAN ID may be null to remove this value from the metadata.
	 *
	 * @param vlan VLAN ID
	 */
	public void setVlan(Integer vlan) {
		if(vlan != null && (vlan < 1 || vlan > 4094)) {
			throw new IllegalArgumentException("VLAN ID must be a number from 1 to 4094");
		}

		this.vlan = vlan;
	}


	/**
	 * Set the VLAN name for this metadata.
	 * The VLAN name may be null to remove this value from the metadata. If not null,
	 * the VLAN name MUST NOT be an empty string or whitespace only.
	 *
	 * @param vlanName VLAN name for this metadata
	 */
	public void setVlanName(String vlanName) {
		if(StringUtils.isWhitespace(vlanName)) {
			throw new IllegalArgumentException("VLAN name must not be empty or whitespace only");
		}

		this.vlanName = vlanName;
	}


	/**
	 * Set the port number for this metadata.
	 * The port number may be null to remove this value from the metadata. If not null,
	 * the port number MUST NOT be less than 0.
	 *
	 * @param port Port number
	 */
	public void setPort(Integer port) {
		if(port != null && (port < 0 || port > 65535)) {
			throw new IllegalArgumentException("Port must be a number from 0 to 65535");
		}

		this.port = port;
	}


	/**
	 * Set the administrative-domain for this metadata.
	 * The administrative-domain may be null to remove this value from the metadata. If not null,
	 * the administrative-domain MUST NOT be an empty string or whitespace only.
	 *
	 * @param administrativeDomain administrative-domain for this metadata
	 */
	public void setAdministrativeDomain(String administrativeDomain) {
		if(StringUtils.isWhitespace(administrativeDomain)) {
			throw new IllegalArgumentException("administrative-domain must not be empty or whitespace only");
		}

		this.administrativeDomain = administrativeDomain;
	}


	@Override
	protected void serializeNestedElements(final ArrayBuilder<?> builder) throws CBORSerializationException {
		try {
			// Build dictionary path to this element
			StringBuilder dictPathSb = new StringBuilder("<");
			dictPathSb.append(this.namespace);
			dictPathSb.append(">");
			dictPathSb.append(this.elementName);

			// Get dictionary entry for this element
			DictionarySimpleElement elementEntry = DictionaryProvider.getInstance().findElementByPath(dictPathSb.toString());

			// Serialize 'vlan'
			if(this.vlan != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORLayer2Information.VLAN, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnsignedInteger(this.vlan));
			}


			// Serialize 'vlan-name'
			if(this.vlanName != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORLayer2Information.VLAN_NAME, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.vlanName));
			}


			// Serialize 'port'
			if(this.port != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORLayer2Information.PORT, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnsignedInteger(this.port));
			}


			// Serialize 'administrative-domain'
			if(this.administrativeDomain != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORLayer2Information.ADMINISTRATIVE_DOMAIN, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.administrativeDomain));
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
