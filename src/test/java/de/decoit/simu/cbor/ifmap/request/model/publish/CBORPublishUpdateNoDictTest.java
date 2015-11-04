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
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
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
public class CBORPublishUpdateNoDictTest {
	private final AbstractIdentifier identifierA = new CBORIdentity("my-dns-name", IfMapIdentityType.DNS_NAME);
	private final AbstractIdentifier identifierB = new CBORDevice("device-01", false);
	private final List<AbstractMetadata> metadata = new ArrayList<>();
	private final String publisherId = "my-publisher-id";
	private final ZonedDateTime ifMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String capabilityName1 = "cap-01";
	private final String capabilityName2 = "cap-02";
	private final String devAttrName = "my-attribute";


	public CBORPublishUpdateNoDictTest() {
		AbstractMetadata m1 = new CBORCapability(this.publisherId, this.ifMapTimestamp, this.capabilityName1);
		AbstractMetadata m2 = new CBORCapability(this.publisherId, this.ifMapTimestamp, this.capabilityName2);
		AbstractMetadata m3 = new CBORDeviceAttribute(this.publisherId, this.ifMapTimestamp, this.devAttrName);

		this.metadata.add(m1);
		this.metadata.add(m2);
		this.metadata.add(m3);
	}


	@Test
	public void testCborSerialize_SingleIdentifier() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">publish");

		CBORPublishUpdate instance = new CBORPublishUpdate(this.identifierA, null);
		instance.addMetadata(this.metadata.get(0));
		instance.addMetadata(this.metadata.get(1));
		instance.addMetadata(this.metadata.get(2));

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (1 identifier):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66675706461746582686C69666574696D656773657373696F6E887831687474703A2F2F7777772E747275"
				+ "73746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F32686964656E7469747984646E616D656B6D792D646E732D6E616D65647479"
				+ "706568646E732D6E616D6580F6686D65746164617461808C7839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F3230"
				+ "31302F49464D41504D455441444154412F326A6361706162696C697479887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D6964"
				+ "6F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D6361"
				+ "7264696E616C6974796A6D756C746956616C756584F6646E616D6580666361702D30317839687474703A2F2F7777772E74727573746564636F6D707574696E6767"
				+ "726F75702E6F72672F323031302F49464D41504D455441444154412F326A6361706162696C697479887269666D61702D7075626C69736865722D69646F6D792D70"
				+ "75626C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075B"
				+ "CA007169666D61702D63617264696E616C6974796A6D756C746956616C756584F6646E616D6580666361702D30327839687474703A2F2F7777772E747275737465"
				+ "64636F6D707574696E6767726F75702E6F72672F323031302F49464D41504D455441444154412F32706465766963652D617474726962757465887269666D61702D"
				+ "7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D65737461"
				+ "6D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E616C6974796A6D756C746956616C756584F6646E616D65806C6D792D6174747269"
				+ "62757465");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_TwoIdentifiers() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">publish");

		CBORPublishUpdate instance = new CBORPublishUpdate(this.identifierA, this.identifierB);
		instance.addMetadata(this.metadata.get(0));
		instance.addMetadata(this.metadata.get(1));
		instance.addMetadata(this.metadata.get(2));

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (2 identifiers):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66675706461746582686C69666574696D656773657373696F6E8C7831687474703A2F2F7777772E747275"
				+ "73746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F32686964656E7469747984646E616D656B6D792D646E732D6E616D65647479"
				+ "706568646E732D6E616D65807831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F326664"
				+ "65766963658084F6646E616D6580696465766963652D3031F6686D65746164617461808C7839687474703A2F2F7777772E74727573746564636F6D707574696E67"
				+ "67726F75702E6F72672F323031302F49464D41504D455441444154412F326A6361706162696C697479887269666D61702D7075626C69736865722D69646F6D792D"
				+ "7075626C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A07"
				+ "5BCA007169666D61702D63617264696E616C6974796A6D756C746956616C756584F6646E616D6580666361702D30317839687474703A2F2F7777772E7472757374"
				+ "6564636F6D707574696E6767726F75702E6F72672F323031302F49464D41504D455441444154412F326A6361706162696C697479887269666D61702D7075626C69"
				+ "736865722D69646F6D792D7075626C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672"
				+ "616374696F6EC482281A075BCA007169666D61702D63617264696E616C6974796A6D756C746956616C756584F6646E616D6580666361702D30327839687474703A"
				+ "2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41504D455441444154412F32706465766963652D6174747269"
				+ "62757465887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869"
				+ "666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E616C6974796A6D756C746956616C756584F6646E61"
				+ "6D65806C6D792D617474726962757465");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
