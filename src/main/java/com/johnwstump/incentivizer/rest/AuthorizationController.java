package com.johnwstump.incentivizer.rest;

import com.johnwstump.incentivizer.model.email.InvalidEmailException;
import com.johnwstump.incentivizer.model.password.InvalidPasswordException;
import com.johnwstump.incentivizer.model.user.impl.User;
import com.johnwstump.incentivizer.model.user.impl.UserMapper;
import com.johnwstump.incentivizer.model.user.impl.dto.AuthRequest;
import com.johnwstump.incentivizer.model.user.impl.dto.RegistrationRequest;
import com.johnwstump.incentivizer.model.user.impl.dto.UserDTO;
import com.johnwstump.incentivizer.security.web.jwt.JwtTokenUtil;
import com.johnwstump.incentivizer.services.user.IUserService;
import com.johnwstump.incentivizer.services.user.impl.UserAlreadyExistsException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.Optional;

/**
 * Controller for exposing endpoints related to authorization, including user registration
 * and log in.
 */
@RestController
@CommonsLog
@RequestMapping("/api")
public class AuthorizationController extends AbstractRestController {
    private IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private UserMapper userMapper;

    @Autowired
    public AuthorizationController(IUserService userService, AuthenticationManager authenticationManager,
                                   JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userMapper = new UserMapper();
    }

    /**
     * Attempts to register a new user with the information from the provided request.
     *
     * @param registrationRequest A populated instance of RegistrationRequest.
     * @return A response indicating the location of the created resource. A 422-UNPROCESSABLE_ENTITY
     * error will be returned if the information in the Registration Request does not represent a new
     * user with a valid email and password.
     */
    @PostMapping("/register")
    @CrossOrigin(origins = "*")
    public ResponseEntity<User> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        try {
            User registeredUser = userService.registerNewUser(registrationRequest);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                    "/{userId}").buildAndExpand(registeredUser.getId()).toUri();

            return ResponseEntity.created(location).build();
        } catch (InvalidPasswordException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getCombinedMessage());
        } catch (UserAlreadyExistsException | InvalidEmailException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    /**
     * Attempts to log in the user represented by the credentials in the provided AuthRequest.
     *
     * @param request An instance of AuthRequest containing the credentials which will be used to log in.
     * @return A response with an authorization header with a JWT Token and a body containing the User
     * display object corresponding to the logged in user. If the credentials are invalid, an error response
     * is returned with error 401-UNAUTHORIZED
     */
    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public ResponseEntity<UserDTO> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(), request.getPassword()
                            )
                    );
            org.springframework.security.core.userdetails.User principal
                    = (org.springframework.security.core.userdetails.User) authenticate.getPrincipal();

            Optional<User> possibleUser = userService.findByEmail(principal.getUsername());

            if (possibleUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            User user = possibleUser.get();

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtTokenUtil.generateAccessToken(user)
                    )
                    .body(userMapper.toUserDTO(user));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Endpoint used to authenticate a user. While not explicit here, the
     * Spring Security configuration will result in an Unauthorized error if
     * authentication header associated with this request does not have a valid JWT
     * Token. As a result, a successful hit of this endpoint indicates the user is authenticated.
     *
     * @param principal A Principal representing the user associated with the auth request.
     * @return The principal provided.
     */
    @RequestMapping("/authenticate")
    public Principal authenticate(Principal principal) {
        return principal;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        log.debug("Logging debug test");
        log.info("Logging info test.");
        log.warn("Logging warn test.");
        log.error("Logging error test.");

        return new ResponseEntity<>("I live.", HttpStatus.OK);
    }

    @GetMapping("/testError")
    public ResponseEntity<String> error() throws Exception {
        throw new Exception("This is a bad thing.");
    }
}
