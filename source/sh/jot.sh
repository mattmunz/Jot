
# TODO Replace this entire file with a Java program (part of MShell/Babe)

resourcesDir=$PROGRAMMING_DIRECTORY/Resources

beanutilsClasspath=$resourcesDir/Commons/BeanUtils/commons-beanutils-1.9.2/commons-beanutils-1.9.2.jar
commonsLoggingClasspath=$resourcesDir/Commons/Logging/commons-logging-1.2/commons-logging-1.2.jar

mongoDriverDir=$resourcesDir/MongoDB/mongodb-driver
mongoClasspath=$mongoDriverDir/bson-3.0.4.jar:$mongoDriverDir/mongodb-driver-core-3.0.4.jar:$mongoDriverDir/mongodb-driver-3.0.4.jar

jacksonDir=$resourcesDir/Jackson/2.7.0
jacksonCoreDir=$jacksonDir/core
jacksonCoreClasspath=$jacksonCoreDir/jackson-core-2.7.0.jar:$jacksonCoreDir/jackson-databind-2.7.0.jar:$jacksonCoreDir/jackson-annotations-2.7.0.jar
jacksonClasspath=$jacksonCoreClasspath:$jacksonDir/module/jackson-module-jaxb-annotations-2.7.0.jar

guavaClasspath=$resourcesDir/Guava/guava-18.0.jar

cliClasspath=$PROGRAMMING_DIRECTORY/CommandLineInterface/build/jars/commandLineInterface.jar
miscellanyClasspath=$PROGRAMMING_DIRECTORY/Miscellany/build/jars/miscellany.jar

jotDir=$PROGRAMMING_DIRECTORY/Jot

classpath=$guavaClasspath:$commonsLoggingClasspath:$beanutilsClasspath:$jacksonClasspath:$mongoClasspath:$miscellanyClasspath:$cliClasspath:$jotDir/build/jars/jot.jar

stty -icanon min 1

java -classpath $classpath mattmunz.jot.CLI $jotDir/deployments/$MSHELL_HOST

stty sane
