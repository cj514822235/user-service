package com.tw.userservice.modle;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto extends EntityBase {

    private String userId;

    private List<Task> tasks;

}
