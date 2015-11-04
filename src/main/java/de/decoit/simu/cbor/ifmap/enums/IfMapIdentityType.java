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
 * Enum to be used for the 'type' attribute of the IF-MAP identity identifier.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public enum IfMapIdentityType {
	AIK_NAME("aik-name"),
	DISTINGUISHED_NAME("distinguished-name"),
	DNS_NAME("dns-name"),
	EMAIL_ADDRESS("email-address"),
	KERBEROS_PRINCIPAL("kerberos-principal"),
	USERNAME("username"),
	SIP_URI("sip-uri"),
	TEL_URI("tel-uri"),
	HIP_HIT("hip-hit"),
	OTHER("other");

	@Getter
	private final String xmlName;


	private IfMapIdentityType(final String s) {
		xmlName = s;
	}


	/**
	 * Lookup the matching enum constant for the specified XML name.
	 *
	 * @param name XML name
	 * @return Matching enum constant
	 */
	public static IfMapIdentityType fromXmlName(String name) {
		if(name == null) {
			throw new IllegalArgumentException("Null pointer for XML name provided");
		}

		switch(name) {
			case "aik-name":
				return IfMapIdentityType.AIK_NAME;
			case "distinguished-name":
				return IfMapIdentityType.DISTINGUISHED_NAME;
			case "dns-name":
				return IfMapIdentityType.DNS_NAME;
			case "email-address":
				return IfMapIdentityType.EMAIL_ADDRESS;
			case "kerberos-principal":
				return IfMapIdentityType.KERBEROS_PRINCIPAL;
			case "username":
				return IfMapIdentityType.USERNAME;
			case "sip-uri":
				return IfMapIdentityType.SIP_URI;
			case "tel-uri":
				return IfMapIdentityType.TEL_URI;
			case "hip-hit":
				return IfMapIdentityType.HIP_HIT;
			case "other":
				return IfMapIdentityType.OTHER;
			default:
				throw new IllegalArgumentException("Unknown XML name provided");
		}
	}
}
