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
import co.nstant.in.cbor.builder.MapBuilder;
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.enums.IfMapCardinality;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
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
public class CBORServerCapabilityTest extends AbstractTestBase {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);


	@Test
	public void testConstructor() {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);

		assertEquals(IfMapNamespaces.IFMAP_SERVER, instance.getNamespace());
		assertEquals(CBORServerCapability.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.SINGLE_VALUE, instance.getIfMapCardinality());
		assertTrue(instance.getCapabilities().isEmpty());
	}


	@Test
	public void testAddCapability() {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);

		Set<String> result = instance.getCapabilities();
		assertTrue(result.size() == 1);
		assertTrue(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22));
	}


	@Test
	public void testAddCapability_SameTwice() {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);

		Set<String> result = instance.getCapabilities();
		assertTrue(result.size() == 1);
		assertTrue(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22));
	}


	@Test
	public void testAddCapability_TwoDifferent() {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_21);

		Set<String> result = instance.getCapabilities();
		assertTrue(result.size() == 2);
		assertTrue(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22));
		assertTrue(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_21));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddCapability_null() {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);
		instance.addCapability(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddCapability_EmptyString() {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);
		instance.addCapability("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddCapability_Whitespaces() {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);
		instance.addCapability("   ");
	}


	@Test
	public void testRemoveCapability() {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_21);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_20);

		instance.removeCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);

		Set<String> result = instance.getCapabilities();
		assertTrue(result.size() == 2);
		assertFalse(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22));
		assertTrue(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_21));
		assertTrue(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_20));
	}


	@Test
	public void testRemoveCapability_SameTwice() {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_21);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_20);

		instance.removeCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);
		instance.removeCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);

		Set<String> result = instance.getCapabilities();
		assertTrue(result.size() == 2);
		assertFalse(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22));
		assertTrue(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_21));
		assertTrue(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_20));
	}


	@Test
	public void testRemoveCapability_TwoDifferent() {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_21);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_20);

		instance.removeCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);
		instance.removeCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_21);

		Set<String> result = instance.getCapabilities();
		assertTrue(result.size() == 1);
		assertFalse(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22));
		assertFalse(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_21));
		assertTrue(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_20));
	}


	@Test
	public void testRemoveCapability_NotExistent() {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_21);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_20);

		instance.removeCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_10);

		Set<String> result = instance.getCapabilities();
		assertTrue(result.size() == 3);
		assertTrue(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22));
		assertTrue(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_21));
		assertTrue(result.contains(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_20));
	}


	@Test
	public void testHasCapability_true() {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);

		assertTrue(instance.hasCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22));
	}


	@Test
	public void testHasCapability_false() {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);

		assertFalse(instance.hasCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_21));
	}


	@Test
	public void testCborSerialize() throws Exception {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_21);
		instance.addCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_20);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84030188006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003008CF6008076"
				+ "69666D61702D626173652D76657273696F6E2D322E30F600807669666D61702D626173652D76657273696F6E2D322E32F600807669666D61702D626173652D7665"
				+ "7273696F6E2D322E31");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test(expected = CBORSerializationException.class)
	public void testCborSerialize_emptySet() throws Exception {
		CBORServerCapability instance = new CBORServerCapability(this.publisherId, this.ifMapTimestamp);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);
	}
}
