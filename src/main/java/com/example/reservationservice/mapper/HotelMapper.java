package com.example.reservationservice.mapper;

import com.example.reservationservice.domain.Hotel;
import com.example.reservationservice.dto.CreateHotelDTO;
import com.example.reservationservice.dto.HotelDTO;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {
    public Hotel createHotelDTOTOHotel(CreateHotelDTO dto){
        Hotel hotel = new Hotel();
        hotel.setManagerId(dto.getManagerId());
        hotel.setManagerEmail(dto.getManagerEmail());
        hotel.setCity(dto.getCity());
        hotel.setName(dto.getName());
        hotel.setDesc(dto.getDesc());
        hotel.setNoOfRooms(dto.getNoOfRooms());

        return hotel;
    }

    public HotelDTO hotelToHotelDTO(Hotel hotel){
        HotelDTO dto = new HotelDTO();
        dto.setCity(hotel.getCity());
        dto.setNoOfRooms(hotel.getNoOfRooms());
        dto.setDesc(hotel.getDesc());
        dto.setName(hotel.getName());

        return dto;
    }
}
