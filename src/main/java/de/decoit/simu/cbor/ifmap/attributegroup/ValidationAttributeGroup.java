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
package de.decoit.simu.cbor.ifmap.attributegroup;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import de.decoit.simu.cbor.ifmap.enums.IfMapValidationType;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;



/**
 * Java representation of the IF-MAP validationAttributes attribute group.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class ValidationAttributeGroup extends AbstractAttributeGroup {
	public static final String VALIDATION = "validation";

	@Getter
	private IfMapValidationType validationType;


	/**
	 * Create an object with pre-set validation type.
	 * The validation type must not be null, it must be a valid enum value.
	 *
	 * @param validationType IF-MAP validation type
	 */
	public ValidationAttributeGroup(final IfMapValidationType validationType) {
		if(validationType == null) {
			throw new IllegalArgumentException("Validation type must not be null");
		}

		this.validationType = validationType;
	}


	@Override
	public void serializeAttributeGroup(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		try {
			// Serialize 'validation'
			{
				DataItem cborName = this.getAttributeNameMapping(ValidationAttributeGroup.VALIDATION, elementEntry);
				DataItem cborValue = this.getAttributeEnumValueMapping(ValidationAttributeGroup.VALIDATION, this.validationType.getXmlName(), elementEntry);

				builder.add(cborName);
				builder.add(cborValue);
			}
		}
		catch(Exception ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
