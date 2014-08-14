package au.com.sensis.stubby.js;

import au.com.sensis.stubby.model.StubExchange;
import au.com.sensis.stubby.model.StubRequest;
import au.com.sensis.stubby.model.StubResponse;
import au.com.sensis.stubby.service.model.ResponseBody;

public class ScriptWorld { // the world as a script sees it
    private String scriptType;
    private StubRequest request;
    private StubResponse response;
    private Long delay;

    public ScriptWorld(StubRequest request, ResponseBody responseBody, StubExchange exchange) { // copy everything so the script can't change the server state
        this.scriptType = exchange.getScriptType();
        this.request = new StubRequest(request);
        this.response = new StubResponse(exchange.getResponse());
        if (responseBody != null) {
            response.setBody(responseBody.getBody());
        }
        this.delay = exchange.getDelay();
    }

    public String getScriptType() {
        return scriptType;
    }

    public StubRequest getRequest() {
        return request;
    }

    public StubResponse getResponse() {
        return response;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {  // allow this to be changed for current request
        this.delay = delay;
    }
}
