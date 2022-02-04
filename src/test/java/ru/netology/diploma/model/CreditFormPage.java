package ru.netology.diploma.model;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class CreditFormPage extends FormPage {
    private SelenideElement buyButton = $(byText("Купить в кредит"));
    private SelenideElement headerText = $(byText("Кредит по данным карты"));


    @Override
    protected SelenideElement getBuyButton() {
        return buyButton;
    }

    @Override
    public SelenideElement getHeaderText() {
        return headerText;
    }
}
