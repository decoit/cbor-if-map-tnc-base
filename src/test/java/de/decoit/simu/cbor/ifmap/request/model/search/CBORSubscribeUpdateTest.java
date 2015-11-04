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
package de.decoit.simu.cbor.ifmap.request.model.search;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.identifier.CBORAccessRequest;
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
public class CBORSubscribeUpdateTest extends AbstractTestBase {
	private final String name = "sub01";
	private final CBORAccessRequest identifier;
	private final String matchLinks = "match-links-filter";
	private final String resultFilter = "result-filter";
	private final String terminalIdentifierType = "access-request";
	private final Integer maxDepth = 2;
	private final Integer maxSize = 5000000;


	public CBORSubscribeUpdateTest() {
		this.identifier = new CBORAccessRequest("my-access-request");
		this.identifier.setAdministrativeDomain("access-request:administrative-domain");
	}


	@Test
	public void testConstructor() {
		CBORSubscribeUpdate instance = new CBORSubscribeUpdate(this.name, this.identifier);

		assertEquals("update", instance.getElementName());
		assertEquals(this.name, instance.getName());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NameNull() {
		CBORSubscribeUpdate instance = new CBORSubscribeUpdate(null, this.identifier);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NameEmptyString() {
		CBORSubscribeUpdate instance = new CBORSubscribeUpdate("", this.identifier);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NameWhitespaces() {
		CBORSubscribeUpdate instance = new CBORSubscribeUpdate("   ", this.identifier);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_IdentifierNull() {
		CBORSubscribeUpdate instance = new CBORSubscribeUpdate(this.name, null);
	}


	@Test
	public void testCborSerialize() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">subscribe");

		CBORSubscribeUpdate instance = new CBORSubscribeUpdate(this.name, this.identifier);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F60082006573756230318400008400716D792D6163636573732D726571756573740178246163636573732D"
				+ "726571756573743A61646D696E6973747261746976652D646F6D61696E80");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">subscribe");

		CBORSubscribeUpdate instance = new CBORSubscribeUpdate(this.name, this.identifier);
		instance.getSearchTypeAttributes().setMatchLinks(this.matchLinks);
		instance.getSearchTypeAttributes().setMaxDepth(this.maxDepth);
		instance.getSearchTypeAttributes().setMaxSize(this.maxSize);
		instance.getSearchTypeAttributes().setResultFilter(this.resultFilter);
		instance.getSearchTypeAttributes().setTerminalIdentifierType(this.terminalIdentifierType);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F6008C0065737562303102726D617463682D6C696E6B732D66696C7465720302041A004C4B40056D726573"
				+ "756C742D66696C746572066E6163636573732D726571756573748400008400716D792D6163636573732D726571756573740178246163636573732D726571756573"
				+ "743A61646D696E6973747261746976652D646F6D61696E80");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
