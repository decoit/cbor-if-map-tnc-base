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
import de.decoit.simu.cbor.ifmap.enums.IfMapEventType;
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
public class CBOREventTest extends AbstractTestBase {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final ZonedDateTime discoveredTime = ZonedDateTime.parse("2011-12-03T10:10:15.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String discovererId = "discoverer-publisher-id";
	private final String name = "event:name";
	private final Integer magnitude = 5;
	private final IfMapSignificance significance = IfMapSignificance.CRITICAL;
	private final String information = "event-information";
	private final Integer confidence = 42;
	private final String vulnerabilityUri = "event:vulnerability-uri";
	private final String otherTypeDefinition = "other-type-definition";


	@Test
	public void testConstructor() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);

		assertEquals(IfMapNamespaces.IFMAP_METADATA, instance.getNamespace());
		assertEquals(CBOREvent.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.MULTI_VALUE, instance.getIfMapCardinality());
		assertEquals(this.name, instance.getName());
		assertEquals(TimestampHelper.toUTC(this.discoveredTime), instance.getDiscoveredTime());
		assertEquals(this.discovererId, instance.getDiscovererId());
		assertEquals(this.magnitude, instance.getMagnitude());
		assertEquals(this.confidence, instance.getConfidence());
		assertEquals(this.significance, instance.getSignificance());
		assertNull(instance.getInformation());
		assertNull(instance.getOtherTypeDefinition());
		assertNull(instance.getType());
		assertNull(instance.getVulnerabilityUri());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullName() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, null, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyName() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, "", this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_WhitespacesName() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, "   ", this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullDiscoveredTime() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, null, this.discovererId, this.magnitude, this.confidence, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullDiscovererId() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, null, this.magnitude, this.confidence, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyDiscovererId() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, "", this.magnitude, this.confidence, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_WhitespacesDiscovererId() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, "   ", this.magnitude, this.confidence, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NegMagnitude() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, -1, this.confidence, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_MagnitudeGreater100() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, 101, this.confidence, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NegConfidence() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, -1, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_ConfidenceGreater100() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, 101, this.significance);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullSignificance() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, null);
	}


	@Test
	public void testSetInformation() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
		instance.setInformation(this.information);
		assertEquals(this.information, instance.getInformation());
	}


	@Test
	public void testSetInformation_null() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
		instance.setInformation(this.information);
		instance.setInformation(null);
		assertNull(instance.getInformation());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetInformation_EmptyString() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
		instance.setInformation("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetInformation_Whitespaces() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
		instance.setInformation("   ");
	}


	@Test
	public void testSetType() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
		instance.setType(IfMapEventType.BEHAVIORAL_CHANGE);
		assertEquals(IfMapEventType.BEHAVIORAL_CHANGE, instance.getType());
		assertNull(instance.getOtherTypeDefinition());
	}


	@Test
	public void testSetType_Type_Otd() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
		instance.setType(IfMapEventType.BEHAVIORAL_CHANGE, this.otherTypeDefinition);
		assertEquals(IfMapEventType.BEHAVIORAL_CHANGE, instance.getType());
		assertNull(instance.getOtherTypeDefinition());
	}


	@Test
	public void testSetType_Other_Otd() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
		instance.setType(IfMapEventType.OTHER, this.otherTypeDefinition);
		assertEquals(IfMapEventType.OTHER, instance.getType());
		assertEquals(this.otherTypeDefinition, instance.getOtherTypeDefinition());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetType_Other() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
		instance.setType(IfMapEventType.OTHER);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetType_Other_NoOtd() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
		instance.setType(IfMapEventType.OTHER, null);
	}


	@Test
	public void testSetVulnerabilityUri() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
		instance.setVulnerabilityUri(this.vulnerabilityUri);
		assertEquals(this.vulnerabilityUri, instance.getVulnerabilityUri());
	}


	@Test
	public void testSetVulnerabilityUri_null() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
		instance.setVulnerabilityUri(this.vulnerabilityUri);
		instance.setVulnerabilityUri(null);
		assertNull(instance.getVulnerabilityUri());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetVulnerabilityUri_EmptyString() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
		instance.setVulnerabilityUri("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetVulnerabilityUri_Whitespaces() {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, this.significance);
		instance.setVulnerabilityUri("   ");
	}


	@Test
	public void testCborSerialize_Critical() throws Exception {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, IfMapSignificance.CRITICAL);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (critical):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010B88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003019818F60080"
				+ "6A6576656E743A6E616D65F60180C11A4ED9E777F6028077646973636F76657265722D7075626C69736865722D6964F6038005F60480182AF6058000");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_Important() throws Exception {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, IfMapSignificance.IMPORTANT);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (important):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010B88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003019818F60080"
				+ "6A6576656E743A6E616D65F60180C11A4ED9E777F6028077646973636F76657265722D7075626C69736865722D6964F6038005F60480182AF6058001");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_Informational() throws Exception {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, IfMapSignificance.INFORMATIONAL);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (informational):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010B88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003019818F60080"
				+ "6A6576656E743A6E616D65F60180C11A4ED9E777F6028077646973636F76657265722D7075626C69736865722D6964F6038005F60480182AF6058002");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_TypeP2P() throws Exception {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, IfMapSignificance.INFORMATIONAL);
		instance.setType(IfMapEventType.P2P);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (type-p2p):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010B88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA000301981CF60080"
				+ "6A6576656E743A6E616D65F60180C11A4ED9E777F6028077646973636F76657265722D7075626C69736865722D6964F6038005F60480182AF6058002F6068000");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_TypeOther_full() throws Exception {
		CBOREvent instance = new CBOREvent(this.publisherId, this.ifMapTimestamp, this.name, this.discoveredTime, this.discovererId, this.magnitude, this.confidence, IfMapSignificance.INFORMATIONAL);
		instance.setType(IfMapEventType.OTHER, this.otherTypeDefinition);
		instance.setInformation(this.information);
		instance.setVulnerabilityUri(this.vulnerabilityUri);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (type-other, full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010B88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003019828F60080"
				+ "6A6576656E743A6E616D65F60180C11A4ED9E777F6028077646973636F76657265722D7075626C69736865722D6964F6038005F60480182AF6058002F6068007F6"
				+ "0780756F746865722D747970652D646566696E6974696F6EF60880716576656E742D696E666F726D6174696F6EF60980776576656E743A76756C6E65726162696C"
				+ "6974792D757269");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
