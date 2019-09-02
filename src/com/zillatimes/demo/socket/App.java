package com.zillatimes.demo.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * socket demo for send hex-data to client
 * @Author Zilla Chen
 */
public class App {

    private static List<byte[]> datas = new ArrayList<>();
    private static final int PORT = 8899;

    public static void main(String[] args) throws Exception {
        initData();
        start();
    }

    private static void start() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                while (true) {
                    Socket socket = serverSocket.accept();
                    new Thread(new Task(socket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Handler
     */
    static class Task implements Runnable {

        private Socket socket;

        public Task(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                handlerSocket();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void handlerSocket() throws IOException {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            input(inputStream);
            output(outputStream);
        }

        /**
         * deal outputStream
         *
         * @param outputStream
         */
        private void output(final OutputStream outputStream) {
            new Thread(() -> {
                int count = datas.size();
                int i = 0;
                while (true) {
                    try {
                        outputStream.write(datas.get(i++));
                    } catch (IOException e) {

                    }
                    if (i == count) {
                        i = 0;
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        /**
         * deal inputStream
         *
         * @param inputStream
         */
        private void input(InputStream inputStream) {
            byte[] data = new byte[1024];
            new Thread(() -> {
                while (true) {
                    int len;
                    while (true) {
                        try {
                            if (!((len = inputStream.read(data)) != -1)) break;
                            System.out.println(new String(data, 0, len));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * init test data
     *
     * @throws IOException
     */
    private static void initData() throws IOException {
        FileReader fr = new FileReader("data.txt");
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        while ((line = br.readLine()) != null) {
            datas.add(Utils.hexStr2Bytes(line));
        }
    }
}
