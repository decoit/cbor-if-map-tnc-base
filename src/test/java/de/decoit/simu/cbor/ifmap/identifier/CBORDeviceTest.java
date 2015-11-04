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
public class CBORDeviceTest extends AbstractTestBase {
	private final String name = "device:name";
	private final String aikName = "device:aik-name";


	@Test
	public void testConstructor_Name() {
		CBORDevice instance = new CBORDevice(this.name, false);
		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("device", instance.getElementName());
		assertEquals("device:name", instance.getName());
		assertFalse(instance.isAikName());
	}


	@Test
	public void testConstructor_AikName() {
		CBORDevice instance = new CBORDevice(this.aikName, true);
		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("device", instance.getElementName());
		assertEquals("device:aik-name", instance.getName());
		assertTrue(instance.isAikName());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Name_null() {
		CBORDevice instance = new CBORDevice(null, false);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Name_EmptyString() {
		CBORDevice instance = new CBORDevice("", false);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Name_Whitespaces() {
		CBORDevice instance = new CBORDevice("   ", false);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_AikName_null() {
		CBORDevice instance = new CBORDevice(null, true);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_AikName_EmptyString() {
		CBORDevice instance = new CBORDevice("", true);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_AikName_Whitespaces() {
		CBORDevice instance = new CBORDevice("   ", true);
	}


	@Test
	public void testCborSerialize_Name() throws Exception {
		CBORDevice instance = new CBORDevice(this.name, false);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (name):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("8400018084F600806B6465766963653A6E616D65");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_AikName() throws Exception {
		CBORDevice instance = new CBORDevice(this.aikName, true);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (aik-name):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("8400018084F601806F6465766963653A61696B2D6E616D65");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
