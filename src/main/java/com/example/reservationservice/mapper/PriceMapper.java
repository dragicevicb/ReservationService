package com.example.reservationservice.mapper;

import com.example.reservationservice.dto.PriceRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class PriceMapper {
    public PriceRequestDTO generateRequest(Long id){
        PriceRequestDTO dto = new PriceRequestDTO();
        dto.setId(id);

        return dto;
    }

}
