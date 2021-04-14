package com.chengxi.tool.message.controller;


import com.chengxi.tool.message.dto.SendMessageReq;
import com.chengxi.tool.message.service.MessageService;
import com.go.basetool.utils.JsonDtoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {

    @Autowired
    MessageService messageService;
    @PostMapping("sendPhoneMsg")
    @ResponseBody
    public JsonDtoWrapper sendPhoneMsg(@RequestBody SendMessageReq sendMessageReq){
        return messageService.sendPhoneMsg(sendMessageReq.getPhoneNumber());
    }
}
