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
import de.decoit.simu.cbor.ifmap.deserializer.vendor.VendorIdentifierDeserializer;
import de.decoit.simu.cbor.ifmap.identifier.extended.AbstractExtendedIdentifier;
import de.decoit.simu.cbor.ifmap.identifier.extended.CBORExtendedIfMapServer;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class ExtendedIdentifierDeserializerManagerTest extends AbstractTestBase {
	@After
	public void tearDown() {
		ExtendedIdentifierDeserializerManager.unregisterVendorDeserializer(DummyExtendedIdentifier.class);
	}


	@Test
	public void testDeserialize_CBORExtendedIfMapServer() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("8403D9A410008200782269666D61702D7365727665723A61646D696E697374"
				+ "7261746976652D646F6D61696E80");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORExtendedIfMapServer result = ExtendedIdentifierDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), 
																					topLevelArray.getDataItems().get(1), 
																					(Array)topLevelArray.getDataItems().get(2), 
																					(Array)topLevelArray.getDataItems().get(3), 
																					CBORExtendedIfMapServer.class);
		assertNotNull(result);
		assertEquals("ifmap-server:administrative-domain", result.getAdministrativeDomain());
	}


	@Test
	public void testRegisterVendorDeserializer() {
		ExtendedIdentifierDeserializerManager.registerVendorDeserializer(new VendorDeserializer(), DummyExtendedIdentifier.class, IfMapNamespaces.IFMAP_SERVER, "dummy-extended-identifier");
		assertTrue(ExtendedIdentifierDeserializerManager.hasVendorDeserializer(DummyExtendedIdentifier.class));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_null_null_null_null() {
		ExtendedIdentifierDeserializerManager.registerVendorDeserializer(null, null, null, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_null_Class_String_String() {
		ExtendedIdentifierDeserializerManager.registerVendorDeserializer(null, DummyExtendedIdentifier.class, IfMapNamespaces.IFMAP_SERVER, CBORExtendedIfMapServer.XML_NAME);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_VendorDeserializer_null_String_String() {
		ExtendedIdentifierDeserializerManager.registerVendorDeserializer(new VendorDeserializer(), null, IfMapNamespaces.IFMAP_SERVER, CBORExtendedIfMapServer.XML_NAME);
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_VendorDeserializer_Class_null_String() {
		ExtendedIdentifierDeserializerManager.registerVendorDeserializer(new VendorDeserializer(), DummyExtendedIdentifier.class, null, "dummy-metadata-A");
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_VendorDeserializer_Class_EmptyString_String() {
		ExtendedIdentifierDeserializerManager.registerVendorDeserializer(new VendorDeserializer(), DummyExtendedIdentifier.class, "", "dummy-metadata-A");
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_VendorDeserializer_Class_Whitespaces_String() {
		ExtendedIdentifierDeserializerManager.registerVendorDeserializer(new VendorDeserializer(), DummyExtendedIdentifier.class, "   ", "dummy-metadata-A");
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_VendorDeserializer_Class_String_null() {
		ExtendedIdentifierDeserializerManager.registerVendorDeserializer(new VendorDeserializer(), DummyExtendedIdentifier.class, IfMapNamespaces.IFMAP_METADATA, null);
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_VendorDeserializer_Class_String_EmptyString() {
		ExtendedIdentifierDeserializerManager.registerVendorDeserializer(new VendorDeserializer(), DummyExtendedIdentifier.class, IfMapNamespaces.IFMAP_METADATA, "");
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterVendorDeserializer_VendorDeserializer_Class_String_Whitespaces() {
		ExtendedIdentifierDeserializerManager.registerVendorDeserializer(new VendorDeserializer(), DummyExtendedIdentifier.class, IfMapNamespaces.IFMAP_METADATA, "   ");
	}


	@Test(expected = IllegalStateException.class)
	public void testRegisterVendorDeserializer_ExistingDeserializer() {
		ExtendedIdentifierDeserializerManager.registerVendorDeserializer(new VendorDeserializer(), DummyExtendedIdentifier.class, IfMapNamespaces.IFMAP_SERVER, "dummy-extended-identifier");
		ExtendedIdentifierDeserializerManager.registerVendorDeserializer(new VendorDeserializer(), DummyExtendedIdentifier.class, IfMapNamespaces.IFMAP_SERVER, "dummy-extended-identifier");
	}


	@Test
	public void testUnregisterVendorDeserializer() {
		assertFalse(ExtendedIdentifierDeserializerManager.hasVendorDeserializer(DummyExtendedIdentifier.class));

		ExtendedIdentifierDeserializerManager.registerVendorDeserializer(new VendorDeserializer(), DummyExtendedIdentifier.class, IfMapNamespaces.IFMAP_SERVER, "dummy-extended-identifier");
		assertTrue(ExtendedIdentifierDeserializerManager.hasVendorDeserializer(DummyExtendedIdentifier.class));

		ExtendedIdentifierDeserializerManager.unregisterVendorDeserializer(DummyExtendedIdentifier.class);
		assertFalse(ExtendedIdentifierDeserializerManager.hasVendorDeserializer(DummyExtendedIdentifier.class));
	}


	private static class VendorDeserializer implements VendorIdentifierDeserializer<DummyExtendedIdentifier> {
		@Override
		public DummyExtendedIdentifier deserialize(final Array attributes, final Array nestedTags, final DictionarySimpleElement elementDictEntry) {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}
	
	
	private static class DummyExtendedIdentifier extends AbstractExtendedIdentifier {
		public DummyExtendedIdentifier() {
			super(IfMapNamespaces.IFMAP_SERVER, "dummy-extended-identifier", "dummy-adm-domain", new HashMap<>(), new HashMap<>());
		}
	}
}
