package ru.netology.diploma;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.diploma.model.CreditFormPage;
import ru.netology.diploma.model.FormFiller;
import ru.netology.diploma.model.FormPage;

import java.time.Duration;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class CreditWebFormTest {
    private FormFiller successFormFiller;
    private CreditFormPage page = new CreditFormPage();

    @BeforeEach
    void init() {
        successFormFiller = FormFiller.builder()
                .cardNumber("4444 4444 4444 4441")
                .month("12")
                .year("23")
                .cardHolder("IVAN IVANOV")
                .cvc("123")
                .build();
        open("http://localhost:8080");
    }

    @Test
    @DisplayName("Проверка успешного сценария кредита по данным карты")
    void successfulCreditTest() {
        FormPage resultPage = page.fillForm(successFormFiller);
        resultPage.getHeaderText().shouldBe(visible);
        resultPage.getResultPopUp().shouldBe(visible, Duration.ofMillis(16000));
    }

    @Test
    @DisplayName("Проверка сценария отказа в кредите по данным карты")
    void unsuccessfulCreditTest() {
        successFormFiller.setCardNumber("4444 4444 4444 4442");
        FormPage resultPage = page.fillForm(successFormFiller);
        resultPage.getNegativeResultPopUp().shouldBe(visible, Duration.ofMillis(16000));
    }

    @Test
    @DisplayName("Проверка сценария отказа в кредите по данным карты не из списка для теста")
    void unsuccessfulCreditTestV2() {
        successFormFiller.setCardNumber("4444 4444 4444 4445");
        FormPage resultPage = page.fillForm(successFormFiller);
        resultPage.getNegativeResultPopUp().shouldBe(visible, Duration.ofMillis(16000));
    }

    @ParameterizedTest
    @MethodSource("cardNumbers")
    @DisplayName("Проверка предупреждения о длине номера карты")
    void incorrectCardNumberLengthTest(String cardNumber) {
        successFormFiller.setCardNumber(cardNumber);
        FormPage resultPage = page.fillForm(successFormFiller);
        resultPage.getIncorrectFormatCardNumberWarning().shouldBe(visible);
    }

    static Stream<Arguments> cardNumbers() {
        return Stream.of(
                arguments("4444"),
                arguments("4444 4444 FFFF 4441"),
                arguments("")
        );
    }

    @Test
    @DisplayName("Проверка предупреждения о некорректном месяце")
    void incorrectMonthTest() {
        successFormFiller.setMonth("13");
        FormPage resultPage = page.fillForm(successFormFiller);
        assertEquals("Неверно указан срок действия карты", resultPage.getIncorrectMonthWarning().getText());
    }

    @Test
    @DisplayName("Проверка сокрытия предупреждения о некорректном месяце")
    void correctMonthWarningTest() {
        successFormFiller.setMonth("13");
        FormPage resultPage = page.fillForm(successFormFiller);

        FormPage page1 = resultPage.updateMonth("08");
        page1.getIncorrectMonthWarning().shouldNot(visible);
    }

    @Test
    @DisplayName("Проверка предупреждения о некорректном cvc")
    void incorrectCvcWarningTest() {
        successFormFiller.setCvc("13");
        FormPage resultPage = page.fillForm(successFormFiller);
        assertEquals("Неверный формат", resultPage.getIncorrectCvcWarning().getText());
    }

    @Test
    @DisplayName("Проверка сокрытия предупреждения о некорректном cvc")
    void correctCvcWarningTest() {
        successFormFiller.setCvc("13");
        FormPage resultPage = page.fillForm(successFormFiller);
        FormPage page1 = resultPage.updateCvc("085");
        page1.getIncorrectCvcWarning().shouldNot(visible);
    }

}
