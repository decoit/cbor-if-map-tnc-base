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

import de.decoit.simu.cbor.ifmap.enums.IfMapCardinality;
import de.decoit.simu.cbor.ifmap.metadata.AbstractMetadata;
import java.time.ZonedDateTime;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;



/**
 * Abstract base class for any metadata with cardinality multiValue.
 * Concrete multi value metadata classes MUST extend this class instead
 * of extending {@link AbstractMetadata} directly.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public abstract class AbstractMultiValueMetadata extends AbstractMetadata {
	/**
	 * Create a new multi value metadata object with the specified information.
	 * This constructor should be used for metadata that is sent from the SERVER to the CLIENT. It includes
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamp is stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC.
	 *
	 * @param namespace
	 * @param elementName Metadata element name
	 * @param ifMapPublisherId IF-MAP client publisher ID
	 * @param ifMapTimestamp Timestamp for the metadata
	 */
	public AbstractMultiValueMetadata(String namespace, String elementName, String ifMapPublisherId, ZonedDateTime ifMapTimestamp) {
		super(namespace, elementName, ifMapPublisherId, ifMapTimestamp, IfMapCardinality.MULTI_VALUE);
	}


	/**
	 * Create a new multi value metadata object with the specified information.
	 * This constructor should be used for metadata that is sent from the CLIENT to the SERVER. It does not include
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamp is stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC.
	 *
	 * @param namespace
	 * @param elementName Metadata element name
	 */
	public AbstractMultiValueMetadata(String namespace, String elementName) {
		super(namespace, elementName, IfMapCardinality.MULTI_VALUE);
	}
}
