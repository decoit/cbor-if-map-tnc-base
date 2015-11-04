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
package de.decoit.simu.cbor.ifmap.attributegroup;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class SessionAttributeGroupTest {
	private final String sessionId = "this-is-a-session-id";
	private final String otherSessionId = "this-is-another-session-id";


	@Test
	public void testConstructor() {
		SessionAttributeGroup instance = new SessionAttributeGroup(sessionId);
		assertEquals("this-is-a-session-id", instance.getSessionId());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null() throws Exception {
		SessionAttributeGroup instance = new SessionAttributeGroup(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyString() throws Exception {
		SessionAttributeGroup instance = new SessionAttributeGroup("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Whitespaces() throws Exception {
		SessionAttributeGroup instance = new SessionAttributeGroup("   ");
	}


	@Test
	public void testEquals_true() {
		SessionAttributeGroup instance1 = new SessionAttributeGroup(sessionId);
		SessionAttributeGroup instance2 = new SessionAttributeGroup(sessionId);

		assertTrue(instance1.equals(instance2));
	}


	@Test
	public void testEquals_false() {
		SessionAttributeGroup instance1 = new SessionAttributeGroup(sessionId);
		SessionAttributeGroup instance2 = new SessionAttributeGroup(otherSessionId);

		assertFalse(instance1.equals(instance2));
	}
}
