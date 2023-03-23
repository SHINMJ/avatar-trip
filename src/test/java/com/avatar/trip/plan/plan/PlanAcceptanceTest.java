package com.avatar.trip.plan.plan;

import static com.avatar.trip.plan.auth.AuthAcceptanceTest.사용자_로그인;
import static com.avatar.trip.plan.theme.ThemeAcceptanceTest.테마_등록_되어있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.avatar.trip.plan.AcceptanceTest;
import com.avatar.trip.plan.plan.dto.PlanRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("일정 관리")
public class PlanAcceptanceTest extends AcceptanceTest {
    private static final String  END_POINT = "/plans";

    @DisplayName("새로운 일정을 생성한다.")
    @Test
    void createSchedule() {
        String accessToken = 사용자_로그인("user@email.com", "aaaaa", "user");
        Long themeId = 테마_등록_되어있음(accessToken, "가족과", false);
        Long placeId = 1L;

        PlanRequest request = new PlanRequest(placeId, List.of(themeId), 1, 2, null, null);

        ExtractableResponse<Response> response = 일정_등록_요청(accessToken, request);

        등록됨(response);

        String id = getIdFromHeader(response, END_POINT.length() + 1);

        ExtractableResponse<Response> findResponse = 일정_한건_조회_요청(accessToken, id);

        조회됨(findResponse);
        assertAll(
            () -> assertThat(findResponse.jsonPath().getList("themes", String.class)).containsExactly("가족과"),
            () -> assertThat(findResponse.jsonPath().getString("period")).isEqualTo("1박 2일")
        );
    }

    public static ExtractableResponse<Response> 일정_등록_요청(String accessToken, PlanRequest request){
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .body(request)
            .when().post(END_POINT)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 일정_한건_조회_요청(String accessToken, String id){
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .when().get(END_POINT + "/" + id)
            .then().log().all()
            .extract();
    }
}
