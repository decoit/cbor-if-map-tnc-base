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
package de.decoit.simu.cbor.ifmap.request;

import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.attributegroup.ValidationAttributeGroup;
import de.decoit.simu.cbor.ifmap.enums.IfMapValidationType;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.attributegroup.SearchTypeAttributeGroup;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;



/**
 * Java representation of the IF-MAP search request.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORSearchRequest extends AbstractSessionIdRequest {
	public final static String XML_NAME = "search";
	
	@Getter
	private final AbstractIdentifier identifier;
	@Getter
	private ValidationAttributeGroup validationAttributes;
	@Getter
	private final SearchTypeAttributeGroup searchTypeAttributes;


	/**
	 * Create a new search request.
	 *
	 * @param sessionId IF-MAP session ID
	 * @param identifier Identifier to start search at
	 */
	public CBORSearchRequest(String sessionId, AbstractIdentifier identifier) {
		super(IfMapNamespaces.IFMAP, XML_NAME, sessionId);

		if(identifier == null) {
			throw new IllegalArgumentException("Identifier must not be null");
		}

		this.identifier = identifier;
		this.validationAttributes = null;
		this.searchTypeAttributes = new SearchTypeAttributeGroup();
	}


	/**
	 *
	 * @param validationType IF-MAP validation type, may be null
	 */
	public void setValidation(IfMapValidationType validationType) {
		if(validationType != null) {
			this.validationAttributes = new ValidationAttributeGroup(validationType);
		}
		else {
			this.validationAttributes = null;
		}
	}


	@Override
	protected void serializeAttributes(final ArrayBuilder<?> builder) throws CBORSerializationException {
		super.serializeAttributes(builder);

		try {
			// Build dictionary path to this element
			StringBuilder dictPathSb = new StringBuilder("<");
			dictPathSb.append(this.namespace);
			dictPathSb.append(">");
			dictPathSb.append(this.elementName);

			// Get dictionary entry for this element
			DictionarySimpleElement elementEntry = DictionaryProvider.getInstance().findElementByPath(dictPathSb.toString());

			// Serialize search paramters
			{
				this.searchTypeAttributes.serializeAttributeGroup(builder, elementEntry);
			}

			// Serialize 'validation'
			if(this.validationAttributes != null) {
				this.validationAttributes.serializeAttributeGroup(builder, elementEntry);
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}


	@Override
	protected void serializeNestedElements(final ArrayBuilder<?> builder) throws CBORSerializationException {
		this.identifier.cborSerialize(builder);
	}
}
