package com.youotech.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Objects;

/**
 * @Author: HanJiafeng
 * @Date: 2018-1-9 11:05:05
 * @Desc: 数据库操作类
 */
public class Operator {

    private final static Log LOGGER = LogFactory.getLog(Operator.class);

    /**
     * 查询白名单列表
     * @return 以英文句号.隔开的白名单
     */
    public static String query_WhiteList(){

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        StringBuilder middleSB = new StringBuilder();
        String whiteTypes = "error";
        try {
            connection = DataSourceConfig.DATA_SOURCE.getConnection();//获取连接
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT SD_TYPE FROM se_device");
            while (resultSet.next()){
                middleSB.append(resultSet.getString(1)).append(".");
            }
            whiteTypes = middleSB.toString();
        }catch (SQLException e){
            LOGGER.error("数据库连接获取失败:" + e.getMessage());
            e.printStackTrace();
        }catch (Exception e){
            LOGGER.error("获取白名单失败:" + e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                Objects.requireNonNull(resultSet).close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                Objects.requireNonNull(statement).close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                Objects.requireNonNull(connection).close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return whiteTypes;
    }

    /**
     * 插入终端信息和非法USB设备类型
     * @param ip IP
     * @param mac MAC
     * @param userName 用户名
     * @param hostName 主机名
     * @param usbType usb类型
     */
    public static void insert_DeviceInfo(String ip,String mac,String userName,String hostName,String usbType){

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DataSourceConfig.DATA_SOURCE.getConnection();//获取连接
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO se_rules(SD_TYPE, SR_TYPE, ID_MAC, ID_USRNAME, ID_HOSTNAME, SR_DATE)" +
                    "VALUE ('" + usbType + "','" + ip + "','" + mac + "','" + userName + "','" + hostName + "','" +
                    new Date(System.currentTimeMillis()) + "')");
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                Objects.requireNonNull(statement).close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                Objects.requireNonNull(connection).close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
