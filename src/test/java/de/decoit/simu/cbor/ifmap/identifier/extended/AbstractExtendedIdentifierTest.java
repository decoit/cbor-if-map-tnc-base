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
package de.decoit.simu.cbor.ifmap.identifier.extended;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DoublePrecisionFloat;
import co.nstant.in.cbor.model.NegativeInteger;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import javax.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class AbstractExtendedIdentifierTest extends AbstractTestBase {
	@Test
	public void testSetValue_String() {
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue("dummy-value");
		
		assertEquals(new UnicodeString("dummy-value"), instance.getValue());
		assertTrue(instance.simpleElements.isEmpty());
		assertTrue(instance.complexElements.isEmpty());
	}
	
	
	@Test
	public void testSetValue_String_null() {
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue("dummy-value");
		instance.setValue((String) null);
		
		assertNull(instance.getValue());
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetValue_String_EmptyString() {
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue("");
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetValue_String_Whitespaces() {
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue("   ");
	}


	@Test
	public void testSetValue_Long_positive() {
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue(42L);
		
		assertEquals(new UnsignedInteger(42L), instance.getValue());
		assertTrue(instance.simpleElements.isEmpty());
		assertTrue(instance.complexElements.isEmpty());
	}
	
	
	@Test
	public void testSetValue_Long_0() {
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue(0L);
		
		assertEquals(new UnsignedInteger(0L), instance.getValue());
		assertTrue(instance.simpleElements.isEmpty());
		assertTrue(instance.complexElements.isEmpty());
	}
	
	
	@Test
	public void testSetValue_Long_negative() {
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue(-42L);
		
		assertEquals(new NegativeInteger(-42L), instance.getValue());
		assertTrue(instance.simpleElements.isEmpty());
		assertTrue(instance.complexElements.isEmpty());
	}
	
	
	@Test
	public void testSetValue_Long_null() {
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue(42L);
		instance.setValue((Long) null);
		
		assertNull(instance.getValue());
	}


	@Test
	public void testSetValue_Double() {
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue(42.0);
		
		assertEquals(new DoublePrecisionFloat(42.0), instance.getValue());
		assertTrue(instance.simpleElements.isEmpty());
		assertTrue(instance.complexElements.isEmpty());
	}
	
	
	@Test
	public void testSetValue_Double_null() {
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue(42.0);
		instance.setValue((Double) null);
		
		assertNull(instance.getValue());
	}


	@Test
	public void testSetValue_byteArr() {
		byte[] value = {(byte) 0x3C, (byte) 0x42};
		
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue(value);
		
		assertEquals(new ByteString(value), instance.getValue());
		assertTrue(instance.simpleElements.isEmpty());
		assertTrue(instance.complexElements.isEmpty());
	}
	
	
	@Test
	public void testSetValue_byteArr_null() {
		byte[] value = {(byte) 0x3C, (byte) 0x42};
		
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue(value);
		instance.setValue((byte[]) null);
		
		assertNull(instance.getValue());
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetValue_byteArr_empty() {
		byte[] value = {};
		
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue(value);
	}


	@Test
	public void testSetValue_Boolean_true() {
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue(true);
		
		assertEquals(new SimpleValue(SimpleValueType.TRUE), instance.getValue());
		assertTrue(instance.simpleElements.isEmpty());
		assertTrue(instance.complexElements.isEmpty());
	}
	
	
	@Test
	public void testSetValue_Boolean_false() {
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue(false);
		
		assertEquals(new SimpleValue(SimpleValueType.FALSE), instance.getValue());
		assertTrue(instance.simpleElements.isEmpty());
		assertTrue(instance.complexElements.isEmpty());
	}
	
	
	@Test
	public void testSetValue_Boolean_null() {
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		instance.setValue(true);
		instance.setValue((Boolean) null);
		
		assertNull(instance.getValue());
	}
	
	
	@Test
	public void testCborSerialize() throws Exception {
		AbstractExtendedIdentifierImpl instance = new AbstractExtendedIdentifierImpl();
		
		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("8463646E73D9A41063646569866261646361646D626461182A63646561"
				+ "62763190F662736580623231F66365736580627632F6626365846363656119029A646365656162763288F665636565736580"
				+ "627631F664636573658063333333F6636563658264656365611903E7627631");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}

	
	public class AbstractExtendedIdentifierImpl extends AbstractExtendedIdentifier {
		public AbstractExtendedIdentifierImpl() {
			super("dummy-namespace", "dummy-ext-identifier", "adm", new HashMap<>(), new HashMap<>());
			
			this.attributes.put("dummy-attr", new UnsignedInteger(42L));
			this.attributes.put("dummy-enum-attr", new UnicodeString("val-one"));
			this.simpleElements.put("nested-se", new UnicodeString("21"));
			this.simpleElements.put("nested-enum-se", new UnicodeString("val-two"));
			this.complexElements.add(new AbstractComplexTypeImpl());
			this.complexElements.add(new AbstractComplexTypeEnumImpl());
		}
	}
	
	
	public class AbstractComplexTypeImpl extends AbstractComplexType {
		public AbstractComplexTypeImpl() {
			super("nested-ce", new HashMap<>(), new HashMap<>());
			
			this.attributes.put("ce-attr", new UnsignedInteger(666L));
			this.attributes.put("ce-enum-attr", new UnicodeString("val-two"));
			this.simpleElements.put("ce-nested-se", new UnicodeString("333"));
			this.simpleElements.put("ce-nested-enum-se", new UnicodeString("val-one"));
		}
	}
	
	
	public class AbstractComplexTypeEnumImpl extends AbstractComplexType {
		public AbstractComplexTypeEnumImpl() {
			super("nested-enum-ce", new HashMap<>(), new HashMap<>());
			
			this.attributes.put("ece-attr", new UnsignedInteger(999L));
			this.value = new UnicodeString("val-one");
		}
	}
}
