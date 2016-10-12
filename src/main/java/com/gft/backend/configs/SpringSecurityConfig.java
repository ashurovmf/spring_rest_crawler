package com.gft.backend.configs;

import com.gft.backend.services.AuthenticationService;
import com.gft.backend.utils.CustomTokenEnhancer;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

import javax.sql.DataSource;

/**
 * Created by miav on 2016-10-03.
 */
@Configuration
@ComponentScan({"com.gft.backend"})
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{

    private static final Logger logger = Logger.getLogger(SpringSecurityConfig.class);

    @Configuration
    @EnableWebSecurity
    @EnableHazelcastHttpSession
    @Order(1)
    public class MainSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        DataSource dataSource;

        @Autowired
        AuthenticationService authenticationService;

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(authenticationService).passwordEncoder(new ShaPasswordEncoder());
        }

        @Bean
        public HazelcastInstance embeddedHazelcast() {
            Config hazelcastConfig = new Config();
            return Hazelcast.newHazelcastInstance(hazelcastConfig);
        }

        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    // allow everybody access to registration rest
                    .antMatchers("/","/login**","/register**").permitAll()
                    .anyRequest().authenticated();
        }
    }

    /**
     * A Resource Server (Our rest-services)
     */
    @Configuration
    @EnableResourceServer
    @Order(2)
    protected static class ResourceServerConfig extends ResourceServerConfigurerAdapter {
        @Autowired
        DataSource dataSource;
        @Bean
        public JdbcTokenStore tokenStore() {
            return new JdbcTokenStore(dataSource);
        }
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources
                    .tokenStore(tokenStore());
        }
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    // allow everybody access to registration rest
                    .antMatchers("/","/login**","/register**").permitAll()
                    .anyRequest().authenticated();
        }
    }

    /**
     * Configure the OAuth 2.0 Authorization Server mechanism
     */
    @Configuration
    @EnableAuthorizationServer
    @Order(3)
    protected static class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
        @Autowired
        DataSource dataSource;
        @Bean
        public JdbcTokenStore tokenStore() {
            return new JdbcTokenStore(dataSource);
        }
        @Bean
        protected AuthorizationCodeServices authorizationCodeServices() {
            return new JdbcAuthorizationCodeServices(dataSource);
        }

        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }

        @Autowired
        @Qualifier("authenticationManagerBean")
        AuthenticationManager authenticationManager;

        @Autowired
        AuthenticationService authenticationService;

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .authorizationCodeServices(authorizationCodeServices())
                    .tokenStore(tokenStore())
                    .authenticationManager(authenticationManager)
                    .userDetailsService(authenticationService)
                    .tokenEnhancer(tokenEnhancer);
        }
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients
                    .jdbc(dataSource)
                    .passwordEncoder(passwordEncoder());
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security.allowFormAuthenticationForClients();
        }

        @Autowired
        CustomTokenEnhancer tokenEnhancer;

        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices tokenServices = new DefaultTokenServices();
            tokenServices.setSupportRefreshToken(true);
            tokenServices.setTokenStore(tokenStore());
            tokenServices.setTokenEnhancer(tokenEnhancer);
            return tokenServices;
        }
    }
}
