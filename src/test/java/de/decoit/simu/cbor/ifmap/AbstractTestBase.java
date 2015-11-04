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
package de.decoit.simu.cbor.ifmap;

import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import org.junit.AfterClass;
import org.junit.BeforeClass;



/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public abstract class AbstractTestBase {
	@BeforeClass
	public static void testClassSetup() throws URISyntaxException, IOException {
		URI dictFile = ClassLoader.getSystemResource("ifmap-base.dict").toURI();
		DictionaryProvider.getInstance().replaceDictionary(Paths.get(dictFile));

		URI dummyDictFile = ClassLoader.getSystemResource("dummy.dict").toURI();
		DictionaryProvider.getInstance().extendDictionary(Paths.get(dummyDictFile));
	}


	@AfterClass
	public static void testClassTearDown() {
		DictionaryProvider.getInstance().clear();
	}
}
