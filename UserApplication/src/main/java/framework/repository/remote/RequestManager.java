package framework.repository.remote;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

public class RequestManager {
    private final Gson gson;

    public RequestManager() {
        this.gson = new Gson();
    }

    public <T> Optional<T> sendGet(final String urlPath, final TypeToken<T> typeToken) {
        final RestTemplate restTemplate = new RestTemplate();
        Optional<T> response = Optional.empty();
        try {
            final String json = restTemplate.getForObject(urlPath, String.class);
            final T object = this.gson.fromJson(json, typeToken.getType());
            response = Optional.ofNullable(object);
        } catch (final Exception ignored) {
        }
        return response;
    }

    public <T> Optional<T> sendPost(final String urlPath, final TypeToken<T> typeToken, final Map<String, String> request) {
        final RestTemplate restTemplate = new RestTemplate();
        Optional<T> response = Optional.empty();
        final String message = this.gson.toJson(request);
        try {
            final String json = restTemplate.postForObject(urlPath, message, String.class);
            final T object = this.gson.fromJson(json, typeToken.getType());
            response = Optional.ofNullable(object);
        } catch (final Exception ignored) {
        }
        return response;
    }
}
