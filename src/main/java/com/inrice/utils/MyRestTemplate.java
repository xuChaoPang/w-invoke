package com.inrice.utils;

import com.alibaba.fastjson.JSON;
import com.inrice.config.UriConfig;
import com.inrice.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xurongmao
 * 处理请求转发以及结果集转发
 */
@Component
public class MyRestTemplate {

    public static String CODE_PARAMA_NAME = "serviceCode";

    public static String BODY_PARAMA_NAME = "inParam";

    @Autowired
    private UriConfig uriConfig;

    public void get (String serviceCode) {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        Map<String, String> paramasMap;
        if (request != null) {
            paramasMap = HttpContextUtils.getParameterMap(request);
            paramasMap.remove(CODE_PARAMA_NAME);
        } else {
            paramasMap = new HashMap<>(16);
        }

        String uri = uriConfig.getUriByCode(serviceCode);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(uri);
        for (String key: paramasMap.keySet()) {
            uriComponentsBuilder.queryParam(key, paramasMap.get(key));
        }
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> requestEntity = new HttpEntity<String>(null, getHeads(request));

        ResponseEntity<String> responseEntity = restTemplate.exchange(uriComponentsBuilder.build().encode().toUri(), HttpMethod.GET,
                requestEntity, String.class);
        setResponse(responseEntity);
    }


    public void post (PostDto postDto) {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();

        String serviceCode = postDto.getServiceCode();

        String uri = uriConfig.getUriByCode(serviceCode);
        RestTemplate restTemplate = new RestTemplate();
        String requestBody = "";
        if (postDto.getInParam() != null) {
            requestBody = postDto.getInParam().toJSONString();
        }
        HttpEntity<String> requestEntity = new HttpEntity<String>(requestBody, getHeads(request));


        URI uriL = null;
        try {
            uriL = new URI(uri);
            ResponseEntity<String> responseEntity = restTemplate.exchange(uriL, HttpMethod.POST,
                    requestEntity, String.class);
            setResponse(responseEntity);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将responseEntity的结果写入response
     * 包括head 和 cookie 和 响应体
     * @param responseEntity
     */
    public void setResponse (ResponseEntity<String> responseEntity) {
        HttpServletResponse response = HttpContextUtils.getResponse();


        response.setCharacterEncoding("UTF-8");
        if (response == null || responseEntity == null ) {
            return;
        }
        response.setContentType(responseEntity.getHeaders().getContentType().toString());
        response.setStatus(responseEntity.getStatusCodeValue());
//        HttpHeaders httpHeaders =  responseEntity.getHeaders();
//        if (httpHeaders == null || httpHeaders.size() == 0) {
//            return;
//        }
//        for ( String key : responseEntity.getHeaders().keySet() ) {
//            List<String> valueList = httpHeaders.getValuesAsList(key);
//            for (String value : valueList) {
//                response.addHeader(value, value);
//            }
//        }
        String responseBody = responseEntity.getBody();

        if (responseBody != null) {
            try (Writer writer = response.getWriter()) {
                writer.write(responseBody );
            } catch (IOException e) {

               System.out.println(e.getStackTrace());
            }
        }

    }

    private HttpHeaders getHeads (HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (request == null) {
            return  httpHeaders;
        }
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headName = headerNames.nextElement();
            httpHeaders.add(headName, request.getHeader(headName) );
        }

        return httpHeaders;
    }




}
