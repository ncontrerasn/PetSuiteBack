package com.petsuite.security;

import com.petsuite.Services.dto.InfoUser_Dto;
import com.petsuite.Services.model.JwtUserDetails;
import com.petsuite.Services.model.JwtAuthenticationToken;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.List;
import org.springframework.security.core.Authentication;

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private JwtValidator validator;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) usernamePasswordAuthenticationToken;
        String token = jwtAuthenticationToken.getToken();
        
        
         InfoUser_Dto jwtUser=validator.validate(token);
             
        if (jwtUser == null) {
            throw new RuntimeException("JWT Token is incorrect");
            
        }
        

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(jwtUser.getRole());
        return new JwtUserDetails(jwtUser.getUser(), jwtUser.getPassword(),
                token,
                grantedAuthorities);
         
         
         
        
       


    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
    }
}

