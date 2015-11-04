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
import co.nstant.in.cbor.model.DataItem;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.ifmap.enums.IfMapIdentityType;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of an IF-MAP identity identifier.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORIdentity extends AbstractIdentifier {
	public static final String XML_NAME = "identity";
	public static final String NAME = "name";
	public static final String ADMINISTRATIVE_DOMAIN = "administrative-domain";
	public static final String TYPE = "type";
	public static final String OTHER_TYPE_DEFINITION = "other-type-definition";

	@Getter
	private final String name;
	@Getter
	private String administrativeDomain;
	@Getter
	private final IfMapIdentityType type;
	@Getter
	private final String otherTypeDefinition;


	/**
	 * Create a new identity identifier with the specified name and type values.
	 * When using IfMapIdentityType.OTHER as type this constructor will raise an
	 * exception because a value for other-type-definition is required in that case.
	 * Administrative domain is set to null.
	 *
	 * @param name Identity name
	 * @param type Identity type
	 */
	public CBORIdentity(final String name, final IfMapIdentityType type) {
		this(name, type, null);
	}


	/**
	 * Create a new identity identifier with the specified name, type and other-type-definition values.
	 * The other-type-definition parameter is required to be not blank if type is IfMapIdentityType.OTHER.
	 * For all other types the other-type-definition value will be discarded and replaced by null.
	 *
	 * @param name Identity name
	 * @param type Identity type
	 * @param otherTypeDefinition Other type definition if applicable, may be null
	 */
	public CBORIdentity(final String name, final IfMapIdentityType type, String otherTypeDefinition) {
		super(IfMapNamespaces.IFMAP, XML_NAME);

		if(StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Name must not be blank");
		}

		if(type == null) {
			throw new IllegalArgumentException("Type must not be null");
		}

		if(type == IfMapIdentityType.OTHER && StringUtils.isBlank(otherTypeDefinition)) {
			throw new IllegalArgumentException("other-type-definition must not be blank if type is OTHER");
		}
		// No other type definition is required if type is different from OTHER
		else if(type != IfMapIdentityType.OTHER) {
			otherTypeDefinition = null;
		}

		this.name = name;
		this.type = type;
		this.otherTypeDefinition = otherTypeDefinition;
		this.administrativeDomain = null;
	}


	/**
	 * Set an administrative-domain for this identity identifier.
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

			// Serialize 'name'
			{
				DataItem cborName = this.getAttributeNameMapping(CBORIdentity.NAME, elementEntry);

				builder.add(cborName);
				builder.add(this.name);
			}


			// Serialize 'type'
			{
				DataItem cborName = this.getAttributeNameMapping(CBORIdentity.TYPE, elementEntry);
				DataItem cborValue = this.getAttributeEnumValueMapping(CBORIdentity.TYPE, this.type.getXmlName(), elementEntry);

				builder.add(cborName);
				builder.add(cborValue);
			}


			// Serialize 'administrative-domain'
			if(this.administrativeDomain != null) {
				DataItem cborName = this.getAttributeNameMapping(CBORIdentity.ADMINISTRATIVE_DOMAIN, elementEntry);

				builder.add(cborName);
				builder.add(this.administrativeDomain);
			}


			// Serialize 'other-type-definition'
			if(this.otherTypeDefinition != null) {
				DataItem cborName = this.getAttributeNameMapping(CBORIdentity.OTHER_TYPE_DEFINITION, elementEntry);

				builder.add(cborName);
				builder.add(this.otherTypeDefinition);
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
