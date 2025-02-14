package com.ruqimobility.precisiontest.utils;

import com.ruqi.auth.support.restful.domain.ruqi.User;
import com.ruqi.common.constant.ReturnCodes;
import com.ruqi.common.utils.Assert;
import com.ruqi.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ruqi.auth.support.restful.config.Constant.RUQI_USER_SESSION_KEY;


@Slf4j
public class LoginUtil {

    /**
     * 获得当前服务线程的登录用户信息
     * 请用getCurrentLoginUser()
     *
     * @return 用户信息
     */
    @Deprecated
    public static User getCurrentThreadLoginUser() {
        return getUserFromHttpSession();
    }

    /**
     * 从当前登陆会话中获取登陆用户信息
     *
     * @return 用户信息
     */
    public static User getUserFromHttpSession() {
        try {
            HttpSession session = getHttpSession();
            String userStr = (String) session.getAttribute(RUQI_USER_SESSION_KEY);
            return JsonUtils.fromJson(userStr, User.class);
        } catch (Exception e) {
            log.warn("getUserFromHttpSession failed.", e);
            return null;
        }
    }

    /**
     * 设置当前服务线程的登录用户信息
     */
    public static void setCurrentThreadLoginUser(User user) {
        HttpSession session = getHttpSession();
        session.setAttribute(RUQI_USER_SESSION_KEY, JsonUtils.toJson(user));
    }
    public static List<String> transferCookie(Cookie[] cookies){
        List<String> cookieList = new ArrayList<>();
        for(Cookie cookie:cookies){
            if(Objects.equals(cookie.getName(),"TGC")) {
                String ck = cookie.getName() + "=" + cookie.getValue();
                cookieList.add(ck);
            }
        }
        return cookieList;
    }

    private static HttpSession getHttpSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request.getSession();
    }

    public static Long getCurrentLoginUid() {
        return getCurrentLoginUser().getUid();
    }

    public static User getCurrentLoginUser() {
        User loginUser = null;
        if(getUserFromHttpSession() != null){
            loginUser = getUserFromHttpSession();
            Assert.notNull(loginUser, ReturnCodes.USER_NOT_LOGIN);
        }
        return loginUser;
    }

    public static String escapeChar(String before){
        if(!ObjectUtils.isEmpty(before)){
            before = before.replaceAll("\\\\", "\\\\\\\\") ;
            before = before.replaceAll("_", "\\\\_") ;
            before = before.replaceAll("%", "\\\\\\\\%") ;
        }
        return before;
    }
}
