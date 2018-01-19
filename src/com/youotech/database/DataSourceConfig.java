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

    //WEB数据源，插入的数据供WEB服务器调用
    public final static BasicDataSource WEB_DATA_SOURCE = new BasicDataSource();

    //短信平台数据源，插入短信平台数据库用户报警
    public final static BasicDataSource SMP_DATA_SOURCE = new BasicDataSource();
    /**
     * 初始化BasicDataSource数据源
     */
    public static void init(){

        LOGGER.info("初始化WEB数据源...");
        WEB_DATA_SOURCE.setDriverClassName(PropertyUtil.getProperty("webJdbc.driverClassName"));
        WEB_DATA_SOURCE.setUsername(PropertyUtil.getProperty("webJdbc.username"));
        WEB_DATA_SOURCE.setPassword(PropertyUtil.getProperty("webJdbc.password"));
        WEB_DATA_SOURCE.setUrl(PropertyUtil.getProperty("webJdbc.url"));

        //配置初始化大小,最小空闲连接,最大空闲连接,最大活跃数目
        WEB_DATA_SOURCE.setInitialSize(PropertyUtil.getIntProperty("dbcp.initialSize"));
        WEB_DATA_SOURCE.setMinIdle(PropertyUtil.getIntProperty("dbcp.minIdle"));
        WEB_DATA_SOURCE.setMaxIdle(PropertyUtil.getIntProperty("dbcp.maxIdle"));
        WEB_DATA_SOURCE.setMaxTotal(PropertyUtil.getIntProperty("dbcp.maxTotal"));
        //配置获取连接等待超时时间
        WEB_DATA_SOURCE.setMaxWaitMillis(PropertyUtil.getIntProperty("dbcp.maxWaitMillis"));
        //配置间隔多久才进行一次检测,检测需要关闭的控线连接,单位是毫秒
        WEB_DATA_SOURCE.setTimeBetweenEvictionRunsMillis(PropertyUtil.getIntProperty("dbcp.timeBetweenEvictionRunsMillis"));
        //配置连接池中连接最小生存时间
        WEB_DATA_SOURCE.setMinEvictableIdleTimeMillis(PropertyUtil.getIntProperty("dbcp.minEvictableIdleTimeMillis"));
        //配置确定查询SQL语句
        WEB_DATA_SOURCE.setValidationQuery(PropertyUtil.getProperty("webDbcp.validationQuery"));
        //配置连接空闲验证有效性
        WEB_DATA_SOURCE.setTestWhileIdle(PropertyUtil.getBooleanProperty("dbcp.testWhileIdle"));
        //配置归还验证有效性
        WEB_DATA_SOURCE.setTestOnBorrow(PropertyUtil.getBooleanProperty("dbcp.testOnBorrow"));
        LOGGER.info("WEB数据源初始化成功");

        LOGGER.info("初始化短信平台数据源...");
        SMP_DATA_SOURCE.setDriverClassName(PropertyUtil.getProperty("smsJdbc.driverClassName"));
        SMP_DATA_SOURCE.setUsername(PropertyUtil.getProperty("smsJdbc.username"));
        SMP_DATA_SOURCE.setPassword(PropertyUtil.getProperty("smsJdbc.password"));
        SMP_DATA_SOURCE.setUrl(PropertyUtil.getProperty("smsJdbc.url"));

        //配置初始化大小,最小空闲连接,最大空闲连接,最大活跃数目
        SMP_DATA_SOURCE.setInitialSize(PropertyUtil.getIntProperty("dbcp.initialSize"));
        SMP_DATA_SOURCE.setMinIdle(PropertyUtil.getIntProperty("dbcp.minIdle"));
        SMP_DATA_SOURCE.setMaxIdle(PropertyUtil.getIntProperty("dbcp.maxIdle"));
        SMP_DATA_SOURCE.setMaxTotal(PropertyUtil.getIntProperty("dbcp.maxTotal"));
        //配置获取连接等待超时时间
        SMP_DATA_SOURCE.setMaxWaitMillis(PropertyUtil.getIntProperty("dbcp.maxWaitMillis"));
        //配置间隔多久才进行一次检测,检测需要关闭的控线连接,单位是毫秒
        SMP_DATA_SOURCE.setTimeBetweenEvictionRunsMillis(PropertyUtil.getIntProperty("dbcp.timeBetweenEvictionRunsMillis"));
        //配置连接池中连接最小生存时间
        SMP_DATA_SOURCE.setMinEvictableIdleTimeMillis(PropertyUtil.getIntProperty("dbcp.minEvictableIdleTimeMillis"));
        //配置确定查询SQL语句
        SMP_DATA_SOURCE.setValidationQuery(PropertyUtil.getProperty("smpDbcp.validationQuery"));
        //配置连接空闲验证有效性
        SMP_DATA_SOURCE.setTestWhileIdle(PropertyUtil.getBooleanProperty("dbcp.testWhileIdle"));
        //配置归还验证有效性
        SMP_DATA_SOURCE.setTestOnBorrow(PropertyUtil.getBooleanProperty("dbcp.testOnBorrow"));
        LOGGER.info("短信平台数据源初始化成功");


    }
}
