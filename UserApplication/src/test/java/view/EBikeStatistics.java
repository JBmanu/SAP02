package view;

import application.Application;
import application.EBikeControllerPort;
import application.RepositoryPort;
import application.concreate.ApplicationImpl;
import adapter.EBikeRepository;
import framework.repository.local.EBikeRepositoryImpl;
import adapter.UserRepository;
import framework.repository.local.UserRepositoryImpl;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EBikeStatistics {
    private static final float SOME_CREDITS = 150f;
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final EBikeRepository ebikeRepository = new EBikeRepositoryImpl();
    private Application application;
    private final String username = "username";
    private final String password = "password";
    private final String eBikeId = "0";

    @Given("The user has sign in and hired a e-bike")
    public void theUserHasSignInAndHiredAEBike() {
        final RepositoryPort repositoryPort = new RepositoryPort.RepositoryPortImpl(this.userRepository, this.ebikeRepository);
        this.ebikeRepository.add();
        this.application = new ApplicationImpl();
        this.application.setRepository(repositoryPort);
        this.application.setEBikeController(new EBikeControllerPort.EBikeControllerPortImpl());
        this.application.signUp(this.username, this.password);
        this.application.addCreditsOf(SOME_CREDITS);
    }

    @When("The user hired a e-bike")
    public void theUserHiredAEBike() {
        this.application.hireEBike(this.eBikeId);
        assertTrue(this.application.hasHireEBike());
    }

    @Then("The system shows statistics of the e-bike")
    public void theSystemShowsStatisticsOfTheEBike() {
        assertTrue(this.application.eBikeBattery().isPresent());
    }

    @When("The user ride the e-bike")
    public void theUserRideTheEBike() {
        this.application.hireEBike(this.eBikeId);
        assertTrue(this.application.hasHireEBike());
    }

    @Then("The system update and show the statistics of the e-bike")
    public void theSystemUpdateAndShowTheStatisticsOfTheEBike() {
        assertTrue(this.application.eBikeId().isPresent());
        assertTrue(this.application.eBikePosition().isPresent());
        assertTrue(this.application.eBikeState().isPresent());
    }
}
