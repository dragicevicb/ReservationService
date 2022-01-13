package com.example.reservationservice.service;

import com.example.reservationservice.dto.PriceRequestDTO;
import org.springframework.retry.annotation.Retryable;

import java.io.IOException;

public interface HttpService {
    @Retryable(value = RuntimeException.class)
    int getDiscount(PriceRequestDTO dto) throws IOException;
}
