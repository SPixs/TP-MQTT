package com.ntico.mqtt;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.io.FileReader;

/**
 * This class connects to AlphaVantage API And returns a list of OhlcDTO objects
 */
public class AlphaConnect {

    public static List<OhlcDTO> getOHLC(String symbol) throws Exception {

        System.out.println("Requesting OHLC");

        URL url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="+symbol+"&interval=5min&apikey=demo&datatype=csv");
        
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        List<OhlcDTO> beans = new CsvToBeanBuilder(reader).withType(OhlcDTO.class).build().parse();
        System.out.println("COUNT : " + beans.size());
        return beans;
    }
}