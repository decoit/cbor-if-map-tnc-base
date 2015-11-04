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

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.builder.MapBuilder;
import de.decoit.simu.cbor.ifmap.enums.IfMapIdentityType;
import de.decoit.simu.cbor.ifmap.enums.IfMapLifetime;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.ifmap.identifier.CBORDevice;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import de.decoit.simu.cbor.ifmap.metadata.AbstractMetadata;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORCapability;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORDeviceAttribute;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORDiscoveredBy;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class AbstractUpdateNotifyTest {
	private final AbstractIdentifier identifierA = new CBORIdentity("my-dns-name", IfMapIdentityType.DNS_NAME);
	private final AbstractIdentifier identifierB = new CBORDevice("device-01", false);
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String capabilityName1 = "cap-01";
	private final String capabilityName2 = "cap-02";
	private final String devAttrName = "my-attribute";
	private final AbstractMetadata m1 = new CBORCapability(this.publisherId, this.ifMapTimestamp, this.capabilityName1);
	private final AbstractMetadata m2 = new CBORCapability(this.publisherId, this.ifMapTimestamp, this.capabilityName2);
	private final AbstractMetadata m3 = new CBORDeviceAttribute(this.publisherId, this.ifMapTimestamp, this.devAttrName);


	@Test
	public void testConstructor_IdentifierA_IdentifierB() {
		AbstractUpdateNotify instance = new AbstractUpdateNotifyImpl(this.identifierA, this.identifierB);

		assertEquals("test-dummy", instance.getElementName());
		assertTrue(instance.getMetadata().isEmpty());
		assertEquals(IfMapLifetime.SESSION, instance.getLifetime());
		assertEquals(this.identifierA, instance.getIdentifierA());
		assertEquals(this.identifierB, instance.getIdentifierB());
	}


	@Test
	public void testConstructor_IdentifierA_null() {
		AbstractUpdateNotify instance = new AbstractUpdateNotifyImpl(this.identifierA, null);

		assertEquals("test-dummy", instance.getElementName());
		assertTrue(instance.getMetadata().isEmpty());
		assertEquals(IfMapLifetime.SESSION, instance.getLifetime());
		assertEquals(this.identifierA, instance.getIdentifierA());
		assertNull(instance.getIdentifierB());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null_IdentifierB() {
		AbstractUpdateNotify instance = new AbstractUpdateNotifyImpl(null, this.identifierB);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_IdentifierA_IdentifierA() {
		AbstractUpdateNotify instance = new AbstractUpdateNotifyImpl(this.identifierA, this.identifierA);
	}


	@Test
	public void testAddMetadata() {
		AbstractUpdateNotify instance = new AbstractUpdateNotifyImpl(this.identifierA, null);

		instance.addMetadata(this.m1);
		instance.addMetadata(this.m2);

		assertTrue(instance.getMetadata().size() == 2);
		assertEquals(this.m1, instance.getMetadata().get(0));
		assertEquals(this.m2, instance.getMetadata().get(1));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddMetadata_null() {
		AbstractUpdateNotify instance = new AbstractUpdateNotifyImpl(this.identifierA, null);

		instance.addMetadata(null);
	}


	@Test
	public void testRemoveMetadata() {
		AbstractUpdateNotify instance = new AbstractUpdateNotifyImpl(this.identifierA, null);

		instance.addMetadata(this.m1);
		instance.addMetadata(this.m2);

		instance.removeMetadata(this.m1);

		assertTrue(instance.getMetadata().size() == 1);
		assertEquals(this.m2, instance.getMetadata().get(0));
	}


	@Test
	public void testRemoveMetadata_null() {
		AbstractUpdateNotify instance = new AbstractUpdateNotifyImpl(this.identifierA, null);

		instance.addMetadata(this.m1);
		instance.addMetadata(this.m2);

		instance.removeMetadata(null);

		assertTrue(instance.getMetadata().size() == 2);
		assertEquals(this.m1, instance.getMetadata().get(0));
		assertEquals(this.m2, instance.getMetadata().get(1));
	}


	@Test
	public void testRemoveMetadata_NonExistent() {
		AbstractUpdateNotify instance = new AbstractUpdateNotifyImpl(this.identifierA, null);

		instance.addMetadata(this.m1);
		instance.addMetadata(this.m2);

		instance.removeMetadata(new CBORDiscoveredBy(this.publisherId, this.ifMapTimestamp));

		assertTrue(instance.getMetadata().size() == 2);
		assertEquals(this.m1, instance.getMetadata().get(0));
		assertEquals(this.m2, instance.getMetadata().get(1));
	}


	@Test
	public void testRemoveMetadata_SameTwice() {
		AbstractUpdateNotify instance = new AbstractUpdateNotifyImpl(this.identifierA, null);

		instance.addMetadata(this.m1);
		instance.addMetadata(this.m2);

		instance.removeMetadata(this.m1);
		instance.removeMetadata(this.m1);

		assertTrue(instance.getMetadata().size() == 1);
		assertEquals(this.m2, instance.getMetadata().get(0));
	}


	@Test
	public void testRemoveAllMetadata() {
		AbstractUpdateNotify instance = new AbstractUpdateNotifyImpl(this.identifierA, null);

		instance.addMetadata(this.m1);
		instance.addMetadata(this.m2);

		instance.removeAllMetadata();

		assertTrue(instance.getMetadata().isEmpty());
	}


	@Test
	public void testSetLifetime() {
		AbstractUpdateNotify instance = new AbstractUpdateNotifyImpl(this.identifierA, null);

		instance.setLiftime(IfMapLifetime.FOREVER);

		assertEquals(IfMapLifetime.FOREVER, instance.getLifetime());
	}


	public class AbstractUpdateNotifyImpl extends AbstractUpdateNotify {
		public AbstractUpdateNotifyImpl(AbstractIdentifier idA, AbstractIdentifier idB) {
			super("test-dummy", idA, idB);
		}
	}
}
