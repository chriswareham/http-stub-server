package au.com.sensis.stubby.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import au.com.sensis.stubby.script.Script;
import au.com.sensis.stubby.script.ScriptWorld;
import au.com.sensis.stubby.model.StubExchange;
import au.com.sensis.stubby.model.StubRequest;
import au.com.sensis.stubby.model.StubResponse;
import au.com.sensis.stubby.service.model.MatchResult;
import au.com.sensis.stubby.service.model.RequestPattern;
import au.com.sensis.stubby.service.model.StubServiceExchange;
import au.com.sensis.stubby.service.model.StubServiceResult;
import au.com.sensis.stubby.utils.FileUtils;
import au.com.sensis.stubby.utils.JsonUtils;
import au.com.sensis.stubby.utils.ResourceResolver;
import au.com.sensis.stubby.utils.StringUtils;

/**
 * This class implements the service operations.
 */
public class StubServiceImpl implements StubService {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(StubService.class);

    private LinkedList<StubServiceExchange> serviceExchanges = new LinkedList<StubServiceExchange>();
    private LinkedList<StubRequest> requests = new LinkedList<StubRequest>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadStubbedExchanges(final ResourceResolver resolver, final String filename) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Loading stubbed exchanges from " + filename);
        }
        lock.writeLock().lock();
        BufferedReader reader = null;
        try {
            InputStream responses = resolver.getResource(filename);
            reader = new BufferedReader(new InputStreamReader(responses));
            for (String fn = reader.readLine(); fn != null; fn = reader.readLine()) {
                if (!StringUtils.isBlank(fn)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Loading stubbed exchange from " + fn);
                    }
                    InputStream is = resolver.getResource(fn);
                    StubExchange exchange = JsonUtils.deserialize(is, StubExchange.class);
                    addResponseInternal(exchange);
                    FileUtils.close(is);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load responses from " + filename, e);
        } finally {
            FileUtils.close(reader);
            lock.writeLock().unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addStubbedExchange(StubExchange exchange) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Adding response: " + JsonUtils.prettyPrint(exchange));
        }
        lock.writeLock().lock();
        try {
            addResponseInternal(exchange);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public StubServiceResult findMatch(StubRequest request) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Got request: " + JsonUtils.prettyPrint(request));
        }

        lock.writeLock().lock();
        try {
            requests.addFirst(request);
        } finally {
            lock.writeLock().unlock();
        }

        try {
            lock.readLock().lock();
            List<MatchResult> attempts = new ArrayList<MatchResult>();
            for (StubServiceExchange serviceExchange : serviceExchanges) {
                MatchResult matchResult = serviceExchange.matches(request);
                attempts.add(matchResult);
                if (matchResult.matches()) {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Matched: " + request.getPath() + "");
                    }
                    StubExchange exchange = serviceExchange.getExchange();
                    if (serviceExchange.isScript()) {
                        ScriptWorld world = new ScriptWorld(request, serviceExchange.getResponseBody(), exchange); // creates deep copies of objects
                        new Script(serviceExchange.getScript().getScript()).execute(world);
                        return new StubServiceResult(attempts, world.getResponse(), world.getDelay());
                    } else {
                        StubResponse response = new StubResponse(exchange.getResponse());
                        if (serviceExchange.getResponseBody() != null) {
                            response.setBody(serviceExchange.getResponseBody().getBody());
                        }
                        return new StubServiceResult(attempts, response, exchange.getDelay());
                    }
                }
            }

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Didn't match: " + request.getPath());
            }

            notifyAll(); // inform any waiting threads that a new request has come in

            return new StubServiceResult(attempts); // no match (empty list)
        } catch (Exception e) {
            throw new RuntimeException("Error matching request", e);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public StubServiceExchange getResponse(int index) throws NotFoundException {
        lock.readLock().lock();
        try {
            return serviceExchanges.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException("Response does not exist: " + index);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<StubServiceExchange> getResponses() {
        lock.readLock().lock();
        try {
            return new ArrayList<StubServiceExchange>(serviceExchanges);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void deleteResponse(int index) throws NotFoundException {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Deleting response: " + index);
        }
        lock.writeLock().lock();
        try {
            serviceExchanges.remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Response does not exist: " + index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void deleteResponses() {
        LOGGER.trace("Deleting all responses");
        lock.writeLock().lock();
        try {
            serviceExchanges.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public StubRequest getRequest(int index) throws NotFoundException {
        lock.readLock().lock();
        try {
            return requests.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException("Response does not exist: " + index);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<StubRequest> getRequests() {
        lock.readLock().lock();
        try {
            return new ArrayList<StubRequest>(requests);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<StubRequest> findRequests(StubRequest filter, long timeout) { // blocking call
        long remaining = timeout;
        while (remaining > 0) {
            List<StubRequest> result = findRequests(filter);
            if (result.isEmpty()) {
                try {
                    long start = System.currentTimeMillis();
                    wait(remaining); // wait for a request to come in, or time to expire
                    remaining -= System.currentTimeMillis() - start;
                } catch (InterruptedException e) {
                    throw new RuntimeException("Interrupted while waiting for request");
                }
            } else {
                return result;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<StubRequest> findRequests(StubRequest filter) {
        lock.readLock().lock();
        try {
            RequestPattern pattern = new RequestPattern(filter);
            List<StubRequest> result = new ArrayList<StubRequest>();
            for (StubRequest request : requests) {
                if (pattern.match(request).matches()) {
                    result.add(request);
                }
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void deleteRequest(int index) throws NotFoundException {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Deleting request: " + index);
        }
        lock.writeLock().lock();
        try {
            requests.remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException("Request does not exist: " + index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void deleteRequests() {
        LOGGER.trace("Deleting all requests");
        lock.writeLock().lock();
        try {
            requests.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void addResponseInternal(StubExchange exchange) {
        StubServiceExchange internal = new StubServiceExchange(exchange);
        // remove existing stubbed request (ie, will never match anymore)
        serviceExchanges.remove(internal);
        // ensure most recent match first
        serviceExchanges.addFirst(internal);
    }
}
