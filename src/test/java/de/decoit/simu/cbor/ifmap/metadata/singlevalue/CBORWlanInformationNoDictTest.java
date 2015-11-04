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
import de.decoit.simu.cbor.ifmap.enums.IfMapWlanSecurityType;
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
public class CBORWlanInformationNoDictTest {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String ssid = "wlan-ssid";
	private final IfMapWlanSecurityType groupSecurityType = IfMapWlanSecurityType.TKIP;
	private final String otherTypeDefinition = "other-type-definition";


	@Test
	public void testCborSerialize() throws Exception {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, this.groupSecurityType);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.TKIP, null);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.TKIP, null);

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
				+ "2F49464D41504D455441444154412F3270776C616E2D696E666F726D6174696F6E887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865"
				+ "722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61"
				+ "702D63617264696E616C6974796B73696E676C6556616C756594F675737369642D756E69636173742D73656375726974798063626970F675737369642D756E6963"
				+ "6173742D73656375726974798064746B6970F673737369642D67726F75702D73656375726974798064746B6970F67818737369642D6D616E6167656D656E742D73"
				+ "656375726974798063626970F67818737369642D6D616E6167656D656E742D73656375726974798064746B6970");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		CBORWlanInformation instance = new CBORWlanInformation(this.publisherId, this.ifMapTimestamp, IfMapWlanSecurityType.OTHER, this.otherTypeDefinition);
		instance.setSsid(this.ssid);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidManagementSecurity(IfMapWlanSecurityType.OTHER, this.otherTypeDefinition);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null);
		instance.addSsidUnicastSecurity(IfMapWlanSecurityType.OTHER, this.otherTypeDefinition);

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
				+ "2F49464D41504D455441444154412F3270776C616E2D696E666F726D6174696F6E887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865"
				+ "722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61"
				+ "702D63617264696E616C6974796B73696E676C6556616C75659818F664737369648069776C616E2D73736964F675737369642D756E69636173742D736563757269"
				+ "74798063626970F675737369642D756E69636173742D736563757269747982756F746865722D747970652D646566696E6974696F6E756F746865722D747970652D"
				+ "646566696E6974696F6E656F74686572F673737369642D67726F75702D736563757269747982756F746865722D747970652D646566696E6974696F6E756F746865"
				+ "722D747970652D646566696E6974696F6E656F74686572F67818737369642D6D616E6167656D656E742D73656375726974798063626970F67818737369642D6D61"
				+ "6E6167656D656E742D736563757269747982756F746865722D747970652D646566696E6974696F6E756F746865722D747970652D646566696E6974696F6E656F74"
				+ "686572");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
