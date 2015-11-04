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
package de.decoit.simu.cbor.ifmap.request.model.search;

import de.decoit.simu.cbor.ifmap.attributegroup.SearchTypeAttributeGroup;
import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import lombok.Getter;



/**
 * Java representation of the IF-MAP update subscribe type.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public final class CBORSubscribeUpdate extends AbstractSubscribeType {
	public static final String XML_NAME = "update";
	
	@Getter
	private final AbstractIdentifier identifier;
	@Getter
	private final SearchTypeAttributeGroup searchTypeAttributes;


	/**
	 * Create a new delete subscribe type for the specified subscription name.
	 *
	 * @param name Subsciption name
	 * @param identifier Identifier to start search at
	 */
	public CBORSubscribeUpdate(String name, AbstractIdentifier identifier) {
		super(XML_NAME, name);

		if(identifier == null) {
			throw new IllegalArgumentException("Identifier must not be null");
		}

		this.identifier = identifier;
		this.searchTypeAttributes = new SearchTypeAttributeGroup();
	}


	@Override
	protected void serializeAttributes(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		super.serializeAttributes(builder, elementEntry);

		try {
			// Serialize search paramters
			{
				this.searchTypeAttributes.serializeAttributeGroup(builder, elementEntry);
			}
		}
		catch(CBORSerializationException ex) {
			throw ex;
		}
		catch(Exception ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}


	@Override
	protected void serializeNestedElements(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		this.identifier.cborSerialize(builder);
	}
}
