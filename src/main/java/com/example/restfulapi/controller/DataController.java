package com.example.restfulapi.controller;

import com.example.restfulapi.dto.Data;
import com.example.restfulapi.dto.DataResponse;
import com.example.restfulapi.service.SendDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

  private final SendDataService sendDataService;

  public DataController(SendDataService sendDataService) {
    this.sendDataService = sendDataService;
  }

  @PostMapping("/api/v1/send")
  public ResponseEntity<Object> sendDataToQueue(@RequestBody Data data) {
    sendDataService.sendData(data);
    return ResponseEntity.ok(new DataResponse(HttpStatus.OK.value(), "Success", data));
  }

  @GetMapping("/api/v1/hello")
  public ResponseEntity<Object> getHello() {
    return ResponseEntity.ok(new DataResponse(HttpStatus.OK.value(), "Success", "Hello World!"));
  }
}
