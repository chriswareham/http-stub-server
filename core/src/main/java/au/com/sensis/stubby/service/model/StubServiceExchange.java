package au.com.sensis.stubby.service.model;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import au.com.sensis.stubby.model.StubExchange;
import au.com.sensis.stubby.model.StubRequest;
import au.com.sensis.stubby.model.StubResponse;
import au.com.sensis.stubby.utils.ResourceResolver;

/**
 * This class provides a wrapper for stubbed exchanges.
 */
public class StubServiceExchange {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(StubServiceExchange.class);

    /**
     * The wrapped stubbed exchange.
     */
    private StubExchange exchange;
    /**
     * The pattern that matches incoming requests.
     */
    private RequestPattern requestPattern;
    /**
     * The response body to return for matched requests.
     */
    private ResponseBody responseBody;
    /**
     * The script to execute for matched requests.
     */
    private Script script;
    /**
     * XXX document me.
     */
    private List<MatchResult> attempts;

    /**
     * Instantiate an instance of the wrapper from a stubbed exchange.
     *
     * @param resolver the resource resolver to use
     * @param exchange the stubbed exchange to wrap
     */
    public StubServiceExchange(final ResourceResolver resolver, final StubExchange exchange) {
        this.exchange = exchange;
        this.requestPattern = new RequestPattern(resolver, exchange.getRequest());
        this.responseBody = new ResponseBody(resolver, exchange.getResponse());
        this.script = new Script(resolver, exchange);
        this.attempts = new ArrayList<MatchResult>();
    }

    public MatchResult matches(final StubRequest message) throws URISyntaxException {
        MatchResult result = requestPattern.match(message);
        if (LOGGER.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder("Match result: ");
            sb.append(result.score());
            for (MatchField field : result.getFields()) {
                sb.append(" field ").append(field.getFieldName());
                sb.append(" score: ").append(field.score());
            }
            LOGGER.trace(sb.toString());
        }
        if (result.score() >= 5) {
            // only record attempts that match request path
            attempts.add(result);
        }
        return result;
    }

    public StubExchange getExchange() {
        return exchange;
    }

    public StubResponse getResponse() {
        return exchange.getResponse();
    }

    public ResponseBody getResponseBody() {
        return responseBody;
    }

    public boolean isScript() {
        return script.getScript() != null;
    }

    public Script getScript() {
        return script;
    }

    public List<MatchResult> getAttempts() {
        return attempts;
    }

    @Override
    public String toString() {
        return requestPattern.toString();
    }

    @Override
    public int hashCode() {
        return requestPattern.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StubServiceExchange)) {
            return false;
        }
        StubServiceExchange other = (StubServiceExchange) obj;
        return requestPattern.equals(other.requestPattern);
    }
}
