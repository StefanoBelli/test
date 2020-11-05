# test
Testing some ISPW things (svn, travis, sonarcloud)

## Workflow

 * Maven
 * Travis CI
 * SonarCloud
 
## Maven

 ```
 mvn archetype:generate -DgroupId=pkg.name -DartifactId=project
 cd project
 rm -r src
 $EDITOR pom.xml # by adding <packaging>pom</packaging> (in root <project> tag)
 mvn archetype:generate -DgroupId=pkg.name -DartifactId=moduleI # for I = 1,...,n
 ```
 
 Edit pom.xml(s) if necessary (ref. website, version, ...)

### Maven compiling

 ```mvn compile```
 
### Maven testing

 ```mvn test```
 
### Maven packaging

 ```mvn package```
 
 This will generate jars: one for each module: to have a working jar, add this in plugin management for plugin (artifactId) ```maven-jar-plugin```
 
 ```
 <configuration>
  <archive>
   <manifest>
    <mainClass>package.to.class.containing.MainMethod</mainClass>
    <addClasspath>true</addClasspath>
   </manifest>
  </archive>
 </configuration>
```
 in each module containing a class with ```main``` method
 
 
### Eclipse compat

 It is possible to use project modules with Eclipse by importing them or importing the root project
 
 
## Travis

 This configuration lets travis compile, test, static-analyze, upload to SonarCloud and deploy on each tag on main branch
 
## SonarCloud

 The SonarCloud's ```sonar-project.properties``` is configured to analyze every java source file in ```cool_project/{server,client}/src/main/java/...```
 
 Not able to report code coverage

## Missing features from Maven

 * Code coverage
 
## JavaFX with java14

 * [thanks](https://github.com/javafxports/openjdk-jfx/issues/236#issuecomment-426583174)
 
#### Add javafx required dependencies

 In subproject ```pom.xml``` file (```<dependencies>```):
 
 ```
 <!-- https://mvnrepository.com/artifact/org.openjfx/javafx-controls -->
 <dependency>
  <groupId>org.openjfx</groupId>
  <artifactId>javafx-controls</artifactId>
  <version>16-ea+3</version>
	</dependency>
 ```

#### Embed javafx classes in final jar

 In subproject ```pom.xml``` file (```<plugins>```):
 
 ```
 <plugin>
  <artifactId>maven-assembly-plugin</artifactId>
  <configuration>
   <archive>
    <manifest>
     <mainClass>ssynx.test.App</mainClass>
    </manifest>
   </archive>
   <descriptorRefs>
    <descriptorRef>jar-with-dependencies</descriptorRef>
   </descriptorRefs>
  </configuration>
 </plugin>
 ```
 
#### Packaging

 In subproject:
 
 ```
 $ mvn assembly:single
 ```
 
 Output: ```target/client-jar-with-dependencies.jar```
 
#### Troubleshooting

 ```error javafx runtime components are missing and are required to run this application...``` something like that.
 
  set your entry point not to be a class which extends Application from JFX.
  
  src/main/java/ssynx/test/App.java
  
  ```java
  package ssynx.test;
  
  // real entry point
  public class App {
     public static void main(String[] args) {
        HelloWorld.main(args);
     }
  }
  ```
 
  src/main/java/ssynx/test/HelloWorld.java
  
  ```java
  package ssynx.test;
  
  public class HelloWorld extends Application {
    @Override
    public /*...*/ start(/*...*/) {
      /*...*/
    }

    public static void main(String[] args) {
      /*...*/
      launch(args);
    }
   }
   ```
