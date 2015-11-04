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

import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;



/**
 * Java representation of the IF-MAP renew-session request.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORRenewSessionRequest extends AbstractSessionIdRequest {
	public final static String XML_NAME = "renewSession";
	
	
	/**
	 * Create a new renew-session request for the specified session ID.
	 *
	 * @param sessionId IF-MAP session ID
	 */
	public CBORRenewSessionRequest(String sessionId) {
		super(IfMapNamespaces.IFMAP, XML_NAME, sessionId);
	}
}
