package au.com.sensis.stubby.service.model;

import au.com.sensis.stubby.model.StubExchange;
import au.com.sensis.stubby.utils.FileUtils;
import au.com.sensis.stubby.utils.ResourceResolver;

public class Script {

    private String script;

    public Script(final ResourceResolver resolver, final StubExchange exchange) {
        if (exchange.getScriptFile() != null) {
            this.script = FileUtils.read(resolver.getResource(exchange.getScriptFile()));
        } else {
            this.script = exchange.getScript();
        }
    }

    public String getScript() {
        return script;
    }
}
