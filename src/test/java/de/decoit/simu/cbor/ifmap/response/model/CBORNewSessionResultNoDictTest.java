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
package de.decoit.simu.cbor.ifmap.response.model;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
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
public class CBORNewSessionResultNoDictTest {
	private final String sessionId = "my-session-id";
	private final String publisherId = "my-publisher-id";
	private final Integer maxPollResultSize = 50000000;


	@Test
	public void testCborSerialize() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORNewSessionResult instance = new CBORNewSessionResult(this.sessionId, this.publisherId);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F6706E657753657373696F6E526573756C74846A73657373696F6E2D69646D6D792D73657373696F6E2D69"
				+ "647269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D696480");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORNewSessionResult instance = new CBORNewSessionResult(this.sessionId, this.publisherId);
		instance.setMaxPollResultSize(this.maxPollResultSize);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F6706E657753657373696F6E526573756C74866A73657373696F6E2D69646D6D792D73657373696F6E2D69"
				+ "647269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D6964746D61782D706F6C6C2D726573756C742D73697A651A02FAF08080");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
