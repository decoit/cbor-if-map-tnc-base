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
import de.decoit.simu.cbor.ifmap.enums.IfMapEventType;
import de.decoit.simu.cbor.ifmap.enums.IfMapSignificance;
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
public class CBOREventNoDictTest {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final ZonedDateTime discoveredTime = ZonedDateTime.parse("2011-12-03T10:10:15.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String discovererId = "discoverer-publisher-id";
	private final String name = "event:name";
	private final Integer magnitude = 5;
	private final String information = "event-information";
	private final Integer confidence = 42;
	private final String vulnerabilityUri = "event:vulnerability-uri";
	private final String otherTypeDefinition = "other-type-definition";


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

		byte[] expResult = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32656576656E74887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D61702D"
				+ "74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E616C69"
				+ "74796A6D756C746956616C75659818F6646E616D65806A6576656E743A6E616D65F66F646973636F76657265642D74696D6580C11A4ED9E777F66D646973636F76"
				+ "657265722D69648077646973636F76657265722D7075626C69736865722D6964F6696D61676E69747564658005F66A636F6E666964656E636580182AF66C736967"
				+ "6E69666963616E63658068637269746963616C");

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

		byte[] expResult = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32656576656E74887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D61702D"
				+ "74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E616C69"
				+ "74796A6D756C746956616C75659818F6646E616D65806A6576656E743A6E616D65F66F646973636F76657265642D74696D6580C11A4ED9E777F66D646973636F76"
				+ "657265722D69648077646973636F76657265722D7075626C69736865722D6964F6696D61676E69747564658005F66A636F6E666964656E636580182AF66C736967"
				+ "6E69666963616E63658069696D706F7274616E74");

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

		byte[] expResult = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32656576656E74887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D61702D"
				+ "74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E616C69"
				+ "74796A6D756C746956616C75659818F6646E616D65806A6576656E743A6E616D65F66F646973636F76657265642D74696D6580C11A4ED9E777F66D646973636F76"
				+ "657265722D69648077646973636F76657265722D7075626C69736865722D6964F6696D61676E69747564658005F66A636F6E666964656E636580182AF66C736967"
				+ "6E69666963616E6365806D696E666F726D6174696F6E616C");

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

		byte[] expResult = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32656576656E74887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D61702D"
				+ "74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E616C69"
				+ "74796A6D756C746956616C7565981CF6646E616D65806A6576656E743A6E616D65F66F646973636F76657265642D74696D6580C11A4ED9E777F66D646973636F76"
				+ "657265722D69648077646973636F76657265722D7075626C69736865722D6964F6696D61676E69747564658005F66A636F6E666964656E636580182AF66C736967"
				+ "6E69666963616E6365806D696E666F726D6174696F6E616CF664747970658063703270");

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

		byte[] expResult = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32656576656E74887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D61702D"
				+ "74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E616C69"
				+ "74796A6D756C746956616C75659828F6646E616D65806A6576656E743A6E616D65F66F646973636F76657265642D74696D6580C11A4ED9E777F66D646973636F76"
				+ "657265722D69648077646973636F76657265722D7075626C69736865722D6964F6696D61676E69747564658005F66A636F6E666964656E636580182AF66C736967"
				+ "6E69666963616E6365806D696E666F726D6174696F6E616CF6647479706580656F74686572F6756F746865722D747970652D646566696E6974696F6E80756F7468"
				+ "65722D747970652D646566696E6974696F6EF66B696E666F726D6174696F6E80716576656E742D696E666F726D6174696F6EF67176756C6E65726162696C697479"
				+ "2D75726980776576656E743A76756C6E65726162696C6974792D757269");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
