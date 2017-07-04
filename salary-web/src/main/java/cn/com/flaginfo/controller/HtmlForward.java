package cn.com.flaginfo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.flaginfo.user.auth.common.StringUtil;


/**
 * 解决跨域问题
 */
@Controller
@RequestMapping("/person")
public class HtmlForward {
    @RequestMapping(value = "/info/{type}")
    public void forward(@PathVariable(value="type") String type ,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	String defaultUrl = request.getContextPath()+"/personInfo/personList.html";
        if(StringUtils.isEmpty(type)){
        	toHtml(response,  defaultUrl);
        	return;
        }
        switch (type){
            case "personList": //测试首页
                toHtml(response, request.getContextPath()+"/personInfo/personList.html");
                break;
            case "statistics": //测试首页
            	toHtml(response, request.getContextPath()+"/personInfo/Statistics.html");
            	break;
           default:
                toHtml(response,  defaultUrl);
        }
    }

    private void toHtml(HttpServletResponse response,String path) throws Exception{
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.sendRedirect(path);
    }
}
