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
     * 查询黑名单列表
     * @return 以英文句号.隔开的黑名单
     */
    public static String query_WhiteList(Connection connection,String osName){

        Statement statement = null;
        ResultSet resultSet = null;
        StringBuilder middleSB = new StringBuilder();
        String whiteTypes = "error";
        String sqlStr = "SELECT TL_PATH FROM usb_typelist WHERE TL_ALLOW = '禁用' AND TL_OSNAME = '" + osName + "'";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlStr);
            while (resultSet.next()){
                middleSB.append(resultSet.getString(1)).append(".");
            }
            whiteTypes = middleSB.toString();
        }catch (Exception e){
            LOGGER.error("查询黑名单列表失败:" + e.getMessage() + "\nSQL:" + sqlStr);
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
        }

        return whiteTypes;
    }

    /**
     * 查询注册表位置对应的USB设备类型
     * @param path 注册表路径
     * @return USB设备类型
     */
    public static String query_UsbType(Connection connection,String path){

        Statement statement = null;
        ResultSet resultSet = null;
        String usbType = "error";
        String sqlStr = String.format("SELECT TL_TYPE FROM usb_typelist WHERE TL_PATH = '%s'",path).replace("\\","\\\\");//将单斜杠替换为双斜杠

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlStr);
            while (resultSet.next()){
                usbType = resultSet.getString(1);
            }
        }catch (Exception e){
            LOGGER.error("查询USB设备类型失败:" + e.getMessage() + "\nSQL:" + sqlStr);
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
        }

        return usbType;
    }

    /**
     * 插入终端信息和非法USB设备类型
     * @param ip IP
     * @param mac MAC
     * @param userName 用户名
     * @param hostName 主机名
     * @param usbType usb类型
     * @return 成功插入数量,-1:插入异常,0:没插入一条,other:插入other条
     */
    public static int insert_DeviceInfo(Connection connection,String ip,String mac,String userName,String hostName,String usbType){

        int insertCode = -1;

        Statement statement = null;
        String sqlStr = String.format("INSERT INTO usb_illegaldevice(ID_TYPE, ID_IP, ID_MAC, ID_USRNAME, ID_HOSTNAME, ID_DATE)" +
                "VALUE ('%s','%s','%s','%s','%s','%s')",usbType,ip,mac,userName,hostName,new Date(System.currentTimeMillis()));
        try {
            statement = connection.createStatement();
            insertCode = statement.executeUpdate(sqlStr);
        }catch (Exception e){
            LOGGER.error("插入终端信息和非法USB设备类型失败:" + e.getMessage() + "\nSQL:" + sqlStr);
            e.printStackTrace();
        }finally {
            try {
                Objects.requireNonNull(statement).close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return insertCode;
    }

    /**
     * 向短信平台表插入报警信息和手机号
     * @param webConnection 查询手机号的数据库连接
     * @param smpConnection 短信平台数据库连接
     * @param msg 通知内容
     */
    public static void insertWarnMsg(Connection webConnection,Connection smpConnection,String msg){

        String querySqlStr = "SELECT PN_NUMBER FROM usb_phonenumber";
        Statement webStatement = null;
        Statement smpStatement = null;
        ResultSet resultSet = null;
        try {
            webStatement = webConnection.createStatement();
            smpStatement = smpConnection.createStatement();
            resultSet = webStatement.executeQuery(querySqlStr);
            while (resultSet.next()){
                try {
                    String phoneNumber = resultSet.getString(1);
                    smpStatement.executeUpdate("INSERT INTO OutBox(Mbno,Msg,SendTime)VALUE('" + phoneNumber + "','"
                            + msg + "','" + new Timestamp(System.currentTimeMillis()) + ")')");//每获取一个手机号就插入一条数据
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                Objects.requireNonNull(resultSet).close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                Objects.requireNonNull(smpStatement).close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                Objects.requireNonNull(webStatement).close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
