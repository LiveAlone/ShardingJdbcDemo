package org.yqj.sj.demo.util;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;

/**
 * @author yaoqijun on 2017-12-05.
 * 构建 Druid Datasource util
 */
public class DataSourceUtil {

    private static final String URL_PREFIX = "jdbc:mysql://127.0.0.1:3306/";

    private static final String USER_NAME = "root";

    private static final String PASSWORD = "anywhere";

    public static DataSource createDataSource(final String dataSourceName) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(URL_PREFIX + dataSourceName + "?characterEncoding=UTF-8");
        dataSource.setUsername(USER_NAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(50);
        dataSource.setMaxWait(30000);
        dataSource.setInitialSize(1);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(280); // sec
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000); //
        dataSource.setValidationQuery("SELECT 1 FROM DUAL");
        dataSource.setTestOnBorrow(true);
        return dataSource;
    }
}
