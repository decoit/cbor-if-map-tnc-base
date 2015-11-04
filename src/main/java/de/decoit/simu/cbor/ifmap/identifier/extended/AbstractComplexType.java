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

import co.nstant.in.cbor.builder.AbstractBuilder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.DoublePrecisionFloat;
import co.nstant.in.cbor.model.NegativeInteger;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;
import de.decoit.simu.cbor.ifmap.AbstractNestedElementBase;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.xml.dictionary.DictionaryComplexElement;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Abstract base class for complex elements used in IF-MAP extended identifiers.
 * A class extending this base class must provide the element name and maps for
 * attributes and simple elements. Simple elements are such that only contain text
 * and no nested elements. Which attributes and simple elements are available has
 * to be defined and managed by the subclass. A complex element may not contain a
 * value and simple or complex elements at the same time. If a value is set, any
 * existing nested elements will be ignored during serialization.
 * 
 * Subclasses should not override the serialization methods defined in this class
 * and use the provided mechanisms instead.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public abstract class AbstractComplexType extends AbstractNestedElementBase {
	protected final Map<String, DataItem> attributes;
	protected final Map<String, DataItem> simpleElements;
	protected final List<AbstractComplexType> complexElements;
	@Getter
	protected DataItem value;


	public AbstractComplexType(String elementName, Map<String, DataItem> attributes, Map<String, DataItem> simpleElements) {
		super(elementName);

		if(attributes == null) {
			throw new IllegalArgumentException("attributes parameter must not be null");
		}

		if(simpleElements == null) {
			throw new IllegalArgumentException("simpleElements parameter must not be null");
		}

		this.attributes = attributes;
		this.simpleElements = simpleElements;
		this.complexElements = new ArrayList<>();
		
		// Tell the superclass that we need the parent builder for nested element serialization
		this.nestedElementProvideParentBuilder = true;
	}


	/**
	 * Add a nested complex element to this complex element.
	 * If a contained value was set before, it will be reset to null while adding a complex element.
	 *
	 * @param element Nested complex element
	 * @return True if adding the element was successful, false otherwise
	 */
	public boolean addComplexElement(AbstractComplexType element) {
		if(element == null) {
			throw new IllegalArgumentException("Cannot add null pointer as complex element");
		}
		
		this.value = null;
		
		return complexElements.add(element);
	}
	
	
	/**
	 * Set the value that is contained by this element.
	 * This will clear any simple or complex element added to this element.
	 * 
	 * @param value Value to set
	 */
	public void setValue(String value) {
		if(StringUtils.isWhitespace(value)) {
			throw new IllegalArgumentException("Value string must not be empty or whitespace only");
		}
		
		if(value == null) {
			setValue((DataItem) null);
		}
		else {
			setValue(new UnicodeString(value));
		}
	}
	
	
	/**
	 * Set the value that is contained by this element.
	 * This will clear any simple or complex element added to this element.
	 * 
	 * @param value Value to set
	 */
	public void setValue(Long value) {
		if(value == null) {
			setValue((DataItem) null);
		}
		else if(value < 0) {
			setValue(new NegativeInteger(value));
		}
		else {
			setValue(new UnsignedInteger(value));
		}
	}
	
	
	/**
	 * Set the value that is contained by this element.
	 * This will clear any simple or complex element added to this element.
	 * 
	 * @param value Value to set
	 */
	public void setValue(Double value) {
		if(value == null) {
			setValue((DataItem) null);
		}
		else {
			setValue(new DoublePrecisionFloat(value));
		}
	}
	
	
	/**
	 * Set the value that is contained by this element.
	 * This will clear any simple or complex element added to this element.
	 * 
	 * @param value Value to set
	 */
	public void setValue(byte[] value) {
		if(value == null) {
			setValue((DataItem) null);
		}
		else {
			if(value.length == 0) {
				throw new IllegalArgumentException("Byte array must not be empty");
			}
			
			setValue(new ByteString(value));
		}
	}
	
	
	/**
	 * Set the value that is contained by this element.
	 * This will clear any simple or complex element added to this element.
	 * 
	 * @param value Value to set
	 */
	public void setValue(Boolean value) {
		if(value == null) {
			setValue((DataItem) null);
		}
		else if(value == true) {
			setValue(new SimpleValue(SimpleValueType.TRUE));
		}
		else {
			setValue(new SimpleValue(SimpleValueType.FALSE));
		}
	}
	
	
	/**
	 * Set the contained value as data item.
	 * This will clear any simple or complex element added to this element.
	 * 
	 * @param value Value as data item
	 */
	private void setValue(DataItem value) {
		if(value != null) {
			this.simpleElements.clear();
			this.complexElements.clear();
		}
		
		this.value = value;
	}


	@Override
	protected void serializeAttributes(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		try {
			// Serialize custom attributes
			for(Map.Entry<String, DataItem> mapEntry : this.attributes.entrySet()) {
				DataItem cborName = this.getAttributeNameMapping(mapEntry.getKey(), elementEntry);
				DataItem cborValue;
				
				DataItem mapValue = mapEntry.getValue();
				if(mapValue instanceof UnicodeString) {
					UnicodeString us = (UnicodeString) mapValue;
					
					cborValue = this.getAttributeEnumValueMapping(mapEntry.getKey(), us.getString(), elementEntry);
				}
				else {
					cborValue = mapValue;
				}
				
				builder.add(cborName);
				builder.add(cborValue);
				
				// TODO: Check for Dictionary Enum-Value Mapping for UnicodeString values
			}
		}
		catch(RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}


	@Override
	protected void serializeNestedElements(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		try {
			if(this.value != null) {
				DataItem cborValue;
				
				if(this.value instanceof UnicodeString) {
					UnicodeString us = (UnicodeString) this.value;
					
					cborValue = this.getElementEnumValueMapping(us.getString(), elementEntry);
				}
				else {
					cborValue = this.value;
				}
				
				builder.add(cborValue);
				
				// TODO: Check for Dictionary Enum-Value Mapping for UnicodeString values
			}
			else {
				ArrayBuilder<?> localBuilder = builder.addArray();
				
				if(this.simpleElements.size() > 0 || this.complexElements.size() > 0) {
					for(Map.Entry<String, DataItem> mapEntry : this.simpleElements.entrySet()) {
						DataItem cborName = this.getNestedElementNameMapping(mapEntry.getKey(), elementEntry);
						DataItem cborValue;

						DataItem mapValue = mapEntry.getValue();
						if(mapValue instanceof UnicodeString) {
							UnicodeString us = (UnicodeString) mapValue;

							cborValue = this.getNestedElementEnumValueMapping(mapEntry.getKey(), us.getString(), elementEntry);
						}
						else {
							cborValue = mapValue;
						}

						localBuilder.add(new SimpleValue(SimpleValueType.NULL));
						localBuilder.add(cborName);
						localBuilder.addArray();
						localBuilder.add(cborValue);
					}

					if(!this.complexElements.isEmpty()) {
						if(elementEntry instanceof DictionaryComplexElement) {
							DictionaryComplexElement complexElementEntry = (DictionaryComplexElement) elementEntry;

							for(AbstractComplexType act : this.complexElements) {
								act.cborSerialize(localBuilder, complexElementEntry);
							}
						}
						else if(elementEntry == null) {
							for(AbstractComplexType act : this.complexElements) {
								act.cborSerialize(localBuilder, null);
							}
						}
						else {
							throw new CBORSerializationException("Complex nested elements on extended identifier with non-complex dictionary entry");
						}
					}
				}
			}
		}
		catch(RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
