import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;

public class RegisterService {

    public static final String URL = "http://Consul:8500";
    public static final String NAME_SERVICE = "eBike-service";
    public static final String ADDRESS_SERVICE = "EBikeRepository";
    private final Consul consul;

    public RegisterService() {
        this.consul = Consul.builder()
                .withUrl(URL)
                .build();
    }

    public void register(final int port) {
        this.consul.agentClient().register(ImmutableRegistration.builder()
                .id(NAME_SERVICE)
                .name(NAME_SERVICE)
                .address(ADDRESS_SERVICE)
                .port(port)
                .build());
    }


}
