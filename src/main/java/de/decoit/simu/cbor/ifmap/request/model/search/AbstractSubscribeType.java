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
package de.decoit.simu.cbor.ifmap.request.model.search;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnicodeString;
import de.decoit.simu.cbor.ifmap.AbstractNestedElementBase;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Abstract base class for all IF-MAP subscription types.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public abstract class AbstractSubscribeType extends AbstractNestedElementBase {
	public static final String NAME = "name";

	@Getter
	private final String name;


	/**
	 * Create a new subscribe type.
	 *
	 * @param elementName Element name to use during serialization
	 * @param name Subsciption name
	 */
	public AbstractSubscribeType(String elementName, String name) {
		super(elementName);

		if(StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Subscription name must not be blank");
		}

		this.name = name;
	}


	@Override
	protected void serializeAttributes(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		try {
			DataItem cborName = this.getAttributeNameMapping(AbstractSubscribeType.NAME, elementEntry);

			builder.add(cborName);
			builder.add(new UnicodeString(this.name));
		}
		catch(Exception ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
