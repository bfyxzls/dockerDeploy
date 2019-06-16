package com.lind.service1.exception;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 异常拦截器.
 */
@Slf4j
@ControllerAdvice
public class ErrorHandler {
  static String getFullRequestUrl(HttpServletRequest request) {
    String fullRequestUrl = request.getRequestURI();
    String queryString = request.getQueryString();
    if (StringUtils.isNotBlank(queryString)) {
      fullRequestUrl = fullRequestUrl + "?" + queryString;
    }
    return fullRequestUrl;
  }

  /**
   * 错误对象赋值.
   *
   * @param status    .
   * @param code      .
   * @param exception .
   * @param request   .
   * @return
   */
  private ErrorResponse getErrorResponse(HttpStatus status,
                                         String code,
                                         Exception exception,
                                         HttpServletRequest request) {
    ErrorResponse.ErrorItem errorItem = ErrorResponse.ErrorItem.builder()
        .code(code)
        .message(exception.getMessage())
        .build();

    ErrorResponse errorResponse = ErrorResponse.builder()
        .extra(exception.getStackTrace())
        .status(status.value())
        .path(getFullRequestUrl(request))
        .method(request.getMethod())
        .errors(errorItem)
        .build();
    return errorResponse;
  }

  @ExceptionHandler(Exception.class)
  private ResponseEntity<ErrorResponse> exception(Exception exception, HttpServletRequest request) {
    ErrorResponse errorResponse = getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.INTERNAL_SERVER_ERROR, exception, request);
    logger.error("handle Exception, response = {}", errorResponse);
    return buildResponseEntity(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  private ResponseEntity<ErrorResponse> methodArgumentNotValidException(
      MethodArgumentNotValidException exception, HttpServletRequest request) {
    ErrorResponse errorResponse = getErrorResponse(HttpStatus.BAD_REQUEST, ErrorCodes.PARAM_ERROR, exception, request);
    logger.error("handle MethodArgumentNotValidException, response = {}", errorResponse);
    return buildResponseEntity(errorResponse);
  }

  @ExceptionHandler(IllegalStateException.class)
  private ResponseEntity<ErrorResponse> illegalStateException(IllegalStateException exception,
                                                              HttpServletRequest request) {

    ErrorResponse errorResponse = getErrorResponse(HttpStatus.PRECONDITION_FAILED, ErrorCodes.PRECONDITION_FAILED, exception, request);


    logger.error("handle IllegalStateException, response = {}", errorResponse);
    return buildResponseEntity(errorResponse);
  }

  @ExceptionHandler(NotFound.class)
  private ResponseEntity<ErrorResponse> notFoundException(IllegalStateException exception,
                                                              HttpServletRequest request) {

    ErrorResponse errorResponse = getErrorResponse(HttpStatus.PRECONDITION_FAILED, ErrorCodes.PRECONDITION_FAILED, exception, request);


    logger.error("handle IllegalStateException, response = {}", errorResponse);
    return buildResponseEntity(errorResponse);
  }


  private ResponseEntity<ErrorResponse> buildResponseEntity(ErrorResponse errorResponse) {
    return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
  }

}
