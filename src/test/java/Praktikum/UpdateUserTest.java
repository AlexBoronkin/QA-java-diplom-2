package Praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class UpdateUserTest {

    private User user;
    private User userForUpdate;
    private Steps steps;
    private Login login;
    private String accessToken;

    @Before
    public void setUser() {
        user = User.getRandomUser();
        userForUpdate = User.getRandomUser();
        steps = new Steps();
        login = new Login(user);
    }

    @Test
    @DisplayName("Изменяем email авторизованного пользователя")
    public void authUserEmailUpdateTest() {

        User originalUser = User.getRandomUser();
        User clone = originalUser.clone();
        clone.setEmail(RandomStringUtils.randomAlphabetic(15) + "@yandex.ru");

        String accessToken = steps.createUser(originalUser).extract().header("Authorization");
        steps.updateUser(accessToken, clone);
        ValidatableResponse updatedCloneResponse = steps.getUser(accessToken);

        updatedCloneResponse.body("user.name", equalTo(originalUser.getName())).and().body("user.email", equalTo(clone.getEmail().toLowerCase()));

    }

    @Test
    @DisplayName("Изменяем пароль авторизованного пользователя")
    public void authUserPasswordUpdateTest() {

        User originalUser = User.getRandomUser();
        User clone = originalUser.clone();
        clone.setPassword("12345");

        String password = clone.getPassword();
        Assert.assertEquals("12345", password);

        String accessToken = steps.createUser(originalUser).extract().header("Authorization");
        ValidatableResponse response = steps.updateUser(accessToken, clone);
        response.assertThat().statusCode(200).and().body("success", is(true));



    }

    @Test
    @DisplayName("Изменяем имя авторизованного пользователя")
    public void authUserNameUpdateTest() {

        User originalUser = User.getRandomUser();
        User clone = originalUser.clone();
        clone.setName(RandomStringUtils.randomAlphabetic(15));

        String accessToken = steps.createUser(originalUser).extract().header("Authorization");
        steps.updateUser(accessToken, clone);
        ValidatableResponse updatedCloneResponse = steps.getUser(accessToken);

        updatedCloneResponse.body("user.name", equalTo(clone.getName())).and().body("user.email", equalTo(originalUser.getEmail().toLowerCase()));

    }

    @Test
    @DisplayName("Изменяем данные не авторизованного пользователя")
    public void noAuthUserUpdateTest() {
        userForUpdate.setEmail(RandomStringUtils.randomAlphabetic(15) + "@google.com");
        ValidatableResponse response = steps.updateUser("", userForUpdate);
        response.assertThat().statusCode(401).and().body("message", equalTo("You should be authorised"));
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            steps.deleteUser(accessToken);
        }
    }


}
