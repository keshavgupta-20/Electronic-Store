package com.yash.Electronic.store.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class LoginHelper {
    public static  String getEmailOfLoggedInUser(Authentication authentication){

        if (authentication instanceof OAuth2AuthenticationToken){
            var aOAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
            var clientId =aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            var oauth2User = (OAuth2User) authentication.getPrincipal();
            String userName = "";
            if (clientId.equalsIgnoreCase("google")){
                userName = oauth2User.getAttribute("email").toString();
                System.out.println("Getting email from google");
            }
            else if (clientId.equalsIgnoreCase("github")) {
                System.out.println("Getting email from github");
            }
            return userName;
        }
        else{
            System.out.println("Getting data from local host");
            return authentication.getName();
        }

    }
}
