package au.com.sensis.stubby.service.model;

import au.com.sensis.stubby.model.StubResponse;
import au.com.sensis.stubby.utils.FileUtils;
import au.com.sensis.stubby.utils.ResourceResolver;

public class ResponseBody {
    private Object body;

    public ResponseBody(final ResourceResolver resolver, final StubResponse response) {
        if (response != null && response.getFile() != null) {
            this.body = FileUtils.read(resolver.getResource(response.getFile()));
        } else if (response != null) {
            this.body = response.getBody();
        }
    }

    public Object getBody() {
        return body;
    }
}
