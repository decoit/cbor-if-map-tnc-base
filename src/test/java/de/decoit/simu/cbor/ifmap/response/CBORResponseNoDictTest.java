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
import de.decoit.simu.cbor.ifmap.enums.IfMapValidationType;
import de.decoit.simu.cbor.ifmap.response.model.CBORNewSessionResult;
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
public class CBORResponseNoDictTest {
	private final String sessionId = "my-session-id";
	private final String publisherId = "my-publisher-id";
	private final Integer maxPollResultSize = 50000000;
	private final CBORNewSessionResult newSessionResult;


	public CBORResponseNoDictTest() {
		this.newSessionResult = new CBORNewSessionResult(this.sessionId, this.publisherId);
		this.newSessionResult.setMaxPollResultSize(this.maxPollResultSize);
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

		byte[] expResult = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41502F3268726573706F6E7365826A76616C69646174696F6E63416C6C84F6706E657753657373696F6E526573756C74866A73657373696F6E2D69646D"
				+ "6D792D73657373696F6E2D69647269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D6964746D61782D706F6C6C2D726573756C742D"
				+ "73697A651A02FAF08080");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
