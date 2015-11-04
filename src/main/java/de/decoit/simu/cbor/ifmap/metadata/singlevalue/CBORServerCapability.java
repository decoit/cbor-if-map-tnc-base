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
package de.decoit.simu.cbor.ifmap.metadata.singlevalue;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.UnicodeString;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of the IF-MAP server-capability operational metadata.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class CBORServerCapability extends AbstractSingleValueMetadata {
	public static final String XML_NAME = "server-capability";
	public static final String CAPABILITY = "capability";

	private final Set<String> capabilities;


	/**
	 * Create a new server-capability metadata.
	 * This constructor should be used for metadata that is sent from the SERVER to the CLIENT. It includes
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamp is stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * Attribute for capability is initialized with an empty set. This set MUST be filled with
	 * at least one valid item, otherwise the serialization process will not be successful.
	 *
	 * @param ifMapPublisherId IF-MAP client publisher ID
	 * @param ifMapTimestamp Timestamp for the metadata
	 */
	public CBORServerCapability(String ifMapPublisherId, ZonedDateTime ifMapTimestamp) {
		super(IfMapNamespaces.IFMAP_SERVER, XML_NAME, ifMapPublisherId, ifMapTimestamp);

		this.capabilities = new HashSet<>();
	}


	/**
	 * Create a new server-capability metadata.
	 * This constructor should be used for metadata that is sent from the CLIENT to the SERVER. It does not include
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamp is stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * Attribute for capability is initialized with an empty set. This set MUST be filled with
	 * at least one valid item, otherwise the serialization process will not be successful.
	 */
	public CBORServerCapability() {
		super(IfMapNamespaces.IFMAP_SERVER, XML_NAME);

		this.capabilities = new HashSet<>();
	}


	/**
	 * Returns an immutable view of the capabilities set.
	 *
	 * @return Immutable set view
	 */
	public Set<String> getCapabilities() {
		return Collections.unmodifiableSet(this.capabilities);
	}


	/**
	 * Add a new capability to the metadata.
	 * The capability string MUST NOT be blank.
	 *
	 * @param capability Capability to add
	 */
	public void addCapability(String capability) {
		if(StringUtils.isBlank(capability)) {
			throw new IllegalArgumentException("Capability must not be blank");
		}

		this.capabilities.add(capability);
	}


	/**
	 * Remove an existing capability from this metadata.
	 *
	 * @param capability Capability to remove
	 */
	public void removeCapability(String capability) {
		this.capabilities.remove(capability);
	}


	/**
	 * Test if this metadata contains the specified capability.
	 *
	 * @param capability Capability to look for
	 * @return Result of Set.contains(capability)
	 */
	public boolean hasCapability(String capability) {
		return this.capabilities.contains(capability);
	}


	@Override
	protected void serializeNestedElements(final ArrayBuilder<?> builder) throws CBORSerializationException {
		if(this.capabilities.isEmpty()) {
			throw new CBORSerializationException("Capabilities set must not be empty");
		}

		try {
			// Build dictionary path to this element
			StringBuilder dictPathSb = new StringBuilder("<");
			dictPathSb.append(this.namespace);
			dictPathSb.append(">");
			dictPathSb.append(this.elementName);

			// Get dictionary entry for this element
			DictionarySimpleElement elementEntry = DictionaryProvider.getInstance().findElementByPath(dictPathSb.toString());

			// Serialize 'capability'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBORServerCapability.CAPABILITY, elementEntry);

				this.capabilities.stream().forEach((c) -> {
					builder.add(new SimpleValue(SimpleValueType.NULL));
					builder.add(cborName);
					builder.addArray();
					builder.add(new UnicodeString(c));
				});
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}


	/**
	 * This class provides a list of TCG defined MAP-Server capabilities via static member variables.
	 */
	public static class TcgDefinedCapabilities {
		public static final String FRACTIONAL_TIMESTAMPS = "fractional-timestamps";
		public static final String EXTENDED_IDENTIFIER_SEARCH_TERMINATION = "extended-identifier-search-termination";
		public static final String MAP_CONTENT_AUTHORIZATION = "map-content-authorization";
		public static final String IFMAP_BASE_VERSION_22 = "ifmap-base-version-2.2";
		public static final String IFMAP_BASE_VERSION_21 = "ifmap-base-version-2.1";
		public static final String IFMAP_BASE_VERSION_20 = "ifmap-base-version-2.0";
		public static final String IFMAP_BASE_VERSION_11 = "ifmap-base-version-1.1";
		public static final String IFMAP_BASE_VERSION_10 = "ifmap-base-version-1.0";


		/**
		 * Private constructor, this class is not meant to be instanciated.
		 */
		private TcgDefinedCapabilities() { }
	}
}
