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
package de.decoit.simu.cbor.ifmap.response.model.search;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import de.decoit.simu.cbor.ifmap.AbstractNestedElementBase;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.ifmap.metadata.AbstractMetadata;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;



/**
 * Java representation of a single search or poll result item.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class SearchResultItem extends AbstractNestedElementBase {
	public static final String XML_NAME = "resultItem";
	public static final String METADATA = "metadata";

	@Getter
	private final AbstractIdentifier identifierA;
	@Getter
	private AbstractIdentifier identifierB;
	private final List<AbstractMetadata> metadata;


	/**
	 * Create a new result item.
	 *
	 * @param identifierA
	 */
	public SearchResultItem(AbstractIdentifier identifierA) {
		this(identifierA, null);
	}


	public SearchResultItem(AbstractIdentifier identifierA, AbstractIdentifier identifierB) {
		super(XML_NAME);

		if(identifierA == null) {
			throw new IllegalArgumentException("Identifier A must not be null");
		}

		if(identifierA == null) {
			throw new IllegalArgumentException("Identifier A must be set for result item");
		}

		if(identifierA.equals(identifierB)) {
			throw new IllegalArgumentException("Identifier A must not be equal to identifier B");
		}

		this.identifierA = identifierA;
		this.identifierB = identifierB;
		this.metadata = new ArrayList<>();
	}


	public void setIdentifierB(final AbstractIdentifier identifierB) {
		if(this.identifierA.equals(identifierB)) {
			throw new IllegalArgumentException("Identifier B must not be equal to identifier A");
		}

		this.identifierB = identifierB;
	}


	/**
	 * Add new metadata to this search result item.
	 *
	 * @param metadata Metadata to add
	 */
	public void addMetadata(AbstractMetadata metadata) {
		if(metadata == null) {
			throw new IllegalArgumentException("Metadata must not be null");
		}

		this.metadata.add(metadata);
	}


	/**
	 * Remove metadata from this search result item.
	 *
	 * @param metadata Metadata to remove
	 */
	public void removeMetadata(AbstractMetadata metadata) {
		this.metadata.remove(metadata);
	}


	/**
	 * Remove all metadata from this search result item.
	 */
	public void removeAllMetadata() {
		this.metadata.clear();
	}


	/**
	 * Returns an immutable view of the metadata list.
	 *
	 * @return Immutable list view
	 */
	public List<AbstractMetadata> getMetadata() {
		return Collections.unmodifiableList(this.metadata);
	}


	@Override
	protected void serializeNestedElements(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		if(this.metadata.isEmpty()) {
			throw new CBORSerializationException("Cannot serialize result item with no metadata");
		}

		try {
			// Serialize identifier A
			this.identifierA.cborSerialize(builder);

			// Serialize identifier B
			if(this.identifierB != null) {
				this.identifierB.cborSerialize(builder);
			}


			// Serialize 'metadata'
			DataItem cborName = this.getNestedElementNameMapping(SearchResultItem.METADATA, elementEntry);

			builder.add(new SimpleValue(SimpleValueType.NULL));
			builder.add(cborName);
			builder.addArray();
			ArrayBuilder<?> metadataBuilder = builder.addArray();

			// Serialize metadata objects
			for(AbstractMetadata m : this.metadata) {
				m.cborSerialize(metadataBuilder);
			}
		}
		catch(RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
