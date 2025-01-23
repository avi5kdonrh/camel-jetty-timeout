package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

@Configuration
public class SlowClient {


    public String testClient() {
        String host = "localhost";
        int port = 8081;
        StringBuilder data = new StringBuilder();

        try (
                Socket socket = new Socket(host, port);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        ) {
            System.out.println("Connected to server.");
            OutputStream outputStream = socket.getOutputStream();
            String request = "POST /test/rest HTTP/1.1\r\n"
                    + "Host: localhost\r\n"
                    + "Content-Length: 10\r\n"
                    + "Connection: keep-alive\r\n\r\n";
            outputStream.write(request.getBytes());
            outputStream.flush();
            System.out.println("Sent headers.");

            for (int i = 1; i < 11; i++) {
                outputStream.write('A');
                System.out.println("Sent byte: A");
                if(i == 9) {
                    TimeUnit.SECONDS.sleep(40);
                }
            }
            byte[] bytes = new byte[1024];
            int readCount;
            while ((readCount = socket.getInputStream().read(bytes)) != -1) {
                data.append((new String(bytes, 0, readCount)));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return data.toString();
    }
}
