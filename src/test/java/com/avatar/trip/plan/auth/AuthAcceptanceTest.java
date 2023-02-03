package com.avatar.trip.plan.auth;

import static com.avatar.trip.plan.authority.AuthorityAcceptanceTest.권한_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.avatar.trip.plan.AcceptanceTest;
import com.avatar.trip.plan.auth.dto.LoginRequest;
import com.avatar.trip.plan.auth.dto.TokenRequest;
import com.avatar.trip.plan.user.dto.UserRequest;
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

    @Test
    @DisplayName("회원 가입 성공")
    void join_success() {
        //given
        String accessToken = 관리자_로그인();
        권한_생성_요청(accessToken, "ROLE_USER", "");

        //when
        ExtractableResponse<Response> response = 회원가입_요청("test@email.com", "11111", "test사용자");

        //then
        등록됨(response);
        ExtractableResponse<Response> loginResponse = 로그인_요청("test@email.com", "11111");
        assertThat(loginResponse.jsonPath().getString("accessToken")).isNotEmpty();
    }

    @Test
    @DisplayName("reissue 성공")
    void refresh_success() {
        // 로그인 되어 있음.
        ExtractableResponse<Response> login = 로그인_요청(ADMIN_EMAIL, ADMIN_PASSWORD);
        String refreshToken = login.jsonPath().getString("refreshToken");
        String accessToken = login.jsonPath().getString("accessToken");

        // refresh
        ExtractableResponse<Response> response = reissue(accessToken, refreshToken);

        // 새로운 토큰 발급 받음.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String newRefreshToken = response.jsonPath().getString("refreshToken");
        String newAccessToken = response.jsonPath().getString("accessToken");

        assertAll(
            () -> assertThat(newAccessToken).isNotEqualTo(accessToken),
            () -> assertThat(newRefreshToken).isNotEqualTo(refreshToken)
        );
    }

    @Test
    @DisplayName("reissue 실패")
    void refresh_failed() {
        // 로그인 되어 있음.
        ExtractableResponse<Response> login = 로그인_요청(ADMIN_EMAIL, ADMIN_PASSWORD);
        String accessToken = login.jsonPath().getString("accessToken");

        // refresh
        ExtractableResponse<Response> response = reissue(accessToken, "invalid");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 회원가입_요청(String email, String password, String nickname){
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new UserRequest(email, password, nickname))
            .when().post(ENDPOINT_PREFIX+"/join")
            .then().log().all()
            .extract();
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

    public static String 관리자_로그인(){
        ExtractableResponse<Response> response = 로그인_요청(ADMIN_EMAIL, ADMIN_PASSWORD);
        return response.jsonPath().getString("accessToken");
    }

    public static String 사용자_로그인(String email, String password, String nickname){
        ExtractableResponse<Response> join = 회원가입_요청(email, password, nickname);
        등록됨(join);
        ExtractableResponse<Response> login = 로그인_요청(email, password);
        return login.jsonPath().getString("accessToken");
    }

    ExtractableResponse<Response> reissue(String accessToken, String refreshToken){
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new TokenRequest(accessToken, refreshToken))
            .when().post(ENDPOINT_PREFIX + "/refresh")
            .then().log().all()
            .extract();
    }
}
