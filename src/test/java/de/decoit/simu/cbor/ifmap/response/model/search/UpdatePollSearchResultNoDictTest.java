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
public class UpdatePollSearchResultNoDictTest {
	private final AbstractIdentifier indentifierA = new CBORIdentity("my-dns-name", IfMapIdentityType.DNS_NAME);
	private final AbstractIdentifier indentifierB = new CBORDevice("device-01", false);
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String capabilityName1 = "cap-01";
	private final String capabilityName2 = "cap-02";
	private final String devAttrName = "my-attribute";
	private final List<SearchResultItem> searchResultItems = new ArrayList<>();
	private final String name = "sr-name";


	public UpdatePollSearchResultNoDictTest() {
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
	public void testCborSerialize_NoResults() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response+pollResult");

		UpdatePollSearchResult instance = new UpdatePollSearchResult();
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

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66C757064617465526573756C7482646E616D656773722D6E616D6580");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_TwoResults() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response+pollResult");

		UpdatePollSearchResult instance = new UpdatePollSearchResult();
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

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66C757064617465526573756C7482646E616D656773722D6E616D6588F66A726573756C744974656D8088"
				+ "7831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F32686964656E7469747984646E616D"
				+ "656B6D792D646E732D6E616D65647479706568646E732D6E616D6580F6686D6574616461746180847839687474703A2F2F7777772E74727573746564636F6D7075"
				+ "74696E6767726F75702E6F72672F323031302F49464D41504D455441444154412F32706465766963652D617474726962757465887269666D61702D7075626C6973"
				+ "6865722D69646F6D792D7075626C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D667261"
				+ "6374696F6EC482281A075BCA007169666D61702D63617264696E616C6974796A6D756C746956616C756584F6646E616D65806C6D792D617474726962757465F66A"
				+ "726573756C744974656D808C7831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F326664"
				+ "65766963658084F6646E616D6580696465766963652D30317831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F3230"
				+ "31302F49464D41502F32686964656E7469747984646E616D656B6D792D646E732D6E616D65647479706568646E732D6E616D6580F6686D65746164617461808878"
				+ "39687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41504D455441444154412F326A636170616269"
				+ "6C697479887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869"
				+ "666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E616C6974796A6D756C746956616C756584F6646E61"
				+ "6D6580666361702D30317839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41504D4554414441"
				+ "54412F326A6361706162696C697479887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D61702D74696D65737461"
				+ "6D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E616C6974796A6D756C74"
				+ "6956616C756584F6646E616D6580666361702D3032");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
