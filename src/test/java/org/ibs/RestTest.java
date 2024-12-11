package org.ibs;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class RestTest {
    String baseUrl = "http://localhost:8080/";
    String url = "api/food";
    String resetUrl = "api/data/reset";
    String banan = "{\n \"name\": \"Банан\",\n" +
            "\"type\": \"FRUIT\",\n" +
            " \"exotic\": false \n }";
    String pitaya = "{\"name\": \"Питайя\",\n" +
            "\"type\": \"FRUIT\",\n" +
            " \"exotic\": true}";
    String cucumber = "{\"name\": \"Огурец\",\n" +
            "\"type\": \"VEGETABLE\",\n" +
            " \"exotic\": false}";

    @Test
    void test1() throws InterruptedException {

        //Проверяем что продуктов которые будем добавлять нет в базе
        Cookies cookies = given()
                .baseUri(baseUrl)
                .when()
                .get(url)
                .then()
                .assertThat()
                .statusCode(200)
                .log().all()
                .extract().detailedCookies();

        //Добавим банан
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(banan)
                .when()
                .post(baseUrl + url)
                .then()
                .assertThat()
                .statusCode(200);

        //Добавим питайю
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(pitaya)
                .when()
                .post(baseUrl+url)
                .then()
                .assertThat()
                .statusCode(200);

        //Добавим огурец
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(cucumber)
                .when()
                .post(baseUrl+url)
                .then()
                .assertThat()
                .statusCode(200);

        //Проверим что продукты добавились
        given()
                .cookies(cookies)
                .when()
                .get(baseUrl + url)
                .then()
                .assertThat()
                .statusCode(200)
                .log().all();

        //Очистим таблицу
        given()
                .cookies(cookies)
                .baseUri(baseUrl)
                .when()
                .post(resetUrl)
                .then()
                .assertThat()
                .statusCode(200)
                .log().all();

        //Проверим что таблица была очищена от добавленых нами продуктов
        given()
                .cookies(cookies)
                .when()
                .get(baseUrl + url)
                .then()
                .assertThat()
                .statusCode(200)
                .log().all();
    }

}
