package Praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class LoginTest {

    private User user;
    private Login login;
    private Steps steps;
    private String accessToken;


    @Before
    public void setUser() {
        user = User.getRandomUser();
        steps = new Steps();
        login = new Login(user);
    }

    @Test
    @DisplayName("Проверка успешной авторизации существующего пользователя")
    public void loginExistingUserIsSuccessTest() {
        steps.createUser(user);
        ValidatableResponse responce = steps.loginUser(login);
        responce.assertThat().statusCode(200).and().body("success", is(true));
    }

    @Test
    @DisplayName("Проверка авторизации без ввода логина")
    public void loginWithoutEmailTest() {
        user.setEmail("");
        ValidatableResponse responce = steps.loginUser(login);
        responce.assertThat().statusCode(401).and().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверка авторизации без ввода пароля")
    public void loginWithoutPasswordTest() {
        user.setPassword("");
        ValidatableResponse responce = steps.loginUser(login);
        responce.assertThat().statusCode(401).and().body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void CleanUp() {
        if (accessToken != null) {
            steps.deleteUser(accessToken);
        }
    }













}
