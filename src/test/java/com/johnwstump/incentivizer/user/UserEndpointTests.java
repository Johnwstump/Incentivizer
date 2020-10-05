package com.johnwstump.incentivizer.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnwstump.incentivizer.RESTTests;
import com.johnwstump.incentivizer.controllers.UserController;
import com.johnwstump.incentivizer.model.User;
import com.johnwstump.incentivizer.services.IUser;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Verifies the endpoint functionality for the User object. These tests are decoupled from the Service
 * implementation (and correctness) via mockito.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class)
@WithMockUser
public class UserEndpointTests extends RESTTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUser userService;

    User mockUser = new User("Norman", "testEmail@test.org");

    /**
     * We ensure that the endpoint returns the correct User specified by the ID indicated in the path.
     * @throws Exception
     */
    @Test
    public void testSingleUserGetEndpoint() throws Exception {

        Mockito.when(userService.findById(mockUser.getId())).thenReturn(java.util.Optional.ofNullable(mockUser));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/users/" + mockUser.getId());

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        checkResponseCode(result, HttpStatus.OK);

        ObjectMapper mapper = new ObjectMapper();

        String expected = mapper.writeValueAsString(mockUser);

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }

    /**
     * We ensure that the endpoint correctly returns the all Users when called
     * without the ID parameter.
     * @throws Exception
     */
    @Test
    public void testAllUsersGetEndpoint() throws Exception {

        List<User> allUsers = new ArrayList<User>();
        allUsers.add(mockUser);

        Mockito.when(userService.getAllUsers()).thenReturn(allUsers);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/users/");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        checkResponseCode(result, HttpStatus.OK);

        ObjectMapper mapper = new ObjectMapper();

        String expected = mapper.writeValueAsString(allUsers);

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }

    /**
     * We want to ensure that the post is successful, and that the response header contains the
     * GET location for the created object.
     */
    @Test
    public void testUserPostEndpoint() throws Exception {

        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(mockUser);

        ObjectMapper mapper = new ObjectMapper();

        String mockUserJson = mapper.writeValueAsString(mockUser);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/users/").accept(MediaType.APPLICATION_JSON).content(mockUserJson)
                .contentType(MediaType.APPLICATION_JSON).with(csrf());

        this.mockMvc.perform(requestBuilder);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        checkResponseCode(result, HttpStatus.CREATED);

        Assertions.assertEquals("http://localhost/users/" + mockUser.getId(), result.getResponse().getHeader(HttpHeaders.LOCATION));
    }


    /**
     * We simply want to verify that no errors occur and the result is OK.
     */
    @Test
    public void testUserDeleteEndpoint() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String mockUserJson = mapper.writeValueAsString(mockUser);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
                "/users/" + mockUser.getId()).with(csrf());

        this.mockMvc.perform(requestBuilder);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        checkResponseCode(result, HttpStatus.OK);
    }

    @Test
    public void testUserPutEndpoint() throws Exception {
        testUserPutWhenUserDoesNotExist();
        testUserPutWhenUserDoesExist();
    }

    /**
     * We want to make sure that the PUT method returns Not Found when the user being updated does not exist,
     * since I want to disallow creation via PUT.
     * @throws Exception
     */
    private void testUserPutWhenUserDoesNotExist() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Mockito.when(userService.findById(mockUser.getId())).thenReturn(java.util.Optional.ofNullable(null));

        String mockUserJson = mapper.writeValueAsString(mockUser);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
                "/users/" + mockUser.getId()).accept(MediaType.APPLICATION_JSON).content(mockUserJson)
                .contentType(MediaType.APPLICATION_JSON).with(csrf());

        this.mockMvc.perform(requestBuilder);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        checkResponseCode(result, HttpStatus.NOT_FOUND);
    }

    /**
     * If the user does exist, we need to verify that the endpoint return OK.
     * @throws Exception
     */
    private void testUserPutWhenUserDoesExist() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Mockito.when(userService.findById(mockUser.getId())).thenReturn(java.util.Optional.ofNullable(mockUser));

        String mockUserJson = mapper.writeValueAsString(mockUser);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
                "/users/" + mockUser.getId()).accept(MediaType.APPLICATION_JSON).content(mockUserJson)
                .contentType(MediaType.APPLICATION_JSON).with(csrf());

        this.mockMvc.perform(requestBuilder);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        checkResponseCode(result, HttpStatus.OK);
    }

}