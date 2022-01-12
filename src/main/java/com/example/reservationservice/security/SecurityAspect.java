package com.example.reservationservice.security;

import com.example.reservationservice.controller.ReservationController;
import com.example.reservationservice.dto.*;
import com.example.reservationservice.service.TokenService;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Aspect
@Configuration
public class SecurityAspect {

    @Value("${oauth.jwt.secret}")
    private String jwtSecret;

    private TokenService tokenService;

    public SecurityAspect(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Around("@annotation(com.example.reservationservice.security.CheckSecurity)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //Dohvata potpis metode
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //Proverava da li token postoji, ako postoji dohvata ga
        String token = null;
        DTO dto = null;
        for(int i = 0; i < methodSignature.getParameterNames().length; i++) {
            if(methodSignature.getParameterNames()[i].equals("authorization")) {
                //Check bearer schema
                if(joinPoint.getArgs()[i].toString().startsWith("Bearer")) {
                    //Get token
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                }
            }
            if(methodSignature.getParameterNames()[i].equals("dto")) {
                dto = (DTO) joinPoint.getArgs()[i];
            }
        }
        //Ako nema authorizaion header-a
        if(token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Claims claims = tokenService.parseToken(token);
        //Ako ne može da parsira token
        if(claims == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(dto == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CheckSecurity checkSecurity = method.getAnnotation(CheckSecurity.class);
        String type = claims.get("type", String.class);
        Long id = claims.get("id", Long.class);

        if(Arrays.asList(checkSecurity.roles()).contains(type)) {
            if(type.equals("M")){
                return joinPoint.proceed();
            }else{
                if(id.equals(dto.getGuestId())){
                    return joinPoint.proceed();
                }
            }
        }

        //Ako nije admin
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
