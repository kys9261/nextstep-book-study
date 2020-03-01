package webserver;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String method;
    private String path;
    private Map<String, String> headerMap = new HashMap<>();
    private Map<String, String> queryStringMap = new HashMap<>();

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String line = bufferedReader.readLine();

        if (line == null) {
            return;
        }

        String[] tokens = line.split(" ");
        int contentLength = 0;
        Map<String, String> headerMap = new HashMap<String, String>();
        while (!"".equals(line)) {
            line = bufferedReader.readLine();
            if(line.contains("Content-Length")) {
                contentLength = getContentLength(line);
            } else {
                String[] splitedHeader = line.split(": ");
                if (splitedHeader.length == 2) {
                    headerMap.put(splitedHeader[0], splitedHeader[1]);
                }
            }
            line = bufferedReader.readLine();
        }

        String bodyData = IOUtils.readData(bufferedReader, Integer.parseInt(headerMap.get("Content-Length")));
        queryStringMap = HttpRequestUtils.parseQueryString(bodyData);

        method = tokens[0];
        path = tokens[1];
    }

    private int getContentLength(String line) {
        String[] headerTokens = line.split(":");
        return Integer.parseInt(headerTokens[1].trim());
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String headerName) {
        return headerMap.get(headerName);
    }

    public String getParameter(String queryName) {
        return queryStringMap.get(queryName);
    }
}
