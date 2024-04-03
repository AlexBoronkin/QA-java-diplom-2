package Praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest {

    private User user;
    private Login login;
    private Steps steps;
    private String accessToken;


    @Before
    public void setOrder() {
        user = User.getRandomUser();
        steps = new Steps();
        login = new Login(user);
    }

    @Test
    @DisplayName("Cоздать заказ без авторизации")
    public void createOrderWithoutAuthTest() {
        ValidatableResponse response = steps.createOrderWithoutLogin();
        response.assertThat().statusCode(200).and().body("success", is(true));
    }

    @Test
    @DisplayName("Создать заказ с авторизацией и ингредиентами")
    public void createOrderWithAuthAndIngredientsTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.loginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        ValidatableResponse response1 = steps.createOrderLoggedUser(accessToken);
        response1.assertThat().statusCode(200).and().body("success", is(true));
    }

    @Test
    @DisplayName("Создать заказ с авторизацией и без ингредиентов")
    public void createOrderWithAuthAndWithoutIngredientsTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.loginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        steps.createOrderWithoutIngredients(accessToken);

    }

    @Test
    @DisplayName("Создать заказ с неверным хешем ингредиентов")
    public void createOrderWithWrongIngredientHashTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.loginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        steps.createOrderWithWrongIngredientHash(accessToken);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            steps.deleteUser(accessToken);
        }
    }

}
