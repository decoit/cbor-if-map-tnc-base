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
package de.decoit.simu.cbor.ifmap.deserializer;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import de.decoit.simu.cbor.ifmap.identifier.extended.CBORExtendedIfMapServer;
import java.io.ByteArrayInputStream;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class ExtendedIdentifierDeserializerManagerNoDictTest {
	@Test
	public void testDeserialize_CBORExtendedIfMapServer() throws Exception {
		byte[] input = DatatypeConverter.parseHexBinary("847838687474703A2F2F7777772E74727573746564636F6D707574696E6767"
				+ "726F75702E6F72672F323031332F49464D41502D5345525645522F31D9A4106C69666D61702D736572766572827561646D69"
				+ "6E6973747261746976652D646F6D61696E782269666D61702D7365727665723A61646D696E6973747261746976652D646F6D"
				+ "61696E80");

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		CborDecoder cd = new CborDecoder(bis);
		List<DataItem> diList = cd.decode();
		Array topLevelArray = (Array) diList.get(0);

		CBORExtendedIfMapServer result = ExtendedIdentifierDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), topLevelArray.getDataItems().get(1), (Array)topLevelArray.getDataItems().get(2), (Array)topLevelArray.getDataItems().get(3), CBORExtendedIfMapServer.class);
		assertNotNull(result);
		assertEquals("ifmap-server:administrative-domain", result.getAdministrativeDomain());
	}
}
