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
package de.decoit.simu.cbor.ifmap.request;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.enums.IfMapValidationType;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.identifier.CBORDevice;
import de.decoit.simu.cbor.ifmap.identifier.CBORIpAddress;
import de.decoit.simu.cbor.ifmap.metadata.singlevalue.CBORDeviceIp;
import de.decoit.simu.cbor.ifmap.request.model.publish.CBORPublishDelete;
import de.decoit.simu.cbor.ifmap.request.model.publish.CBORPublishNotify;
import de.decoit.simu.cbor.ifmap.request.model.publish.CBORPublishUpdate;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.util.Arrays;
import javax.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Slf4j
public class CBORPublishRequestNoDictTest {
	private final CBORPublishUpdate pu;
	private final CBORPublishNotify pn;
	private final CBORPublishDelete pd;
	private final String sessionId = "my-session-id";


	public CBORPublishRequestNoDictTest() throws Exception {
		CBORDevice dev = new CBORDevice("server01", false);
		CBORIpAddress ipAddr = new CBORIpAddress(InetAddress.getByName("10.10.100.17"));
		CBORDeviceIp devIpMeta = new CBORDeviceIp();

		this.pu = new CBORPublishUpdate(dev, ipAddr);
		this.pu.addMetadata(devIpMeta);

		this.pn = new CBORPublishNotify(dev, ipAddr);
		this.pn.addMetadata(devIpMeta);

		this.pd = new CBORPublishDelete(dev, ipAddr);
	}


	@Test
	public void testCborSerialize_OnePublishType() throws Exception {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);
		instance.addPublishType(this.pu);
		instance.setValidation(IfMapValidationType.ALL);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (1 type):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41502F32677075626C697368846A73657373696F6E2D69646D6D792D73657373696F6E2D69646A76616C69646174696F6E63416C6C84F6667570646174"
				+ "6582686C69666574696D656773657373696F6E8C7831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49"
				+ "464D41502F32666465766963658084F6646E616D65806873657276657230317831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F7570"
				+ "2E6F72672F323031302F49464D41502F326A69702D61646472657373846576616C7565D9A411440A0A64116474797065644950763480F6686D6574616461746180"
				+ "847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41504D455441444154412F326964657669"
				+ "63652D6970827169666D61702D63617264696E616C6974796B73696E676C6556616C756580");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test
	public void testCborSerialize_ThreePublishTypes() throws Exception {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);
		instance.addPublishType(this.pu);
		instance.addPublishType(this.pd);
		instance.addPublishType(this.pn);
		instance.setValidation(IfMapValidationType.ALL);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);

		ab.end();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		CborEncoder ce = new CborEncoder(bos);
		ce.encode(cb.build());

		log.info("CBOR serialize (3 types):");
		log.info(DatatypeConverter.printHexBinary(bos.toByteArray()));

		byte[] expResult = DatatypeConverter.parseHexBinary("847831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F32303130"
				+ "2F49464D41502F32677075626C697368846A73657373696F6E2D69646D6D792D73657373696F6E2D69646A76616C69646174696F6E63416C6C8CF6667570646174"
				+ "6582686C69666574696D656773657373696F6E8C7831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49"
				+ "464D41502F32666465766963658084F6646E616D65806873657276657230317831687474703A2F2F7777772E74727573746564636F6D707574696E6767726F7570"
				+ "2E6F72672F323031302F49464D41502F326A69702D61646472657373846576616C7565D9A411440A0A64116474797065644950763480F6686D6574616461746180"
				+ "847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41504D455441444154412F326964657669"
				+ "63652D6970827169666D61702D63617264696E616C6974796B73696E676C6556616C756580F66664656C65746580887831687474703A2F2F7777772E7472757374"
				+ "6564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F32666465766963658084F6646E616D65806873657276657230317831687474703A"
				+ "2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F326A69702D61646472657373846576616C7565D9A411"
				+ "440A0A64116474797065644950763480F6666E6F7469667982686C69666574696D656773657373696F6E8C7831687474703A2F2F7777772E74727573746564636F"
				+ "6D707574696E6767726F75702E6F72672F323031302F49464D41502F32666465766963658084F6646E616D65806873657276657230317831687474703A2F2F7777"
				+ "772E74727573746564636F6D707574696E6767726F75702E6F72672F323031302F49464D41502F326A69702D61646472657373846576616C7565D9A411440A0A64"
				+ "116474797065644950763480F6686D6574616461746180847839687474703A2F2F7777772E74727573746564636F6D707574696E6767726F75702E6F72672F3230"
				+ "31302F49464D41504D455441444154412F32696465766963652D6970827169666D61702D63617264696E616C6974796B73696E676C6556616C756580");

		assertTrue("Byte array mismatch", Arrays.equals(expResult, bos.toByteArray()));
	}


	@Test(expected = CBORSerializationException.class)
	public void testCborSerialize_NoPublishTypes() throws Exception {
		CBORPublishRequest instance = new CBORPublishRequest(this.sessionId);

		CborBuilder cb = new CborBuilder();
		ArrayBuilder ab = cb.addArray();

		instance.cborSerialize(ab);
	}
}
