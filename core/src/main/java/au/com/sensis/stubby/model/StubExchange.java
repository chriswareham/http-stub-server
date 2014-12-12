package au.com.sensis.stubby.model;

public class StubExchange {

    private StubRequest request;

    private StubResponse response;

    private Long delay;

    private String scriptType;

    private String script;

    private String scriptFile;

    /**
     * Default constructor.
     */
    public StubExchange() {
        super();
    }

    /**
     * Copy constructor.
     *
     * @param exchange the exchange to copy
     */
    public StubExchange(final StubExchange exchange) {
        request = exchange.request != null ? new StubRequest(exchange.request) : null;
        response = exchange.response != null ? new StubResponse(exchange.response) : null;
        delay = exchange.delay;
        scriptType = exchange.scriptType;
        script = exchange.script;
        scriptFile = exchange.scriptFile;
    }

    public StubRequest getRequest() {
        return request;
    }

    public void setRequest(final StubRequest request) {
        this.request = request;
    }

    public StubResponse getResponse() {
        return response;
    }

    public void setResponse(final StubResponse response) {
        this.response = response;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(final Long delay) {
        this.delay = delay;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(final String scriptType) {
        this.scriptType = scriptType;
    }

    public String getScript() {
        return script;
    }

    public void setScript(final String script) {
        this.script = script;
    }

    public String getScriptFile() {
        return scriptFile;
    }

    public void setScriptFile(final String scriptFile) {
        this.scriptFile = scriptFile;
    }
}
