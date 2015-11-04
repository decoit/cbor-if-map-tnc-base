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

import de.decoit.simu.cbor.xml.dictionary.DictionaryComplexElement;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleAttribute;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import de.decoit.simu.cbor.xml.dictionary.exception.DictionaryPathException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * This class provides untility methods to evaluate a nested dictionary path on a dictionary entry.
 * A nested dictionary path does not define a namespace to start from. Instead it starts at a provided entry and evaluates the
 * path from there on. It may look like these:<br>
 * - ELEMENTNAME<br>
 * - ELEMENTNAME@ATTRIBUTENAME<br>
 * - ELEMENTNAME_1+ELEMENTNAME_2<br>
 * - ELEMENTNAME_1+ELEMENTNAME_2@ATTRIBUTENAME
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class DictionaryHelper {
	private static final Pattern NESTED_PATH_PATTERN = Pattern.compile("^([a-zA-Z_][\\w-.]*(?:\\+[a-zA-Z_][\\w-.]*)*)(?:@([a-zA-Z_:][-a-zA-Z0-9_:.]*))?$");


	public static DictionarySimpleElement findNestedElement(final String path, final DictionarySimpleElement fromElement) throws DictionaryPathException {
		if(path == null) {
			throw new DictionaryPathException("Null reference for dictionary path");
		}

		Matcher m = DictionaryHelper.NESTED_PATH_PATTERN.matcher(path);

		// Test if provided path is valid
		if(m.matches()) {
			String[] elements = m.group(1).split("\\+");

			// Only continue if provided start element is complex, otherwise return null
			if(fromElement instanceof DictionaryComplexElement) {
				DictionaryComplexElement fromComplexElement = (DictionaryComplexElement) fromElement;

				// Read the first element
				DictionarySimpleElement eEntry = fromComplexElement.lookupNestedElement(elements[0]);

				// Iterate over the remaining elements
				for(int i=1; i<elements.length; i++) {
					// If previous entry was a complex element, read the next and continue. Otherwise return null.
					if(eEntry instanceof DictionaryComplexElement) {
						DictionaryComplexElement complexEntry = (DictionaryComplexElement) eEntry;

						eEntry = complexEntry.lookupNestedElement(elements[i]);
					}
					else {
						return null;
					}
				}

				// Return the last element entry
				// Will be the target element if it exists in the dictionary. Otherwise it is null.
				return eEntry;
			}
			else {
				return null;
			}
		}
		else {
			throw new DictionaryPathException("Cannot evaluate dictionary path: " + path);
		}
	}


	public static DictionarySimpleAttribute findAttributeOfElement(final String path, final DictionarySimpleElement fromElement) throws DictionaryPathException {
		DictionarySimpleElement targetElement = DictionaryHelper.findNestedElement(path, fromElement);

		if(targetElement != null) {
			Matcher m = DictionaryHelper.NESTED_PATH_PATTERN.matcher(path);

			// Test if provided path is valid (it should be at this point) and fill groups
			if(m.matches()) {
				String attribute = m.group(2);

				if(attribute != null) {
					return targetElement.lookupAttribute(attribute);
				}
				else {
					throw new DictionaryPathException("Path specifies no target attribute: " + path);
				}
			}
		}

		return null;
	}


	protected DictionaryHelper() { }
}
