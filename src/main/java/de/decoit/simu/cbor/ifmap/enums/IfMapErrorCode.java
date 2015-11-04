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
 * Enum to be used for the 'errorCode' attribute of the ErrorResult IF-MAP response.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public enum IfMapErrorCode {
	ACCESS_DENIED("AccessDenied"),
	FAILURE("Failure"),
	INVALID_IDENTIFIER("InvalidIdentifier"),
	INVALID_IDENTIFIER_TYPE("InvalidIdentifierType"),
	IDENTIFIER_TOO_LONG("IdentifierTooLong"),
	INVALID_METADATA("InvalidMetadata"),
	INVALID_SCHEMA_VERSION("InvalidSchemaVersion"),
	INVALID_SESSION_ID("InvalidSessionID"),
	METADATA_TOO_LONG("MetadataTooLong"),
	SEARCH_RESULTS_TOO_BIG("SearchResultsTooBig"),
	POLL_RESULTS_TOO_BIG("PollResultsTooBig"),
	SYSTEM_ERROR("SystemError");

	@Getter
	private final String xmlName;


	private IfMapErrorCode(final String s) {
		xmlName = s;
	}


	/**
	 * Lookup the matching enum constant for the specified XML name.
	 *
	 * @param name XML name
	 * @return Matching enum constant
	 */
	public static IfMapErrorCode fromXmlName(String name) {
		if(name == null) {
			throw new IllegalArgumentException("Null pointer for XML name provided");
		}

		switch(name) {
			case "AccessDenied":
				return IfMapErrorCode.ACCESS_DENIED;
			case "Failure":
				return IfMapErrorCode.FAILURE;
			case "InvalidIdentifier":
				return IfMapErrorCode.INVALID_IDENTIFIER;
			case "InvalidIdentifierType":
				return IfMapErrorCode.INVALID_IDENTIFIER_TYPE;
			case "IdentifierTooLong":
				return IfMapErrorCode.IDENTIFIER_TOO_LONG;
			case "InvalidMetadata":
				return IfMapErrorCode.INVALID_METADATA;
			case "InvalidSchemaVersion":
				return IfMapErrorCode.INVALID_SCHEMA_VERSION;
			case "InvalidSessionID":
				return IfMapErrorCode.INVALID_SESSION_ID;
			case "MetadataTooLong":
				return IfMapErrorCode.METADATA_TOO_LONG;
			case "SearchResultsTooBig":
				return IfMapErrorCode.SEARCH_RESULTS_TOO_BIG;
			case "PollResultsTooBig":
				return IfMapErrorCode.POLL_RESULTS_TOO_BIG;
			case "SystemError":
				return IfMapErrorCode.SYSTEM_ERROR;
			default:
				throw new IllegalArgumentException("Unknown XML name provided");
		}
	}
}
