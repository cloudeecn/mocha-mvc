Mocha MVC, just another lightweight WebMVC framework

Inspired by Google [Guice](https://github.com/google/guice), this is a small web MVC framework focused on lightweight and performance.

It works well with [Spring](https://spring.io) and Guice, also it can work with other DIs or even without any DIs.

# Feature

 * Use DSL to configure url mappings, get rid of XML ( You can also Google Guice to get rid of XML totally ;) )
 
 * Automatic parameter resolve without any configuration
 
 * Fast and lightweight
 

# Quick start

There are only 4 steps start a new project with Mocha MVC, no DIs are required.

 1. Create a maven ( packaging war of course ) project, add following code in pom.xml

``` xml

	<repositories>
		<repository>
			<id>cw-releases</id>
			<name>Cirnoworks Releases</name>
			<url>http://repo1.cirnoworks.com/releases/</url>
			<releases>
				<enabled>true</enabled>
			</releases>
			<snapshots>
				<enabled>false</enabled>
			</snapshots>
			<layout>default</layout>
		</repository>
		<repository>
			<id>cw-snapshots</id>
			<name>Cirnoworks Snapshots</name>
			<url>http://repo1.cirnoworks.com/snapshots/</url>
			<releases>
				<enabled>false</enabled>
			</releases>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
			<layout>default</layout>
		</repository>
	</repositories>
	<dependencies>
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
			<version>3.0.1</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<!-- You can change to your favorite logger -->
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-simple</artifactId>
			<version>1.7.12</version>
		</dependency>
		<dependency>
			<groupId>works.cirno.mocha</groupId>
			<artifactId>mvc-core</artifactId>
			<version>0.1-SNAPSHOT</version>
		</dependency>
	</dependencies>

```

2. Create Controller class

``` java

	package example;
	
	import works.cirno.mocha.*;
	
	public class HelloController {
		void hello(PrintWriter writer, String name){
			writer.print("Hello " + name);
		}
	}

```

3. Create Configurator class

``` java

	package example;
	
	import works.cirno.mocha.*;
	
	public class HelloConfigurator extends MVCConfigurator {
		public void configure(){
			serve("/hello/${name}").with(HelloController.class, "hello");
		}
	}

```

4. Add Mocha's dispatcher filter into web.xml
``` xml

	<filter>
		<filter-name>dispatcher</filter-name>
		<filter-class>works.cirno.mocha.DispatcherFilter</filter-class>
		<init-param>
			<param-name>configurator</param-name>
			<param-value>example.HelloController</param-value>
		</init-param>
	</filter>

``` 

Run it! Access /hello/<your name> under your contextPath from browser, the result will be "Hello <your name>"

# More

Sorry for lack of document now, please read the example project for integration with Spring/Guice and more usage.

There will a detailed document in the wiki, so please stay tuned! 

# License

Copyright (c) 2014-2015 Cloudee Huang (cloudeecn@gmail.com / https://github.com/cloudeecn)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 