package com.avatar.trip.plan.theme;

import static com.avatar.trip.plan.auth.AuthAcceptanceTest.관리자_로그인;
import static org.assertj.core.api.Assertions.assertThat;

import com.avatar.trip.plan.AcceptanceTest;
import com.avatar.trip.plan.theme.dto.ThemeRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;;
import org.springframework.http.MediaType;

@DisplayName("테마 관리")
public class ThemeAcceptanceTest extends AcceptanceTest {
    private static final String ENDPOINT = "/themes";

    @Test
    @DisplayName("관리자 테마 관리")
    void manageThemeOfAdmin() {
        // 관리자 로그인
        String accessToken = 관리자_로그인();
        String theme = "아이와";
        boolean isAdmin = true;

        // 테마 등록
        Long id = 테마_등록_되어있음(accessToken, theme, isAdmin);

        // 테마 한건 조회
        ExtractableResponse<Response> findResponse = 테마_한건_조회(accessToken, id);
        조회됨(findResponse);
        assertThat(findResponse.jsonPath().getBoolean("admin")).isEqualTo(isAdmin);

        // 테마 목록 조회
        ExtractableResponse<Response> listResponse = 테마_목록_조회(accessToken, isAdmin);
        조회됨(listResponse);
        assertThat(listResponse.jsonPath().getInt("totalElements")).isEqualTo(1);

        // 테마 삭제
        ExtractableResponse<Response> deleteResponse = 테마_삭제_요청(accessToken, id);
        삭제됨(deleteResponse);

        // 테마 목록 조회
        ExtractableResponse<Response> listResponse2 = 테마_목록_조회(accessToken, isAdmin);
        조회됨(listResponse2);
        assertThat(listResponse2.jsonPath().getInt("totalElements")).isEqualTo(0);
    }

    public static Long 테마_등록_되어있음(String accessToken, String theme, boolean isAdmin){
        ExtractableResponse<Response> response = 테마_등록_요청(accessToken, theme, isAdmin);
        등록됨(response);
        String uri = response.header("Location");

        String id = uri.substring(ENDPOINT.length() + 1);
        return Long.valueOf(id);
    }

    static ExtractableResponse<Response> 테마_등록_요청(String accessToken, String theme, boolean isAdmin){
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .body(new ThemeRequest(theme, isAdmin))
            .when().post(ENDPOINT)
            .then().log().all()
            .extract();
    }

    ExtractableResponse<Response> 테마_한건_조회(String accessToken, Long id){
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .when().get(ENDPOINT+"/"+id)
            .then().log().all()
            .extract();
    }



    ExtractableResponse<Response> 테마_목록_조회(String accessToken, boolean isAdmin){
        String endpoint = ENDPOINT;
        if(!isAdmin){
            endpoint += "/by/users";
        }

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .param("size", 10)
            .param("page", 0)
            .when().get(endpoint)
            .then().log().all()
            .extract();
    }

    ExtractableResponse<Response> 테마_삭제_요청(String accessToken, Long id){
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .when().delete(ENDPOINT+"/"+id)
            .then().log().all()
            .extract();
    }

}
