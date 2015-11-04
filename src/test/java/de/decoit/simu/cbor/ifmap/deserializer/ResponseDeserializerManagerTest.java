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

import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.CBORDeserializer;
import de.decoit.simu.cbor.ifmap.CBORSerializer;
import de.decoit.simu.cbor.ifmap.enums.IfMapErrorCode;
import de.decoit.simu.cbor.ifmap.enums.IfMapIdentityType;
import de.decoit.simu.cbor.ifmap.identifier.AbstractIdentifier;
import de.decoit.simu.cbor.ifmap.identifier.CBORDevice;
import de.decoit.simu.cbor.ifmap.identifier.CBORIdentity;
import de.decoit.simu.cbor.ifmap.metadata.AbstractMetadata;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORCapability;
import de.decoit.simu.cbor.ifmap.metadata.multivalue.CBORDeviceAttribute;
import de.decoit.simu.cbor.ifmap.response.CBORResponse;
import de.decoit.simu.cbor.ifmap.response.model.CBOREndSessionResult;
import de.decoit.simu.cbor.ifmap.response.model.CBORErrorResult;
import de.decoit.simu.cbor.ifmap.response.model.CBORNewSessionResult;
import de.decoit.simu.cbor.ifmap.response.model.CBORPollResult;
import de.decoit.simu.cbor.ifmap.response.model.CBORPublishReceived;
import de.decoit.simu.cbor.ifmap.response.model.CBORPurgePublisherReceived;
import de.decoit.simu.cbor.ifmap.response.model.CBORRenewSessionResult;
import de.decoit.simu.cbor.ifmap.response.model.CBORSearchResult;
import de.decoit.simu.cbor.ifmap.response.model.CBORSubscribeReceived;
import de.decoit.simu.cbor.ifmap.response.model.search.DeletePollSearchResult;
import de.decoit.simu.cbor.ifmap.response.model.search.SearchResult;
import de.decoit.simu.cbor.ifmap.response.model.search.SearchResultItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class ResponseDeserializerManagerTest extends AbstractTestBase {
	@Test
	public void testDeserialize_CBORErrorResult() throws Exception {
		CBORErrorResult inputResult = new CBORErrorResult(IfMapErrorCode.FAILURE);
		inputResult.setName("sub-01");
		inputResult.setErrorString("custom");
		
		CBORResponse r = new CBORResponse(inputResult);
		
		byte[] cborBytes = CBORSerializer.serializeResponse(r);
		
		CBORResponse result = CBORDeserializer.deserializeResponse(cborBytes);
		
		assertEquals(r, result);
	}
	
	
	@Test
	public void testDeserialize_CBORPublishReceived() throws Exception {
		CBORPublishReceived inputResult = new CBORPublishReceived();
		
		CBORResponse r = new CBORResponse(inputResult);
		
		byte[] cborBytes = CBORSerializer.serializeResponse(r);
		
		CBORResponse result = CBORDeserializer.deserializeResponse(cborBytes);
		
		assertEquals(r, result);
	}
	
	
	@Test
	public void testDeserialize_CBORSearchResult() throws Exception {
		AbstractIdentifier identifierA = new CBORIdentity("my-dns-name", IfMapIdentityType.DNS_NAME);
		AbstractIdentifier identifierB = new CBORDevice("device-01", false);
	
		AbstractMetadata m1 = new CBORCapability("cap-01");
		AbstractMetadata m2 = new CBORCapability("cap-02");
		AbstractMetadata m3 = new CBORDeviceAttribute("my-attribute");

		SearchResultItem sri1 = new SearchResultItem(identifierA);
		sri1.addMetadata(m3);

		SearchResultItem sri2 = new SearchResultItem(identifierB);
		sri2.setIdentifierB(identifierA);
		sri2.addMetadata(m1);
		sri2.addMetadata(m2);
		
		SearchResult sr = new SearchResult();
		sr.setName("sr-name");
		sr.addSearchResultItem(sri1);
		sr.addSearchResultItem(sri2);
		
		CBORSearchResult inputResult = new CBORSearchResult(sr);
		
		CBORResponse r = new CBORResponse(inputResult);
		
		byte[] cborBytes = CBORSerializer.serializeResponse(r);
		
		CBORResponse result = CBORDeserializer.deserializeResponse(cborBytes);
		
		assertEquals(r, result);
	}
	
	
	@Test
	public void testDeserialize_CBORSubscribeReceived() throws Exception {
		CBORSubscribeReceived inputResult = new CBORSubscribeReceived();
		
		CBORResponse r = new CBORResponse(inputResult);
		
		byte[] cborBytes = CBORSerializer.serializeResponse(r);
		
		CBORResponse result = CBORDeserializer.deserializeResponse(cborBytes);
		
		assertEquals(r, result);
	}
	
	
	@Test
	public void testDeserialize_CBORPollResult() throws Exception {
		AbstractIdentifier identifierA = new CBORIdentity("my-dns-name", IfMapIdentityType.DNS_NAME);
		AbstractIdentifier identifierB = new CBORDevice("device-01", false);
	
		AbstractMetadata m1 = new CBORCapability("cap-01");
		AbstractMetadata m2 = new CBORCapability("cap-02");
		AbstractMetadata m3 = new CBORDeviceAttribute("my-attribute");

		SearchResultItem sri1 = new SearchResultItem(identifierA);
		sri1.addMetadata(m3);

		SearchResultItem sri2 = new SearchResultItem(identifierB);
		sri2.setIdentifierB(identifierA);
		sri2.addMetadata(m1);
		sri2.addMetadata(m2);
		
		DeletePollSearchResult dpsr = new DeletePollSearchResult();
		dpsr.addSearchResultItem(sri1);
		dpsr.addSearchResultItem(sri2);
		
		CBORPollResult inputResult = new CBORPollResult();
		inputResult.addPollResult(dpsr);
		
		CBORResponse r = new CBORResponse(inputResult);
		
		byte[] cborBytes = CBORSerializer.serializeResponse(r);
		
		CBORResponse result = CBORDeserializer.deserializeResponse(cborBytes);
		
		assertEquals(r, result);
	}
	
	
	@Test
	public void testDeserialize_CBORPurgePublisherReceived() throws Exception {
		CBORPurgePublisherReceived inputResult = new CBORPurgePublisherReceived();
		
		CBORResponse r = new CBORResponse(inputResult);
		
		byte[] cborBytes = CBORSerializer.serializeResponse(r);
		
		CBORResponse result = CBORDeserializer.deserializeResponse(cborBytes);
		
		assertEquals(r, result);
	}
	
	
	@Test
	public void testDeserialize_CBORNewSessionResult() throws Exception {
		CBORNewSessionResult inputResult = new CBORNewSessionResult("my-session", "pub-id");
		inputResult.setMaxPollResultSize(42424242);
		
		CBORResponse r = new CBORResponse(inputResult);
		
		byte[] cborBytes = CBORSerializer.serializeResponse(r);
		
		CBORResponse result = CBORDeserializer.deserializeResponse(cborBytes);
		
		assertEquals(r, result);
	}
	
	
	@Test
	public void testDeserialize_CBOREndSessionResult() throws Exception {
		CBOREndSessionResult inputResult = new CBOREndSessionResult();
		
		CBORResponse r = new CBORResponse(inputResult);
		
		byte[] cborBytes = CBORSerializer.serializeResponse(r);
		
		CBORResponse result = CBORDeserializer.deserializeResponse(cborBytes);
		
		assertEquals(r, result);
	}
	
	
	@Test
	public void testDeserialize_CBORRenewSessionResult() throws Exception {
		CBORRenewSessionResult inputResult = new CBORRenewSessionResult();
		
		CBORResponse r = new CBORResponse(inputResult);
		
		byte[] cborBytes = CBORSerializer.serializeResponse(r);
		
		CBORResponse result = CBORDeserializer.deserializeResponse(cborBytes);
		
		assertEquals(r, result);
	}
}
