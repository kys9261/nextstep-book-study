package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
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
            if (line == null) {
                return;
            }

            String url = HttpRequestUtils.getUrl(line);

            Map<String, String> headerMap = new HashMap<String, String>();
            while (!"".equals(line)) {
                line = bufferedReader.readLine();

                log.info(line);

                String[] splitedHeader = line.split(": ");
                if (splitedHeader.length == 2) {
                    headerMap.put(splitedHeader[0], splitedHeader[1]);
                }
            }

            if (url.startsWith("/user/create")) {
                String bodyData = IOUtils.readData(bufferedReader, Integer.parseInt(headerMap.get("Content-Length")));
                Map<String, String> queryStringMap = HttpRequestUtils.parseQueryString(bodyData);
                User user = new User(queryStringMap.get("userId"), queryStringMap.get("password"), queryStringMap.get("name"), queryStringMap.get("email"));
                DataBase.addUser(user);

                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos);
            } else if (url.equals("/user/login")) {
                String bodyData = IOUtils.readData(bufferedReader, Integer.parseInt(headerMap.get("Content-Length")));
                Map<String, String> queryStringMap = HttpRequestUtils.parseQueryString(bodyData);

                String cookie = "";
                String redirectUrl = "";
                User existUser = DataBase.findUserById(queryStringMap.get("userId"));
                if(existUser == null || !existUser.getPassword().equals(queryStringMap.get("password"))) {
                    cookie = "logined=false";
                    redirectUrl = "/user/login_failed.html";
                } else {
                    cookie = "logined=true";
                    redirectUrl = "/index.html";
                }

                DataOutputStream dos = new DataOutputStream(out);
                response302HeaderWithCookie(dos, redirectUrl, cookie);
            } else if (url.startsWith("/user/list")) {
                Map<String, String> cookies = HttpRequestUtils.parseCookies(headerMap.get("Cookie"));
                if(cookies.get("logined") == null || !Boolean.parseBoolean(cookies.get("logined"))) {
                    DataOutputStream dos = new DataOutputStream(out);
                    response302Header(dos);
                } else {
                    int idx = 3;

                    Collection<User> userList = DataBase.findAll();
                    StringBuilder sb = new StringBuilder();
                    sb.append("<tr>");
                    for(User user : userList) {
                        sb.append("<th scope=\"row\">"+idx+"</th><td>"+user.getUserId()+"</td> <td>"+user.getName()+"</td> <td>"+user.getEmail()+"</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>");
                        idx++;
                    }

                    String fileData = new String(Files.readAllBytes(new File("./webapp" + url).toPath()) );
                    fileData = fileData.replace("%user_list%", URLDecoder.decode(sb.toString(), "UTF-8"));

                    DataOutputStream dos = new DataOutputStream(out);
                    byte[] body = fileData.getBytes();
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                }
            } else {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());

                if(headerMap.get("Accept").contains("text/css")){
                    response200HeaderWithCss(dos, body.length);
                } else {
                    response200Header(dos, body.length);
                }
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

    private void response302HeaderWithCookie(DataOutputStream dos, String redirectUrl, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
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

    private void response200HeaderWithCss(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
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
