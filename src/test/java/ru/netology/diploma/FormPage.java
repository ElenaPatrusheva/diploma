package ru.netology.diploma;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FormPage {
    private SelenideElement buyButton = $(byText("Купить"));
    @Getter
    private SelenideElement headerText = $(byText("Оплата по карте"));
    private SelenideElement cardNumberField = $("input[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthField = $("input[placeholder='08']");
    private SelenideElement yearField = $("input[placeholder='22']");
    private SelenideElement cardHolderField = $$("input[class='input__control']").get(3);
    private SelenideElement cvcField = $$("input[class='input__control']").get(4);
    private SelenideElement nextButton = $(byText("Продолжить"));
    @Getter
    private SelenideElement incorrectFormatCardNumberWarning = $$("span[class='input__sub'").get(0);
    @Getter
    private SelenideElement resultPopUp = $(byText("Операция одобрена Банком."));
    @Getter
    private SelenideElement negativeResultPopUp = $(byText("Ошибка! Банк отказал в проведении операции."));
    @Getter
    private SelenideElement incorrectMonthWarning = $(byText("Неверно указан срок действия карты"));
    @Getter
    private SelenideElement incorrectCvcWarning = $(byText("Неверный формат"));

    public FormPage fillForm(FormFiller formFiller) {
        getBuyButton().click();
        cardNumberField.click();
        cardNumberField.setValue(formFiller.getCardNumber());
        monthField.click();
        monthField.setValue(formFiller.getMonth());
        yearField.click();
        yearField.setValue(formFiller.getYear());
        cardHolderField.click();
        cardHolderField.setValue(formFiller.getCardHolder());
        cvcField.click();
        cvcField.setValue(formFiller.getCvc());
        nextButton.click();
        return this;
    }

    public FormPage updateMonth(String month) {
        monthField.doubleClick();
        monthField.sendKeys(month);
        monthField.setValue(month);
        nextButton.click();
        return this;
    }

    public FormPage updateCvc(String cvc) {
        cvcField.doubleClick();
        cvcField.sendKeys(cvc);
        nextButton.click();
        return this;
    }

    protected SelenideElement getBuyButton() {
        return buyButton;
    }
}
