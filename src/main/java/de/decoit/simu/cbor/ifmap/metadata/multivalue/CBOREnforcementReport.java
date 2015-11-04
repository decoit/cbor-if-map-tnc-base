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
package de.decoit.simu.cbor.ifmap.metadata.multivalue;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SimpleValueType;
import co.nstant.in.cbor.model.UnicodeString;
import de.decoit.simu.cbor.ifmap.enums.IfMapEnforcementAction;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.time.ZonedDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of the IF-MAP enforcement-report metadata.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class CBOREnforcementReport extends AbstractMultiValueMetadata {
	public static final String XML_NAME = "enforcement-report";
	public static final String ENFORCEMENT_ACTION = "enforcement-action";
	public static final String OTHER_TYPE_DEFINITION = "other-type-definition";
	public static final String ENFORCEMENT_REASON = "enforcement-reason";

	@Getter
	private final IfMapEnforcementAction enforcementAction;
	@Getter
	private String otherTypeDefinition;
	@Getter
	private String enforcementReason;


	/**
	 * Create a new enforcement-report metadata with the specified information.
	 * This constructor should be used for metadata that is sent from the SERVER to the CLIENT. It includes
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamps are stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * When using IfMapIdentityType.OTHER as type this constructor will raise an
	 * exception because a value for other-type-definition is required in that case.
	 *
	 * @param ifMapPublisherId IF-MAP client publisher ID
	 * @param ifMapTimestamp Timestamp for the metadata
	 * @param enforcementAction Enforcement action executed
	 */
	public CBOREnforcementReport(String ifMapPublisherId, ZonedDateTime ifMapTimestamp, IfMapEnforcementAction enforcementAction) {
		this(ifMapPublisherId, ifMapTimestamp, enforcementAction, null);
	}


	/**
	 * Create a new enforcement-report metadata with the specified information.
	 * This constructor should be used for metadata that is sent from the SERVER to the CLIENT. It includes
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamps are stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * The other-type-definition parameter is required to be not blank if enforcementAction
	 * is IfMapEnforcementAction.OTHER. For all other types the other-type-definition value
	 * will be discarded and replaced by null.
	 *
	 * @param ifMapPublisherId IF-MAP client publisher ID
	 * @param ifMapTimestamp Timestamp for the metadata
	 * @param enforcementAction Enforcement action executed
	 * @param otherTypeDefinition Other type definition if applicable, may be null
	 */
	public CBOREnforcementReport(String ifMapPublisherId, ZonedDateTime ifMapTimestamp, IfMapEnforcementAction enforcementAction, String otherTypeDefinition) {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME, ifMapPublisherId, ifMapTimestamp);

		if(enforcementAction == null) {
			throw new IllegalArgumentException("Enforcement action must not be null");
		}

		if(enforcementAction == IfMapEnforcementAction.OTHER && StringUtils.isBlank(otherTypeDefinition)) {
			throw new IllegalArgumentException("other-type-definition must not be blank if type is OTHER");
		}
		// No other type definition is required if type is different from OTHER
		else if(enforcementAction != IfMapEnforcementAction.OTHER) {
			otherTypeDefinition = null;
		}

		this.enforcementAction = enforcementAction;
		this.otherTypeDefinition = otherTypeDefinition;
		this.enforcementReason = null;
	}


	/**
	 * Create a new enforcement-report metadata with the specified information.
	 * This constructor should be used for metadata that is sent from the CLIENT to the SERVER. It does not include
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamps are stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * When using IfMapIdentityType.OTHER as type this constructor will raise an
	 * exception because a value for other-type-definition is required in that case.
	 *
	 * @param enforcementAction Enforcement action executed
	 */
	public CBOREnforcementReport(IfMapEnforcementAction enforcementAction) {
		this(enforcementAction, null);
	}


	/**
	 * Create a new enforcement-report metadata with the specified information.
	 * This constructor should be used for metadata that is sent from the CLIENT to the SERVER. It does not include
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamps are stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.<br>
	 * The other-type-definition parameter is required to be not blank if enforcementAction
	 * is IfMapEnforcementAction.OTHER. For all other types the other-type-definition value
	 * will be discarded and replaced by null.
	 *
	 * @param enforcementAction Enforcement action executed
	 * @param otherTypeDefinition Other type definition if applicable, may be null
	 */
	public CBOREnforcementReport(IfMapEnforcementAction enforcementAction, String otherTypeDefinition) {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME);

		if(enforcementAction == null) {
			throw new IllegalArgumentException("Enforcement action must not be null");
		}

		if(enforcementAction == IfMapEnforcementAction.OTHER && StringUtils.isBlank(otherTypeDefinition)) {
			throw new IllegalArgumentException("other-type-definition must not be blank if type is OTHER");
		}
		// No other type definition is required if type is different from OTHER
		else if(enforcementAction != IfMapEnforcementAction.OTHER) {
			otherTypeDefinition = null;
		}

		this.enforcementAction = enforcementAction;
		this.otherTypeDefinition = otherTypeDefinition;
		this.enforcementReason = null;
	}


	/**
	 * Set the enforcement-reason for this metadata.
	 * The enforcement-reason may be null to remove this value from the metadata. If not null,
	 * the enforcement-reason MUST NOT be an empty string or whitespace only.
	 *
	 * @param enforcementReason enforcement-reason for this metadata
	 */
	public void setEnforcementReason(String enforcementReason) {
		if(StringUtils.isWhitespace(enforcementReason)) {
			throw new IllegalArgumentException("administrative-domain must not be empty or whitespace only");
		}

		this.enforcementReason = enforcementReason;
	}


	@Override
	protected void serializeNestedElements(final ArrayBuilder<?> builder) throws CBORSerializationException {
		try {
			// Build dictionary path to this element
			StringBuilder dictPathSb = new StringBuilder("<");
			dictPathSb.append(this.namespace);
			dictPathSb.append(">");
			dictPathSb.append(this.elementName);

			// Get dictionary entry for this element
			DictionarySimpleElement elementEntry = DictionaryProvider.getInstance().findElementByPath(dictPathSb.toString());

			// Serialize 'enforcement-action'
			{
				DataItem cborName = this.getNestedElementNameMapping(CBOREnforcementReport.ENFORCEMENT_ACTION, elementEntry);
				DataItem cborValue = this.getNestedElementEnumValueMapping(CBOREnforcementReport.ENFORCEMENT_ACTION, this.enforcementAction.getXmlName(), elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(cborValue);
			}


			// Serialize 'other-type-definition'
			if(this.otherTypeDefinition != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBOREnforcementReport.OTHER_TYPE_DEFINITION, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.otherTypeDefinition));
			}


			// Serialize 'enforcement-reason'
			if(this.enforcementReason != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBOREnforcementReport.ENFORCEMENT_REASON, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.enforcementReason));
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
