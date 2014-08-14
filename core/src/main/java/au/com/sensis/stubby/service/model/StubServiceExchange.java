package au.com.sensis.stubby.service.model;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import au.com.sensis.stubby.model.StubExchange;
import au.com.sensis.stubby.model.StubRequest;
import au.com.sensis.stubby.model.StubResponse;

public class StubServiceExchange { // wrap exchange model with some extra runtime info

    private static final Logger LOGGER = Logger.getLogger(StubServiceExchange.class);

    private StubExchange exchange;
    private RequestPattern requestPattern;
    private ResponseBody responseBody;
    private Script script;
    private List<MatchResult> attempts;

    public StubServiceExchange(StubExchange exchange) {
        this.exchange = exchange;
        this.requestPattern = new RequestPattern(exchange.getRequest());
        this.responseBody = new ResponseBody(exchange.getResponse());
        this.script = new Script(exchange);
        this.attempts = new ArrayList<MatchResult>();
    }

    public MatchResult matches(StubRequest message) throws URISyntaxException {
        MatchResult result = requestPattern.match(message);
        for (MatchField field : result.getFields()) {
            LOGGER.trace("Match outcome: " + field);
        }
        LOGGER.trace("Match score: " + result.score());
        if (result.score() >= 5) { // only record attempts that match request path
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
    public int hashCode() { // hash/equality is based on the request pattern only
        return requestPattern.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof StubServiceExchange)
                && ((StubServiceExchange)obj).requestPattern.equals(requestPattern);
    }

}
