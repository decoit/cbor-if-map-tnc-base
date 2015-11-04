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
public class CBORIpMacTest extends AbstractTestBase {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final ZonedDateTime startTime = ZonedDateTime.parse("2011-12-03T10:00:00.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final ZonedDateTime endTime = ZonedDateTime.parse("2011-12-03T12:30:00.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String dhcpServer = "dhcp-0815";


	@Test
	public void testConstructor() {
		CBORIpMac instance = new CBORIpMac(this.publisherId, this.ifMapTimestamp);

		assertEquals(IfMapNamespaces.IFMAP_METADATA, instance.getNamespace());
		assertEquals(CBORIpMac.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.MULTI_VALUE, instance.getIfMapCardinality());
		assertNull(instance.getStartTime());
		assertNull(instance.getEndTime());
		assertNull(instance.getDhcpServer());
	}


	@Test
	public void testSetStartTime() {
		CBORIpMac instance = new CBORIpMac(this.publisherId, this.ifMapTimestamp);
		instance.setStartTime(this.startTime);
		assertEquals(TimestampHelper.toUTC(this.startTime), instance.getStartTime());
	}


	@Test
	public void testSetStartTime_null() {
		CBORIpMac instance = new CBORIpMac(this.publisherId, this.ifMapTimestamp);
		instance.setStartTime(this.startTime);
		instance.setStartTime(null);
		assertNull(instance.getStartTime());
	}


	@Test
	public void testSetEndTime() {
		CBORIpMac instance = new CBORIpMac(this.publisherId, this.ifMapTimestamp);
		instance.setEndTime(this.endTime);
		assertEquals(TimestampHelper.toUTC(this.endTime), instance.getEndTime());
	}


	@Test
	public void testSetEndTime_null() {
		CBORIpMac instance = new CBORIpMac(this.publisherId, this.ifMapTimestamp);
		instance.setEndTime(this.endTime);
		instance.setEndTime(null);
		assertNull(instance.getEndTime());
	}


	@Test
	public void testSetDhcpServer() {
		CBORIpMac instance = new CBORIpMac(this.publisherId, this.ifMapTimestamp);
		instance.setDhcpServer(this.dhcpServer);
		assertEquals(this.dhcpServer, instance.getDhcpServer());
	}


	@Test
	public void testSetDhcpServer_null() {
		CBORIpMac instance = new CBORIpMac(this.publisherId, this.ifMapTimestamp);
		instance.setDhcpServer(this.dhcpServer);
		instance.setDhcpServer(null);
		assertNull(instance.getDhcpServer());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetDhcpServer_EmptyString() {
		CBORIpMac instance = new CBORIpMac(this.publisherId, this.ifMapTimestamp);
		instance.setDhcpServer("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetDhcpServer_Whitespaces() {
		CBORIpMac instance = new CBORIpMac(this.publisherId, this.ifMapTimestamp);
		instance.setDhcpServer("   ");
	}


	@Test
	public void testCborSerialize() throws Exception {
		CBORIpMac instance = new CBORIpMac(this.publisherId, this.ifMapTimestamp);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010C88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030180");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		CBORIpMac instance = new CBORIpMac(this.publisherId, this.ifMapTimestamp);
		instance.setStartTime(this.startTime);
		instance.setEndTime(this.endTime);
		instance.setDhcpServer(this.dhcpServer);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010C88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003018CF60080C1"
				+ "1A4ED9E510F60180C11A4EDA0838F6028069646863702D30383135");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
