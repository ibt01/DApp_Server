package com.chengxi.prescription.reseps.controller;

import com.chengxi.prescription.reseps.dto.CreateRespReq;
import com.chengxi.prescription.reseps.dto.GetMyResepReq;
import com.chengxi.prescription.reseps.dto.ShowMyResepQrDto;
import com.chengxi.prescription.reseps.dto.ShowMyResepReq;
import com.chengxi.prescription.reseps.service.ResepService;
import com.go.basetool.bean.UserClient;
import com.go.basetool.commonreq.PageReq;
import com.go.basetool.threadstatus.AbstractController;
import com.go.basetool.utils.JsonDtoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.print.DocFlavor;


@Controller
@RequestMapping("resep")
public class ResepController extends AbstractController {
    @Autowired
    ResepService resepService;

    @PostMapping("/createResep")
    @ResponseBody
    public JsonDtoWrapper createResep(@RequestBody CreateRespReq createRespReq) {
        return resepService.createResep(createRespReq, getLoginUser());
    }

    @PostMapping("/getMyReseps")
    @ResponseBody
    public JsonDtoWrapper getMyReseps(@RequestBody GetMyResepReq pageReq) {
        return resepService.getMyReseps(pageReq, getLoginUser());
    }

    @PostMapping("/getResepById")
    @ResponseBody
    public JsonDtoWrapper getResepById(@RequestBody GetMyResepReq getMyResepReq){
        return resepService.getResepById(getMyResepReq);
    }

    @PostMapping("/showMyResepsQr")
    @ResponseBody
    public JsonDtoWrapper showMyResepsQr(@RequestBody ShowMyResepReq showMyResepReq) {
        return resepService.showMyResepsQr(showMyResepReq, getLoginUser());
    }

    @PostMapping("/getResetByQr")
    @ResponseBody
    public JsonDtoWrapper getResetByQr(@RequestBody ShowMyResepQrDto showMyResepQrDto) {
        return resepService.getResetByQr(showMyResepQrDto, getLoginUser());
    }

    @PostMapping("/dealResetByQr")
    @ResponseBody
    public JsonDtoWrapper dealResetByQr(@RequestBody ShowMyResepQrDto showMyResepQrDto) {
        return resepService.dealResetByQr(showMyResepQrDto, getLoginUser());
    }
}
