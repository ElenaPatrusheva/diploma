package ru.netology.diploma;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.diploma.db.DBManager;
import ru.netology.diploma.db.MySQLManager;
import ru.netology.diploma.db.PostgresManager;
import ru.netology.diploma.model.CreditFormPage;
import ru.netology.diploma.model.FormFiller;
import ru.netology.diploma.model.FormPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

class DBTest {
    private FormFiller successFormFiller;
    private FormPage page = new FormPage();
    private CreditFormPage creditFormPage = new CreditFormPage();
    private boolean isMySQL = Boolean.parseBoolean(System.getProperty("ms-enabled"));
    private DBManager dbManager;
    @BeforeEach
    void init() {
        if (isMySQL) {
            dbManager = new MySQLManager();
        } else {
            dbManager = new PostgresManager();
        }
        dbManager.cleanDB();
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
    @DisplayName("Проверка успешного сценария покупки")
    void successfulPaymentTest() {
        FormPage resultPage = page.fillForm(successFormFiller);
        resultPage.getHeaderText().shouldBe(visible);
        resultPage.getResultPopUp().shouldBe(visible, Duration.ofMillis(16000));
        Assertions.assertEquals("APPROVED", dbManager.getPaymentResult());
    }

    @Test
    @DisplayName("Проверка НЕуспешного сценария покупки")
    void unsuccessfulPaymentTest() {
        FormPage resultPage = page.fillForm(successFormFiller);
        resultPage.getHeaderText().shouldBe(visible);
        resultPage.getNegativeResultPopUp().shouldBe(visible, Duration.ofMillis(16000));
        Assertions.assertEquals("DECLINED", dbManager.getPaymentResult());
    }

    @Test
    @DisplayName("Проверка успешного сценария кредита по данным карты")
    void successfulCreditTest() {
        FormPage resultPage = creditFormPage.fillForm(successFormFiller);
        resultPage.getHeaderText().shouldBe(visible);
        resultPage.getResultPopUp().shouldBe(visible, Duration.ofMillis(16000));
        Assertions.assertEquals("APPROVED", dbManager.getCreditResult());
    }

    @Test
    @DisplayName("Проверка сценария отказа в кредите по данным карты")
    void unsuccessfulCreditTest() {
        successFormFiller.setCardNumber("4444 4444 4444 4442");
        FormPage resultPage = creditFormPage.fillForm(successFormFiller);
        resultPage.getHeaderText().shouldBe(visible);
        resultPage.getNegativeResultPopUp().shouldBe(visible, Duration.ofMillis(16000));
        Assertions.assertEquals("DECLINED", dbManager.getCreditResult());
    }

    @AfterEach
    void destroy() {
        dbManager.cleanDB();
    }
}
