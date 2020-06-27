package com.xiao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;


@Configuration
@EnableResourceServer
public class OauthConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    /**
     * 指定Token的持久化策略
     * InMemoryTokenStore 表示将 token 存储在内存
     * Redis 表示将 token 存储在Redis
     * JdbcTokenStore 表示将 token 存储在数据库
     * @return
     */
    @Bean
    public TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /**
     * 指定当前资源的id和存储方案
     * @param resource
     * @throws Exception
     */
    public void configure(ResourceServerSecurityConfigurer resource) throws Exception {
        resource.resourceId("source_api").tokenStore(jdbcTokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //指定不同请求方式访问资源所需的权限
            .antMatchers(HttpMethod.GET, "/**").access("#oauth2.hasScope('read')")
            .antMatchers(HttpMethod.POST, "/**").access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.PATCH, "/**").access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.PUT, "/**").access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.DELETE, "/**").access("#oauth2.hasScope('write')")
                .and()
            .headers().addHeaderWriter((request, response) -> {
            response.addHeader("Access-Control-Allow_Origin", "*");//允许跨域
            //如果是跨域的预检请求，则原封不动向下传达请求头信息
            if(request.getMethod().equals("OPTIONS")) {
                response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Allow-Methods"));
                response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Allow-Headers"));
            }

        });
    }
}
