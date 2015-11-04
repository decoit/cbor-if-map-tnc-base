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



/**
 * Enum defining the namespaces defined by the IF-MAP standard.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class IfMapNamespaces {
	public static final String IFMAP = "http://www.trustedcomputinggroup.org/2010/IFMAP/2";
	public static final String IFMAP_METADATA = "http://www.trustedcomputinggroup.org/2010/IFMAPMETADATA/2";
	public static final String IFMAP_OPEARATIONAL_METADATA = "http://www.trustedcomputinggroup.org/2012/IFMAPOPERATIONAL-METADATA/1";
	public static final String IFMAP_SERVER = "http://www.trustedcomputinggroup.org/2013/IFMAP-SERVER/1";
	
	
	protected IfMapNamespaces() { }
}
