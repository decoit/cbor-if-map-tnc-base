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
public class CBORPublishDeleteTest extends AbstractTestBase {
	private final String filter = "my-filter-string";
	private final AbstractIdentifier identifierA = new CBORIdentity("my-dns-name", IfMapIdentityType.DNS_NAME);
	private final AbstractIdentifier identifierB = new CBORDevice("device-01", false);


	@Test
	public void testConstructor_IdentifierA_IdentifierB() {
		CBORPublishDelete instance = new CBORPublishDelete(this.identifierA, this.identifierB);

		assertEquals("delete", instance.getElementName());
		assertEquals(this.identifierA, instance.getIdentifierA());
		assertEquals(this.identifierB, instance.getIdentifierB());
		assertNull(instance.getFilter());
	}


	@Test
	public void testConstructor_IdentifierA_null() {
		CBORPublishDelete instance = new CBORPublishDelete(this.identifierA, null);

		assertEquals("delete", instance.getElementName());
		assertEquals(this.identifierA, instance.getIdentifierA());
		assertNull(instance.getIdentifierB());
		assertNull(instance.getFilter());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null_IdentifierB() {
		CBORPublishDelete instance = new CBORPublishDelete(null, this.identifierB);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_IdentifierA_IdentifierA() {
		CBORPublishDelete instance = new CBORPublishDelete(this.identifierA, this.identifierA);
	}


	@Test
	public void testSetFilter() {
		CBORPublishDelete instance = new CBORPublishDelete(this.identifierA, null);

		instance.setFilter(this.filter);

		assertEquals(this.filter, instance.getFilter());
	}


	@Test
	public void testSetFilter_null() {
		CBORPublishDelete instance = new CBORPublishDelete(this.identifierA, null);

		instance.setFilter(this.filter);
		instance.setFilter(null);

		assertNull(instance.getFilter());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetFilter_EmptyString() {
		CBORPublishDelete instance = new CBORPublishDelete(this.identifierA, null);

		instance.setFilter("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetFilter_Whitespaces() {
		CBORPublishDelete instance = new CBORPublishDelete(this.identifierA, null);

		instance.setFilter("   ");
	}


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

		byte[] expResult = DatatypeConverter.parseHexBinary("84F6028084000284006B6D792D646E732D6E616D65020280");

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

		byte[] expResult = DatatypeConverter.parseHexBinary("84F6028088000284006B6D792D646E732D6E616D6502028000018084F60080696465766963652D3031");

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

		byte[] expResult = DatatypeConverter.parseHexBinary("84F6028200706D792D66696C7465722D737472696E6788000284006B6D792D646E732D6E616D650202800001"
				+ "8084F60080696465766963652D3031");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
