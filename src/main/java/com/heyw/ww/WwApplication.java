package com.heyw.ww;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.heyw.ww.my.Seq;
import com.heyw.ww.my.Snow;
import com.heyw.ww.my.Snowflake2;

@SpringBootApplication
@Slf4j
public class WwApplication {

	public static void main(String[] args) {
		SpringApplication.run(WwApplication.class, args);
		try {
			ServerSocket server = new ServerSocket(5555);
			while (true) {
				Socket client = server.accept(); //等待客户端的连接，如果没有获取连接  ,在此步一直等待
				new Thread(new ServerThread(client)).start(); //为每个客户端连接开启一个线程
			}
		} catch(IOException e) {
            e.printStackTrace();
		}
	}
}

@Slf4j
class ServerThread extends Thread {

    private Socket client;

    public ServerThread(Socket client) {
        this.client = client;
    }

    @SneakyThrows
    @Override
    public void run() {
        // log.info("客户端:" + client.getInetAddress().getLocalHost() + "已连接到服务器");
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        //读取客户端发送来的消息
        String mess = br.readLine();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

        String[] list = mess.split(":");
        if (list.length < 2) {
            bw.write(mess + "\r\n");
            bw.flush();
            return ;
        }
        String name = filter(list[1]);

        Long id = -1l;
        switch (list[0]) {
            case "seq":
            break;
            case "snow":
            break;
            case "snowflake":
                id = Snowflake2.build(name).gen();
            break;
            default:
            break;
        }
        bw.write(id.toString() + "\r\n");
        bw.flush();
    }

    protected static String filter(String name) {
        if (name == null || name == "") {
            return "";
        }
        char[] ctmp = new char[name.length()];

        int i = 0;
        for (char s : name.toCharArray()) {
            if ((s >= '0' && s <= '9') || (s >= 'a' && s <= 'z')) {
                ctmp[i] = s;
                i++;
            }
        }
        return String.valueOf(ctmp).trim();
    }
}
