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
public class IfMapWlanSecurityTypeTest {
	@Test
	public void testFromXmlName_Open() {
		IfMapWlanSecurityType result = IfMapWlanSecurityType.fromXmlName("open");
		assertEquals(IfMapWlanSecurityType.OPEN, result);
	}


	@Test
	public void testFromXmlName_Wep() {
		IfMapWlanSecurityType result = IfMapWlanSecurityType.fromXmlName("wep");
		assertEquals(IfMapWlanSecurityType.WEP, result);
	}


	@Test
	public void testFromXmlName_Tkip() {
		IfMapWlanSecurityType result = IfMapWlanSecurityType.fromXmlName("tkip");
		assertEquals(IfMapWlanSecurityType.TKIP, result);
	}


	@Test
	public void testFromXmlName_Ccmp() {
		IfMapWlanSecurityType result = IfMapWlanSecurityType.fromXmlName("ccmp");
		assertEquals(IfMapWlanSecurityType.CCMP, result);
	}


	@Test
	public void testFromXmlName_Bip() {
		IfMapWlanSecurityType result = IfMapWlanSecurityType.fromXmlName("bip");
		assertEquals(IfMapWlanSecurityType.BIP, result);
	}


	@Test
	public void testFromXmlName_Other() {
		IfMapWlanSecurityType result = IfMapWlanSecurityType.fromXmlName("other");
		assertEquals(IfMapWlanSecurityType.OTHER, result);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXmlName_null() {
		IfMapWlanSecurityType result = IfMapWlanSecurityType.fromXmlName(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXmlName_UnknownXmlName() {
		IfMapWlanSecurityType result = IfMapWlanSecurityType.fromXmlName("UnknownXmlName");
	}
}
