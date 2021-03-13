package com.example.springredditclone.security;

import com.example.springredditclone.model.Subreddit;
import com.example.springredditclone.model.User;
import com.example.springredditclone.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.example.springredditclone.util.Constants.*;

//Filters can be used to implement authentication feature for the application besides the primary functions login and logout.
//A typical scenario is when a user tries to access a page that requires authentication
//and if he is not logged in, the application should display the login page.
//And after the user has been authenticated successfully, he is allowed to access any pages that require authentication.
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    //@Qualifier("userDetailsServiceImpl") //indicates which bean to use when more than one bean is available for autowiring
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //calling the class's getJwtFromRequest method which retrieves the Bearer Token (ie. JWT)
        //from the HttpServletRequest object we are passing as input.
        String jwt = getJwtFromRequest(request);

        //Once we retrieve the token, we pass it to the validateToken() method of the JwtProvider class.
        if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {

            //Once the JWT is validated, we retrieve the username from the token by calling the getUsernameFromJWT() method.
            String username = jwtProvider.getUsernameFromJWT(jwt);

            //Once we get the username, we retrieve the user using the UserDetailsService class and store the user inside the SecurityContext
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }
}

/**
 //This filter will check the existence and validity of the access token on the Authorization header.
 //We will specify which endpoints will be subject to this filter in our configuration class.
 @Component
 public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

 public JwtAuthenticationFilter(AuthenticationManager authManager) {
 super(authManager);
 }

 //The doFilterInternal method intercepts the requests then checks the Authorization header.
 //If the header is not present or does not start with “BEARER”, it proceeds to the filter chain.
 @Override
 protected void doFilterInternal(HttpServletRequest req,
 HttpServletResponse res,
 FilterChain chain) throws IOException, ServletException {

 String header = req.getHeader(HEADER_STRING);   //Authorization

 if (header == null || !header.startsWith(TOKEN_PREFIX)) {   //TOKEN_PREFIX = 'Bearer '
 chain.doFilter(req, res);
 return;
 }

 UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

 SecurityContextHolder.getContext().setAuthentication(authentication);
 chain.doFilter(req, res);
 }

 // Reads the JWT from the Authorization header, and then uses JWT to validate the token
 private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
 String token = request.getHeader(HEADER_STRING);

 if (token != null) {
 // parse the token.
 String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
 .build()
 .verify(token.replace(TOKEN_PREFIX, ""))
 .getSubject();

 if (user != null) {
 // new arraylist means authorities
 return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
 }

 return null;
 }

 return null;
 }

 }
 **/
