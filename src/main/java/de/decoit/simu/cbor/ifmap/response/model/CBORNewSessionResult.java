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
package de.decoit.simu.cbor.ifmap.response.model;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;
import de.decoit.simu.cbor.ifmap.attributegroup.SessionAttributeGroup;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of the IF-MAP new-session response.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORNewSessionResult extends AbstractResult {
	public static final String XML_NAME = "newSessionResult";
	public static final String IFMAP_PUBLISHER_ID = "ifmap-publisher-id";
	public static final String MAX_POLL_RESULT_SIZE = "max-poll-result-size";

	@Getter
	private final SessionAttributeGroup sessionAttributes;
	@Getter
	private final String ifMapPublisherId;
	@Getter
	private Integer maxPollResultSize;


	/**
	 * Create a new new-session response.
	 *
	 * @param sessionId IF-MAP session ID
	 * @param ifMapPublisherId IF-MAP publisher ID
	 */
	public CBORNewSessionResult(String sessionId, String ifMapPublisherId) {
		super(XML_NAME);

		if(StringUtils.isBlank(ifMapPublisherId)) {
			throw new IllegalArgumentException("Publisher ID must not be blank");
		}

		this.sessionAttributes = new SessionAttributeGroup(sessionId);
		this.ifMapPublisherId = ifMapPublisherId;
		this.maxPollResultSize = null;
	}


	/**
	 * Set the maximum poll result size attribute for this response.
	 * The parameter may be null to remove this attribute from the response.
	 * If not null, the parameter MUST be greater than 0 because it specifies
	 * a buffer size. The IF-MAP specification states a minimum amount of
	 * 5.000.000 bytes buffer size that a MAP-Server MUST support.
	 *
	 * @param maxPollResultSize Poll result buffer size in bytes, may be null
	 */
	public void setMaxPollResultSize(Integer maxPollResultSize) {
		if(maxPollResultSize != null) {
			if(maxPollResultSize < 1) {
				throw new IllegalArgumentException("Maximum poll result size must be greater than 0");
			}
		}

		this.maxPollResultSize = maxPollResultSize;
	}


	@Override
	protected void serializeAttributes(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		try {
			// Serialize 'session-id'
			this.sessionAttributes.serializeAttributeGroup(builder, elementEntry);


			// Serialize 'ifmap-publisher-id'
			{
				DataItem cborName = this.getAttributeNameMapping(CBORNewSessionResult.IFMAP_PUBLISHER_ID, elementEntry);

				builder.add(cborName);
				builder.add(new UnicodeString(this.ifMapPublisherId));
			}


			// Serialize 'max-poll-result-size'
			if(this.maxPollResultSize != null) {
				DataItem cborName = this.getAttributeNameMapping(CBORNewSessionResult.MAX_POLL_RESULT_SIZE, elementEntry);

				builder.add(cborName);
				builder.add(new UnsignedInteger(this.maxPollResultSize));
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
