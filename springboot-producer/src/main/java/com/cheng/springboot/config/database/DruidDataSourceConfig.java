package com.cheng.springboot.config.database;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author cheng
 *         2018/11/6 23:31
 */
@Configuration
@EnableTransactionManagement
public class DruidDataSourceConfig {

    private static Logger logger = LoggerFactory.getLogger(DruidDataSourceConfig.class);

    public static String DRIVER_CLASSNAME;

    @Autowired
    private DruidDataSourceSettings druidSettings;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigure() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource() throws SQLException {

        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setDriverClassName(druidSettings.getDriverClassName());
        DRIVER_CLASSNAME = druidSettings.getDriverClassName();
        dataSource.setUrl(druidSettings.getUrl());
        dataSource.setUsername(druidSettings.getUsername());
        dataSource.setPassword(druidSettings.getPassword());
        dataSource.setInitialSize(druidSettings.getInitialSize());
        dataSource.setMinIdle(druidSettings.getMinIdle());
        dataSource.setMaxActive(druidSettings.getMaxActive());
        dataSource.setTimeBetweenEvictionRunsMillis(druidSettings.getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(druidSettings.getMinEvictableIdleTimeMillis());
        dataSource.setValidationQuery(druidSettings.getValidationQuery());
        dataSource.setTestWhileIdle(druidSettings.isTestWhileIdle());
        dataSource.setTestOnBorrow(druidSettings.isTestOnBorrow());
        dataSource.setTestOnReturn(druidSettings.isTestOnReturn());
        dataSource.setPoolPreparedStatements(druidSettings.isPoolPreparedStatements());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(
                druidSettings.getMaxPoolPreparedStatementPerConnectionSize());
        dataSource.setFilters(druidSettings.getFilters());
        dataSource.setConnectionProperties(druidSettings.getConnectionProperties());

        logger.info(" druid datasource config : {} ", dataSource);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {

        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(dataSource());
        return txManager;
    }
}

