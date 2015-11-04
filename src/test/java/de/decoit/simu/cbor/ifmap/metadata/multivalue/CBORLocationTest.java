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
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORLocation.LocationInformation;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.ifmap.util.TimestampHelper;
import java.io.ByteArrayOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import javax.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class CBORLocationTest extends AbstractTestBase {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final ZonedDateTime discoveredTime = ZonedDateTime.parse("2011-12-03T10:10:15.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String discovererId = "discoverer-publisher-id";
	private final String[] locInfoTypes = new String[]{"type-1", "type-2", "type-3"};
	private final String[] locInfoValues = new String[]{"value-1", "value-2", "value-3"};


	@Test
	public void testConstructor() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId);

		assertEquals(IfMapNamespaces.IFMAP_METADATA, instance.getNamespace());
		assertEquals(CBORLocation.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.MULTI_VALUE, instance.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.discoveredTime), instance.getDiscoveredTime());
		assertEquals(this.discovererId, instance.getDiscovererId());
		assertTrue(instance.getLocationInformation().isEmpty());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullDiscoveredTime() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, null, this.discovererId);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullDiscovererId() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyDiscovererId() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, "");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_WhitespaceDiscovererId() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, "   ");
	}


	@Test
	public void testAddLocationInformation() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId);
		instance.addLocationInformation(this.locInfoTypes[0], this.locInfoValues[0]);

		Set<LocationInformation> result = instance.getLocationInformation();
		assertTrue(result.size() == 1);

		for(LocationInformation li : result) {
			assertEquals(this.locInfoTypes[0], li.getType());
			assertEquals(this.locInfoValues[0], li.getValue());
		}
	}


	@Test
	public void testAddLocationInformation_SameTwice() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId);
		instance.addLocationInformation(this.locInfoTypes[0], this.locInfoValues[0]);
		instance.addLocationInformation(this.locInfoTypes[0], this.locInfoValues[0]);

		Set<LocationInformation> result = instance.getLocationInformation();
		assertTrue(result.size() == 1);
	}


	@Test
	public void testAddLocationInformation_TwoDifferent() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId);
		instance.addLocationInformation(this.locInfoTypes[0], this.locInfoValues[0]);
		instance.addLocationInformation(this.locInfoTypes[1], this.locInfoValues[1]);

		Set<LocationInformation> result = instance.getLocationInformation();
		assertTrue(result.size() == 2);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddLocationInformation_null_String() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId);
		instance.addLocationInformation(null, this.locInfoValues[0]);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddLocationInformation_String_null() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId);
		instance.addLocationInformation(this.locInfoTypes[0], null);
	}


	@Test
	public void testRemoveLocationInformation() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId);
		instance.addLocationInformation(this.locInfoTypes[0], this.locInfoValues[0]);
		instance.addLocationInformation(this.locInfoTypes[1], this.locInfoValues[1]);
		instance.addLocationInformation(this.locInfoTypes[2], this.locInfoValues[2]);

		Set<LocationInformation> result = instance.getLocationInformation();
		instance.removeLocationInformation(this.locInfoTypes[1], this.locInfoValues[1]);
		assertTrue(result.size() == 2);
	}


	@Test
	public void testRemoveLocationInformation_SameTwice() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId);
		instance.addLocationInformation(this.locInfoTypes[0], this.locInfoValues[0]);
		instance.addLocationInformation(this.locInfoTypes[1], this.locInfoValues[1]);
		instance.addLocationInformation(this.locInfoTypes[2], this.locInfoValues[2]);

		Set<LocationInformation> result = instance.getLocationInformation();
		instance.removeLocationInformation(this.locInfoTypes[1], this.locInfoValues[1]);
		instance.removeLocationInformation(this.locInfoTypes[1], this.locInfoValues[1]);
		assertTrue(result.size() == 2);
	}


	@Test
	public void testRemoveLocationInformation_TwoDifferent() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId);
		instance.addLocationInformation(this.locInfoTypes[0], this.locInfoValues[0]);
		instance.addLocationInformation(this.locInfoTypes[1], this.locInfoValues[1]);
		instance.addLocationInformation(this.locInfoTypes[2], this.locInfoValues[2]);

		Set<LocationInformation> result = instance.getLocationInformation();
		instance.removeLocationInformation(this.locInfoTypes[1], this.locInfoValues[1]);
		instance.removeLocationInformation(this.locInfoTypes[0], this.locInfoValues[0]);
		assertTrue(result.size() == 1);
	}


	@Test
	public void testRemoveLocationInformation_ExchangedIndices() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId);
		instance.addLocationInformation(this.locInfoTypes[0], this.locInfoValues[0]);
		instance.addLocationInformation(this.locInfoTypes[1], this.locInfoValues[1]);
		instance.addLocationInformation(this.locInfoTypes[2], this.locInfoValues[2]);

		Set<LocationInformation> result = instance.getLocationInformation();
		instance.removeLocationInformation(this.locInfoTypes[1], this.locInfoValues[0]);
		instance.removeLocationInformation(this.locInfoTypes[0], this.locInfoValues[1]);
		assertTrue(result.size() == 3);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveLocationInformation_null_String() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId);
		instance.removeLocationInformation(null, this.locInfoValues[0]);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveLocationInformation_String_null() {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId);
		instance.removeLocationInformation(this.locInfoTypes[0], null);
	}


	@Test(expected = CBORSerializationException.class)
	public void testCborSerialize_empty() throws Exception {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);
	}


	@Test
	public void testCborSerialize() throws Exception {
		CBORLocation instance = new CBORLocation(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId);
		instance.addLocationInformation(this.locInfoTypes[0], this.locInfoValues[0]);
		instance.addLocationInformation(this.locInfoTypes[1], this.locInfoValues[1]);
		instance.addLocationInformation(this.locInfoTypes[2], this.locInfoValues[2]);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84010E88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030194F60280C1"
				+ "1A4ED9E777F6038077646973636F76657265722D7075626C69736865722D6964F600840066747970652D31016776616C75652D3180F600840066747970652D3301"
				+ "6776616C75652D3380F600840066747970652D32016776616C75652D3280");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
