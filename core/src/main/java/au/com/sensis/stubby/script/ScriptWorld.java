package au.com.sensis.stubby.script;

import au.com.sensis.stubby.model.StubExchange;
import au.com.sensis.stubby.model.StubRequest;
import au.com.sensis.stubby.model.StubResponse;
import au.com.sensis.stubby.service.model.ResponseBody;

/**
 * The world as a script sees it.
 */
public class ScriptWorld {
    /**
     * The script type.
     */
    private String scriptType;
    /**
     * The request.
     */
    private StubRequest request;
    /**
     * The response.
     */
    private StubResponse response;
    /**
     * The delay the script can introduce in handling the request.
     */
    private Long delay;

    /**
     * Creates the world as a script sees it by copying the server state.
     *
     * @param request the request
     * @param responseBody the response body
     * @param exchange the current exchange
     */
    public ScriptWorld(final StubRequest request, final ResponseBody responseBody, final StubExchange exchange) {
        this.scriptType = exchange.getScriptType();
        this.request = new StubRequest(request);
        this.response = new StubResponse(exchange.getResponse());
        if (responseBody != null) {
            response.setBody(responseBody.getBody());
        }
        this.delay = exchange.getDelay();
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
     * Get the request.
     *
     * @return the request
     */
    public StubRequest getRequest() {
        return request;
    }

    /**
     * Get the response.
     *
     * @return the response
     */
    public StubResponse getResponse() {
        return response;
    }

    /**
     * Get the delay the script can introduce in handling the request.
     *
     * @return the delay the script can introduce in handling the request
     */
    public Long getDelay() {
        return delay;
    }

    /**
     * Set the delay the script can introduce in handling the request.
     *
     * @param delay the delay the script can introduce in handling the request
     */
    public void setDelay(final Long delay) {  // allow this to be changed for current request
        this.delay = delay;
    }
}
