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
public class IfMapLifetimeTest {
	@Test
	public void testFromXmlName_Session() {
		IfMapLifetime result = IfMapLifetime.fromXmlName("session");
		assertEquals(IfMapLifetime.SESSION, result);
	}


	@Test
	public void testFromXmlName_Forever() {
		IfMapLifetime result = IfMapLifetime.fromXmlName("forever");
		assertEquals(IfMapLifetime.FOREVER, result);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXmlName_null() {
		IfMapLifetime result = IfMapLifetime.fromXmlName(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXmlName_UnknownXmlName() {
		IfMapLifetime result = IfMapLifetime.fromXmlName("UnknownXmlName");
	}
}
