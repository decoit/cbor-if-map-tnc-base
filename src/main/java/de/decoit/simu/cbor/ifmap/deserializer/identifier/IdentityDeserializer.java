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
import de.decoit.simu.cbor.ifmap.enums.IfMapIdentityType;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.util.List;
import lombok.extern.slf4j.Slf4j;



/**
 * The singleton instance of this class may be used to deserialize identifiers of type {@link CBORIdentity}.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class IdentityDeserializer implements InternalIdentifierDeserializer<CBORIdentity> {
	private static IdentityDeserializer instance;


	/**
	 * Get the singleton instance of this deserializer.
	 *
	 * @return Deserializer instance
	 */
	public static IdentityDeserializer getInstance() {
		if(instance == null) {
			instance = new IdentityDeserializer();
		}

		return instance;
	}


	/**
	 * Private constructor, this is a singleton class.
	 */
	private IdentityDeserializer() {}


	@Override
	public CBORIdentity deserialize(final Array attributes, 
									final Array nestedTags, 
									final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		if(log.isDebugEnabled()) {
			log.debug("Attributes array: " + attributes);
			log.debug("Nested tags array: " + nestedTags);
			log.debug("Dictionary entry: " + elementDictEntry);
		}

		// Initially define the required variables to build the target object
		String name = null;
		String administrativeDomain = null;
		IfMapIdentityType type = null;
		String otherTypeDefinition = null;

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
				case CBORIdentity.NAME:
					name = IdentifierDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
				case CBORIdentity.ADMINISTRATIVE_DOMAIN:
					administrativeDomain = IdentifierDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
				case CBORIdentity.OTHER_TYPE_DEFINITION:
					otherTypeDefinition = IdentifierDeserializerManager.processUnicodeStringItem(attrValue, true);
					break;
				case CBORIdentity.TYPE:
					String enumValue = IdentifierDeserializerManager.getAttributeEnumValueXmlName(attrName, attrValue, elementDictEntry);
					type = IfMapIdentityType.fromXmlName(enumValue);
			}
		}

		// Build return value object
		CBORIdentity rv = new CBORIdentity(name, type, otherTypeDefinition);
		rv.setAdministrativeDomain(administrativeDomain);

		return rv;
	}
}
