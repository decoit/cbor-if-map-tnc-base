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

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import de.decoit.simu.cbor.ifmap.deserializer.RequestDeserializerManager;
import de.decoit.simu.cbor.ifmap.deserializer.ResponseDeserializerManager;
import de.decoit.simu.cbor.ifmap.exception.CBORDeserializationException;
import de.decoit.simu.cbor.ifmap.request.AbstractRequest;
import de.decoit.simu.cbor.ifmap.response.CBORResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * This is the public interface class that should be used when deserializing requests or responses.
 * The CBOR data that should be transformed into Java objects can be provided as either a byte array or
 * {@link InputStream}. In both cases the data must follow the specified structure, otherwise the
 * process will fail.
 * 
 * @author Thomas Rix (rix@decoit.de)
 */
public class CBORDeserializer {
	/**
	 * Transform a byte array that contains CBOR data representing an IF-MAP request into java objects.
	 * The byte array is loaded into a {@link ByteArrayInputStream} that is then passed to the 
	 * deserializeRequest(InputStream) method.
	 * 
	 * @param cborBytes CBOR byte string representing a IF-MAP request
	 * @return The deserialized request object structure
	 * @throws CBORDeserializationException if anything goes wrong during deserialization
	 */
	public static AbstractRequest deserializeRequest(byte[] cborBytes) throws CBORDeserializationException {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(cborBytes);

			return deserializeRequest(bis);
		}
		catch(RuntimeException ex) {
			throw new CBORDeserializationException("RuntimeException during deserialization, see nested exception for details", ex);
		}
	}
	
	
	/**
	 * Transform the bytes inside the provided {@link InputStream} into java objects.
	 * The bytes must represent a valid IF-MAP request.
	 * 
	 * @param cborInputStream CBOR byte string representing a IF-MAP request
	 * @return The deserialized request object structure
	 * @throws CBORDeserializationException if anything goes wrong during deserialization
	 */
	public static AbstractRequest deserializeRequest(InputStream cborInputStream) throws CBORDeserializationException {
		try {
			CborDecoder cd = new CborDecoder(cborInputStream);
			List<DataItem> diList = cd.decode();
			
			if(diList.isEmpty()) {
				throw new CBORDeserializationException("InputStream did not contain CBOR data items");
			}
			
			if(diList.get(0).getMajorType() != MajorType.ARRAY) {
				throw new CBORDeserializationException("Invalid data structure! "
													   + "Expected top level array data item, found: "
													   + diList.get(0).getMajorType());
			}
			
			Array topLevelArray = (Array) diList.get(0);
		
			AbstractRequest result = RequestDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), 
																	 topLevelArray.getDataItems().get(1), 
																	 (Array)topLevelArray.getDataItems().get(2), 
																	 (Array)topLevelArray.getDataItems().get(3));
			
			return result;
		}
		catch(CborException ex) {
			throw new CBORDeserializationException("Error during CBOR decoding, see nested exception for details", ex);
		}
		catch(RuntimeException ex) {
			throw new CBORDeserializationException("RuntimeException during deserialization, see nested exception for details", ex);
		}
	}
	
	
	/**
	 * Transform a byte array that contains CBOR data representing an IF-MAP response into java objects.
	 * The byte array is loaded into a {@link ByteArrayInputStream} that is then passed to the 
	 * deserializeResponse(InputStream) method.
	 * 
	 * @param cborBytes CBOR byte string representing a IF-MAP response
	 * @return The deserialized response object structure
	 * @throws CBORDeserializationException if anything goes wrong during deserialization
	 */
	public static CBORResponse deserializeResponse(byte[] cborBytes) throws CBORDeserializationException {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(cborBytes);

			return deserializeResponse(bis);
		}
		catch(RuntimeException ex) {
			throw new CBORDeserializationException("RuntimeException during deserialization, see nested exception for details", ex);
		}
	}
	
	
	/**
	 * Transform the bytes inside the provided {@link InputStream} into java objects.
	 * The bytes must represent a valid IF-MAP response.
	 * 
	 * @param cborInputStream CBOR byte string representing a IF-MAP response
	 * @return The deserialized response object structure
	 * @throws CBORDeserializationException if anything goes wrong during deserialization
	 */
	public static CBORResponse deserializeResponse(InputStream cborInputStream) throws CBORDeserializationException {
		try {
			CborDecoder cd = new CborDecoder(cborInputStream);
			List<DataItem> diList = cd.decode();
			
			if(diList.isEmpty()) {
				throw new CBORDeserializationException("InputStream did not contain CBOR data items");
			}
			
			if(diList.get(0).getMajorType() != MajorType.ARRAY) {
				throw new CBORDeserializationException("Invalid data structure! "
													   + "Expected top level array data item, found: "
													   + diList.get(0).getMajorType());
			}
			
			Array topLevelArray = (Array) diList.get(0);
		
			CBORResponse result = ResponseDeserializerManager.deserialize(topLevelArray.getDataItems().get(0), 
																	 topLevelArray.getDataItems().get(1), 
																	 (Array)topLevelArray.getDataItems().get(2), 
																	 (Array)topLevelArray.getDataItems().get(3));
			
			return result;
		}
		catch(CborException ex) {
			throw new CBORDeserializationException("Error during CBOR decoding, see nested exception for details", ex);
		}
		catch(RuntimeException ex) {
			throw new CBORDeserializationException("RuntimeException during deserialization, see nested exception for details", ex);
		}
	}
}
