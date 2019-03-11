package com.qhjys.springcloud.controller;//package com.qhjys.springcloud.controller;
//
//import com.qhjys.springcloud.util.SwaggerUtil;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Slf4j
//@RestController
//@RequestMapping("/provider/swagger")
//@Api(tags = "Swagger")
//public class SwaggerController {
//
//    @Resource
//    private SwaggerUtil swaggerUtil;
//
//    @PostMapping(value = "/login")
//    @ApiOperation(value = "Swagger-Login")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userName", value = "账号", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query")
//    })
//    public void login(HttpServletRequest request, HttpServletResponse response, String userName, String password) {
//        try {
//            if (swaggerUtil.validSwagger(userName, password)) {
//                request.getSession().setAttribute("swagger_flag", true);
//                response.sendRedirect("/swagger-ui.html");
//            } else {
//                response.setContentType("text/html;charset=UTF-8");
//                response.setCharacterEncoding("UTF-8");
//                response.getWriter().print("<script>alert('账号或密码错误');window.history.go(-1);</script>");
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}
