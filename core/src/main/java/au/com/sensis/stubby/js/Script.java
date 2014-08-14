package au.com.sensis.stubby.js;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

public class Script {
    private static final String DEFAULT_SCRIPT_TYPE = "JavaScript";

    private String source;

    public Script(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    private ScriptEngine createEngine(String scriptType) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName(scriptType != null ? scriptType : DEFAULT_SCRIPT_TYPE);
        if (engine == null) {
            throw new IllegalArgumentException("Unsupported script type: " + scriptType);
        }
        engine.setContext(new SimpleScriptContext());
        return engine;
    }

    public Object execute(ScriptWorld world) {
        ScriptEngine engine = createEngine(world.getScriptType()); // TODO: refactor to use 'exhange.scriptType'

        engine.put("request", world.getRequest()); // TODO: refactor to use 'exchange.request'
        engine.put("response", world.getResponse()); // TODO: refactor to use 'exchange.response'
        engine.put("exchange", world);

        try {
            return engine.eval(source); // note: return value is not used by stub server at present
        } catch (ScriptException e) {
            throw new RuntimeException("Error executing script", e);
        }
    }
}
