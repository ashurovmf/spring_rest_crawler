package com.gft.backend.controllers;

import com.gft.backend.dao.UserDAO;
import com.gft.backend.entities.FacebookAuthWrapper;
import com.gft.backend.entities.UserInfo;
import com.gft.backend.services.FacebookHttpService;
import com.gft.backend.utils.DummyAuthenticationBuilder;
import com.gft.backend.utils.FacebookContext;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by miav on 2016-10-11.
 */
@Controller
public class FacebookController {
    private static final Logger logger = Logger.getLogger(FacebookController.class);

    @Autowired
    JdbcTokenStore tokenStore;

    @Autowired
    UserDAO userDAO;

    @Autowired
    FacebookHttpService facebookService;

    @Autowired
    FacebookContext facebookContext;

    @Autowired
    DefaultTokenServices tokenServices;

    JsonFactory parserFactory = new JsonFactory();

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String registrationAccessCode(@RequestBody FacebookAuthWrapper authPrincipal)
            throws Exception {
        logger.debug("****** get request for "+authPrincipal.getUserID());
        String tokenRequest = facebookService.sendHttpRequest("GET", FacebookContext.Facebook_ME_URL,
                new String[]{"access_token","fields"},
                new String[]{authPrincipal.getAccessToken(),"name,email"});
        logger.debug("****** get response from FB "+tokenRequest);
        Map<String, Object> responseFromFB = parseResponseFromFB(authPrincipal, tokenRequest);
        if(authPrincipal.getUserID().equals(responseFromFB.get("id"))){
            return getAccessTokenForFBUser(authPrincipal, responseFromFB);
        }

        return "{ \"error\":\"User data is not correct\" }";
    }

    private String getAccessTokenForFBUser(@RequestBody FacebookAuthWrapper authPrincipal,
                                           Map<String, Object> responseFromFB) {
        UserInfo userInfo = userDAO.getUserInfo(authPrincipal.getUserID());
        if(userInfo == null){
            userDAO.setUserInfo(authPrincipal.getUserID(),"EMPTY",
                    (String) responseFromFB.get("email"), "Facebook");
        }
        OAuth2Authentication auth = (OAuth2Authentication) DummyAuthenticationBuilder
                .getOAuth(authPrincipal.getUserID());
        logger.debug("AUTH:"+auth);
        OAuth2AccessToken accessToken = tokenServices.createAccessToken(auth);
        return "{ \"id\":\""+authPrincipal.getUserID()+
                "\",\"token_type\": \""+accessToken.getTokenType()+
                "\", \"access_token\": \""+accessToken.getValue()+"\" }";
    }

    private Map<String,Object> parseResponseFromFB(@RequestBody FacebookAuthWrapper authPrincipal,
                                                   String tokenRequest) throws IOException {
        HashMap<String,Object> result = new HashMap<>();
        JsonParser parser = parserFactory.createJsonParser(tokenRequest);
        boolean isAuthenticated = false;
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String fieldname = parser.getCurrentName();
            if ("id".equals(fieldname)) {
                parser.nextToken();
                String fbUserId = parser.getText();
                result.put("id",fbUserId);
                logger.debug("FB user id:"+fbUserId);

            }
            if ("email".equals(fieldname)) {
                parser.nextToken();
                String userEmail = parser.getText();
                result.put("email",userEmail);
                logger.debug("FB user email:"+userEmail);
            }
            if ("name".equals(fieldname)) {
                parser.nextToken();
                result.put("name",parser.getText());
            }
        }
        return result;
    }
}
