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

        final RepositoryPort repositoryPort = new RepositoryPort.RepositoryPortImpl(userRepository, ebikeRepository);
        final Application application = new ApplicationImpl();
        application.setUserRepository(repositoryPort);

        final UserViewEventPort userViewEventPort = new UserViewEventPort.UserViewEventPortImpl(application);
        final View view = new ViewImpl();
        view.setEventPort(userViewEventPort);

    }
}
