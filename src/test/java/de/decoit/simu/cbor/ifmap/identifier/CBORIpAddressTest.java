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
import de.decoit.simu.cbor.ifmap.enums.IfMapIpAddressType;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
public class CBORIpAddressTest extends AbstractTestBase {
	private final String administrativeDomain = "ip-address:administrative-domain";
	private final InetAddress ipv4;
	private final InetAddress ipv6;


	public CBORIpAddressTest() throws UnknownHostException {
		this.ipv4 = InetAddress.getByName("255.3.2.1");
		this.ipv6 = InetAddress.getByName("2001:0db8:85a3:08d3:1319:8a2e:0370:7344");
	}


	@Test
	public void testConstructor_IPv4() {
		CBORIpAddress instance = new CBORIpAddress(this.ipv4);
		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("ip-address", instance.getElementName());
		assertEquals(this.ipv4, instance.getValue());
		assertEquals(IfMapIpAddressType.IPV4, instance.getType());
		assertNull(instance.getAdministrativeDomain());
	}


	@Test
	public void testConstructor_IPv6() {
		CBORIpAddress instance = new CBORIpAddress(this.ipv6);
		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("ip-address", instance.getElementName());
		assertEquals(this.ipv6, instance.getValue());
		assertEquals(IfMapIpAddressType.IPV6, instance.getType());
		assertNull(instance.getAdministrativeDomain());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null() {
		CBORIpAddress instance = new CBORIpAddress(null);
	}


	@Test
	public void testSetAdministrativeDomain() {
		CBORIpAddress instance = new CBORIpAddress(this.ipv4);

		instance.setAdministrativeDomain(this.administrativeDomain);
		assertEquals(this.administrativeDomain, instance.getAdministrativeDomain());
	}


	@Test
	public void testSetAdministrativeDomain_null() {
		CBORIpAddress instance = new CBORIpAddress(this.ipv4);

		instance.setAdministrativeDomain(this.administrativeDomain);
		instance.setAdministrativeDomain(null);
		assertNull(instance.getAdministrativeDomain());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetAdministrativeDomain_EmptyString() {
		CBORIpAddress instance = new CBORIpAddress(this.ipv4);

		instance.setAdministrativeDomain("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetAdministrativeDomain_Whitespaces() {
		CBORIpAddress instance = new CBORIpAddress(this.ipv4);

		instance.setAdministrativeDomain("   ");
	}


	@Test
	public void testCborSerialize_IPv4() throws Exception {
		CBORIpAddress instance = new CBORIpAddress(this.ipv4);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (IPv4):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("8400038400D9A41144FF030201020080");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_IPv6() throws Exception {
		CBORIpAddress instance = new CBORIpAddress(this.ipv6);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (IPv6):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("8400038400D9A4125020010DB885A308D313198A2E03707344020180");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		CBORIpAddress instance = new CBORIpAddress(this.ipv4);
		instance.setAdministrativeDomain(this.administrativeDomain);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (IPv4+AdmDomain):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("8400038600D9A41144FF030201020001782069702D616464726573733A61646D696E6973747261746976652D"
				+ "646F6D61696E80");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
