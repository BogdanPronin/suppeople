package com.github.bogdan;

import com.github.bogdan.utilitis.NewThread;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException, SQLException, UnsupportedEncodingException {
        System.out.println( URLDecoder.decode("Hell%C3%B6%20W%C3%B6rld%40Java", StandardCharsets.UTF_8.toString()));
    }
}
