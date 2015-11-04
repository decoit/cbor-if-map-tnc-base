# DECOIT CBOR-IF-MAP Bridge - TNC Base Elements
This library provides Java classes that provide mapping between IF-MAP XML and a specific [CBOR](http://www.cbor.io) data structure. It can use an XML dictionary created with our [XML-CBOR-Dictionary](https://github.com/decoit/cbor-xml-dictionary) library to reduce the size of the CBOR output significantly. If no dictionary was provided, it uses a simple string based mapping that achieves only a small reduction in output size.

The main purpose of this library is converting IF-MAP into a CBOR data structure to reduce the bandwith required to send the data over a network. It may also be used to reduce the space required when storing IF-MAP structures on disk. The resulting CBOR data structure consists of several nested arrays to keep ordering of data intact during conversion.

It features full IF-MAP 2.2 support as described in the following two documents published by the Trusted Computing Group.

* [TNC IF-MAP Binding for SOAP v2.2](http://www.trustedcomputinggroup.org/resources/tnc_ifmap_binding_for_soap_specification)
* [TNC IF-MAP Metadata for Network Security v1.1](http://www.trustedcomputinggroup.org/resources/tnc_ifmap_metadata_for_network_security)

This library may be used on both server and client side as it features full support for serialization and deserialization of requests and responses.

The library was developed during the [SIMU research](http://www.simu-project.de) project.

## Preparation
The following requirements must be met to compile and use this library:

* Java 8 or higher
* Maven 3
* [XML-CBOR-Dictionary](https://github.com/decoit/cbor-xml-dictionary) installed in local Maven repository

To compile this project the Oracle JDK is preferred but it may work as well on other JDK implementations. Any Java 8 compatible JRE (Oracle, OpenJDK, Apple) should be able to run the application.

## Installation
Follow these steps to compile the project and install the JAR to your local Maven repository:

* Open a command prompt and change directory to the root of this project
* Execute `mvn install`

## Usage
To use this library in your application simply add the dependency to your pom.xml file:

```xml
<dependency>
    <groupId>de.decoit.simu</groupId>
    <artifactId>cbor-ifmap-tnc-base</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

To create a CBOR representation of your IF-MAP structure simply build the structure with pure Java code using the provided identifier, metadata, request and response classes. The pass the created object structure to the `CBORSerializer` class. The top level element must be a subclass of `AbstractRequest` or `AbstractResponse`. The resulting byte stream may either be retrieved as a Java byte array or directly injected into an `OutputStream`, depending on what you want to do with the CBOR structure.

```java
CBORPublishRequest req = new CBORPublishRequest("my-session-id");

// ... add elements to the request here ...

byte[] result = CBORSerializer.serializeRequest(req);
```

To retrieve a Java object structure from a CBOR byte stream simply pass it to the `CBORDeserializer` class. The CBOR byte stream may be provided as a Java byte array or as an `InputStream`.

```java
AbstractRequest newReq = CBORDeserializer.deserializeRequest(result);
```

### Extension Interface
It is possible to extend this library with your own extended identifiers and vendor-specific metadata and use it in requests and responses. Please have a look at our [CBOR-IF-MAP SIMU Extensions](https://github.com/decoit/cbor-if-map-simu-extensions) project to find out how this works in detail.

## License
The source code and all other contents of this repository are copyright by DECOIT GmbH and licensed under the terms of the [Apache License Version 2.0](http://www.apache.org/licenses/). A copy of the license may be found inside the LICENSE file.