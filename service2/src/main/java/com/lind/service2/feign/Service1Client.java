package com.lind.service2.feign;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("service1")
public interface Service1Client {

  @GetMapping("/get")
  String get();

  @GetMapping("/data")
  ResponseEntity<?> getData();

  @GetMapping("/map")
  ResponseEntity<?> getMap();

  @GetMapping("/err1")
  ResponseEntity<?> getError1();

  @GetMapping("/err2")
  Map<String, Object> getErr2();
}
