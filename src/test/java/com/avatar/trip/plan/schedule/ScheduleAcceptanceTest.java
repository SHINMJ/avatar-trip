package com.avatar.trip.plan.schedule;


import static com.avatar.trip.plan.auth.AuthAcceptanceTest.사용자_로그인;
import static com.avatar.trip.plan.plan.PlanAcceptanceTest.일정등록되어있음;
import static com.avatar.trip.plan.theme.ThemeAcceptanceTest.테마_등록_되어있음;
import static org.assertj.core.api.Assertions.assertThat;

import com.avatar.trip.plan.AcceptanceTest;
import com.avatar.trip.plan.schedule.dto.ScheduleBudgetRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleNoteRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleRequest;
import com.avatar.trip.plan.schedule.dto.ScheduleResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("스케쥴 관리")
public class ScheduleAcceptanceTest extends AcceptanceTest {

    private static final String  END_POINT = "/schedules";

    @DisplayName("1일차 2개 스케쥴 관리")
    @Test
    void manageSchedule() {
        /**
         * 로그인되어 있다.
         * 테마가 생성되어 있다.
         * 1박2일 여행계획이 생성되어 있다.
         * 1일차 첫번째 스케쥴을 등록한다. -> 1일차 첫번째 스케쥴이 등록된다.
         * 첫번째 스케쥴을 조회한다. -> 조회된다.
         * 첫번째 스케쥴의 예산을 입력한다. -> 수정된다.
         * 첫번째 스케쥴의 메모를 입력한다. -> 수정된다.
         * 1일차 두번재 스케쥴을 입력한다. -> 등록된다.
         * 두번째 스케쥴을 조회한다. -> 조회된다.
         * 두번째 스케쥴의 예산을 입력한다. -> 수정된다.
         * 1일차 총예산을 조회한다. -> 총예산이 조회된다.
         * 1일차 모든 스케쥴을 조회한다. -> 스케쥴 목록(2개)이 조회된다.
         * 1일차 첫번째 스케쥴을 삭제한다. -> 삭제된다.
         * 1일차 모든 스케쥴을 조회한다. -> 스케쥴 목록(1개)이 조회된다.
         */
        String accessToken = 사용자_로그인("user@email.com", "aaaaa", "user");
        Long themeId = 테마_등록_되어있음(accessToken, "가족과", false);
        Long planId = 일정등록되어있음(accessToken, 1L, List.of(themeId), 1, 2);

        ExtractableResponse<Response> saveResponse1 = 스케쥴_등록_요청(accessToken,
            new ScheduleRequest(1, 2L, 1, planId));
        등록됨(saveResponse1);

        Long schedule1Id = Long.valueOf(getIdFromHeader(saveResponse1, END_POINT.length() + 1));
        ExtractableResponse<Response> schedule1Response = 스케쥴_한건_조회(accessToken, schedule1Id);
        조회됨(schedule1Response);

        ExtractableResponse<Response> schedule1Budget = 스케쥴_예산입력_요청(accessToken, schedule1Id,
            new ScheduleBudgetRequest(BigDecimal.valueOf(200_000)));
        수정됨(schedule1Budget);

        ExtractableResponse<Response> schedule1Note = 스케쥴_메모입력_요청(accessToken, schedule1Id,
            new ScheduleNoteRequest("note"));
        수정됨(schedule1Note);

        ExtractableResponse<Response> saveResponse2 = 스케쥴_등록_요청(accessToken,
            new ScheduleRequest(1, 3L, 2, planId));
        등록됨(saveResponse2);

        Long schedule2Id = Long.valueOf(getIdFromHeader(saveResponse2, END_POINT.length() + 1));
        ExtractableResponse<Response> schedule2Response = 스케쥴_한건_조회(accessToken, schedule2Id);
        조회됨(schedule2Response);

        ExtractableResponse<Response> schedule2Budget = 스케쥴_예산입력_요청(accessToken, schedule2Id,
            new ScheduleBudgetRequest(BigDecimal.valueOf(10_000)));
        수정됨(schedule2Budget);

        ExtractableResponse<Response> totalResponse = 스케쥴_n_일차_예산_조회_요청(accessToken, planId, 1);
        조회됨(totalResponse);
        assertThat(totalResponse.body().as(BigDecimal.class)).isEqualByComparingTo(BigDecimal.valueOf(210_000.00));


        ExtractableResponse<Response> listResponse1 = 스케쥴_n_일차_목록_조회_요청(accessToken, planId, 1);
        조회됨(listResponse1);
        assertThat(listResponse1.body().as(List.class).size()).isEqualTo(2);

        ExtractableResponse<Response> deleteResponse = 스케쥴_한건_삭제(accessToken, schedule1Id);
        삭제됨(deleteResponse);

        ExtractableResponse<Response> listResponse2 = 스케쥴_n_일차_목록_조회_요청(accessToken, planId, 1);
        조회됨(listResponse2);
        assertThat(listResponse2.body().as(List.class).size()).isEqualTo(1);

    }

    ExtractableResponse<Response> 스케쥴_등록_요청(String accessToken, ScheduleRequest request){
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .body(request)
            .when().post(END_POINT)
            .then().log().all()
            .extract();
    }

    ExtractableResponse<Response> 스케쥴_한건_조회(String accessToken, Long id){
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .when().get(END_POINT + "/" + id)
            .then().log().all()
            .extract();
    }

    ExtractableResponse<Response> 스케쥴_예산입력_요청(String accessToken, Long id, ScheduleBudgetRequest request){
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .body(request)
            .when().put(END_POINT+"/budgets/"+id)
            .then().log().all()
            .extract();
    }

    ExtractableResponse<Response> 스케쥴_메모입력_요청(String accessToken, Long id, ScheduleNoteRequest request){
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .body(request)
            .when().put(END_POINT+"/notes/"+id)
            .then().log().all()
            .extract();
    }

    ExtractableResponse<Response> 스케쥴_n_일차_예산_조회_요청(String accessToken, Long planId, int day){
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .when().get(END_POINT + "/" + planId +"/budgets/"+day)
            .then().log().all()
            .extract();
    }

    ExtractableResponse<Response> 스케쥴_n_일차_목록_조회_요청(String accessToken, Long planId, int day){
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .when().get(END_POINT + "/" + planId +"/"+day)
            .then().log().all()
            .extract();
    }

    ExtractableResponse<Response> 스케쥴_한건_삭제(String accessToken, Long id){
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .when().delete(END_POINT + "/" + id)
            .then().log().all()
            .extract();
    }
}
