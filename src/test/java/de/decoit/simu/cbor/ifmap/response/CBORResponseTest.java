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
package de.decoit.simu.cbor.ifmap.response;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.enums.IfMapValidationType;
import de.decoit.simu.cbor.ifmap.response.model.CBORNewSessionResult;
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
public class CBORResponseTest extends AbstractTestBase {
	private final String sessionId = "my-session-id";
	private final String publisherId = "my-publisher-id";
	private final Integer maxPollResultSize = 50000000;
	private final CBORNewSessionResult newSessionResult;


	public CBORResponseTest() {
		this.newSessionResult = new CBORNewSessionResult(this.sessionId, this.publisherId);
		this.newSessionResult.setMaxPollResultSize(this.maxPollResultSize);
	}


	@Test
	public void testConstructor() {
		CBORResponse instance = new CBORResponse(this.newSessionResult);

		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("response", instance.getElementName());
		assertEquals(this.newSessionResult, instance.getResult());
		assertNull(instance.getValidationAttributes());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null() {
		CBORResponse instance = new CBORResponse(null);
	}


	@Test
	public void testSetValidation() {
		CBORResponse instance = new CBORResponse(this.newSessionResult);

		instance.setValidation(IfMapValidationType.NONE);

		assertEquals(IfMapValidationType.NONE, instance.getValidationAttributes().getValidationType());
	}


	@Test
	public void testSetValidation_null() {
		CBORResponse instance = new CBORResponse(this.newSessionResult);

		instance.setValidation(IfMapValidationType.NONE);
		instance.setValidation(null);

		assertNull(instance.getValidationAttributes());
	}


	@Test
	public void testCborSerialize() throws Exception {
		CBORResponse instance = new CBORResponse(this.newSessionResult);
		instance.setValidation(IfMapValidationType.ALL);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000D82000384F60686006D6D792D73657373696F6E2D6964016F6D792D7075626C69736865722D6964021A"
				+ "02FAF08080");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
