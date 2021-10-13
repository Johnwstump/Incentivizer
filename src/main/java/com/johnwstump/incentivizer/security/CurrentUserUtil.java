package com.johnwstump.incentivizer.security;

import com.johnwstump.incentivizer.security.web.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class CurrentUserUtil {

    private final JwtTokenFilter jwtTokenFilter;

    @Autowired
    public CurrentUserUtil(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    public long getCurrentUserId(HttpServletRequest request) {
        return jwtTokenFilter.getAuthorizedUserId(request);
    }
}
