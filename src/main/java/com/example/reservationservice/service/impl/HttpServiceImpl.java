package com.example.reservationservice.service.impl;

import com.example.reservationservice.dto.PriceDTO;
import com.example.reservationservice.dto.PriceRequestDTO;
import com.example.reservationservice.service.HttpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class HttpServiceImpl implements HttpService {

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int getDiscount(PriceRequestDTO dto) throws IOException {

        URL url = new URL("http://localhost:8081/user/price");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("GET");
        OutputStream outStream = con.getOutputStream();
        OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, StandardCharsets.UTF_8);
        outStreamWriter.write(objectMapper.writeValueAsString(dto));
        outStreamWriter.flush();
        outStreamWriter.close();
        outStream.close();

        con.connect();

        String result;
        if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result2 = bis.read();
            while (result2 != -1) {
                buf.write((byte) result2);
                result2 = bis.read();
            }
            result = buf.toString();

            PriceDTO priceDTO = objectMapper.readValue(result, PriceDTO.class);
            return priceDTO.getPrice();
        }else{
            throw new RuntimeException();
        }
    }
}

