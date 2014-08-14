package au.com.sensis.stubby.model;

public class StubExchange {

    private StubRequest request;
    private StubResponse response;
    private Long delay;
    private String scriptType;
    private String script;
    private String scriptFile;

    public StubExchange() { }

    public StubExchange(StubExchange other) { // copy constructor
        this.request = (other.request != null) ? new StubRequest(other.request) : null;
        this.response = (other.response != null) ? new StubResponse(other.response) : null;
        this.delay = other.delay;
        this.scriptType = other.scriptType;
        this.script = other.script;
        this.scriptFile = other.scriptFile;
    }

    public StubRequest getRequest() {
        return request;
    }

    public void setRequest(StubRequest request) {
        this.request = request;
    }

    public StubResponse getResponse() {
        return response;
    }

    public void setResponse(StubResponse response) {
        this.response = response;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScriptFile() {
        return scriptFile;
    }

    public void setScriptFile(String scriptFile) {
        this.scriptFile = scriptFile;
    }
}
