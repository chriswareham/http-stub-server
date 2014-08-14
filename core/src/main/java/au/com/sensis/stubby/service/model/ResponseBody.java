package au.com.sensis.stubby.service.model;

import au.com.sensis.stubby.model.StubResponse;
import au.com.sensis.stubby.utils.FileUtils;

public class ResponseBody {
    private Object body;

    public ResponseBody(StubResponse response) {
        if (response != null && response.getFile() != null) {
            this.body = FileUtils.read(response.getFile());
        } else if (response != null) {
            this.body = response.getBody();
        }
    }

    public Object getBody() {
        return body;
    }
}
