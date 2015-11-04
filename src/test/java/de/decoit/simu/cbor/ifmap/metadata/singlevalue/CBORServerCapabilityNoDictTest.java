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
public class CBORServerCapabilityNoDictTest {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);


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

		byte[] expResult = DatatypeConverter.parseHexBinary("847838687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303133"
				+ "2F49464D41502D5345525645522F31717365727665722D6361706162696C697479887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865"
				+ "722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61"
				+ "702D63617264696E616C6974796B73696E676C6556616C75658CF66A6361706162696C697479807669666D61702D626173652D76657273696F6E2D322E30F66A63"
				+ "61706162696C697479807669666D61702D626173652D76657273696F6E2D322E32F66A6361706162696C697479807669666D61702D626173652D76657273696F6E"
				+ "2D322E31");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
