package application;

import application.concreate.ApplicationImpl;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SignIn {
    public static final String EMPTY_FIELD = "";
    private final RepositoryPort repositoryPort = new RepositoryPort.RepositoryPortImpl();
    private Application application = new ApplicationImpl();

    @Before
    public void setup() {
        this.application = new ApplicationImpl();
        this.application.setRepository(this.repositoryPort);
    }

    @Given("The user {string} is not registered")
    public void theUserIsNotRegistered(final String username) {
        assertFalse(this.application.isRegistered(username));
    }
    @When("The user {string} sign in with wrong username and {string} as password")
    public void theUserSignInWithWrongUsername(final String username, final String password) {
        final Optional<ErrorApplication> error = this.application.signIn(username, password);
        assertTrue(error.isPresent());
        assertEquals(ErrorApplication.NOT_REGISTERED, error.get());
    }
    @Then("The system shows an error message, the user {string} is not registered")
    public void theSystemShowsAnErrorMessageTheUserIsNotRegistered(final String username) {
        assertFalse(this.application.isRegistered(username));
    }


    @Given("The user {string} is registered with {string}")
    public void theUserIsRegistered(final String username, final String password) {
        final Optional<ErrorApplication> error = this.application.signUp(username, password);
        assertTrue(error.isEmpty());
        assertTrue(this.application.isRegistered(username));
        this.application.logout();
    }

    @When("The user {string} sign in with wrong password {string}")
    public void theUserSignInWithWrongPassword(final String username, final String password) {
        final Optional<ErrorApplication> error = this.application.signIn(username, password);
        assertTrue(error.isPresent());
        assertEquals(ErrorApplication.WRONG_PASSWORD, error.get());
    }
    @Then("The system shows an error message, the password is wrong")
    public void theSystemShowsAnErrorMessageThePasswordIsWrong() {
        assertFalse(this.application.userIsLogged());
    }


    @When("The user sign in with empty username or password")
    public void theUserSignInWithEmptyUsernameOrPassword() {
        final Optional<ErrorApplication> error = this.application.signIn(EMPTY_FIELD, EMPTY_FIELD);
        assertTrue(error.isPresent());
        assertEquals(ErrorApplication.EMPTY_FIELD, error.get());
    }
    @Then("The system give an error message, the username or password is empty")
    public void theSystemGiveAnErrorMessageTheUsernameOrPasswordIsEmpty() {
        assertFalse(this.application.userIsLogged());
    }


    @When("The user sign in with correct username and password, {string} and {string}")
    public void theUserSignInWithCorrectUsernameAndPasswordAnd(final String username, final String password) {
        final Optional<ErrorApplication> error = this.application.signIn(username, password);
        assertTrue(error.isEmpty());
    }
    @Then("User access to the service")
    public void userAccessToTheService() {
        assertTrue(this.application.userIsLogged());
    }

}
