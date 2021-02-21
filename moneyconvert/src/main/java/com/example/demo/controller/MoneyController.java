package com.example.demo.controller;

import com.example.demo.result.MoneyConvertResult;
import com.example.demo.service.MoneyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MoneyController {
    @Autowired
    private MoneyConverter converter;

    @GetMapping
    public String getCurrencyForm(Model model){
        model.addAttribute("currency",new MoneyConvertResult());
        model.addAttribute("countries",converter.getCurrencyRateList());
        model.addAttribute("checkPost",false);
        return "Currency";
    }

    @PostMapping
    public String ConvertCurrency(@ModelAttribute MoneyConvertResult convertResult, BindingResult result, Model model){
        if(!result.hasErrors()) {
            float convertResultAtoB =converter.getResult(convertResult.getAmount(),convertResult.getCurrencyA(),convertResult.getCurrencyB());
            float AtoBAmount1=converter.getResult(1,convertResult.getCurrencyA(),convertResult.getCurrencyB());
            float BtoAAmount1=converter.getResult(1,convertResult.getCurrencyB(),convertResult.getCurrencyA());
            model.addAttribute("result",converter.formatCurrency(convertResultAtoB));
            model.addAttribute("currency",convertResult);
            model.addAttribute("exchangeA",converter.formatCurrency(AtoBAmount1));
            model.addAttribute("exchangeB",converter.formatCurrency(BtoAAmount1));
            model.addAttribute("countries",converter.getCurrencyRateList());
            model.addAttribute("checkPost",true);
        }
        return "Currency";
    }

}
