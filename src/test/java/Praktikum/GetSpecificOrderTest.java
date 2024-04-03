package Praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class GetSpecificOrderTest {

    private User user;
    private Steps steps;
    private String accessToken;
    private Login login;

    @Before
    public void setUser() {
        user = User.getRandomUser();
        steps = new Steps();
        login = new Login(user);
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getAuthUserOrdersTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.loginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        steps.createOrderLoggedUser(accessToken);
        steps.getUserOrders(accessToken);
    }

    @Test
    @DisplayName("Получение заказов не авторизованного пользователя")
    public void getNoAuthUserOrdersTest() {
        ValidatableResponse response = steps.getListOfOrdersWithoutUserToken();
        response.assertThat().statusCode(401).and().body("message", equalTo("You should be authorised"));
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            steps.deleteUser(accessToken);
        }
    }



}
