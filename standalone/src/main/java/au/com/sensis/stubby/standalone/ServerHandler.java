package au.com.sensis.stubby.standalone;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.apache.log4j.Logger;

import au.com.sensis.stubby.model.StubParam;
import au.com.sensis.stubby.model.StubRequest;
import au.com.sensis.stubby.service.JsonServiceInterface;
import au.com.sensis.stubby.service.NotFoundException;
import au.com.sensis.stubby.service.StubService;
import au.com.sensis.stubby.service.model.StubServiceResult;
import au.com.sensis.stubby.utils.JsonUtils;
import au.com.sensis.stubby.utils.RequestFilterBuilder;

public class ServerHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(ServerHandler.class);
    private static final Pattern CONTROL_PATTERN = Pattern.compile("^/_control/(.+?)(/(\\d+))?$");

    private StubService service;
    private JsonServiceInterface jsonService;
    private Thread shutdownHook; // if set, use for graceful shutdown

    public ServerHandler() {
        service = new StubService();
        jsonService = new JsonServiceInterface(service);
    }

    public void loadResponses(String filename) {
        service.loadResponses(filename);
    }

    public void setShutdownHook(Thread shutdownHook) {
        this.shutdownHook = shutdownHook;
    }

    @Override
    public void handle(HttpExchange exchange) {
        long start = 0L;
        if (LOGGER.isTraceEnabled()) {
            start = System.currentTimeMillis();
        }
        try {
            // force clients to close connection to work around issue with Keep-Alive in HttpServer (Java 7)
            // see: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=8009548
            // TODO: should probably use another HTTP server (not the 'com.sun' one)
            exchange.getResponseHeaders().set("Connection", "close");

            String path = exchange.getRequestURI().getPath();
            if (path.startsWith("/_control/")) {
                handleControl(exchange);
            } else {
                handleMatch(exchange);
            }
        } catch (Exception e) {
            LOGGER.error(e);
            try {
                returnError(exchange, e.getMessage());
            } catch (IOException ex) {
                LOGGER.error(ex);
            }
        } finally {
            exchange.close();
        }
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Server handle processing time(ms): " + (System.currentTimeMillis() - start));
        }
    }

    private void handleMatch(HttpExchange exchange) throws Exception {
        StubServiceResult result = service.findMatch(Transformer.fromExchange(exchange));
        if (result.matchFound()) {
            Long delay = result.getDelay();
            if (delay != null && delay > 0) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Delayed request, sleeping for " + delay + " ms...");
                }
                Thread.sleep(delay);
            }
            Transformer.populateExchange(result.getResponse(), exchange);
        } else {
            returnNotFound(exchange, "No stubbed method matched request");
        }
    }

    private void handleControl(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        Matcher matcher = CONTROL_PATTERN.matcher(path);
        if (matcher.matches()) {
            String endpoint = matcher.group(1);
            String indexStr = matcher.group(3);
            if (indexStr != null) {
                int index = Integer.parseInt(indexStr);
                if ("requests".equals(endpoint)) {
                    handleRequest(exchange, index);
                } else if ("responses".equals(endpoint)) {
                    handleResponse(exchange, index);
                } else {
                    returnNotFound(exchange, "No control method matched request");
                }
            } else {
                if ("requests".equals(endpoint)) {
                    handleRequests(exchange);
                } else if ("responses".equals(endpoint)) {
                    handleResponses(exchange);
                } else if ("shutdown".equals(endpoint)) {
                    handleShutdown(exchange);
                } else if ("version".equals(endpoint)) {
                    handleVersion(exchange);
                } else {
                    returnNotFound(exchange, "No control method matched request");
                }
            }
        }
    }

    private void returnOk(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1); // no body
        exchange.getResponseBody().close();
    }

    private void returnNotFound(HttpExchange exchange, String message) throws IOException {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0); // unknown body length
        exchange.getResponseBody().write(message.getBytes());
        exchange.getResponseBody().close();
    }

    private void returnError(HttpExchange exchange, String message) throws IOException {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0); // unknown body length
        exchange.getResponseBody().write(message.getBytes());
        exchange.getResponseBody().close();
    }

    private void returnJson(HttpExchange exchange, Object model) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0); // unknown body length
        JsonUtils.serialize(exchange.getResponseBody(), model);
        exchange.getResponseBody().close();
    }

    private void handleResponses(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if ("POST".equals(method)) {
            jsonService.addResponse(exchange.getRequestBody());
            returnOk(exchange);
        } else if ("DELETE".equals(method)) {
            service.deleteResponses();
            returnOk(exchange);
        } else if ("GET".equals(method)) {
            returnJson(exchange, service.getResponses());
        } else {
            throw new RuntimeException("Unsupported method: " + method);
        }
    }

    private void handleResponse(HttpExchange exchange, int index) throws IOException {
        String method = exchange.getRequestMethod();
        if ("GET".equals(method)) {
            try {
                returnJson(exchange, service.getResponse(index));
            } catch (NotFoundException e) {
                returnNotFound(exchange, e.getMessage());
            }
        } else {
            throw new RuntimeException("Unsupported method: " + method);
        }
    }

    private void handleRequests(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if ("DELETE".equals(method)) {
            service.deleteRequests();
            returnOk(exchange);
        } else if ("GET".equals(method)) {
            List<StubParam> params = Transformer.fromExchangeParams(exchange);
            StubRequest filter = createFilter(params);
            Long wait = getWaitParam(params);
            if (wait != null && wait > 0) { // don't allow zero wait
                returnJson(exchange, service.findRequests(filter, wait));
            } else {
                returnJson(exchange, service.findRequests(filter));
            }
        } else {
            throw new RuntimeException("Unsupported method: " + method);
        }
    }

    private void handleRequest(HttpExchange exchange, int index) throws IOException {
        String method = exchange.getRequestMethod();
        if ("GET".equals(method)) {
            try {
                returnJson(exchange, service.getRequest(index));
            } catch (NotFoundException e) {
                returnNotFound(exchange, e.getMessage());
            }
        } else {
            throw new RuntimeException("Unsupported method: " + method);
        }
    }

    private StubRequest createFilter(List<StubParam> params) {
        return new RequestFilterBuilder().fromParams(params).getFilter();
    }

    private Long getWaitParam(List<StubParam> params) {
        for (StubParam param : params) {
            if ("wait".equals(param.getName())) {
                return Long.parseLong(param.getValue());
            }
        }
        return null; // not found
    }

    private void handleShutdown(HttpExchange exchange) throws IOException {
        if (shutdownHook != null) {
            LOGGER.info("Received shutdown request, attempting to shutdown gracefully...");
            returnOk(exchange);
            shutdownHook.start(); // attempt graceful shutdown
        } else {
            LOGGER.error("Received shutdown request, but don't know how to shutdown gracefully! (ignoring)");
            returnError(exchange, "Graceful shutdown not supported");
        }
    }

    private void handleVersion(HttpExchange exchange) throws IOException {
        InputStream stream = getClass().getResourceAsStream("/au/com/sensis/stubby/standalone/version.properties");
        try {
            Properties props = new Properties();
            props.load(stream);
            returnJson(exchange, props);
        } finally {
            stream.close();
        }
    }
}
