# AspectJ4TANGO

[![Download](https://api.bintray.com/packages/hzgde/hzg-wpn-projects/tango-aspects/images/download.svg) ](https://bintray.com/hzgde/hzg-wpn-projects/tango-aspects/_latestVersion)

## Usage:

Add the following to your pom:

```$xml
        <properties>
            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
            <maven.compiler.source>1.8</maven.compiler.source>
            <maven.compiler.target>1.8</maven.compiler.target>
            <aspectj.version>1.8.9</aspectj.version>
        </properties>

        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjrt</artifactId>
            <version>${aspectj.version}</version>
        </dependency>
        <dependency>
            <groupId>de.hzg.wpi.tango</groupId>
            <artifactId>tango-aspects</artifactId>
            <version>LATEST</version>
        </dependency>
        
        <!-- ... -->
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>aspectj-maven-plugin</artifactId>
            <version>1.10</version>
            <executions>
                <execution>
                    <id>aspectj</id>
                    <goals>
                        <goal>compile</goal>
                        <goal>test-compile</goal>
                    </goals>
                    <phase>process-sources</phase>
                </execution>
            </executions>
            <configuration>
                <source>${maven.compiler.source}</source>
                <target>${maven.compiler.target}</target>
                <complianceLevel>${maven.compiler.target}</complianceLevel>
                <aspectLibraries>
                    <aspectLibrary>
                        <groupId>de.hzg.wpi.tango</groupId>
                        <artifactId>tango-aspects</artifactId>
                    </aspectLibrary>
                </aspectLibraries>
                <showWeaveInfo>true</showWeaveInfo>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>org.aspectj</groupId>
                    <artifactId>aspectjtools</artifactId>
                    <version>${aspectj.version}</version>
                </dependency>
            </dependencies>
        </plugin>
```

and then in the code:

```$java

@SelfRegisteringTangoService
public class MyDevice

```