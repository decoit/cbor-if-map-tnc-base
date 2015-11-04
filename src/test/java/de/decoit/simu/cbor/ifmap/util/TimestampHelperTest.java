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
package de.decoit.simu.cbor.ifmap.util;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.Map;
import co.nstant.in.cbor.model.NegativeInteger;
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;
import de.decoit.simu.cbor.ifmap.enums.CBORTags;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class TimestampHelperTest {
	@Test
	public void testToUTC() {
		ZonedDateTime input = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		ZonedDateTime expResult = input.withZoneSameInstant(ZoneOffset.UTC);

		ZonedDateTime result = TimestampHelper.toUTC(input);
		assertEquals(expResult, result);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testToUTC_null() {
		ZonedDateTime result = TimestampHelper.toUTC(null);
	}


	@Test
	public void testToXsdDateTime() {
		ZonedDateTime input = ZonedDateTime.parse("2011-12-03T10:15:30.123456+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		String expResult = "2011-12-03T10:15:30+01:00";
		String result = TimestampHelper.toXsdDateTime(input);
		assertEquals(expResult, result);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testToXsdDateTime_null() {
		String result = TimestampHelper.toXsdDateTime(null);
	}


	@Test
	public void testToEpochTime() {
		ZonedDateTime input = ZonedDateTime.parse("2011-12-03T10:15:30.123456Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		long expResult = 1322907330L;
		long result = TimestampHelper.toEpochTime(input);
		assertEquals(expResult, result);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testToEpochTime_null() {
		long result = TimestampHelper.toEpochTime(null);
	}


	@Test
	public void testToEpochTimeDataItem() {
		ZonedDateTime input = ZonedDateTime.parse("2011-12-03T10:15:30.123456Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		DataItem expResult = new UnsignedInteger(1322907330L);
		expResult.setTag(CBORTags.DATE_TIME_EPOCH.getTagDataItem());

		DataItem result = TimestampHelper.toEpochTimeDataItem(input);
		assertEquals(expResult, result);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testToEpochTimeDataItem_null() {
		DataItem result = TimestampHelper.toEpochTimeDataItem(null);
	}


	@Test
	public void testToIfMapTimestampFractionDataItem() {
		ZonedDateTime input = ZonedDateTime.parse("2011-12-03T10:15:30.123456Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);

		DataItem result = TimestampHelper.toIfMapTimestampFractionDataItem(input);
		assertEquals(MajorType.ARRAY, result.getMajorType());

		Array resultArray = (Array) result;
		assertEquals(2, resultArray.getDataItems().size());

		assertEquals(MajorType.NEGATIVE_INTEGER, resultArray.getDataItems().get(0).getMajorType());
		assertEquals(MajorType.UNSIGNED_INTEGER, resultArray.getDataItems().get(1).getMajorType());

		NegativeInteger ni = (NegativeInteger) resultArray.getDataItems().get(0);
		UnsignedInteger ui = (UnsignedInteger) resultArray.getDataItems().get(1);

		assertEquals(-9, ni.getValue().intValueExact());
		assertEquals(123456000, ui.getValue().intValueExact());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testToIfMapTimestampFractionDataItem_null() {
		DataItem result = TimestampHelper.toIfMapTimestampFractionDataItem(null);
	}


	@Test
	public void testFromXsdDateTime() {
		String input = "2011-12-03T10:15:30+01:00";
		ZonedDateTime expResult = ZonedDateTime.parse("2011-12-03T10:15:30+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		ZonedDateTime result = TimestampHelper.fromXsdDateTime(input);
		assertEquals(expResult, result);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXsdDateTime_null() {
		ZonedDateTime result = TimestampHelper.fromXsdDateTime(null);
	}


	@Test
	public void testFromEpochTime() {
		long input = 1322907330L;
		ZonedDateTime expResult = ZonedDateTime.parse("2011-12-03T10:15:30Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		ZonedDateTime result = TimestampHelper.fromEpochTime(input);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromEpochTimeDataItem_NanoFraction() {
		DataItem timestamp = new UnsignedInteger(1322907330L);
		timestamp.setTag(CBORTags.DATE_TIME_EPOCH.getTagDataItem());

		Array fraction = new Array();
		fraction.add(new NegativeInteger(-9L));
		fraction.add(new UnsignedInteger(123456000L));
		fraction.setTag(CBORTags.DECIMAL_FRACTION.getTagDataItem());

		ZonedDateTime expResult = ZonedDateTime.parse("2011-12-03T10:15:30.123456Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		ZonedDateTime result = TimestampHelper.fromEpochTimeDataItem(timestamp, fraction);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromEpochTimeDataItem_MicroFraction() {
		DataItem timestamp = new UnsignedInteger(1322907330L);
		timestamp.setTag(CBORTags.DATE_TIME_EPOCH.getTagDataItem());

		Array fraction = new Array();
		fraction.add(new NegativeInteger(-6L));
		fraction.add(new UnsignedInteger(123456L));
		fraction.setTag(CBORTags.DECIMAL_FRACTION.getTagDataItem());

		ZonedDateTime expResult = ZonedDateTime.parse("2011-12-03T10:15:30.123456Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		ZonedDateTime result = TimestampHelper.fromEpochTimeDataItem(timestamp, fraction);
		assertEquals(expResult, result);
	}


	@Test
	public void testFromEpochTimeDataItem_NoFraction() {
		DataItem timestamp = new UnsignedInteger(1322907330L);
		timestamp.setTag(CBORTags.DATE_TIME_EPOCH.getTagDataItem());

		Array fraction = null;

		ZonedDateTime expResult = ZonedDateTime.parse("2011-12-03T10:15:30Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		ZonedDateTime result = TimestampHelper.fromEpochTimeDataItem(timestamp, fraction);
		assertEquals(expResult, result);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromEpochTimeDataItem_null_null() {
		ZonedDateTime result = TimestampHelper.fromEpochTimeDataItem(null, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromEpochTimeDataItem_UntaggedTimestamp() {
		DataItem timestamp = new UnsignedInteger(1322907330L);

		Array fraction = new Array();
		fraction.add(new NegativeInteger(-6L));
		fraction.add(new UnsignedInteger(123456L));
		fraction.setTag(CBORTags.DECIMAL_FRACTION.getTagDataItem());

		ZonedDateTime result = TimestampHelper.fromEpochTimeDataItem(timestamp, fraction);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromEpochTimeDataItem_InvalidTimestampMT() {
		DataItem timestamp = new UnicodeString("1322907330");
		timestamp.setTag(CBORTags.DATE_TIME_EPOCH.getTagDataItem());

		Array fraction = new Array();
		fraction.add(new NegativeInteger(-6L));
		fraction.add(new UnsignedInteger(123456L));
		fraction.setTag(CBORTags.DECIMAL_FRACTION.getTagDataItem());

		ZonedDateTime result = TimestampHelper.fromEpochTimeDataItem(timestamp, fraction);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromEpochTimeDataItem_UntaggedFraction() {
		DataItem timestamp = new UnsignedInteger(1322907330L);
		timestamp.setTag(CBORTags.DATE_TIME_EPOCH.getTagDataItem());

		Array fraction = new Array();
		fraction.add(new NegativeInteger(-6L));
		fraction.add(new UnsignedInteger(123456L));

		ZonedDateTime result = TimestampHelper.fromEpochTimeDataItem(timestamp, fraction);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromEpochTimeDataItem_InvalidFractionMT() {
		DataItem timestamp = new UnsignedInteger(1322907330L);
		timestamp.setTag(CBORTags.DATE_TIME_EPOCH.getTagDataItem());

		Map fraction = new Map();
		fraction.setTag(CBORTags.DECIMAL_FRACTION.getTagDataItem());

		ZonedDateTime result = TimestampHelper.fromEpochTimeDataItem(timestamp, fraction);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromEpochTimeDataItem_InvalidFractionExponentMT() {
		DataItem timestamp = new UnsignedInteger(1322907330L);
		timestamp.setTag(CBORTags.DATE_TIME_EPOCH.getTagDataItem());

		Array fraction = new Array();
		fraction.add(new UnicodeString("-6"));
		fraction.add(new UnsignedInteger(123456L));
		fraction.setTag(CBORTags.DECIMAL_FRACTION.getTagDataItem());

		ZonedDateTime result = TimestampHelper.fromEpochTimeDataItem(timestamp, fraction);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromEpochTimeDataItem_InvalidFractionMantissaMT() {
		DataItem timestamp = new UnsignedInteger(1322907330L);
		timestamp.setTag(CBORTags.DATE_TIME_EPOCH.getTagDataItem());

		Array fraction = new Array();
		fraction.add(new NegativeInteger(-6L));
		fraction.add(new UnicodeString("123456"));
		fraction.setTag(CBORTags.DECIMAL_FRACTION.getTagDataItem());

		ZonedDateTime result = TimestampHelper.fromEpochTimeDataItem(timestamp, fraction);
	}
	
	
	@Test
	public void makeJaCoCoHappy() {
		TimestampHelperExt instance = new TimestampHelperExt();
	}
	
	private static class TimestampHelperExt extends TimestampHelper {}
}
