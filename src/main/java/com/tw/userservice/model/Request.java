package com.tw.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Request {

  // private Long Id;
   private String UserId;
   private Level level;
}
