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
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of an IF-MAP access-request identifier.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORAccessRequest extends AbstractIdentifier {
	public static final String XML_NAME = "access-request";
	public static final String NAME = "name";
	public static final String ADMINISTRATIVE_DOMAIN = "administrative-domain";

	@Getter
	private final String name;
	@Getter
	private String administrativeDomain;


	/**
	 * Create a new access-request identifier using the specified name.
	 *
	 * @param name Access request name
	 */
	public CBORAccessRequest(final String name) {
		super(IfMapNamespaces.IFMAP, XML_NAME);

		if(StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Name must not be blank");
		}

		this.name = name;
		this.administrativeDomain = null;
	}


	/**
	 * Set an administrative-domain for this identity identifier.
	 * The administrative-domain may be null to remove this value from the metadata. If not null,
	 * the administrative-domain MUST NOT be an empty string or whitespace only.
	 *
	 * @param administrativeDomain administrative-domain value, may be null
	 */
	public void setAdministrativeDomain(final String administrativeDomain) {
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
				DataItem cborName = this.getAttributeNameMapping(CBORAccessRequest.NAME, elementEntry);

				builder.add(cborName);
				builder.add(this.name);
			}


			// Serialize 'administrative-domain'
			if(this.administrativeDomain != null) {
				DataItem cborName = this.getAttributeNameMapping(CBORAccessRequest.ADMINISTRATIVE_DOMAIN, elementEntry);

				builder.add(cborName);
				builder.add(this.administrativeDomain);
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
