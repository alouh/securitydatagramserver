package com.youotech.net;

import com.youotech.util.PropertyUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Objects;

/**
 * @Author: HanJiafeng
 * @Date: 13:22 2018/1/9
 * @Desc: UDP服务器.接收客户端发来的报文,分析并给与回应
 */
public class Server implements Runnable {

    private final static Log LOGGER = LogFactory.getLog(Server.class);

    private boolean isRunning = true;

    @Override
    public void run() {

        int port = PropertyUtil.getIntProperty("server.port");
        LOGGER.info("加载服务器端口:" + port);

        DatagramSocket datagramSocket = null;//UDP套接字对象

        byte[] recBuf = new byte[1024];//接受字符数组

        try {
            datagramSocket = new DatagramSocket(port);
            LOGGER.info("开启服务,端口:" + port);

            while (isRunning){
                try {
                    DatagramPacket datagramPacket = new DatagramPacket(recBuf, recBuf.length);//数据报包对象
                    datagramSocket.receive(datagramPacket);//接收数据报包
                    LOGGER.info("收到来自:" + datagramPacket.getAddress() + "的数据报包");
                    new Thread(new PacketProcessor(datagramPacket)).start();//启动数据报包处理器
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
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
     * 关闭服务器
     */
    public void close(){
        isRunning = false;
    }
}
