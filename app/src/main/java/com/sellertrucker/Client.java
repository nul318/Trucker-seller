package com.sellertrucker;

import android.app.Notification;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by seongwonlee on 2017. 7. 3..
 */

public class Client {
    private SocketChannel socketChannel;
    private Charset charset;

    public Client(String ip, int port) throws JSONException {

        this.socketChannel = null;
        charset = Charset.forName("UTF-8");
        Log.e("Status", "서버 접속 시도");
        connection(ip, port);

//        JSONObject json = new JSONObject();
//        json.put("code", "98");
//        String id = json.toString();
//        System.out.println("Sending Data : " + id);
//        sendMessage(id);
    }

    private void sendMessage(String str) {
        try {
            charset = Charset.forName("UTF-8");
            ByteBuffer byteBuffer = charset.encode(str);
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
        }
    }

    private void connection(String ip, int port) {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(true);
            socketChannel.connect(new InetSocketAddress(ip, port));
            receive();
            Log.e("Status", "서버 접속");
        } catch (IOException e) {
            Log.e("Status", "서버 접속 불가");
        }
    }

    private void receive() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
                        socketChannel.read(byteBuffer);
                        byteBuffer.flip();
                        String msg = charset.decode(byteBuffer).toString();
//                        System.out.println("Receive Data : " + msg);
//                        if(charset !=null){
//                            msg = charset.decode(byteBuffer).toString();
//                        }

                        if(isJSONValid(msg)) {
                            JSONObject str = new JSONObject(msg);
                            Log.e("Response", msg);
                            if (str.get("code").equals("02")) {


                                SplashActivity.builder = (NotificationCompat.Builder) new NotificationCompat.Builder(SplashActivity.activity)
                                        .setSmallIcon(R.drawable.logo)
                                        .setContentTitle("요청이 왔습니다")
                                        .setContentText(str.getString("message"))
                                        .setSmallIcon(R.drawable.logo)
                                        .setAutoCancel(true)
                                        .setWhen(System.currentTimeMillis())
                                        .setDefaults(Notification.DEFAULT_ALL);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    SplashActivity.builder.setCategory(Notification.CATEGORY_MESSAGE)
                                            .setPriority(Notification.PRIORITY_HIGH)
                                            .setVisibility(Notification.VISIBILITY_PUBLIC);
                                }

                                SplashActivity.mNotificationManager.notify(1, SplashActivity.builder.build());

                            }
                        }
                    } catch (IOException e) {
                        System.out.println(e);
                        close();
                        System.exit(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    private void close() {
        if (!socketChannel.isConnected()) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                System.out.println("Client Close Error" + e);
            }
        }
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}