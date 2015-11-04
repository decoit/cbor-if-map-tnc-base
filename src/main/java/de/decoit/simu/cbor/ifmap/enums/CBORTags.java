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
package de.decoit.simu.cbor.ifmap.enums;

import co.nstant.in.cbor.model.Tag;



/**
 * A collection of tags available in CBOR.
 * This contains both the default CBOR tags and the tags specified in the SIMU project.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
public enum CBORTags {
	DATE_TIME_STRING(0L),
	DATE_TIME_EPOCH(1L),
	POSITIVE_BIGNUM(2L),
	NEGATIVE_BIGNUM(3L),
	DECIMAL_FRACTION(4L),
	BIGFLOAT(5L),
	BASE64_URL_ENCODING(21L),
	BASE64_ENCODING(22L),
	BASE16_ENCODING(23L),
	ENCODED_CBOR_DATA_ITEM(24L),
	URI(32L),
	BASE64_URL(33L),
	BASE64(34L),
	REGULAR_EXPRESSION(35L),
	MIME_MESSAGE(36L),
	IF_MAP_EXTENDED_IDENTIFIER(42000L),
	IPV4_ADDRESS(42001L),
	IPV6_ADDRESS(42002L),
	MAC_ADDRESS(42003L),
	SELF_DESCRIBE_CBOR(55799L);

	private final long tagNumber;


	private CBORTags(long i) {
		this.tagNumber = i;
	}


	/**
	 * Create a Tag data item representing this tag.
	 *
	 * @return Tag data item
	 */
	public Tag getTagDataItem() {
		Tag di = new Tag(this.tagNumber);
		return di;
	}


	/**
	 * Get the matching enum constant for the specified tag number.
	 *
	 * @param i Tag number
	 * @return Matching enum constant
	 * @throws IllegalArgumentException when no tag is found for that number
	 */
	public static CBORTags fromTagNumber(long i) {
		if(i == 0L) {
			return CBORTags.DATE_TIME_STRING;
		}
		else if(i == 1L) {
			return CBORTags.DATE_TIME_EPOCH;
		}
		else if(i == 2L) {
			return CBORTags.POSITIVE_BIGNUM;
		}
		else if(i == 3L) {
			return CBORTags.NEGATIVE_BIGNUM;
		}
		else if(i == 4L) {
			return CBORTags.DECIMAL_FRACTION;
		}
		else if(i == 5L) {
			return CBORTags.BIGFLOAT;
		}
		else if(i == 21L) {
			return CBORTags.BASE64_URL_ENCODING;
		}
		else if(i == 22L) {
			return CBORTags.BASE64_ENCODING;
		}
		else if(i == 23L) {
			return CBORTags.BASE16_ENCODING;
		}
		else if(i == 24L) {
			return CBORTags.ENCODED_CBOR_DATA_ITEM;
		}
		else if(i == 32L) {
			return CBORTags.URI;
		}
		else if(i == 33L) {
			return CBORTags.BASE64_URL;
		}
		else if(i == 34L) {
			return CBORTags.BASE64;
		}
		else if(i == 35L) {
			return CBORTags.REGULAR_EXPRESSION;
		}
		else if(i == 36L) {
			return CBORTags.MIME_MESSAGE;
		}
		else if(i == 42000L) {
			return CBORTags.IF_MAP_EXTENDED_IDENTIFIER;
		}
		else if(i == 42001L) {
			return CBORTags.IPV4_ADDRESS;
		}
		else if(i == 42002L) {
			return CBORTags.IPV6_ADDRESS;
		}
		else if(i == 42003L) {
			return CBORTags.MAC_ADDRESS;
		}
		else if(i == 55799L) {
			return CBORTags.SELF_DESCRIBE_CBOR;
		}
		else {
			throw new IllegalArgumentException("Unknown tag value: " + i);
		}
	}
}