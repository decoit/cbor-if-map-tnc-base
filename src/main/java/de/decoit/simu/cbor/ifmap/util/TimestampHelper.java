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
import co.nstant.in.cbor.model.NegativeInteger;
import co.nstant.in.cbor.model.UnsignedInteger;
import de.decoit.simu.cbor.ifmap.enums.CBORTags;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;



/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class TimestampHelper {
	private static final DateTimeFormatter IF_MAP_TIMESTAMP_FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME;


	public static ZonedDateTime toUTC(ZonedDateTime input) {
		if(input == null) {
			throw new IllegalArgumentException("Input time must not be null");
		}

		return input.withZoneSameInstant(ZoneOffset.UTC);
	}


	public static String toXsdDateTime(ZonedDateTime input) {
		if(input == null) {
			throw new IllegalArgumentException("Input time must not be null");
		}

		return IF_MAP_TIMESTAMP_FORMAT.format(input.truncatedTo(ChronoUnit.SECONDS));
	}


	public static long toEpochTime(ZonedDateTime input) {
		if(input == null) {
			throw new IllegalArgumentException("Input time must not be null");
		}

		return input.toEpochSecond();
	}


	public static DataItem toEpochTimeDataItem(ZonedDateTime input) {
		long epoch = toEpochTime(input);
		DataItem rv;
		if(epoch < 0) {
			rv = new NegativeInteger(epoch);
		}
		else {
			rv = new UnsignedInteger(epoch);
		}
		rv.setTag(CBORTags.DATE_TIME_EPOCH.getTagDataItem());

		return rv;
	}


	public static DataItem toIfMapTimestampFractionDataItem(ZonedDateTime input) {
		if(input == null) {
			throw new IllegalArgumentException("Input time must not be null");
		}

		long fraction = input.getNano();

		Array rv = new Array();
		rv.add(new NegativeInteger(-9L));
		rv.add(new UnsignedInteger(fraction));
		rv.setTag(CBORTags.DECIMAL_FRACTION.getTagDataItem());

		return rv;
	}


	public static ZonedDateTime fromXsdDateTime(String input) {
		if(input == null) {
			throw new IllegalArgumentException("Input string must not be null");
		}

		return ZonedDateTime.parse(input, IF_MAP_TIMESTAMP_FORMAT);
	}


	public static ZonedDateTime fromEpochTime(long input) {
		Instant i = Instant.ofEpochSecond(input);
		return ZonedDateTime.ofInstant(i, ZoneOffset.UTC);
	}


	public static ZonedDateTime fromEpochTimeDataItem(DataItem timestamp, DataItem fraction) {
		if(timestamp == null) {
			throw new IllegalArgumentException("Data item must not be null");
		}

		if(CBORTags.DATE_TIME_EPOCH.getTagDataItem().equals(timestamp.getTag())) {
			ZonedDateTime rv;
			if(timestamp.getMajorType() == MajorType.UNSIGNED_INTEGER) {
				UnsignedInteger ui = (UnsignedInteger) timestamp;
				rv = fromEpochTime(ui.getValue().longValueExact());
			}
			else if(timestamp.getMajorType() == MajorType.NEGATIVE_INTEGER) {
				NegativeInteger ni = (NegativeInteger) timestamp;
				rv = fromEpochTime(ni.getValue().longValueExact());
			}
			else {
				throw new IllegalArgumentException("Data item has invalid major type");
			}

			rv = rv.withNano(parseIfMapTimestampFractionDataItem(fraction));
			return rv;
		}
		else {
			throw new IllegalArgumentException("Data item is not tagges as epoch time");
		}
	}


	private static int parseIfMapTimestampFractionDataItem(DataItem fraction) {
		if(fraction == null) {
			return 0;
		}

		if(CBORTags.DECIMAL_FRACTION.getTagDataItem().equals(fraction.getTag())) {
			if(fraction.getMajorType() == MajorType.ARRAY) {
				Array fractionArray = (Array) fraction;
				DataItem exponent = fractionArray.getDataItems().get(0);
				DataItem mantissa = fractionArray.getDataItems().get(1);

				if(exponent == null || mantissa == null) {
					throw new IllegalArgumentException("Exponent or mantissa is null");
				}

				long exponentNum;
				if(exponent.getMajorType() == MajorType.UNSIGNED_INTEGER) {
					UnsignedInteger ui = (UnsignedInteger) exponent;
					exponentNum = ui.getValue().longValueExact();
				}
				else if(exponent.getMajorType() == MajorType.NEGATIVE_INTEGER) {
					NegativeInteger ui = (NegativeInteger) exponent;
					exponentNum = ui.getValue().longValueExact();
				}
				else {
					throw new IllegalArgumentException("Invalid major type for decimal fraction exponent");
				}

				long mantissaNum;
				if(mantissa.getMajorType() == MajorType.UNSIGNED_INTEGER) {
					UnsignedInteger ui = (UnsignedInteger) mantissa;
					mantissaNum = ui.getValue().longValueExact();
				}
				else if(mantissa.getMajorType() == MajorType.NEGATIVE_INTEGER) {
					NegativeInteger ui = (NegativeInteger) mantissa;
					mantissaNum = ui.getValue().longValueExact();
				}
				else {
					throw new IllegalArgumentException("Invalid major type for decimal fraction mantissa");
				}

				if(log.isDebugEnabled()) {
					log.debug("Decimal fraction exponent: " + exponentNum);
					log.debug("Decimal fraction mantissa: " + mantissaNum);
					log.debug("Decimal fraction exponent diff: " + (exponentNum - (-9.0)));
				}

				int nano = (int) (mantissaNum * Math.pow(10.0, exponentNum - (-9.0)));

				if(log.isDebugEnabled()) {
					log.debug("Timestamp fraction nanoseconds: " + nano);
				}

				return nano;
			}
			else {
				throw new IllegalArgumentException("Invalid major type for decimal fraction");
			}
		}
		else {
			throw new IllegalArgumentException("Fraction data item is not tagged with decimal fraction");
		}
	}


	protected TimestampHelper() { }
}
