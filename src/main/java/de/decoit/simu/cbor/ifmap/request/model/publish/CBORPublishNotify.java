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
package de.decoit.simu.cbor.ifmap.request.model.publish;

import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;



/**
 * Java representation of the IF-MAP notify publish request type
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORPublishNotify extends AbstractUpdateNotify {
	public static final String XML_NAME = "notify";
	
	
	/**
	 * Create a new notify publish type object with one identifier.
	 *
	 * @param identifierA Identifier to add to this publish request
	 */
	public CBORPublishNotify(AbstractIdentifier identifierA) {
		this(identifierA, null);
	}


	/**
	 * Create a new notify publish type object with one or two identifiers.
	 *
	 * @param identifierA First identifier to add to this publish request
	 * @param identifierB Second identifier to add to this publish request, may be null
	 */
	public CBORPublishNotify(AbstractIdentifier identifierA, AbstractIdentifier identifierB) {
		super(XML_NAME, identifierA, identifierB);
	}
}
