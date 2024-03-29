package com.avatar.trip.plan;

import static org.assertj.core.api.Assertions.assertThat;

import com.avatar.trip.plan.utils.DatabaseInitialize;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class AcceptanceTest {
    public static final String ADMIN_EMAIL = "admin@email.com";
    public static final String ADMIN_PASSWORD = "11111";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseInitialize databaseInitialize;

    @BeforeEach
    public void setUp() {
        if(RestAssured.port == RestAssured.UNDEFINED_PORT){
            RestAssured.port = port;
            databaseInitialize.afterPropertiesSet();
        }
        databaseInitialize.execute();

        databaseInitialize.saveInitData();
    }

    public static void 등록됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 조회됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static String getIdFromHeader(ExtractableResponse<Response> response, int length){
        String uri = response.header("Location");
        return uri.substring(length);
    }

}
