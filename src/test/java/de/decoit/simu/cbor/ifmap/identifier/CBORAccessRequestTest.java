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
package de.decoit.simu.cbor.ifmap.identifier;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import java.io.ByteArrayOutputStream;
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
public class CBORAccessRequestTest extends AbstractTestBase {
	private final String name = "access-request:name";
	private final String administrativeDomain = "access-request:administrative-domain";


	@Test
	public void testConstructor() {
		CBORAccessRequest instance = new CBORAccessRequest(this.name);
		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("access-request", instance.getElementName());
		assertEquals(this.name, instance.getName());
		assertNull(instance.getAdministrativeDomain());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null() {
		CBORAccessRequest instance = new CBORAccessRequest(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyString() {
		CBORAccessRequest instance = new CBORAccessRequest("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Whitespaces() {
		CBORAccessRequest instance = new CBORAccessRequest("   ");
	}


	@Test
	public void testSetAdministrativeDomain() {
		CBORAccessRequest instance = new CBORAccessRequest(this.name);

		instance.setAdministrativeDomain(this.administrativeDomain);
		assertEquals(this.administrativeDomain, instance.getAdministrativeDomain());
	}


	@Test
	public void testSetAdministrativeDomain_null() {
		CBORAccessRequest instance = new CBORAccessRequest(this.name);

		instance.setAdministrativeDomain(this.administrativeDomain);
		instance.setAdministrativeDomain(null);
		assertNull(instance.getAdministrativeDomain());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetAdministrativeDomain_EmptyString() {
		CBORAccessRequest instance = new CBORAccessRequest(this.name);

		instance.setAdministrativeDomain("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetAdministrativeDomain_Whitespaces() {
		CBORAccessRequest instance = new CBORAccessRequest(this.name);

		instance.setAdministrativeDomain("   ");
	}


	@Test
	public void testCborSerialize() throws Exception {
		CBORAccessRequest instance = new CBORAccessRequest(this.name);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("8400008200736163636573732D726571756573743A6E616D6580");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		CBORAccessRequest instance = new CBORAccessRequest(this.name);
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

		byte[] expResult = DatatypeConverter.parseHexBinary("8400008400736163636573732D726571756573743A6E616D650178246163636573732D726571756573743A61"
				+ "646D696E6973747261746976652D646F6D61696E80");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
