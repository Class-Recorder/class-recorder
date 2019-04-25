package com.classrecorder.teacherserver.util;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class IsoToUtf {

    public static List<String> windowsConvert(List<String> command) {
        return command.stream()
                .map(c -> c.replaceAll("/",  Matcher.quoteReplacement("\\")))
                .map(c -> {
                    try {
                        return new String(c.getBytes("ISO-8859-1"), "UTF-8");
                    } catch(UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return c;
                    }
                })
                .collect(Collectors.toList());
    }

    public static List<String> linuxConvert(List<String> command) {
        return command.stream()
                .map(c -> {
                    try {
                        return new String(c.getBytes("ISO-8859-1"), "UTF-8");
                    } catch(UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return c;
                    }
                })
                .collect(Collectors.toList());
    }

    public static String linuxConvert(String text) {
        try {
            return new String(text.getBytes("ISO-8859-1"), "UTF-8");
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
            return text;
        }
    }

}