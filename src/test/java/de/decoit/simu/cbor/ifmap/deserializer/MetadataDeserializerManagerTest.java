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
package de.decoit.simu.cbor.ifmap.deserializer;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.deserializer.vendor.VendorMetadataDeserializer;
import de.decoit.simu.cbor.ifmap.enums.IfMapCardinality;
import de.decoit.simu.cbor.ifmap.enums.IfMapEnforcementAction;
import de.decoit.simu.cbor.ifmap.enums.IfMapEventType;
import de.decoit.simu.cbor.ifmap.enums.IfMapSignificance;
import de.decoit.simu.cbor.ifmap.enums.IfMapWlanSecurityType;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.AbstractMultiValueMetadata;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORCapability;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORDeviceAttribute;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORDeviceCharacteristic;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBOREnforcementReport;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBOREvent;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORIpMac;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORLayer2Information;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORLocation;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORRequestForInvestigation;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORRole;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORUnexpectedBehavior;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.AbstractSingleValueMetadata;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORAccessRequestDevice;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORAccessRequestIp;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORAccessRequestMac;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORAuthenticatedAs;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORAuthenticatedBy;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORClientTime;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORDeviceIp;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORDiscoveredBy;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORServerCapability;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORWlanInformation;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.ifmap.util.TimestampHelper;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.io.ByteArrayInputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class MetadataDeserializerManagerTest extends AbstractTestBase {
	private final ZonedDateTime expIfMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String expIfMapPublisherId = "my-publisher-id";


	@After
	public void tearDown() {
		MetadataDeserializerManager.clearAllVendorDeserializers();
	}


	@Test
	public void testDeserialize_CBORAccessRequestDevice() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010088006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030080");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORAccessRequestDevice result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORAccessRequestDevice.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.SINGLE_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
	}


	@Test
	public void testDeserialize_CBORAccessRequestIp() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010188006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030080");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORAccessRequestIp result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORAccessRequestIp.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.SINGLE_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
	}


	@Test
	public void testDeserialize_CBORAccessRequestMac() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010288006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030080");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORAccessRequestMac result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORAccessRequestMac.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.SINGLE_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
	}


	@Test
	public void testDeserialize_CBORAuthenticatedAs() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010388006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030080");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORAuthenticatedAs result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORAuthenticatedAs.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.SINGLE_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
	}


	@Test
	public void testDeserialize_CBORAuthenticatedBy() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010488006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030080");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORAuthenticatedBy result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORAuthenticatedBy.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.SINGLE_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
	}


	@Test
	public void testDeserialize_CBORClientTime() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("8402008A006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030004C11A4ED9"
				+ "E8B380");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		ZonedDateTime expCurrentTime = ZonedDateTime.parse("2011-12-03T10:15:31.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);

		CBORClientTime result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORClientTime.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.SINGLE_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(TimestampHelper.toUTC(expCurrentTime.truncatedTo(ChronoUnit.SECONDS)), result.getCurrentTime());
	}


	@Test
	public void testDeserialize_CBORDeviceIp() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010888006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030080");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORDeviceIp result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORDeviceIp.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.SINGLE_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
	}


	@Test
	public void testDeserialize_CBORDiscoveredBy() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010988006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030080");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORDiscoveredBy result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORDiscoveredBy.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.SINGLE_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
	}


	@Test
	public void testDeserialize_CBORServerCapability() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84030188006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003008CF6008076"
				+ "69666D61702D626173652D76657273696F6E2D322E30F600807669666D61702D626173652D76657273696F6E2D322E32F600807669666D61702D626173652D7665"
				+ "7273696F6E2D322E31");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORServerCapability result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORServerCapability.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.SINGLE_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(3, result.getCapabilities().size());
		assertTrue(result.hasCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_22));
		assertTrue(result.hasCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_21));
		assertTrue(result.hasCapability(CBORServerCapability.TcgDefinedCapabilities.IFMAP_BASE_VERSION_20));
	}


	@Test
	public void testDeserialize_CBORWlanInformation() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84011288006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003009818F60080"
				+ "69776C616E2D73736964F6018004F6018200756F746865722D747970652D646566696E6974696F6E05F6028200756F746865722D747970652D646566696E697469"
				+ "6F6E05F6038004F6038200756F746865722D747970652D646566696E6974696F6E05");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		IfMapWlanSecurityType expGroupSecurityType = IfMapWlanSecurityType.OTHER;
		String expGroupSecurityOtd = "other-type-definition";

		CBORWlanInformation result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORWlanInformation.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.SINGLE_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(expGroupSecurityType, result.getSsidGroupSecurity().getWlanSecurityType());
		assertEquals(expGroupSecurityOtd, result.getSsidGroupSecurity().getOtherTypeDefinition());
		assertTrue(result.getSsidManagementSecurity().size() == 2);
		assertTrue(result.hasSsidManagementSecurity(IfMapWlanSecurityType.BIP, null));
		assertTrue(result.hasSsidManagementSecurity(IfMapWlanSecurityType.OTHER, expGroupSecurityOtd));
		assertTrue(result.getSsidUnicastSecurity().size() == 2);
		assertTrue(result.hasSsidUnicastSecurity(IfMapWlanSecurityType.BIP, null));
		assertTrue(result.hasSsidUnicastSecurity(IfMapWlanSecurityType.OTHER, expGroupSecurityOtd));
	}


	@Test
	public void testDeserialize_CBORCapability() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010588006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030188F600806F"
				+ "6361706162696C6974793A6E616D65F6018078206361706162696C6974793A61646D696E6973747261746976652D646F6D61696E");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		String expName = "capability:name";
		String expAdmDomain = "capability:administrative-domain";

		CBORCapability result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORCapability.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.MULTI_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(expName, result.getName());
		assertEquals(expAdmDomain, result.getAdministrativeDomain());
	}


	@Test
	public void testDeserialize_CBORDeviceAttribute() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010688006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030184F6008075"
				+ "6465766963652D6174747269627574653A6E616D65");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		String expName = "device-attribute:name";

		CBORDeviceAttribute result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORDeviceAttribute.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.MULTI_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(expName, result.getName());
	}


	@Test
	public void testDeserialize_CBORDeviceCharacteristic() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010788006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003019820F60580"
				+ "C11A4ED9E777F6068077646973636F76657265722D7075626C69736865722D6964F6078078266465766963652D63686172616374657269737469633A646973636F"
				+ "766572792D6D6574686F64F60080716D616E7566616374757265722D30383135F601806C6D6F64656C2D303831352D32F60280756F7065726174696E672D737973"
				+ "74656D2D30383135F6038063763432F604806B6465766963652D30383135");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		ZonedDateTime expDiscoveredTime = ZonedDateTime.parse("2011-12-03T10:10:15.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		String expDiscovererId = "discoverer-publisher-id";
		String expDiscoveryMethod = "device-characteristic:discovery-method";
		String expManufacturer = "manufacturer-0815";
		String expModel = "model-0815-2";
		String expOs = "operating-system-0815";
		String expOsVersion = "v42";
		String expDeviceType = "device-0815";

		CBORDeviceCharacteristic result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORDeviceCharacteristic.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.MULTI_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(TimestampHelper.toUTC(expDiscoveredTime.truncatedTo(ChronoUnit.SECONDS)), result.getDiscoveredTime());
		assertEquals(expDiscovererId, result.getDiscovererId());
		assertEquals(expDiscoveryMethod, result.getDiscoveryMethod());
		assertEquals(expManufacturer, result.getManufacturer());
		assertEquals(expModel, result.getModel());
		assertEquals(expOs, result.getOs());
		assertEquals(expOsVersion, result.getOsVersion());
		assertEquals(expDeviceType, result.getDeviceType());
	}


	@Test
	public void testDeserialize_CBOREnforcementReport() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010A88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003018CF6008002"
				+ "F60180756F746865722D747970652D646566696E6974696F6EF602806C6A7573742D666F722D66756E");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		String expOtherTypeDefinition = "other-type-definition";
		String expEnforcementReason = "just-for-fun";
		IfMapEnforcementAction expEnforcementAction = IfMapEnforcementAction.OTHER;

		CBOREnforcementReport result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBOREnforcementReport.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.MULTI_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(expEnforcementAction, result.getEnforcementAction());
		assertEquals(expEnforcementReason, result.getEnforcementReason());
		assertEquals(expOtherTypeDefinition, result.getOtherTypeDefinition());
	}


	@Test
	public void testDeserialize_CBOREvent() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010B88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003019828F60080"
				+ "6A6576656E743A6E616D65F60180C11A4ED9E777F6028077646973636F76657265722D7075626C69736865722D6964F6038005F60480182AF6058002F6068007F6"
				+ "0780756F746865722D747970652D646566696E6974696F6EF60880716576656E742D696E666F726D6174696F6EF60980776576656E743A76756C6E65726162696C"
				+ "6974792D757269");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		ZonedDateTime expDiscoveredTime = ZonedDateTime.parse("2011-12-03T10:10:15.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		String expDiscovererId = "discoverer-publisher-id";
		String expName = "event:name";
		Integer expMagnitude = 5;
		IfMapSignificance expSignificance = IfMapSignificance.INFORMATIONAL;
		String expInformation = "event-information";
		Integer expConfidence = 42;
		String expVulnerabilityUri = "event:vulnerability-uri";
		String expOtherTypeDefinition = "other-type-definition";
		IfMapEventType expType = IfMapEventType.OTHER;

		CBOREvent result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBOREvent.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.MULTI_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(TimestampHelper.toUTC(expDiscoveredTime.truncatedTo(ChronoUnit.SECONDS)), result.getDiscoveredTime());
		assertEquals(expDiscovererId, result.getDiscovererId());
		assertEquals(expName, result.getName());
		assertEquals(expMagnitude, result.getMagnitude());
		assertEquals(expConfidence, result.getConfidence());
		assertEquals(expSignificance, result.getSignificance());
		assertEquals(expType, result.getType());
		assertEquals(expOtherTypeDefinition, result.getOtherTypeDefinition());
		assertEquals(expInformation, result.getInformation());
		assertEquals(expVulnerabilityUri, result.getVulnerabilityUri());
	}


	@Test
	public void testDeserialize_CBORIpMac() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010C88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA0003018CF60080C1"
				+ "1A4ED9E510F60180C11A4EDA0838F6028069646863702D30383135");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		ZonedDateTime expStartTime = ZonedDateTime.parse("2011-12-03T10:00:00.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		ZonedDateTime expEndTime = ZonedDateTime.parse("2011-12-03T12:30:00.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		String expDhcpServer = "dhcp-0815";

		CBORIpMac result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORIpMac.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.MULTI_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(TimestampHelper.toUTC(expStartTime.truncatedTo(ChronoUnit.SECONDS)), result.getStartTime());
		assertEquals(TimestampHelper.toUTC(expEndTime.truncatedTo(ChronoUnit.SECONDS)), result.getEndTime());
		assertEquals(expDhcpServer, result.getDhcpServer());
	}


	@Test
	public void testDeserialize_CBORLayer2Information() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010D88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030190F6008018"
				+ "2AF6018067766C616E2D3432F60280191092F6038078286C61796572322D696E666F726D6174696F6E3A61646D696E6973747261746976652D646F6D61696E");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		Integer expVlan = 42;
		String expVlanName = "vlan-42";
		Integer expPort = 4242;
		String expAdminstrativeDomain = "layer2-information:administrative-domain";

		CBORLayer2Information result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORLayer2Information.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.MULTI_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(expVlan, result.getVlan());
		assertEquals(expVlanName, result.getVlanName());
		assertEquals(expPort, result.getPort());
		assertEquals(expAdminstrativeDomain, result.getAdministrativeDomain());
	}


	@Test
	public void testDeserialize_CBORLocation() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010E88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030194F60280C1"
				+ "1A4ED9E777F6038077646973636F76657265722D7075626C69736865722D6964F600840066747970652D31016776616C75652D3180F600840066747970652D3301"
				+ "6776616C75652D3380F600840066747970652D32016776616C75652D3280");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		ZonedDateTime expDiscoveredTime = ZonedDateTime.parse("2011-12-03T10:10:15.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		String expDiscovererId = "discoverer-publisher-id";
		String[] locInfoTypes = new String[]{"type-1", "type-2", "type-3"};
		String[] locInfoValues = new String[]{"value-1", "value-2", "value-3"};

		CBORLocation result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORLocation.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.MULTI_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(TimestampHelper.toUTC(expDiscoveredTime.truncatedTo(ChronoUnit.SECONDS)), result.getDiscoveredTime());
		assertEquals(expDiscovererId, result.getDiscovererId());
		assertEquals(locInfoTypes.length, result.getLocationInformation().size());

		for(CBORLocation.LocationInformation locInfo : result.getLocationInformation()) {
			assertTrue(Arrays.binarySearch(locInfoTypes, locInfo.getType()) >= 0);
			assertTrue(Arrays.binarySearch(locInfoValues, locInfo.getValue()) >= 0);
		}
	}


	@Test
	public void testDeserialize_CBORRequestForInvestigation() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84010F88006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030184F600806D"
				+ "7266692D7175616C6966696572");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		String expQualifier = "rfi-qualifier";

		CBORRequestForInvestigation result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORRequestForInvestigation.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.MULTI_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(expQualifier, result.getQualifier());
	}


	@Test
	public void testDeserialize_CBORRole() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84011088006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA00030188F6008067"
				+ "6D792D726F6C65F60180781A726F6C653A61646D696E6973747261746976652D646F6D61696E");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		String expName = "my-role";
		String expAdminstrativeDomain = "role:administrative-domain";

		CBORRole result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORRole.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.MULTI_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(expName, result.getName());
		assertEquals(expAdminstrativeDomain, result.getAdministrativeDomain());
	}


	@Test
	public void testDeserialize_CBORUnexpectedBehavior() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84011188006F6D792D7075626C69736865722D696401C11A4ED9E8B202C482281A075BCA000301981CF60080"
				+ "C11A4ED9E777F6018077646973636F76657265722D7075626C69736865722D6964F6038005F6058000F60480182AF606806D6265686176696F722D74797065F602"
				+ "80746265686176696F722D696E666F726D6174696F6E");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		ZonedDateTime expDiscoveredTime = ZonedDateTime.parse("2011-12-03T10:10:15.654321+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		String expDiscovererId = "discoverer-publisher-id";
		Integer expMagnitude = 5;
		IfMapSignificance expSignificance = IfMapSignificance.CRITICAL;
		Integer expConfidence = 42;
		String expType = "behavior-type";

		CBORUnexpectedBehavior result = MetadataDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORUnexpectedBehavior.class);
		assertEquals(this.expIfMapPublisherId, result.getIfMapPublisherId());
		assertEquals(IfMapCardinality.MULTI_VALUE, result.getIfMapCardinality());
		assertEquals(TimestampHelper.toUTC(this.expIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(TimestampHelper.toUTC(expDiscoveredTime.truncatedTo(ChronoUnit.SECONDS)), result.getDiscoveredTime());
		assertEquals(expDiscovererId, result.getDiscovererId());
		assertEquals(expType, result.getType());
		assertEquals(expMagnitude, result.getMagnitude());
		assertEquals(expConfidence, result.getConfidence());
		assertEquals(expSignificance, result.getSignificance());
	}


	@Test
	public void testRegisterVendorDeserializer() {
		MetadataDeserializerManager.registerVendorDeserializer(new VendorDeserializerDMA(), DummyMetadataA.class, IfMapNamespaces.IFMAP_METADATA, "dummy-metadata-A");
		assertTrue(MetadataDeserializerManager.hasVendorDeserializer(DummyMetadataA.class));
	}


	@Test
	public void testRegisterVendorDeserializer_TwoDifferent() {
		MetadataDeserializerManager.registerVendorDeserializer(new VendorDeserializerDMA(), DummyMetadataA.class, IfMapNamespaces.IFMAP_METADATA, "dummy-metadata-A");
		MetadataDeserializerManager.registerVendorDeserializer(new VendorDeserializerDMB(), DummyMetadataB.class, IfMapNamespaces.IFMAP_METADATA, "dummy-metadata-B");
		assertTrue(MetadataDeserializerManager.hasVendorDeserializer(DummyMetadataA.class));
		assertTrue(MetadataDeserializerManager.hasVendorDeserializer(DummyMetadataB.class));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_null_null_null_null() {
		MetadataDeserializerManager.registerVendorDeserializer(null, null, null, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_VendorDeserializer_null_String_String() {
		MetadataDeserializerManager.registerVendorDeserializer(new VendorDeserializerDMA(), null, IfMapNamespaces.IFMAP_METADATA, "dummy-metadata-A");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_null_Class_String_String() {
		MetadataDeserializerManager.registerVendorDeserializer(null, DummyMetadataA.class, IfMapNamespaces.IFMAP_METADATA, "dummy-metadata-A");
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_VendorDeserializer_Class_null_String() {
		MetadataDeserializerManager.registerVendorDeserializer(new VendorDeserializerDMA(), DummyMetadataA.class, null, "dummy-metadata-A");
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_VendorDeserializer_Class_EmptyString_String() {
		MetadataDeserializerManager.registerVendorDeserializer(new VendorDeserializerDMA(), DummyMetadataA.class, "", "dummy-metadata-A");
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_VendorDeserializer_Class_Whitespaces_String() {
		MetadataDeserializerManager.registerVendorDeserializer(new VendorDeserializerDMA(), DummyMetadataA.class, "   ", "dummy-metadata-A");
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_VendorDeserializer_Class_String_null() {
		MetadataDeserializerManager.registerVendorDeserializer(new VendorDeserializerDMA(), DummyMetadataA.class, IfMapNamespaces.IFMAP_METADATA, null);
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_VendorDeserializer_Class_String_EmptyString() {
		MetadataDeserializerManager.registerVendorDeserializer(new VendorDeserializerDMA(), DummyMetadataA.class, IfMapNamespaces.IFMAP_METADATA, "");
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_VendorDeserializer_Class_String_Whitespaces() {
		MetadataDeserializerManager.registerVendorDeserializer(new VendorDeserializerDMA(), DummyMetadataA.class, IfMapNamespaces.IFMAP_METADATA, "   ");
	}


	@Test(expected = IllegalStateException.class)
	public void testRegisterVendorDeserializer_SameTwice() {
		MetadataDeserializerManager.registerVendorDeserializer(new VendorDeserializerDMA(), DummyMetadataA.class, IfMapNamespaces.IFMAP_METADATA, "dummy-metadata-A");
		MetadataDeserializerManager.registerVendorDeserializer(new VendorDeserializerDMA(), DummyMetadataA.class, IfMapNamespaces.IFMAP_METADATA, "dummy-metadata-A");
	}


	@Test
	public void testUnregisterVendorDeserializer() {
		MetadataDeserializerManager.registerVendorDeserializer(new VendorDeserializerDMA(), DummyMetadataA.class, IfMapNamespaces.IFMAP_METADATA, "dummy-metadata-A");
		MetadataDeserializerManager.registerVendorDeserializer(new VendorDeserializerDMB(), DummyMetadataB.class, IfMapNamespaces.IFMAP_METADATA, "dummy-metadata-B");

		MetadataDeserializerManager.unregisterVendorDeserializer(DummyMetadataA.class);
		assertFalse(MetadataDeserializerManager.hasVendorDeserializer(DummyMetadataA.class));
		assertTrue(MetadataDeserializerManager.hasVendorDeserializer(DummyMetadataB.class));
	}


	private static class VendorDeserializerDMA implements VendorMetadataDeserializer<DummyMetadataA> {
		@Override
		public DummyMetadataA deserialize(Array attributes, DataItem nestedTags, DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}


	private static class VendorDeserializerDMB implements VendorMetadataDeserializer<DummyMetadataB> {
		@Override
		public DummyMetadataB deserialize(Array attributes, DataItem nestedTags, DictionarySimpleElement elementDictEntry) throws CBORDeserializationException {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}
	
	
	private static class DummyMetadataA extends AbstractSingleValueMetadata {
		public DummyMetadataA() {
			super(IfMapNamespaces.IFMAP_METADATA, "dummy-metadata-A");
		}
	}
	
	
	private static class DummyMetadataB extends AbstractMultiValueMetadata {
		public DummyMetadataB() {
			super(IfMapNamespaces.IFMAP_METADATA, "dummy-metadata-B");
		}
	}
}
