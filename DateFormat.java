package com.jpm.Stock.Main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
    public Date parse(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date parsedDate = new Date();
        try {
            parsedDate = formatter.parse(date);
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;
    }
}
