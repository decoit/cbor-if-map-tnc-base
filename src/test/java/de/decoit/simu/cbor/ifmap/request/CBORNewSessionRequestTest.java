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
public class CBORNewSessionRequestTest extends AbstractTestBase {
	private final Integer maxPollResultSize = 5000000;


	@Test
	public void testConstructor() {
		CBORNewSessionRequest instance = new CBORNewSessionRequest();

		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("newSession", instance.getElementName());
		assertNull(instance.getMaxPollResultSize());
	}


	@Test
	public void testSetMaxPollResultSize() {
		CBORNewSessionRequest instance = new CBORNewSessionRequest();

		instance.setMaxPollResultSize(this.maxPollResultSize);

		assertEquals(this.maxPollResultSize, instance.getMaxPollResultSize());
	}


	@Test
	public void testSetMaxPollResultSize_null() {
		CBORNewSessionRequest instance = new CBORNewSessionRequest();

		instance.setMaxPollResultSize(this.maxPollResultSize);
		instance.setMaxPollResultSize(null);

		assertNull(instance.getMaxPollResultSize());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetMaxPollResultSize_Zero() {
		CBORNewSessionRequest instance = new CBORNewSessionRequest();

		instance.setMaxPollResultSize(0);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetMaxPollResultSize_Negative() {
		CBORNewSessionRequest instance = new CBORNewSessionRequest();

		instance.setMaxPollResultSize(-500);
	}


	@Test
	public void testCborSerialize() throws Exception {
		CBORNewSessionRequest instance = new CBORNewSessionRequest();

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000A8080");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		CBORNewSessionRequest instance = new CBORNewSessionRequest();
		instance.setMaxPollResultSize(this.maxPollResultSize);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000A82001A004C4B4080");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
