package application;

import application.concreate.ApplicationImpl;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SignUp {
    public static final String EMPTY_FIELD = "";
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
        assertFalse(error.isEmpty());
        assertEquals(ErrorApplication.SAME_USERNAME, error.get());
    }
    @Then("The system shows an error message, the username {string} is already registered")
    public void theSystemShowsAnErrorMessageTheUsernameIsAlreadyRegistered(final String username) {
        assertFalse(this.application.containUser(username));
    }


    @When("The user sign up with correct username {string} and password {string}")
    public void theUserSignUpWithCorrectUsernameAndPassword(final String username, final String password) {
        final Optional<ErrorApplication> error = this.application.signUp(username, password);
        assertTrue(error.isEmpty());
        assertTrue(this.application.containUser(username));
    }
    @Then("The system register user {string}, and user access to the service")
    public void theSystemRegisterUserAndUserAccessToTheService(final String username) {
        assertTrue(this.application.containUser(username));
    }


    @When("The user sign up with empty username or password")
    public void theUserSignUpWithEmptyUsernameOrPassword() {
        final Optional<ErrorApplication> error = this.application.signUp(EMPTY_FIELD, EMPTY_FIELD);
        assertFalse(error.isEmpty());
        assertEquals(ErrorApplication.EMPTY_FIELD, error.get());
    }
    @Then("The system shows an error message, the username or password is empty")
    public void theSystemShowsAnErrorMessageTheUsernameOrPasswordIsEmpty() {
        assertFalse(this.application.containUser(EMPTY_FIELD));
    }



}
