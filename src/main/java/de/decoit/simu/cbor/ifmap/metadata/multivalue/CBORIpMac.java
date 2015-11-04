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
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.ifmap.util.TimestampHelper;
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
 * Java representation of the IF-MAP ip-mac metadata.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class CBORIpMac extends AbstractMultiValueMetadata {
	public static final String XML_NAME = "ip-mac";
	public static final String START_TIME = "start-time";
	public static final String END_TIME = "end-time";
	public static final String DHCP_SERVER = "dhcp-server";

	@Getter
	private ZonedDateTime startTime;
	@Getter
	private ZonedDateTime endTime;
	@Getter
	private String dhcpServer;


	/**
	 * Create a new ip-mac metadata.
	 * This constructor should be used for metadata that is sent from the SERVER to the CLIENT. It includes
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamps are stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.
	 *
	 * @param ifMapPublisherId IF-MAP client publisher ID
	 * @param ifMapTimestamp Timestamp for the metadata
	 */
	public CBORIpMac(String ifMapPublisherId, ZonedDateTime ifMapTimestamp) {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME, ifMapPublisherId, ifMapTimestamp);

		this.startTime = null;
		this.endTime = null;
		this.dhcpServer = null;
	}


	/**
	 * Create a new ip-mac metadata.
	 * This constructor should be used for metadata that is sent from the CLIENT to the SERVER. It does not include
	 * the attributes ifmap-publisher-id, ifmap-timestamp and ifmap-timestamp-fraction.
	 * The timestamps are stored with UTC timezone. If a timestamp with another timezone is
	 * passed into the constructor, it will be converted to UTC. The IF-MAP timestamp fraction
	 * element will be calculated from the provided timestamp.
	 */
	public CBORIpMac() {
		super(IfMapNamespaces.IFMAP_METADATA, XML_NAME);

		this.startTime = null;
		this.endTime = null;
		this.dhcpServer = null;
	}


	/**
	 * Set the start time for this metadata.
	 * The start time may be null to remove this value from the metadata. The timestamps are
	 * stored with UTC timezone. If a timestamp with another timezone is passed, it will be
	 * converted to UTC.
	 *
	 * @param startTime Start time of DHCP lease
	 */
	public void setStartTime(ZonedDateTime startTime) {
		if(startTime != null) {
			startTime = TimestampHelper.toUTC(startTime);
		}

		this.startTime = startTime;
	}


	/**
	 * Set the end time for this metadata.
	 * The end time may be null to remove this value from the metadata. The timestamps are
	 * stored with UTC timezone. If a timestamp with another timezone is passed, it will be
	 * converted to UTC.
	 *
	 * @param endTime End time of DHCP lease
	 */
	public void setEndTime(ZonedDateTime endTime) {
		if(endTime != null) {
			endTime = TimestampHelper.toUTC(endTime);
		}

		this.endTime = endTime;
	}


	/**
	 * Set the dhcp-server for this metadata.
	 * The dhcp-server may be null to remove this value from the metadata. If not null,
	 * the dhcp-server MUST NOT be an empty string or whitespace only.
	 *
	 * @param dhcpServer dhcp-server for this metadata
	 */
	public void setDhcpServer(String dhcpServer) {
		if(StringUtils.isWhitespace(dhcpServer)) {
			throw new IllegalArgumentException("DHCP server must not be empty or whitespace only");
		}

		this.dhcpServer = dhcpServer;
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

			// Serialize 'start-time'
			if(this.startTime != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORIpMac.START_TIME, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(TimestampHelper.toEpochTimeDataItem(this.startTime));
			}


			// Serialize 'end-time'
			if(this.endTime != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORIpMac.END_TIME, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(TimestampHelper.toEpochTimeDataItem(this.endTime));
			}


			// Serialize 'dhcp-server'
			if(this.dhcpServer != null) {
				DataItem cborName = this.getNestedElementNameMapping(CBORIpMac.DHCP_SERVER, elementEntry);

				builder.add(new SimpleValue(SimpleValueType.NULL));
				builder.add(cborName);
				builder.addArray();
				builder.add(new UnicodeString(this.dhcpServer));
			}
		}
		catch(DictionaryPathException | RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
