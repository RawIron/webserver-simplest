
my_home := $$HOME/workspace_java
package_home := $(my_home)/javaScratchpad

junit.jar:

Runner.class:	Runner.java junit.jar
	cd ./workspace_java/simpleWebServer; javac -classpath './workspace_java/javaScratchpad/junit.jar:./workspace_java/javaScratchpad/javax.servlet.jar:./workspace_java/javaScratchpad/' -sourcepath ./workspace_java/javaScratchpad/ -Xlint Runner.java


LoggerTest:	LoggerTest.class junit.jar
	cd ./workspace_java; java -classpath './workspace_java/javaScratchpad/junit.jar:./workspace_java/javaScratchpad/' junit.textui.TestRunner simpleWebServer.LoggerTest


Runner:	Runner.class
	cd ./workspace_java; java -classpath './workspace_java/javaScratchpad/junit.jar:./workspace_java/javaScratchpad/' simpleWebServer/Runner


clean:
	rm -f *.class
