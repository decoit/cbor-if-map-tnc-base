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
import de.decoit.simu.cbor.ifmap.enums.IfMapEnforcementAction;
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
public class CBOREnforcementReportNoDictTest {
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String otherTypeDefinition = "other-type-definition";
	private final String enforcementReason = "just-for-fun";


	@Test
	public void testCborSerialize_TypeBlock() throws Exception {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.BLOCK);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (block):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F3272656E666F7263656D656E742D7265706F7274887269666D61702D7075626C69736865722D69646F6D792D7075626C6973"
				+ "6865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA00716966"
				+ "6D61702D63617264696E616C6974796A6D756C746956616C756584F672656E666F7263656D656E742D616374696F6E8065626C6F636B");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_TypeQuarantine() throws Exception {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.QUARANTINE);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (quarantine):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F3272656E666F7263656D656E742D7265706F7274887269666D61702D7075626C69736865722D69646F6D792D7075626C6973"
				+ "6865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA00716966"
				+ "6D61702D63617264696E616C6974796A6D756C746956616C756584F672656E666F7263656D656E742D616374696F6E806A71756172616E74696E65");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_TypeOtherFull() throws Exception {
		CBOREnforcementReport instance = new CBOREnforcementReport(this.publisherId, this.ifMapTimestamp, IfMapEnforcementAction.OTHER, this.otherTypeDefinition);
		instance.setEnforcementReason(this.enforcementReason);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (other, full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F3272656E666F7263656D656E742D7265706F7274887269666D61702D7075626C69736865722D69646F6D792D7075626C6973"
				+ "6865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA00716966"
				+ "6D61702D63617264696E616C6974796A6D756C746956616C75658CF672656E666F7263656D656E742D616374696F6E80656F74686572F6756F746865722D747970"
				+ "652D646566696E6974696F6E80756F746865722D747970652D646566696E6974696F6EF672656E666F7263656D656E742D726561736F6E806C6A7573742D666F72"
				+ "2D66756E");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}
}
