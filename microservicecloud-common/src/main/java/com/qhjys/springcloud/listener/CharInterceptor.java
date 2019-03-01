package com.qhjys.springcloud.listener;

import com.alibaba.fastjson.JSONObject;
import com.qhjys.springcloud.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * 特殊字符过滤器
 *
 * @author lzy
 */
@Slf4j
public class CharInterceptor implements HandlerInterceptor {

    private String encoding = "UTF-8";
    private String[] illegalChars;
    private String illegalChar = "$,',\",<,>,\\";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object o) throws Exception {
        illegalChars = illegalChar.split(",");

        //设置HTTP ONLY属性
        res.setHeader("Set-Cookie", "name=value; HttpOnly");

        //必须手动指定编码格式
        req.setCharacterEncoding(encoding);

        String tempURL = req.getRequestURI();

        //防止跨站点请求伪造漏洞
        String basePath = req.getHeader("X-Forwarded-Scheme") + "://" + req.getServerName();
        if(req.getHeader("X-Forwarded-Scheme") == null) {
            basePath = req.getScheme() + "://" + req.getServerName();
        }
        String referer = req.getHeader("Referer");
        if (referer != null && !referer.contains(basePath)) {
            //必须手动指定编码格式
            res.setContentType("text/html;charset=" + encoding);
            res.setCharacterEncoding(encoding);
            res.getWriter().print(JSONObject.toJSON(Response.create().error("发现跨域攻击")));
            return false;
        }

        Enumeration params = req.getParameterNames();

        //是否执行过滤  true：执行过滤  false：不执行过滤
        boolean executable = true;

        //非法状态  true：非法  false；不非法
        boolean illegalStatus = false;
        String illegalChar = "";
        //对参数名与参数进行判断
        w:
        while (params.hasMoreElements()) {

            String paramName = (String) params.nextElement();
            executable = true;

            if (executable && !paramName.equals("content")) {
                String[] paramValues = req.getParameterValues(paramName);

                f1:
                for (int i = 0; i < paramValues.length; i++) {

                    String paramValue = paramValues[i];

                    f2:
                    for (int j = 0; j < illegalChars.length; j++) {
                        illegalChar = illegalChars[j];

                        if (paramValue.indexOf(illegalChar) != -1) {
                            log.error("发现非法字符：" + illegalChar);
                            illegalStatus = true;//非法状态
                            break f2;
                        }
                    }

                    if (illegalStatus) {
                        break f1;
                    }

                }
            }

            if (illegalStatus) {
                break w;
            }
        }
        //对URL进行判断
        for (int j = 0; j < illegalChars.length; j++) {

            illegalChar = illegalChars[j];

            if (tempURL.indexOf(illegalChar) != -1) {
                illegalStatus = true;//非法状态
                break;
            }
        }

        if (illegalStatus) {
            log.error("当前链接中存在非法字符：" + illegalChar);

            //必须手动指定编码格式
            res.setContentType("text/html;charset=" + encoding);
            res.setCharacterEncoding(encoding);
            res.getWriter().print(JSONObject.toJSON(Response.create().error("发现非法字符")));
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
