import adapter.UserViewEventPort;
import adapter.View;
import application.Application;
import application.UserRepositoryPort;
import application.concreate.ApplicationImpl;
import entity.user.UserRepository;
import entity.user.concreate.UserRepositoryImpl;
import framework.view.ViewImpl;

public final class Launcher {
    public static void main(final String[] args) {
        final UserRepository userRepository = new UserRepositoryImpl();

        final UserRepositoryPort userRepositoryPort = new UserRepositoryPort.UserRepositoryPortImpl(userRepository);
        final Application application = new ApplicationImpl();
        application.setUserRepository(userRepositoryPort);

        final UserViewEventPort userViewEventPort = new UserViewEventPort.UserViewEventPortImpl(application);
        final View view = new ViewImpl();
        view.setEventPort(userViewEventPort);

    }
}
