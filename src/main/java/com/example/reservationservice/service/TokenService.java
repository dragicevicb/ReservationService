package com.example.reservationservice.service;

import io.jsonwebtoken.Claims;

public interface TokenService {
    //Generiše token koji se vraća klijentu
    public String generate(Claims claims);
    //Parsira token koji dobija od klijenta
    public Claims parseToken(String jwt);
}
