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
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.response.model.search.SearchResult;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;



/**
 * Java representation of the IF-MAP search-result response.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORSearchResult extends AbstractResult {
	public static final String XML_NAME = "searchResult";
	public static final String NAME = "name";
	
	@Getter
	private final SearchResult result;


	/**
	 * Create a new search result.
	 *
	 * @param result Result content
	 */
	public CBORSearchResult(SearchResult result) {
		super(XML_NAME);

		if(result == null) {
			throw new IllegalArgumentException("Search result must not be null");
		}

		this.result = result;
	}


	@Override
	public void cborSerialize(final ArrayBuilder<?> builder, final DictionarySimpleElement parentElementEntry) throws CBORSerializationException {
		// Redirect serialization to the search result class
		this.result.cborSerialize(builder, parentElementEntry);
	}
}
