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

import de.decoit.simu.cbor.ifmap.enums.IfMapValidationType;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class ValidationAttributeGroupTest {
	@Test
	public void testConstructor() {
		ValidationAttributeGroup instance = new ValidationAttributeGroup(IfMapValidationType.ALL);
		assertEquals(IfMapValidationType.ALL, instance.getValidationType());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null() throws Exception {
		ValidationAttributeGroup instance = new ValidationAttributeGroup(null);
	}


	@Test
	public void testEquals_true() {
		ValidationAttributeGroup instance1 = new ValidationAttributeGroup(IfMapValidationType.ALL);
		ValidationAttributeGroup instance2 = new ValidationAttributeGroup(IfMapValidationType.ALL);

		assertTrue(instance1.equals(instance2));
	}


	@Test
	public void testEquals_false() {
		ValidationAttributeGroup instance1 = new ValidationAttributeGroup(IfMapValidationType.ALL);
		ValidationAttributeGroup instance2 = new ValidationAttributeGroup(IfMapValidationType.BASE_ONLY);

		assertFalse(instance1.equals(instance2));
	}
}
