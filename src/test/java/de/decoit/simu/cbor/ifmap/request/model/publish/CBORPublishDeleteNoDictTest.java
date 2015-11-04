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
package de.decoit.simu.cbor.ifmap.request.model.publish;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.enums.IfMapIdentityType;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.ifmap.identifier.CBORDevice;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryComplexElement;
import de.decoit.simu.cbor.xml.dictionary.DictionaryNamespace;
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
public class CBORPublishDeleteNoDictTest {
	private final String filter = "my-filter-string";
	private final AbstractIdentifier identifierA = new CBORIdentity("my-dns-name", IfMapIdentityType.DNS_NAME);
	private final AbstractIdentifier identifierB = new CBORDevice("device-01", false);


	@Test
	public void testCborSerialize_SingleIdentifier() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">publish");

		CBORPublishDelete instance = new CBORPublishDelete(this.identifierA, null);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (1 identifier):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66664656C65746580847831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75"
				+ "702E6F72672F323031302F49464D41502F32686964656E7469747984646E616D656B6D792D646E732D6E616D65647479706568646E732D6E616D6580");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_TwoIdentifiers() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">publish");

		CBORPublishDelete instance = new CBORPublishDelete(this.identifierA, this.identifierB);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (2 identifiers):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66664656C65746580887831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75"
				+ "702E6F72672F323031302F49464D41502F32686964656E7469747984646E616D656B6D792D646E732D6E616D65647479706568646E732D6E616D65807831687474"
				+ "703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F32666465766963658084F6646E616D6580696465"
				+ "766963652D3031");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_full() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">publish");

		CBORPublishDelete instance = new CBORPublishDelete(this.identifierA, this.identifierB);
		instance.setFilter(this.filter);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66664656C657465826666696C746572706D792D66696C7465722D737472696E67887831687474703A2F2F"
				+ "7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F32686964656E7469747984646E616D656B6D792D646E732D"
				+ "6E616D65647479706568646E732D6E616D65807831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F4946"
				+ "4D41502F32666465766963658084F6646E616D6580696465766963652D3031");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
