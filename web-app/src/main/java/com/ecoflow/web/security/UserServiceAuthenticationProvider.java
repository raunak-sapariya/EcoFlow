package com.ecoflow.web.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.ecoflow.web.client.ApiException;
import com.ecoflow.web.client.ServiceUnavailableException;
import com.ecoflow.web.client.UserClient;

/**
 * Spring Security asks this class "is this username/password valid?".
 * We don't have a user table here - we ask user-service over REST.
 */
@Component
public class UserServiceAuthenticationProvider implements AuthenticationProvider {

	private final UserClient userClient;

	public UserServiceAuthenticationProvider(UserClient userClient) {
		this.userClient = userClient;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = String.valueOf(authentication.getCredentials());
		try {
			AppUserPrincipal principal = new AppUserPrincipal(userClient.login(username, password));
			return UsernamePasswordAuthenticationToken.authenticated(principal, null, principal.getAuthorities());
		}
		catch (ApiException ex) {
			if (ex.getStatus() == 403) {
				throw new DisabledException(ex.getMessage());
			}
			throw new BadCredentialsException(ex.getMessage());
		}
		catch (ServiceUnavailableException ex) {
			throw new AuthenticationServiceException("Login is temporarily unavailable (user-service is not reachable)", ex);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
