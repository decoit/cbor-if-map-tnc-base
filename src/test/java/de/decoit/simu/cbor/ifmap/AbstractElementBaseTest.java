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
package de.decoit.simu.cbor.ifmap;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.ArrayBuilder;
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
public class AbstractElementBaseTest extends AbstractTestBase {
	@Test
	public void testConstructor() {
		AbstractElementBase result = new AbstractElementBaseImpl();

		assertEquals(AbstractElementBaseImpl.statNamespace, result.getNamespace());
		assertEquals(AbstractElementBaseImpl.statElementName, result.getElementName());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullNamespace() {
		AbstractElementBase result = new AbstractElementBaseImpl_NullNamespace();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyNamespace() {
		AbstractElementBase result = new AbstractElementBaseImpl_EmptyNamespace();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_WhitespaceNamespace() {
		AbstractElementBase result = new AbstractElementBaseImpl_WhitespaceNamespace();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullElementName() {
		AbstractElementBase result = new AbstractElementBaseImpl_NullElementName();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyElementName() {
		AbstractElementBase result = new AbstractElementBaseImpl_EmptyElementName();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_WhitespaceElementName() {
		AbstractElementBase result = new AbstractElementBaseImpl_WhitespaceElementName();
	}


	@Test
	public void testCborSerialize() throws Exception {
		AbstractElementBase instance = new AbstractElementBaseImpl();

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("8463646E7361648080");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	private static class AbstractElementBaseImpl extends AbstractElementBase {
		private static final String statNamespace = "dummy-namespace";
		private static final String statElementName = "dummy";

		public AbstractElementBaseImpl() {
			super(statNamespace, statElementName);
		}
	}


	private static class AbstractElementBaseImpl_NullNamespace extends AbstractElementBase {
		private static final String statNamespace = null;
		private static final String statElementName = "dummy";

		public AbstractElementBaseImpl_NullNamespace() {
			super(statNamespace, statElementName);
		}
	}


	private static class AbstractElementBaseImpl_EmptyNamespace extends AbstractElementBase {
		private static final String statNamespace = "";
		private static final String statElementName = "dummy";

		public AbstractElementBaseImpl_EmptyNamespace() {
			super(statNamespace, statElementName);
		}
	}


	private static class AbstractElementBaseImpl_WhitespaceNamespace extends AbstractElementBase {
		private static final String statNamespace = "   ";
		private static final String statElementName = "dummy";

		public AbstractElementBaseImpl_WhitespaceNamespace() {
			super(statNamespace, statElementName);
		}
	}


	private static class AbstractElementBaseImpl_NullElementName extends AbstractElementBase {
		private static final String statNamespace = "dummy-namespace";
		private static final String statElementName = null;

		public AbstractElementBaseImpl_NullElementName() {
			super(statNamespace, statElementName);
		}
	}


	private static class AbstractElementBaseImpl_EmptyElementName extends AbstractElementBase {
		private static final String statNamespace = "dummy-namespace";
		private static final String statElementName = "";

		public AbstractElementBaseImpl_EmptyElementName() {
			super(statNamespace, statElementName);
		}
	}


	private static class AbstractElementBaseImpl_WhitespaceElementName extends AbstractElementBase {
		private static final String statNamespace = "dummy-namespace";
		private static final String statElementName = "   ";

		public AbstractElementBaseImpl_WhitespaceElementName() {
			super(statNamespace, statElementName);
		}
	}
}
