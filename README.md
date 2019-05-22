# DomainIE
Prototype for Modular Domain Specific Information Extraction System. Developed from https://github.com/yogaadrian/openIE by adding component for adapting openIE to domainIE.

# Prerequisites

- Java 1.8
- [Ant](http://ant.apache.org/) (How to install Ant on [Windows](https://www.mkyong.com/ant/how-to-install-apache-ant-on-windows/) / [OS X](https://www.mkyong.com/ant/how-to-apache-ant-on-mac-os-x/))

# Installation
All source codes are placed in the /src directory. Application is developed using IntelliJ IDEA.

- Clone this repo or [download here](https://github.com/irfanariq/DomainIE/archive/master.zip). <br />
  `$ git clone https://github.com/yogaadrian/OpenIE.git`
  <br />
- Open file `build.properties` and modify property `jdk.home.1.8` to your `JAVA_HOME` path. [More about JAVA_HOME](http://sbndev.astro.umd.edu/wiki/Finding_and_Setting_JAVA_HOME) <br />
  e.g. Windows: <br />
  `jdk.home.1.8=C:/Program Files/Java/jdk1.8.0_92` <br />
  e.g. Mac OS X: <br />
  `jdk.home.1.8=/Library/Java/JavaVirtualMachines/jdk1.8.0_92.jdk/Contents/Home`
  <br />
- Compile app (This will generate `out` and `target` directories needed to run the app) <br />
  `$ ant`

# Run program
This will open the main application window.
## UNIX
```
$ java -cp .:out/artifacts/OpenIE_jar/*:jars/*:target/classes id.ac.itb.gui.OpenIeJFrame
```

## Windows
```
$ java -cp '.;out/artifacts/OpenIE_jar/*;jars/*;target/classes' id.ac.itb.gui.OpenIeJFrame
```

# Creating a plugin
To create your own plugin, please see `/examples` for more information.