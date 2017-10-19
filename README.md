# YAWL Dashboard Backend

This is the backend for the dashboard and notifications module of the [YAWL Web Frontend](https://github.com/Floaz/yawl-web-frontend).
> YAWL is a BPM/Workflow system, based on a concise and powerful modelling language, that handles complex data transformations, and full integration with organizational resources and external Web Services."

[More about YAWL here](http://www.yawlfoundation.org/)


## Requirements


YAWL Dashboard Backend is based on the Spring Framework and the Java programming language.
The most libraries are downloaded automatically by the package manager gradle. But for this to work, you have to install the latest [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [gradle](https://gradle.org/gradle-download/).


This project uses the YAWL library. Do the following steps to make the YAWL library available for gradle:

1. Download and install maven from <https://maven.apache.org/download.cgi>.
2. Download the latest version of the YAWL library. E.g.<https://github.com/yawlfoundation/yawl/releases/download/v4.1/YAWL_LibraryJars_4.1.zip>
3. Unzip the zip file.
4. Use maven to install the yawl library jar to local maven repo: `mvn install:install-file -Dfile=yawl-lib-4.1.jar -DgroupId=org.yawlfoundation.yawl -DartifactId=yawl-lib -Dversion=4.1 -Dpackaging=jar`


## Compile

1. Open shell
2. Goto project root directory
3. Run `gradle build`

The result can be found under the path `build/libs/yawl-dashboard-backend.war`!
