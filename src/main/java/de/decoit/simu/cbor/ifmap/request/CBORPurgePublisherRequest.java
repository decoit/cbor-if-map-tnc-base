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
import co.nstant.in.cbor.model.UnicodeString;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryNamespace;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleAttribute;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of the IF-MAP purge-publisher request.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORPurgePublisherRequest extends AbstractSessionIdRequest {
	public final static String XML_NAME = "purgePublisher";
	public static final String IFMAP_PUBLISHER_ID = "ifmap-publisher-id";

	@Getter
	private final String ifMapPublisherId;


	/**
	 * Create a new purge-publisher request with the specified session ID and publisher ID.
	 *
	 * @param sessionId IF-MAP session ID
	 * @param publisherId IF-MAP publisher ID
	 */
	public CBORPurgePublisherRequest(String sessionId, String publisherId) {
		super(IfMapNamespaces.IFMAP, XML_NAME, sessionId);

		if(StringUtils.isBlank(publisherId)) {
			throw new IllegalArgumentException("Publisher ID must not be blank");
		}

		this.ifMapPublisherId = publisherId;
	}


	@Override
	protected void serializeAttributes(final ArrayBuilder<?> builder) throws CBORSerializationException {
		// Call serializeAttributes on super class
		super.serializeAttributes(builder);

		try {
			// Build dictionary path to this element
			StringBuilder dictPathSb = new StringBuilder("<");
			dictPathSb.append(this.namespace);
			dictPathSb.append(">");
			dictPathSb.append(this.elementName);

			// Get dictionary entry for this element
			DictionarySimpleElement elementEntry = DictionaryProvider.getInstance().findElementByPath(dictPathSb.toString());

			// Serialize 'ifmap-publisher-id'
			{
				DataItem cborName = this.getAttributeNameMapping(CBORPurgePublisherRequest.IFMAP_PUBLISHER_ID, elementEntry);

				builder.add(cborName);
				builder.add(new UnicodeString(this.ifMapPublisherId));
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
