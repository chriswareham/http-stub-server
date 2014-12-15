package au.com.sensis.stubby.service.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import au.com.sensis.stubby.model.StubParam;
import au.com.sensis.stubby.model.StubRequest;
import au.com.sensis.stubby.utils.EqualsUtils;
import au.com.sensis.stubby.utils.FileUtils;
import au.com.sensis.stubby.utils.ResourceResolver;

public class RequestPattern {

    public static final Pattern DEFAULT_PATTERN = Pattern.compile(".*");

    private Pattern method;

    private Pattern path;

    private Set<ParamPattern> params;

    private Set<ParamPattern> headers;

    private BodyPattern body;

    public RequestPattern(final ResourceResolver resolver, final StubRequest request) {
        this.method = toPattern(request.getMethod());
        this.path = toPattern(request.getPath());
        this.params = toPattern(request.getParams());
        this.headers = toPattern(request.getHeaders());
        if (request.getFile() != null) {
            this.body = toBodyPattern(FileUtils.read(resolver.getResource(request.getFile())));
        } else {
            this.body = toBodyPattern(request.getBody());
        }
    }

    public MatchResult match(final StubRequest message) {
        MatchResult result = new MatchResult();

        MatchField methodField = new MatchField(FieldType.METHOD, "method", method);
        if (method != null) {
            if (method.matcher(message.getMethod()).matches()) {
                result.addField(methodField.asMatch(message.getMethod()));
            } else {
                result.addField(methodField.asMatchFailure(message.getMethod()));
            }
        }

        MatchField pathField = new MatchField(FieldType.PATH, "path", path);
        if (path.matcher(message.getPath()).matches()) {
            result.addField(pathField.asMatch(message.getPath()));
        } else {
            result.addField(pathField.asMatchFailure(message.getPath()));
        }

        for (ParamPattern paramPattern : params) {
            result.addField(matchParam(message, paramPattern));
        }

        for (ParamPattern headerPattern : headers) {
            result.addField(matchHeader(message, headerPattern));
        }

        if (body != null) {
            if (message.getBody() != null) {
                result.addField(body.matches(message));
            } else {
                result.addField(new MatchField(FieldType.BODY, "body", "<pattern>").asNotFound());
            }
        }

        return result;
    }

    public Pattern getMethod() {
        return method;
    }

    public Pattern getPath() {
        return path;
    }

    public BodyPattern getBody() {
        return body;
    }

    public Set<ParamPattern> getParams() {
        return Collections.unmodifiableSet(params);
    }

    public Set<ParamPattern> getHeaders() {
        return Collections.unmodifiableSet(headers);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof RequestPattern)
                && ((RequestPattern) obj).method.pattern().equals(method.pattern())
                && ((RequestPattern) obj).path.pattern().equals(path.pattern())
                && ((RequestPattern) obj).params.equals(params)
                && ((RequestPattern) obj).headers.equals(headers)
                && EqualsUtils.equals(((RequestPattern) obj).body, body);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (method  == null ? 0 : method.pattern().hashCode());
        result = prime * result + (path    == null ? 0 : path.pattern().hashCode());
        result = prime * result + (body    == null ? 0 : body.hashCode());
        result = prime * result + (headers == null ? 0 : headers.hashCode());
        result = prime * result + (params  == null ? 0 : params.hashCode());
        return result;
    }

    private Pattern toPattern(final String value) {
        return value != null ? Pattern.compile(value) : DEFAULT_PATTERN;
    }

    private Set<ParamPattern> toPattern(final List<StubParam> values) {
        Set<ParamPattern> patterns = Collections.emptySet();
        if (values != null) {
            patterns = new HashSet<ParamPattern>(values.size());
            for (StubParam value : values) {
                patterns.add(new ParamPattern(value.getName(), toPattern(value.getValue())));
            }
        }
        return patterns;
    }

    private BodyPattern toBodyPattern(final Object object) {
        if (object != null) {
            if (object instanceof String) {
                return new TextBodyPattern(object.toString());
            } else if (object instanceof Map || object instanceof List) {
                return new JsonBodyPattern(object);
            } else {
                throw new IllegalArgumentException("Unexpected body type: " + object);
            }
        } else {
            return null; // don't match body
        }
    }

    private MatchField matchParam(final StubRequest message, final ParamPattern pattern) {
        MatchField field = new MatchField(FieldType.QUERY_PARAM, pattern.getName(), pattern.getPattern());
        List<String> values = message.getParams(pattern.getName());
        if (!values.isEmpty()) {
            for (String value : values) {
                if (pattern.getPattern().matcher(value).matches()) {
                    return field.asMatch(value);
                }
            }
            return field.asMatchFailure(values.size() > 1 ? values : values.get(0)); // don't wrap in array if only single value
        } else {
            return field.asNotFound();
        }
    }

    private MatchField matchHeader(final StubRequest message, final ParamPattern pattern) {
        MatchField field = new MatchField(FieldType.HEADER, pattern.getName(), pattern.getPattern());
        List<String> values = message.getHeaders(pattern.getName()); // case insensitive lookup
        if (!values.isEmpty()) {
            for (String value : values) {
                if (pattern.getPattern().matcher(value).matches()) {
                    return field.asMatch(value);
                }
            }
            return field.asMatchFailure(values.size() > 1 ? values : values.get(0)); // don't wrap in array if only single value
        } else {
            return field.asNotFound();
        }
    }
}
