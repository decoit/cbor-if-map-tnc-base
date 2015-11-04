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
package de.decoit.simu.cbor.ifmap.response.model.search;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.enums.IfMapIdentityType;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.ifmap.identifier.CBORDevice;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import de.decoit.simu.cbor.ifmap.metadata.AbstractMetadata;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORCapability;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORDeviceAttribute;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORDiscoveredBy;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.io.ByteArrayOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
public class SearchResultItemTest extends AbstractTestBase {
	private final AbstractIdentifier identifierA = new CBORIdentity("my-dns-name", IfMapIdentityType.DNS_NAME);
	private final AbstractIdentifier identifierB = new CBORDevice("device-01", false);
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String capabilityName1 = "cap-01";
	private final String capabilityName2 = "cap-02";
	private final String devAttrName = "my-attribute";
	private final AbstractMetadata m1 = new CBORCapability(this.publisherId, this.ifMapTimestamp, this.capabilityName1);
	private final AbstractMetadata m2 = new CBORCapability(this.publisherId, this.ifMapTimestamp, this.capabilityName2);
	private final AbstractMetadata m3 = new CBORDeviceAttribute(this.publisherId, this.ifMapTimestamp, this.devAttrName);


	@Test
	public void testConstructor_IdentifierA() {
		SearchResultItem instance = new SearchResultItem(this.identifierA);

		assertEquals("resultItem", instance.getElementName());
		assertEquals(this.identifierA, instance.getIdentifierA());
		assertNull(instance.getIdentifierB());
		assertTrue(instance.getMetadata().isEmpty());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null() {
		SearchResultItem instance = new SearchResultItem(null);
	}


	@Test
	public void testConstructor_IdentifierA_IdentifierB() {
		SearchResultItem instance = new SearchResultItem(this.identifierA, this.identifierB);

		assertEquals("resultItem", instance.getElementName());
		assertEquals(this.identifierA, instance.getIdentifierA());
		assertEquals(this.identifierB, instance.getIdentifierB());
		assertTrue(instance.getMetadata().isEmpty());
	}


	@Test
	public void testConstructor_IdentifierA_null() {
		SearchResultItem instance = new SearchResultItem(this.identifierA, null);

		assertEquals("resultItem", instance.getElementName());
		assertEquals(this.identifierA, instance.getIdentifierA());
		assertNull(instance.getIdentifierB());
		assertTrue(instance.getMetadata().isEmpty());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_IdentifierA_IdentifierA() {
		SearchResultItem instance = new SearchResultItem(this.identifierA, this.identifierA);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null_IdentifierB() {
		SearchResultItem instance = new SearchResultItem(null, this.identifierB);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_null_null() {
		SearchResultItem instance = new SearchResultItem(null, null);
	}


	@Test
	public void testSetIdentifierB() {
		SearchResultItem instance = new SearchResultItem(this.identifierA, null);
		instance.setIdentifierB(this.identifierB);

		assertEquals(this.identifierB, instance.getIdentifierB());
	}


	@Test
	public void testSetIdentifierB_null() {
		SearchResultItem instance = new SearchResultItem(this.identifierA, null);
		instance.setIdentifierB(this.identifierB);
		instance.setIdentifierB(null);

		assertNull(instance.getIdentifierB());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetIdentifierB_IdentifierA() {
		SearchResultItem instance = new SearchResultItem(this.identifierA, null);
		instance.setIdentifierB(this.identifierA);
	}


	@Test
	public void testAddMetadata() {
		SearchResultItem instance = new SearchResultItem(this.identifierA, null);

		instance.addMetadata(this.m1);
		instance.addMetadata(this.m2);

		assertTrue(instance.getMetadata().size() == 2);
		assertEquals(this.m1, instance.getMetadata().get(0));
		assertEquals(this.m2, instance.getMetadata().get(1));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddMetadata_null() {
		SearchResultItem instance = new SearchResultItem(this.identifierA, null);

		instance.addMetadata(null);
	}


	@Test
	public void testRemoveMetadata() {
		SearchResultItem instance = new SearchResultItem(this.identifierA, null);

		instance.addMetadata(this.m1);
		instance.addMetadata(this.m2);

		instance.removeMetadata(this.m1);

		assertTrue(instance.getMetadata().size() == 1);
		assertEquals(this.m2, instance.getMetadata().get(0));
	}


	@Test
	public void testRemoveMetadata_null() {
		SearchResultItem instance = new SearchResultItem(this.identifierA, null);

		instance.addMetadata(this.m1);
		instance.addMetadata(this.m2);

		instance.removeMetadata(null);

		assertTrue(instance.getMetadata().size() == 2);
		assertEquals(this.m1, instance.getMetadata().get(0));
		assertEquals(this.m2, instance.getMetadata().get(1));
	}


	@Test
	public void testRemoveMetadata_NonExistent() {
		SearchResultItem instance = new SearchResultItem(this.identifierA, null);

		instance.addMetadata(this.m1);
		instance.addMetadata(this.m2);

		instance.removeMetadata(new CBORDiscoveredBy(this.publisherId, this.ifMapTimestamp));

		assertTrue(instance.getMetadata().size() == 2);
		assertEquals(this.m1, instance.getMetadata().get(0));
		assertEquals(this.m2, instance.getMetadata().get(1));
	}


	@Test
	public void testRemoveMetadata_SameTwice() {
		SearchResultItem instance = new SearchResultItem(this.identifierA, null);

		instance.addMetadata(this.m1);
		instance.addMetadata(this.m2);

		instance.removeMetadata(this.m1);
		instance.removeMetadata(this.m1);

		assertTrue(instance.getMetadata().size() == 1);
		assertEquals(this.m2, instance.getMetadata().get(0));
	}


	@Test
	public void testRemoveAllMetadata() {
		SearchResultItem instance = new SearchResultItem(this.identifierA, null);

		instance.addMetadata(this.m1);
		instance.addMetadata(this.m2);

		instance.removeAllMetadata();

		assertTrue(instance.getMetadata().isEmpty());
	}


	@Test
	public void testCborSerialize_SingleIdentifier() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response+searchResult");

		SearchResultItem instance = new SearchResultItem(this.identifierA, null);
		instance.addMetadata(this.m1);
		instance.addMetadata(this.m2);
		instance.addMetadata(this.m3);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (1 identifier):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F6008088000284006B6D792D646E732D6E616D65020280F600808C010588006F6D792D7075626C69736865"
				+ "722D696401C11A4ED9E8B202C482281A075BCA00030184F60080666361702D3031010588006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A"
				+ "075BCA00030184F60080666361702D3032010688006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030184F600806C6D792D6174"
				+ "74726962757465");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_TwoIdentifiers() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response+searchResult");

		SearchResultItem instance = new SearchResultItem(this.identifierA, this.identifierB);
		instance.addMetadata(this.m1);
		instance.addMetadata(this.m2);
		instance.addMetadata(this.m3);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (2 identifiers):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F600808C000284006B6D792D646E732D6E616D6502028000018084F60080696465766963652D3031F60080"
				+ "8C010588006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030184F60080666361702D3031010588006F6D792D7075626C697368"
				+ "65722D696401C11A4ED9E8B202C482281A075BCA00030184F60080666361702D3032010688006F6D792D7075626C69736865722D696401C11A4ED9E8B202C48228"
				+ "1A075BCA00030184F600806C6D792D617474726962757465");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test(expected = CBORSerializationException.class)
	public void testCborSerialize_EmptyMetadata() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response+searchResult");

		SearchResultItem instance = new SearchResultItem(this.identifierA, this.identifierB);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);
	}
}
