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
public class CBORPurgePublisherRequestTest extends AbstractTestBase {
	private final String sessionId = "my-session-id";
	private final String publisherId = "my-publisher-id";


	@Test
	public void testConstructor() {
		CBORPurgePublisherRequest instance = new CBORPurgePublisherRequest(this.sessionId, this.publisherId);

		assertEquals(IfMapNamespaces.IFMAP, instance.getNamespace());
		assertEquals("purgePublisher", instance.getElementName());
		assertEquals(this.sessionId, instance.getSessionAttributes().getSessionId());
		assertEquals(this.publisherId, instance.getIfMapPublisherId());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null_String() {
		CBORPurgePublisherRequest instance = new CBORPurgePublisherRequest(null, this.publisherId);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyString_String() {
		CBORPurgePublisherRequest instance = new CBORPurgePublisherRequest("", this.publisherId);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_Whitespaces_String() {
		CBORPurgePublisherRequest instance = new CBORPurgePublisherRequest("   ", this.publisherId);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_String_null() {
		CBORPurgePublisherRequest instance = new CBORPurgePublisherRequest(this.sessionId, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_String_EmptyString() {
		CBORPurgePublisherRequest instance = new CBORPurgePublisherRequest(this.sessionId, "");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_String_Whitespaces() {
		CBORPurgePublisherRequest instance = new CBORPurgePublisherRequest(this.sessionId, "   ");
	}


	@Test
	public void testCborSerialize() throws Exception {
		CBORPurgePublisherRequest instance = new CBORPurgePublisherRequest("my-session-id", "my-publisher-id");

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84000984006D6D792D73657373696F6E2D6964016F6D792D7075626C69736865722D696480");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
