package com.xiao.config;

import com.xiao.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.crypto.interfaces.PBEKey;
import javax.sql.DataSource;
import java.security.PublicKey;

@Configuration
@EnableAuthorizationServer
public class oauthServerConfig extends AuthorizationServerConfigurerAdapter {
    //数据库对象
    @Autowired
    private DataSource dataSource;

    //认证业务对象
    @Autowired
    private SysUserService userService;

    //授权模式专用对象
    @Autowired
    private AuthenticationManager authenticationManager;

    //客户端信息来源
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    //token保存策略
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    //授权信息保存策略
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    //授权码模式数据来源
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    //指定客户端信息的数据来源
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService());
    }

    //验证token的策略
    public void configure(AuthorizationServerSecurityConfigurer securityConfigurer) {
        securityConfigurer.allowFormAuthenticationForClients();
        securityConfigurer.checkTokenAccess("isAuthenticated()");
    }

    //oauth2的主配置
    public void configure(AuthorizationServerEndpointsConfigurer endpointsConfigurer) {
        endpointsConfigurer.approvalStore(approvalStore())
                .authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices())
                .tokenStore(tokenStore());
    }

}
