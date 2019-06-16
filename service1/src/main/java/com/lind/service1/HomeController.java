package com.lind.service1;

import com.google.common.collect.ImmutableMap;
import com.lind.service1.dto.UserDto;
import com.lind.service1.feign.Service2Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  @Autowired
  Service2Client service2Client;

  @GetMapping("/")
  public String index() {
    return "hello index service1:" + service2Client.get();
  }

  @GetMapping("/get")
  public String get() {
    return "service1.get";
  }

  @GetMapping("/data")
  public ResponseEntity<?> getData() {
    return ResponseEntity.ok(UserDto.builder().age(1).userName("zzl").build());
  }

  @GetMapping("/map")
  public ResponseEntity<?> getMap() {
    return ResponseEntity.ok(
        ImmutableMap.of("data", UserDto.builder().age(1).userName("zzl").build()));
  }

  private void err1() {
    int a = 0;
    int b = 1 / a;
  }

  private void err2() {
    int a = 0;
    int b = 1 / a;
  }

  @GetMapping("/err1")
  public ResponseEntity<?> getError1() {
    err1();
    err2();
    return ResponseEntity.ok(UserDto.builder().age(1 / 1).userName("zzl").build());
  }

  @GetMapping("/err2")
  public UserDto getError2() {
    int a = 0;
    return UserDto.builder().age(1 / a).userName("zzl").build();
  }
}
