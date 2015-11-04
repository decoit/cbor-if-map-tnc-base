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
import co.nstant.in.cbor.model.UnicodeString;
import co.nstant.in.cbor.model.UnsignedInteger;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Java representation of the IF-MAP search type.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@Getter
public final class SearchTypeAttributeGroup extends AbstractAttributeGroup {
	public static final String MATCH_LINKS = "match-links";
	public static final String MAX_DEPTH = "max-depth";
	public static final String MAX_SIZE = "max-size";
	public static final String RESULT_FILTER = "result-filter";
	public static final String TERMINAL_IDENTIFIER_TYPE = "terminal-identifier-type";

	private String matchLinks;
	private Integer maxDepth;
	private Integer maxSize;
	private String resultFilter;
	private String terminalIdentifierType;


	/**
	 * Createa new search for the specified identifier.
	 */
	public SearchTypeAttributeGroup() {
		this.matchLinks = null;
		this.maxDepth = null;
		this.maxSize = null;
		this.resultFilter = null;
		this.terminalIdentifierType = null;
	}


	/**
	 * Add a filter to match metadata on links to the identifier.
	 * The string may be null to remove this attribute from the search. If not null,
	 * the filter string MUST NOT be empty or whitespace only.
	 *
	 * @param matchLinks Filter string
	 */
	public void setMatchLinks(String matchLinks) {
		if(StringUtils.isWhitespace(matchLinks)) {
			throw new IllegalArgumentException("Match links must not be empty or whitespace only");
		}

		this.matchLinks = matchLinks;
	}


	/**
	 * Set the maximum search depth for this search.
	 *
	 * @param maxDepth Maximum depth, must be positive or 0
	 */
	public void setMaxDepth(Integer maxDepth) {
		if(maxDepth != null) {
			if(maxDepth < 0) {
				throw new IllegalArgumentException("Max depth must be positive or 0");
			}
		}

		this.maxDepth = maxDepth;
	}


	/**
	 * Set the maximum result size for this search.
	 *
	 * @param maxSize Maximum size, must be positive or 0
	 */
	public void setMaxSize(Integer maxSize) {
		if(maxSize != null) {
			if(maxSize < 1) {
				throw new IllegalArgumentException("Max size must be positive");
			}
		}

		this.maxSize = maxSize;
	}


	/**
	 * Set a result filter for this search.
	 * The string may be null to remove this attribute from the search. If not null,
	 * the filter string MUST NOT be empty or whitespace only.
	 *
	 * @param resultFilter Filter string
	 */
	public void setResultFilter(String resultFilter) {
		if(StringUtils.isWhitespace(resultFilter)) {
			throw new IllegalArgumentException("Result filter must not be empty or whitespace only");
		}

		this.resultFilter = resultFilter;
	}


	/**
	 * Add the identifier type which terminates the search.
	 * The string may be null to remove this attribute from the search. If not null,
	 * the identifier type MUST NOT be empty or whitespace only.
	 *
	 * @param terminalIdentifierType
	 */
	public void setTerminalIdentifierType(String terminalIdentifierType) {
		if(StringUtils.isWhitespace(terminalIdentifierType)) {
			throw new IllegalArgumentException("Terminal identifier type must not be empty or whitespace only");
		}

		this.terminalIdentifierType = terminalIdentifierType;
	}


	@Override
	public void serializeAttributeGroup(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		try {
			// Serialize 'match-links'
			if(this.matchLinks != null) {
				DataItem cborName = this.getAttributeNameMapping(SearchTypeAttributeGroup.MATCH_LINKS, elementEntry);

				builder.add(cborName);
				builder.add(new UnicodeString(this.matchLinks));
			}


			// Serialize 'max-depth'
			if(this.maxDepth != null) {
				DataItem cborName = this.getAttributeNameMapping(SearchTypeAttributeGroup.MAX_DEPTH, elementEntry);

				builder.add(cborName);
				builder.add(new UnsignedInteger(this.maxDepth));
			}


			// Serialize 'max-size'
			if(this.maxSize != null) {
				DataItem cborName = this.getAttributeNameMapping(SearchTypeAttributeGroup.MAX_SIZE, elementEntry);

				builder.add(cborName);
				builder.add(new UnsignedInteger(this.maxSize));
			}


			// Serialize 'result-filter'
			if(this.resultFilter != null) {
				DataItem cborName = this.getAttributeNameMapping(SearchTypeAttributeGroup.RESULT_FILTER, elementEntry);

				builder.add(cborName);
				builder.add(new UnicodeString(this.resultFilter));
			}


			// Serialize 'terminal-identifier-type'
			if(this.terminalIdentifierType != null) {
				DataItem cborName = this.getAttributeNameMapping(SearchTypeAttributeGroup.TERMINAL_IDENTIFIER_TYPE, elementEntry);

				builder.add(cborName);
				builder.add(new UnicodeString(this.terminalIdentifierType));
			}
		}
		catch(Exception ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
