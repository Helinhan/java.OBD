package com.hantong.control;

import com.hantong.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class Entry{


    @RequestMapping("/interface")
    public String Interface(){
        return "redirect:swagger-ui.html";
    }

    @RequestMapping("/login")
    public String Login(){
        return "login";
    }


    @RequestMapping(value = "/do/**")
    public String Do(ModelMap model,HttpServletRequest request) throws Exception{

        String path = request.getAttribute( HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE ).toString();
        path = path.replace("/do/","");

        try {
            org.springframework.util.ResourceUtils.getURL("classpath:templates/" + path + ".ftl");
        }catch (Exception e) {
            org.springframework.util.ResourceUtils.getURL("classpath:templates/" + path + ".html");
        }

        model.put("request",request);
        return path;
    }

}
