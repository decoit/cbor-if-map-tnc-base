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
package de.decoit.simu.cbor.ifmap.util;

import de.decoit.simu.cbor.ifmap.AbstractTestBase;
import de.decoit.simu.cbor.xml.dictionary.Dictionary;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleAttribute;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class DictionaryHelperTest extends AbstractTestBase {
	@Test
	public void testFindNestedElement() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleElement result = DictionaryHelper.findNestedElement("update+metadata", fromElement);

		assertNotNull(result);
		assertEquals("metadata", result.getXmlName());
	}


	@Test(expected = DictionaryPathException.class)
	public void testFindNestedElement_null_Element() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleElement result = DictionaryHelper.findNestedElement(null, fromElement);
	}


	@Test(expected = DictionaryPathException.class)
	public void testFindNestedElement_EmptyString_Element() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleElement result = DictionaryHelper.findNestedElement("", fromElement);
	}


	@Test(expected = DictionaryPathException.class)
	public void testFindNestedElement_Whitespaces_Element() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleElement result = DictionaryHelper.findNestedElement("   ", fromElement);
	}


	@Test
	public void testFindNestedElement_String_null() throws Exception {
		DictionarySimpleElement result = DictionaryHelper.findNestedElement("update+metadata", null);

		assertNull(result);
	}


	@Test
	public void testFindNestedElement_NotExistentTargetElement() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleElement result = DictionaryHelper.findNestedElement("update+my-element", fromElement);

		assertNull(result);
	}


	@Test
	public void testFindNestedElement_NotExistentPathElement() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleElement result = DictionaryHelper.findNestedElement("my-update+metadata", fromElement);

		assertNull(result);
	}


	@Test(expected = DictionaryPathException.class)
	public void testFindNestedElement_MalformedPath() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleElement result = DictionaryHelper.findNestedElement("update@attr+my-element", fromElement);
	}


	@Test
	public void testFindAttributeOfElement() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleAttribute result = DictionaryHelper.findAttributeOfElement("update@lifetime", fromElement);

		assertNotNull(result);
		assertEquals("lifetime", result.getXmlName());
	}


	@Test(expected = DictionaryPathException.class)
	public void testFindAttributeOfElement_null_Element() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleAttribute result = DictionaryHelper.findAttributeOfElement(null, fromElement);
	}


	@Test(expected = DictionaryPathException.class)
	public void testFindAttributeOfElement_EmptyString_Element() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleAttribute result = DictionaryHelper.findAttributeOfElement("", fromElement);
	}


	@Test(expected = DictionaryPathException.class)
	public void testFindAttributeOfElement_Whitespaces_Element() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleAttribute result = DictionaryHelper.findAttributeOfElement("   ", fromElement);
	}


	@Test
	public void testFindAttributeOfElement_String_null() throws Exception {
		DictionarySimpleAttribute result = DictionaryHelper.findAttributeOfElement("update@lifetime", null);

		assertNull(result);
	}


	@Test
	public void testFindAttributeOfElement_NotExistentTargetElement() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleAttribute result = DictionaryHelper.findAttributeOfElement("update+my-element@myattr", fromElement);

		assertNull(result);
	}


	@Test
	public void testFindAttributeOfElement_NotExistentPathElement() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleAttribute result = DictionaryHelper.findAttributeOfElement("my-update+metadata@myattr", fromElement);

		assertNull(result);
	}


	@Test
	public void testFindAttributeOfElement_NotExistentAttribute() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleAttribute result = DictionaryHelper.findAttributeOfElement("update@myattr", fromElement);

		assertNull(result);
	}


	@Test(expected = DictionaryPathException.class)
	public void testFindAttributeOfElement_MalformedPath() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleAttribute result = DictionaryHelper.findAttributeOfElement("update@attr+my-element", fromElement);
	}


	@Test(expected = DictionaryPathException.class)
	public void testFindAttributeOfElement_MissingAttribute() throws Exception {
		Dictionary dict = DictionaryProvider.getInstance();

		DictionarySimpleElement fromElement = dict.findElementByPath("<http://www.trustedcomputinggroup.org/2010/IFMAP/2>publish");

		DictionarySimpleAttribute result = DictionaryHelper.findAttributeOfElement("update+metadata", fromElement);
	}
	
	
	@Test
	public void makeJaCoCoHappy() {
		DictionaryHelperExt instance = new DictionaryHelperExt();
	}
	
	
	private static class DictionaryHelperExt extends DictionaryHelper {}
}
