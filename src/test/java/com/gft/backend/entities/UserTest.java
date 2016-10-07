package com.gft.backend.entities;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by miav on 2016-08-19.
 */
public class UserTest {

    @Test
    public void initUserTest() throws Exception {
        User user = new User();
        user.setEmail("AppId1");
        user.setPassword("Cert-Id-test-1");

        User user2 = new User();

        assertEquals("AppId1", user.getEmail());
        assertEquals("Cert-Id-test-1", user.getPassword());
    }
}
