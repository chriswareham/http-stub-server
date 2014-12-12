package au.com.sensis.stubby.service.model;

import java.util.regex.Pattern;

import org.apache.commons.lang.builder.EqualsBuilder;

import au.com.sensis.stubby.utils.EqualsUtils;
import au.com.sensis.stubby.utils.JsonUtils;

public class MatchField {

    public enum FieldType {
        PATH,
        METHOD,
        QUERY_PARAM,
        HEADER,
        BODY;
    }

    public enum MatchType {
        NOT_FOUND,
        MATCH_FAILURE,
        MATCH;
    }

    private FieldType fieldType;
    private String fieldName;
    private MatchType matchType;
    private Object expectedValue; // sometimes will be a Pattern, a JSON object etc.
    private Object actualValue; // could be string, JSON object etc.
    private String message;

    public MatchField(FieldType fieldType, String fieldName, Object expectedValue) {
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.expectedValue = expectedValue;
    }

    public MatchField asMatch(Object actualValue) {
        this.matchType = MatchType.MATCH;
        this.actualValue = actualValue;
        return this;
    }

    public MatchField asNotFound() {
        this.matchType = MatchType.NOT_FOUND;
        return this;
    }

    public MatchField asMatchFailure(Object actualValue) {
        this.matchType = MatchType.MATCH_FAILURE;
        this.actualValue = actualValue;
        return this;
    }

    public MatchField asMatchFailure(Object actualValue, String message) {
        this.matchType = MatchType.MATCH_FAILURE;
        this.actualValue = actualValue;
        this.message = message;
        return this;
    }

    public int score() { // attempt to give some weight to matches so we can guess 'near misses'
        switch (matchType) {
        case NOT_FOUND:
            return 0;
        case MATCH_FAILURE:
            switch (fieldType) {
            case PATH:
            case METHOD:
                return 0; // these guys always exist, so the fact that they're found is unimportant
            case HEADER:
            case QUERY_PARAM:
            case BODY:
                return 1; // if found but didn't match
            default:
                throw new IllegalStateException(); // impossible
            }
        case MATCH:
            switch (fieldType) {
            case PATH:
                return 5; // path is most important in the match
            default:
                return 2;
            }
        default:
            throw new IllegalStateException(); // impossible
        }
    }

    @Override
    public String toString() {
        return JsonUtils.serialize(this);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof MatchField)) {
            return false;
        }
        MatchField other = (MatchField) object;
        if (!fieldName.equals(other.fieldName) || !fieldType.equals(other.fieldType) || !matchType.equals(other.matchType)) {
            return false;
        }
        if (!EqualsUtils.equals(message, other.message)) {
            return false;
        }
        if (!EqualsUtils.equals(normalise(expectedValue), normalise(other.expectedValue))) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(actualValue, other.actualValue);
    }

    private Object normalise(final Object value) {
        if (value != null && value instanceof Pattern) {
            // compare regex string not 'Pattern' object
            return ((Pattern) value).pattern();
        }
        return value;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public Object getExpectedValue() {
        return expectedValue;
    }

    public Object getActualValue() {
        return actualValue;
    }

    public String getMessage() {
        return message;
    }
}
