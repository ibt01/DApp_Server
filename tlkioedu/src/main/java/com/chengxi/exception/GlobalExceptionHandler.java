package com.chengxi.exception;


import com.go.basetool.APIResultCode;
import com.go.basetool.exception.GlobalException;
import com.go.basetool.threadstatus.AbstractController;
import com.go.basetool.utils.JsonDtoWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends AbstractController {

    @ExceptionHandler(value = Exception.class)
    public JsonDtoWrapper handler(Exception e) {
        JsonDtoWrapper r = new JsonDtoWrapper();
        if (e instanceof GlobalException) {
            GlobalException selectNoFindException = (GlobalException) e;
            r.setMsg(selectNoFindException.getMyException());
            r.setCode(selectNoFindException.getCode());
            r.setExtraMsg(selectNoFindException.getExtraMsg());
            log.error(selectNoFindException.getMessage());
            return r;
        } else if (e instanceof UnexpectedRollbackException) {
            if (null != getLoginUser().getCurrentAPIResultCode()) {
                r.setCodeMsg(getLoginUser().getCurrentAPIResultCode());
            } else {
                r.setCode(APIResultCode.UNKNOWN_ERROR.getCode());
                r.setMsg(e.toString());
                r.setExtraMsg(e.getMessage());
            }
            log.error(e.getMessage());
            return r;
        } else {
            r.setCode(APIResultCode.UNKNOWN_ERROR.getCode());
            r.setMsg("系统正忙，稍后再试");
            r.setExtraMsg(e.getMessage());
            log.error(e.getMessage());
            return r;
        }
    }
}

