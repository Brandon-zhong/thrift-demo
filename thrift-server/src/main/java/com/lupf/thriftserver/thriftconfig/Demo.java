package com.lupf.thriftserver.thriftconfig;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author brandon
 * create on 2020-07-08
 * desc:
 */
@RestController
@RequestMapping("/demo")
public class Demo {

    @GetMapping("/hello")
    public String demo() {
        return "this is demo";
    }

}
