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
public class CBORRequestForInvestigationTest extends AbstractTestBase {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String qualifier = "rfi-qualifier";


	@Test
	public void testConstructor() {
		CBORRequestForInvestigation instance = new CBORRequestForInvestigation(this.publisherId, this.ifMapTimestamp);

		assertEquals(IfMapNamespaces.IFMAP_METADATA, instance.getNamespace());
		assertEquals(CBORRequestForInvestigation.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.MULTI_VALUE, instance.getIfMapCardinality());
		assertNull(instance.getQualifier());
	}


	@Test
	public void testSetQualifier() {
		CBORRequestForInvestigation instance = new CBORRequestForInvestigation(this.publisherId, this.ifMapTimestamp);
		instance.setQualifier(this.qualifier);
		assertEquals(this.qualifier, instance.getQualifier());
	}


	@Test
	public void testSetQualifier_null() {
		CBORRequestForInvestigation instance = new CBORRequestForInvestigation(this.publisherId, this.ifMapTimestamp);
		instance.setQualifier(this.qualifier);
		instance.setQualifier(null);
		assertNull(instance.getQualifier());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetQualifier_EmptyString() {
		CBORRequestForInvestigation instance = new CBORRequestForInvestigation(this.publisherId, this.ifMapTimestamp);
		instance.setQualifier("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetQualifier_Whitespaces() {
		CBORRequestForInvestigation instance = new CBORRequestForInvestigation(this.publisherId, this.ifMapTimestamp);
		instance.setQualifier("   ");
	}


	@Test
	public void testCborSerialize() throws Exception {
		CBORRequestForInvestigation instance = new CBORRequestForInvestigation(this.publisherId, this.ifMapTimestamp);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010F88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030180");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		CBORRequestForInvestigation instance = new CBORRequestForInvestigation(this.publisherId, this.ifMapTimestamp);
		instance.setQualifier(this.qualifier);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010F88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030184F600806D"
				+ "7266692D7175616C6966696572");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
