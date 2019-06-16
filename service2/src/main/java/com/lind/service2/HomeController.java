package com.lind.service2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lind.service2.feign.Service1Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
  @Autowired
  Service1Client service1Client;
  @Autowired
  ObjectMapper objectMapper;

  @GetMapping("/")
  public String index() {
    return "serivce2 hello index" + service1Client.get();
  }

  @GetMapping("/get")
  public String get() {
    return "service2.get";
  }

  @GetMapping("/err1")
  public String getError() throws Exception {
    ResponseEntity<?> response = service1Client.getError1();
    return objectMapper.writeValueAsString(response.getStatusCode());
  }
}
