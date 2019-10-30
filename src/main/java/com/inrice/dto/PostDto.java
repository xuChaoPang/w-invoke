package com.inrice.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 前端请求 requestBody
 */
@Data
public class PostDto {
    private String serviceCode;
    private JSONObject inParam;
}
