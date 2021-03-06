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
public class CBORRoleTest extends AbstractTestBase {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String name = "my-role";
	private final String administrativeDomain = "role:administrative-domain";


	@Test
	public void testConstructor() {
		CBORRole instance = new CBORRole(this.publisherId, this.ifMapTimestamp, this.name);

		assertEquals(IfMapNamespaces.IFMAP_METADATA, instance.getNamespace());
		assertEquals(CBORRole.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.MULTI_VALUE, instance.getIfMapCardinality());
		assertEquals(this.name, instance.getName());
		assertNull(instance.getAdministrativeDomain());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullName() {
		CBORRole instance = new CBORRole(this.publisherId, this.ifMapTimestamp, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyStringName() {
		CBORRole instance = new CBORRole(this.publisherId, this.ifMapTimestamp, "");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_WhitespaceName() {
		CBORRole instance = new CBORRole(this.publisherId, this.ifMapTimestamp, "   ");
	}


	@Test
	public void testSetAdministrativeDomain() {
		CBORRole instance = new CBORRole(this.publisherId, this.ifMapTimestamp, this.name);

		instance.setAdministrativeDomain(this.administrativeDomain);
		assertEquals(this.administrativeDomain, instance.getAdministrativeDomain());
	}


	@Test
	public void testSetAdministrativeDomain_null() {
		CBORRole instance = new CBORRole(this.publisherId, this.ifMapTimestamp, this.name);

		instance.setAdministrativeDomain(this.administrativeDomain);
		instance.setAdministrativeDomain(null);
		assertNull(instance.getAdministrativeDomain());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetAdministrativeDomain_EmptyString() {
		CBORRole instance = new CBORRole(this.publisherId, this.ifMapTimestamp, this.name);

		instance.setAdministrativeDomain("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetAdministrativeDomain_Whitespaces() {
		CBORRole instance = new CBORRole(this.publisherId, this.ifMapTimestamp, this.name);

		instance.setAdministrativeDomain("   ");
	}


	@Test
	public void testCborSerialize() throws Exception {
		CBORRole instance = new CBORRole(this.publisherId, this.ifMapTimestamp, this.name);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84011088006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030184F6008067"
				+ "6D792D726F6C65");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		CBORRole instance = new CBORRole(this.publisherId, this.ifMapTimestamp, this.name);
		instance.setAdministrativeDomain(this.administrativeDomain);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84011088006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030188F6008067"
				+ "6D792D726F6C65F60180781A726F6C653A61646D696E6973747261746976652D646F6D61696E");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
