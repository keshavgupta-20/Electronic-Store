package com.yash.Electronic.store.security;

import com.yash.Electronic.store.entites.User;
import com.yash.Electronic.store.service.UserServices;
import com.yash.Electronic.store.dtos.UserDto;
import com.yash.Electronic.store.repository.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserServices userServices;

    @Autowired
    private UserRepo userRepo;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        logger.info("OAuthSuccessHandler");
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        Optional<User> existingUser = userRepo.findByEmail(email);
        UserDto userDto;

        if (existingUser.isEmpty()) {
            userDto = new UserDto();
            userDto.setName(name);
            userDto.setImageName("user.jpeg");
            userDto.setEmail(email);
            userDto.setPassword(UUID.randomUUID().toString());
            userDto.setGender("Not specified");
            userDto.setEmailVerified(true);
            userDto.setUserId(UUID.randomUUID().toString());

            userDto = userServices.create_User(userDto);  // ensure userDto is returned
        } else {
            userDto = new ModelMapper().map(existingUser.get(), UserDto.class);
        }

        // Store in session
        request.getSession().setAttribute("userObj", userDto);

        System.out.println("Hello");
        User loggedInUser = userRepo.findByEmail(email).orElse(null);
        if (loggedInUser == null) {
            // handle appropriately
            response.sendRedirect("/electrohub/login?error=notfound");
            return;
        }
        // fetch full user with roles
        boolean isAdmin = loggedInUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));


        if (isAdmin) {
            System.out.println("Admin");
            redirectStrategy.sendRedirect(request, response, "electrohub/admin/dashboard");
        } else {
            System.out.println("Not Admin");
            redirectStrategy.sendRedirect(request, response, "/electrohub/");
        }
    }
}
