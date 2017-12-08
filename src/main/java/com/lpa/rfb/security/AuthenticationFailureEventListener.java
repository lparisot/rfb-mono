package com.lpa.rfb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent authenticationFailureBadCredentialsEvent) {
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authenticationFailureBadCredentialsEvent.getAuthentication().getDetails();

        loginAttemptService.loginFailed(webAuthenticationDetails.getRemoteAddress());
    }
}
