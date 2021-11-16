package com.javacsvreader.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class SourceUserAnswer {

    private String source;
    private Long count;
}
