package au.com.sensis.stubby.model;

public class StubResponse extends StubMessage {

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }
}
