package com.example.springredditclone.util;

import lombok.experimental.UtilityClass;

/*
Lombok annotation, this annotation will make the following changes at compile time to our class:

        Marks the class as final.
        It generates a private no-arg constructor.
        It only allows the methods or fields to be static.
 */
@UtilityClass
public class Constants {
    //refer tp JwtAuthenticationFilter class - successfulAuthentication method
    //You can set the secret to whatever you want, but the best practice is making the secret key as long as your hash.
    //We use the HS256 algorithm in this example, so our secret key is 256 bits/32 chars.
    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 900_000; // 15 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/services/controller/user";
}
