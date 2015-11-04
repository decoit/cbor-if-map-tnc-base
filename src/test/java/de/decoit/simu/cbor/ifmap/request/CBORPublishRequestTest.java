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
package de.decoit.simu.cbor.ifmap.request;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.enums.IfMapValidationType;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.identifier.CBORDevice;
import de.decoit.simu.cbor.ifmap.identifier.CBORIpAddress;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORDeviceIp;
import de.decoit.simu.cbor.ifmap.request.model.publish.CBORPublishDelete;
import de.decoit.simu.cbor.ifmap.request.model.publish.CBORPublishNotify;
import de.decoit.simu.cbor.ifmap.request.model.publish.CBORPublishUpdate;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
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
public class CBORPublishRequestTest extends AbstractTestBase {
	private final CBORPublishUpdate pu;
	private final CBORPublishNotify pn;
	private final CBORPublishDelete pd;
	private final String sessionId = "my-session-id";

	
	public CBORPublishRequestTest() throws Exception {
		CBORDevice dev = new CBORDevice("server01", false);
		CBORIpAddress ipAddr = new CBORIpAddress(InetAddress.getByName("10.10.100.17"));
		CBORDeviceIp devIpMeta = new CBORDeviceIp();

		this.pu = new CBORPublishUpdate(dev, ipAddr);
		this.pu.addMetadata(devIpMeta);

		this.pn = new CBORPublishNotify(dev, ipAddr);
		this.pn.addMetadata(devIpMeta);

		this.pd = new CBORPublishDelete(dev, ipAddr);
	}


	@Test
	public void testConstructor() {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);

		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("publish", instance.getElementName());
		assertEquals(this.sessionId, instance.getSessionAttributes().getSessionId());
		assertNull(instance.getValidationAttributes());
		assertTrue(instance.getPublishTypes().isEmpty());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null() {
		CBORPublishRequest instance = new CBORPublishRequest(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyString() {
		CBORPublishRequest instance = new CBORPublishRequest("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Whitespaces() {
		CBORPublishRequest instance = new CBORPublishRequest("   ");
	}


	@Test
	public void testSetValidation() {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);

		instance.setValidation(IfMapValidationType.NONE);

		assertEquals(IfMapValidationType.NONE, instance.getValidationAttributes().getValidationType());
	}


	@Test
	public void testSetValidation_null() {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);

		instance.setValidation(IfMapValidationType.NONE);
		instance.setValidation(null);

		assertNull(instance.getValidationAttributes());
	}


	@Test
	public void testAddPublishType() {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);

		instance.addPublishType(this.pu);
		instance.addPublishType(this.pn);
		instance.addPublishType(this.pd);

		assertEquals(3, instance.getPublishTypes().size());
		assertEquals(this.pu, instance.getPublishTypes().get(0));
		assertEquals(this.pn, instance.getPublishTypes().get(1));
		assertEquals(this.pd, instance.getPublishTypes().get(2));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddPublishType_null() {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);

		instance.addPublishType(null);
	}


	@Test
	public void testRemovePublishType() {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);

		instance.addPublishType(this.pu);
		instance.addPublishType(this.pd);

		instance.removePublishType(this.pd);
		assertEquals(1, instance.getPublishTypes().size());
	}


	@Test
	public void testRemovePublishType_null() {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);

		instance.addPublishType(this.pu);
		instance.addPublishType(this.pd);

		instance.removePublishType(null);
		assertEquals(2, instance.getPublishTypes().size());
	}


	@Test
	public void testRemovePublishType_SameTwice() {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);

		instance.addPublishType(this.pu);
		instance.addPublishType(this.pd);

		instance.removePublishType(this.pd);
		instance.removePublishType(this.pd);
		assertEquals(1, instance.getPublishTypes().size());
	}


	@Test
	public void testRemovePublishType_NonExistent() {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);

		instance.addPublishType(this.pu);
		instance.addPublishType(this.pd);

		instance.removePublishType(this.pn);
		assertEquals(2, instance.getPublishTypes().size());
	}


	@Test
	public void testCborSerialize_OnePublishType() throws Exception {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);
		instance.addPublishType(this.pu);
		instance.setValidation(IfMapValidationType.ALL);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (1 type):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000584006D6D792D73657373696F6E2D6964010384F6008200008C00018084F60080687365727665723031"
				+ "00038400D9A411440A0A6411020080F6008084010882030080");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_ThreePublishTypes() throws Exception {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);
		instance.addPublishType(this.pu);
		instance.addPublishType(this.pd);
		instance.addPublishType(this.pn);
		instance.setValidation(IfMapValidationType.ALL);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (3 types):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000584006D6D792D73657373696F6E2D696401038CF6008200008C00018084F60080687365727665723031"
				+ "00038400D9A411440A0A6411020080F6008084010882030080F602808800018084F6008068736572766572303100038400D9A411440A0A6411020080F601820000"
				+ "8C00018084F6008068736572766572303100038400D9A411440A0A6411020080F6008084010882030080");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test(expected = CBORSerializationException.class)
	public void testCborSerialize_NoPublishTypes() throws Exception {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);
	}
}
