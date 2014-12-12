package au.com.sensis.stubby.model;

/**
 * This class describes a stubbed exchange.
 */
public class StubExchange {
    /**
     * The stubbed request.
     */
    private StubRequest request;
    /**
     * The stubbed response.
     */
    private StubResponse response;
    /**
     * The processing delay (in milliseconds).
     */
    private Long delay;
    /**
     * The script type.
     */
    private String scriptType;
    /**
     * The script.
     */
    private String script;
    /**
     * The script filename.
     */
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

    /**
     * Get the stubbed request.
     *
     * @return the stubbed request
     */
    public StubRequest getRequest() {
        return request;
    }

    /**
     * Set the stubbed request.
     *
     * @param request the stubbed request
     */
    public void setRequest(final StubRequest request) {
        this.request = request;
    }

    /**
     * Get the stubbed response.
     *
     * @return the stubbed response
     */
    public StubResponse getResponse() {
        return response;
    }

    /**
     * Set the stubbed response.
     *
     * @param response the stubbed response
     */
    public void setResponse(final StubResponse response) {
        this.response = response;
    }

    /**
     * Get the processing delay (in milliseconds).
     *
     * @return the processing delay
     */
    public Long getDelay() {
        return delay;
    }

    /**
     * Set the processing delay (in milliseconds).
     *
     * @param delay the processing delay
     */
    public void setDelay(final Long delay) {
        this.delay = delay;
    }

    /**
     * Get the script type.
     *
     * @return the script type
     */
    public String getScriptType() {
        return scriptType;
    }

    /**
     * Set the script type.
     *
     * @param scriptType the script type
     */
    public void setScriptType(final String scriptType) {
        this.scriptType = scriptType;
    }

    /**
     * Get the script.
     *
     * @return the script
     */
    public String getScript() {
        return script;
    }

    /**
     * Set the script.
     *
     * @param script the script
     */
    public void setScript(final String script) {
        this.script = script;
    }

    /**
     * Get the script filename.
     *
     * @return the script filename
     */
    public String getScriptFile() {
        return scriptFile;
    }

    /**
     * Set the script filename.
     *
     * @param scriptFile the script filename
     */
    public void setScriptFile(final String scriptFile) {
        this.scriptFile = scriptFile;
    }
}
