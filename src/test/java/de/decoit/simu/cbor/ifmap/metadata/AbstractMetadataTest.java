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
package de.decoit.simu.cbor.ifmap.metadata;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.ifmap.enums.IfMapCardinality;
import de.decoit.simu.cbor.ifmap.util.TimestampHelper;
import java.io.ByteArrayOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
public class AbstractMetadataTest extends AbstractTestBase {
	@Test
	public void testConstructor() {
		AbstractMetadata result = new AbstractMetadataImpl();

		assertEquals(AbstractMetadataImpl.statPublisherId, result.getIfMapPublisherId());
		assertEquals(TimestampHelper.toUTC(AbstractMetadataImpl.statIfMapTimestamp), result.getIfMapTimestamp());
		assertEquals(IfMapCardinality.SINGLE_VALUE, result.getIfMapCardinality());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullPublisherId() {
		AbstractMetadata result = new AbstractMetadataImpl_NullPublisherId();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyPublisherId() {
		AbstractMetadata result = new AbstractMetadataImpl_EmptyPublisherId();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_WhitespacePublisherId() {
		AbstractMetadata result = new AbstractMetadataImpl_WhitespacePublisherId();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullTimestamp() {
		AbstractMetadata result = new AbstractMetadataImpl_NullTimestamp();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NullCardinality() {
		AbstractMetadata result = new AbstractMetadataImpl_NullCardinality();
	}


	@Test
	public void testCborSerialize() throws Exception {
		AbstractMetadata instance = new AbstractMetadataImpl();

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize:");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("8463646E73616488637069646F64756D6D792D7075626C6973686572627473C11A4ED9E8B263747366C482281A075BCA0064636172640080");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	private static class AbstractMetadataImpl extends AbstractMetadata {
		private static final String statNamespace = "dummy-namespace";
		private static final String statElementName = "dummy";
		private static final String statPublisherId = "dummy-publisher";
		private static final ZonedDateTime statIfMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);

		public AbstractMetadataImpl() {
			super(statNamespace, statElementName, statPublisherId, statIfMapTimestamp, IfMapCardinality.SINGLE_VALUE);
		}
	}


	private static class AbstractMetadataImpl_NullPublisherId extends AbstractMetadata {
		private static final String statNamespace = "dummy-namespace";
		private static final String statElementName = "dummy";
		private static final String statPublisherId = null;
		private static final ZonedDateTime statIfMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);

		public AbstractMetadataImpl_NullPublisherId() {
			super(statNamespace, statElementName, statPublisherId, statIfMapTimestamp, IfMapCardinality.SINGLE_VALUE);
		}
	}


	private static class AbstractMetadataImpl_EmptyPublisherId extends AbstractMetadata {
		private static final String statNamespace = "dummy-namespace";
		private static final String statElementName = "dummy";
		private static final String statPublisherId = "";
		private static final ZonedDateTime statIfMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);

		public AbstractMetadataImpl_EmptyPublisherId() {
			super(statNamespace, statElementName, statPublisherId, statIfMapTimestamp, IfMapCardinality.SINGLE_VALUE);
		}
	}


	private static class AbstractMetadataImpl_WhitespacePublisherId extends AbstractMetadata {
		private static final String statNamespace = "dummy-namespace";
		private static final String statElementName = "dummy";
		private static final String statPublisherId = "   ";
		private static final ZonedDateTime statIfMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);

		public AbstractMetadataImpl_WhitespacePublisherId() {
			super(statNamespace, statElementName, statPublisherId, statIfMapTimestamp, IfMapCardinality.SINGLE_VALUE);
		}
	}


	private static class AbstractMetadataImpl_NullTimestamp extends AbstractMetadata {
		private static final String statNamespace = "dummy-namespace";
		private static final String statElementName = "dummy";
		private static final String statPublisherId = "dummy-publisher";
		private static final ZonedDateTime statIfMapTimestamp = null;

		public AbstractMetadataImpl_NullTimestamp() {
			super(statNamespace, statElementName, statPublisherId, statIfMapTimestamp, IfMapCardinality.SINGLE_VALUE);
		}
	}


	private static class AbstractMetadataImpl_NullCardinality extends AbstractMetadata {
		private static final String statNamespace = "dummy-namespace";
		private static final String statElementName = "dummy";
		private static final String statPublisherId = "dummy-publisher";
		private static final ZonedDateTime statIfMapTimestamp = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);

		public AbstractMetadataImpl_NullCardinality() {
			super(statNamespace, statElementName, statPublisherId, statIfMapTimestamp, null);
		}
	}
}
