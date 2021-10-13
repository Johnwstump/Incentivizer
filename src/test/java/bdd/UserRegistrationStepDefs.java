package bdd;

import com.johnwstump.incentivizer.dao.UserRepository;
import com.johnwstump.incentivizer.model.email.InvalidEmailException;
import com.johnwstump.incentivizer.model.password.InvalidPasswordException;
import com.johnwstump.incentivizer.model.user.impl.dto.RegistrationRequest;
import com.johnwstump.incentivizer.services.user.IUserService;
import com.johnwstump.incentivizer.services.user.impl.UserAlreadyExistsException;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRegistrationStepDefs {
    @Autowired
    private IUserService userService;
    @Autowired
    private UserRepository userRepository;

    private Exception registrationResult;

    @Before(value = "@User")
    public void beforeUser(){
        userRepository.deleteAll();
    }

    @Given("I have the following users registered")
    public void iHaveTheFollowingUsersRegistered(DataTable table) throws UserAlreadyExistsException, InvalidEmailException, InvalidPasswordException {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);

        RegistrationRequest newUser = new RegistrationRequest();
        for (Map<String, String> columns : rows) {
            String name = columns.get("name");
            String password = columns.get("password");
            String email = columns.get("email");

            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setMatchingPassword(password);

            userService.registerNewUser(newUser);
        }
    }

    @When("I register a user with email {string}")
    public void iRegisterAUserWithEmail(String email) {
        try {
            RegistrationRequest newUser = new RegistrationRequest();
            newUser.setName("TomTest");
            String password = "Password!124";
            newUser.setPassword(password);
            newUser.setMatchingPassword(password);
            newUser.setEmail(email);
            userService.registerNewUser(newUser);
        }
        catch (Exception ex){
            this.registrationResult = ex;
        }
    }

    @Then("I should receive an error indicating the email is invalid")
    public void iShouldReceiveAnErrorIndicatingTheEmailIsInvalid() {
        assertThat(registrationResult).as("Exception should have been thrown").isNotNull();
        assertThat(registrationResult).as("Exception should indicate email is invalid").isInstanceOf(InvalidEmailException.class);
    }

    @And("there should (still )be (only )1 registered user with email {string}")
    public void thereShouldBeOnly1RegisteredUserWithEmail(String emailAddress) {
        emailAddress = emailAddress.toUpperCase();
        int actualNumUsers = userRepository.findByEmail(emailAddress).isPresent() ? 1 : 0;
        assertThat(actualNumUsers).isEqualTo(1);
    }

    @And("there should be a registered user with email {string}")
    public void thereShouldBeARegisteredUserWithEmail(String emailAddress) {
        thereShouldBeOnly1RegisteredUserWithEmail(emailAddress);
    }

    @Then("I should receive an error indicating the user already exists")
    public void iShouldReceiveAnErrorIndicatingTheUserAlreadyExists() {
        assertThat(registrationResult).as("Exception should have been thrown").isNotNull();
        assertThat(registrationResult).as("Exception should indicate user exists").isInstanceOf(UserAlreadyExistsException.class);
    }

    @Then("I should receive an error indicating the password is invalid")
    public void iShouldReceiveAnErrorIndicatingThePasswordIsInvalid() {
        assertThat(registrationResult).as("Exception should have been thrown").isNotNull();
        assertThat(registrationResult).as("Exception should indicate password is invalid").isInstanceOf(InvalidPasswordException.class);
    }

    @Given("I register a user with email")
    public void iRegisterAUserWithLongEmail(String email) {
        iRegisterAUserWithEmail(email);
    }

    @Given("I register a user with password {string}")
    public void iRegisterAUserWithPassword(String password) {
        try {
            RegistrationRequest newUser = new RegistrationRequest();
            newUser.setName("TodTest");
            newUser.setPassword(password);
            newUser.setMatchingPassword(password);
            newUser.setEmail("TestPassEmail@test.com");
            userService.registerNewUser(newUser);
        } catch (Exception ex) {
            this.registrationResult = ex;
        }
    }
}
