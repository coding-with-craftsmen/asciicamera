ascii camera
===========

Create an ascii art by using you raspberry pi camera

## Prerequisite

java 1.6+

maven 3.0+

## Building

The command `mvn clean package` will result in a fat-jar in the target directory named `asciicamera-1.0-SNAPSHOT.one-jar.jar`

## Running

After copying the jar to your raspberry pi, you can run the application with `java -jar asciicamera-1.0-SNAPSHOT.one-jar.jar`

When adding a filename as parameter the camera is not used but the given filename.


