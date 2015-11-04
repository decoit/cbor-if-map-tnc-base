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
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;
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
public class AbstractExtendedIdentifierNoDictTest {
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

		byte[] expResult = DatatypeConverter.parseHexBinary("846F64756D6D792D6E616D657370616365D9A4107464756D6D792D6578"
				+ "742D6964656E746966696572867561646D696E6973747261746976652D646F6D61696E6361646D6A64756D6D792D61747472"
				+ "182A6F64756D6D792D656E756D2D617474726776616C2D6F6E6590F6696E65737465642D736580623231F66E6E6573746564"
				+ "2D656E756D2D7365806776616C2D74776FF6696E65737465642D6365846763652D6174747219029A6C63652D656E756D2D61"
				+ "7474726776616C2D74776F88F67163652D6E65737465642D656E756D2D7365806776616C2D6F6E65F66C63652D6E65737465"
				+ "642D73658063333333F66E6E65737465642D656E756D2D636582686563652D617474721903E76776616C2D6F6E65");

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
