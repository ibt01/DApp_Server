package com.chengxi.prescription.file.service;


import com.chengxi.prescription.file.component.GlobalFileManagerConfig;
import com.go.basetool.APIResultCode;
import com.go.basetool.bean.UserClient;
import com.go.basetool.utils.JsonDtoWrapper;
import com.go.dto.OOSRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Calendar;

@Slf4j
@Service
public class FileService {

    @Autowired
    GlobalFileManagerConfig globalFileManagerConfig;

    public JsonDtoWrapper<OOSRes> upLoadFile(MultipartFile file, String business, UserClient userClient) throws Exception {
        JsonDtoWrapper jto = new JsonDtoWrapper<>();

        if (file.isEmpty()) {
            jto.setCodeMsg(APIResultCode.FILE_NULL);
            return jto;
        }

        String fileName = userClient.getUserID() + file.getOriginalFilename();
        String childFilePath = getChildFilePath(fileName, business);

        File dest = new File(getAbsoluteFilePath(childFilePath));
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest); // 保存文件
            jto.setCodeMsg(APIResultCode.SUCCESS);
            OOSRes oosRes = new OOSRes();
            oosRes.setUrl(getFileUrl(childFilePath, business));

            jto.setData(oosRes);
            return jto;
        } catch (Exception e) {
            log.error("store file", e);
            jto.setCodeMsg(APIResultCode.FAILURE);
            return jto;
        }
    }

    public String getChildFilePath(String fileName, String business) {
        String strDate = "" + Calendar.getInstance().get(Calendar.YEAR) + Calendar.getInstance().get(Calendar.MONTH) + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String childFilePath = business + "_" + strDate + "_" + fileName;
        return childFilePath;
    }

    public String getAbsoluteFilePath(String childFilePath) {
        return globalFileManagerConfig.getFileRootPath() + childFilePath;
    }

    public String getFileUrl(String childFilePath, String business) {
        return globalFileManagerConfig.getUrlRootPath() + childFilePath;
    }

    public JsonDtoWrapper readfile(String childFilePath, HttpServletRequest httpServletRequest, HttpServletResponse response, UserClient userClient) {

        JsonDtoWrapper j = new JsonDtoWrapper();

        try {
            String fileName = childFilePath;
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream;" + "filename: " + URLEncoder.encode(fileName));

            // 下载文件能正常显示中文
            response.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "x-auth-token,Origin,Access-Token,X-Requested-With,Content-Type, Accept,multipart/form-data,Authorization,my_cookie,my_uid,version,system_name,system_version,pwd,lang");
            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;


            File dest = new File(getAbsoluteFilePath(childFilePath));
            FileInputStream intstream = new FileInputStream(dest);

            OutputStream os = response.getOutputStream();
            try {

                int i = intstream.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = intstream.read(buffer);
                }
                os.flush();
                log.info("Download  successfully!");
                j.setCodeMsg(APIResultCode.SUCCESS);
                return j;

            } catch (Exception e) {
                System.out.println("Download  failed!");
                j.setCodeMsg(APIResultCode.UNKNOWN_ERROR);
                j.setExtraMsg(e.toString());
                return j;

            } finally {
                if (intstream != null) {
                    try {
                        intstream.close();
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            log.error("", e);
            j.setCodeMsg(APIResultCode.UNKNOWN_ERROR);
            j.setExtraMsg(e.toString());
            return j;
        }
    }
}
