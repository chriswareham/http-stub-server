package au.com.sensis.stubby.test.client;

import au.com.sensis.stubby.test.model.JsonExchange;

public class MessageBuilder {

    private Client client;
    private JsonExchange exchange;

    public MessageBuilder(final Client client) {
        this.client = client;
        this.exchange = new JsonExchange();
    }

    public MessageBuilder setDelay(final Long delay) {
        exchange.delay = delay;
        return this;
    }

    public MessageBuilder setScript(final String script) {
        exchange.script = script;
        return this;
    }

    public MessageBuilder setRequestMethod(final String method) {
        exchange.request().method = method;
        return this;
    }

    public MessageBuilder setRequestPath(final String path) {
        exchange.request().path = path;
        return this;
    }

    public MessageBuilder setResponseStatus(final Integer status) {
        exchange.response().status = status;
        return this;
    }

    public MessageBuilder setResponseBody(final Object body) {
        exchange.response().body = body;
        return this;
    }

    public MessageBuilder setRequestBody(final Object body) {
        exchange.request().body = body;
        return this;
    }

    public MessageBuilder addRequestParam(final String name, final String value) {
        exchange.request().addParam(name, value);
        return this;
    }

    public MessageBuilder setRequestParam(final String name, final String value) {
        exchange.request().setParam(name, value);
        return this;
    }

    public MessageBuilder setRequestHeader(final String name, final String value) {
        exchange.request().setHeader(name, value);
        return this;
    }

    public MessageBuilder addRequestHeader(final String name, final String value) {
        exchange.request().addHeader(name, value);
        return this;
    }

    public MessageBuilder setResponseHeader(final String name, final String value) {
        exchange.response().setHeader(name, value);
        return this;
    }

    public MessageBuilder addResponseHeader(final String name, final String value) {
        exchange.response().addHeader(name, value);
        return this;
    }

    public MessageBuilder stub() {
        client.postMessage(exchange);
        return this;
    }
}
