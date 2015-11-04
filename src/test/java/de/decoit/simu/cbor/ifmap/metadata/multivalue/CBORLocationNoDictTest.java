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
public class CBORLocationNoDictTest {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final ZonedDateTime discoveredTime = ZonedDateTime.parse("2011-12-03T10:10:15.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String discovererId = "discoverer-publisher-id";
	private final String[] locInfoTypes = new String[]{"type-1", "type-2", "type-3"};
	private final String[] locInfoValues = new String[]{"value-1", "value-2", "value-3"};


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

		byte[] expResult = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32686C6F636174696F6E887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D"
				+ "61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E"
				+ "616C6974796A6D756C746956616C756594F66F646973636F76657265642D74696D6580C11A4ED9E777F66D646973636F76657265722D69648077646973636F7665"
				+ "7265722D7075626C69736865722D6964F6746C6F636174696F6E2D696E666F726D6174696F6E84647479706566747970652D316576616C75656776616C75652D31"
				+ "80F6746C6F636174696F6E2D696E666F726D6174696F6E84647479706566747970652D336576616C75656776616C75652D3380F6746C6F636174696F6E2D696E66"
				+ "6F726D6174696F6E84647479706566747970652D326576616C75656776616C75652D3280");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
