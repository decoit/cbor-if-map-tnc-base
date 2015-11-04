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
public class IfMapIdentityTypeTest {
	@Test
	public void testFromXmlName_AikName() {
		IfMapIdentityType result = IfMapIdentityType.fromXmlName("aik-name");
		assertEquals(IfMapIdentityType.AIK_NAME, result);
	}


	@Test
	public void testFromXmlName_DistinguishedName() {
		IfMapIdentityType result = IfMapIdentityType.fromXmlName("distinguished-name");
		assertEquals(IfMapIdentityType.DISTINGUISHED_NAME, result);
	}


	@Test
	public void testFromXmlName_DnsName() {
		IfMapIdentityType result = IfMapIdentityType.fromXmlName("dns-name");
		assertEquals(IfMapIdentityType.DNS_NAME, result);
	}


	@Test
	public void testFromXmlName_EmailAddress() {
		IfMapIdentityType result = IfMapIdentityType.fromXmlName("email-address");
		assertEquals(IfMapIdentityType.EMAIL_ADDRESS, result);
	}


	@Test
	public void testFromXmlName_KerberosPrincipal() {
		IfMapIdentityType result = IfMapIdentityType.fromXmlName("kerberos-principal");
		assertEquals(IfMapIdentityType.KERBEROS_PRINCIPAL, result);
	}


	@Test
	public void testFromXmlName_Username() {
		IfMapIdentityType result = IfMapIdentityType.fromXmlName("username");
		assertEquals(IfMapIdentityType.USERNAME, result);
	}


	@Test
	public void testFromXmlName_SipUri() {
		IfMapIdentityType result = IfMapIdentityType.fromXmlName("sip-uri");
		assertEquals(IfMapIdentityType.SIP_URI, result);
	}


	@Test
	public void testFromXmlName_TelUri() {
		IfMapIdentityType result = IfMapIdentityType.fromXmlName("tel-uri");
		assertEquals(IfMapIdentityType.TEL_URI, result);
	}


	@Test
	public void testFromXmlName_HipHit() {
		IfMapIdentityType result = IfMapIdentityType.fromXmlName("hip-hit");
		assertEquals(IfMapIdentityType.HIP_HIT, result);
	}


	@Test
	public void testFromXmlName_Other() {
		IfMapIdentityType result = IfMapIdentityType.fromXmlName("other");
		assertEquals(IfMapIdentityType.OTHER, result);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXmlName_null() {
		IfMapIdentityType result = IfMapIdentityType.fromXmlName(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXmlName_UnknownXmlName() {
		IfMapIdentityType result = IfMapIdentityType.fromXmlName("UnknownXmlName");
	}
}
