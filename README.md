### Project to simulate idleTimeout for the jetty REST endpoint.
#### Run with
`mvn spring-boot:run`

The SlowClient will automatically call the jetty endpoint by sending one byte at a time and with the help of the Thread.sleep, it will take more than 30 seconds to completely send the payload.
This will cause the server to throw the idle timeout expired exception.

#### Thread dumps can be monitored while the client sleeps
```declarative
jstack -l $(jps | grep -i Application | awk '{print $1}') | grep -i java.io.InputStream.read -B 10 -A 15
```

```xml
"qtp1719519917-57" #57 prio=5 os_prio=0 tid=0x00007fc618004800 nid=0x6495b in Object.wait() [0x00007fc72f1fd000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x00000006843f84f8> (a java.util.ArrayDeque)
	at java.lang.Object.wait(Object.java:502)
	at org.eclipse.jetty.server.HttpInput.blockForContent(HttpInput.java:584)
	at org.eclipse.jetty.server.HttpInput$1.blockForContent(HttpInput.java:1164)
	at org.eclipse.jetty.server.HttpInput.read(HttpInput.java:330)
	- locked <0x00000006843f84f8> (a java.util.ArrayDeque)
	at java.io.InputStream.read(InputStream.java:101)
	at org.apache.camel.util.IOHelper.copy(IOHelper.java:222)
	at org.apache.camel.util.IOHelper.copy(IOHelper.java:177)
	at org.apache.camel.util.IOHelper.copyAndCloseInput(IOHelper.java:237)
	at org.apache.camel.util.IOHelper.copyAndCloseInput(IOHelper.java:233)
	at org.apache.camel.http.common.HttpHelper.readResponseBodyFromInputStream(HttpHelper.java:243)
	at org.apache.camel.http.common.HttpHelper.readRequestBodyFromServletRequest(HttpHelper.java:194)
	at org.apache.camel.http.common.DefaultHttpBinding.parseBody(DefaultHttpBinding.java:595)
	at org.apache.camel.http.common.HttpMessage.createBody(HttpMessage.java:87)
	at org.apache.camel.impl.MessageSupport.getBody(MessageSupport.java:54)
	at org.apache.camel.http.common.DefaultHttpBinding.readBody(DefaultHttpBinding.java:186)
	at org.apache.camel.http.common.DefaultHttpBinding.readRequest(DefaultHttpBinding.java:109)
	at org.apache.camel.http.common.HttpMessage.<init>(HttpMessage.java:58)
	at org.apache.camel.component.jetty.CamelContinuationServlet.doService(CamelContinuationServlet.java:190)
	at org.apache.camel.http.common.CamelServlet.service(CamelServlet.java:79)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:750)

```