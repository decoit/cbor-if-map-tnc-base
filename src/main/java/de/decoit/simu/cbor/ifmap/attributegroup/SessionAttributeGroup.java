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
package de.decoit.simu.cbor.ifmap.attributegroup;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnicodeString;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of the IF-MAP sessionAttributes attribute group.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class SessionAttributeGroup extends AbstractAttributeGroup {
	public static final String SESSION_ID = "session-id";

	@Getter
	private String sessionId;


	/**
	 * Create an object with pre-set session ID.
	 * The session ID must be a valid string, it must not be null or
	 * whitespace only.
	 *
	 * @param sessionId IF-MAP session ID
	 */
	public SessionAttributeGroup(final String sessionId) {
		if(StringUtils.isBlank(sessionId)) {
			throw new IllegalArgumentException("Session ID must not be blank");
		}

		this.sessionId = sessionId;
	}


	@Override
	public void serializeAttributeGroup(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		try {
			// Serialize 'session-id'
			{
				DataItem cborName = this.getAttributeNameMapping(SessionAttributeGroup.SESSION_ID, elementEntry);

				builder.add(cborName);
				builder.add(new UnicodeString(this.sessionId));
			}
		}
		catch(Exception ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
