package Praktikum;


import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class Steps extends SetBaseURL {

    @Step ("Создаем нового пользователя")
    public ValidatableResponse createUser(User user) {
        return given().log().all().spec(getSpec()).body(user).when().post(REGISTER_ENDPOINT).then().log().all();
    }

    @Step ("Авторизация")
    public ValidatableResponse loginUser(Login loginUser) {
        return given().log().all().spec(getSpec()).body(loginUser).when().post(LOGIN_ENDPOINT).then().log().all();
    }

    @Step ("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given().log().all().spec(getSpec()).header("Authorization", accessToken).when().delete(USER_ENDPOINT).then().log().all();
    }

    @Step ("Изменение пользователя")
    public ValidatableResponse updateUser(String accessToken, User user) {
        return given().log().all().spec(getSpec()).header("Authorization", accessToken).body(user).when().patch(USER_ENDPOINT).then().log().all();
    }

    @Step ("Получение данных пользователя")
    public ValidatableResponse getUser(String accessToken) {
        return given().log().all().spec(getSpec()).header("Authorization", accessToken).when().get(USER_ENDPOINT).then().log().all();
    }

    @Step ("Создание заказа будучи авторизованым")
    public ValidatableResponse createOrderLoggedUser(String accessToken) {
        return given().spec(getSpec()).header("Authorization", accessToken).when().body("{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\",\"61c0c5a71d1f82001bdaaa72\"]\n}").post(ORDER_ENDPOINT).then().log().all();
    }

    @Step ("Попытка создать заказ без авторизации")
    public ValidatableResponse createOrderWithoutLogin() {
        return given().spec(getSpec()).when().body("{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\",\"61c0c5a71d1f82001bdaaa72\"]\n}").post(ORDER_ENDPOINT).then().log().all();
    }

    @Step ("Передаем некорректный хэш ингредиента при создании заказа")
    public ValidatableResponse createOrderWithWrongIngredientHash(String accessToken) {
        return given().spec(getSpec()).header("Authorization", accessToken).when().body("{\n\"ingredients\": [\"12345c0c5a71d1f82001bdaaa6d\",\"12345c0c5a71d1f82001bdaaa6f\",\"12345c0c5a71d1f82001bdaaa72\"]\n}").post(ORDER_ENDPOINT).then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Step ("Попытка создать заказ без ингредиентов")
    public ValidatableResponse createOrderWithoutIngredients (String accessToken) {
        return given().spec(getSpec()).header("Authorization", accessToken).when().post(ORDER_ENDPOINT).then().assertThat().statusCode(400).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step ("Получение ингредиентов")
    public ValidatableResponse getIngredients() {
        return given().spec(getSpec()).when().get(INGREDIENTS_ENDPOINT).then().log().all();
    }

    @Step ("Получение списка заказов авторизованного пользователя")
    public ValidatableResponse getUserOrders(String accessToken) {
        return given().spec(getSpec()).header("Authorization", accessToken).when().get(ORDER_ENDPOINT).then().log().all().assertThat().statusCode(200).and().body("success", is(true));
    }

    @Step ("Получение списка заказов не авторизовавшись")
    public ValidatableResponse getListOfOrdersWithoutUserToken() {
        return given().spec(getSpec()).when().get(ORDER_ENDPOINT).then().log().all();
    }




}
