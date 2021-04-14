package com.chengxi.filter;

import com.chengxi.cache.constant.CacheConstant;
import com.chengxi.cache.service.RedisClient;
import com.go.basetool.APIResultCode;
import com.go.basetool.bean.UserClient;
import com.go.basetool.threadstatus.DataBinderManager;
import com.go.basetool.threadstatus.ThreadConstant;
import com.go.basetool.user.AuthLoginRes;
import com.go.basetool.utils.JsonDtoWrapper;
import com.go.constant.ComYesNo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Component
public class LoginFilter implements Filter {


    @Autowired
    RedisClient redisClient;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        redisClient = ctx.getBean("redisClient", RedisClient.class);
        log.info("[redis对象],初始化...");
    }

    public static List<String> setUrlReleaseRestriction = new ArrayList<>();
    public static List<String> setEpManagerInterface = new ArrayList<>();
    public static List<String> setCenterManagerInterface = new ArrayList<>();

    public static List<String> requirepPrivateKeyPWDManagerPassword = new ArrayList<>();

    static {
        setUrlReleaseRestriction.add("/sendPhoneMsg");
        setUrlReleaseRestriction.add("/searchHospitalByBluryName");
        setUrlReleaseRestriction.add("/login");
        setUrlReleaseRestriction.add("/register");
        setUrlReleaseRestriction.add("/readfile/");
        requirepPrivateKeyPWDManagerPassword.add(("/downloadFile"));
    }

    public static Boolean checkRequirepPrivateKeyPWDManagerPassword(String url){
        for (String urlItr : requirepPrivateKeyPWDManagerPassword) {
            if (url.indexOf(urlItr) != -1) {
                return true;
            }
        }
        return false;
    }
    public static Boolean checkManagerInterface(String url) {
        for (String urlItr : setEpManagerInterface) {
            if (url.indexOf(urlItr) != -1) {
                return true;
            }
        }
        return false;
    }

    public static Boolean checkSuperManagerInterface(String url) {
        for (String urlItr : setCenterManagerInterface) {
            if (url.indexOf(urlItr) != -1) {
                return true;
            }
        }
        return false;
    }

    public static Boolean releaseRestriction(String url) {
        for (String urlItr : setUrlReleaseRestriction) {
            if (url.indexOf(urlItr) != -1) {
                return true;
            }
        }
        return false;
    }


    public AuthLoginRes auth_login(String cookie, String uid) {

        AuthLoginRes authLoginRes = new AuthLoginRes();
        if (StringUtils.isEmpty(cookie) || StringUtils.isEmpty(uid)) {
            authLoginRes.setB(ComYesNo.no);
            return authLoginRes;
        }

        String userInfo = redisClient.get(CacheConstant.getUserInfoRrefix(uid));
        if (StringUtils.isEmpty(userInfo)) {
            authLoginRes.setB(ComYesNo.no);
            return authLoginRes;
        }

        UserClient userClient = new Gson().fromJson(userInfo, UserClient.class);
        if (null == userClient || StringUtils.isEmpty(userClient.getCookie()) || StringUtils.isEmpty(userClient.getUserID())) {
            authLoginRes.setB(ComYesNo.no);
            return authLoginRes;
        }

        if (cookie.equals(userClient.getCookie())) {
            authLoginRes.setUserClient(userClient);
            authLoginRes.setB(ComYesNo.yes);
            return authLoginRes;
        }

        authLoginRes.setB(ComYesNo.no);
        return authLoginRes;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");

        //response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization,token, Content-Type,my_cookie,my_uid,my_privatekeypass");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization,token, content-type,my_cookie,my_uid,my_privatekeypass,version,system_name,system_version,pwd,lang");
        //response.setHeader("Access-Control-Allow-Headers", "my_privatekeypass,x-auth-token,Origin,Access-Token,X-Requested-With,Content-Type, Accept,multipart/form-data,Authorization,my_cookie,my_uid,version,system_name,system_version,pwd,lang,");

        Enumeration<String> headerNames = request.getHeaderNames();
        log.info("------");
        while (headerNames.hasMoreElements()) {
            String nextElement = headerNames.nextElement();
            log.error(nextElement + ":" + request.getHeader(nextElement));
        }
        log.info("------");

        String cookieValue = ((HttpServletRequest) servletRequest).getHeader("my_cookie");
        String uid = ((HttpServletRequest) servletRequest).getHeader("my_uid");
        String priaveKeyPassManager= ((HttpServletRequest) servletRequest).getHeader("my_privatekeypass");


        UserClient userClient = new UserClient();
        DataBinderManager.<UserClient>getDataBinder(ThreadConstant.REQUEST_USER_BINDER).put(userClient);


        String url = ((HttpServletRequest) servletRequest).getRequestURI();


        if (releaseRestriction(url)) {
            log.info("--------enter login");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        log.info(url);


        if (!StringUtils.isEmpty(cookieValue)) {
            Integer checkStatus = checkCookieAndPrivilege(cookieValue, uid);
            if (checkok == checkStatus) {
                userClient.setCookie(cookieValue);
                userClient.setUserID(uid);
                userClient.setIp(getIpAddr(request));
                DataBinderManager.<UserClient>getDataBinder(ThreadConstant.REQUEST_USER_BINDER).put(userClient);

                filterChain.doFilter(servletRequest, servletResponse);
            } else if (checknoprivilege == checkStatus) {
                JsonDtoWrapper< String> jo = new JsonDtoWrapper();
                jo.setCodeMsg(APIResultCode.NO_PRIVILEGE);
                servletResponse.setContentType("application/json");
                servletResponse.setCharacterEncoding("UTF-8");
                PrintWriter out = servletResponse.getWriter();
                out.print(new Gson().toJson(jo));
                out.flush();
            } else if (checkcookieinvalid == checkStatus) {


                log.error("pe_cookie == null || pe_cookie.getValue() == null");

                JsonDtoWrapper<String> jo = new JsonDtoWrapper();
                jo.setCodeMsg(APIResultCode.NOT_LOGIN);
                servletResponse.setContentType("application/json");
                servletResponse.setCharacterEncoding("UTF-8");
                PrintWriter out = servletResponse.getWriter();
                out.print(new Gson().toJson(jo));
                out.flush();
            }else if(frozened == checkStatus){
                JsonDtoWrapper<String> jo = new JsonDtoWrapper();
                jo.setCodeMsg(APIResultCode.USER_FROZENED);
                servletResponse.setContentType("application/json");
                servletResponse.setCharacterEncoding("UTF-8");
                PrintWriter out = servletResponse.getWriter();
                out.print(new Gson().toJson(jo));
                out.flush();
            }else if(privateKeyNotRight == checkStatus){
                JsonDtoWrapper<String> jo = new JsonDtoWrapper();
                jo.setCodeMsg(APIResultCode.PASSWORD_ERROR);
                servletResponse.setContentType("application/json");
                servletResponse.setCharacterEncoding("UTF-8");
                PrintWriter out = servletResponse.getWriter();
                out.print(new Gson().toJson(jo));
                out.flush();
            }
            return;
        }

        log.error("pe_cookie == null || pe_cookie.getValue() == null");

        JsonDtoWrapper<String> jo = new JsonDtoWrapper();
        jo.setCodeMsg(APIResultCode.NOT_LOGIN);
        servletResponse.setContentType("application/json");
        servletResponse.setCharacterEncoding("UTF-8");
        PrintWriter out = servletResponse.getWriter();
        out.print(new Gson().toJson(jo));
        out.flush();
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }

    public static Integer checkok = 1;
    public static Integer checkcookieinvalid = 2;
    public static Integer checknoprivilege = 3;
    public static Integer frozened = 4;
    public static Integer privateKeyNotRight = 5;

    Integer checkCookieAndPrivilege(String cookie, String uid) {
        AuthLoginRes authLoginRes = auth_login(cookie, uid);
        if (null != authLoginRes && authLoginRes.getB() == ComYesNo.yes) {
            return checkok;
        } else {
            return checkcookieinvalid;
        }
    }

    @Override
    public void destroy() {

    }
}
