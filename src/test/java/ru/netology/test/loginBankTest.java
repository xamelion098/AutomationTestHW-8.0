package ru.netology.test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.SQLHelper.cleanDB;

class LoginTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");

    }

    @AfterAll
    static void close() {
        cleanDB();

    }

    @Test
    void shouldSuccessfulLogin() {
        var loginPage = new LoginPage();
        var registratedUser = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(registratedUser);
        verificationPage.verificationPageVisible();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    void shouldFailLogin() {
        var loginPage = new LoginPage();
        var notRegistratedUser = DataHelper.generateRandomUser();
        var verificationPage = loginPage.validLogin(notRegistratedUser);
        verificationPage.errorNotificationVisible();
    }

    @Test
    void shouldLoginWithWrongCode() {
        var loginPage = new LoginPage();
        var registratedUser = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(registratedUser);
        verificationPage.verificationPageVisible();
        var verificationCode = DataHelper.getRandomCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.errorNotificationVisible();
    }

    @Test
    void shouldTestBlockedSystem() {
        var loginPage = new LoginPage();
        var registratedUser = DataHelper.getWrongAuthInfo();
        loginPage.notValidPass(registratedUser);
        loginPage.findErrorMessage("Пароль введен неверно более 3-х раз!Система заблокирована!");
    }
}