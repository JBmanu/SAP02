package view;

import application.Application;
import application.EBikeControllerPort;
import application.RepositoryPort;
import application.concreate.ApplicationImpl;
import application.utils.ThreadUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class Credits {
    private static final float SOME_CREDITS = 150f;
    private Application application;
    private final String username = "username";
    private final String password = "password";
    private final String eBikeId = "0";

    @Given("The user has sign in")
    public void theUserHasSignIn() {
        final RepositoryPort repositoryPort = new RepositoryPort.RepositoryPortImpl();
        this.application = new ApplicationImpl();
        this.application.setRepository(repositoryPort);
        this.application.setEBikeController(new EBikeControllerPort.EBikeControllerPortImpl());
        this.application.signUp(this.username, this.password);
        this.application.addCreditsOf(SOME_CREDITS);
    }

    @When("The user open the application")
    public void theUserOpenTheApplication() {
        assertTrue(this.application.userIsLogged());
    }

    @Then("The system shows the credit")
    public void theSystemShowsTheCredit() {
        assertEquals(SOME_CREDITS, this.application.creditsOfUser().get());
    }

    @When("The user pay the unlock hire service")
    public void theUserPayTheUnlockHireService() {
        this.application.hireEBike(this.eBikeId);
        assertEquals(SOME_CREDITS - ApplicationImpl.CREDITS_FOR_HIRE, this.application.creditsOfUser().get());
    }

    @And("pay the ride service")
    public void payTheRideService() {
        ThreadUtils.sleep(1000);
        assertEquals(SOME_CREDITS - ApplicationImpl.CREDITS_FOR_HIRE - ApplicationImpl.CREDITS_FOR_RIDE, this.application.creditsOfUser().get());
    }

    @Then("The system shows the residual credit")
    public void theSystemShowsTheResidualCredit() {
        assertTrue(this.application.creditsOfUser().isPresent());
    }
}
