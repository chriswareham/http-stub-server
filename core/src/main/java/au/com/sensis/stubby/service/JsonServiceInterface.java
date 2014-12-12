package au.com.sensis.stubby.service;

import java.io.InputStream;
import java.io.OutputStream;

import au.com.sensis.stubby.model.StubExchange;
import au.com.sensis.stubby.model.StubRequest;
import au.com.sensis.stubby.utils.JsonUtils;

public class JsonServiceInterface { // interface using serialized JSON strings/streams

    private StubService service;

    public JsonServiceInterface(final StubService service) {
        this.service = service;
    }

    public void addResponse(final String exchange) {
        service.addStubbedExchange(JsonUtils.deserialize(exchange, StubExchange.class));
    }

    public void addResponse(final InputStream stream) {
        service.addStubbedExchange(JsonUtils.deserialize(stream, StubExchange.class));
    }

    public String getResponse(final int index) {
        return JsonUtils.serialize(service.getResponse(index));
    }

    public void getResponse(final OutputStream stream, final int index) {
        JsonUtils.serialize(stream, service.getResponse(index));
    }

    public String getResponses() {
        return JsonUtils.serialize(service.getResponses());
    }

    public void getResponses(final OutputStream stream) {
        JsonUtils.serialize(stream, service.getResponses());
    }

    public void deleteResponse(final int index) throws NotFoundException {
        service.deleteResponse(index);
    }

    public void deleteResponses() {
        service.deleteResponses();
    }

    public String getRequest(final int index) throws NotFoundException {
        return JsonUtils.serialize(service.getRequest(index));
    }

    public void getRequest(final OutputStream stream, final int index) throws NotFoundException {
        JsonUtils.serialize(stream, service.getRequest(index));
    }

    public String getRequests() {
        return JsonUtils.serialize(service.getRequests());
    }

    public void getRequests(final OutputStream stream) {
        JsonUtils.serialize(stream, service.getRequests());
    }

    public String findRequest(final StubRequest filter) {
        return JsonUtils.serialize(service.findRequests(filter));
    }

    public String findRequest(final StubRequest filter, final long wait) {
        return JsonUtils.serialize(service.findRequests(filter, wait));
    }

    public void findRequest(final OutputStream stream, final StubRequest filter) throws NotFoundException {
        JsonUtils.serialize(stream, service.findRequests(filter));
    }

    public void deleteRequest(final int index) throws NotFoundException {
        service.deleteRequest(index);
    }

    public void deleteRequests() {
        service.deleteRequests();
    }
}
