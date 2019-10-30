package com.inrice.controller;

import com.inrice.dto.PostDto;
import com.inrice.utils.HttpContextUtils;
import com.inrice.utils.MyRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xurongmao
 */
@RestController
@RequestMapping("/services/v1/invoke")
public class InvokeController {

    @Autowired
    private MyRestTemplate myRestTemplate;

    @GetMapping
    public void  get (@RequestParam String serviceCode) {
        myRestTemplate.get(serviceCode);
        return;

    }

    @PostMapping
    public void  post (@RequestBody PostDto postDto) {

        myRestTemplate.post(postDto);
        return;

    }
}
