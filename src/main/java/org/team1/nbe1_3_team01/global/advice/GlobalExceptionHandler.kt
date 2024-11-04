package org.team1.nbe1_3_team01.global.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.Error

@RestControllerAdvice
class GlobalExceptionHandler {

    /**
     * 미처 잡지 못한 예외를 잡는 Handler
     */
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<Error> {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")
    }

    /**
     * 커스텀한 예외 처리
     */
    @ExceptionHandler(AppException::class)
    fun handleBoardExceptions(e: AppException): ResponseEntity<Error> {
        return buildErrorResponse(e.errorCode.status, e.errorCode.getMessage())
    }

    /**
     * 요청 파라미터 누락 시 발생하는 에러
     */
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleRequestParameterException(e: MissingServletRequestParameterException): ResponseEntity<Error> {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "요청 파라미터 '${e.parameterName}'가 누락되었습니다.")
    }

    /**
     * @Valid 검증 오류 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(e: MethodArgumentNotValidException): ResponseEntity<Error> {
        val errors = e.bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
        return buildErrorResponse(HttpStatus.BAD_REQUEST, errors)
    }

    // BadRequest 응답 생성
    private fun buildErrorResponse(status: HttpStatus, message: Any): ResponseEntity<Error> {
        return ResponseEntity.status(status).body(Error.of(status.value(), message))
    }
}
