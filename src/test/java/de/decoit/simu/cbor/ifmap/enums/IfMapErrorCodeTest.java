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

import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class IfMapErrorCodeTest {
	@Test
	public void testFromXmlName_AccessDenied() {
		IfMapErrorCode result = IfMapErrorCode.fromXmlName("AccessDenied");
		assertEquals(IfMapErrorCode.ACCESS_DENIED, result);
	}


	@Test
	public void testFromXmlName_Failure() {
		IfMapErrorCode result = IfMapErrorCode.fromXmlName("Failure");
		assertEquals(IfMapErrorCode.FAILURE, result);
	}

	@Test
	public void testFromXmlName_InvalidIdentifier() {
		IfMapErrorCode result = IfMapErrorCode.fromXmlName("InvalidIdentifier");
		assertEquals(IfMapErrorCode.INVALID_IDENTIFIER, result);
	}


	@Test
	public void testFromXmlName_InvalidIdentifierType() {
		IfMapErrorCode result = IfMapErrorCode.fromXmlName("InvalidIdentifierType");
		assertEquals(IfMapErrorCode.INVALID_IDENTIFIER_TYPE, result);
	}


	@Test
	public void testFromXmlName_IdentifierTooLong() {
		IfMapErrorCode result = IfMapErrorCode.fromXmlName("IdentifierTooLong");
		assertEquals(IfMapErrorCode.IDENTIFIER_TOO_LONG, result);
	}


	@Test
	public void testFromXmlName_InvalidMetadata() {
		IfMapErrorCode result = IfMapErrorCode.fromXmlName("InvalidMetadata");
		assertEquals(IfMapErrorCode.INVALID_METADATA, result);
	}


	@Test
	public void testFromXmlName_InvalidSchemaVersion() {
		IfMapErrorCode result = IfMapErrorCode.fromXmlName("InvalidSchemaVersion");
		assertEquals(IfMapErrorCode.INVALID_SCHEMA_VERSION, result);
	}


	@Test
	public void testFromXmlName_InvalidSessionID() {
		IfMapErrorCode result = IfMapErrorCode.fromXmlName("InvalidSessionID");
		assertEquals(IfMapErrorCode.INVALID_SESSION_ID, result);
	}


	@Test
	public void testFromXmlName_MetadataTooLong() {
		IfMapErrorCode result = IfMapErrorCode.fromXmlName("MetadataTooLong");
		assertEquals(IfMapErrorCode.METADATA_TOO_LONG, result);
	}


	@Test
	public void testFromXmlName_SearchResultsTooBig() {
		IfMapErrorCode result = IfMapErrorCode.fromXmlName("SearchResultsTooBig");
		assertEquals(IfMapErrorCode.SEARCH_RESULTS_TOO_BIG, result);
	}


	@Test
	public void testFromXmlName_PollResultsTooBig() {
		IfMapErrorCode result = IfMapErrorCode.fromXmlName("PollResultsTooBig");
		assertEquals(IfMapErrorCode.POLL_RESULTS_TOO_BIG, result);
	}


	@Test
	public void testFromXmlName_SystemError() {
		IfMapErrorCode result = IfMapErrorCode.fromXmlName("SystemError");
		assertEquals(IfMapErrorCode.SYSTEM_ERROR, result);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXmlName_null() {
		IfMapErrorCode result = IfMapErrorCode.fromXmlName(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXmlName_UnknownXmlName() {
		IfMapErrorCode result = IfMapErrorCode.fromXmlName("UnknownXmlName");
	}
}
