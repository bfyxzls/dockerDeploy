package com.lind.service1.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse {
  private int status;
  private String method;
  private String path;
  private Object extra;
  private ErrorItem errors;

  @Builder(toBuilder = true)
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public static class ErrorItem {
    private String code;
    private String message;
  }

}
