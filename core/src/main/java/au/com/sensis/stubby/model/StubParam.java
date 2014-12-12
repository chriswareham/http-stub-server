package au.com.sensis.stubby.model;

/**
 * This class describes a stubbed parameter.
 */
public class StubParam {
    /**
     * The parameter name.
     */
    private String name;
    /**
     * The parameter value.
     */
    private String value;

    /**
     * Default constructor.
     */
    public StubParam() {
        super();
    }

    /**
     * Full constructor.
     *
     * @param name the parameter name
     * @param value the parameter value
     */
    public StubParam(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Copy constructor.
     *
     * @param param the parameter to copy
     */
    public StubParam(final StubParam param) {
        this.name = param.name;
        this.value = param.value;
    }

    /**
     * Get the parameter name.
     *
     * @return the parameter name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the parameter name.
     *
     * @param name the parameter name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get the parameter value.
     *
     * @return the parameter value
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the parameter value.
     *
     * @param value the parameter value
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name + "=" + value;
    }
}
