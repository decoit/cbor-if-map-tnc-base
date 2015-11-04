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
package de.decoit.simu.cbor.ifmap.response.model.search;

import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.UnicodeString;
import de.decoit.simu.cbor.ifmap.AbstractNestedElementBase;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;



/**
 * Abstract base class for search and poll result responses.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class SearchResult extends AbstractNestedElementBase {
	public static final String XML_NAME = "searchResult";
	public static final String NAME = "name";

	@Getter
	private String name;
	private final List<SearchResultItem> resultItems;


	/**
	 * Create a new search result.
	 */
	public SearchResult() {
		this(XML_NAME);
	}


	SearchResult(String elementName) {
		super(elementName);

		this.resultItems = new ArrayList<>();
		this.name = null;
	}


	/**
	 * Set the name of this search result response.
	 * The name may be null to remove the name from this response.
	 * If not null, the name MUST NOT be empty or whitespace only.
	 *
	 * @param name Search result name
	 */
	public void setName(String name) {
		if(StringUtils.isWhitespace(name)) {
			throw new IllegalArgumentException("Name must not be empty or whitespace only");
		}

		this.name = name;
	}


	/**
	 * Add a new search result item to this result.
	 *
	 * @param item Search result item to add
	 */
	public void addSearchResultItem(SearchResultItem item) {
		if(item == null) {
			throw new IllegalArgumentException("Search result item must not be null");
		}

		this.resultItems.add(item);
	}


	/**
	 * Remove an exising search result item from this result.
	 *
	 * @param item Search result item to remove
	 */
	public void removeSearchResultItem(SearchResultItem item) {
		this.resultItems.remove(item);
	}


	/**
	 * Returns an immutable view of the result items list.
	 *
	 * @return Immutable list view
	 */
	public List<SearchResultItem> getResultItems() {
		return Collections.unmodifiableList(this.resultItems);
	}


	@Override
	protected void serializeAttributes(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		try {
			// Serialize 'name'
			if(this.name != null) {
				DataItem cborName = this.getAttributeNameMapping(SearchResult.NAME, elementEntry);

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
		try {
			// Serialize search result items
			for(SearchResultItem sri : resultItems) {
				sri.cborSerialize(builder, elementEntry);
			}
		}
		catch(RuntimeException ex) {
			throw new CBORSerializationException("Error during serialization, see nested exception for details", ex);
		}
	}
}
