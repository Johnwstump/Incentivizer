package com.johnwstump.incentivizer.rest;

import com.johnwstump.incentivizer.security.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public abstract class AbstractRestController {

    CurrentUserUtil currentUserUtil;

    protected AbstractRestController() {
        currentUserUtil = null;
    }

    Optional<HttpServletRequest> getCurrentHttpRequest() {
        return
                Optional.ofNullable(
                        RequestContextHolder.getRequestAttributes()
                )
                        .filter(ServletRequestAttributes.class::isInstance)
                        .map(ServletRequestAttributes.class::cast)
                        .map(ServletRequestAttributes::getRequest);
    }

    private long getAuthorizedId() {
        if (getCurrentHttpRequest().isEmpty()) {
            throw new IllegalStateException("Current HTTP Request cannot be determined.");
        }

        return currentUserUtil.getCurrentUserId(getCurrentHttpRequest().get());
    }

    @Autowired
    public void setCurrentUserUtil(CurrentUserUtil currentUserUtil) {
        this.currentUserUtil = currentUserUtil;
    }
}
