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
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.ifmap.identifier.CBORDevice;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import de.decoit.simu.cbor.ifmap.metadata.AbstractMetadata;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORCapability;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORDeviceAttribute;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.io.ByteArrayOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class DeletePollSearchResultTest extends AbstractTestBase {
	private final AbstractIdentifier indentifierA = new CBORIdentity("my-dns-name", IfMapIdentityType.DNS_NAME);
	private final AbstractIdentifier indentifierB = new CBORDevice("device-01", false);
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String capabilityName1 = "cap-01";
	private final String capabilityName2 = "cap-02";
	private final String devAttrName = "my-attribute";
	private final List<SearchResultItem> searchResultItems = new ArrayList<>();
	private final String name = "sr-name";


	public DeletePollSearchResultTest() {
		AbstractMetadata m1 = new CBORCapability(this.publisherId, this.ifMapTimestamp, this.capabilityName1);
		AbstractMetadata m2 = new CBORCapability(this.publisherId, this.ifMapTimestamp, this.capabilityName2);
		AbstractMetadata m3 = new CBORDeviceAttribute(this.publisherId, this.ifMapTimestamp, this.devAttrName);

		SearchResultItem sri1 = new SearchResultItem(this.indentifierA);
		sri1.addMetadata(m3);

		SearchResultItem sri2 = new SearchResultItem(this.indentifierB);
		sri2.setIdentifierB(this.indentifierA);
		sri2.addMetadata(m1);
		sri2.addMetadata(m2);

		this.searchResultItems.add(sri1);
		this.searchResultItems.add(sri2);
	}


	@Test
	public void testConstructor() {
		DeletePollSearchResult instance = new DeletePollSearchResult();

		assertEquals("deleteResult", instance.getElementName());
		assertNull(instance.getName());
		assertTrue(instance.getResultItems().isEmpty());
	}


	@Test
	public void testCborSerialize_NoResults() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response+pollResult");

		DeletePollSearchResult instance = new DeletePollSearchResult();
		instance.setName(this.name);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (no results):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F60282006773722D6E616D6580");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_TwoResults() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response+pollResult");

		DeletePollSearchResult instance = new DeletePollSearchResult();
		instance.setName(this.name);
		instance.addSearchResultItem(this.searchResultItems.get(0));
		instance.addSearchResultItem(this.searchResultItems.get(1));

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (two results):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F60282006773722D6E616D6588F6008088000284006B6D792D646E732D6E616D65020280F6008084010688"
				+ "006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030184F600806C6D792D617474726962757465F600808C00018084F600806964"
				+ "65766963652D3031000284006B6D792D646E732D6E616D65020280F6008088010588006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075B"
				+ "CA00030184F60080666361702D3031010588006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030184F60080666361702D3032");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
