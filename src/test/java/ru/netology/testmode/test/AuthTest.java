package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    public void clicker() {
        $$("button").find(exactText("Продолжить")).click();
    }

    public void setData(DataGenerator.RegistrationDto status) {
        $("[data-test-id='login'] .input__control").setValue(status.getLogin());
        $("[data-test-id='password'] .input__control").setValue(status.getPassword());
        clicker();
    }

    public void errorText(String error) {
        $("[data-test-id=error-notification]").shouldHave(text(error));
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        setData(registeredUser);
        $("h2.heading").shouldHave(text(" Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        setData(notRegisteredUser);
        errorText("Ошибка! Неверно указан логин или пароль");
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        setData(blockedUser);
        errorText("Ошибка! Пользователь заблокирован");
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] .input__control").setValue(wrongLogin);
        $("[data-test-id='password'] .input__control").setValue(registeredUser.getPassword());
        clicker();
        errorText("Ошибка! Неверно указан логин или пароль");


    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] .input__control").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] .input__control").setValue(wrongPassword);
        clicker();
        errorText("Ошибка! Неверно указан логин или пароль");
    }


}
