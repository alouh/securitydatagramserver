package com.youotech.net;

import com.youotech.database.Operator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Objects;

/**
 * @Author: HanJiafeng
 * @Date: 14:27 2018/1/9
 * @Desc: 数据报包处理器.分析数据报包,根据里面的标志位给与不同的处理
 */
public class PacketProcessor implements Runnable {
    private final static Log LOGGER = LogFactory.getLog(PacketProcessor.class);

    private DatagramPacket datagramPacket;

    PacketProcessor(DatagramPacket datagramPacket){
        this.datagramPacket = datagramPacket;
    }

    @Override
    public void run() {
        byte[] recBuf = datagramPacket.getData();

        String msg = new String(recBuf);
        msg = msg.substring(0,msg.indexOf("&&&"));//使用"&&&"作为结束符获取控制报文

        SocketAddress clientAdd = datagramPacket.getSocketAddress();//获取远程客户端IP地址 + 端口
        String flagStr = arrayStrToStr(msg, ",", 0);//标志位
        StringBuilder backMsg = new StringBuilder();
        switch (flagStr){
            case "1":
                LOGGER.info("查询白名单");
                backMsg.append("1").append(",");//添加查询白名单标志位和分隔符
                String allowTypes = Operator.query_WhiteList();//查询白名单
                switch (allowTypes) {
                    case "error"://查询白名单失败
                        backMsg.append("2");
                        break;
                    case ""://白名单为空
                        backMsg.append("3");
                        break;
                    default://查询白名单结果
                        backMsg.append("1").append(",").append(allowTypes);
                        break;
                }
                break;
            case "2":
                LOGGER.info("插入终端信息");
                String usbInfoStr = arrayStrToStr(msg,",",1);

                String ipStr = datagramPacket.getAddress().getHostAddress();
                String usbType = arrayStrToStr(usbInfoStr,";",0);
                String macStr = arrayStrToStr(usbInfoStr,";",1);
                String userName = arrayStrToStr(usbInfoStr,";",2);
                String hostName = arrayStrToStr(usbInfoStr,";",3);

                Operator.insert_DeviceInfo(ipStr,macStr,userName,hostName,usbType);//插入终端信息和USB类型

                break;
            default:
                System.out.println("break:" + flagStr);
                break;
        }

        if (flagStr.equals("1") || flagStr.equals("2")) {
            LOGGER.info("预发送:" + backMsg + "\n客户端地址" + clientAdd);
            send(backMsg.toString(), clientAdd);//发送UDP数据报包到客户端
        }
    }

    /**
     * 发送UDP数据报包到指定地址的指定UDP端口
     * @param sendMsg 报文内容
     * @param add 目标地址
     */
    private void send(String sendMsg,SocketAddress add){

        DatagramSocket datagramSocket = null;

        try {
            byte[] sendBuf = sendMsg.getBytes("gbk");
            DatagramPacket packet = new DatagramPacket(sendBuf,sendBuf.length,add);
            datagramSocket = new DatagramSocket();
            datagramSocket.send(packet);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                Objects.requireNonNull(datagramSocket).close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用指定分隔符解析字符串并取出指定索引的字符串,避免因异常引起的服务器退出
     * @param str 原始字符串
     * @param regex 分隔符
     * @param index 索引
     * @return 字符串数组中指定位置的字符串
     */
    private String arrayStrToStr(String str,String regex,int index){

        String[] msgStr = str.split(regex);
        try {
            return msgStr[index];
        }catch (Exception e){
            LOGGER.error("来自客户端:" + datagramPacket.getSocketAddress() + "的报文:" + str + "解析错误");
        }
        return "";
    }
}
