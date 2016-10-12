package com.gft.backend.utils;


import com.gft.backend.entities.FacebookAuthWrapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by miav on 2016-10-12.
 */
public class DummyAuthenticationBuilder {
    public static Authentication getOAuth(FacebookAuthWrapper authPrincipal){
        HashMap<String, String> authorizationParameters = new HashMap<String, String>();
        authorizationParameters.put("scope", "write");
        authorizationParameters.put("username", "mobile_client");
        authorizationParameters.put("client_id", "mobile-client");
        authorizationParameters.put("grant", "password");

        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("ROLE_USER", "write");
        Set<String> scopes = new HashSet<>();
        scopes.add("write");
        Set<String> resourceIds = new HashSet<>();
        scopes.add("mobile-public");
        OAuth2Request authorizationRequest =  new OAuth2Request(authorizationParameters,"mobile-client",
                authorityList, true, scopes, resourceIds, "https://localhost:8443/", null, null);

        // Create principal and auth token
        User userPrincipal = new User(authPrincipal.getUserID(), "", true, true, true, true, authorityList);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, authorityList) ;

        OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest, authenticationToken);
        authenticationRequest.setAuthenticated(true);

        return authenticationRequest;
    }
}
