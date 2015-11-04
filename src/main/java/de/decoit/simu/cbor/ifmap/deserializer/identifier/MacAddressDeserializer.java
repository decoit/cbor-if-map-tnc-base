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
package de.decoit.simu.cbor.ifmap.deserializer.identifier;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import de.decoit.simu.cbor.ifmap.deserializer.IdentifierDeserializerManager;
import de.decoit.simu.cbor.ifmap.enums.CBORTags;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.identifier.CBORMacAddress;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.util.List;
import lombok.extern.slf4j.Slf4j;



/**
 * The singleton instance of this class may be used to deserialize identifiers of type {@link CBORMacAddress}.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class MacAddressDeserializer implements InternalIdentifierDeserializer<CBORMacAddress> {
	private static MacAddressDeserializer instance;


	/**
	 * Get the singleton instance of this deserializer.
	 *
	 * @return Deserializer instance
	 */
	public static MacAddressDeserializer getInstance() {
		if(instance == null) {
			instance = new MacAddressDeserializer();
		}

		return instance;
	}


	/**
	 * Private constructor, this is a singleton class.
	 */
	private MacAddressDeserializer() {}


	@Override
	public CBORMacAddress deserialize(final Array attributes, 
									  final Array nestedTags, 
									  final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		if(log.isDebugEnabled()) {
			log.debug("Attributes array: " + attributes);
			log.debug("Nested tags array: " + nestedTags);
			log.debug("Dictionary entry: " + elementDictEntry);
		}

		// Initially define the required variables to build the target object
		byte[] value = null;
		String administrativeDomain = null;

		// Get list of all attribute data items
		List<DataItem> attributesDataItems = attributes.getDataItems();

		// Iterate over the data items in steps of 2
		for(int i=0; i<attributesDataItems.size(); i=i+2) {
			// Get name and value data items
			DataItem attrName = attributesDataItems.get(i);
			DataItem attrValue = attributesDataItems.get(i+1);

			String attrNameStr = IdentifierDeserializerManager.getAttributeXmlName(attrName, elementDictEntry);

			// Process the attribute value
			switch(attrNameStr) {
				case CBORMacAddress.VALUE:
					if(!attrValue.hasTag()) {
						log.warn("'value' attribute of 'mac-address' is not tagged, deserializartion outcome may be undefined");
					}
					else if(!attrValue.getTag().equals(CBORTags.MAC_ADDRESS.getTagDataItem())) {
						log.warn("'value' attribute of 'mac-address' has unknown tag, deserializartion outcome may be undefined");
					}
					value = IdentifierDeserializerManager.processByteStringItem(attrValue, true);
					break;
				case CBORMacAddress.ADMINISTRATIVE_DOMAIN:
					administrativeDomain = IdentifierDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
			}
		}

		// Build return value object
		CBORMacAddress rv = new CBORMacAddress(value);
		rv.setAdministrativeDomain(administrativeDomain);

		return rv;
	}
}
