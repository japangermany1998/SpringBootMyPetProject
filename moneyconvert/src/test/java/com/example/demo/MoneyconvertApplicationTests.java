package com.example.demo;

import com.example.demo.service.MoneyConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MoneyconvertApplicationTests {
	MoneyConverter moneyConverter=new MoneyConverter();

	void contextLoads() {
		float rate=moneyConverter.getExchangeRate("USD");
		assertThat(rate).isEqualTo(rate);
	}

}
