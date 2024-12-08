import com.orbitz.consul.Consul;
import com.orbitz.consul.model.catalog.CatalogService;

import java.util.List;
import java.util.Optional;

public class ServiceDiscovery {
    public static final String URL = "http://Consul:8500";
    public static final String USER_SERVICE = "user-service";
    public static final String E_BIKE_SERVICE = "eBike-service";
    private final Consul consul;

    public ServiceDiscovery() {
        this.consul = Consul.builder()
                .withUrl(URL)
                .build();
    }

    public String userUrl() {
        final List<CatalogService> serviceList = this.consul.catalogClient().getService(USER_SERVICE).getResponse();
        final Optional<CatalogService> firstService = serviceList.stream().findFirst();
        return firstService.map(service -> "http://" + service.getServiceAddress() + ":" + service.getServicePort())
                .orElse("");
    }

    public String eBikeUrl() {
        final List<CatalogService> serviceList = this.consul.catalogClient().getService(E_BIKE_SERVICE).getResponse();
        final Optional<CatalogService> firstService = serviceList.stream().findFirst();
        return firstService.map(service -> "http://" + service.getServiceAddress() + ":" + service.getServicePort())
                .orElse("");
    }


}
