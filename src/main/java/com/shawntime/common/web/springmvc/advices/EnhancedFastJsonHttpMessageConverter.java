package com.shawntime.common.web.springmvc.advices;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * Created by Fanbaoyin on 2015/9/15.
 * add DefaultDateFormat Property
 */
public class EnhancedFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {
    public EnhancedFastJsonHttpMessageConverter() {
        super();
    }

    public String getDefaultDateFormat() {
        return JSON.DEFFAULT_DATE_FORMAT;
    }

    public void setDefaultDateFormat(String defaultDateFormat) {
        JSON.DEFFAULT_DATE_FORMAT = defaultDateFormat;
    }
}
