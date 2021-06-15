package com.johnwstump.incentivizer.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnwstump.incentivizer.RESTTests;
import com.johnwstump.incentivizer.model.IUser;
import com.johnwstump.incentivizer.model.impl.User;
import com.johnwstump.incentivizer.rest.UserController;
import com.johnwstump.incentivizer.model.impl.UserRecord;
import com.johnwstump.incentivizer.services.IUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Verifies the endpoint functionality for the User object. These tests are decoupled from the Service
 * implementation (and correctness) via mockito.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class)
@WithMockUser
class UserEndpointTests extends RESTTests {
    private static final String PATH_IDENTIFIER = "/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    private UserRecord mockUser1 = new UserRecord("Norman", "testEmail@test.org");
    private UserRecord mockUser2 = new UserRecord("Archie", "anotherEmail@test.org");

    /**
     * We ensure that the endpoint returns the correct User specified by the ID indicated in the path.
     */
    @Test
    void testSingleUserGetEndpoint() throws Exception {
        // We mock the service behavior to return the mockUser directly
        Mockito.when(userService.findById(mockUser1.getId())).thenReturn(Optional.ofNullable(mockUser1));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                PATH_IDENTIFIER + "/" + mockUser1.getId());

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        checkResponseCode(result, HttpStatus.OK);

        String expectedUser = new ObjectMapper().writeValueAsString(mockUser1);

        // The resulting JSON should contain the expected user information
        JSONAssert.assertEquals(expectedUser, result.getResponse()
                .getContentAsString(), false);
    }

    /**
     * We ensure that the endpoint correctly returns all Users when called
     * without the ID parameter.
     */
    @Test
    void testAllUsersGetEndpoint() throws Exception {
        List<UserRecord> allUsers = new ArrayList<>();
        allUsers.add(mockUser1);
        allUsers.add(mockUser2);

        Mockito.when(userService.getAllUsers()).thenReturn(allUsers);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                PATH_IDENTIFIER + "/");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        checkResponseCode(result, HttpStatus.OK);

        String expectedListOfUsers = new ObjectMapper().writeValueAsString(allUsers);

        JSONAssert.assertEquals(expectedListOfUsers, result.getResponse()
                .getContentAsString(), false);
    }

    /**
     * We want to ensure that the post is successful, and that the response header contains the
     * GET location for the created object.
     */
    @Test
    void testUserPostEndpoint() throws Exception {
        Mockito.when(userService.save(Mockito.any(IUser.class))).thenReturn(mockUser1);

        // Create a DTO pojo from the mock user
        User user = new User(mockUser1.getName(), mockUser1.getEmail());

        String mockUserJson = new ObjectMapper().writeValueAsString(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                PATH_IDENTIFIER + "/").accept(MediaType.APPLICATION_JSON).content(mockUserJson)
                .contentType(MediaType.APPLICATION_JSON).with(csrf());

        this.mockMvc.perform(requestBuilder);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        checkResponseCode(result, HttpStatus.CREATED);

        String returnedGetPath = result.getResponse().getHeader(HttpHeaders.LOCATION);
        String actualGetPath = PATH_IDENTIFIER + "/" + mockUser1.getId();

        Assertions.assertTrue(returnedGetPath != null && returnedGetPath.endsWith(actualGetPath));
    }

    /**
     * We simply want to verify that no errors occur and the result is OK.
     */
    @Test
    void testUserDeleteEndpoint() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
                PATH_IDENTIFIER + "/" + mockUser1.getId()).with(csrf());

        this.mockMvc.perform(requestBuilder);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        checkResponseCode(result, HttpStatus.OK);
    }

    @Test
    void testUserPutEndpoint() throws Exception {
        testUserPutWhenUserDoesNotExist();
        testUserPutWhenUserDoesExist();
    }

    /**
     * We want to make sure that the PUT method returns Not Found when the user being updated does not exist,
     * since I want to disallow creation via PUT.
     */
    private void testUserPutWhenUserDoesNotExist() throws Exception {
        Mockito.when(userService.findById(mockUser1.getId())).thenReturn(java.util.Optional.empty());
        User user = new User(mockUser1.getName(), mockUser1.getEmail());

        String mockUserJson = new ObjectMapper().writeValueAsString(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
                PATH_IDENTIFIER + "/" + mockUser1.getId()).accept(MediaType.APPLICATION_JSON).content(mockUserJson)
                .contentType(MediaType.APPLICATION_JSON).with(csrf());

        this.mockMvc.perform(requestBuilder);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        checkResponseCode(result, HttpStatus.NOT_FOUND);
    }

    /**
     * If the user does exist, we need to verify that the endpoint return OK.
     */
    private void testUserPutWhenUserDoesExist() throws Exception {
        Mockito.when(userService.findById(mockUser1.getId())).thenReturn(java.util.Optional.ofNullable(mockUser1));
        User user = new User(mockUser1.getName(), mockUser1.getEmail());

        String mockUserJson = new ObjectMapper().writeValueAsString(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
                PATH_IDENTIFIER + "/" + mockUser1.getId()).accept(MediaType.APPLICATION_JSON).content(mockUserJson)
                .contentType(MediaType.APPLICATION_JSON).with(csrf());

        this.mockMvc.perform(requestBuilder);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        checkResponseCode(result, HttpStatus.OK);
    }

}