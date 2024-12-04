package application;

import application.concreate.ApplicationImpl;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

public class Credits {
    private final Optional<UserRepositoryPort> repositoryPort = Optional.of(new UserRepositoryPort.UserRepositoryPortImpl());
    private final Application application = new ApplicationImpl(
            Optional.empty(), this.repositoryPort, Optional.empty());


    @Given("The user {string} has sign in with password {string}")
    public void theUserHasSignIn(final String username, final String password) {
        this.application.signUp(username, password);
    }


    @When("The user add negative credit {double}")
    public void theUserAddNegativeCredit(final double negativeCredits) {
    }
    @Then("The system shows an error message, the credit is negative")
    public void theSystemShowsAnErrorMessageTheCreditIsNegative() {
    }


    @When("The user add some credit {double}")
    public void theUserAddSomeCredit(final double someCredits) {
    }
    @Then("The system notify add credit")
    public void theSystemNotifyAddCredit() {
    }


    @When("The user add empty credit {double}")
    public void theUserAddEmptyCredit(final double zeroCredits) {

    }
    @Then("The system shows an error message, the credit is empty")
    public void theSystemShowsAnErrorMessageTheCreditIsEmpty() {
    }

}
