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
package de.decoit.simu.cbor.ifmap.enums;

import lombok.Getter;



/**
 * Enum to be used for the 'type' attribute of the event IF-MAP metadata.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public enum IfMapEventType {
	P2P("p2p"),
	CVE("cve"),
	BOTNET_INFECTION("botnet infection"),
	WORM_INFECTION("worm infection"),
	EXCESSIVE_FLOWS("excessive flows"),
	BEHAVIORAL_CHANGE("behavioral change"),
	POLICY_VIOLATION("policy violation"),
	OTHER("other");

	@Getter
	private final String xmlName;


	private IfMapEventType(final String s) {
		xmlName = s;
	}


	/**
	 * Lookup the matching enum constant for the specified XML name.
	 *
	 * @param name XML name
	 * @return Matching enum constant
	 */
	public static IfMapEventType fromXmlName(String name) {
		if(name == null) {
			throw new IllegalArgumentException("Null pointer for XML name provided");
		}

		switch(name) {
			case "p2p":
				return IfMapEventType.P2P;
			case "cve":
				return IfMapEventType.CVE;
			case "botnet infection":
				return IfMapEventType.BOTNET_INFECTION;
			case "worm infection":
				return IfMapEventType.WORM_INFECTION;
			case "excessive flows":
				return IfMapEventType.EXCESSIVE_FLOWS;
			case "behavioral change":
				return IfMapEventType.BEHAVIORAL_CHANGE;
			case "policy violation":
				return IfMapEventType.POLICY_VIOLATION;
			case "other":
				return IfMapEventType.OTHER;
			default:
				throw new IllegalArgumentException("Unknown XML name provided");
		}
	}
}
