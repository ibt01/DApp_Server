package com.chengxi.prescription.file.controller;

import com.chengxi.prescription.file.service.FileService;
import com.go.basetool.threadstatus.AbstractController;
import com.go.basetool.utils.JsonDtoWrapper;
import com.go.dto.OOSRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("file")
public class FileManager extends AbstractController {
    @Autowired
    FileService fileService;

    @PostMapping("upLoadFile")
    @ResponseBody
    public JsonDtoWrapper<OOSRes> upLoadFile(@RequestParam("file") MultipartFile file) throws Exception {
        return fileService.upLoadFile(file, "edu", getLoginUser());
    }

    @CrossOrigin
    @RequestMapping(value = "/readfile/{childFilePath}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void downloadFile(@PathVariable(value = "childFilePath") String childFilePath, HttpServletRequest request, HttpServletResponse response) {
        fileService.readfile(childFilePath, request, response, getLoginUser());
    }


}
