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
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.enums.IfMapIdentityType;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import de.decoit.simu.cbor.ifmap.enums.IfMapValidationType;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
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
public class CBORSearchRequestTest extends AbstractTestBase {
	private final String sessionId = "my-session-id";
	private final CBORIdentity identifier;
	private final String matchLinks = "match-links-filter";
	private final String resultFilter = "result-filter";
	private final String terminalIdentifierType = "access-request";
	private final Integer maxDepth = 2;
	private final Integer maxSize = 5000000;


	public CBORSearchRequestTest() {
		identifier = new CBORIdentity("my-identity", IfMapIdentityType.OTHER, "my-otd");
		identifier.setAdministrativeDomain("identity-ad");
	}


	@Test
	public void testConstructor() {
		CBORSearchRequest instance = new CBORSearchRequest(this.sessionId, this.identifier);

		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("search", instance.getElementName());
		assertEquals(this.sessionId, instance.getSessionAttributes().getSessionId());
		assertEquals(this.identifier, instance.getIdentifier());
		assertNull(instance.getValidationAttributes());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null_Identifier() {
		CBORSearchRequest instance = new CBORSearchRequest(null, this.identifier);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyString_Identifier() {
		CBORSearchRequest instance = new CBORSearchRequest("", this.identifier);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Whitespaces_Identifier() {
		CBORSearchRequest instance = new CBORSearchRequest("   ", this.identifier);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_String_null() {
		CBORSearchRequest instance = new CBORSearchRequest(this.sessionId, null);
	}


	@Test
	public void testSetValidation() {
		CBORSearchRequest instance = new CBORSearchRequest(this.sessionId, this.identifier);

		instance.setValidation(IfMapValidationType.NONE);

		assertEquals(IfMapValidationType.NONE, instance.getValidationAttributes().getValidationType());
	}


	@Test
	public void testSetValidation_null() {
		CBORSearchRequest instance = new CBORSearchRequest(this.sessionId, this.identifier);

		instance.setValidation(IfMapValidationType.NONE);
		instance.setValidation(null);

		assertNull(instance.getValidationAttributes());
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

		byte[] expResult = DatatypeConverter.parseHexBinary("84000682006D6D792D73657373696F6E2D696484000288006B6D792D6964656E746974790209016B6964656E"
				+ "746974792D616403666D792D6F746480");

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

		byte[] expResult = DatatypeConverter.parseHexBinary("8400068E006D6D792D73657373696F6E2D696402726D617463682D6C696E6B732D66696C7465720302041A00"
				+ "4C4B40056D726573756C742D66696C746572066E6163636573732D72657175657374010184000288006B6D792D6964656E746974790209016B6964656E74697479"
				+ "2D616403666D792D6F746480");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
