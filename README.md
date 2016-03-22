## A very simple Web Server Implementation

* only GET requests
* takes the path of the resource and returns the content of the file
* request is assigned to one thread, taken from a thread pool

### Quick Start

run the tests with
```
mvn test
```

start the web server
```
mvn compile
mvn exec:java -Dexec.mainClass='server.Runner'
```

open the test page in your browser
```
http://localhost:8080
```

load a page
```
http://localhost:8080/page1.html
```
