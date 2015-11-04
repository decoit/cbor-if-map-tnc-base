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
package de.decoit.simu.cbor.ifmap.deserializer;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.enums.IfMapIdentityType;
import de.decoit.simu.cbor.ifmap.enums.IfMapIpAddressType;
import de.decoit.simu.cbor.ifmap.identifier.CBORAccessRequest;
import de.decoit.simu.cbor.ifmap.identifier.CBORDevice;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import de.decoit.simu.cbor.ifmap.identifier.CBORIpAddress;
import de.decoit.simu.cbor.ifmap.identifier.CBORMacAddress;
import java.io.ByteArrayInputStream;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class IdentifierDeserializerManagerTest extends AbstractTestBase {
	@Test
	public void testDeserialize_CBORAccessRequest() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("8400008200736163636573732D726571756573743A6E616D6580");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORAccessRequest ar = IdentifierDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORAccessRequest.class);
		assertEquals("access-request:name", ar.getName());
		assertNull(ar.getAdministrativeDomain());
	}


	@Test
	public void testDeserialize_CBORAccessRequest_full() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("8400008400736163636573732D726571756573743A6E616D6501782"
				+ "46163636573732D726571756573743A61646D696E6973747261746976652D646F6D61696E80");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORAccessRequest ar = IdentifierDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORAccessRequest.class);
		assertEquals("access-request:name", ar.getName());
		assertEquals("access-request:administrative-domain", ar.getAdministrativeDomain());
	}


	@Test
	public void testDeserialize_CBORDevice_name() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("8400018084F600806B6465766963653A6E616D65");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORDevice dev = IdentifierDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORDevice.class);
		assertEquals("device:name", dev.getName());
		assertFalse(dev.isAikName());
	}


	@Test
	public void testDeserialize_CBORDevice_aikName() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("8400018084F601806F6465766963653A61696B2D6E616D65");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORDevice dev = IdentifierDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORDevice.class);
		assertEquals("device:aik-name", dev.getName());
		assertTrue(dev.isAikName());
	}


	@Test
	public void testDeserialize_CBORIdentity() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84000284006D6964656E746974793A6E616D65020080");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORIdentity ident = IdentifierDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORIdentity.class);
		assertEquals("identity:name", ident.getName());
		assertNull(ident.getAdministrativeDomain());
		assertEquals(IfMapIdentityType.AIK_NAME, ident.getType());
		assertNull(ident.getOtherTypeDefinition());
	}


	@Test
	public void testDeserialize_CBORIdentity_OtherAdmDomain() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84000288006D6964656E746974793A6E616D65020901781E6964656E746974793A61646D696E"
				+ "6973747261746976652D646F6D61696E03776964656E746974793A6D792D637573746F6D2D7479706580");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORIdentity ident = IdentifierDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORIdentity.class);
		assertEquals("identity:name", ident.getName());
		assertEquals("identity:administrative-domain", ident.getAdministrativeDomain());
		assertEquals(IfMapIdentityType.OTHER, ident.getType());
		assertEquals("identity:my-custom-type", ident.getOtherTypeDefinition());
	}



	@Test
	public void testDeserialize_CBORIpAddress_IPv4AdmDomain() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("8400038600D9A41144FF030201020001782069702D616464726573733A61646D696E6973747261746976652D646F6D61696E80");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORIpAddress ipAddr = IdentifierDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORIpAddress.class);
		assertEquals(InetAddress.getByName("255.3.2.1"), ipAddr.getValue());
		assertEquals(IfMapIpAddressType.IPV4, ipAddr.getType());
		assertEquals("ip-address:administrative-domain", ipAddr.getAdministrativeDomain());
	}


	@Test
	public void testDeserialize_CBORIpAddress_IPv6() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("8400038400D9A4125020010DB885A308D313198A2E03707344020180");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORIpAddress ipAddr = IdentifierDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORIpAddress.class);
		assertEquals(InetAddress.getByName("2001:0db8:85a3:08d3:1319:8a2e:0370:7344"), ipAddr.getValue());
		assertEquals(IfMapIpAddressType.IPV6, ipAddr.getType());
		assertNull(ipAddr.getAdministrativeDomain());
	}



	@Test
	public void testDeserialize_CBORMacAddress() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("8400048200D9A41346008041AEFD7E80");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		byte[] expValue = DatatypeConverter.parseHexBinary("008041aefd7e");

		CBORMacAddress macAddr = IdentifierDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORMacAddress.class);
		assertTrue(Arrays.equals(expValue, macAddr.getValue()));
		assertNull(macAddr.getAdministrativeDomain());
	}


	@Test
	public void testDeserialize_CBORMacAddress_full() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("8400048400D9A41346008041AEFD7E0178216D61632D616464726573733A61646D696E6973747261746976652D646F6D61696E80");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		byte[] expValue = DatatypeConverter.parseHexBinary("008041aefd7e");

		CBORMacAddress macAddr = IdentifierDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORMacAddress.class);
		assertTrue(Arrays.equals(expValue, macAddr.getValue()));
		assertEquals("mac-address:administrative-domain", macAddr.getAdministrativeDomain());
	}
}
