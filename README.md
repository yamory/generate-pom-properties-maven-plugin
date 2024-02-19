# generate-pom-properties-maven-plugin

This plugin generates pom.properties for all libraries, including indirect dependent libraries of a Maven project.

## requirements

- Java 11+
- Maven 3+

## usage

Add the JitPack repository to your project's pom.xml.

```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
```

Next, add the generate-pom-properties-maven-plugin. 
By default, `pom.properties` is generated in `target/classes/.pom`.

```xml
             <plugin>
                <groupId>io.yamory</groupId>
                <artifactId>generate-pom-properties-maven-plugin</artifactId>
                <version>0.0.1</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>generatePomProperties</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <outputDirectory>${project.build.outputDirectory}</outputDirectory> <!-- default -->
                    <outputPomDirectoryName>.pom</outputPomDirectoryName> <!-- default -->
                </configuration>
            </plugin>
```

To include the generated pom.properties in the FatJar using the [maven-assembly-plugin](https://maven.apache.org/plugins/maven-assembly-plugin/), configure it as follows.

```xml
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.6.0</version>
                <configuration>
                    <descriptors>
                        <descriptor>${basedir}/assembly.xml</descriptor>
                    </descriptors>

                    <archive>
                        <manifest>
                            <mainClass>com.example.SampleApp</mainClass>
                        </manifest>
                    </archive>
                </configuration>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```

Create `assembly.xml` in the project directory and describe it as follows.

```xml
<assembly xmlns="http://maven.apache.org/ASSEMBLY/2.2.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/ASSEMBLY/2.2.0 http://maven.apache.org/xsd/assembly-2.2.0.xsd">
    <id>jar-with-dependencies</id>
    <formats>
        <format>jar</format>
    </formats>
    <fileSets>
        <fileSet>
            <directory>${basedir}/target/classes/.pom</directory>
            <outputDirectory></outputDirectory>
            <includes>
                <include>**/*</include>
            </includes>
        </fileSet>
    </fileSets>
    <includeBaseDirectory>false</includeBaseDirectory>
    <dependencySets>
        <dependencySet>
            <outputDirectory>/</outputDirectory>
            <useProjectArtifact>true</useProjectArtifact>
            <unpack>true</unpack>
            <scope>runtime</scope>
        </dependencySet>
    </dependencySets>
</assembly>
```

## example pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>example-maven-project</artifactId>
    <version>1.0-SNAPSHOT</version>

    <name>example-maven-project</name>

    <properties>
        <java.version>11</java.version>
        <maven.compiler.source>${java.version}</maven.compiler.source>
        <maven.compiler.target>${java.version}</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- your dependencies -->
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>io.yamory</groupId>
                <artifactId>generate-pom-properties-maven-plugin</artifactId>
                <version>0.0.1</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>generatePomProperties</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <outputDirectory>${project.build.outputDirectory}</outputDirectory>
                    <outputPomDirectoryName>.pom</outputPomDirectoryName>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.6.0</version>
                <configuration>
                    <descriptors>
                        <descriptor>${basedir}/assembly.xml</descriptor>
                    </descriptors>

                    <archive>
                        <manifest>
                            <mainClass>com.example.AppSample</mainClass>
                        </manifest>
                    </archive>
                </configuration>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <!-- other your plugins -->
        </plugins>
    </build>
</project>
```
## properties

| name                   | description                                                 | default                            |
| ---------------------- | ----------------------------------------------------------- | ---------------------------------- |
| outputDirectory        | The directory where the `pom.properties` is generated.      | `${project.build.outputDirectory}` |
| outputPomDirectoryName | The directory name where the `pom.properties` is generated. | `.pom`                             |

`pom.properties` is generated in `${outputDirectory}/${outputPomDirectoryName}/META-INF/maven/${groupId}/${artifactId}/pom.properties`.

## contribution

### build

```bash
git clone https://github.com/yamory/generate-pom-properties-maven-plugin.git
cd generate-pom-properties-maven-plugin
./mvnw install
```

### run

To generate `pom.properties` only, execute the following command.

```bash
./mvnw io.yamory:generate-pom-properties-maven-plugin:generatePomProperties
```

## release

### how to release

Generate release tags and release commits.

```bash
./mvnw release:prepare
```

Next, when you create a release on your GitHub repository, it will be released on JitPack.
