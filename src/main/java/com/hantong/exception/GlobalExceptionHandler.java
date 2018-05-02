package com.hantong.exception;

import com.hantong.code.ErrorCode;
import com.hantong.result.Result;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

@ControllerAdvice
@RestController
@EnableWebMvc
public class GlobalExceptionHandler {
    private static Logger LOGGER= Logger.getLogger(GlobalExceptionHandler.class);

    private String realmName = "Authentication token";

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public Result exceptionNoauth(HttpServletRequest request,
                                  HttpServletResponse response, AccessDeniedException e){
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + this.realmName + "\"");
        return new Result(ErrorCode.Unauthorized);
    }

    //其他未处理的异常
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(Exception e){
        LOGGER.warn(e.toString());

        ModelAndView mv = new ModelAndView();
        mv.setView(new RedirectView("/500.html"));

        return mv;
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ModelAndView exceptionTemplate(FileNotFoundException e) {
        LOGGER.warn(e.getMessage());
        ModelAndView mv = new ModelAndView();
        mv.setView(new RedirectView("/404.html"));

        return mv;
    }
}
