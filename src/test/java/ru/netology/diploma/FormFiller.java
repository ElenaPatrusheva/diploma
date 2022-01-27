package ru.netology.diploma;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormFiller {
    private String cardNumber;
    private String month;
    private String year;
    private String cardHolder;
    private String cvc;
    private String result;
}
