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
import de.decoit.simu.cbor.ifmap.enums.IfMapErrorCode;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.io.ByteArrayOutputStream;
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
public class CBORErrorResultNoDictTest {
	private final String errorString = "custom-error";
	private final String name = "sub-01";


	@Test
	public void testCborSerialize_AccessDenied() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORErrorResult instance = new CBORErrorResult(IfMapErrorCode.ACCESS_DENIED);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (AccessDenied):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7482696572726F72436F64656C41636365737344656E69656480");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_Failure() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORErrorResult instance = new CBORErrorResult(IfMapErrorCode.FAILURE);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (Failure):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7482696572726F72436F6465674661696C75726580");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_InvalidIdentifier() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORErrorResult instance = new CBORErrorResult(IfMapErrorCode.INVALID_IDENTIFIER);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (InvalidIdentifier):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7482696572726F72436F646571496E76616C69644964656E74696669657280");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_InvalidIdentifierType() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORErrorResult instance = new CBORErrorResult(IfMapErrorCode.INVALID_IDENTIFIER_TYPE);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (InvalidIdentifierType):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7482696572726F72436F646575496E76616C69644964656E74696669657254"
				+ "79706580");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_IdentifierTooLong() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORErrorResult instance = new CBORErrorResult(IfMapErrorCode.IDENTIFIER_TOO_LONG);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (IdentifierTooLong):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7482696572726F72436F6465714964656E746966696572546F6F4C6F6E6780");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_InvalidMetadata() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORErrorResult instance = new CBORErrorResult(IfMapErrorCode.INVALID_METADATA);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (InvalidMetadata):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7482696572726F72436F64656F496E76616C69644D6574616461746180");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_InvalidSchemaVersion() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORErrorResult instance = new CBORErrorResult(IfMapErrorCode.INVALID_SCHEMA_VERSION);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (InvalidSchemaVersion):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7482696572726F72436F646574496E76616C6964536368656D615665727369"
				+ "6F6E80");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_InvalidSessionID() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORErrorResult instance = new CBORErrorResult(IfMapErrorCode.INVALID_SESSION_ID);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (InvalidSessionID):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7482696572726F72436F646570496E76616C696453657373696F6E494480");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_MetadataTooLong() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORErrorResult instance = new CBORErrorResult(IfMapErrorCode.METADATA_TOO_LONG);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (MetadataTooLong):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7482696572726F72436F64656F4D65746164617461546F6F4C6F6E6780");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_SearchResultsTooBig() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORErrorResult instance = new CBORErrorResult(IfMapErrorCode.SEARCH_RESULTS_TOO_BIG);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (SearchResultsTooBig):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7482696572726F72436F646573536561726368526573756C7473546F6F4269"
				+ "6780");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_PollResultsTooBig() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORErrorResult instance = new CBORErrorResult(IfMapErrorCode.POLL_RESULTS_TOO_BIG);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (PollResultsTooBig):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7482696572726F72436F646571506F6C6C526573756C7473546F6F42696780");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_SystemError() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORErrorResult instance = new CBORErrorResult(IfMapErrorCode.SYSTEM_ERROR);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (SystemError):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7482696572726F72436F64656B53797374656D4572726F7280");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_SystemError_full() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response");

		CBORErrorResult instance = new CBORErrorResult(IfMapErrorCode.SYSTEM_ERROR);
		instance.setErrorString(this.errorString);
		instance.setName(this.name);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (SystemError full):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7484696572726F72436F64656B537973"
				+ "74656D4572726F72646E616D65667375622D303184F66B6572726F72537472696E67806C637573746F6D2D6572726F72");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_SystemError_full_InsidePollResult() throws Exception {
		DictionarySimpleElement parentElementEntry = DictionaryProvider.getInstance().findElementByPath("<" + IfMapNamespaces.IFMAP + ">response+pollResult");

		CBORErrorResult instance = new CBORErrorResult(IfMapErrorCode.SYSTEM_ERROR);
		instance.setErrorString(this.errorString);
		instance.setName(this.name);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, parentElementEntry);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (SystemError full inside PollResult):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7484696572726F72436F64656B537973"
				+ "74656D4572726F72646E616D65667375622D303184F66B6572726F72537472696E67806C637573746F6D2D6572726F72");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}
}
