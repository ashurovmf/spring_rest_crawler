package com.gft.backend.services;


import com.gft.backend.dao.UserDAO;
import com.gft.backend.entities.UserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by miav on 2016-10-06.
 */
@Service
public class AuthenticationService implements UserDetailsService {
    private static final Logger logger = Logger.getLogger(AuthenticationService.class);

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserInfo userInfo = userDAO.getUserInfo(username);
        GrantedAuthority authority = new SimpleGrantedAuthority(userInfo.getRole());
        UserDetails userDetails = (UserDetails)new User(userInfo.getUsername(),
                userInfo.getPassword(), true, true, true, true,
                AuthorityUtils.createAuthorityList("ROLE_USER", "write"));
        return userDetails;
    }
}
