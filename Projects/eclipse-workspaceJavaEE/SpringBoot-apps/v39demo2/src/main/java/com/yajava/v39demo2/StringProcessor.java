package com.yajava.v39demo2;

import org.springframework.stereotype.Component;

@Component
public class StringProcessor {

    public String reverse(String text) {
        return new StringBuilder(text).reverse().toString();
    }

    public int stringLength(String text) {
        return text.length();
    }

    public String prefixString(String text) {
        return "BEGIN - " + text;
    }
}
