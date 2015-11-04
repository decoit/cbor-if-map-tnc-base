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
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import de.decoit.simu.cbor.ifmap.enums.IfMapLifetime;
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
 * Abstract base class for update and notify publish request types.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public abstract class AbstractUpdateNotify extends AbstractPublishType {
	public static final String METADATA = "metadata";
	public static final String LIFETIME = "lifetime";

	protected List<AbstractMetadata> metadata;
	@Getter
	protected IfMapLifetime lifetime;


	/**
	 * Create a new update or notify publish request type with the specified element name.
	 * Lifetime is set to SESSION by default, which is in line with the IF-MAP specification.
	 *
	 * @param elementName Name for the XML element
	 * @param identifierA First identifier to add to this publish request
	 * @param identifierB Second identifier to add to this publish request, may be null
	 */
	public AbstractUpdateNotify(String elementName, AbstractIdentifier identifierA, AbstractIdentifier identifierB) {
		super(elementName, identifierA, identifierB);

		this.metadata = new ArrayList<>();
		this.lifetime = IfMapLifetime.SESSION;
	}


	/**
	 * Add new metadata to this update or notify request.
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
	 * Remove metadata from this update or notify request.
	 *
	 * @param metadata Metadata to remove
	 */
	public void removeMetadata(AbstractMetadata metadata) {
		this.metadata.remove(metadata);
	}


	/**
	 * Remove all metadata from this update or notify request.
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


	public void setLiftime(IfMapLifetime lifetime) {
		if(lifetime == null) {
			throw new IllegalArgumentException("Lifetime must not be null");
		}

		this.lifetime = lifetime;
	}


	@Override
	protected void serializeAttributes(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		try {
			// Serialize 'lifetime'
			{
				DataItem cborName = this.getAttributeNameMapping(AbstractUpdateNotify.LIFETIME, elementEntry);
				DataItem cborValue = this.getAttributeEnumValueMapping(AbstractUpdateNotify.LIFETIME, this.lifetime.getXmlName(), elementEntry);

				builder.add(cborName);
				builder.add(cborValue);
			}
		}
		catch(Exception ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}


	@Override
	protected void serializeNestedElements(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		if(this.metadata.isEmpty()) {
			throw new CBORSerializationException("Cannot serialize update/notify request with no metadata");
		}

		super.serializeNestedElements(builder, elementEntry);

		try {
			// Serialize 'metadata'
			{
				DataItem cborName = this.getNestedElementNameMapping(AbstractUpdateNotify.METADATA, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				ArrayBuilder<?> metadataBuilder = builder.addArray();

				for(AbstractMetadata m : this.metadata) {
					m.cborSerialize(metadataBuilder);
				}
			}
		}
		catch(CBORSerializationException ex) {
			throw ex;
		}
		catch(Exception ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
