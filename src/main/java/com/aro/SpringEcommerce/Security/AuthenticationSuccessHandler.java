package com.aro.SpringEcommerce.Security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.expires.in}")
    private int EXPIRES_IN;

    @Autowired
    private JwtUtil tokenHelper;

    @Autowired
    private JsonMapper jsonMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ){

        clearAuthenticationAttributes(request);
        UserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String jwt = tokenHelper.generateToken( user.getUsername() );

        UserTokenState userTokenState = new UserTokenState(jwt, EXPIRES_IN);
        try {
            String jwtResponse = jsonMapper.writeValueAsString(userTokenState);
            response.setContentType("application/json");
            response.getWriter().write(jwtResponse);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Getter @Setter
    private static class UserTokenState {
        private String jws;
        private int expires;

        public UserTokenState(String jws, int expires){
            this.jws = jws;
            this.expires = expires;
        }
    }
}
