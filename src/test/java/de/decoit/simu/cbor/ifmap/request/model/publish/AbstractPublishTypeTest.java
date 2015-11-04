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
package de.decoit.simu.cbor.ifmap.request.model.publish;

import de.decoit.simu.cbor.ifmap.enums.IfMapIdentityType;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.ifmap.identifier.CBORDevice;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class AbstractPublishTypeTest {
	private final AbstractIdentifier identifierA = new CBORIdentity("my-dns-name", IfMapIdentityType.DNS_NAME);
	private final AbstractIdentifier identifierB = new CBORDevice("device-01", false);


	@Test
	public void testConstructor_IdentifierA_IdentifierB() {
		AbstractPublishType instance = new AbstractPublishTypeImpl(this.identifierA, this.identifierB);

		assertEquals("test-dummy", instance.getElementName());
		assertEquals(this.identifierA, instance.getIdentifierA());
		assertEquals(this.identifierB, instance.getIdentifierB());
	}


	@Test
	public void testConstructor_IdentifierA_null() {
		AbstractPublishType instance = new AbstractPublishTypeImpl(this.identifierA, null);

		assertEquals("test-dummy", instance.getElementName());
		assertEquals(this.identifierA, instance.getIdentifierA());
		assertNull(instance.getIdentifierB());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null_IdentifierB() {
		AbstractPublishType instance = new AbstractPublishTypeImpl(null, this.identifierB);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_IdentifierA_IdentifierA() {
		AbstractPublishType instance = new AbstractPublishTypeImpl(this.identifierA, this.identifierA);
	}


	@Test
	public void testSetIdentifierB() {
		AbstractPublishType instance = new AbstractPublishTypeImpl(this.identifierA, null);
		instance.setIdentifierB(this.identifierB);

		assertEquals(this.identifierB, instance.getIdentifierB());
	}


	@Test
	public void testSetIdentifierB_null() {
		AbstractPublishType instance = new AbstractPublishTypeImpl(this.identifierA, null);
		instance.setIdentifierB(this.identifierB);
		instance.setIdentifierB(null);

		assertNull(instance.getIdentifierB());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetIdentifierB_IdentifierA() {
		AbstractPublishType instance = new AbstractPublishTypeImpl(this.identifierA, null);
		instance.setIdentifierB(this.identifierA);
	}


	public class AbstractPublishTypeImpl extends AbstractPublishType {
		public AbstractPublishTypeImpl(AbstractIdentifier idA, AbstractIdentifier idB) {
			super("test-dummy", idA, idB);
		}
	}
}
