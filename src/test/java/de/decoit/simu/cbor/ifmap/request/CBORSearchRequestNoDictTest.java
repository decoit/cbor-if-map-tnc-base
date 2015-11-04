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
public class CBORSearchRequestNoDictTest {
	private final String sessionId = "my-session-id";
	private final CBORIdentity identifier;
	private final String matchLinks = "match-links-filter";
	private final String resultFilter = "result-filter";
	private final String terminalIdentifierType = "access-request";
	private final Integer maxDepth = 2;
	private final Integer maxSize = 5000000;


	public CBORSearchRequestNoDictTest() {
		identifier = new CBORIdentity("my-identity", IfMapIdentityType.OTHER, "my-otd");
		identifier.setAdministrativeDomain("identity-ad");
	}


	@Test
	public void testCborSerialize() throws Exception {
		CBORSearchRequest instance = new CBORSearchRequest(this.sessionId, this.identifier);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41502F3266736561726368826A73657373696F6E2D69646D6D792D73657373696F6E2D6964847831687474703A2F2F7777772E74727573746564636F6D"
				+ "707574696E6767726F75702E6F72672F323031302F49464D41502F32686964656E7469747988646E616D656B6D792D6964656E746974796474797065656F746865"
				+ "727561646D696E6973747261746976652D646F6D61696E6B6964656E746974792D6164756F746865722D747970652D646566696E6974696F6E666D792D6F746480");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		CBORSearchRequest instance = new CBORSearchRequest(this.sessionId, this.identifier);
		instance.setValidation(IfMapValidationType.BASE_ONLY);
		instance.getSearchTypeAttributes().setMatchLinks(this.matchLinks);
		instance.getSearchTypeAttributes().setMaxDepth(this.maxDepth);
		instance.getSearchTypeAttributes().setMaxSize(this.maxSize);
		instance.getSearchTypeAttributes().setResultFilter(this.resultFilter);
		instance.getSearchTypeAttributes().setTerminalIdentifierType(this.terminalIdentifierType);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41502F32667365617263688E6A73657373696F6E2D69646D6D792D73657373696F6E2D69646B6D617463682D6C696E6B73726D617463682D6C696E6B73"
				+ "2D66696C746572696D61782D646570746802686D61782D73697A651A004C4B406D726573756C742D66696C7465726D726573756C742D66696C7465727818746572"
				+ "6D696E616C2D6964656E7469666965722D747970656E6163636573732D726571756573746A76616C69646174696F6E68426173654F6E6C79847831687474703A2F"
				+ "2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F32686964656E7469747988646E616D656B6D792D696465"
				+ "6E746974796474797065656F746865727561646D696E6973747261746976652D646F6D61696E6B6964656E746974792D6164756F746865722D747970652D646566"
				+ "696E6974696F6E666D792D6F746480");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
