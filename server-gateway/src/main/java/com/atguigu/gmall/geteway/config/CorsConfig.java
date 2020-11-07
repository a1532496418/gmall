package com.atguigu.gmall.geteway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;


@Configuration// CorsConfig.xml
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter(){
        //配置跨域
        //声明一个对象CorsConfiguration
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");//允许访问的网络
        corsConfiguration.addAllowedMethod("*"); // 设置请求方法 * 表示任意
        corsConfiguration.addAllowedHeader("*"); // 所有请求头信息 * 表示任意
        corsConfiguration.setAllowCredentials(true);//是否从服务器获取cookie

        //  CorsConfigurationSource 这个是接口，接口不能new !
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);


        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }
}
