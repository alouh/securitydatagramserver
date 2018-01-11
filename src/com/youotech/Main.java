package com.youotech;

import com.youotech.database.DataSourceConfig;
import com.youotech.net.Server;

public class Main {

    public static void main(String[] args){

        DataSourceConfig.init();//初始化连接池

        Server server = new Server();//初始化服务器
        Thread thread = new Thread(server);//构造线程对象
        thread.start();//运行线程
    }
}
