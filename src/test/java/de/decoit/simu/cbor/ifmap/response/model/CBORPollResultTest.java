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
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.enums.IfMapErrorCode;
import de.decoit.simu.cbor.ifmap.enums.IfMapIdentityType;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.ifmap.identifier.CBORDevice;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import de.decoit.simu.cbor.ifmap.metadata.AbstractMetadata;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORCapability;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORDeviceAttribute;
import de.decoit.simu.cbor.ifmap.response.model.search.AbstractPollSearchResult;
import de.decoit.simu.cbor.ifmap.response.model.search.DeletePollSearchResult;
import de.decoit.simu.cbor.ifmap.response.model.search.NotifyPollSearchResult;
import de.decoit.simu.cbor.ifmap.response.model.search.SearchPollSearchResult;
import de.decoit.simu.cbor.ifmap.response.model.search.SearchResultItem;
import de.decoit.simu.cbor.ifmap.response.model.search.UpdatePollSearchResult;
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
public class CBORPollResultTest extends AbstractTestBase {
	private final AbstractIdentifier indentifierA = new CBORIdentity("my-dns-name", IfMapIdentityType.DNS_NAME);
	private final AbstractIdentifier indentifierB = new CBORDevice("device-01", false);
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String capabilityName1 = "cap-01";
	private final String capabilityName2 = "cap-02";
	private final String devAttrName = "my-attribute";
	private final String name = "sr-name";
	private final CBORErrorResult errorResult = new CBORErrorResult(IfMapErrorCode.FAILURE);
	private final DeletePollSearchResult deleteResult = new DeletePollSearchResult();
	private final NotifyPollSearchResult notifyResult = new NotifyPollSearchResult();
	private final SearchPollSearchResult searchResult = new SearchPollSearchResult();
	private final UpdatePollSearchResult updateResult = new UpdatePollSearchResult();
	private final String errorString = "custom-error";


	public CBORPollResultTest() {
		AbstractMetadata m1 = new CBORCapability(this.publisherId, this.ifMapTimestamp, this.capabilityName1);
		AbstractMetadata m2 = new CBORCapability(this.publisherId, this.ifMapTimestamp, this.capabilityName2);
		AbstractMetadata m3 = new CBORDeviceAttribute(this.publisherId, this.ifMapTimestamp, this.devAttrName);

		SearchResultItem sri1 = new SearchResultItem(this.indentifierA);
		sri1.addMetadata(m3);

		SearchResultItem sri2 = new SearchResultItem(this.indentifierB);
		sri2.setIdentifierB(this.indentifierA);
		sri2.addMetadata(m1);
		sri2.addMetadata(m2);

		this.deleteResult.addSearchResultItem(sri1);
		this.deleteResult.addSearchResultItem(sri2);

		this.notifyResult.addSearchResultItem(sri1);
		this.notifyResult.addSearchResultItem(sri2);

		this.searchResult.addSearchResultItem(sri1);
		this.searchResult.addSearchResultItem(sri2);

		this.updateResult.addSearchResultItem(sri1);
		this.updateResult.addSearchResultItem(sri2);

		this.errorResult.setErrorString(this.errorString);
		this.errorResult.setName("sub-01");
	}


	@Test
	public void testConstructor() {
		CBORPollResult instance = new CBORPollResult();

		assertEquals("pollResult", instance.getElementName());
		assertTrue(instance.getResults().isEmpty());
	}


	@Test
	public void testAddPollResult_CBORErrorResult() {
		CBORPollResult instance = new CBORPollResult();
		instance.addPollResult(this.errorResult);

		assertEquals(1, instance.getResults().size());
		assertEquals(this.errorResult, instance.getResults().get(0));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddPollResult_CBORErrorResult_null() {
		CBORPollResult instance = new CBORPollResult();
		instance.addPollResult((CBORErrorResult) null);
	}


	@Test
	public void testAddPollResult_AbstractPollSearchResult() {
		CBORPollResult instance = new CBORPollResult();
		instance.addPollResult(this.deleteResult);
		instance.addPollResult(this.searchResult);
		instance.addPollResult(this.notifyResult);
		instance.addPollResult(this.updateResult);

		assertEquals(4, instance.getResults().size());
		assertEquals(this.deleteResult, instance.getResults().get(0));
		assertEquals(this.searchResult, instance.getResults().get(1));
		assertEquals(this.notifyResult, instance.getResults().get(2));
		assertEquals(this.updateResult, instance.getResults().get(3));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddPollResult_AbstractPollSearchResult_null() {
		CBORPollResult instance = new CBORPollResult();
		instance.addPollResult((AbstractPollSearchResult) null);
	}


	@Test
	public void testRemovePollResult_CBORErrorResult() {
		CBORPollResult instance = new CBORPollResult();
		instance.addPollResult(this.errorResult);

		instance.removePollResult(this.errorResult);

		assertTrue(instance.getResults().isEmpty());
	}


	@Test
	public void testRemovePollResult_CBORErrorResult_null() {
		CBORPollResult instance = new CBORPollResult();
		instance.addPollResult(this.errorResult);

		instance.removePollResult((CBORErrorResult) null);

		assertEquals(1, instance.getResults().size());
	}


	@Test
	public void testRemovePollResult_AbstractPollSearchResult() {
		CBORPollResult instance = new CBORPollResult();
		instance.addPollResult(this.deleteResult);

		instance.removePollResult(this.deleteResult);

		assertTrue(instance.getResults().isEmpty());
	}


	@Test
	public void testRemovePollResult_AbstractPollSearchResult_null() {
		CBORPollResult instance = new CBORPollResult();
		instance.addPollResult(this.deleteResult);

		instance.removePollResult((AbstractPollSearchResult) null);

		assertEquals(1, instance.getResults().size());
	}


	@Test
	public void testCborSerialize() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORPollResult instance = new CBORPollResult();
		instance.addPollResult(this.errorResult);
		instance.addPollResult(this.deleteResult);
		instance.addPollResult(this.notifyResult);
		instance.addPollResult(this.searchResult);
		instance.addPollResult(this.updateResult);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F6018094F60484000101667375622D303184F600806C637573746F6D"
				+ "2D6572726F72F6028088F6008088000284006B6D792D646E732D6E616D65020280F6008084010688006F6D792D7075626C69"
				+ "736865722D696401C11A4ED9E8B202C482281A075BCA00030184F600806C6D792D617474726962757465F600808C00018084"
				+ "F60080696465766963652D3031000284006B6D792D646E732D6E616D65020280F6008088010588006F6D792D7075626C6973"
				+ "6865722D696401C11A4ED9E8B202C482281A075BCA00030184F60080666361702D3031010588006F6D792D7075626C697368"
				+ "65722D696401C11A4ED9E8B202C482281A075BCA00030184F60080666361702D3032F6038088F6008088000284006B6D792D"
				+ "646E732D6E616D65020280F6008084010688006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA"
				+ "00030184F600806C6D792D617474726962757465F600808C00018084F60080696465766963652D3031000284006B6D792D64"
				+ "6E732D6E616D65020280F6008088010588006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00"
				+ "030184F60080666361702D3031010588006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003"
				+ "0184F60080666361702D3032F6008088F6008088000284006B6D792D646E732D6E616D65020280F6008084010688006F6D79"
				+ "2D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030184F600806C6D792D617474726962757465F600"
				+ "808C00018084F60080696465766963652D3031000284006B6D792D646E732D6E616D65020280F6008088010588006F6D792D"
				+ "7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030184F60080666361702D3031010588006F6D792D70"
				+ "75626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030184F60080666361702D3032F6018088F60080880002"
				+ "84006B6D792D646E732D6E616D65020280F6008084010688006F6D792D7075626C69736865722D696401C11A4ED9E8B202C4"
				+ "82281A075BCA00030184F600806C6D792D617474726962757465F600808C00018084F60080696465766963652D3031000284"
				+ "006B6D792D646E732D6E616D65020280F6008088010588006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482"
				+ "281A075BCA00030184F60080666361702D3031010588006F6D792D7075626C69736865722D696401C11A4ED9E8B202C48228"
				+ "1A075BCA00030184F60080666361702D3032");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
