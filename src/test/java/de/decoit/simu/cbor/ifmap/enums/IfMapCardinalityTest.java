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
package de.decoit.simu.cbor.ifmap.enums;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class IfMapCardinalityTest {
	@Test
	public void testFromXmlName_SingleValue() {
		IfMapCardinality result = IfMapCardinality.fromXmlName("singleValue");
		assertEquals(IfMapCardinality.SINGLE_VALUE, result);
	}


	@Test
	public void testFromXmlName_MultiValue() {
		IfMapCardinality result = IfMapCardinality.fromXmlName("multiValue");
		assertEquals(IfMapCardinality.MULTI_VALUE, result);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXmlName_null() {
		IfMapCardinality result = IfMapCardinality.fromXmlName(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFromXmlName_UnknownXmlName() {
		IfMapCardinality result = IfMapCardinality.fromXmlName("UnknownXmlName");
	}
}
