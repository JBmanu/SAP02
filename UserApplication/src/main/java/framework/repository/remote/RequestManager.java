package framework.repository.remote;

import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

import static framework.repository.remote.Root.URL_ROOT;

public class RequestManager {
    private final Gson gson;

    public RequestManager() {
        this.gson = new Gson();
    }

    public <T> Optional<T> sendPost(final String urlPath, final Class<T> responseType) {
        final RestTemplate restTemplate = new RestTemplate();
        Optional<T> response = Optional.empty();
        try {
            response = Optional.ofNullable(restTemplate.getForObject(URL_ROOT + urlPath, responseType));
        } catch (final Exception ignored) {
        }
        return response;
    }

    public <T> Optional<T> sendPost(final String urlPath, final Class<T> responseType, final Map<String, String> request) {
        final RestTemplate restTemplate = new RestTemplate();
        Optional<T> response = Optional.empty();
        final String message = this.gson.toJson(request);
        try {
            response = Optional.ofNullable(restTemplate.postForObject(URL_ROOT + urlPath, message, responseType));
        } catch (final Exception ignored) {
        }
        return response;
    }



}
