package com.avatar.trip.plan.authority;

import static com.avatar.trip.plan.auth.AuthAcceptanceTest.관리자_로그인;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.avatar.trip.plan.AcceptanceTest;
import com.avatar.trip.plan.authority.dto.AuthorityRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("권한 관리")
public class AuthorityAcceptanceTest extends AcceptanceTest {
    private static final String END_POINT = "/authorities";

    @Test
    @DisplayName("권한을 관리한다.")
    void manageAuthority() {
        String role = "ROLE_USER";
        //관리자 로그인 되어 있음.
        String accessToken = 관리자_로그인();

        //권한 생성
        ExtractableResponse<Response> response = 권한_생성_요청(accessToken, role, "");
        권한_생성_됨(response);

        //권한 목록 조회
        ExtractableResponse<Response> find = 권한_목록_조회(accessToken, 10, 0);
        권한_목록_조회됨(find, 2);

        //권한 한건 조회
        ExtractableResponse<Response> roleUser = 권한_조회(accessToken, role);
        권한_조회됨(roleUser, role);

        //권한 삭제
        ExtractableResponse<Response> delete = 권한_삭제_요청(accessToken, roleUser.jsonPath().getLong("id"));
        권한_삭제됨(delete);
    }

    public static ExtractableResponse<Response> 권한_생성_요청(String accessToken, String roleId, String name){
        AuthorityRequest request = new AuthorityRequest(roleId, name);
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post(END_POINT)
            .then().log().all()
            .extract();
    }

    public static void 권한_생성_됨(ExtractableResponse<Response> response){
       assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 권한_조회(String accessToken, String role){
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(END_POINT+"/"+role)
            .then().log().all()
            .extract();
    }

    public static void 권한_조회됨(ExtractableResponse<Response> response, String role){
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getString("roleId")).isEqualTo(role)
        );
    }

    private ExtractableResponse<Response> 권한_목록_조회(String accessToken, int size, int page){
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("size", size)
            .param("page", page)
            .when().get(END_POINT)
            .then().log().all()
            .extract();
    }

    private void 권한_목록_조회됨(ExtractableResponse<Response> response, int numberOfElements){
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getInt("numberOfElements")).isEqualTo(numberOfElements)
        );
    }

    private ExtractableResponse<Response> 권한_삭제_요청(String accessToken, Long id){
        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(END_POINT+"/"+id)
            .then().log().all()
            .extract();
    }

    private void 권한_삭제됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
