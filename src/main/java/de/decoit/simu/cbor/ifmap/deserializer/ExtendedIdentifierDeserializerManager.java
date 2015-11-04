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
import de.decoit.simu.cbor.ifmap.deserializer.identifier.extended.IfMapServerDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.vendor.VendorIdentifierDeserializer;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.identifier.extended.AbstractExtendedIdentifier;
import de.decoit.simu.cbor.ifmap.identifier.extended.CBORExtendedIfMapServer;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Central class for managing extended identifier deserializers.
 * Users of this library should never use the methods provided by this class directly except when
 * registering and managing their own custom deserializers.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class ExtendedIdentifierDeserializerManager extends AbstractDeserializerManager {
	private static final HashMap<Class<? extends AbstractExtendedIdentifier>, VendorIdentifierDeserializer<? extends AbstractExtendedIdentifier>> registeredDeserializers = new HashMap<>();
	private static final HashMap<String, Class<? extends AbstractExtendedIdentifier>> targetClassMap = new HashMap<>();
	private static final String resolveMapKeySeparator = "?";
	private static boolean initialized = false;
	
	static {
		init();
	}


	/**
	 * Deserialize an object of the specified class from the specified data items.
	 * The attributes and nested tags arrays may be empty but never null.
	 *
	 * @param <T> Type of the object to be deserialized, must be a subclass of {@link AbstractExtendedIdentifier}
	 * @param namespace CBOR data item representing the element namespace
	 * @param cborName CBOR data item representing the element name
	 * @param attributes CBOR array data item containing the element's attributes
	 * @param nestedTags CBOR array data item containing the element's nested tags
	 * @param identifierType Type of the object to be deserialized
	 * @return The deserialized object
	 * @throws CBORDeserializationException if deserialization failed
	 */
	public static <T extends AbstractExtendedIdentifier> T deserialize(final DataItem namespace, 
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
			if(hasVendorDeserializer(identifierType)) {
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
	 * Register a deserializer object for vendor specific extended identifiers.
	 * The deserializer class must implement VendorIdentifierDeserializer for the type specified
	 * as target class.
	 *
	 * @param <M> Vendor specific identifier type
	 * @param deserializer Deserializer class object
	 * @param targetClass Type of the object to be deserialized
	 * @param namespace Namespace of the registered element
	 * @param elementName Name of the registered element
	 */
	public static <M extends AbstractExtendedIdentifier> void registerVendorDeserializer(VendorIdentifierDeserializer<M> deserializer, 
																						 Class<M> targetClass, 
																						 String namespace, 
																						 String elementName) {
		if(deserializer == null) {
			throw new IllegalArgumentException("VendorIdentifierDeserializer object must not be null");
		}

		if(targetClass == null) {
			throw new IllegalArgumentException("Target class must not be null");
		}

		if(registeredDeserializers.containsKey(targetClass)) {
			throw new IllegalStateException("Deserializer already registered for " + targetClass.getCanonicalName());
		}

		registerResolveKey(namespace, elementName, targetClass);
		registeredDeserializers.put(targetClass, deserializer);
	}


	/**
	 * Unregister a registered deserializer for vendor specific extended identifiers.
	 *
	 * @param <M> Vendor specific identifier type
	 * @param targetClass Type for which the deserializer was registered
	 */
	public static <M extends AbstractExtendedIdentifier> void unregisterVendorDeserializer(Class<M> targetClass) {
		registeredDeserializers.remove(targetClass);
	}


	/**
	 * Check a deserializer is registered for the specified target class.
	 *
	 * @param <M> Vendor specific identifier type
	 * @param targetClass Type for which the deserializer was registered
	 * @return true if deserializer was found, false otherwise
	 */
	public static <M extends AbstractExtendedIdentifier> boolean hasVendorDeserializer(Class<M> targetClass) {
		return registeredDeserializers.containsKey(targetClass);
	}


	/**
	 * Unregister all registered deserializer for vendor specific extended identifiers.
	 * This operation sets the initialized flag to false which will cause all deserializers of the IF-MAP base
	 * package to be registered again when queried.
	 */
	public static void clearAllVendorDeserializers() {
		registeredDeserializers.clear();
		
		initialized = false;
	}
	
	
	/**
	 * Get the domain class Class object of the element defined by the specified namespace and element name.
	 * 
	 * @param namespace Namespace of the element to resolve
	 * @param elementName Name of the element to resolve
	 * @return Class object of the domain class
	 */
	public static Class<? extends AbstractExtendedIdentifier> resolveTargetClass(String namespace, String elementName) {
		if(StringUtils.isBlank(namespace)) {
			throw new IllegalArgumentException("Namespace must not be blank");
		}
		
		if(StringUtils.isBlank(elementName)) {
			throw new IllegalArgumentException("Element name must not be blank");
		}
		
		// If default deserializers were not registered yet, do so
		if(!initialized) {
			init();
		}
		
		String mapKey = namespace + resolveMapKeySeparator + elementName;
		
		return targetClassMap.get(mapKey);
	}


	/**
	 * Initialize this deserializer class.
	 * This means that all default deserializers are registered for usage.
	 */
	private static void init() {
		registeredDeserializers.put(CBORExtendedIfMapServer.class, IfMapServerDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_SERVER, CBORExtendedIfMapServer.XML_NAME, CBORExtendedIfMapServer.class);

		initialized = true;
	}
	
	
	/**
	 * Register a target class resolve key with this deserializer.
	 * The key is generated of the namespace and name of the target element. It is required to look up {@link Class}
	 * objects representing target element when deserializing.
	 * 
	 * @param namespace Namespace of the target element
	 * @param elementName Name of the target element
	 * @param targetClass The {@link Class} object to be registered for this element
	 */
	private static void registerResolveKey(String namespace, String elementName, Class<? extends AbstractExtendedIdentifier> targetClass) {
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
	private ExtendedIdentifierDeserializerManager() {}
}
