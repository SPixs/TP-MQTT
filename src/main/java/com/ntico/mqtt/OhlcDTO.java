package com.ntico.mqtt;

import com.opencsv.bean.CsvBindByName;

public class OhlcDTO {
	
    @CsvBindByName
    public String timestamp;

    @CsvBindByName
    public String open;

    @CsvBindByName
    public String high;

    @CsvBindByName
    public String low;

    @CsvBindByName
    public String close;

    @CsvBindByName
    public String volume;
    
    @Override
    public String toString() {
    	return "["+timestamp+"] - Open=" + open +", High=" + high + ", Low=" + low + ", Close=" + close + ", Volume=" + volume;
    }
}