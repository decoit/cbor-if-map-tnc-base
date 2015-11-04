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
import de.decoit.simu.cbor.ifmap.request.model.search.AbstractSubscribeType;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;



/**
 * Java representation of the IF-MAP subscribe request.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORSubscribeRequest extends AbstractSessionIdRequest {
	public final static String XML_NAME = "subscribe";
	
	@Getter
	private ValidationAttributeGroup validationAttributes;
	private final List<AbstractSubscribeType> subscribeTypes;


	/**
	 * Create a new subscribe request.
	 * Attribute for subscribe types is initialized with an empty list. This list MUST be filled with
	 * at least one valid item, otherwise the serialization process will not be successful.
	 *
	 * @param sessionId IF-MAP session ID
	 */
	public CBORSubscribeRequest(String sessionId) {
		super(IfMapNamespaces.IFMAP, XML_NAME, sessionId);

		this.validationAttributes = null;
		this.subscribeTypes = new ArrayList<>();
	}


	/**
	 * Add a new subscription type to this request.
	 *
	 * @param subscribeType Subscription type
	 */
	public void addSubscribeType(AbstractSubscribeType subscribeType) {
		if(subscribeType == null) {
			throw new IllegalArgumentException("Subscibe type must not be null");
		}

		this.subscribeTypes.add(subscribeType);
	}


	/**
	 * Remove an existing subscription type from this request.
	 *
	 * @param subscribeType Subscription type
	 */
	public void removeSubscribeType(AbstractSubscribeType subscribeType) {
		this.subscribeTypes.remove(subscribeType);
	}


	/**
	 * Returns an immutable view of the subscribe types list.
	 *
	 * @return Immutable list view
	 */
	public List<AbstractSubscribeType> getSubscripeTypes() {
		return Collections.unmodifiableList(this.subscribeTypes);
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
		if(this.subscribeTypes.isEmpty()) {
			throw new CBORSerializationException("Cannot serialize subscribe request without subscribe types");
		}

		try {
			// Build dictionary path to this element
			StringBuilder dictPathSb = new StringBuilder("<");
			dictPathSb.append(this.namespace);
			dictPathSb.append(">");
			dictPathSb.append(this.elementName);

			// Get dictionary entry for this element
			DictionarySimpleElement elementEntry = DictionaryProvider.getInstance().findElementByPath(dictPathSb.toString());

			// Serialize subscribe types
			for(AbstractSubscribeType ast : this.subscribeTypes) {
				ast.cborSerialize(builder, elementEntry);
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
