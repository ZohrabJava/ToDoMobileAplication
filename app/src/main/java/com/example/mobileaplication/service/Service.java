package com.example.mobileaplication.service;

import com.example.mobileaplication.model.TaskDto;
import com.example.mobileaplication.model.User;

import java.util.List;

public class Service {

    public static TaskDto getTaskDtoFromListById(List<TaskDto> taskDtoList,Long id){
        TaskDto dto = new TaskDto();
        for (TaskDto taskDto : taskDtoList) {
            if (taskDto.getTaskId().equals(id)){
                dto = taskDto;
                break;
            }
        }
        return dto;
    }
    public static User getUserDtoFromListById(List<User> usersList, Long id){
        User dto = new User();
        for (User user : usersList) {
            if (user.getUserId().equals(id)){
                dto = user;
                break;
            }
        }
        return dto;
    }
}
