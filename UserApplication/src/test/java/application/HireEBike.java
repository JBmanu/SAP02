package application;

import application.concreate.ApplicationImpl;
import adapter.EBikeRepository;
import framework.repository.local.EBikeRepositoryImpl;
import adapter.UserRepository;
import framework.repository.local.UserRepositoryImpl;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class HireEBike {
    public static final float SOME_CREDITS = 100f;
    public static final float ZERO_CREDITS = 0;
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final EBikeRepository eBikeRepository = new EBikeRepositoryImpl();
    private final RepositoryPort repositoryPort = new RepositoryPort.RepositoryPortImpl(this.userRepository, this.eBikeRepository);
    private Application application;
    private Optional<Message> error;

    @Given("The user {string} has sign in with password {string}, can to start ride e-bike")
    public void theUserHasSignInWithPasswordCanToStartRideEBike(final String username, final String password) {
        this.application = new ApplicationImpl();
        this.application.setRepository(this.repositoryPort);

        this.application.signUp(username, password);
        assertTrue(this.application.userIsLogged());

        this.eBikeRepository.add();
    }

    @And("There is an e-bike free with id {string}")
    public void thereIsAnEBikeFreeWithId(final String eBikeId) {
        assertTrue(this.application.isFreeEBike(eBikeId));
    }


    @When("The user start ride e-bike {string}")
    public void theUserStartRideEBike(final String eBikeId) {
        this.error = this.application.hireEBike(eBikeId);
    }


    @Given("The user has zero credit")
    public void theUserHasCredit() {
        assertEquals(ZERO_CREDITS, this.application.creditsOfUser().get());
    }

    @Then("The system shows an error message, the user has no credit")
    public void theSystemShowsAnErrorMessageTheUserHasNoCredit() {
        assertTrue(this.error.isPresent());
        assertEquals(Message.Error.ZERO_CREDITS, this.error.get());
        assertFalse(this.application.hasHireEBike());
    }


    @Given("The e-bike {string} is in use from other user {string} password {string}")
    public void theEBikeIsInUse(final String eBikeId, final String username, final String password) {
        final Application application1 = new ApplicationImpl();
        application1.setRepository(this.repositoryPort);
        application1.signUp(username, password);
        application1.addCreditsOf(SOME_CREDITS);
        application1.hireEBike(eBikeId);
        assertTrue(this.application.isInUseEBike(eBikeId));
    }

    @Then("The system shows an error message, the e-bike is already in use by another user")
    public void theSystemShowsAnErrorMessageTheEBikeIsAlreadyInUseByAnotherUser() {
        assertTrue(this.error.isPresent());
        assertEquals(Message.Error.EBIKE_IN_USE, this.error.get());
        assertFalse(this.application.hasHireEBike());
    }


    @Given("the e-bike {string} is with low battery")
    public void theEBikeIsWithEmptyBattery(final String eBikeId) {
        this.eBikeRepository.setLowBattery(eBikeId);
        assertTrue(this.application.isLowBatteryEBike(eBikeId));
    }

    @Then("The system shows an error message, the e-bike has no battery")
    public void theSystemShowsAnErrorMessageTheEBikeHasNoBattery() {
        assertTrue(this.error.isPresent());
        assertEquals(Message.Error.EBIKE_LOW_BATTERY, this.error.get());
        assertFalse(this.application.hasHireEBike());
    }


    @Given("The user has some credit {int} and the e-bike {string} is free and has battery")
    public void theUserHasSomeCreditAndTheEBikeIsFreeAndHasBattery(final int credits, final String eBikeId) {
        this.application.addCreditsOf(credits);
        assertEquals(credits, this.application.creditsOfUser().get());
        assertTrue(this.application.isFreeEBike(eBikeId));
        assertFalse(this.application.isLowBatteryEBike(eBikeId));
    }

    @Then("The system starts the ride, notify the user, subtract some credits, it's minor of {float}")
    public void theSystemStartsTheRideNotifyTheUserSubtractTheCredit(final float minusCredits) {
        assertTrue(this.error.isEmpty());
        assertTrue(this.application.creditsOfUser().get() < minusCredits);
        assertTrue(this.application.hasHireEBike());
    }

    @And("change the state of the e-bike {string}")
    public void changeTheStateOfTheEBike(final String eBikeId) {
        assertFalse(this.application.isFreeEBike(eBikeId));
        assertTrue(this.application.isInUseEBike(eBikeId));
    }

}
