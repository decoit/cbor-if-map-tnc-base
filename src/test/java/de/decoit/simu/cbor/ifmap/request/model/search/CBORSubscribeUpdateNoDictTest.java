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
public class CBORSubscribeUpdateNoDictTest {
	private final String name = "sub01";
	private final CBORAccessRequest identifier;
	private final String matchLinks = "match-links-filter";
	private final String resultFilter = "result-filter";
	private final String terminalIdentifierType = "access-request";
	private final Integer maxDepth = 2;
	private final Integer maxSize = 5000000;


	public CBORSubscribeUpdateNoDictTest() {
		this.identifier = new CBORAccessRequest("my-access-request");
		this.identifier.setAdministrativeDomain("access-request:administrative-domain");
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

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66675706461746582646E616D65657375623031847831687474703A2F2F7777772E74727573746564636F"
				+ "6D707574696E6767726F75702E6F72672F323031302F49464D41502F326E6163636573732D7265717565737484646E616D65716D792D6163636573732D72657175"
				+ "6573747561646D696E6973747261746976652D646F6D61696E78246163636573732D726571756573743A61646D696E6973747261746976652D646F6D61696E80");

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

		byte[] expResult = DatatypeConverter.parseHexBinary("84F6667570646174658C646E616D656573756230316B6D617463682D6C696E6B73726D617463682D6C696E6B"
				+ "732D66696C746572696D61782D646570746802686D61782D73697A651A004C4B406D726573756C742D66696C7465726D726573756C742D66696C74657278187465"
				+ "726D696E616C2D6964656E7469666965722D747970656E6163636573732D72657175657374847831687474703A2F2F7777772E74727573746564636F6D70757469"
				+ "6E6767726F75702E6F72672F323031302F49464D41502F326E6163636573732D7265717565737484646E616D65716D792D6163636573732D726571756573747561"
				+ "646D696E6973747261746976652D646F6D61696E78246163636573732D726571756573743A61646D696E6973747261746976652D646F6D61696E80");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
