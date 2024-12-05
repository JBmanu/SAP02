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
        final RepositoryPort repositoryPort = new RepositoryPort.RepositoryPortImpl();
        this.application = new ApplicationImpl();
        this.application.setRepository(repositoryPort);
    }


    @And("another user is register with username {string} and password {string}")
    public void anotherUserIsRegisterWithUsernameAndPassword(final String username, final String password) {
        final Optional<ErrorApplication> error = this.application.signUp(username, password);
        assertTrue(error.isEmpty());
    }
    @When("The user sign up with username {string} and password {string}")
    public void theUserSignUpWithUsername(final String username, final String password) {
        final Optional<ErrorApplication> error = this.application.signUp(username, password);
        assertFalse(error.isEmpty());
        assertEquals(ErrorApplication.SAME_USERNAME, error.get());
    }
    @Then("The system shows an error message, the username {string} is already registered")
    public void theSystemShowsAnErrorMessageTheUsernameIsAlreadyRegistered(final String username) {
        assertTrue(this.application.isRegistered(username));
    }


    @When("The user sign up with correct username {string} and password {string}")
    public void theUserSignUpWithCorrectUsernameAndPassword(final String username, final String password) {
        final Optional<ErrorApplication> error = this.application.signUp(username, password);
        assertTrue(error.isEmpty());
    }
    @Then("The system register user {string}, and user access to the service")
    public void theSystemRegisterUserAndUserAccessToTheService(final String username) {
        assertTrue(this.application.isRegistered(username));
        assertTrue(this.application.userIsLogged());
    }


    @When("The user sign up with empty username or password")
    public void theUserSignUpWithEmptyUsernameOrPassword() {
        final Optional<ErrorApplication> error = this.application.signUp(EMPTY_FIELD, EMPTY_FIELD);
        assertFalse(error.isEmpty());
        assertEquals(ErrorApplication.EMPTY_FIELD, error.get());
    }
    @Then("The system shows an error message, the username or password is empty")
    public void theSystemShowsAnErrorMessageTheUsernameOrPasswordIsEmpty() {
        assertFalse(this.application.isRegistered(EMPTY_FIELD));
    }



}
