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
import de.decoit.simu.cbor.ifmap.enums.IfMapIdentityType;
import de.decoit.simu.cbor.ifmap.enums.IfMapIpAddressType;
import de.decoit.simu.cbor.ifmap.enums.IfMapLifetime;
import de.decoit.simu.cbor.ifmap.enums.IfMapValidationType;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.ifmap.identifier.CBORDevice;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import de.decoit.simu.cbor.ifmap.identifier.CBORIpAddress;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORDeviceIp;
import de.decoit.simu.cbor.ifmap.request.AbstractRequest;
import de.decoit.simu.cbor.ifmap.request.CBOREndSessionRequest;
import de.decoit.simu.cbor.ifmap.request.CBORNewSessionRequest;
import de.decoit.simu.cbor.ifmap.request.CBORPollRequest;
import de.decoit.simu.cbor.ifmap.request.CBORPublishRequest;
import de.decoit.simu.cbor.ifmap.request.CBORPurgePublisherRequest;
import de.decoit.simu.cbor.ifmap.request.CBORRenewSessionRequest;
import de.decoit.simu.cbor.ifmap.request.CBORSearchRequest;
import de.decoit.simu.cbor.ifmap.request.CBORSubscribeRequest;
import de.decoit.simu.cbor.ifmap.request.model.publish.AbstractUpdateNotify;
import de.decoit.simu.cbor.ifmap.request.model.publish.CBORPublishDelete;
import de.decoit.simu.cbor.ifmap.request.model.search.CBORSubscribeDelete;
import de.decoit.simu.cbor.ifmap.request.model.search.CBORSubscribeUpdate;
import java.io.ByteArrayInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
public class RequestDeserializerManagerNoDictTest {
	@Test
	public void testDeserialize_CBORPublishRequest() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767"
				+ "726F75702E6F72672F323031302F49464D41502F32677075626C697368846A73657373696F6E2D69646D6D792D7365737369"
				+ "6F6E2D69646A76616C69646174696F6E63416C6C8CF66675706461746582686C69666574696D656773657373696F6E8C7831"
				+ "687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F3266"
				+ "6465766963658084F6646E616D65806873657276657230317831687474703A2F2F7777772E74727573746564636F6D707574"
				+ "696E6767726F75702E6F72672F323031302F49464D41502F326A69702D61646472657373846576616C7565D9A411440A0A64"
				+ "116474797065644950763480F6686D6574616461746180847839687474703A2F2F7777772E74727573746564636F6D707574"
				+ "696E6767726F75702E6F72672F323031302F49464D41504D455441444154412F32696465766963652D6970827169666D6170"
				+ "2D63617264696E616C6974796B73696E676C6556616C756580F66664656C65746580887831687474703A2F2F7777772E7472"
				+ "7573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F32666465766963658084F6646E616D"
				+ "65806873657276657230317831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F"
				+ "323031302F49464D41502F326A69702D61646472657373846576616C7565D9A411440A0A64116474797065644950763480F6"
				+ "666E6F7469667982686C69666574696D656773657373696F6E8C7831687474703A2F2F7777772E74727573746564636F6D70"
				+ "7574696E6767726F75702E6F72672F323031302F49464D41502F32666465766963658084F6646E616D658068736572766572"
				+ "30317831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41"
				+ "502F326A69702D61646472657373846576616C7565D9A411440A0A64116474797065644950763480F6686D65746164617461"
				+ "80847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41"
				+ "504D455441444154412F32696465766963652D6970827169666D61702D63617264696E616C6974796B73696E676C6556616C"
				+ "756580");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);
		
		AbstractRequest result = RequestDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), 
																 topLevelArray.getDataItems().get(1), 
																 (Array)topLevelArray.getDataItems().get(2), 
																 (Array)topLevelArray.getDataItems().get(3));
		
		assertTrue(result instanceof CBORPublishRequest);
		CBORPublishRequest castResult = (CBORPublishRequest) result;
		
		assertEquals(IfMapValidationType.ALL, castResult.getValidationAttributes().getValidationType());
		assertEquals(3, castResult.getPublishTypes().size());
		
		castResult.getPublishTypes().stream().forEach((pt) -> {
			// Assert identifier A
			if(pt.getIdentifierA() instanceof CBORDevice) {
				CBORDevice dev = (CBORDevice) pt.getIdentifierA();
				
				assertEquals("server01", dev.getName());
				assertFalse(dev.isAikName());
			}
			else if(pt.getIdentifierA() instanceof CBORIpAddress) {
				CBORIpAddress ipAddr = (CBORIpAddress) pt.getIdentifierA();
				
				try {
					assertEquals(InetAddress.getByName("10.10.100.17"), ipAddr.getValue());
					assertEquals(IfMapIpAddressType.IPV4, ipAddr.getType());
				} 
				catch (UnknownHostException ex) {
					fail();
				}
			}
			else {
				fail("Unknown type of identifier A");
			}

			
			// Assert identifier B
			if(pt.getIdentifierB() instanceof CBORDevice) {
				CBORDevice dev = (CBORDevice) pt.getIdentifierB();
				
				assertEquals("server01", dev.getName());
				assertFalse(dev.isAikName());
			}
			else if(pt.getIdentifierB() instanceof CBORIpAddress) {
				CBORIpAddress ipAddr = (CBORIpAddress) pt.getIdentifierB();
				
				try {
					assertEquals(InetAddress.getByName("10.10.100.17"), ipAddr.getValue());
					assertEquals(IfMapIpAddressType.IPV4, ipAddr.getType());
				} 
				catch (UnknownHostException ex) {
					fail();
				}
			}
			else {
				fail("Unknown type of identifier B");
			}
			
			
			// Assert publish type properties
			if(pt instanceof AbstractUpdateNotify) {
				AbstractUpdateNotify aun = (AbstractUpdateNotify) pt;
				
				assertEquals(IfMapLifetime.SESSION, aun.getLifetime());
		
				assertEquals(1, aun.getMetadata().size());
				aun.getMetadata().stream().forEach((m) -> {
					assertTrue(m instanceof CBORDeviceIp);
				});
			}
			else if(pt instanceof CBORPublishDelete) {
				CBORPublishDelete pd = (CBORPublishDelete) pt;
				
				assertNull(pd.getFilter());
			}
			else {
				fail("Unknown publish type");
			}
		});
	}
	
	
	@Test
	public void testDeserialize_CBORSearchRequest() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767"
				+ "726F75702E6F72672F323031302F49464D41502F32667365617263688E6A73657373696F6E2D69646D6D792D73657373696F"
				+ "6E2D69646B6D617463682D6C696E6B73726D617463682D6C696E6B732D66696C746572696D61782D646570746802686D6178"
				+ "2D73697A651A004C4B406D726573756C742D66696C7465726D726573756C742D66696C74657278187465726D696E616C2D69"
				+ "64656E7469666965722D747970656E6163636573732D726571756573746A76616C69646174696F6E68426173654F6E6C7984"
				+ "7831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F"
				+ "32686964656E7469747988646E616D656B6D792D6964656E746974796474797065656F746865727561646D696E6973747261"
				+ "746976652D646F6D61696E6B6964656E746974792D6164756F746865722D747970652D646566696E6974696F6E666D792D6F"
				+ "746480");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);
		
		AbstractRequest result = RequestDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), 
																 topLevelArray.getDataItems().get(1), 
																 (Array)topLevelArray.getDataItems().get(2), 
																 (Array)topLevelArray.getDataItems().get(3));
		
		assertTrue(result instanceof CBORSearchRequest);
		CBORSearchRequest castResult = (CBORSearchRequest) result;
		
		Integer expMaxDepth = 2;
		Integer expMaxSize = 5000000;
		
		assertEquals(IfMapValidationType.BASE_ONLY, castResult.getValidationAttributes().getValidationType());
		assertEquals("my-session-id", castResult.getSessionAttributes().getSessionId());
		assertEquals("match-links-filter", castResult.getSearchTypeAttributes().getMatchLinks());
		assertEquals("result-filter", castResult.getSearchTypeAttributes().getResultFilter());
		assertEquals("access-request", castResult.getSearchTypeAttributes().getTerminalIdentifierType());
		assertEquals(expMaxDepth, castResult.getSearchTypeAttributes().getMaxDepth());
		assertEquals(expMaxSize, castResult.getSearchTypeAttributes().getMaxSize());
		
		AbstractIdentifier resultIdentifier = castResult.getIdentifier();
		assertTrue(resultIdentifier instanceof CBORIdentity);
		
		CBORIdentity castResultIdentifier = (CBORIdentity) resultIdentifier;
		
		assertEquals("my-identity", castResultIdentifier.getName());
		assertEquals(IfMapIdentityType.OTHER, castResultIdentifier.getType());
		assertEquals("my-otd", castResultIdentifier.getOtherTypeDefinition());
		assertEquals("identity-ad", castResultIdentifier.getAdministrativeDomain());
	}
	
	
	@Test
	public void testDeserialize_CBORSubscribeRequest() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767"
				+ "726F75702E6F72672F323031302F49464D41502F3269737562736372696265846A73657373696F6E2D69646D6D792D736573"
				+ "73696F6E2D69646A76616C69646174696F6E63416C6C88F66664656C65746582646E616D65736D792D6F6C642D7375627363"
				+ "72697074696F6E80F6667570646174658C646E616D656F6D792D737562736372697074696F6E6B6D617463682D6C696E6B73"
				+ "726D617463682D6C696E6B732D66696C746572696D61782D646570746802686D61782D73697A651A004C4B406D726573756C"
				+ "742D66696C7465726D726573756C742D66696C74657278187465726D696E616C2D6964656E7469666965722D747970656E61"
				+ "63636573732D72657175657374847831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F"
				+ "72672F323031302F49464D41502F32686964656E7469747988646E616D656B6D792D6964656E746974796474797065656F74"
				+ "6865727561646D696E6973747261746976652D646F6D61696E6B6964656E746974792D6164756F746865722D747970652D64"
				+ "6566696E6974696F6E666D792D6F746480");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);
		
		AbstractRequest result = RequestDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), 
																 topLevelArray.getDataItems().get(1), 
																 (Array)topLevelArray.getDataItems().get(2), 
																 (Array)topLevelArray.getDataItems().get(3));
		
		assertTrue(result instanceof CBORSubscribeRequest);
		CBORSubscribeRequest castResult = (CBORSubscribeRequest) result;
		
		Integer expMaxDepth = 2;
		Integer expMaxSize = 5000000;
		
		assertEquals(IfMapValidationType.ALL, castResult.getValidationAttributes().getValidationType());
		assertEquals("my-session-id", castResult.getSessionAttributes().getSessionId());
		
		assertEquals(2, castResult.getSubscripeTypes().size());
		
		castResult.getSubscripeTypes().forEach((st) -> {
			if(st instanceof CBORSubscribeDelete) {
				CBORSubscribeDelete castSt = (CBORSubscribeDelete) st;
				
				assertEquals("my-old-subscription", castSt.getName());
			}
			else if(st instanceof CBORSubscribeUpdate) {
				CBORSubscribeUpdate castSt = (CBORSubscribeUpdate) st;
				
				assertEquals("my-subscription", castSt.getName());
				
				assertEquals("match-links-filter", castSt.getSearchTypeAttributes().getMatchLinks());
				assertEquals("result-filter", castSt.getSearchTypeAttributes().getResultFilter());
				assertEquals("access-request", castSt.getSearchTypeAttributes().getTerminalIdentifierType());
				assertEquals(expMaxDepth, castSt.getSearchTypeAttributes().getMaxDepth());
				assertEquals(expMaxSize, castSt.getSearchTypeAttributes().getMaxSize());
				
				AbstractIdentifier resultIdentifier = castSt.getIdentifier();
				assertTrue(resultIdentifier instanceof CBORIdentity);

				CBORIdentity castResultIdentifier = (CBORIdentity) resultIdentifier;

				assertEquals("my-identity", castResultIdentifier.getName());
				assertEquals(IfMapIdentityType.OTHER, castResultIdentifier.getType());
				assertEquals("my-otd", castResultIdentifier.getOtherTypeDefinition());
				assertEquals("identity-ad", castResultIdentifier.getAdministrativeDomain());
			}
			else {
				fail("Unknwon subscribe type");
			}
		});
	}
	
	
	@Test
	public void testDeserialize_CBORPollRequest() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767"
				+ "726F75702E6F72672F323031302F49464D41502F3264706F6C6C846A73657373696F6E2D69646D6D792D73657373696F6E2D"
				+ "69646A76616C69646174696F6E6C4D657461646174614F6E6C7980");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);
		
		AbstractRequest result = RequestDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), 
																 topLevelArray.getDataItems().get(1), 
																 (Array)topLevelArray.getDataItems().get(2), 
																 (Array)topLevelArray.getDataItems().get(3));
		
		assertTrue(result instanceof CBORPollRequest);
		CBORPollRequest castResult = (CBORPollRequest) result;
		
		assertEquals(IfMapValidationType.METADATA_ONLY, castResult.getValidationAttributes().getValidationType());
		assertEquals("my-session-id", castResult.getSessionAttributes().getSessionId());
	}
	
	
	@Test
	public void testDeserialize_CBORPurgePublisherRequest() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767"
				+ "726F75702E6F72672F323031302F49464D41502F326E70757267655075626C6973686572846A73657373696F6E2D69646D6D"
				+ "792D73657373696F6E2D69647269666D61702D7075626C69736865722D69646F6D792D7075626C69736865722D696480");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);
		
		AbstractRequest result = RequestDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), 
																 topLevelArray.getDataItems().get(1), 
																 (Array)topLevelArray.getDataItems().get(2), 
																 (Array)topLevelArray.getDataItems().get(3));
		
		assertTrue(result instanceof CBORPurgePublisherRequest);
		CBORPurgePublisherRequest castResult = (CBORPurgePublisherRequest) result;
		
		assertEquals("my-publisher-id", castResult.getIfMapPublisherId());
		assertEquals("my-session-id", castResult.getSessionAttributes().getSessionId());
	}
	
	
	@Test
	public void testDeserialize_CBORNewSessionRequest() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767"
				+ "726F75702E6F72672F323031302F49464D41502F326A6E657753657373696F6E82746D61782D706F6C6C2D726573756C742D"
				+ "73697A651A004C4B4080");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);
		
		AbstractRequest result = RequestDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), 
																 topLevelArray.getDataItems().get(1), 
																 (Array)topLevelArray.getDataItems().get(2), 
																 (Array)topLevelArray.getDataItems().get(3));
		
		assertTrue(result instanceof CBORNewSessionRequest);
		CBORNewSessionRequest castResult = (CBORNewSessionRequest) result;
		
		Integer expMaxPollResultSize = 5000000;
		
		assertEquals(expMaxPollResultSize, castResult.getMaxPollResultSize());
	}
	
	
	@Test
	public void testDeserialize_CBOREndSessionRequest() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767"
				+ "726F75702E6F72672F323031302F49464D41502F326A656E6453657373696F6E826A73657373696F6E2D69646D6D792D7365"
				+ "7373696F6E2D696480");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);
		
		AbstractRequest result = RequestDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), 
																 topLevelArray.getDataItems().get(1), 
																 (Array)topLevelArray.getDataItems().get(2), 
																 (Array)topLevelArray.getDataItems().get(3));
		
		assertTrue(result instanceof CBOREndSessionRequest);
		CBOREndSessionRequest castResult = (CBOREndSessionRequest) result;
		
		assertEquals("my-session-id", castResult.getSessionAttributes().getSessionId());
	}
	
	
	@Test
	public void testDeserialize_CBORRenewSessionRequest() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767"
				+ "726F75702E6F72672F323031302F49464D41502F326C72656E657753657373696F6E826A73657373696F6E2D69646D6D792D"
				+ "73657373696F6E2D696480");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);
		
		AbstractRequest result = RequestDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), 
																 topLevelArray.getDataItems().get(1), 
																 (Array)topLevelArray.getDataItems().get(2), 
																 (Array)topLevelArray.getDataItems().get(3));
		
		assertTrue(result instanceof CBORRenewSessionRequest);
		CBORRenewSessionRequest castResult = (CBORRenewSessionRequest) result;
		
		assertEquals("my-session-id", castResult.getSessionAttributes().getSessionId());
	}
}
