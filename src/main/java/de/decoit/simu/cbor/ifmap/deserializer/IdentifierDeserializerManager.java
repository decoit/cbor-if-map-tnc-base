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
package de.decoit.simu.cbor.ifmap.deserializer;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import de.decoit.simu.cbor.ifmap.deserializer.identifier.AccessRequestDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.identifier.DeviceDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.identifier.IdentityDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.identifier.InternalIdentifierDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.identifier.IpAddressDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.identifier.MacAddressDeserializer;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.ifmap.identifier.CBORAccessRequest;
import de.decoit.simu.cbor.ifmap.identifier.CBORDevice;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import de.decoit.simu.cbor.ifmap.identifier.CBORIpAddress;
import de.decoit.simu.cbor.ifmap.identifier.CBORMacAddress;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Central class for managing identifier deserializers.
 * Users of this library should never use the methods provided by this class directly.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class IdentifierDeserializerManager extends AbstractDeserializerManager {
	private static final HashMap<Class<? extends AbstractIdentifier>, InternalIdentifierDeserializer<? extends AbstractIdentifier>> registeredDeserializers = new HashMap<>();
	private static final HashMap<String, Class<? extends AbstractIdentifier>> targetClassMap = new HashMap<>();
	private static final String resolveMapKeySeparator = "?";
	private static boolean initialized = false;
	
	static {
		init();
	}


	/**
	 * Deserialize an object of the specified class from the specified data items.
	 * The attributes and nested tags arrays may be empty but never null.
	 *
	 * @param <T> Type of the object to be deserialized, must be a subclass of {@link AbstractIdentifier}
	 * @param namespace CBOR data item representing the element namespace
	 * @param cborName CBOR data item representing the element name
	 * @param attributes CBOR array data item containing the element's attributes
	 * @param nestedTags CBOR array data item containing the element's nested tags
	 * @param identifierType Type of the object to be deserialized
	 * @return The deserialized object
	 * @throws CBORDeserializationException if deserialization failed
	 */
	public static <T extends AbstractIdentifier> T deserialize(final DataItem namespace, 
															   final DataItem cborName, 
															   final Array attributes, 
															   final Array nestedTags, 
															   final Class<T> identifierType) throws CBORDeserializationException {
		if(namespace == null) {
			throw new IllegalArgumentException("Namespace must not be null");
		}

		if(cborName == null) {
			throw new IllegalArgumentException("CBOR name must not be null");
		}

		if(attributes == null) {
			throw new IllegalArgumentException("Attributes array must not be null");
		}

		if(nestedTags == null) {
			throw new IllegalArgumentException("Nested tags array must not be null");
		}
		
		if(identifierType == null) {
			throw new IllegalArgumentException("Target identifier type must not be null");
		}

		try {
			// If default deserializers were not registered yet, do so
			if(!initialized) {
				init();
			}

			// Check if a deserializer for this type was registered
			if(registeredDeserializers.containsKey(identifierType)) {
				DictionarySimpleElement elementEntry = getTopLevelElement(namespace, cborName);

				return identifierType.cast(registeredDeserializers.get(identifierType).deserialize(attributes, nestedTags, elementEntry));
			}

			// If no deserializer was found, fail with exception
			throw new UnsupportedOperationException("Cannot deserialize class: " + identifierType.getCanonicalName());
		}
		catch(RuntimeException ex) {
			throw new CBORDeserializationException("RuntimeException during deserialization, see nested exception"
												   + "for details", ex);
		}
	}
	
	
	/**
	 * Get the domain class Class object of the element defined by the specified namespace and element name.
	 * 
	 * @param namespace Namespace of the element to resolve
	 * @param elementName Name of the element to resolve
	 * @return Class object of the domain class
	 */
	public static Class<? extends AbstractIdentifier> resolveTargetClass(String namespace, String elementName) {
		if(StringUtils.isBlank(namespace)) {
			throw new IllegalArgumentException("Namespace must not be blank");
		}
		
		if(StringUtils.isBlank(elementName)) {
			throw new IllegalArgumentException("Element name must not be blank");
		}
		
		String mapKey = namespace + resolveMapKeySeparator + elementName;
		
		return targetClassMap.get(mapKey);
	}


	/**
	 * Initialize this deserializer class.
	 * This means that all default deserializers are registered for usage.
	 */
	private static void init() {
		registeredDeserializers.put(CBORAccessRequest.class, AccessRequestDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP, "access-request", CBORAccessRequest.class);
		
		registeredDeserializers.put(CBORDevice.class, DeviceDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP, "device", CBORDevice.class);
		
		registeredDeserializers.put(CBORIdentity.class, IdentityDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP, "identity", CBORIdentity.class);
		
		registeredDeserializers.put(CBORIpAddress.class, IpAddressDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP, "ip-address", CBORIpAddress.class);
		
		registeredDeserializers.put(CBORMacAddress.class, MacAddressDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP, "mac-address", CBORMacAddress.class);

		initialized = true;
	}
	
	
	private static void registerResolveKey(String namespace, String elementName, Class<? extends AbstractIdentifier> targetClass) {
		if(StringUtils.isBlank(namespace)) {
			throw new IllegalArgumentException("Namespace must not be blank");
		}
		
		if(StringUtils.isBlank(elementName)) {
			throw new IllegalArgumentException("Element name must not be blank");
		}
		
		if(targetClass == null) {
			throw new IllegalArgumentException("Target class must not be null");
		}
		
		String mapKey = namespace + resolveMapKeySeparator + elementName;
		
		targetClassMap.put(mapKey, targetClass);
	}


	/**
	 * Private constructor, this class is not meant to be instanciated.
	 */
	private IdentifierDeserializerManager() {}
}
