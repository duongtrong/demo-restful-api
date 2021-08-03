package com.example.restfulapi.service;

import com.example.restfulapi.dto.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SendDataService {

  private final RabbitTemplate rabbitTemplate;

  private final RedisTemplate<String, Data> redisTemplate;

  public SendDataService(RabbitTemplate rabbitTemplate, RedisTemplate<String, Data> redisTemplate) {
    this.rabbitTemplate = rabbitTemplate;
    this.redisTemplate = redisTemplate;
  }

  public void sendData(Data data) {
    redisTemplate.opsForHash().put("data", data.getMobileNo(), data);
    rabbitTemplate.convertAndSend("exchange", "binding", data);
  }
}
