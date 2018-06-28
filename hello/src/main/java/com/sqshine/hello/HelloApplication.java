 /*
  * Copyright (C), 2015-2018, XXX有限公司
  * FileName: HelloApplication
  * Author:   sqshine
  * Date:     2018/6/27 15:04
  * Description: ${DESCRIPTION}
  * History:
  * <author>          <time>          <version>          <desc>
  * 作者姓名           修改时间           版本号              描述
  */
 package com.sqshine.hello;

 import org.springframework.boot.SpringApplication;
 import org.springframework.boot.autoconfigure.SpringBootApplication;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PathVariable;
 import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.bind.annotation.RestController;

 /**
  * 〈一句话功能简述〉<br>
  * 〈${DESCRIPTION}〉
  *
  * @author sqshine
  * @create 2018/6/27
  * @since 1.0.0
  */
 @RestController
 @SpringBootApplication
 public class HelloApplication {

     @GetMapping("/hello/{name}")
     public String hello(@PathVariable("name") String name) {
         return "HELLO- " + name;
     }

     @GetMapping("/service/hello")
     public String hello2(@RequestParam("name") String name) {
         return "HELLO:" + name;
     }

     public static void main(String[] args) {
         SpringApplication.run(HelloApplication.class, args);
     }
 }
