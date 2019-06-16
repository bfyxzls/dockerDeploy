package com.lind.service1.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
  private String userName;
  private Integer age;
}
