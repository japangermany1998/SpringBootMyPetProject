package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class CurrencyRate {
  private String country;
  private String countryCode;
  private String currency;
  private String code;
  private float rate; // Quy đổi 1 USD sang được bao nhiêu
}
