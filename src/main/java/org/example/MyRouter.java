package org.example;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
            restConfiguration().component("jetty")
                    .host("0.0.0.0")
                    .port(8081)
                    .contextPath("/test");
            rest("/rest")
                    .post()
                    .route()
                    .to("direct:test");
            from("direct:test")
                    .log(">>> In Rest API with content ${body}")
                    .setBody().constant("Success");
            from("timer:time?period=60s")
                    .to("bean:slowClient?method=testClient")
                    .log("${body}");

    }
}
