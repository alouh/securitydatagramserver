package com.youotech.database;

import com.youotech.util.PropertyUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Author: HanJiafeng
 * @Date: 11:19 2018/1/9
 * @Desc:
 */
public class DataSourceConfig {

    private final static Log LOGGER = LogFactory.getLog(DataSourceConfig.class);

    public final static BasicDataSource DATA_SOURCE = new BasicDataSource();
    /**
     * 初始化BasicDataSource数据源
     */
    public static void init(){

        LOGGER.info("初始化BasicDataSource数据源...");
        DATA_SOURCE.setDriverClassName(PropertyUtil.getProperty("jdbc.driverClassName"));
        DATA_SOURCE.setUsername(PropertyUtil.getProperty("jdbc.username"));
        DATA_SOURCE.setPassword(PropertyUtil.getProperty("jdbc.password"));
        DATA_SOURCE.setUrl(PropertyUtil.getProperty("jdbc.url"));

        //配置初始化大小,最小空闲连接,最大空闲连接,最大活跃数目
        DATA_SOURCE.setInitialSize(PropertyUtil.getIntProperty("dbcp.initialSize"));
        DATA_SOURCE.setMinIdle(PropertyUtil.getIntProperty("dbcp.minIdle"));
        DATA_SOURCE.setMaxIdle(PropertyUtil.getIntProperty("dbcp.maxIdle"));
        DATA_SOURCE.setMaxTotal(PropertyUtil.getIntProperty("dbcp.maxTotal"));
        //配置获取连接等待超时时间
        DATA_SOURCE.setMaxWaitMillis(PropertyUtil.getIntProperty("dbcp.maxWaitMillis"));
        //配置间隔多久才进行一次检测,检测需要关闭的控线连接,单位是毫秒
        DATA_SOURCE.setTimeBetweenEvictionRunsMillis(PropertyUtil.getIntProperty("dbcp.timeBetweenEvictionRunsMillis"));
        //配置连接池中连接最小生存时间
        DATA_SOURCE.setMinEvictableIdleTimeMillis(PropertyUtil.getIntProperty("dbcp.minEvictableIdleTimeMillis"));
        //配置确定查询SQL语句
        DATA_SOURCE.setValidationQuery(PropertyUtil.getProperty("dbcp.validationQuery"));
        //配置连接空闲验证有效性
        DATA_SOURCE.setTestWhileIdle(PropertyUtil.getBooleanProperty("dbcp.testWhileIdle"));
        //配置归还验证有效性
        DATA_SOURCE.setTestOnBorrow(PropertyUtil.getBooleanProperty("dbcp.testOnBorrow"));
        LOGGER.info("初始化BasicDataSource数据源完成");
    }
}
