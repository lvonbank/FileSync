# FileSync
        _______  _
       / ____(_)/ / ___  ___ __  _ __   ___
      / ____/ // / / _ \/ __/\ \/ / /_ / __\
     / /   / // /_/  __(__  ) \  / __ \ /__
    /_/   /_//____/___/____/  / /_/ /_/___/
                             /_/ 
The origin of this project was created in the necessity to service other programs that destroy or misplace their configuration or initialization files. <br />
FileSync was designed to copy a desired file from one or many directories into a temporary folder structure based on the original file including the way it exists in the primary root folder. If for whatever reason this file disappears FileSync will replace it with the temporary file, it has on hand. This temporarily file snapshots are based on a user defined interval. <br />
This Java jar will launch a message center UI for informational purposes. It will log any error or when it discovers a missing file. There is a safety mechanism in place to double check twice before replacing any missing files for the root directory.

## Getting Started



### Prerequisites

What things you need to install the software and how to install them.
* [Java SE Development Kit Downloads](https://www.oracle.com/java/technologies/downloads/) - The JDK framework runs jar files.

### Installing
On a local environment...
    
#### Pulling from the github
```
git clone git@github.com:lvonbank/FileSync.git
cd FileSync
```
    
##### Running Jar
```
REM Execute JDK Installer
jdk-14.0.2_windows-x64_bin.exe

REM Launch Java Jar File
FileSync.jar
```

## Built With

* [Java JDK v14.0.2](https://www.oracle.com/java/technologies/javase/jdk14-archive-downloads.html) - Language Version
* [Eclipse IDE for Java Developers](https://www.eclipse.org/downloads/) - IDE

## Authors

Levi VonBank

## License

This project is licensed under the MIT License.