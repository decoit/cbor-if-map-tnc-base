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
public class CBORMacAddressTest extends AbstractTestBase {
	private final String administrativeDomain = "mac-address:administrative-domain";


	@Test
	public void testConstructor_byteArr() {
		byte[] value = new byte[] {(byte)0x00, (byte)0x80, (byte)0x41, (byte)0xae, (byte)0xfd, (byte)0x7e};

		CBORMacAddress instance = new CBORMacAddress(value);
		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("mac-address", instance.getElementName());
		assertTrue(Arrays.equals(value, instance.getValue()));
		assertNull(instance.getAdministrativeDomain());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_byteArr5() {
		byte[] value = new byte[] {(byte)0x00, (byte)0x80, (byte)0x41, (byte)0xae, (byte)0xfd};
		CBORMacAddress instance = new CBORMacAddress(value);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_byteArr7() {
		byte[] value = new byte[] {(byte)0x00, (byte)0x80, (byte)0x41, (byte)0xae, (byte)0xfd, (byte)0x7e, (byte)0x7e};
		CBORMacAddress instance = new CBORMacAddress(value);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_byteArr_null() {
		CBORMacAddress instance = new CBORMacAddress((byte[]) null);
	}


	@Test
	public void testConstructor_StringDotNotation() {
		byte[] valueDot = new byte[] {(byte)0x80, (byte)0x7e, (byte)0x41, (byte)0xae, (byte)0xfd, (byte)0x00};
		String valueDotString = "807e.41ae.fd00";

		CBORMacAddress instance = new CBORMacAddress(valueDotString);
		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("mac-address", instance.getElementName());
		assertTrue(Arrays.equals(valueDot, instance.getValue()));
		assertNull(instance.getAdministrativeDomain());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_StringDotNotation5() {
		String valueDotString = "807e.41ae.fd";

		CBORMacAddress instance = new CBORMacAddress(valueDotString);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_StringDotNotation7() {
		String valueDotString = "807e.41ae.fd00.00";

		CBORMacAddress instance = new CBORMacAddress(valueDotString);
	}


	@Test
	public void testConstructor_StringDashNotation() {
		byte[] valueDash = new byte[] {(byte)0x80, (byte)0x7e, (byte)0x41, (byte)0xae, (byte)0xfd, (byte)0x00};
		String valueDashString = "80-7e-41-ae-fd-00";

		CBORMacAddress instance = new CBORMacAddress(valueDashString);
		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("mac-address", instance.getElementName());
		assertTrue(Arrays.equals(valueDash, instance.getValue()));
		assertNull(instance.getAdministrativeDomain());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_StringDashNotation5() {
		String valueDashString = "80-7e-41-ae-fd";

		CBORMacAddress instance = new CBORMacAddress(valueDashString);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_StringDashNotation7() {
		String valueDashString = "80-7e-41-ae-fd-00-00";

		CBORMacAddress instance = new CBORMacAddress(valueDashString);
	}


	@Test
	public void testConstructor_StringColonNotation() {
		byte[] valueColon = new byte[] {(byte)0x80, (byte)0x7e, (byte)0x41, (byte)0xae, (byte)0xfd, (byte)0x00};
		String valueColonString = "80:7e:41:ae:fd:00";

		CBORMacAddress instance = new CBORMacAddress(valueColonString);
		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("mac-address", instance.getElementName());
		assertTrue(Arrays.equals(valueColon, instance.getValue()));
		assertNull(instance.getAdministrativeDomain());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_StringColonNotation5() {
		String valueDashString = "80:7e:41:ae:fd";

		CBORMacAddress instance = new CBORMacAddress(valueDashString);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_StringColonNotation7() {
		String valueDashString = "80:7e:41:ae:fd:00:00";

		CBORMacAddress instance = new CBORMacAddress(valueDashString);
	}


	@Test
	public void testConstructor_StringNoSepNotation() {
		byte[] valueNoSep = new byte[] {(byte)0x80, (byte)0x7e, (byte)0x41, (byte)0xae, (byte)0xfd, (byte)0x00};
		String valueNoSepString = "807e41aefd00";

		CBORMacAddress instance = new CBORMacAddress(valueNoSepString);
		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("mac-address", instance.getElementName());
		assertTrue(Arrays.equals(valueNoSep, instance.getValue()));
		assertNull(instance.getAdministrativeDomain());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_StringNoSepNotation5() {
		String valueNoSepString = "807e41aefd";

		CBORMacAddress instance = new CBORMacAddress(valueNoSepString);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_StringNoSepNotation7() {
		String valueNoSepString = "807e41aefd0000";

		CBORMacAddress instance = new CBORMacAddress(valueNoSepString);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_String_null() {
		CBORMacAddress instance = new CBORMacAddress((String) null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_String_EmptyString() {
		CBORMacAddress instance = new CBORMacAddress("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_String_Whitespaces() {
		CBORMacAddress instance = new CBORMacAddress("   ");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_String_IllegalSeparator() {
		String valueColonString = "80:7e:41;ae:fd:00";

		CBORMacAddress instance = new CBORMacAddress(valueColonString);
	}


	@Test
	public void testSetAdministrativeDomain() {
		byte[] value = new byte[] {(byte)0x00, (byte)0x80, (byte)0x41, (byte)0xae, (byte)0xfd, (byte)0x7e};
		CBORMacAddress instance = new CBORMacAddress(value);

		instance.setAdministrativeDomain(this.administrativeDomain);
		assertEquals(this.administrativeDomain, instance.getAdministrativeDomain());
	}


	@Test
	public void testSetAdministrativeDomain_null() {
		byte[] value = new byte[] {(byte)0x00, (byte)0x80, (byte)0x41, (byte)0xae, (byte)0xfd, (byte)0x7e};
		CBORMacAddress instance = new CBORMacAddress(value);

		instance.setAdministrativeDomain(this.administrativeDomain);
		instance.setAdministrativeDomain(null);
		assertNull(instance.getAdministrativeDomain());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetAdministrativeDomain_EmptyString() {
		byte[] value = new byte[] {(byte)0x00, (byte)0x80, (byte)0x41, (byte)0xae, (byte)0xfd, (byte)0x7e};
		CBORMacAddress instance = new CBORMacAddress(value);

		instance.setAdministrativeDomain("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetAdministrativeDomain_Whitespaces() {
		byte[] value = new byte[] {(byte)0x00, (byte)0x80, (byte)0x41, (byte)0xae, (byte)0xfd, (byte)0x7e};
		CBORMacAddress instance = new CBORMacAddress(value);

		instance.setAdministrativeDomain("   ");
	}


	@Test
	public void testCborSerialize() throws Exception {
		byte[] value = new byte[] {(byte)0x00, (byte)0x80, (byte)0x41, (byte)0xae, (byte)0xfd, (byte)0x7e};
		CBORMacAddress instance = new CBORMacAddress(value);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("8400048200D9A41346008041AEFD7E80");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		byte[] value = new byte[] {(byte)0x00, (byte)0x80, (byte)0x41, (byte)0xae, (byte)0xfd, (byte)0x7e};
		CBORMacAddress instance = new CBORMacAddress(value);
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

		byte[] expResult = DatatypeConverter.parseHexBinary("8400048400D9A41346008041AEFD7E0178216D61632D616464726573733A61646D696E697374726174697665"
				+ "2D646F6D61696E80");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
