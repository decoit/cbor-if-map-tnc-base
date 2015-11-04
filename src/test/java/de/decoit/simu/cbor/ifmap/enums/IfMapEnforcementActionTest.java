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
public class IfMapEnforcementActionTest {
	@Test
	public void testFromXmlName_Block() {
		IfMapEnforcementAction result = IfMapEnforcementAction.fromXmlName("block");
		assertEquals(IfMapEnforcementAction.BLOCK, result);
	}


	@Test
	public void testFromXmlName_Quarantine() {
		IfMapEnforcementAction result = IfMapEnforcementAction.fromXmlName("quarantine");
		assertEquals(IfMapEnforcementAction.QUARANTINE, result);
	}

	@Test
	public void testFromXmlName_Other() {
		IfMapEnforcementAction result = IfMapEnforcementAction.fromXmlName("other");
		assertEquals(IfMapEnforcementAction.OTHER, result);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXmlName_null() {
		IfMapEnforcementAction result = IfMapEnforcementAction.fromXmlName(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXmlName_UnknownXmlName() {
		IfMapEnforcementAction result = IfMapEnforcementAction.fromXmlName("UnknownXmlName");
	}
}
