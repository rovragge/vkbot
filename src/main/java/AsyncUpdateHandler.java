import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

public class AsyncUpdateHandler implements FutureCallback<HttpResponse> {

    private ResponseHandler<String> handler;

    AsyncUpdateHandler() {
        handler = new BasicResponseHandler();
    }

    @Override
    public void completed(HttpResponse result) {
        try {
            System.out.println(handler.handleResponse(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Exception ex) {

    }

    @Override
    public void cancelled() {

    }
}
