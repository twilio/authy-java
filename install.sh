function install(){
      mvn install:install-file -Dfile=$1 -DgroupId=$2 -DartifactId=$3 -Dversion=$4 -Dpackaging=jar
}

install dist/authy-java.jar com.authy authy-java 1.0

#<dependency>
#    <groupId>com.authy</groupId>
#    <artifactId>authy-java</artifactId>
#    <version>1.0</version>
#</dependency>