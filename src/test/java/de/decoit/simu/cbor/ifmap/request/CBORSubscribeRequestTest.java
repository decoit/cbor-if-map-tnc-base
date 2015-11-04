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
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import de.decoit.simu.cbor.ifmap.request.model.search.CBORSubscribeDelete;
import de.decoit.simu.cbor.ifmap.request.model.search.CBORSubscribeUpdate;
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
public class CBORSubscribeRequestTest extends AbstractTestBase {
	private final String sessionId = "my-session-id";
	private final CBORSubscribeUpdate subTypeUpd;
	private final CBORSubscribeDelete subTypeDel;
	private final CBORIdentity identifier;
	private final String matchLinks = "match-links-filter";
	private final String resultFilter = "result-filter";
	private final String terminalIdentifierType = "access-request";
	private final Integer maxDepth = 2;
	private final Integer maxSize = 5000000;


	public CBORSubscribeRequestTest() {
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
	public void testConstructor() {
		CBORSubscribeRequest instance = new CBORSubscribeRequest(this.sessionId);

		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("subscribe", instance.getElementName());
		assertEquals(this.sessionId, instance.getSessionAttributes().getSessionId());
		assertNull(instance.getValidationAttributes());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null() {
		CBORSubscribeRequest instance = new CBORSubscribeRequest(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyString() {
		CBORSubscribeRequest instance = new CBORSubscribeRequest("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Whitespaces() {
		CBORSubscribeRequest instance = new CBORSubscribeRequest("   ");
	}


	@Test
	public void testSetValidation() {
		CBORSubscribeRequest instance = new CBORSubscribeRequest(this.sessionId);

		instance.setValidation(IfMapValidationType.NONE);

		assertEquals(IfMapValidationType.NONE, instance.getValidationAttributes().getValidationType());
	}


	@Test
	public void testSetValidation_null() {
		CBORSubscribeRequest instance = new CBORSubscribeRequest(this.sessionId);

		instance.setValidation(IfMapValidationType.NONE);
		instance.setValidation(null);

		assertNull(instance.getValidationAttributes());
	}


	@Test
	public void testAddSubscribeType() {
		CBORSubscribeRequest instance = new CBORSubscribeRequest(this.sessionId);

		instance.addSubscribeType(this.subTypeUpd);
		instance.addSubscribeType(this.subTypeDel);
		instance.addSubscribeType(this.subTypeUpd);

		assertEquals(3, instance.getSubscripeTypes().size());
		assertEquals(this.subTypeUpd, instance.getSubscripeTypes().get(0));
		assertEquals(this.subTypeDel, instance.getSubscripeTypes().get(1));
		assertEquals(this.subTypeUpd, instance.getSubscripeTypes().get(2));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddSubscribeType_null() {
		CBORSubscribeRequest instance = new CBORSubscribeRequest(this.sessionId);

		instance.addSubscribeType(null);
	}


	@Test
	public void testRemoveSubscribeType() {
		CBORSubscribeRequest instance = new CBORSubscribeRequest(this.sessionId);

		instance.addSubscribeType(this.subTypeUpd);
		instance.addSubscribeType(this.subTypeDel);

		instance.removeSubscribeType(this.subTypeDel);
		assertEquals(1, instance.getSubscripeTypes().size());
	}


	@Test
	public void testRemoveSubscribeType_null() {
		CBORSubscribeRequest instance = new CBORSubscribeRequest(this.sessionId);

		instance.addSubscribeType(this.subTypeUpd);
		instance.addSubscribeType(this.subTypeDel);

		instance.removeSubscribeType(null);
		assertEquals(2, instance.getSubscripeTypes().size());
	}


	@Test
	public void testRemoveSubscribeType_SameTwice() {
		CBORSubscribeRequest instance = new CBORSubscribeRequest(this.sessionId);

		instance.addSubscribeType(this.subTypeUpd);
		instance.addSubscribeType(this.subTypeDel);

		instance.removeSubscribeType(this.subTypeDel);
		instance.removeSubscribeType(this.subTypeDel);
		assertEquals(1, instance.getSubscripeTypes().size());
	}


	@Test
	public void testRemoveSubscribeType_NonExistent() {
		CBORSubscribeRequest instance = new CBORSubscribeRequest(this.sessionId);

		instance.addSubscribeType(this.subTypeUpd);
		instance.addSubscribeType(this.subTypeDel);

		instance.removeSubscribeType(new CBORSubscribeDelete("mos2"));
		assertEquals(2, instance.getSubscripeTypes().size());
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

		byte[] expResult = DatatypeConverter.parseHexBinary("84000784006D6D792D73657373696F6E2D6964010384F6018200736D792D6F6C642D73756273637269707469"
				+ "6F6E80");

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

		byte[] expResult = DatatypeConverter.parseHexBinary("84000784006D6D792D73657373696F6E2D6964010388F6018200736D792D6F6C642D73756273637269707469"
				+ "6F6E80F6008C006F6D792D737562736372697074696F6E02726D617463682D6C696E6B732D66696C7465720302041A004C4B40056D726573756C742D66696C7465"
				+ "72066E6163636573732D7265717565737484000288006B6D792D6964656E746974790209016B6964656E746974792D616403666D792D6F746480");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test(expected = CBORSerializationException.class)
	public void testCborSerialize_NoSubscriptionTypes() throws Exception {
		CBORSubscribeRequest instance = new CBORSubscribeRequest(this.sessionId);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);
	}
}
