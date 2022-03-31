import com.codeborne.selenide.Configuration;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;


public class AuthTest {

    @BeforeEach
    public void setUpAll() {
        Configuration.holdBrowserOpen = true; //не закрывает браузер по оканчанию теста
        Configuration.browserSize = "800x800"; //размер открывающегося окна
        open("http://localhost:9999");
    }


    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        val registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] .input__control").setValue(registeredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login] .button__text").click();
        $(withText("Личный кабинет")).shouldBe(visible, Duration.ofSeconds(5));
    }



    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        val notRegisteredUser = getUser("blocked");
        $("[data-test-id=login] .input__control").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=action-login] .button__text").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(visible).shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        val blockedUser = getUser("blocked");
        $("[data-test-id=login] .input__control").setValue(blockedUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login] .button__text").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(visible).shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        val registeredUser = getRegisteredUser("active");
        val wrongLogin = getRandomLogin();
        $("[data-test-id=login] .input__control").setValue(wrongLogin);
        $("[data-test-id=password] .input__control").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login] .button__text").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(visible).shouldHave(text("Неверно указан логин или пароль"));
    }


    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        val registeredUser = getRegisteredUser("active");
        val wrongPassword = getRandomPassword();
        $("[data-test-id=login] .input__control").setValue(registeredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(wrongPassword);
        $("[data-test-id=action-login] .button__text").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(visible).shouldHave(text("Неверно указан логин или пароль"));
    }

}




