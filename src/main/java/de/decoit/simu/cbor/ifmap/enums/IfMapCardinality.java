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
 * Enum to be used for the 'ifmap-cardinality' attribute of IF-MAP metadata.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public enum IfMapCardinality {
	SINGLE_VALUE("singleValue"),
	MULTI_VALUE("multiValue");

	@Getter
	private final String xmlName;


	private IfMapCardinality(final String s) {
		xmlName = s;
	}


	/**
	 * Lookup the matching enum constant for the specified XML name.
	 *
	 * @param name XML name
	 * @return Matching enum constant
	 */
	public static IfMapCardinality fromXmlName(String name) {
		if(name == null) {
			throw new IllegalArgumentException("Null pointer for XML name provided");
		}

		switch(name) {
			case "singleValue":
				return IfMapCardinality.SINGLE_VALUE;
			case "multiValue":
				return IfMapCardinality.MULTI_VALUE;
			default:
				throw new IllegalArgumentException("Unknown XML name provided");
		}
	}
}
