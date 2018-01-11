package com.youotech.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

    private final static Log LOGGER = LogFactory.getLog(PropertyUtil.class);
    private static Properties props;
    static{
        loadProps();
    }

    synchronized static private void loadProps(){
        LOGGER.info("开始加载properties文件内容.......");
        props = new Properties();
        InputStream in = null;
        try {
            in = PropertyUtil.class.getResourceAsStream("/com/youotech/resources/config.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            LOGGER.error("jdbc.properties文件未找到");
        } catch (IOException e) {
            LOGGER.error("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                LOGGER.error("jdbc.properties文件流关闭出现异常");
            }
        }
        LOGGER.info("加载properties文件内容完成...........");
        LOGGER.info("properties文件内容：" + props);
    }

    public static String getProperty(String key){
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }

    //获取int型参数
    public static int getIntProperty(String key){
        if (null == props){
            loadProps();
        }
        String value = props.getProperty(key);
        int a = -1;
        try {
            a = Integer.valueOf(value);
        }catch (Exception e){
            LOGGER.info("配置文件类型错误:(" + key + "," + value + ")");
        }

        return a;
    }
    //获取long型参数
    public static long getLongProperty(String key){
        if (null == props){
            loadProps();
        }
        String value = props.getProperty(key);
        long a = -1;
        try {
            a = Long.valueOf(value);
        }catch (Exception e){
            LOGGER.info("配置文件类型错误:(" + key + "," + value + ")");
        }

        return a;
    }
    //获取boolean型参数
    public static boolean getBooleanProperty(String key){
        if (null == props){
            loadProps();
        }
        String value = props.getProperty(key);
        try {
            if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")){
                return value.equalsIgnoreCase("true");
            }else {
                throw new Exception();
            }
        }catch (Exception e){
            LOGGER.info("配置文件类型错误:(" + key + "," + value + ")");
        }

        return false;
    }
}
