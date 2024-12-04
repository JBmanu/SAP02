package application;

import application.concreate.ApplicationImpl;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class Credits {
    private final float someCredits = 150f;
    private Application application;

    @Given("The user {string} has sign in with password {string}")
    public void theUserHasSignIn(final String username, final String password) {
        final UserRepositoryPort repositoryPort = new UserRepositoryPort.UserRepositoryPortImpl();
        this.application = new ApplicationImpl();
        this.application.setUserRepository(repositoryPort);
        this.application.signUp(username, password);
        assertTrue(this.application.userIsLogged());
        assertEquals(0f, this.application.creditsOfUser().get());
    }


    @When("The user add negative credit {float}")
    public void theUserAddNegativeCredit(final float negativeCredits) {
        final Optional<ErrorApplication> error = this.application.addCreditsTo(negativeCredits);
        assertTrue(error.isPresent());
        assertEquals(ErrorApplication.ADD_NEGATIVE_CREDITS, error.get());
    }
    @Then("The system shows an error message, the credit is negative")
    public void theSystemShowsAnErrorMessageTheCreditIsNegative() {
        assertTrue(this.application.creditsOfUser().isPresent());
        assertEquals(0f, this.application.creditsOfUser().get());
    }


    @When("The user add some credit")
    public void theUserAddSomeCredit() {
        final Optional<ErrorApplication> error = this.application.addCreditsTo(this.someCredits);
        assertTrue(error.isEmpty());
    }
    @Then("The system notify add credit")
    public void theSystemNotifyAddCredit() {
        assertTrue(this.application.creditsOfUser().isPresent());
        assertEquals(this.someCredits, this.application.creditsOfUser().get());
    }


    @When("The user add empty credit")
    public void theUserAddEmptyCredit() {
        final float zeroCredits = 0f;
        final Optional<ErrorApplication> error = this.application.addCreditsTo(zeroCredits);
        assertTrue(error.isPresent());
        assertEquals(ErrorApplication.ADD_ZERO_CREDITS, error.get());
    }
    @Then("The system shows an error message, the credit is empty")
    public void theSystemShowsAnErrorMessageTheCreditIsEmpty() {
        assertTrue(this.application.creditsOfUser().isPresent());
        assertEquals(0f, this.application.creditsOfUser().get());
    }

}
