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
import de.decoit.simu.cbor.ifmap.enums.IfMapCardinality;
import de.decoit.simu.cbor.ifmap.enums.IfMapEnforcementAction;
import de.decoit.simu.cbor.ifmap.enums.IfMapEventType;
import de.decoit.simu.cbor.ifmap.enums.IfMapSignificance;
import de.decoit.simu.cbor.ifmap.enums.IfMapWlanSecurityType;
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
import de.decoit.simu.cbor.ifmap.util.TimestampHelper;
import java.io.ByteArrayInputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
public class MetadataDeserializerManagerNoDictTest {
	private final ZonedDateTime expIfMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	private final String expIfMapPublisherId = "my-publisher-id";


	@Test
	public void testDeserialize_CBORAccessRequestDevice() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32756163636573732D726571756573742D646576696365887269666D61702D7075626C69736865722D69646F6D792D707562"
				+ "6C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA00"
				+ "7169666D61702D63617264696E616C6974796B73696E676C6556616C756580");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32716163636573732D726571756573742D6970887269666D61702D7075626C69736865722D69646F6D792D7075626C697368"
				+ "65722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D"
				+ "61702D63617264696E616C6974796B73696E676C6556616C756580");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32726163636573732D726571756573742D6D6163887269666D61702D7075626C69736865722D69646F6D792D7075626C6973"
				+ "6865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA00716966"
				+ "6D61702D63617264696E616C6974796B73696E676C6556616C756580");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F327061757468656E746963617465642D6173887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865"
				+ "722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61"
				+ "702D63617264696E616C6974796B73696E676C6556616C756580");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F327061757468656E746963617465642D6279887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865"
				+ "722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61"
				+ "702D63617264696E616C6974796B73696E676C6556616C756580");

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
		byte[] input = DatatypeConverter.parseHexBinary("847845687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303132"
				+ "2F49464D41504F5045524154494F4E414C2D4D455441444154412F316B636C69656E742D74696D658A7269666D61702D7075626C69736865722D69646F6D792D70"
				+ "75626C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075B"
				+ "CA007169666D61702D63617264696E616C6974796B73696E676C6556616C75656C63757272656E742D74696D65C11A4ED9E8B380");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32696465766963652D6970887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F6966"
				+ "6D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D6361726469"
				+ "6E616C6974796B73696E676C6556616C756580");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F326D646973636F76657265642D6279887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69"
				+ "646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63"
				+ "617264696E616C6974796B73696E676C6556616C756580");

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
		byte[] input = DatatypeConverter.parseHexBinary("847838687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303133"
				+ "2F49464D41502D5345525645522F31717365727665722D6361706162696C697479887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865"
				+ "722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61"
				+ "702D63617264696E616C6974796B73696E676C6556616C75658CF66A6361706162696C697479807669666D61702D626173652D76657273696F6E2D322E30F66A63"
				+ "61706162696C697479807669666D61702D626173652D76657273696F6E2D322E32F66A6361706162696C697479807669666D61702D626173652D76657273696F6E"
				+ "2D322E31");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F3270776C616E2D696E666F726D6174696F6E887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865"
				+ "722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61"
				+ "702D63617264696E616C6974796B73696E676C6556616C75659818F664737369648069776C616E2D73736964F675737369642D756E69636173742D736563757269"
				+ "74798063626970F675737369642D756E69636173742D736563757269747982756F746865722D747970652D646566696E6974696F6E756F746865722D747970652D"
				+ "646566696E6974696F6E656F74686572F673737369642D67726F75702D736563757269747982756F746865722D747970652D646566696E6974696F6E756F746865"
				+ "722D747970652D646566696E6974696F6E656F74686572F67818737369642D6D616E6167656D656E742D73656375726974798063626970F67818737369642D6D61"
				+ "6E6167656D656E742D736563757269747982756F746865722D747970652D646566696E6974696F6E756F746865722D747970652D646566696E6974696F6E656F74"
				+ "686572");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F326A6361706162696C697479887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69"
				+ "666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264"
				+ "696E616C6974796A6D756C746956616C756588F6646E616D65806F6361706162696C6974793A6E616D65F67561646D696E6973747261746976652D646F6D61696E"
				+ "8078206361706162696C6974793A61646D696E6973747261746976652D646F6D61696E");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32706465766963652D617474726962757465887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865"
				+ "722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61"
				+ "702D63617264696E616C6974796A6D756C746956616C756584F6646E616D6580756465766963652D6174747269627574653A6E616D65");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32756465766963652D6368617261637465726973746963887269666D61702D7075626C69736865722D69646F6D792D707562"
				+ "6C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA00"
				+ "7169666D61702D63617264696E616C6974796A6D756C746956616C75659820F66F646973636F76657265642D74696D6580C11A4ED9E777F66D646973636F766572"
				+ "65722D69648077646973636F76657265722D7075626C69736865722D6964F670646973636F766572792D6D6574686F648078266465766963652D63686172616374"
				+ "657269737469633A646973636F766572792D6D6574686F64F66C6D616E75666163747572657280716D616E7566616374757265722D30383135F6656D6F64656C80"
				+ "6C6D6F64656C2D303831352D32F6626F7380756F7065726174696E672D73797374656D2D30383135F66A6F732D76657273696F6E8063763432F66B646576696365"
				+ "2D74797065806B6465766963652D30383135");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F3272656E666F7263656D656E742D7265706F7274887269666D61702D7075626C69736865722D69646F6D792D7075626C6973"
				+ "6865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA00716966"
				+ "6D61702D63617264696E616C6974796A6D756C746956616C75658CF672656E666F7263656D656E742D616374696F6E80656F74686572F6756F746865722D747970"
				+ "652D646566696E6974696F6E80756F746865722D747970652D646566696E6974696F6EF672656E666F7263656D656E742D726561736F6E806C6A7573742D666F72"
				+ "2D66756E");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32656576656E74887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D61702D"
				+ "74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E616C69"
				+ "74796A6D756C746956616C75659828F6646E616D65806A6576656E743A6E616D65F66F646973636F76657265642D74696D6580C11A4ED9E777F66D646973636F76"
				+ "657265722D69648077646973636F76657265722D7075626C69736865722D6964F6696D61676E69747564658005F66A636F6E666964656E636580182AF66C736967"
				+ "6E69666963616E6365806D696E666F726D6174696F6E616CF6647479706580656F74686572F6756F746865722D747970652D646566696E6974696F6E80756F7468"
				+ "65722D747970652D646566696E6974696F6EF66B696E666F726D6174696F6E80716576656E742D696E666F726D6174696F6EF67176756C6E65726162696C697479"
				+ "2D75726980776576656E743A76756C6E65726162696C6974792D757269");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F326669702D6D6163887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D6170"
				+ "2D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E616C"
				+ "6974796A6D756C746956616C75658CF66A73746172742D74696D6580C11A4ED9E510F668656E642D74696D6580C11A4EDA0838F66B646863702D73657276657280"
				+ "69646863702D30383135");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32726C61796572322D696E666F726D6174696F6E887269666D61702D7075626C69736865722D69646F6D792D7075626C6973"
				+ "6865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA00716966"
				+ "6D61702D63617264696E616C6974796A6D756C746956616C756590F664766C616E80182AF669766C616E2D6E616D658067766C616E2D3432F664706F7274801910"
				+ "92F67561646D696E6973747261746976652D646F6D61696E8078286C61796572322D696E666F726D6174696F6E3A61646D696E6973747261746976652D646F6D61"
				+ "696E");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F32686C6F636174696F6E887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D"
				+ "61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E"
				+ "616C6974796A6D756C746956616C756594F66F646973636F76657265642D74696D6580C11A4ED9E777F66D646973636F76657265722D69648077646973636F7665"
				+ "7265722D7075626C69736865722D6964F6746C6F636174696F6E2D696E666F726D6174696F6E84647479706566747970652D316576616C75656776616C75652D31"
				+ "80F6746C6F636174696F6E2D696E666F726D6174696F6E84647479706566747970652D336576616C75656776616C75652D3380F6746C6F636174696F6E2D696E66"
				+ "6F726D6174696F6E84647479706566747970652D326576616C75656776616C75652D3280");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F327819726571756573742D666F722D696E7665737469676174696F6E887269666D61702D7075626C69736865722D69646F6D"
				+ "792D7075626C69736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC48228"
				+ "1A075BCA007169666D61702D63617264696E616C6974796A6D756C746956616C756584F6697175616C6966696572806D7266692D7175616C6966696572");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F3264726F6C65887269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D69646F69666D61702D74"
				+ "696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169666D61702D63617264696E616C6974"
				+ "796A6D756C746956616C756588F6646E616D6580676D792D726F6C65F67561646D696E6973747261746976652D646F6D61696E80781A726F6C653A61646D696E69"
				+ "73747261746976652D646F6D61696E");

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
		byte[] input = DatatypeConverter.parseHexBinary("847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41504D455441444154412F3273756E65787065637465642D6265686176696F72887269666D61702D7075626C69736865722D69646F6D792D7075626C69"
				+ "736865722D69646F69666D61702D74696D657374616D70C11A4ED9E8B2781869666D61702D74696D657374616D702D6672616374696F6EC482281A075BCA007169"
				+ "666D61702D63617264696E616C6974796A6D756C746956616C7565981CF66F646973636F76657265642D74696D6580C11A4ED9E777F66D646973636F7665726572"
				+ "2D69648077646973636F76657265722D7075626C69736865722D6964F6696D61676E69747564658005F66C7369676E69666963616E63658068637269746963616C"
				+ "F66A636F6E666964656E636580182AF66474797065806D6265686176696F722D74797065F66B696E666F726D6174696F6E80746265686176696F722D696E666F72"
				+ "6D6174696F6E");

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
}
