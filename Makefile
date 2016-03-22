workspace = $$HOME/workspace_java
project = $(workspace)/webserver-simplest
package = $(project)/server
target = $(project)/server


junit.jar:
Runner.java:
LoggerTest.java:


Runner.class:	Runner.java junit.jar
	javac -classpath $(workspace)/junit.jar:$(workspace)/javax.servlet.jar:$(project) -sourcepath $(project) -Xlint $(package)/Runner.java

Runner:	Runner.class
	java -classpath $(workspace)/junit.jar:$(project) server.Runner


LoggerTest.class: LoggerTest.java junit.jar
	javac -classpath $(workspace)/junit.jar:$(project) -sourcepath $(project) -Xlint $(package)/LoggerTest.java

LoggerTest:	LoggerTest.class junit.jar
	java -classpath $(workspace)/junit.jar:$(project) junit.textui.TestRunner server.LoggerTest

test: LoggerTest

clean:
	rm -f $(target)/*.class
