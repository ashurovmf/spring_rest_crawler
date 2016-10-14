package com.gft.backend.configs;

import com.gft.backend.dao.CustomerOrderDAO;
import com.gft.backend.dao.EBayCategoryDAO;
import com.gft.backend.utils.EBayContext;
import com.gft.backend.utils.EBayCredential;
import com.gft.backend.utils.FacebookContext;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

/**
 * Created by miav on 2016-08-18.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({"com.gft.backend"})
@PropertySource("classpath:appconfig.properties")
public class SpringRootConfig {

    @Autowired
    Environment env;

    @Bean
    public EBayContext eBayContext() {
        EBayContext context = new EBayContext();
        EBayCredential credential = new EBayCredential();
        credential.setAppId(env.getProperty("ebay.appid"));
        credential.setDevId(env.getProperty("ebay.devid"));
        credential.setCertId(env.getProperty("ebay.certid"));
        credential.seteBayToken("AgAAAA**AQAAAA**aAAAAA**7UXuVw**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFk4GjDZeGowmdj6x9nY+seQ**ZPMDAA**AAMAAA**FrOucqbvbqWsZ3vvfxTmtix0nOD5rCOokO77+5qUpizbn9vsoAFcmaCJjk40y2LbeCbIal/bYsZzJPNOk/c4+PSfmiZ+y11ZfJvMOh7ASjoCqryv14o0vgwHqe/vhXTFsYkjEYuBOLpepEEUnIyB4pe1v+KZag48s1PZpWtZytPSR3N/4VjDouB3TCtd9vtSjdf4GDY+2Iz0IJIoTBtDv5w7MJDk1/RHjjScuPJNFvGD5LVkc8sxcGO0J086wzJf9FMVoxmYCm+UWEbtHN+yr0gfjLPIoX0OTERKEbIggMjKeWiSjNaZU15UR/uYOVOdy+P+Gr+4vV0Q9vJeml09zOMz1glP3UwHYrUUHtRn3fDmmnnXqOMg96g1rhtjgF9nBwi+c4k6K4gbXxWDqL/byAPeummRPEjKweLmnl4LHsrvJMzCPF56CB0aQlZ99kivk0f471DrLV6UBxOrgfeKjl6+EK9ABrcx3BWtJ0cJ5Ma6Etjvy3xpPR2nWrHhHfWQAFq/5lyAizzdssdP9NJocEiZtcb8ZsqtORZ0sNg7y5sr4oficllCFpH6VrC7nOX62wJohooG5F3AoPmKkShwy1LaW46d0pP4P1CXQHfHskwQRBx+n6UZtbgkqvOn0Un5w/6Gt4MQXfmUyRnDqEn1w+UU3gOmG7wt4f+T6TBMAR9Tv45oHWA1Bue9XqE7fGWuSjv5pP9IAzsVUSYBZ9aJsf+VkHvE5aqAbSUvv4+6OISRRqV7m71R7NSxZMcSKKeB");
        context.setApiCredential(credential);
        context.setApiServerUrl(null);
        context.setSite(Integer.parseInt(env.getProperty("ebay.site")));
        context.setTimeout(Integer.parseInt(env.getProperty("ebay.timeout")));
        return context;
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClients.createDefault();
    }

    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setSocketTimeout(Integer.parseInt(env.getProperty("ebay.timeout")))
                .setConnectTimeout(Integer.parseInt(env.getProperty("ebay.timeout")))
                .build();
    }

    @Bean(name = "dataSource")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(env.getProperty("db.driver"));
        driverManagerDataSource.setUrl(env.getProperty("db.url"));
        driverManagerDataSource.setUsername(env.getProperty("db.username"));
        driverManagerDataSource.setPassword(env.getProperty("db.password"));
        return driverManagerDataSource;
    }

    @Bean
    public FacebookContext getFacebookContext(){
        FacebookContext config = new FacebookContext();
        config.setAppId(env.getProperty("spring.social.facebook.appId"));
        config.setSecretId(env.getProperty("spring.social.facebook.appSecret"));
        return config;
    }

    @Bean
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));

        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.connection.driver_class", env.getProperty("hibernate.connection.driver_class"));
        properties.put("hibernate.connection.url", env.getProperty("hibernate.connection.url"));
        properties.put("hibernate.connection.username", env.getProperty("hibernate.connection.username"));
        properties.put("hibernate.connection.password", env.getProperty("hibernate.connection.password"));

        properties.put("hibernate.cache.use_second_level_cache", "false");
        properties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
        properties.put("hibernate.cache.provider_class", "org.hibernate.cache.EhCacheProvider");
        properties.put("hibernate.cache.use_query_cache", "true");

        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        return properties;
    }

    @Bean
    public HibernateJpaVendorAdapter getJpaVendorAdapter(){
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setGenerateDdl(true);
        return jpaVendorAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setJpaDialect(new HibernateJpaDialect());
        entityManagerFactoryBean.setJpaVendorAdapter(getJpaVendorAdapter());
        entityManagerFactoryBean.setJpaProperties(hibernateProperties());
        entityManagerFactoryBean.setPackagesToScan("com.gft.backend");
        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager getTransactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager(emf);
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public CustomerOrderDAO getCustomerOrderDAO(){
        return new CustomerOrderDAO();
    }

    @Bean
    public EBayCategoryDAO getCategoryDAO() { return new EBayCategoryDAO(); }

}
