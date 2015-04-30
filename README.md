# SwingX

A port of the latest **SwingX** 1.6.6-SNAPSHOT from the subversion repository at java.net.

The GitHub Importer was used to import the SVN repository, so previous history and committers are here.

Even though Swing is no longer maintained, and neither is SwingX (JavaFX being the successor), I still find the Swing library very useful for setting up RCP clients. Hence my interest in maintaining this library.

This is a public repo, please feel free to send pull requests! :)

The following is just a copy from the README.txt file.

## Readme 

SwingLabs SwingX Project - http://swingx.dev.java.net

SwingX is a library of components and utilities extending the Java Swing library; read more at our website, 
http://swingx.dev.java.net, and Wiki page, http://wiki.java.net/bin/view/Javadesktop/SwingLabsSwingX

## Getting the Latest Source

1) Check out the latest code
Download the latest release from our SVN repository; full instructions are at
https://swingx.dev.java.net/servlets/ProjectSource

## Building the Source

SwingX relies on Maven for controlling compilation, building docs, testing, etc. You can use our POM files to build the project, some IDEs can directly invoke Maven for you.

To compile from the command line, you'll need to have Apache Maven 3.x installed; see http://maven.apache.org. 

You can build SwingX by going to the command line and typing
...
mvn package
...

That should be it--this will test and build swingx.jar in the target directory. 
