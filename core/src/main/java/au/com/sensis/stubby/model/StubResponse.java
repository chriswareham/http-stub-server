package au.com.sensis.stubby.model;

/**
 * This class describes a stubbed response.
 */
public class StubResponse extends StubMessage {
    /**
     * The response status.
     */
    private Integer status;

    /**
     * Default constructor.
     */
    public StubResponse() {
        super();
    }

    /**
     * Copy constructor.
     *
     * @param response the response to copy
     */
    public StubResponse(final StubResponse response) {
        super(response);
        this.status = response.status;
    }

    /**
     * Get the response status.
     *
     * @return the response status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Set the response status.
     *
     * @param status the response status
     */
    public void setStatus(final Integer status) {
        this.status = status;
    }
}
