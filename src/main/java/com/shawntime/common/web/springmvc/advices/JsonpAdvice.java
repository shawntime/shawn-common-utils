package com.shawntime.common.web.springmvc.advices;

import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Created by zhouxiaoming on 2015/9/17.
 */
@ControllerAdvice
public class JsonpAdvice extends FastJsonJsonpResponseBodyAdvice {
    public JsonpAdvice() {
        super("callback");
    }
}