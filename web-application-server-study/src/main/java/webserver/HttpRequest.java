package webserver;

import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private String method;
    private String path;
    private Map<String, String> headerMap = new HashMap<>();
    private Map<String, String> queryStringMap = new HashMap<>();

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, Charsets.UTF_8));

        String line = br.readLine();
        log.debug("request line : {}", line);
        if (line == null) {
            return;
        }

        String[] tokens = line.split(" ");
        method = tokens[0];

        line = br.readLine();
        HttpRequestUtils.Pair pair;
        while (line != null && !line.equals("")) {
            log.debug("header : {}", line);
            pair = HttpRequestUtils.parseHeader(line);
            headerMap.put(pair.getKey(), pair.getValue());
            line = br.readLine();
        }

        if(method.equals("GET")) {
            path = HttpRequestUtils.getRequestURL(tokens[1]);
            queryStringMap = HttpRequestUtils.parseQueryString(HttpRequestUtils.getQueryString(tokens[1]));
        } else {
            path = tokens[1];
            String bodyData = IOUtils.readData(br, Integer.parseInt(headerMap.get("Content-Length")));
            queryStringMap = HttpRequestUtils.parseQueryString(bodyData);
        }
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
