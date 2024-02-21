package combus.backend.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /* 200 OK : 요청 성공 */
    SIGNIN_SUCCESS(OK, "로그인 성공"),
    SIGNOUT_SUCCESS(OK, "로그아웃 성공"),
    EMPTY_RESERVATION_SUCCESS(OK, "예약 내역 존재하지 않습니다"),
    LOAD_RESERVATION_SUCCESS(OK,"예약 내역을 성공적으로 불러왔습니다"),
    RESERVATION_ONBOARDING_SUCCUESS(OK, "예약을 성공적으로 탑승 처리 했습니다."),
    RESERVATION_OFFBOARDING_SUCCESS(OK, "예약을 성공적으로 하차 처리 했습니다."),

    DRIVER_HOME_SUCCESS(OK,"버스기사 홈화면을 성공적으로 불러왔습니다"),
    BUS_STOP_DETAIL_SUCCESS(OK, "버스정류장 승객 정보를 성공적으로 불러왔습니다"),
    BUS_CHECK_SUCCESS(OK, "버스 번호 체크에 성공했습니다."),


    /* 201 CREATED : 요청 성공, 자원 생성 */
    CREATE_RESERVATION_SUCCESS(CREATED, "버스 예약을 성공했습니다."),

    /* 400 BAD_REQUEST : 잘못된 요청 */
    FAILED_RESERVATION_CREATTION(BAD_REQUEST,"예약이 실패하였습니다. 다시 시도해주세요" ),


    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    SESSION_EXPIRED(UNAUTHORIZED,"세션이 만료되었습니다. 다시 로그인해주세요"),
    AUTH_NUMBER_INCORRECT(UNAUTHORIZED, "회원 번호가 옳지 않습니다"),

    /* 403 FORBIDDEN : 권한이 없는 사용자 */

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    ACCOUNT_NOT_FOUND(NOT_FOUND, "계정 정보를 찾을 수 없습니다"),
    DRIVER_HOME_FAILED(NOT_FOUND,"버스 기사 홈 화면 로딩에 실패했습니다."),
    DRIVER_INFO_FAILED(NOT_FOUND,"버스 상세 화면 로딩에 실패하였습니다."),
    BUS_CHECK_FAILED(NOT_FOUND, "버스 번호 체크에 실패했습니다"),
    BUS_VIDEO_CHECK_FAILED(NOT_FOUND, "버스 번호 체크에 실패했습니다");


    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */


    private final HttpStatus httpStatus;
    private final String detail;

}