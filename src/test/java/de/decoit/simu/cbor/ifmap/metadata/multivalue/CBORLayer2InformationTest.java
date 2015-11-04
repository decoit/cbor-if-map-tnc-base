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
package de.decoit.simu.cbor.ifmap.metadata.multivalue;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.enums.IfMapCardinality;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.ifmap.util.TimestampHelper;
import java.io.ByteArrayOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import javax.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class CBORLayer2InformationTest extends AbstractTestBase {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final Integer vlan = 42;
	private final String vlanName = "vlan-42";
	private final Integer port = 4242;
	private final String administrativeDomain = "layer2-information:administrative-domain";


	@Test
	public void testConstructor() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);

		assertEquals(IfMapNamespaces.IFMAP_METADATA, instance.getNamespace());
		assertEquals(CBORLayer2Information.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.MULTI_VALUE, instance.getIfMapCardinality());
		assertNull(instance.getVlan());
		assertNull(instance.getVlanName());
		assertNull(instance.getPort());
		assertNull(instance.getAdministrativeDomain());
	}


	@Test
	public void testSetVlan() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);
		instance.setVlan(this.vlan);
		assertEquals(this.vlan, instance.getVlan());
	}


	@Test
	public void testSetVlan_null() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);
		instance.setVlan(this.vlan);
		instance.setVlan(null);
		assertNull(instance.getVlan());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetVlan_IllegalVlanLow() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);
		instance.setVlan(0);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetVlan_IllegalVlanHigh() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);
		instance.setVlan(4095);
	}


	@Test
	public void testSetVlanName() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);
		instance.setVlanName(this.vlanName);
		assertEquals(this.vlanName, instance.getVlanName());
	}


	@Test
	public void testSetVlanName_null() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);
		instance.setVlanName(this.vlanName);
		instance.setVlanName(null);
		assertNull(instance.getVlanName());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetVlanName_EmptyString() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);
		instance.setVlanName("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetVlanName_Whitespaces() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);
		instance.setVlanName("   ");
	}


	@Test
	public void testSetPort() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);
		instance.setPort(this.port);
		assertEquals(this.port, instance.getPort());
	}


	@Test
	public void testSetPort_null() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);
		instance.setPort(this.port);
		instance.setPort(null);
		assertNull(instance.getPort());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetPort_IllegalPortLow() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);
		instance.setVlan(-1);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetPort_IllegalPortHigh() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);
		instance.setVlan(65536);
	}


	@Test
	public void testSetAdministrativeDomain() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);

		instance.setAdministrativeDomain(this.administrativeDomain);
		assertEquals(this.administrativeDomain, instance.getAdministrativeDomain());
	}


	@Test
	public void testSetAdministrativeDomain_null() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);

		instance.setAdministrativeDomain(this.administrativeDomain);
		instance.setAdministrativeDomain(null);
		assertNull(instance.getAdministrativeDomain());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetAdministrativeDomain_EmptyString() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);

		instance.setAdministrativeDomain("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetAdministrativeDomain_Whitespaces() {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);

		instance.setAdministrativeDomain("   ");
	}


	@Test
	public void testCborSerialize() throws Exception {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010D88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030180");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		CBORLayer2Information instance = new CBORLayer2Information(this.publisherId, this.ifMapTimestamp);
		instance.setVlan(this.vlan);
		instance.setVlanName(this.vlanName);
		instance.setPort(this.port);
		instance.setAdministrativeDomain(this.administrativeDomain);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010D88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030190F6008018"
				+ "2AF6018067766C616E2D3432F60280191092F6038078286C61796572322D696E666F726D6174696F6E3A61646D696E6973747261746976652D646F6D61696E");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
