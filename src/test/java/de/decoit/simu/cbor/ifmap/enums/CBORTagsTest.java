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
public class CBORTagsTest {
	@Test
	public void testFromTagNumber_DateTimeString() {
		long i = 0L;
		CBORTags expResult = CBORTags.DATE_TIME_STRING;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_DateTimeEpoch() {
		long i = 1L;
		CBORTags expResult = CBORTags.DATE_TIME_EPOCH;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_PositiveBignum() {
		long i = 2L;
		CBORTags expResult = CBORTags.POSITIVE_BIGNUM;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_NegativeBignum() {
		long i = 3L;
		CBORTags expResult = CBORTags.NEGATIVE_BIGNUM;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_DecimalFraction() {
		long i = 4L;
		CBORTags expResult = CBORTags.DECIMAL_FRACTION;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_Bigfloat() {
		long i = 5L;
		CBORTags expResult = CBORTags.BIGFLOAT;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_Base64UrlEncoding() {
		long i = 21L;
		CBORTags expResult = CBORTags.BASE64_URL_ENCODING;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_Base64Encoding() {
		long i = 22L;
		CBORTags expResult = CBORTags.BASE64_ENCODING;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_Base16Encoding() {
		long i = 23L;
		CBORTags expResult = CBORTags.BASE16_ENCODING;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_EncodedCbor() {
		long i = 24L;
		CBORTags expResult = CBORTags.ENCODED_CBOR_DATA_ITEM;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_Uri() {
		long i = 32L;
		CBORTags expResult = CBORTags.URI;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_Base64Url() {
		long i = 33L;
		CBORTags expResult = CBORTags.BASE64_URL;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_Base64() {
		long i = 34L;
		CBORTags expResult = CBORTags.BASE64;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_RegEx() {
		long i = 35L;
		CBORTags expResult = CBORTags.REGULAR_EXPRESSION;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_MimeMessage() {
		long i = 36L;
		CBORTags expResult = CBORTags.MIME_MESSAGE;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_ExtendedIdentifier() {
		long i = 42000L;
		CBORTags expResult = CBORTags.IF_MAP_EXTENDED_IDENTIFIER;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_IPv4() {
		long i = 42001L;
		CBORTags expResult = CBORTags.IPV4_ADDRESS;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_IPv6() {
		long i = 42002L;
		CBORTags expResult = CBORTags.IPV6_ADDRESS;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_MacAddress() {
		long i = 42003L;
		CBORTags expResult = CBORTags.MAC_ADDRESS;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromTagNumber_SelfDescribeCbor() {
		long i = 55799L;
		CBORTags expResult = CBORTags.SELF_DESCRIBE_CBOR;
		CBORTags result = CBORTags.fromTagNumber(i);
		assertEquals(expResult, result);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromTagNumber_UnknownTagNumber() {
		long i = -1L;
		CBORTags result = CBORTags.fromTagNumber(i);
	}
}
