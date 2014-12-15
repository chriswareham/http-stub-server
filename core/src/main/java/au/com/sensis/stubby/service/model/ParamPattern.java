package au.com.sensis.stubby.service.model;

import java.util.regex.Pattern;

public class ParamPattern {
    /**
     * The parameter name.
     */
    private String name;
    /**
     * The paramter value pattern.
     */
    private Pattern pattern;

    /**
     * Constructor.
     *
     * @param name the parameter name
     * @param pattern the parameter value pattern
     */
    public ParamPattern(String name, Pattern pattern) {
        this.name = name;
        this.pattern = pattern;
    }

    /**
     * Copy constructor.
     *
     * @other the parameter pattern to copy
     */
    public ParamPattern(ParamPattern other) {
        this.name = other.name;
        this.pattern = other.pattern;
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
     * Get the parameter value pattern.
     *
     * @return the parameter value pattern
     */
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return name + " =~ m/" + pattern.pattern() + "/";
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ParamPattern)
                && ((ParamPattern)obj).name.equals(name)
                && ((ParamPattern)obj).pattern.pattern().equals(pattern.pattern());
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + name.hashCode();
        result = 31 * result + pattern.pattern().hashCode();
        return result;
    }
}
