package com.johnwstump.incentivizer.security.web.jwt;

import com.johnwstump.incentivizer.services.user.impl.UserDetailsServiceImpl;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@CommonsLog
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil,
                          UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) {
        try {
            UsernamePasswordAuthenticationToken authentication = getAuthorization(request);

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthorization(HttpServletRequest request) {
        long userId = getAuthorizedUserId(request);

        UserDetails userDetails = userDetailsService.loadUserById(userId);

        return new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                        List.of() : userDetails.getAuthorities()
        );
    }

    public long getAuthorizedUserId(HttpServletRequest request) {
        // Ensure presence of auth header
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(header) || !header.startsWith("Bearer ")) {
            throw new IllegalStateException("Authorization header is invalid.");
        }

        final String token = header.split(" ")[1].trim();
        // Ensure token is valid
        if (!jwtTokenUtil.validate(token)) {
            throw new IllegalStateException("Authorization header is invalid.");

        }

        return jwtTokenUtil.getUserId(token);
    }
}
