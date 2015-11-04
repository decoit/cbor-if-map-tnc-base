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
package de.decoit.simu.cbor.ifmap.identifier.extended;

import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import java.util.HashMap;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;



/**
 * Java representation of an IF-MAP ifmap-server operational extended identifier.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORExtendedIfMapServer extends AbstractExtendedIdentifier {
	public static final String XML_NAME = "ifmap-server";

	/**
	 * Create a new ifmap-server operational extended identifier.
	 *
	 * @param administrativeDomain
	 */
	public CBORExtendedIfMapServer(String administrativeDomain) {
		super(IfMapNamespaces.IFMAP_SERVER, XML_NAME, administrativeDomain, new HashMap<>(), new HashMap<>());
	}
}
