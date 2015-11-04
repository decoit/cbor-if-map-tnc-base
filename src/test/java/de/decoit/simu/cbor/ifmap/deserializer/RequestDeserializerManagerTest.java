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
public class RequestDeserializerManagerTest extends AbstractTestBase {
	@Test
	public void testDeserialize_CBORPublishRequest() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84000584006D6D792D73657373696F6E2D696401038CF6008200008C000180"
				+ "84F6008068736572766572303100038400D9A411440A0A6411020080F6008084010882030080F602808800018084F6008068"
				+ "736572766572303100038400D9A411440A0A6411020080F6018200008C00018084F6008068736572766572303100038400D9"
				+ "A411440A0A6411020080F6008084010882030080");

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
		assertEquals("my-session-id", castResult.getSessionAttributes().getSessionId());
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
		byte[] input = DatatypeConverter.parseHexBinary("8400068E006D6D792D73657373696F6E2D696402726D617463682D6C696E6B"
				+ "732D66696C7465720302041A004C4B40056D726573756C742D66696C746572066E6163636573732D72657175657374010184"
				+ "000288006B6D792D6964656E746974790209016B6964656E746974792D616403666D792D6F746480");

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
		byte[] input = DatatypeConverter.parseHexBinary("84000784006D6D792D73657373696F6E2D6964010388F6018200736D792D6F"
				+ "6C642D737562736372697074696F6E80F6008C006F6D792D737562736372697074696F6E02726D617463682D6C696E6B732D"
				+ "66696C7465720302041A004C4B40056D726573756C742D66696C746572066E6163636573732D726571756573748400028800"
				+ "6B6D792D6964656E746974790209016B6964656E746974792D616403666D792D6F746480");

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
				
				Integer expMaxDepth = 2;
				Integer expMaxSize = 5000000;
				
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
		byte[] input = DatatypeConverter.parseHexBinary("84000884006D6D792D73657373696F6E2D6964010280");

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
		byte[] input = DatatypeConverter.parseHexBinary("84000984006D6D792D73657373696F6E2D6964016F6D792D7075626C697368"
				+ "65722D696480");

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
		byte[] input = DatatypeConverter.parseHexBinary("84000A82001A004C4B4080");

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
		byte[] input = DatatypeConverter.parseHexBinary("84000B82006D6D792D73657373696F6E2D696480");

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
		byte[] input = DatatypeConverter.parseHexBinary("84000C82006D6D792D73657373696F6E2D696480");

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
