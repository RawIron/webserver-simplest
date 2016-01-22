/*
 *
 */

package server;
 
import java.io.*;
import java.net.*;
import java.util.*;

import server.WorkerPool;


class SocketServer {

    Config settings = null;
    WorkerPool workerPool = null;
    boolean stopped = false;

    public SocketServer(WorkerPool workerPool, Config config) {
        this.workerPool = workerPool;
        this.settings = config;
    }

    public void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(settings.port);
        while (!isStopped()) {
            Socket serveThisSocket = serverSocket.accept();
            Worker worker = workerPool.hireWorker();
            worker.youGotWorkWith(serveThisSocket);
        }
    }

    public void stop() {
        stopped = true;
    }
    protected boolean isStopped() {
        return stopped;
    }
}

