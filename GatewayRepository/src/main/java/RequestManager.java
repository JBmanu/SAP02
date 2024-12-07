import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public final class RequestManager {
    private final OkHttpClient client;
    private final Gson gson;

    public RequestManager() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    private Request createGETRequest(final String url) {
        return new Request.Builder()
                .url(url)
                .build();
    }

    private Request createPOSTRequest(final String url, final String body) {
        return new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"), body))
                .build();
    }

    private String createResponse(final Request request) {
        try (final Response response = this.client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return this.gson.toJson(response.body().string());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String send(final String url) {
        return this.createResponse(this.createGETRequest(url));
    }

    public String send(final String url, final String body) {
        return this.createResponse(this.createPOSTRequest(url, body));
    }
}
