package com.example.demo.service;

import com.example.demo.Sorting.CurrencyRateSorting;
import com.example.demo.model.CurrencyRate;
import com.example.demo.model.ExchangeRate;
import com.example.demo.network.NetWorkDAO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;


@Data
@Service
public class MoneyConverter {
  private List<ExchangeRate> list=new ArrayList<>();
  private List<CurrencyRate> currencyRateList=new ArrayList<>();

  @Autowired
  private NetWorkDAO netWorkDAO;
  /**
   * Đọc dữ liệu từ file JSON vào JsonNode masterNode
   */

  public MoneyConverter(){
//    GetFromWebApi();
    loadExchangeRateFromJSON();
    parseCurrencyCode();
  }

  private void loadExchangeRateFromJSON() {
    File file = null;
    try {
      file = ResourceUtils.getFile("classpath:static/exchangerate-api.json");
      FileReader reader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(reader);
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode masterNode = objectMapper.readTree(bufferedReader);
      JsonNode rate = masterNode.get("conversion_rates");
      Iterator<Map.Entry<String, JsonNode>> iter=rate.fields();
      while (iter.hasNext()){
        var node=iter.next();
        ExchangeRate rate1=new ExchangeRate(node.getKey(),Float.parseFloat(String.valueOf(node.getValue())));
        list.add(rate1);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Lấy tỷ giá chuyển đổi 1 USD sang currencyCode
   * @param currencyCode
   * @return
   */
  public float getExchangeRate(String currencyCode) {
    for (ExchangeRate e:list){
      if(e.getCode().equalsIgnoreCase(currencyCode)){
        return e.getRate();
      }
    }
    return 0;
  }

  public void parseCurrencyCode() {
    try {
      File file1= ResourceUtils.getFile("classpath:static/currency.csv");
      CsvMapper mapper = new CsvMapper();
      CsvSchema schema=CsvSchema.emptySchema().withHeader();
      ObjectReader oReader=mapper.readerFor(CurrencyRate.class).with(schema);
      Reader reader=new FileReader(file1);
      MappingIterator<CurrencyRate> mi=oReader.readValues(reader);
      while (mi.hasNext()){
        CurrencyRate currencyRate=mi.next();
        boolean check=true;
        for (CurrencyRate rate:currencyRateList){
        if(rate.getCode().equals(currencyRate.getCode())) {
          check=false;
          break;
        }
        }
        if(check==true){
          currencyRate.setRate(getExchangeRate(currencyRate.getCode()));
          currencyRateList.add(currencyRate);
        }
      }
      currencyRateList.sort(new CurrencyRateSorting());
    }catch (IOException e){

    }
  }

  public void GetFromWebApi() {
    // Setting URL
    try {
      String rawJSON=netWorkDAO.request("https://v6.exchangerate-api.com/v6/23c7707ada880786bf11e212/latest/USD");
      JSONObject root=new JSONObject(rawJSON);
      JSONObject conversion = root.getJSONObject("conversion_rates");
      Iterator iter=conversion.keys();
      while (iter.hasNext()){
        String key=(String) iter.next();
        float value=Float.parseFloat(String.valueOf(conversion.getJSONObject(key)));
        ExchangeRate rate=new ExchangeRate(key,value);
        list.add(rate);
      }
    }catch (Exception e){

    }
  }

  public String formatCurrency(float money){
    String[] realresults= BigDecimal.valueOf(money).toPlainString().split("[.]");
    StringBuilder builder=new StringBuilder();
    int start=realresults[0].length();
    for (int i=realresults[0].length()-1;i>=0;i--){
      builder.append(realresults[0].charAt(i));
      if(i ==start-3 && i!=0){
        builder.append(',');
        start=i;
      }
    }
    return String.valueOf(builder.reverse())+'.'+realresults[1];
  }

  public float getResult(float amount,String currencyA,String currencyB){
    return Float.valueOf(amount * getExchangeRate(currencyB) / getExchangeRate(currencyA));
  }
}
