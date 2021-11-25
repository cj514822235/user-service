package com.tw.userservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ShareTask extends EntityBase {
    private String userId;
    private Long taskId;
}
