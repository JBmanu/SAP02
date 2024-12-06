import adapter.ViewEventPort;
import adapter.View;
import application.Application;
import application.EBikeControllerPort;
import application.RepositoryPort;
import application.ViewPort;
import application.concreate.ApplicationImpl;
import entity.ebike.EBikeRepository;
import entity.ebike.concreate.EBikeRepositoryImpl;
import entity.user.UserRepository;
import entity.user.concreate.UserRepositoryImpl;
import framework.view.ViewImpl;
import org.springframework.web.client.RestTemplate;


public final class Launcher {
    public static void main(final String[] args) {
        final UserRepository userRepository = new UserRepositoryImpl();
        final EBikeRepository ebikeRepository = new EBikeRepositoryImpl();
        for (int i = 0; i < 10; i++) ebikeRepository.add();

        final EBikeControllerPort ebikeControllerPort = new EBikeControllerPort.EBikeControllerPortImpl();
        final Application application = new ApplicationImpl();
        application.setRepository(new RepositoryPort.RepositoryPortImpl(userRepository, ebikeRepository));
        application.setEBikeController(ebikeControllerPort);

        final ViewEventPort viewEventPort = new ViewEventPort.ViewEventPortImpl(application);
        final View view = new ViewImpl();
        view.setEventPort(viewEventPort);
        application.setView(new ViewPort.ViewPortImpl(view));

//        final Application application1 = new ApplicationImpl();
//        application1.setRepository(new RepositoryPort.RepositoryPortImpl(userRepository, ebikeRepository));
//
//        final UserViewEventPort userViewEventPort1 = new UserViewEventPort.UserViewEventPortImpl(application1);
//        final View view1 = new ViewImpl();
//        view1.setEventPort(userViewEventPort1);


        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:3000/users";

        try {
            final String response = restTemplate.getForObject(url, String.class);
            System.out.println(response);
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }
}
