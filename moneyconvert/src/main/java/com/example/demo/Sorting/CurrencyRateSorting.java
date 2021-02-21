package com.example.demo.Sorting;

import com.example.demo.model.CurrencyRate;

import java.util.Comparator;

public class CurrencyRateSorting implements Comparator<CurrencyRate> {

    @Override
    public int compare(CurrencyRate o1, CurrencyRate o2) {
        return o1.getCurrency().compareTo(o2.getCurrency());
    }
}
