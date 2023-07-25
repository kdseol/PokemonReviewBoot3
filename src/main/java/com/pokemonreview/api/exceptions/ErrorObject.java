package com.pokemonreview.api.exceptions;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Data
public class ErrorObject {
    private Integer statusCode;
    private String message;
    //private Date timestamp;
    private String timestamp;

    public String getTimestamp() {
        LocalDateTime ldt = LocalDateTime.now();
        return DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss E a",
                Locale.KOREA).format(ldt);
    }
}