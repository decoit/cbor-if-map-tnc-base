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

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.builder.ArrayBuilder;
import de.decoit.simu.cbor.ifmap.exception.CBORSerializationException;
import de.decoit.simu.cbor.ifmap.request.AbstractRequest;
import de.decoit.simu.cbor.ifmap.response.AbstractResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * This is the public interface class that should be used when serializing requests or responses.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
public class CBORSerializer {
	/**
	 * Transform a Java object request structure into a CBOR byte string.
	 * 
	 * @param input Object structure to serialize
	 * @return Array containing the CBOR byte array
	 * @throws CBORSerializationException if anything goes wrong during serialization
	 */
	public static byte[] serializeRequest(AbstractRequest input) throws CBORSerializationException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		serializeRequest(input, bos);
		
		return bos.toByteArray();
	}
	
	
	/**
	 * Transform a Java object request structure into a CBOR byte string.
	 * The CBOR bytes will be sent to the specified target {@link OutputStream}.
	 * 
	 * @param input Object structure to serialize
	 * @param targetStream Bytes will be sent to this stream
	 * @throws CBORSerializationException if anything goes wrong during serialization
	 */
	public static void serializeRequest(AbstractRequest input, OutputStream targetStream) throws CBORSerializationException {
		try {
			CborBuilder cb = new CborBuilder();
			ArrayBuilder ab = cb.addArray();

			input.cborSerialize(ab);

			ab.end();

			CborEncoder ce = new CborEncoder(targetStream);
			ce.encode(cb.build());
		}
		catch(CborException ex) {
			throw new CBORSerializationException("CBOR encoding failed, see nested exception for details", ex);
		}
		catch(RuntimeException ex) {
			throw new CBORSerializationException("RuntimeException during serialization, see nested exception for details", ex);
		}
	}
	
	
	/**
	 * Transform a Java object response structure into a CBOR byte string.
	 * 
	 * @param input Object structure to serialize
	 * @return Array containing the CBOR byte array
	 * @throws CBORSerializationException if anything goes wrong during serialization
	 */
	public static byte[] serializeResponse(AbstractResponse input) throws CBORSerializationException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		serializeResponse(input, bos);
		
		return bos.toByteArray();
	}
	
	
	/**
	 * Transform a Java object response structure into a CBOR byte string.
	 * The CBOR bytes will be sent to the specified target {@link OutputStream}.
	 * 
	 * @param input Object structure to serialize
	 * @param targetStream Bytes will be sent to this stream
	 * @throws CBORSerializationException if anything goes wrong during serialization
	 */
	public static void serializeResponse(AbstractResponse input, OutputStream targetStream) throws CBORSerializationException {
		try {
			CborBuilder cb = new CborBuilder();
			ArrayBuilder ab = cb.addArray();

			input.cborSerialize(ab);

			ab.end();

			CborEncoder ce = new CborEncoder(targetStream);
			ce.encode(cb.build());
		}
		catch(CborException ex) {
			throw new CBORSerializationException("CBOR encoding failed, see nested exception for details", ex);
		}
		catch(RuntimeException ex) {
			throw new CBORSerializationException("RuntimeException during serialization, see nested exception for details", ex);
		}
	}
}
