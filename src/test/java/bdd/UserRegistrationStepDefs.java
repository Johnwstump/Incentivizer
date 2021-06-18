package bdd;

import com.johnwstump.incentivizer.dao.UserRepository;
import com.johnwstump.incentivizer.model.email.InvalidEmailException;
import com.johnwstump.incentivizer.model.user.impl.UserDTO;
import com.johnwstump.incentivizer.services.IUserService;
import com.johnwstump.incentivizer.services.impl.UserAlreadyExistsException;
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

public class UserRegistrationStepDefs extends CucumberSpringContextConfiguration{
    @Autowired
    private IUserService userService;
    @Autowired
    private UserRepository userRepository;

    private Exception registrationResult;

    @Before(value="user")
    public void beforeUser(){
        userRepository.deleteAll();
    }

    @Given("I have the following users registered")
    public void iHaveTheFollowingUsersRegistered(DataTable table) throws UserAlreadyExistsException, InvalidEmailException {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);

        UserDTO newUser = new UserDTO();
        for (Map<String, String> columns : rows) {
            String name = columns.get("name");
            String password = columns.get("password");
            String email = columns.get("email");

            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPlainPassword(password);
            newUser.setMatchingPlainPassword(password);

            userService.registerNewUser(newUser);
        }
    }

    @When("I register a user with email {string}")
    public void iRegisterAUserWithEmail(String email) {
        try {
            UserDTO newUser = new UserDTO();
            newUser.setName("TomTest");
            String password = "password";
            newUser.setPlainPassword(password);
            newUser.setMatchingPlainPassword(password);
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

    @And("there should (still )be (only ){int} registered user with email {string}")
    public void thereShouldBeOnlyXRegisteredUserWithEmail(int expectedNumUsers, String emailAddress) {
        emailAddress = emailAddress.toUpperCase();
        int actualNumUsers = userRepository.findByEmail(emailAddress).size();
        assertThat(actualNumUsers).isEqualTo(expectedNumUsers);
    }

    @And("there should be a registered user with email {string}")
    public void thereShouldBeOnlyXRegisteredUserWithEmail(String emailAddress) {
        thereShouldBeOnlyXRegisteredUserWithEmail(1, emailAddress);
    }

    @Then("I should receive an error indicating the user already exists")
    public void iShouldReceiveAnErrorIndicatingTheUserAlreadyExists() {
        assertThat(registrationResult).as("Exception should have been thrown").isNotNull();
        assertThat(registrationResult).as("Exception should indicate user exists").isInstanceOf(UserAlreadyExistsException.class);
    }

    @Given("I register a user with email")
    public void iRegisterAUserWithLongEmail(String email) {
        iRegisterAUserWithEmail(email);
    }
}
