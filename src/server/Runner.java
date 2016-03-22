package server;
 
import java.io.*;
import java.net.*;
import java.util.*;

import server.Config;
import server.Logger;
import server.SocketServer;
import server.HttpRequestStreamWorker;


class Runner {
    public static void main(String[] args) throws Exception {
        Config commandLine = commandLineOptionsIntoConfig(args);

        LoggerFactory loggerFactory = new LoggerFactory();
        Logger logger = loggerFactory.build(LoggerModels.DEFAULT);
        ConfigDefaults defaults = new ConfigDefaults();

        Config config = new Config(defaults, logger);
        config.load();
        config.list();

        WorkerPool pool = new HttpRequestStreamWorkerPool(config);
        pool.init();

        SocketServer webServer = new SocketServer(pool, config);
        webServer.start();
    }

    protected static Config commandLineOptionsIntoConfig(String[] args) {
        Config commandLineOptions = null;
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        return commandLineOptions;
    }
}

