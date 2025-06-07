package com.metaverse.workflow.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String dateToString(Date date, String format) {
        if(date == null) return null;
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static Date stringToDate(String dateStr, String format) {
        if(dateStr == null) return null;
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
//    public static  Date stringToDate(String dateString, String format)  {
//        for (String formats : DATE_FORMATS) {
//            try {
//                DateFormat sdf = new SimpleDateFormat(formats);
//                sdf.setLenient(false);
//                return sdf.parse(dateString);
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return null;
//
//    }
//    private static final String[] DATE_FORMATS = {
//            "yyyy-MM-dd",
//            "dd-MM-yyyy",
//            "MM-dd-yyyy",
//            "dd/MM/yyyy",
//            "MM/dd/yyyy",
//            "yyyy/MM/dd",
//            "dd MMM yyyy",
//            "MMM dd, yyyy",
//            "yyyyMMdd"
//    };


}
