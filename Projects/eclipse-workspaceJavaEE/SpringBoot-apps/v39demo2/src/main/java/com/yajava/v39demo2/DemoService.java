package com.yajava.v39demo2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DemoService {

    @Autowired
    private ApplicationContext applicationContext;

    public String textReverse(String text) {
        StringProcessor myBean = (StringProcessor)applicationContext.getBean("stringService");
        return myBean.reverse(text);
    }
}
