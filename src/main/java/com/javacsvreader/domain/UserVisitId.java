package com.javacsvreader.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UserVisitId implements Serializable {

    private String email;
    private String phone;
    private String source;
}
