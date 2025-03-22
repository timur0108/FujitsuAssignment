package com.fuj.fujitsuproject.regionalbasefee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuj.fujitsuproject.city.City;
import com.fuj.fujitsuproject.city.CityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class RegionalBaseFeeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CityRepository cityRepository;

    @Test
    void testCreatingNewRbf() throws Exception{

        City city = new City();
        city.setName("TestCity");
        city.setStationName("mockStation");
        City savedCity = cityRepository.save(city);

        RegionalBaseFeeCreateDTO regionalBaseFeeCreateDTO = new RegionalBaseFeeCreateDTO();
        regionalBaseFeeCreateDTO.setAmount(BigDecimal.valueOf(10));
        regionalBaseFeeCreateDTO.setCityId(savedCity.getId());
        regionalBaseFeeCreateDTO.setVehicleId(1L);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(regionalBaseFeeCreateDTO);

        mockMvc.perform(post("/api/rbf").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        //RegionalBaseFee regionalBaseFee = regionalBaseFeeRepository.f
//        Vehicle usedVehicle = vehicleRepository.findById(1L).orElseThrow();
//        RegionalBaseFee createdFee = regionalBaseFeeRepository
//                .findByVehicleAndCityAndActiveTrue(usedVehicle, city).orElseThrow();
//
//        assertEquals(createdFee.isActive(), true);
//        assertEquals(createdFee.);

    }
}
