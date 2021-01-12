# Buchain Java SDK

## Introduction
Java developers can easily operate Gas blockchain via the Buchain Java SDK. And you can complete the installation of the SDK in Maven or by downloading the jar package in a few minutes.

1. [docs](/docs) are the usage documentations for the Buchain Java SDK.
2. [examples](/examples) are some examples of a project based on Maven.
3. [libs](/libs) are the jar package and the dependency packages for the Buchain Java SDK.
4. [src](/src)  is the source code for the Buchain Java SDK.

## Environment

JDK 8 or above.

## Installation

> Note: V1.x has been prompted for maintenance, and V2.x is recommended.

#### Mode 1：Adding Dependencies to Maven Projects (Recommended)
To use the Buchain Java SDK in a Maven project, just add the remote repository provided by Gas to the maven configuration and add the corresponding dependency to pom.xml.

This article uses version 1.0.0 as an example

Maven remote repository
``` xml
<repository>
    <id>releases</id>                
    <url>http://maven.bubidev.cn/content/repositories/releases/</url>
    <releases>
        <enabled>true</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```
Add the following in the dependencies tag：
``` xml
<dependency>
  <groupId>cn.bubi.sdk</groupId>
  <artifactId>bubichain-sdk</artifactId>
  <version>1.0.1</version>
</dependency>
```
#### Mode 2: Import the JAR Package in the Project
1. Download Buchain Java SDK Development Kit
2. Unzip the development package
3. Import bubichain-sdk-{version}.jar and the included libs jar into your project

## Example project
Buchain Java SDK provides rich examples for developers' reference

[Sample document entry](docs/SDK.md "")
