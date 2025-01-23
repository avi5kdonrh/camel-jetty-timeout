### Project to simulate idleTimeout for the jetty REST endpoint.
#### Run with
```declarative
mvn spring-boot:run
```
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

#### The logs should show the timeout after 30 seconds
```declarative
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::               (v2.5.13)

2025-01-23 22:29:54.566  INFO 411923 --- [           main] org.example.Application                  : Starting Application using Java 1.8.0_432 on machine1 with PID 411923 (/home/user/camel-jetty-timeout/target/classes started by user in /home/user/camel-jetty-timeout)
2025-01-23 22:29:54.567  INFO 411923 --- [           main] org.example.Application                  : No active profile set, falling back to 1 default profile: "default"
2025-01-23 22:29:55.064  INFO 411923 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.apache.camel.spring.boot.CamelAutoConfiguration' of type [org.apache.camel.spring.boot.CamelAutoConfiguration$$EnhancerBySpringCGLIB$$7447c6ce] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2025-01-23 22:29:55.138  WARN 411923 --- [           main] io.undertow.websockets.jsr               : UT026010: Buffer pool was not set on WebSocketDeploymentInfo, the default pool will be used
2025-01-23 22:29:55.144  INFO 411923 --- [           main] io.undertow.servlet                      : Initializing Spring embedded WebApplicationContext
2025-01-23 22:29:55.144  INFO 411923 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 557 ms
2025-01-23 22:29:55.423  INFO 411923 --- [           main] o.a.c.i.converter.DefaultTypeConverter   : Type converters loaded (core: 195, classpath: 23)
2025-01-23 22:29:55.548  INFO 411923 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint(s) beneath base path '/actuator'
2025-01-23 22:29:55.581  INFO 411923 --- [           main] o.a.c.c.s.CamelHttpTransportServlet      : Initialized CamelHttpTransportServlet[name=CamelServlet, contextPath=]
2025-01-23 22:29:55.582  INFO 411923 --- [           main] io.undertow                              : starting server: Undertow - 2.2.17.SP4-redhat-00001
2025-01-23 22:29:55.585  INFO 411923 --- [           main] org.xnio                                 : XNIO version 3.8.7.SP1-redhat-00001
2025-01-23 22:29:55.587  INFO 411923 --- [           main] org.xnio.nio                             : XNIO NIO Implementation Version 3.8.7.SP1-redhat-00001
2025-01-23 22:29:55.615  INFO 411923 --- [           main] org.jboss.threads                        : JBoss Threads version 3.1.0.Final
2025-01-23 22:29:55.634  INFO 411923 --- [           main] o.s.b.w.e.undertow.UndertowWebServer     : Undertow started on port(s) 8080 (http)
2025-01-23 22:29:55.643  INFO 411923 --- [           main] o.a.camel.spring.boot.RoutesCollector    : Loading additional Camel XML routes from: classpath:camel/*.xml
2025-01-23 22:29:55.644  INFO 411923 --- [           main] o.a.camel.spring.boot.RoutesCollector    : Loading additional Camel XML rests from: classpath:camel-rest/*.xml
2025-01-23 22:29:55.651  INFO 411923 --- [           main] o.a.camel.spring.SpringCamelContext      : Apache Camel 2.23.2.fuse-7_11_0-00037-redhat-00001 (CamelContext: camel-1) is starting
2025-01-23 22:29:55.652  INFO 411923 --- [           main] o.a.c.m.ManagedManagementStrategy        : JMX is enabled
2025-01-23 22:29:55.721  INFO 411923 --- [           main] o.a.camel.spring.SpringCamelContext      : StreamCaching is not in use. If using streams then its recommended to enable stream caching. See more details at http://camel.apache.org/stream-caching.html
2025-01-23 22:29:55.752  INFO 411923 --- [           main] org.eclipse.jetty.util.log               : Logging initialized @1547ms to org.eclipse.jetty.util.log.Slf4jLog
2025-01-23 22:29:55.758  INFO 411923 --- [           main] o.a.camel.spring.SpringCamelContext      : Route: route1 started and consuming from: direct://test
2025-01-23 22:29:55.758  INFO 411923 --- [           main] o.a.camel.spring.SpringCamelContext      : Route: route2 started and consuming from: timer://time?period=60s
2025-01-23 22:29:55.778  INFO 411923 --- [           main] org.eclipse.jetty.server.Server          : jetty-9.4.43.v20210629-redhat-00004; built: 2022-05-05T16:15:34.613Z; git: d8f335da664ffdb3f6f6fa5a0791591427d95e2a; jvm 1.8.0_432-b06
2025-01-23 22:29:55.789  INFO 411923 --- [           main] o.e.jetty.server.handler.ContextHandler  : Started o.e.j.s.ServletContextHandler@6f25bf88{/,null,AVAILABLE}
2025-01-23 22:29:55.794  INFO 411923 --- [           main] o.e.jetty.server.AbstractConnector       : Started ServerConnector@40d52be7{HTTP/1.1, (http/1.1)}{0.0.0.0:8081}
2025-01-23 22:29:55.794  INFO 411923 --- [           main] org.eclipse.jetty.server.Server          : Started @1589ms
2025-01-23 22:29:55.794  INFO 411923 --- [           main] o.a.camel.spring.SpringCamelContext      : Route: route3 started and consuming from: jetty:http://0.0.0.0:8081/test/rest?httpMethodRestrict=POST
2025-01-23 22:29:55.794  INFO 411923 --- [           main] o.a.camel.spring.SpringCamelContext      : Total 3 routes, of which 3 are started
2025-01-23 22:29:55.795  INFO 411923 --- [           main] o.a.camel.spring.SpringCamelContext      : Apache Camel 2.23.2.fuse-7_11_0-00037-redhat-00001 (CamelContext: camel-1) started in 0.143 seconds
2025-01-23 22:29:55.796  INFO 411923 --- [           main] org.example.Application                  : Started Application in 1.403 seconds (JVM running for 1.592)
Connected to server.
Sent headers.
Sent byte: A
Sent byte: A
Sent byte: A
Sent byte: A
Sent byte: A
Sent byte: A
Sent byte: A
Sent byte: A
Sent byte: A

2025-01-23 22:30:26.831  WARN 411923 --- [tp1719519917-57] org.eclipse.jetty.server.HttpChannel     : handleException /test/rest java.io.IOException: java.util.concurrent.TimeoutException: Idle timeout expired: 30000/30000 ms

```