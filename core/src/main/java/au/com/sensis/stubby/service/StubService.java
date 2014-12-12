package au.com.sensis.stubby.service;

import java.util.List;

import au.com.sensis.stubby.model.StubExchange;
import au.com.sensis.stubby.model.StubRequest;
import au.com.sensis.stubby.service.model.StubServiceExchange;
import au.com.sensis.stubby.service.model.StubServiceResult;

/**
 * This interface describes the service operations.
 */
public interface StubService {
    /**
     * Load a list of stubbed exchanges from a file.
     *
     * @param filename the name of the file to load a list of stubbed exchanges from
     */
    void loadStubbedExchanges(String filename);

    /**
     * Add a stubbed exchange.
     *
     * @param exchange the stubbed exchange to add
     */
    void addStubbedExchange(StubExchange exchange);

    StubServiceResult findMatch(StubRequest request);

    StubServiceExchange getResponse(int index) throws NotFoundException;

    List<StubServiceExchange> getResponses();

    void deleteResponse(int index) throws NotFoundException;

    void deleteResponses();

    StubRequest getRequest(int index) throws NotFoundException;

    List<StubRequest> getRequests();

    List<StubRequest> findRequests(StubRequest filter, long timeout);

    List<StubRequest> findRequests(StubRequest filter);

    void deleteRequest(int index) throws NotFoundException;

    void deleteRequests();
}
