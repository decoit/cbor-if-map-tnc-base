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
package de.decoit.simu.cbor.ifmap.deserializer.response;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import de.decoit.simu.cbor.ifmap.enums.IfMapCardinality;
import de.decoit.simu.cbor.ifmap.enums.IfMapIdentityType;
import de.decoit.simu.cbor.ifmap.identifier.CBORDevice;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORCapability;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORDeviceAttribute;
import de.decoit.simu.cbor.ifmap.response.model.CBORSearchResult;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.ifmap.util.TimestampHelper;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.io.ByteArrayInputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
public class SearchResultDeserializerNoDictTest {
	@Test
	public void testDeserialize() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84F66C736561726368526573756C7482646E616D656773722D6E616D6588F6"
				+ "6A726573756C744974656D80887831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72"
				+ "672F323031302F49464D41502F32686964656E7469747984646E616D656B6D792D646E732D6E616D65647479706568646E73"
				+ "2D6E616D6580F6686D6574616461746180847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F"
				+ "75702E6F72672F323031302F49464D41504D455441444154412F32706465766963652D617474726962757465887269666D61"
				+ "702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9"
				+ "E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E61"
				+ "6C6974796A6D756C746956616C756584F6646E616D65806C6D792D617474726962757465F66A726573756C744974656D808C"
				+ "7831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F"
				+ "32666465766963658084F6646E616D6580696465766963652D30317831687474703A2F2F7777772E74727573746564636F6D"
				+ "707574696E6767726F75702E6F72672F323031302F49464D41502F32686964656E7469747984646E616D656B6D792D646E73"
				+ "2D6E616D65647479706568646E732D6E616D6580F6686D6574616461746180887839687474703A2F2F7777772E7472757374"
				+ "6564636F6D707574696E6767726F75702E6F72672F323031302F49464D41504D455441444154412F326A6361706162696C69"
				+ "7479887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D61702D74696D6573"
				+ "74616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D6170"
				+ "2D63617264696E616C6974796A6D756C746956616C756584F6646E616D6580666361702D30317839687474703A2F2F777777"
				+ "2E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41504D455441444154412F326A6361"
				+ "706162696C697479887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D6170"
				+ "2D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA00"
				+ "7169666D61702D63617264696E616C6974796A6D756C746956616C756584F6646E616D6580666361702D3032");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);
		
		String dictPath = "<" + IfMapNamespaces.IFMAP + ">response+searchResult";
		DictionarySimpleElement dse = DictionaryProvider.getInstance().findElementByPath(dictPath);
		
		CBORSearchResult result = SearchResultDeserializer
										.getInstance()
										.deserialize((Array)topLevelArray.getDataItems().get(2), 
													 (Array)topLevelArray.getDataItems().get(3),
													 dse);
		
		assertNotNull(result);
		
		assertEquals("sr-name", result.getResult().getName());
		assertEquals(2, result.getResult().getResultItems().size());
		
		result.getResult().getResultItems().stream().forEach((ri) -> {
			if(ri.getIdentifierB() == null) {
				assertTrue(ri.getIdentifierA() instanceof CBORIdentity);
				
				CBORIdentity castIdentifier = (CBORIdentity) ri.getIdentifierA();
				
				assertEquals("my-dns-name", castIdentifier.getName());
				assertEquals(IfMapIdentityType.DNS_NAME, castIdentifier.getType());
				
				assertEquals(1, ri.getMetadata().size());
				assertTrue(ri.getMetadata().get(0) instanceof CBORDeviceAttribute);
				
				CBORDeviceAttribute castMetadata = (CBORDeviceAttribute) ri.getMetadata().get(0);
				
				ZonedDateTime expTimestamp = TimestampHelper.toUTC(ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME));
				
				assertEquals("my-publisher-id", castMetadata.getIfMapPublisherId());
				assertEquals(expTimestamp, castMetadata.getIfMapTimestamp());
				assertEquals(IfMapCardinality.MULTI_VALUE, castMetadata.getIfMapCardinality());
				assertEquals("my-attribute", castMetadata.getName());
			}
			else {
				CBORIdentity identity;
				CBORDevice device;
				
				if(ri.getIdentifierA() instanceof CBORIdentity) {
					assertTrue(ri.getIdentifierA() instanceof CBORIdentity);
					assertTrue(ri.getIdentifierB() instanceof CBORDevice);
					
					identity = (CBORIdentity) ri.getIdentifierA();
					device = (CBORDevice) ri.getIdentifierB();
				}
				else {
					assertTrue(ri.getIdentifierA() instanceof CBORDevice);
					assertTrue(ri.getIdentifierB() instanceof CBORIdentity);
					
					device = (CBORDevice) ri.getIdentifierA();
					identity = (CBORIdentity) ri.getIdentifierB();
				}
				
				assertEquals("my-dns-name", identity.getName());
				assertEquals(IfMapIdentityType.DNS_NAME, identity.getType());
				
				assertEquals("device-01", device.getName());
				assertFalse(device.isAikName());
				
				assertEquals(2, ri.getMetadata().size());
				
				ri.getMetadata().stream().forEach((m) -> {
					assertTrue(m instanceof CBORCapability);
					
					CBORCapability castMetadata = (CBORCapability) m;
					
					ZonedDateTime expTimestamp = TimestampHelper.toUTC(ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME));
					
					assertEquals("my-publisher-id", castMetadata.getIfMapPublisherId());
					assertEquals(expTimestamp, castMetadata.getIfMapTimestamp());
					assertEquals(IfMapCardinality.MULTI_VALUE, castMetadata.getIfMapCardinality());
					
					if(!"cap-02".equals(castMetadata.getName()) && !"cap-01".equals(castMetadata.getName())) {
						fail();
					}
				});
			}
		});
	}
}
