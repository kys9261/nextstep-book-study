package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            String line = bufferedReader.readLine();
            if(line == null) {
                return;
            }

            String url = HttpRequestUtils.getUrl(line);

            Map<String, String> headerMap = new HashMap<String, String>();
            while(!"".equals(line)) {
                line = bufferedReader.readLine();

                String[] splitedHeader = line.split(": ");
                if(splitedHeader.length == 2) {
                    headerMap.put(splitedHeader[0], splitedHeader[1]);
                }
            }

            if(url.startsWith("/user/create")) {
                String bodyData = IOUtils.readData(bufferedReader, Integer.parseInt(headerMap.get("Content-Length")));
                Map<String, String> queryStringMap = HttpRequestUtils.parseQueryString(bodyData);
                User user = new User(queryStringMap.get("userId"), queryStringMap.get("password"), queryStringMap.get("name"), queryStringMap.get("email"));

                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos);
            } else {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
