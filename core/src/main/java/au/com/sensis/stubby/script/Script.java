package au.com.sensis.stubby.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

/**
 * ...
 */
public class Script {
    /**
     * The default script type.
     */
    private static final String DEFAULT_SCRIPT_TYPE = "JavaScript";

    /**
     * The script source.
     */
    private String source;

    public String getSource() {
        return source;
    }

    /**
     * Constructor.
     *
     * @param source the script source
     */
    public Script(final String source) {
        this.source = source;
    }

    /**
     * Execute a script.
     *
     * @param world the environment to execute the script with
     * @return the return value of the script execution
     */
    public Object execute(final ScriptWorld world) {
        ScriptEngine engine = createEngine(world.getScriptType()); // TODO: refactor to use 'exhange.scriptType'

        engine.put("request", world.getRequest()); // TODO: refactor to use 'exchange.request'
        engine.put("response", world.getResponse()); // TODO: refactor to use 'exchange.response'
        engine.put("exchange", world);

        try {
            return engine.eval(source);
        } catch (ScriptException e) {
            throw new IllegalStateException("Error executing script", e);
        }
    }

    /**
     * Create a script engine.
     *
     * @param scriptType the script type
     * @return a script engine
     */
    private ScriptEngine createEngine(final String scriptType) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName(scriptType != null ? scriptType : DEFAULT_SCRIPT_TYPE);
        if (engine == null) {
            throw new IllegalArgumentException("Unsupported script type: " + scriptType);
        }
        engine.setContext(new SimpleScriptContext());
        return engine;
    }
}
