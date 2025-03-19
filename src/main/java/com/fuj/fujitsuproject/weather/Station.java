package com.fuj.fujitsuproject.weather;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class Station {

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "wmocode")
    private String wmoCode;

    @JacksonXmlProperty(localName = "phenomenon")
    private String phenomenon;

    @JacksonXmlProperty(localName = "windspeed")
    private BigDecimal windSpeed;

    @JacksonXmlProperty(localName = "airtemperature")
    private BigDecimal airTemperature;
}
