import adapter.port.ViewEventPort;
import adapter.View;
import application.Application;
import application.EBikeControllerPort;
import application.ViewPort;
import application.concreate.ApplicationImpl;
import adapter.EBikeRepository;
import application.concreate.RepositoryClientPort;
import framework.repository.EBikeRepositoryImpl;
import framework.view.ViewImpl;

public final class Launcher {
    public static void main(final String[] args) {
        final EBikeRepository ebikeRepository = new EBikeRepositoryImpl();
        for (int i = 0; i < 10; i++) ebikeRepository.add();

        final EBikeControllerPort ebikeControllerPort = new EBikeControllerPort.EBikeControllerPortImpl();
        final Application application = new ApplicationImpl();
        application.setRepository(new RepositoryClientPort(ebikeRepository));
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

    }
}
