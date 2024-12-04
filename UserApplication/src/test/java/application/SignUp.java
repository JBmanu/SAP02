package application;

import implementation.ApplicationImpl;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SignUp {
    private Application application;

    @Given("The user is not registered")
    public void theUserIsNotRegistered() {
        this.application = new ApplicationImpl(
                Optional.empty(), Optional.empty(), Optional.empty());
    }

    @And("another user is register with username {string} and password {string}")
    public void anotherUserIsRegisterWithUsernameAndPassword(final String username, final String password) {
        final Optional<ErrorApplication> error = this.application.signUp(username, password);
        assertTrue(error.isEmpty());
        assertTrue(this.application.containUser(username));
    }


    @When("The user sign up with username {string} and password {string}")
    public void theUserSignUpWithUsername(final String username, final String password) {
        final Optional<ErrorApplication> error = this.application.signUp(username, password);
        assertEquals(ErrorApplication.USERNAME, error.get());
    }

    @Then("The system shows an error message, the username is already registered")
    public void theSystemShowsAnErrorMessageTheUsernameIsAlreadyRegistered() {
        throw new PendingException();
    }


    @When("The user sign up with correct username")
    public void theUserSignUpWithCorrectUsername() {
        throw new PendingException();
    }

    @Then("The system register user, and user access to the service")
    public void theSystemRegisterUserAndUserAccessToTheService() {
        throw new PendingException();
    }


    @When("The user sign up with empty username or password")
    public void theUserSignUpWithEmptyUsernameOrPassword() {
        throw new PendingException();
    }

    @Then("The system shows an error message, the username or password is empty")
    public void theSystemShowsAnErrorMessageTheUsernameOrPasswordIsEmpty() {
        throw new PendingException();
    }


}
