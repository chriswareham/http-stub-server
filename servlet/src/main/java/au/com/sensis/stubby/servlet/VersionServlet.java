package au.com.sensis.stubby.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VersionServlet extends AbstractStubServlet {

    private static final long serialVersionUID = 1L;

    private Properties readVersionProps() throws IOException {
        InputStream stream = getClass().getResourceAsStream(
                "/au/com/sensis/stubby/servlet/version.properties");
        try {
            Properties props = new Properties();
            props.load(stream);
            return props;
        } finally {
            stream.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        returnJson(response, readVersionProps());
    }

}
