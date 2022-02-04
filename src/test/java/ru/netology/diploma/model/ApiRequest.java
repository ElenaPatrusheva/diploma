package ru.netology.diploma.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequest {
    private String number;
    private String year;
    private String month;
    private String holder;
    private String cvc;
}
