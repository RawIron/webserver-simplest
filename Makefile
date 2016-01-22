workspace = $$HOME/workspace_java
project = $(workspace)/webserver-simplest
package = $(project)/server
target = $(project)/server

junit.jar:

Runner.class:	./server/Runner.java junit.jar
	cd $(workspace); javac -classpath $(workspace)/junit.jar:$(workspace)/javax.servlet.jar:$(project) -sourcepath $(project) -Xlint $(package)/Runner.java


LoggerTest:	LoggerTest.class junit.jar
	cd ./workspace_java; java -classpath './workspace_java/webserver-simplest/junit.jar:./workspace_java/webserver-simplest/' junit.textui.TestRunner simpleWebServer.LoggerTest


Runner:	Runner.class
	cd ./workspace_java; java -classpath './workspace_java/webserver-simplest/junit.jar:./workspace_java/webserver-simplest/' webserver-simplest/Runner


clean:
	rm -f $(target)/*.class
