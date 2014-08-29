package au.com.sensis.stubby.standalone;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;

import javax.net.ssl.SSLContext;

import org.apache.log4j.Logger;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;

public class HttpsServerInstance extends ServerInstance {

    private static final Logger LOGGER = Logger.getLogger(HttpsServerInstance.class);

    private static final void checkConfig() {
        String[] properties = { // also see 'javax.net.debug' for debugging
            "javax.net.ssl.keyStore",
            "javax.net.ssl.keyStorePassword"
        };

        for (String property : properties) {
            if (System.getProperty(property) == null) {
                LOGGER.warn("Should set property '" + property + "' when using HTTPS connector");
            }
        }
    }

    private HttpsServer server;

    public HttpsServerInstance(int port, ServerHandler handler, Executor executor) throws IOException, NoSuchAlgorithmException {
        checkConfig();
        this.server = HttpsServer.create(allInterfaces(port), SOCKET_BACKLOG);
        this.server.setHttpsConfigurator(new HttpsConfigurator(SSLContext.getDefault()));
        this.server.createContext("/", handler);
        this.server.setExecutor(executor);
        this.server.start();
    }

    @Override
    public HttpsServer getServer() {
        return server;
    }
}
