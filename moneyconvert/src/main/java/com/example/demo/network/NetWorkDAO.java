package com.example.demo.network;

import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class NetWorkDAO {
    public String request(String endpoint) throws Exception{
        StringBuilder sb=new StringBuilder();

        URL url=new URL(endpoint);

        //open a connection to this url
        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();

        //Read in the bytes
        try {
            InputStream inputStream = urlConnection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            //Read them as character
            InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //read 1 line at a time
            String inputLine = bufferedReader.readLine();
            while (inputLine != null) {
                sb.append(inputLine);
                inputLine = bufferedReader.readLine();
            }
        }finally {
            urlConnection.disconnect();
        }
        return sb.toString();
    }
}
