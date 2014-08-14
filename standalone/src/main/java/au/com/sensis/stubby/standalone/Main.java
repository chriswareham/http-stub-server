package au.com.sensis.stubby.standalone;

import au.com.sensis.stubby.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class);

    private static final int NUM_WORKER_THREADS = 10;

    public static void main(String[] args) throws Exception {
        int port = 0;
        int sslPort = 0;
        String responses = null;

        try {
            for (int i = 0; i < args.length; i += 2) {
                if ("--port".equals(args[i]) && i + 1 < args.length) {
                    port = Integer.parseInt(args[i + 1]);
                } else if ("--ssl-port".equals(args[i]) && i + 1 < args.length) {
                    sslPort = Integer.parseInt(args[i + 1]);
                } else if ("--responses".equals(args[i]) && i + 1 < args.length) {
                    responses = args[i + 1];
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Usage : java ... --port <http_port> [--ssl-port <https_port>] [--responses <filename>]");
            System.exit(1);
        }

        final List<ServerInstance> servers = new ArrayList<ServerInstance>();
        final ServerHandler handler = new ServerHandler(); // share between protocols
        final ExecutorService executor = Executors.newFixedThreadPool(NUM_WORKER_THREADS);

        if (!StringUtils.isBlank(responses)) {
            handler.loadResponses(responses);
        }

        if (port > 0) { // HTTP server
            HttpServerInstance httpServer = new HttpServerInstance(port, handler, executor);
            LOGGER.info("Started HTTP server on " + httpServer.getAddress());
            servers.add(httpServer);
        }

        if (sslPort > 0) { // HTTPS server
            HttpsServerInstance httpsServer = new HttpsServerInstance(sslPort, handler, executor);
            LOGGER.info("Started HTTPS server on " + httpsServer.getAddress());
            servers.add(httpsServer);
        }

        Thread shutdownHook = new Thread() {
            public void run() {
                LOGGER.info("Stopping servers...");
                try {
                    for (ServerInstance server : servers) {
                        server.shutdown();
                    }
                    executor.shutdown();
                    executor.awaitTermination(1, TimeUnit.SECONDS);
                } catch (Exception e) {
                    LOGGER.error("Error performing graceful shutdown", e);
                }
            }
        };

        handler.setShutdownHook(shutdownHook); // handle shutdown requests over HTTP
        Runtime.getRuntime().addShutdownHook(shutdownHook); // handle SIGINT etc.
    }

}
