package au.com.sensis.stubby.service.model;

import au.com.sensis.stubby.model.StubExchange;
import au.com.sensis.stubby.utils.FileUtils;

public class Script {
    private String script;

    public Script(StubExchange exchange) {
        if (exchange.getScriptFile() != null) {
            this.script = FileUtils.read(exchange.getScriptFile());
        } else {
            this.script = exchange.getScript();
        }
    }

    public String getScript() {
        return script;
    }
}
