import adapter.UserViewEventPort;
import adapter.View;
import application.Application;
import application.RepositoryPort;
import application.concreate.ApplicationImpl;
import entity.ebike.EBikeRepository;
import entity.ebike.concreate.EBikeRepositoryImpl;
import entity.user.UserRepository;
import entity.user.concreate.UserRepositoryImpl;
import framework.view.ViewImpl;

public final class Launcher {
    public static void main(final String[] args) {
        final UserRepository userRepository = new UserRepositoryImpl();
        final EBikeRepository ebikeRepository = new EBikeRepositoryImpl();
        for (int i = 0; i < 10; i++) ebikeRepository.add();

        final Application application = new ApplicationImpl();
        application.setRepository(new RepositoryPort.RepositoryPortImpl(userRepository, ebikeRepository));

        final UserViewEventPort userViewEventPort = new UserViewEventPort.UserViewEventPortImpl(application);
        final View view = new ViewImpl();
        view.setEventPort(userViewEventPort);

//        final Application application1 = new ApplicationImpl();
//        application1.setRepository(new RepositoryPort.RepositoryPortImpl(userRepository, ebikeRepository));
//
//        final UserViewEventPort userViewEventPort1 = new UserViewEventPort.UserViewEventPortImpl(application1);
//        final View view1 = new ViewImpl();
//        view1.setEventPort(userViewEventPort1);

    }
}
