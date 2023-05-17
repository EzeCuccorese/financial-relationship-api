package com.reba.rebatest.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Manejar el error como desees, por ejemplo, mostrar un mensaje de error personalizado
        return "Error occurred";
    }

    public String getErrorPath() {
        return "/error";
    }
}
