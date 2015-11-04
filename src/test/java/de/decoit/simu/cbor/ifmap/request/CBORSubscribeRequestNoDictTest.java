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
package de.decoit.simu.cbor.ifmap.request;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.enums.IfMapIdentityType;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import de.decoit.simu.cbor.ifmap.request.model.search.CBORSubscribeDelete;
import de.decoit.simu.cbor.ifmap.request.model.search.CBORSubscribeUpdate;
import de.decoit.simu.cbor.ifmap.enums.IfMapValidationType;
import java.io.ByteArrayOutputStream;
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
public class CBORSubscribeRequestNoDictTest {
	private final String sessionId = "my-session-id";
	private final CBORSubscribeUpdate subTypeUpd;
	private final CBORSubscribeDelete subTypeDel;
	private final CBORIdentity identifier;
	private final String matchLinks = "match-links-filter";
	private final String resultFilter = "result-filter";
	private final String terminalIdentifierType = "access-request";
	private final Integer maxDepth = 2;
	private final Integer maxSize = 5000000;


	public CBORSubscribeRequestNoDictTest() {
		this.identifier = new CBORIdentity("my-identity", IfMapIdentityType.OTHER, "my-otd");
		this.identifier.setAdministrativeDomain("identity-ad");

		this.subTypeUpd = new CBORSubscribeUpdate("my-subscription", this.identifier);
		this.subTypeUpd.getSearchTypeAttributes().setMatchLinks(this.matchLinks);
		this.subTypeUpd.getSearchTypeAttributes().setMaxDepth(this.maxDepth);
		this.subTypeUpd.getSearchTypeAttributes().setMaxSize(this.maxSize);
		this.subTypeUpd.getSearchTypeAttributes().setResultFilter(this.resultFilter);
		this.subTypeUpd.getSearchTypeAttributes().setTerminalIdentifierType(this.terminalIdentifierType);

		this.subTypeDel = new CBORSubscribeDelete("my-old-subscription");
	}


	@Test
	public void testCborSerialize_OneSubscriptionType() throws Exception {
		CBORSubscribeRequest instance = new CBORSubscribeRequest(this.sessionId);
		instance.addSubscribeType(this.subTypeDel);
		instance.setValidation(IfMapValidationType.ALL);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (1 type):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41502F3269737562736372696265846A73657373696F6E2D69646D6D792D73657373696F6E2D69646A76616C69646174696F6E63416C6C84F66664656C"
				+ "65746582646E616D65736D792D6F6C642D737562736372697074696F6E80");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_TwoSubscriptionTypes() throws Exception {
		CBORSubscribeRequest instance = new CBORSubscribeRequest(this.sessionId);
		instance.addSubscribeType(this.subTypeDel);
		instance.addSubscribeType(this.subTypeUpd);
		instance.setValidation(IfMapValidationType.ALL);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (2 types):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41502F3269737562736372696265846A73657373696F6E2D69646D6D792D73657373696F6E2D69646A76616C69646174696F6E63416C6C88F66664656C"
				+ "65746582646E616D65736D792D6F6C642D737562736372697074696F6E80F6667570646174658C646E616D656F6D792D737562736372697074696F6E6B6D617463"
				+ "682D6C696E6B73726D617463682D6C696E6B732D66696C746572696D61782D646570746802686D61782D73697A651A004C4B406D726573756C742D66696C746572"
				+ "6D726573756C742D66696C74657278187465726D696E616C2D6964656E7469666965722D747970656E6163636573732D72657175657374847831687474703A2F2F"
				+ "7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F32686964656E7469747988646E616D656B6D792D6964656E"
				+ "746974796474797065656F746865727561646D696E6973747261746976652D646F6D61696E6B6964656E746974792D6164756F746865722D747970652D64656669"
				+ "6E6974696F6E666D792D6F746480");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
