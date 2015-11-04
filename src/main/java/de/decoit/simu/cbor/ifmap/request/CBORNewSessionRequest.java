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
package de.decoit.simu.cbor.ifmap.request;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnsignedInteger;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;



/**
 * Java representation of the IF-MAP new-session request.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORNewSessionRequest extends AbstractRequest {
	public final static String XML_NAME = "newSession";
	public static final String MAX_POLL_RESULT_SIZE = "max-poll-result-size";

	@Getter
	private Integer maxPollResultSize;


	/**
	 * Create a new new-session request.
	 */
	public CBORNewSessionRequest() {
		super(IfMapNamespaces.IFMAP, XML_NAME);

		this.maxPollResultSize = null;
	}


	/**
	 * Set the maximum poll result size attribute for this request.
	 * The parameter may be null to remove this attribute from the request.
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
	protected void serializeAttributes(final ArrayBuilder<?> builder) throws CBORSerializationException {
		try {
			// Build dictionary path to this element
			StringBuilder dictPathSb = new StringBuilder("<");
			dictPathSb.append(this.namespace);
			dictPathSb.append(">");
			dictPathSb.append(this.elementName);

			// Get dictionary entry for this element
			DictionarySimpleElement elementEntry = DictionaryProvider.getInstance().findElementByPath(dictPathSb.toString());

			// Serialize 'max-poll-result-size'
			if(this.maxPollResultSize != null) {
				DataItem cborName = this.getAttributeNameMapping(CBORNewSessionRequest.MAX_POLL_RESULT_SIZE, elementEntry);

				builder.add(cborName);
				builder.add(new UnsignedInteger(this.maxPollResultSize));
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
