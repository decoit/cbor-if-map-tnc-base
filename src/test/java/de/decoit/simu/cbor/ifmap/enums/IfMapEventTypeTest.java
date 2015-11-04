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
public class IfMapEventTypeTest {
	@Test
	public void testFromXmlName_P2P() {
		IfMapEventType result = IfMapEventType.fromXmlName("p2p");
		assertEquals(IfMapEventType.P2P, result);
	}


	@Test
	public void testFromXmlName_CVE() {
		IfMapEventType result = IfMapEventType.fromXmlName("cve");
		assertEquals(IfMapEventType.CVE, result);
	}

	@Test
	public void testFromXmlName_BotnetInfection() {
		IfMapEventType result = IfMapEventType.fromXmlName("botnet infection");
		assertEquals(IfMapEventType.BOTNET_INFECTION, result);
	}

	@Test
	public void testFromXmlName_WormInfection() {
		IfMapEventType result = IfMapEventType.fromXmlName("worm infection");
		assertEquals(IfMapEventType.WORM_INFECTION, result);
	}

	@Test
	public void testFromXmlName_ExcessiveFlows() {
		IfMapEventType result = IfMapEventType.fromXmlName("excessive flows");
		assertEquals(IfMapEventType.EXCESSIVE_FLOWS, result);
	}

	@Test
	public void testFromXmlName_BehavioralChange() {
		IfMapEventType result = IfMapEventType.fromXmlName("behavioral change");
		assertEquals(IfMapEventType.BEHAVIORAL_CHANGE, result);
	}

	@Test
	public void testFromXmlName_PolicyViolation() {
		IfMapEventType result = IfMapEventType.fromXmlName("policy violation");
		assertEquals(IfMapEventType.POLICY_VIOLATION, result);
	}

	@Test
	public void testFromXmlName_Other() {
		IfMapEventType result = IfMapEventType.fromXmlName("other");
		assertEquals(IfMapEventType.OTHER, result);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXmlName_null() {
		IfMapEventType result = IfMapEventType.fromXmlName(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXmlName_UnknownXmlName() {
		IfMapEventType result = IfMapEventType.fromXmlName("UnknownXmlName");
	}
}
