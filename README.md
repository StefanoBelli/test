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
