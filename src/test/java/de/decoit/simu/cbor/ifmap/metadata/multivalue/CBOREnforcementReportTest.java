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
import de.decoit.simu.cbor.ifmap.enums.IfMapEnforcementAction;
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
public class CBOREnforcementReportTest extends AbstractTestBase {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String otherTypeDefinition = "other-type-definition";
	private final String enforcementReason = "just-for-fun";


	@Test
	public void testConstructor() {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.QUARANTINE);

		assertEquals(IfMapNamespaces.IFMAP_METADATA, instance.getNamespace());
		assertEquals(CBOREnforcementReport.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.MULTI_VALUE, instance.getIfMapCardinality());
		assertEquals(IfMapEnforcementAction.QUARANTINE, instance.getEnforcementAction());
		assertNull(instance.getOtherTypeDefinition());
		assertNull(instance.getEnforcementReason());
	}


	@Test
	public void testConstructor_Type_Otd() {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.QUARANTINE, this.otherTypeDefinition);

		assertEquals(IfMapNamespaces.IFMAP_METADATA, instance.getNamespace());
		assertEquals(CBOREnforcementReport.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.MULTI_VALUE, instance.getIfMapCardinality());
		assertEquals(IfMapEnforcementAction.QUARANTINE, instance.getEnforcementAction());
		assertNull(instance.getOtherTypeDefinition());
		assertNull(instance.getEnforcementReason());
	}


	@Test
	public void testConstructor_Other() {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.OTHER, this.otherTypeDefinition);

		assertEquals(IfMapNamespaces.IFMAP_METADATA, instance.getNamespace());
		assertEquals(CBOREnforcementReport.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.MULTI_VALUE, instance.getIfMapCardinality());
		assertEquals(IfMapEnforcementAction.OTHER, instance.getEnforcementAction());
		assertEquals(this.otherTypeDefinition, instance.getOtherTypeDefinition());
		assertNull(instance.getEnforcementReason());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullType() {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Other_NoOtd() {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.OTHER);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Other_NullOtd() {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.OTHER, null);
	}


	@Test
	public void testSetEnforcementReason() {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.BLOCK);
		instance.setEnforcementReason(this.enforcementReason);
		assertEquals(this.enforcementReason, instance.getEnforcementReason());
	}


	@Test
	public void testSetEnforcementReason_null() {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.BLOCK);
		instance.setEnforcementReason(this.enforcementReason);
		instance.setEnforcementReason(null);
		assertNull(instance.getEnforcementReason());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetEnforcementReason_EmptyString() {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.BLOCK);
		instance.setEnforcementReason("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetEnforcementReason_Whitespaces() {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.BLOCK);
		instance.setEnforcementReason("   ");
	}


	@Test
	public void testCborSerialize_TypeBlock() throws Exception {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.BLOCK);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (block):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010A88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030184F6008000");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_TypeQuarantine() throws Exception {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.QUARANTINE);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (quarantine):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010A88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030184F6008001");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_TypeOtherFull() throws Exception {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.OTHER, this.otherTypeDefinition);
		instance.setEnforcementReason(this.enforcementReason);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (other, full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010A88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003018CF6008002"
				+ "F60180756F746865722D747970652D646566696E6974696F6EF602806C6A7573742D666F722D66756E");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
