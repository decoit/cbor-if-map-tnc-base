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
public class CBORDeviceCharacteristicNoDictTest {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final ZonedDateTime discoveredTime = ZonedDateTime.parse("2011-12-03T10:10:15.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String discovererId = "discoverer-publisher-id";
	private final String discoveryMethod = "device-characteristic:discovery-method";
	private final String manufacturer = "manufacturer-0815";
	private final String model = "model-0815-2";
	private final String os = "operating-system-0815";
	private final String osVersion = "v42";
	private final String deviceType = "device-0815";


	@Test
	public void testCborSerialize() throws Exception {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);

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
				+ "2F49464D41504D455441444154412F32756465766963652D6368617261637465726973746963887269666D61702D7075626C69736865722D69646F6D792D707562"
				+ "6C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA00"
				+ "7169666D61702D63617264696E616C6974796A6D756C746956616C75658CF66F646973636F76657265642D74696D6580C11A4ED9E777F66D646973636F76657265"
				+ "722D69648077646973636F76657265722D7075626C69736865722D6964F670646973636F766572792D6D6574686F648078266465766963652D6368617261637465"
				+ "7269737469633A646973636F766572792D6D6574686F64");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		CBORDeviceCharacteristic instance = new CBORDeviceCharacteristic(this.publisherId, this.ifMapTimestamp, this.discoveredTime, this.discovererId, this.discoveryMethod);
		instance.setManufacturer(this.manufacturer);
		instance.setModel(this.model);
		instance.setOs(this.os);
		instance.setOsVersion(this.osVersion);
		instance.setDeviceType(this.deviceType);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32756465766963652D6368617261637465726973746963887269666D61702D7075626C69736865722D69646F6D792D707562"
				+ "6C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA00"
				+ "7169666D61702D63617264696E616C6974796A6D756C746956616C75659820F66F646973636F76657265642D74696D6580C11A4ED9E777F66D646973636F766572"
				+ "65722D69648077646973636F76657265722D7075626C69736865722D6964F670646973636F766572792D6D6574686F648078266465766963652D63686172616374"
				+ "657269737469633A646973636F766572792D6D6574686F64F66C6D616E75666163747572657280716D616E7566616374757265722D30383135F6656D6F64656C80"
				+ "6C6D6F64656C2D303831352D32F6626F7380756F7065726174696E672D73797374656D2D30383135F66A6F732D76657273696F6E8063763432F66B646576696365"
				+ "2D74797065806B6465766963652D30383135");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
