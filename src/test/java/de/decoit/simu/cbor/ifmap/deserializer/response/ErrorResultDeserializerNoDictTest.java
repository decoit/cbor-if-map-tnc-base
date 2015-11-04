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
package de.decoit.simu.cbor.ifmap.deserializer.response;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import de.decoit.simu.cbor.ifmap.enums.IfMapErrorCode;
import de.decoit.simu.cbor.ifmap.response.model.CBORErrorResult;
import de.decoit.simu.cbor.ifmap.util.IfMapNamespaces;
import de.decoit.simu.cbor.xml.dictionary.DictionaryProvider;
import de.decoit.simu.cbor.xml.dictionary.DictionarySimpleElement;
import java.io.ByteArrayInputStream;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class ErrorResultDeserializerNoDictTest {
	@Test
	public void testDeserialize() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("84F66B6572726F72526573756C7484696572726F72436F64656B537973"
				+ "74656D4572726F72646E616D65667375622D303184F66B6572726F72537472696E67806C637573746F6D2D6572726F72");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);
		
		String dictPath = "<" + IfMapNamespaces.IFMAP + ">response+errorResult";
		DictionarySimpleElement dse = DictionaryProvider.getInstance().findElementByPath(dictPath);
		
		CBORErrorResult result = ErrorResultDeserializer
									.getInstance()
									.deserialize((Array)topLevelArray.getDataItems().get(2), 
												 (Array)topLevelArray.getDataItems().get(3),
												 dse);
		
		assertEquals(IfMapErrorCode.SYSTEM_ERROR, result.getErrorCode());
		assertEquals("sub-01", result.getName());
		assertEquals("custom-error", result.getErrorString());
	}
	
}
