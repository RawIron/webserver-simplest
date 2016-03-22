/*
 * 
 */

package server;
 
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import static server.HttpConstants.*;
import server.WorkerPool;
import server.ReverseProxyServer;


class HttpRequestServletWorkerPool extends WorkerPool {

    public HttpRequestServletWorkerPool(Config settings) {
        super(settings);
    }

    protected Worker createWorker(WorkerPool pool, Config settings) {
        Worker w = new HttpRequestServletWorker(pool, settings);
        return w;
    }
}


class HttpRequestServletWorker extends Worker {

    public HttpRequestServletWorker(WorkerPool coworkers, Config config) {
        super(coworkers, config);
    }


    protected void handleClient() throws IOException {

        currentClient.setSoTimeout(settings.timeout);
        currentClient.setTcpNoDelay(true);

        int httpMethod = HTTP_BAD_METHOD;
        File targ = null;
        try {
            BufferedReader is = new BufferedReader(
                                new InputStreamReader(currentClient.getInputStream()));
            String line = is.readLine();

            httpMethod = extractMethod(line);
            if (httpMethod == HTTP_GET) {
                String fname = extractFilename(line);
                targ = openFile(fname);
            }
        } catch (Exception e) {
            /* sent error to client */
            httpMethod = HTTP_HEAD;
        }

        ReverseProxyServer proxy = new ReverseProxyServer(settings.logger);
        proxy.deliverContent(currentClient, httpMethod, targ);

        return;
    }

    protected int extractMethod(String line) {
        String[] words = line.split(" ", 0);

        int method = HTTP_BAD_METHOD;
        if (words[0].equals("GET")) {
            method = HTTP_GET;
        } else if (words[0].equals("HEAD")) {
            method = HTTP_HEAD;
        }
        return method;
    }

    protected String extractFilename(String line) {
        /* extract the file path:
         * GET /foo/bar.html HTTP/1.0
         */
        String[] words = line.split(" ", 0);

        String fname = words[1].replace('/', File.separatorChar);
        if (fname.startsWith(File.separator)) {
            fname = fname.substring(1);
        }

        return fname;
    }

    protected File openFile(String fname) {
        File targ = new File(settings.root, fname);
        if (targ.isDirectory()) {
            File ind = new File(targ, "index.html");
            if (ind.exists()) {
                targ = ind;
            }
        }
        return targ;
    }
}

