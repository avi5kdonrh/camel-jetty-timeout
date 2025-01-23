package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

@Configuration
public class SlowClient {


    public void testClient() {
        String host = "localhost";
        int port = 8081;

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

            for (int i = 0; i < 10; i++) {
                outputStream.write('A');
                System.out.println("Sent byte: A");
                TimeUnit.SECONDS.sleep(i * 4);
            }
            outputStream.flush();
            byte[] bytes = new byte[socket.getInputStream().available()];
            int readCount;
            while ((readCount = socket.getInputStream().read(bytes, 0, bytes.length)) != -1) {
                System.out.println("Finished sending data. " + new String(bytes));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
