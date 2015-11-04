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
package de.decoit.simu.cbor.ifmap.metadata.singlevalue;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.enums.IfMapCardinality;
import de.decoit.simu.cbor.ifmap.enums.IfMapWlanSecurityType;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
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
public class CBORWlanInformationTest extends AbstractTestBase {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String ssid = "wlan-ssid";
	private final IfMapWlanSecurityType groupSecurityType = IfMapWlanSecurityType.TKIP;
	private final String otherTypeDefinition = "other-type-definition";


	@Test
	public void testConstructor() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);

		assertEquals(IfMapNamespaces.IFMAP_METADATA, instance.getNamespace());
		assertEquals(CBORWlanInformation.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.SINGLE_VALUE, instance.getIfMapCardinality());
		assertEquals(this.groupSecurityType, instance.getSsidGroupSecurity().getWlanSecurityType());
		assertNull(instance.getSsidGroupSecurity().getOtherTypeDefinition());
		assertNull(instance.getSsid());
		assertTrue(instance.getSsidManagementSecurity().isEmpty());
		assertTrue(instance.getSsidUnicastSecurity().isEmpty());
	}


	@Test
	public void testConstructor_NotOther_OTD() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType, otherTypeDefinition);

		assertEquals(IfMapNamespaces.IFMAP_METADATA, instance.getNamespace());
		assertEquals(CBORWlanInformation.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.SINGLE_VALUE, instance.getIfMapCardinality());
		assertEquals(this.groupSecurityType, instance.getSsidGroupSecurity().getWlanSecurityType());
		assertNull(instance.getSsidGroupSecurity().getOtherTypeDefinition());
		assertNull(instance.getSsid());
		assertTrue(instance.getSsidManagementSecurity().isEmpty());
		assertTrue(instance.getSsidUnicastSecurity().isEmpty());
	}


	@Test
	public void testConstructor_Other_OTD() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, IfMapWlanSecurityType.OTHER, otherTypeDefinition);

		assertEquals(IfMapNamespaces.IFMAP_METADATA, instance.getNamespace());
		assertEquals(CBORWlanInformation.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.SINGLE_VALUE, instance.getIfMapCardinality());
		assertEquals(IfMapWlanSecurityType.OTHER, instance.getSsidGroupSecurity().getWlanSecurityType());
		assertEquals(this.otherTypeDefinition, instance.getSsidGroupSecurity().getOtherTypeDefinition());
		assertNull(instance.getSsid());
		assertTrue(instance.getSsidManagementSecurity().isEmpty());
		assertTrue(instance.getSsidUnicastSecurity().isEmpty());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Other_NoOTD() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, IfMapWlanSecurityType.OTHER);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Other_Null() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, IfMapWlanSecurityType.OTHER, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Other_EmptyString() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, IfMapWlanSecurityType.OTHER, "");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Other_Whitespaces() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, IfMapWlanSecurityType.OTHER, "   ");
	}


	@Test
	public void testSetSsid() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.setSsid(this.ssid);

		assertEquals(this.ssid, instance.getSsid());
	}


	@Test
	public void testSetSsid_null() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.setSsid(this.ssid);
		instance.setSsid(null);

		assertNull(instance.getSsid());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetSsid_EmptyString() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.setSsid("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetSsid_Whitespaces() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.setSsid("   ");
	}


	@Test
	public void testAddSsidUnicastSecurity() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);

		assertTrue(instance.getSsidUnicastSecurity().size() == 1);
		assertTrue(instance.hasSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null));
	}


	@Test
	public void testAddSsidUnicastSecurity_SameTwice() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);

		assertTrue(instance.getSsidUnicastSecurity().size() == 1);
		assertTrue(instance.hasSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null));
	}


	@Test
	public void testAddSsidUnicastSecurity_TwoDifferent() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.TKIP, null);

		assertTrue(instance.getSsidUnicastSecurity().size() == 2);
		assertTrue(instance.hasSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null));
		assertTrue(instance.hasSsidUnicastSecurity(IfMapWlanSecurityType.TKIP, null));
	}


	@Test
	public void testAddSsidUnicastSecurity_Other_OTD() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.OTHER, this.otherTypeDefinition);

		assertTrue(instance.getSsidUnicastSecurity().size() == 1);
		assertTrue(instance.hasSsidUnicastSecurity(IfMapWlanSecurityType.OTHER, this.otherTypeDefinition));
	}


	@Test
	public void testAddSsidUnicastSecurity_NotOther_OTD() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.BIP, this.otherTypeDefinition);

		assertTrue(instance.getSsidUnicastSecurity().size() == 1);
		assertTrue(instance.hasSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddSsidUnicastSecurity_null_OTD() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidUnicastSecurity(null, this.otherTypeDefinition);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddSsidUnicastSecurity_Other_null() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.OTHER, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddSsidUnicastSecurity_Other_EmptyString() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.OTHER, "");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddSsidUnicastSecurity_Other_Whitespaces() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.OTHER, "   ");
	}


	@Test
	public void testRemoveSsidUnicastSecurity() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.TKIP, null);

		instance.removeSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);

		assertTrue(instance.getSsidUnicastSecurity().size() == 1);
		assertFalse(instance.hasSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null));
		assertTrue(instance.hasSsidUnicastSecurity(IfMapWlanSecurityType.TKIP, null));
	}


	@Test
	public void testRemoveSsidUnicastSecurity_SameTwice() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.TKIP, null);

		instance.removeSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);
		instance.removeSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);

		assertTrue(instance.getSsidUnicastSecurity().size() == 1);
		assertFalse(instance.hasSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null));
		assertTrue(instance.hasSsidUnicastSecurity(IfMapWlanSecurityType.TKIP, null));
	}


	@Test
	public void testRemoveSsidUnicastSecurity_NotExistent() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.TKIP, null);

		instance.removeSsidUnicastSecurity(IfMapWlanSecurityType.CCMP, null);

		assertTrue(instance.getSsidUnicastSecurity().size() == 2);
		assertTrue(instance.hasSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null));
		assertTrue(instance.hasSsidUnicastSecurity(IfMapWlanSecurityType.TKIP, null));
	}


	@Test
	public void testRemoveSsidUnicastSecurity_Other_OTD() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.OTHER, this.otherTypeDefinition);

		instance.removeSsidUnicastSecurity(IfMapWlanSecurityType.OTHER, this.otherTypeDefinition);

		assertTrue(instance.getSsidUnicastSecurity().size() == 1);
		assertTrue(instance.hasSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null));
		assertFalse(instance.hasSsidUnicastSecurity(IfMapWlanSecurityType.OTHER, this.otherTypeDefinition));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveSsidUnicastSecurity_null_OTD() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.removeSsidUnicastSecurity(null, this.otherTypeDefinition);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveSsidUnicastSecurity_Other_null() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.removeSsidUnicastSecurity(IfMapWlanSecurityType.OTHER, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveSsidUnicastSecurity_Other_EmptyString() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.removeSsidUnicastSecurity(IfMapWlanSecurityType.OTHER, "");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveSsidUnicastSecurity_Other_Whitespaces() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.removeSsidUnicastSecurity(IfMapWlanSecurityType.OTHER, "   ");
	}


	@Test
	public void testAddSsidManagementSecurity() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);

		assertTrue(instance.getSsidManagementSecurity().size() == 1);
		assertTrue(instance.hasSsidManagementSecurity(IfMapWlanSecurityType.BIP, null));
	}


	@Test
	public void testAddSsidManagementSecurity_SameTwice() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);

		assertTrue(instance.getSsidManagementSecurity().size() == 1);
		assertTrue(instance.hasSsidManagementSecurity(IfMapWlanSecurityType.BIP, null));
	}


	@Test
	public void testAddSsidManagementSecurity_TwoDifferent() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.TKIP, null);

		assertTrue(instance.getSsidManagementSecurity().size() == 2);
		assertTrue(instance.hasSsidManagementSecurity(IfMapWlanSecurityType.BIP, null));
		assertTrue(instance.hasSsidManagementSecurity(IfMapWlanSecurityType.TKIP, null));
	}


	@Test
	public void testAddSsidManagementSecurity_Other_OTD() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.OTHER, this.otherTypeDefinition);

		assertTrue(instance.getSsidManagementSecurity().size() == 1);
		assertTrue(instance.hasSsidManagementSecurity(IfMapWlanSecurityType.OTHER, this.otherTypeDefinition));
	}


	@Test
	public void testAddSsidManagementSecurity_NotOther_OTD() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.BIP, this.otherTypeDefinition);

		assertTrue(instance.getSsidManagementSecurity().size() == 1);
		assertTrue(instance.hasSsidManagementSecurity(IfMapWlanSecurityType.BIP, null));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddSsidManagementSecurity_null_OTD() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(null, this.otherTypeDefinition);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddSsidManagementSecurity_Other_null() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.OTHER, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddSsidManagementSecurity_Other_EmptyString() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.OTHER, "");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddSsidManagementSecurity_Other_Whitespaces() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.OTHER, "   ");
	}


	@Test
	public void testRemoveSsidManagementSecurity() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.TKIP, null);

		instance.removeSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);

		assertTrue(instance.getSsidManagementSecurity().size() == 1);
		assertFalse(instance.hasSsidManagementSecurity(IfMapWlanSecurityType.BIP, null));
		assertTrue(instance.hasSsidManagementSecurity(IfMapWlanSecurityType.TKIP, null));
	}


	@Test
	public void testRemoveSsidManagementSecurity_SameTwice() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.TKIP, null);

		instance.removeSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);
		instance.removeSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);

		assertTrue(instance.getSsidManagementSecurity().size() == 1);
		assertFalse(instance.hasSsidManagementSecurity(IfMapWlanSecurityType.BIP, null));
		assertTrue(instance.hasSsidManagementSecurity(IfMapWlanSecurityType.TKIP, null));
	}


	@Test
	public void testRemoveSsidManagementSecurity_NotExistent() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.TKIP, null);

		instance.removeSsidManagementSecurity(IfMapWlanSecurityType.CCMP, null);

		assertTrue(instance.getSsidManagementSecurity().size() == 2);
		assertTrue(instance.hasSsidManagementSecurity(IfMapWlanSecurityType.BIP, null));
		assertTrue(instance.hasSsidManagementSecurity(IfMapWlanSecurityType.TKIP, null));
	}


	@Test
	public void testRemoveSsidManagementSecurity_Other_OTD() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.OTHER, this.otherTypeDefinition);

		instance.removeSsidManagementSecurity(IfMapWlanSecurityType.OTHER, this.otherTypeDefinition);

		assertTrue(instance.getSsidManagementSecurity().size() == 1);
		assertTrue(instance.hasSsidManagementSecurity(IfMapWlanSecurityType.BIP, null));
		assertFalse(instance.hasSsidManagementSecurity(IfMapWlanSecurityType.OTHER, this.otherTypeDefinition));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveSsidManagementSecurity_null_OTD() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.removeSsidManagementSecurity(null, this.otherTypeDefinition);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveSsidManagementSecurity_Other_null() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.removeSsidManagementSecurity(IfMapWlanSecurityType.OTHER, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveSsidManagementSecurity_Other_EmptyString() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.removeSsidManagementSecurity(IfMapWlanSecurityType.OTHER, "");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveSsidManagementSecurity_Other_Whitespaces() {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.removeSsidManagementSecurity(IfMapWlanSecurityType.OTHER, "   ");
	}


	@Test
	public void testCborSerialize() throws Exception {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.TKIP, null);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.TKIP, null);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84011288006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030094F6018004"
				+ "F6018002F6028002F6038004F6038002");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, IfMapWlanSecurityType.OTHER, this.otherTypeDefinition);
		instance.setSsid(this.ssid);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.OTHER, this.otherTypeDefinition);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.OTHER, this.otherTypeDefinition);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84011288006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003009818F60080"
				+ "69776C616E2D73736964F6018004F6018200756F746865722D747970652D646566696E6974696F6E05F6028200756F746865722D747970652D646566696E697469"
				+ "6F6E05F6038004F6038200756F746865722D747970652D646566696E6974696F6E05");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test(expected = CBORSerializationException.class)
	public void testCborSerialize_emptyUnicastSecuritySet() throws Exception {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);
	}


	@Test(expected = CBORSerializationException.class)
	public void testCborSerialize_emptyManagementSecuritySet() throws Exception {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);
	}
}
