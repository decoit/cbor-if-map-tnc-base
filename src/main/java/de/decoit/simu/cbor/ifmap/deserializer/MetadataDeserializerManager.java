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
import de.decoit.simu.cbor.ifmap.deserializer.metadata.AccessRequestDeviceDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.AccessRequestIpDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.AccessRequestMacDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.AuthenticatedAsDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.AuthenticatedByDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.CapabilityDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.ClientTimeDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.DeviceAttributeDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.DeviceCharacteristicDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.DeviceIpDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.DiscoveredByDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.EnforcementReportDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.EventDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.IpMacDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.Layer2InformationDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.LocationDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.RequestForInvestigationDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.RoleDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.ServerCapabilityDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.UnexpectedBehaviorDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.metadata.WlanInformationDeserializer;
import de.decoit.simu.cbor.ifmap.deserializer.vendor.VendorMetadataDeserializer;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.metadata.AbstractMetadata;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORCapability;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORDeviceAttribute;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORDeviceCharacteristic;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBOREnforcementReport;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBOREvent;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORIpMac;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORLayer2Information;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORLocation;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORRequestForInvestigation;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORRole;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORUnexpectedBehavior;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORAccessRequestDevice;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORAccessRequestIp;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORAccessRequestMac;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORAuthenticatedAs;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORAuthenticatedBy;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORClientTime;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORDeviceIp;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORDiscoveredBy;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORServerCapability;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORWlanInformation;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Central class for managing metadata deserializers.
 * Users of this library should never use the methods provided by this class directly except when
 * registering and managing their own custom deserializers.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class MetadataDeserializerManager extends AbstractDeserializerManager {
	private static final HashMap<Class<? extends AbstractMetadata>, VendorMetadataDeserializer<? extends AbstractMetadata>> registeredDeserializers = new HashMap<>();
	private static final HashMap<String, Class<? extends AbstractMetadata>> targetClassMap = new HashMap<>();
	private static final String resolveMapKeySeparator = "?";
	private static boolean initialized = false;
	
	static {
		init();
	}
	

	/**
	 * Deserialize an object of the specified class from the specified data items.
	 * The attributes and nested tags arrays may be empty but never null.
	 *
	 * @param <T> Type of the object to be deserialized, must be a subclass of {@link AbstractMetadata}
	 * @param namespace CBOR data item representing the element namespace
	 * @param cborName CBOR data item representing the element name
	 * @param attributes CBOR array data item containing the element's attributes
	 * @param nestedDataItem CBOR data item containing the element's nested tags or value
	 * @param metadataType Type of the object to be deserialized
	 * @return The deserialized object
	 * @throws CBORDeserializationException if deserialization failed
	 */
	public static <T extends AbstractMetadata> T deserialize(final DataItem namespace, 
															 final DataItem cborName, 
															 final Array attributes, 
															 final DataItem nestedDataItem, 
															 final Class<T> metadataType) throws CBORDeserializationException {
		if(namespace == null) {
			throw new IllegalArgumentException("Namespace must not be null");
		}

		if(cborName == null) {
			throw new IllegalArgumentException("CBOR name must not be null");
		}

		if(attributes == null) {
			throw new IllegalArgumentException("Attributes array must not be null");
		}

		if(nestedDataItem == null) {
			throw new IllegalArgumentException("Nested tags array must not be null");
		}
		
		if(metadataType == null) {
			throw new IllegalArgumentException("Target metadata type must not be null");
		}

		try {
			// If default deserializers were not registered yet, do so
			if(!initialized) {
				init();
			}

			// Check if a deserializer for this type was registered
			if(hasVendorDeserializer(metadataType)) {
				DictionarySimpleElement elementEntry = getTopLevelElement(namespace, cborName);

				return metadataType.cast(registeredDeserializers.get(metadataType).deserialize(attributes, nestedDataItem, elementEntry));
			}

			// If no deserializer was found, fail with exception
			throw new UnsupportedOperationException("Cannot deserialize class: " + metadataType.getCanonicalName());
		}
		catch(CBORDeserializationException ex) {
			throw ex;
		}
		catch(Exception ex) {
			throw new CBORDeserializationException("Could not cast deserialization result to target class", ex);
		}
	}


	/**
	 * Register a deserializer object for vendor specific metadata.
	 * The deserializer class must implement VendorMetadataDeserializer for the type specified
	 * as target class.
	 *
	 * @param <M> Vendor specific metadata type
	 * @param deserializer Deserializer class object
	 * @param targetClass Type of the object to be deserialized
	 * @param namespace Namespace of the registered element
	 * @param elementName Name of the registered element
	 */
	public static <M extends AbstractMetadata> void registerVendorDeserializer(VendorMetadataDeserializer<M> deserializer, 
																			   Class<M> targetClass, 
																			   String namespace, 
																			   String elementName) {
		if(deserializer == null) {
			throw new IllegalArgumentException("VendorMetadataDeserializer object must not be null");
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
	 * Unregister a registered deserializer for vendor specific metadata.
	 *
	 * @param <M> Vendor specific metadata type
	 * @param targetClass Type for which the deserializer was registered
	 */
	public static <M extends AbstractMetadata> void unregisterVendorDeserializer(Class<M> targetClass) {
		registeredDeserializers.remove(targetClass);
	}


	/**
	 * Check a deserializer is registered for the specified target class.
	 *
	 * @param <M> Vendor specific metadata type
	 * @param targetClass Type for which the deserializer was registered
	 * @return true if deserializer was found, false otherwise
	 */
	public static <M extends AbstractMetadata> boolean hasVendorDeserializer(Class<M> targetClass) {
		return registeredDeserializers.containsKey(targetClass);
	}


	/**
	 * Unregister all registered deserializer for vendor specific metadata.
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
	public static Class<? extends AbstractMetadata> resolveTargetClass(String namespace, String elementName) {
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
		registeredDeserializers.put(CBORAccessRequestDevice.class, AccessRequestDeviceDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORAccessRequestDevice.XML_NAME, CBORAccessRequestDevice.class);
		
		registeredDeserializers.put(CBORAccessRequestIp.class, AccessRequestIpDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORAccessRequestIp.XML_NAME, CBORAccessRequestIp.class);
		
		registeredDeserializers.put(CBORAccessRequestMac.class, AccessRequestMacDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORAccessRequestMac.XML_NAME, CBORAccessRequestMac.class);
		
		registeredDeserializers.put(CBORAuthenticatedAs.class, AuthenticatedAsDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORAuthenticatedAs.XML_NAME, CBORAuthenticatedAs.class);
		
		registeredDeserializers.put(CBORAuthenticatedBy.class, AuthenticatedByDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORAuthenticatedBy.XML_NAME, CBORAuthenticatedBy.class);
		
		registeredDeserializers.put(CBORCapability.class, CapabilityDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORCapability.XML_NAME, CBORCapability.class);
		
		registeredDeserializers.put(CBORClientTime.class, ClientTimeDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_OPEARATIONAL_METADATA, CBORClientTime.XML_NAME, CBORClientTime.class);
		
		registeredDeserializers.put(CBORDeviceAttribute.class, DeviceAttributeDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORDeviceAttribute.XML_NAME, CBORDeviceAttribute.class);
		
		registeredDeserializers.put(CBORDeviceCharacteristic.class, DeviceCharacteristicDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORDeviceCharacteristic.XML_NAME, CBORDeviceCharacteristic.class);
		
		registeredDeserializers.put(CBORDeviceIp.class, DeviceIpDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORDeviceIp.XML_NAME, CBORDeviceIp.class);
		
		registeredDeserializers.put(CBORDiscoveredBy.class, DiscoveredByDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORDiscoveredBy.XML_NAME, CBORDiscoveredBy.class);
		
		registeredDeserializers.put(CBOREnforcementReport.class, EnforcementReportDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBOREnforcementReport.XML_NAME, CBOREnforcementReport.class);
		
		registeredDeserializers.put(CBOREvent.class, EventDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBOREvent.XML_NAME, CBOREvent.class);
		
		registeredDeserializers.put(CBORIpMac.class, IpMacDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORIpMac.XML_NAME, CBORIpMac.class);
		
		registeredDeserializers.put(CBORLayer2Information.class, Layer2InformationDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORLayer2Information.XML_NAME, CBORLayer2Information.class);
		
		registeredDeserializers.put(CBORLocation.class, LocationDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORLocation.XML_NAME, CBORLocation.class);
		
		registeredDeserializers.put(CBORRequestForInvestigation.class, RequestForInvestigationDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORRequestForInvestigation.XML_NAME, CBORRequestForInvestigation.class);
		
		registeredDeserializers.put(CBORRole.class, RoleDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORRole.XML_NAME, CBORRole.class);
		
		registeredDeserializers.put(CBORServerCapability.class, ServerCapabilityDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_SERVER, CBORServerCapability.XML_NAME, CBORServerCapability.class);
		
		registeredDeserializers.put(CBORUnexpectedBehavior.class, UnexpectedBehaviorDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORUnexpectedBehavior.XML_NAME, CBORUnexpectedBehavior.class);
		
		registeredDeserializers.put(CBORWlanInformation.class, WlanInformationDeserializer.getInstance());
		registerResolveKey(IfMapNamespaces.IFMAP_METADATA, CBORWlanInformation.XML_NAME, CBORWlanInformation.class);

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
	private static void registerResolveKey(String namespace, String elementName, Class<? extends AbstractMetadata> targetClass) {
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
	private MetadataDeserializerManager() {}
}
