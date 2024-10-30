package org.team1.nbe1_3_team01.global.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.Error;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 미처 잡지 못한 예외를 잡는 Handler
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    ResponseEntity<Error> handleException(final Exception e) {
        log.error(e.getMessage(), e);

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );

    }

    /**
     * 커스텀한 예외 처리
     * (다른 에러코드를 보낼 필요가 있다면 메서드 추가 생성 가능)
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<Error> handleBoardExceptions(
            final AppException e
    ) {
        e.printStackTrace();    //개발 완료 후 삭제해야할 코드
        return buildErrorResponse(
                e.getErrorCode().getStatus(),
                e.getErrorCode().getMessage()
        );
    }

    /**
     * 요청 파라미터 누락 시 발생하는 에러
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<Error> handleRequestParameterException(
            final MissingServletRequestParameterException e
    ) {
        e.printStackTrace();
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "요청 파라미터가 누락되었습니다."
        );
    }

    /**
     * @Valid 검증 오류 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleValidationExceptions(
            final MethodArgumentNotValidException e
    ) {
        e.printStackTrace();    //개발 완료 후 삭제해야할 코드

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                errors);
    }

    // BadRequest 응답 생성
    private ResponseEntity<Error> buildErrorResponse(HttpStatus status, Object message) {
        return ResponseEntity.status(status)
                .body(Error.of(status.value(), message));
    }
}
