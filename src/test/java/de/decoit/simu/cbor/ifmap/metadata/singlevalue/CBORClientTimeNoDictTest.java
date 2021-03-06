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
public class CBORClientTimeNoDictTest {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final ZonedDateTime currentTime = ZonedDateTime.parse("2011-12-03T10:15:31.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);


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

		byte[] expResult = DatatypeConverter.parseHexBinary("847845687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303132"
				+ "2F49464D41504F5045524154494F4E414C2D4D455441444154412F316B636C69656E742D74696D658A7269666D61702D7075626C69736865722D69646F6D792D70"
				+ "75626C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075B"
				+ "CA007169666D61702D63617264696E616C6974796B73696E676C6556616C75656C63757272656E742D74696D65C11A4ED9E8B380");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
