package com.avatar.trip.plan.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.avatar.trip.plan.AcceptanceTest;
import com.avatar.trip.plan.auth.dto.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("로그인 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String ENDPOINT_PREFIX = "/auth";

    @DisplayName("로그인 성공")
    @Test
    void login_success() {
        ExtractableResponse<Response> loginRes = 로그인_요청(ADMIN_EMAIL, ADMIN_PASSWORD);

        assertThat(loginRes.jsonPath().getString("accessToken")).isNotEmpty();
    }

    @Test
    @DisplayName("로그인 실패 - 사용자 없음")
    void login_failed_nouser() {
        ExtractableResponse<Response> response = 로그인_요청("test", ADMIN_PASSWORD);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password){
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new LoginRequest(email, password))
            .when().post(ENDPOINT_PREFIX+"/login")
            .then().log().all()
            .extract();
    }

    public static String 관리자_로그인_되어있음(){
        ExtractableResponse<Response> response = 로그인_요청(ADMIN_EMAIL, ADMIN_PASSWORD);
        return response.jsonPath().getString("accessToken");
    }
}
