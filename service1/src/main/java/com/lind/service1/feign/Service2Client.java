package com.lind.service1.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("service2")
public interface Service2Client {
  @GetMapping("/get")
  String get();
}
