package com.mmall;

import org.junit.Test;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CrmTest {
    @Test
    public void postCrm() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2018-01-22");
        System.out.println(date.getTime());
    }

}
