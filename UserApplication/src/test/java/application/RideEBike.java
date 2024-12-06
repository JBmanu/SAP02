package application;

import application.concreate.ApplicationImpl;
import adapter.EBikeRepository;
import framework.repository.EBikeRepositoryImpl;
import adapter.UserRepository;
import framework.repository.UserRepositoryImpl;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class RideEBike {
    public static final float SOME_CREDITS = 15f;
    public static final float ZERO_CREDITS = 0;
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final EBikeRepository eBikeRepository = new EBikeRepositoryImpl();
    private final RepositoryPort repositoryPort = new RepositoryPort.RepositoryPortImpl(this.userRepository, this.eBikeRepository);
    private Application application;

    @Given("The user {string} has sign in with {string}")
    public void theUserHasSignInWith(final String username, final String password) {
        this.application = new ApplicationImpl();
        this.application.setRepository(this.repositoryPort);
        this.application.setEBikeController(new EBikeControllerPort.EBikeControllerPortImpl());
        this.eBikeRepository.add();

        this.application.signUp(username, password);
        this.application.addCreditsOf(SOME_CREDITS);
        assertTrue(this.application.userIsLogged());
    }
    @And("The user hire e-bike {string}")
    public void theUserHireEBike(final String eBikeId) {
        this.application.hireEBike(eBikeId);
        assertTrue(this.application.hasHireEBike());
    }


    @Given("The user has some credit")
    public void theUserHasSomeCredit() {
        assertTrue(this.application.creditsOfUser().get() > ZERO_CREDITS);
    }
    @When("The user ride e-bike")
    public void theUserHireAndRideEBike() {
        assertTrue(this.application.hasHireEBike());
    }


    @Then("The system subtract the credit, notify the user")
    public void theSystemSubtractTheCreditNotifyTheUser() {
        try {
            sleep(1000);
        } catch (final InterruptedException ignored) { }
        assertTrue(this.application.creditsOfUser().get() < SOME_CREDITS - Application.CREDITS_FOR_HIRE);
        this.application.stopEBike();
    }

    @Given("The user has no credit")
    public void theUserHasNoCredit() {
        while (this.application.creditsOfUser().get() > ZERO_CREDITS) {
            this.application.withdrawCredits();
        }
        assertEquals(ZERO_CREDITS, this.application.creditsOfUser().get());
    }
    @Then("The system stops the ride, notify the user and free the e-bike with id {string}")
    public void theSystemStopsTheRideNotifyTheUserAndFreeTheEBike(final String eBikeId) {
        try {
            sleep(1000);
        } catch (final InterruptedException ignored) { }
        assertFalse(this.application.hasHireEBike());
        assertTrue(this.application.isFreeEBike(eBikeId));
    }


    @Given("The e-bike {string} is with low battery")
    public void theEBikeIsWithLowBattery(final String eBikeId) {
        this.repositoryPort.consumeBattery(eBikeId, 100);
        assertTrue(this.application.isLowBatteryEBike(eBikeId));
    }
    @Then("The system stops the ride, notify the user and low battery the e-bike {string}")
    public void theSystemStopsTheRideNotifyTheUserAndLowBatteryTheEBike(final String eBikeId) {
        try {
            sleep(1000);
        } catch (final InterruptedException ignored) { }
        assertFalse(this.application.hasHireEBike());
        assertTrue(this.application.isLowBatteryEBike(eBikeId));
    }
}
