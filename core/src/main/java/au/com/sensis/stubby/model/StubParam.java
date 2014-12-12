package au.com.sensis.stubby.model;

public class StubParam {

    private String name;

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
     * @param param the paramater to copy
     */
    public StubParam(final StubParam param) {
        this.name = param.name;
        this.value = param.value;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }
}
