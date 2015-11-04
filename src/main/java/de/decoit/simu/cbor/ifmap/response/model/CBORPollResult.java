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
import de.decoit.simu.cbor.ifmap.AbstractNestedElementBase;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.response.model.search.AbstractPollSearchResult;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;



/**
 * Java representation of the IF-MAP poll-result response.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public final class CBORPollResult extends AbstractResult {
	public static final String XML_NAME = "pollResult";
	
	private final List<AbstractNestedElementBase> results;


	/**
	 * Create a new poll result response.
	 */
	public CBORPollResult() {
		super(XML_NAME);

		this.results = new ArrayList<>();
	}


	/**
	 * Add a new error result to this poll result.
	 *
	 * @param result Error result
	 */
	public void addPollResult(CBORErrorResult result) {
		if(result == null) {
			throw new IllegalArgumentException("Error result must not be null");
		}

		this.results.add(result);
	}


	/**
	 * Add a new poll search result to this poll result.
	 *
	 * @param result Poll search result
	 */
	public void addPollResult(AbstractPollSearchResult result) {
		if(result == null) {
			throw new IllegalArgumentException("Poll search result must not be null");
		}

		this.results.add(result);
	}


	/**
	 * Remove an existing error result from this poll result.
	 *
	 * @param result Error result
	 */
	public void removePollResult(CBORErrorResult result) {
		this.results.remove(result);
	}


	/**
	 * Remove an existing poll search result from this poll result.
	 *
	 * @param result Poll search result
	 */
	public void removePollResult(AbstractPollSearchResult result) {
		this.results.remove(result);
	}


	/**
	 * Returns an immutable view of the results list.
	 * The list contains objects of type {@link CBORErrorResult} or
	 * {@link AbstractPollSearchResult}. Other types are not allowed.
	 *
	 * @return Immutable list view
	 */
	public List<AbstractNestedElementBase> getResults() {
		return Collections.unmodifiableList(this.results);
	}


	@Override
	protected void serializeNestedElements(final ArrayBuilder<?> builder, final DictionarySimpleElement elementEntry) throws CBORSerializationException {
		// Serialize poll result items
		for(AbstractNestedElementBase result : this.results) {
			result.cborSerialize(builder, elementEntry);
		}
	}
}
