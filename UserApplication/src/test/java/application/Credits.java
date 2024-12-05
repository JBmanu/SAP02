package application;

import application.concreate.ApplicationImpl;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class Credits {
    private static final float SOME_CREDITS = 150f;
    private Application application;

    @Given("The user {string} has sign in with password {string}")
    public void theUserHasSignIn(final String username, final String password) {
        final RepositoryPort repositoryPort = new RepositoryPort.RepositoryPortImpl();
        this.application = new ApplicationImpl();
        this.application.setRepository(repositoryPort);
        this.application.signUp(username, password);
        assertTrue(this.application.userIsLogged());
        assertEquals(0f, this.application.creditsOfUser().get());
    }


    @When("The user add negative credit {float}")
    public void theUserAddNegativeCredit(final float negativeCredits) {
        final Optional<Message> error = this.application.addCreditsOf(negativeCredits);
        assertTrue(error.isPresent());
        assertEquals(Message.Error.ADD_NEGATIVE_CREDITS, error.get());
    }
    @Then("The system shows an error message, the credit is negative")
    public void theSystemShowsAnErrorMessageTheCreditIsNegative() {
        assertTrue(this.application.creditsOfUser().isPresent());
        assertEquals(0f, this.application.creditsOfUser().get());
    }


    @When("The user add some credit")
    public void theUserAddSomeCredit() {
        final Optional<Message> error = this.application.addCreditsOf(SOME_CREDITS);
        assertTrue(error.isEmpty());
    }
    @Then("The system notify add credit")
    public void theSystemNotifyAddCredit() {
        assertTrue(this.application.creditsOfUser().isPresent());
        assertEquals(SOME_CREDITS, this.application.creditsOfUser().get());
    }


    @When("The user add empty credit")
    public void theUserAddEmptyCredit() {
        final float zeroCredits = 0f;
        final Optional<Message> error = this.application.addCreditsOf(zeroCredits);
        assertTrue(error.isPresent());
        assertEquals(Message.Error.ADD_ZERO_CREDITS, error.get());
    }
    @Then("The system shows an error message, the credit is empty")
    public void theSystemShowsAnErrorMessageTheCreditIsEmpty() {
        assertTrue(this.application.creditsOfUser().isPresent());
        assertEquals(0f, this.application.creditsOfUser().get());
    }

}
