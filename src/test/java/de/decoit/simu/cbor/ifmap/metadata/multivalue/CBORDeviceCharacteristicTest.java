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
public class CBORDeviceCharacteristicTest extends AbstractTestBase {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final ZonedDateTime discoveredTime = ZonedDateTime.parse("2011-12-03T10:10:15.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String discovererId = "discoverer-publisher-id";
	private final String discoveryMethod = "device-characteristic:discovery-method";
	private final String manufacturer = "manufacturer-0815";
	private final String model = "model-0815-2";
	private final String os = "operating-system-0815";
	private final String osVersion = "v42";
	private final String deviceType = "device-0815";


	@Test
	public void testConstructor() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);

		assertEquals(IfMapNamespaces.IFMAP_METADATA, instance.getNamespace());
		assertEquals(CBORDeviceCharacteristic.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.MULTI_VALUE, instance.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.discoveredTime), instance.getDiscoveredTime());
		assertEquals(this.discovererId, instance.getDiscovererId());
		assertEquals(this.discoveryMethod, instance.getDiscoveryMethod());
		assertNull(instance.getManufacturer());
		assertNull(instance.getModel());
		assertNull(instance.getOs());
		assertNull(instance.getOsVersion());
		assertNull(instance.getDeviceType());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullDiscoveredTime() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, null, this.discovererId, this.discoveryMethod);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullDiscovererId() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, null, this.discoveryMethod);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyDiscovererId() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, "", this.discoveryMethod);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_WhitespaceDiscovererId() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, "   ", this.discoveryMethod);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullDiscoveryMethod() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyDiscoveryMethod() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, "");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_WhitespaceDiscoveryMethod() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, "   ");
	}


	@Test
	public void testSetManufacturer() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setManufacturer(this.manufacturer);
		assertEquals(this.manufacturer, instance.getManufacturer());
	}


	@Test
	public void testSetManufacturer_null() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setManufacturer(this.manufacturer);
		instance.setManufacturer(null);
		assertNull(instance.getManufacturer());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetManufacturer_EmptyString() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setManufacturer("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetManufacturer_Whitespaces() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setManufacturer("   ");
	}


	@Test
	public void testSetModel() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setModel(this.model);
		assertEquals(this.model, instance.getModel());
	}


	@Test
	public void testSetModel_null() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setModel(this.model);
		instance.setModel(null);
		assertNull(instance.getModel());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetModel_EmptyString() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setModel("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetModel_Whitespaces() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setModel("   ");
	}


	@Test
	public void testSetOs() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setOs(this.os);
		assertEquals(this.os, instance.getOs());
	}


	@Test
	public void testSetOs_null() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setOs(this.os);
		instance.setOs(null);
		assertNull(instance.getOs());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetOs_EmptyString() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setOs("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetOs_Whitespaces() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setOs("   ");
	}


	@Test
	public void testSetOsVersion() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setOsVersion(this.osVersion);
		assertEquals(this.osVersion, instance.getOsVersion());
	}


	@Test
	public void testSetOsVersion_null() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setOsVersion(this.osVersion);
		instance.setOsVersion(null);
		assertNull(instance.getOsVersion());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetOsVersion_EmptyString() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setOsVersion("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetOsVersion_Whitespaces() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setOsVersion("   ");
	}


	@Test
	public void testSetDeviceType() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setDeviceType(this.deviceType);
		assertEquals(this.deviceType, instance.getDeviceType());
	}


	@Test
	public void testSetDeviceType_null() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setDeviceType(this.deviceType);
		instance.setDeviceType(null);
		assertNull(instance.getDeviceType());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetDeviceType_EmptyString() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setDeviceType("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetDeviceType_Whitespaces() {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setDeviceType("   ");
	}


	@Test
	public void testCborSerialize() throws Exception {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010788006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003018CF60580C1"
				+ "1A4ED9E777F6068077646973636F76657265722D7075626C69736865722D6964F6078078266465766963652D63686172616374657269737469633A646973636F76"
				+ "6572792D6D6574686F64");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setManufacturer(this.manufacturer);
		instance.setModel(this.model);
		instance.setOs(this.os);
		instance.setOsVersion(this.osVersion);
		instance.setDeviceType(this.deviceType);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010788006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003019820F60580"
				+ "C11A4ED9E777F6068077646973636F76657265722D7075626C69736865722D6964F6078078266465766963652D63686172616374657269737469633A646973636F"
				+ "766572792D6D6574686F64F60080716D616E7566616374757265722D30383135F601806C6D6F64656C2D303831352D32F60280756F7065726174696E672D737973"
				+ "74656D2D30383135F6038063763432F604806B6465766963652D30383135");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
