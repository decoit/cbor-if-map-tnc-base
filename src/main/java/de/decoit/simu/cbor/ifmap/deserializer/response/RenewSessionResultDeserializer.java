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
package de.decoit.simu.cbor.ifmap.deserializer.response;

import co.nstant.in.cbor.model.Array;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.response.model.CBORRenewSessionResult;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import lombok.extern.slf4j.Slf4j;

/**
 * The singleton instance of this class may be used to deserialize response results of type {@link CBORRenewSessionResult}.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class RenewSessionResultDeserializer implements InternalResultDeserializer<CBORRenewSessionResult> {
	private static RenewSessionResultDeserializer instance;


	/**
	 * Get the singleton instance of this deserializer.
	 *
	 * @return Deserializer instance
	 */
	public static RenewSessionResultDeserializer getInstance() {
		if(instance == null) {
			instance = new RenewSessionResultDeserializer();
		}

		return instance;
	}


	/**
	 * Private constructor, this is a singleton class.
	 */
	private RenewSessionResultDeserializer() {}
	
	
	@Override
	public CBORRenewSessionResult deserialize(final Array attributes, 
											  final Array nestedTags, 
											  final DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
		if(log.isDebugEnabled()) {
			log.debug("Attributes array: " + attributes);
			log.debug("Nested tags array: " + nestedTags);
			log.debug("Dictionary entry: " + elementDictEntry);
		}
		
		CBORRenewSessionResult rv = new CBORRenewSessionResult();
		
		return rv;
	}
}
