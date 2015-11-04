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

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.UnicodeString;
import de.decoit.simu.cbor.ifmap.enums.IfMapErrorCode;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of the IF-MAP error response.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORErrorResult extends AbstractResult {
	public static final String XML_NAME = "errorResult";
	public static final String ERROR_CODE = "errorCode";
	public static final String ERROR_STRING = "errorString";
	public static final String NAME = "name";

	@Getter
	private final IfMapErrorCode errorCode;
	@Getter
	private String errorString;
	@Getter
	private String name;


	/**
	 * Create a new error response.
	 *
	 * @param errorCode IF-MAP error code
	 */
	public CBORErrorResult(IfMapErrorCode errorCode) {
		super(XML_NAME);

		if(errorCode == null) {
			throw new IllegalArgumentException("Error code must not be null");
		}

		this.errorCode = errorCode;
		this.errorString = null;
		this.name = null;
	}


	/**
	 * Set the error string for this error reponse.
	 * The string may be null to remove the error string from this response.
	 * If not null, the string must not be empty or whitespace only.
	 *
	 * @param errorString Error string, may be null
	 */
	public void setErrorString(String errorString) {
		if(StringUtils.isWhitespace(errorString)) {
			throw new IllegalArgumentException("Error string must not be empty or whitespace only");
		}

		this.errorString = errorString;
	}


	public void setName(String name) {
		if(StringUtils.isWhitespace(name)) {
			throw new IllegalArgumentException("Name must not be empty or whitespace only");
		}

		this.name = name;
	}


	@Override
	protected void serializeAttributes(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		try {
			// Serialize 'errorCode'
			{
				DataItem cborName = this.getAttributeNameMapping(CBORErrorResult.ERROR_CODE, elementEntry);
				DataItem cborValue = this.getAttributeEnumValueMapping(CBORErrorResult.ERROR_CODE, this.errorCode.getXmlName(), elementEntry);

				builder.add(cborName);
				builder.add(cborValue);
			}


			// Serialize 'name'
			if(this.name != null) {
				DataItem cborName = this.getAttributeNameMapping(CBORErrorResult.NAME, elementEntry);

				builder.add(cborName);
				builder.add(new UnicodeString(this.name));
			}
		}
		catch(RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}


	@Override
	protected void serializeNestedElements(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		super.serializeNestedElements(builder, elementEntry);

		try {
			// Serialize 'errorString'
			if(this.errorString != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORErrorResult.ERROR_STRING, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.errorString));
			}
		}
		catch(RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
