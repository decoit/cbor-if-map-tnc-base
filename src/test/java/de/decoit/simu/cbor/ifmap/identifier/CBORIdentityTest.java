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
import de.decoit.simu.cbor.ifmap.enums.IfMapIdentityType;
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
public class CBORIdentityTest extends AbstractTestBase {
	private final String name = "identity:name";
	private final String administrativeDomain = "identity:administrative-domain";
	private final String otherTypeDefinition = "identity:my-custom-type";


	@Test
	public void testConstructor() {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.AIK_NAME);
		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("identity", instance.getElementName());
		assertEquals(this.name, instance.getName());
		assertEquals(IfMapIdentityType.AIK_NAME, instance.getType());
		assertNull(instance.getOtherTypeDefinition());
		assertNull(instance.getAdministrativeDomain());
	}


	@Test
	public void testConstructor_NotOther_OTD() {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.AIK_NAME, this.otherTypeDefinition);
		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("identity", instance.getElementName());
		assertEquals(this.name, instance.getName());
		assertEquals(IfMapIdentityType.AIK_NAME, instance.getType());
		assertNull(instance.getOtherTypeDefinition());
		assertNull(instance.getAdministrativeDomain());
	}


	@Test
	public void testConstructor_Other_OTD() {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.OTHER, this.otherTypeDefinition);
		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("identity", instance.getElementName());
		assertEquals(this.name, instance.getName());
		assertEquals(IfMapIdentityType.OTHER, instance.getType());
		assertEquals(this.otherTypeDefinition, instance.getOtherTypeDefinition());
		assertNull(instance.getAdministrativeDomain());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null_Type() {
		CBORIdentity instance = new CBORIdentity(null, IfMapIdentityType.AIK_NAME);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyString_Type() {
		CBORIdentity instance = new CBORIdentity("", IfMapIdentityType.AIK_NAME);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Whitespaces_Type() {
		CBORIdentity instance = new CBORIdentity("   ", IfMapIdentityType.AIK_NAME);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_String_null() {
		CBORIdentity instance = new CBORIdentity(this.name, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_String_Other() {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.OTHER);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_String_Other_null() {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.OTHER, null);
	}


	@Test
	public void testCborSerialize_AikName() throws Exception {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.AIK_NAME);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (aik-name):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000284006D6964656E746974793A6E616D65020080");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_DistinguishedName() throws Exception {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.DISTINGUISHED_NAME);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (distinguished-name):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000284006D6964656E746974793A6E616D65020180");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_DnsName() throws Exception {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.DNS_NAME);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (dns-name):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000284006D6964656E746974793A6E616D65020280");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_EmailAddress() throws Exception {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.EMAIL_ADDRESS);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (email-address):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000284006D6964656E746974793A6E616D65020380");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_KerberosPrincipal() throws Exception {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.KERBEROS_PRINCIPAL);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (kerberos-principal):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000284006D6964656E746974793A6E616D65020480");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_Username() throws Exception {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.USERNAME);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (username):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000284006D6964656E746974793A6E616D65020580");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_SipUri() throws Exception {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.SIP_URI);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (sip-uri):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000284006D6964656E746974793A6E616D65020680");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_TelUri() throws Exception {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.TEL_URI);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (tel-uri):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000284006D6964656E746974793A6E616D65020780");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_HipHit() throws Exception {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.HIP_HIT);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (hip-hit):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000284006D6964656E746974793A6E616D65020880");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_Other_AdmDomain() throws Exception {
		CBORIdentity instance = new CBORIdentity(this.name, IfMapIdentityType.OTHER, this.otherTypeDefinition);
		instance.setAdministrativeDomain(this.administrativeDomain);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (other & administrative-domain):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000288006D6964656E746974793A6E616D65020901781E6964656E746974793A61646D696E697374726174"
				+ "6976652D646F6D61696E03776964656E746974793A6D792D637573746F6D2D7479706580");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
