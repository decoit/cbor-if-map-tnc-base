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
public class AbstractComplexTypeTest extends AbstractTestBase {
	@Test
	public void testAddComplexElement() {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		
		instance.setDummyAttribute("dummy-attr-value");
		instance.setDummySimpleElement("dummy-se-value");
		instance.setDummyComplexElement();
		
		AbstractComplexTypeImpl expNestedCe = new AbstractComplexTypeImpl();
		expNestedCe.setValue("nested-ce");
		
		assertEquals("dummy-complex-type", instance.getElementName());
		assertEquals(new UnicodeString("dummy-attr-value"), instance.attributes.get("dummy"));
		assertEquals(new UnicodeString("dummy-se-value"), instance.simpleElements.get("dummy-se"));
		assertEquals(expNestedCe, instance.complexElements.get(0));
	}


	@Test
	public void testSetValue_String() {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue("dummy-value");
		
		assertEquals(new UnicodeString("dummy-value"), instance.getValue());
	}
	
	
	@Test
	public void testSetValue_String_null() {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue("dummy-value");
		instance.setValue((String) null);
		
		assertNull(instance.getValue());
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetValue_String_EmptyString() {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue("");
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetValue_String_Whitespaces() {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue("   ");
	}


	@Test
	public void testSetValue_Long_positive() {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue(42L);
		
		assertEquals(new UnsignedInteger(42L), instance.getValue());
	}
	
	
	@Test
	public void testSetValue_Long_0() {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue(0L);
		
		assertEquals(new UnsignedInteger(0L), instance.getValue());
	}
	
	
	@Test
	public void testSetValue_Long_negative() {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue(-42L);
		
		assertEquals(new NegativeInteger(-42L), instance.getValue());
	}
	
	
	@Test
	public void testSetValue_Long_null() {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue(42L);
		instance.setValue((Long) null);
		
		assertNull(instance.getValue());
	}


	@Test
	public void testSetValue_Double() {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue(42.0);
		
		assertEquals(new DoublePrecisionFloat(42.0), instance.getValue());
	}
	
	
	@Test
	public void testSetValue_Double_null() {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue(42.0);
		instance.setValue((Double) null);
		
		assertNull(instance.getValue());
	}


	@Test
	public void testSetValue_byteArr() {
		byte[] value = {(byte) 0x3C, (byte) 0x42};
		
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue(value);
		
		assertEquals(new ByteString(value), instance.getValue());
	}
	
	
	@Test
	public void testSetValue_byteArr_null() {
		byte[] value = {(byte) 0x3C, (byte) 0x42};
		
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue(value);
		instance.setValue((byte[]) null);
		
		assertNull(instance.getValue());
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetValue_byteArr_empty() {
		byte[] value = {};
		
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue(value);
	}


	@Test
	public void testSetValue_Boolean_true() {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue(true);
		
		assertEquals(new SimpleValue(SimpleValueType.TRUE), instance.getValue());
	}
	
	
	@Test
	public void testSetValue_Boolean_false() {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue(false);
		
		assertEquals(new SimpleValue(SimpleValueType.FALSE), instance.getValue());
	}
	
	
	@Test
	public void testSetValue_Boolean_null() {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		instance.setValue(true);
		instance.setValue((Boolean) null);
		
		assertNull(instance.getValue());
	}
	
	
	@Test
	public void testCborSerialize() throws Exception {
		AbstractComplexTypeImpl instance = new AbstractComplexTypeImpl();
		
		instance.setDummyAttribute("dummy-attr-value");
		instance.setDummySimpleElement("dummy-se-value");
		instance.setDummyComplexElement();
		
		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab, null);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("84F67264756D6D792D636F6D706C65782D74797065826564756D6D7970"
				+ "64756D6D792D617474722D76616C756588F66864756D6D792D7365806E64756D6D792D73652D76616C7565F67264756D6D79"
				+ "2D636F6D706C65782D7479706580696E65737465642D6365");

		assertTrue(Arrays.equals(expResult, bos.toByteArray()));
	}

	
	public class AbstractComplexTypeImpl extends AbstractComplexType {
		public AbstractComplexTypeImpl() {
			super("dummy-complex-type", new HashMap<>(), new HashMap<>());
		}
		
		
		public void setDummyAttribute(String attr) {
			this.attributes.put("dummy", new UnicodeString(attr));
		}
		
		
		public void setDummySimpleElement(String se) {
			this.simpleElements.put("dummy-se", new UnicodeString(se));
		}
		
		
		public void setDummyComplexElement() {
			AbstractComplexTypeImpl ceObject = new AbstractComplexTypeImpl();
			ceObject.setValue("nested-ce");
			
			this.complexElements.add(ceObject);
		}
	}
}
