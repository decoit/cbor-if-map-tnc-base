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
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class SearchTypeAttributeGroupTest {
	private final String matchLinks = "my-match-links";
	private final Integer maxDepth = 5;
	private final Integer maxSize = 10000000;
	private final String resultFilter = "my-result-filter";
	private final String terminalIdentifierType = "my-terminal-identifier-type";


	@Test
	public void testConstructor() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();

		assertNull(instance.getMatchLinks());
		assertNull(instance.getMaxDepth());
		assertNull(instance.getMaxSize());
		assertNull(instance.getResultFilter());
		assertNull(instance.getTerminalIdentifierType());
	}


	@Test
	public void testSetMatchLinks() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setMatchLinks(this.matchLinks);

		assertEquals(this.matchLinks, instance.getMatchLinks());
	}


	@Test
	public void testSetMatchLinks_null() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setMatchLinks(this.matchLinks);
		instance.setMatchLinks(null);

		assertNull(instance.getMatchLinks());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetMatchLinks_EmptyString() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setMatchLinks("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetMatchLinks_Whitespaces() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setMatchLinks("   ");
	}


	@Test
	public void testSetMaxDepth() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setMaxDepth(this.maxDepth);

		assertEquals(this.maxDepth, instance.getMaxDepth());
	}


	@Test
	public void testSetMaxDepth_null() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setMaxDepth(this.maxDepth);
		instance.setMaxDepth(null);

		assertNull(instance.getMaxDepth());
	}


	@Test
	public void testSetMaxSize() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setMaxSize(this.maxSize);

		assertEquals(this.maxSize, instance.getMaxSize());
	}


	@Test
	public void testSetMaxSize_null() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setMaxSize(this.maxSize);
		instance.setMaxSize(null);

		assertNull(instance.getMaxSize());
	}


	@Test
	public void testSetResultFilter() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setResultFilter(this.resultFilter);

		assertEquals(this.resultFilter, instance.getResultFilter());
	}


	@Test
	public void testSetResultFilter_null() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setResultFilter(this.resultFilter);
		instance.setResultFilter(null);

		assertNull(instance.getResultFilter());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetResultFilter_EmptyString() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setResultFilter("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetResultFilter_Whitespaces() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setResultFilter("   ");
	}


	@Test
	public void testSetTerminalIdentifierType() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setTerminalIdentifierType(this.terminalIdentifierType);

		assertEquals(this.terminalIdentifierType, instance.getTerminalIdentifierType());
	}


	@Test
	public void testSetTerminalIdentifierType_null() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setTerminalIdentifierType(this.terminalIdentifierType);
		instance.setTerminalIdentifierType(null);

		assertNull(instance.getTerminalIdentifierType());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetTerminalIdentifierType_EmptyString() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setTerminalIdentifierType("");
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetTerminalIdentifierType_Whitespaces() {
		SearchTypeAttributeGroup instance = new SearchTypeAttributeGroup();
		instance.setTerminalIdentifierType("   ");
	}
}
