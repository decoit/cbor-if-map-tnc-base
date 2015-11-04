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
public class CBORClientTimeTest extends AbstractTestBase {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final ZonedDateTime currentTime = ZonedDateTime.parse("2011-12-03T10:15:31.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);


	@Test
	public void testConstructor() {
		CBORClientTime instance = new CBORClientTime(this.publisherId, this.ifMapTimestamp, this.currentTime);

		assertEquals(IfMapNamespaces.IFMAP_OPEARATIONAL_METADATA, instance.getNamespace());
		assertEquals(CBORClientTime.XML_NAME, instance.getElementName());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(this.ifMapTimestamp), instance.getIfMapTimestamp());
		assertEquals(IfMapCardinality.SINGLE_VALUE, instance.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.currentTime), instance.getCurrentTime());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null() {
		CBORClientTime instance = new CBORClientTime(this.publisherId, this.ifMapTimestamp, null);
	}


	@Test
	public void testCborSerialize() throws Exception {
		CBORClientTime instance = new CBORClientTime(this.publisherId, this.ifMapTimestamp, this.currentTime);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("8402008A006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030004C11A4ED9"
				+ "E8B380");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
