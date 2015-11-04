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
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnicodeString;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of the delete update request type.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORPublishDelete extends AbstractPublishType {
	public static final String XML_NAME = "delete";
	public static final String FILTER = "filter";

	@Getter
	private String filter;


	/**
	 * Create a new delete publish type object with one identifier.
	 *
	 * @param identifierA Identifier to add to this publish request
	 */
	public CBORPublishDelete(AbstractIdentifier identifierA) {
		this(identifierA, null);
	}


	/**
	 * Create a new delete publish type object with one or two identifiers.
	 *
	 * @param identifierA First identifier to add to this publish request
	 * @param identifierB Second identifier to add to this publish request, may be null
	 */
	public CBORPublishDelete(AbstractIdentifier identifierA, AbstractIdentifier identifierB) {
		super(XML_NAME, identifierA, identifierB);

		this.filter = null;
	}


	/**
	 * Set the filter string for this delete request.
	 * The filter may be null to remove it from this request. If not null,
	 * the filter MUST NOT be an empty string or whitespace only.
	 *
	 * @param filter Filter string
	 */
	public void setFilter(String filter) {
		if(StringUtils.isWhitespace(filter)) {
			throw new IllegalArgumentException("Filter must not be empty or whitespace only");
		}

		this.filter = filter;
	}


	@Override
	protected void serializeAttributes(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		try {
			// Serialize 'filter'
			if(this.filter != null) {
				DataItem cborName = this.getAttributeNameMapping(CBORPublishDelete.FILTER, elementEntry);

				builder.add(cborName);
				builder.add(new UnicodeString(this.filter));
			}
		}
		catch(Exception ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
