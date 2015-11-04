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
import de.decoit.simu.cbor.ifmap.request.model.publish.AbstractPublishType;
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
 * Java representation of the IF-MAP publish request.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORPublishRequest extends AbstractSessionIdRequest {
	public final static String XML_NAME = "publish";
	
	private final List<AbstractPublishType> publishTypes;
	@Getter
	private ValidationAttributeGroup validationAttributes;


	/**
	 * Create a new publish request with the specified session ID and validation type.
	 *
	 * @param sessionId IF-MAP session ID
	 */
	public CBORPublishRequest(String sessionId) {
		super(IfMapNamespaces.IFMAP, XML_NAME, sessionId);

		this.publishTypes = new ArrayList<>();
		this.validationAttributes = null;
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


	/**
	 * Add a new publish type to this request.
	 *
	 * @param pubType Publish type
	 */
	public void addPublishType(final AbstractPublishType pubType) {
		if(pubType == null) {
			throw new IllegalArgumentException("Publish type must not be null");
		}

		this.publishTypes.add(pubType);
	}


	/**
	 * Remove an existing publish type from this request.
	 *
	 * @param pubType Publish type
	 */
	public void removePublishType(final AbstractPublishType pubType) {
		this.publishTypes.remove(pubType);
	}


	/**
	 * Returns an immutable view of the publish types list.
	 *
	 * @return Immutable list view
	 */
	public List<AbstractPublishType> getPublishTypes() {
		return Collections.unmodifiableList(this.publishTypes);
	}


	@Override
	protected void serializeAttributes(final ArrayBuilder<?> builder) throws CBORSerializationException {
		// Call serializeAttributes on super class
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
		if(this.publishTypes.isEmpty()) {
			throw new CBORSerializationException("Cannot serialize publish request without publish types");
		}

		try {
			// Build dictionary path to this element
			StringBuilder dictPathSb = new StringBuilder("<");
			dictPathSb.append(this.namespace);
			dictPathSb.append(">");
			dictPathSb.append(this.elementName);

			// Get dictionary entry for this element
			DictionarySimpleElement elementEntry = DictionaryProvider.getInstance().findElementByPath(dictPathSb.toString());

			for(AbstractPublishType apt : this.publishTypes) {
				apt.cborSerialize(builder, elementEntry);
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
