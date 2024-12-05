package application;

import application.concreate.ApplicationImpl;
import entity.ebike.EBikeRepository;
import entity.ebike.concreate.EBikeRepositoryImpl;
import entity.user.UserRepository;
import entity.user.concreate.UserRepositoryImpl;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.*;

public class StopEBike {
    public static final float SOME_CREDITS = 100f;
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final EBikeRepository eBikeRepository = new EBikeRepositoryImpl();
    private final RepositoryPort repositoryPort = new RepositoryPort.RepositoryPortImpl(this.userRepository, this.eBikeRepository);
    private Application application;

    @Given("The user has sign in with {string} and {string}")
    public void theUserHasSignInWithAnd(final String username, final String password) {
        this.application = new ApplicationImpl();
        this.eBikeRepository.add();
        this.application.setRepository(this.repositoryPort);

        this.application.signUp(username, password);
        this.application.addCreditsOf(SOME_CREDITS);
        assertTrue(this.application.userIsLogged());
    }

    @When("The user ride e-bike {string}")
    public void theUserRideEBike(final String eBikeId) {
        this.application.hireEBike(eBikeId);
        assertTrue(this.application.hasHireEBike());
    }

    @Then("The system stops the ride, notify the user and free the e-bike {string}")
    public void theSystemStopsTheRideNotifyTheUserAndFreeTheEBike(final String eBikeId) {
        this.application.stopEBike();
        assertFalse(this.application.hasHireEBike());
        assertTrue(this.application.isFreeEBike(eBikeId));
    }
}
