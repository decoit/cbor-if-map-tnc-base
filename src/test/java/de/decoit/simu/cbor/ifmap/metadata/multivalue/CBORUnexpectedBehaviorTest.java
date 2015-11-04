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
import de.decoit.simu.cbor.ifmap.enums.IfMapSignificance;
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
public class CBORUnexpectedBehaviorTest extends AbstractTestBase {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final ZonedDateTime discoveredTime = ZonedDateTime.parse("2011-12-03T10:10:15.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String discovererId = "discoverer-publisher-id";
	private final Integer magnitude = 5;
	private final IfMapSignificance significance = IfMapSignificance.CRITICAL;
	private final String information = "behavior-information";
	private final String type = "behavior-type";
	private final Integer confidence = 42;


	@Test
	public void testConstructor() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, this.significance);

		assertEquals(IfMapNamespaces.IFMAP_METADATA, instance.getNamespace());
		assertEquals(CBORUnexpectedBehavior.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.MULTI_VALUE, instance.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.discoveredTime), instance.getDiscoveredTime());
		assertEquals(this.discovererId, instance.getDiscovererId());
		assertEquals(this.magnitude, instance.getMagnitude());
		assertEquals(this.significance, instance.getSignificance());
		assertNull(instance.getInformation());
		assertNull(instance.getConfidence());
		assertNull(instance.getType());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullDiscoveredTime() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, null, this.discovererId, this.magnitude, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullDiscovererId() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, null, this.magnitude, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyDiscovererId() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, "", this.magnitude, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_WhitespacesDiscovererId() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, "   ", this.magnitude, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NegMagnitude() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, -1, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_MagnitudeGreater100() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, 101, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullSignificance() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, null);
	}


	@Test
	public void testSetInformation() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, this.significance);
		instance.setInformation(this.information);
		assertEquals(this.information, instance.getInformation());
	}


	@Test
	public void testSetInformation_null() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, this.significance);
		instance.setInformation(this.information);
		instance.setInformation(null);
		assertNull(instance.getInformation());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetInformation_EmptyString() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, this.significance);
		instance.setInformation("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetInformation_Whitespaces() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, this.significance);
		instance.setInformation("   ");
	}


	@Test
	public void testSetType() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, this.significance);
		instance.setType(this.type);
		assertEquals(this.type, instance.getType());
	}


	@Test
	public void testSetType_null() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, this.significance);
		instance.setType(this.type);
		instance.setType(null);
		assertNull(instance.getType());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetType_EmptyString() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, this.significance);
		instance.setType("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetType_Whitespaces() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, this.significance);
		instance.setType("   ");
	}


	@Test
	public void testSetConfidence() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, this.significance);
		instance.setConfidence(this.confidence);
		assertEquals(this.confidence, instance.getConfidence());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetConfidence_Negative() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, this.significance);
		instance.setConfidence(-1);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetConfidence_Greater100() {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, this.significance);
		instance.setConfidence(101);
	}


	@Test
	public void testCborSerialize_Critical() throws Exception {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, IfMapSignificance.CRITICAL);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (critical):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84011188006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030190F60080C1"
				+ "1A4ED9E777F6018077646973636F76657265722D7075626C69736865722D6964F6038005F6058000");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_Important() throws Exception {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, IfMapSignificance.IMPORTANT);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (important):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84011188006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030190F60080C1"
				+ "1A4ED9E777F6018077646973636F76657265722D7075626C69736865722D6964F6038005F6058001");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_Informational() throws Exception {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, IfMapSignificance.INFORMATIONAL);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (informational):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84011188006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030190F60080C1"
				+ "1A4ED9E777F6018077646973636F76657265722D7075626C69736865722D6964F6038005F6058002");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		CBORUnexpectedBehavior instance = new CBORUnexpectedBehavior(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.magnitude, IfMapSignificance.CRITICAL);
		instance.setConfidence(this.confidence);
		instance.setInformation(this.information);
		instance.setType(this.type);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84011188006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA000301981CF60080"
				+ "C11A4ED9E777F6018077646973636F76657265722D7075626C69736865722D6964F6038005F6058000F60480182AF606806D6265686176696F722D74797065F602"
				+ "80746265686176696F722D696E666F726D6174696F6E");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
