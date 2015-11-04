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
package de.decoit.simu.cbor.ifmap.request.model.publish;

import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.AbstractNestedElementBase;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;



/**
 * Abstract base class for publish type requests.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public abstract class AbstractPublishType extends AbstractNestedElementBase {
	@Getter
	protected AbstractIdentifier identifierA;
	@Getter
	protected AbstractIdentifier identifierB;


	/**
	 * Create a new publish request type with the specified element name and one or two identifiers.
	 * The parameter for identifier A must be set, identifier B may be null if not required.
	 *
	 * @param elementName Name for the XML element
	 * @param identifierA First identifier to add to this publish request
	 * @param identifierB Second identifier to add to this publish request, may be null
	 */
	public AbstractPublishType(final String elementName, final AbstractIdentifier identifierA, final AbstractIdentifier identifierB) {
		super(elementName);

		if(identifierA == null) {
			throw new IllegalArgumentException("Identifier A must be set for publish request");
		}

		if(identifierA.equals(identifierB)) {
			throw new IllegalArgumentException("Identifier A must not be equal to identifier B");
		}

		this.identifierA = identifierA;
		this.identifierB = identifierB;
	}


	public void setIdentifierB(final AbstractIdentifier identifierB) {
		if(this.identifierA.equals(identifierB)) {
			throw new IllegalArgumentException("Identifier B must not be equal to identifier A");
		}

		this.identifierB = identifierB;
	}


	@Override
	protected void serializeNestedElements(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		try {
			this.identifierA.cborSerialize(builder);

			if(this.identifierB != null) {
				this.identifierB.cborSerialize(builder);
			}
		}
		catch(RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
