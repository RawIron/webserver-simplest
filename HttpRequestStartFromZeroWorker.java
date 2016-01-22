/*
 * 
 */

package simpleWebServer;
 
import java.io.*;
import java.net.*;
import java.util.*;

import static simpleWebServer.HttpConstants.*;
import simpleWebServer.WorkerPool;
import simpleWebServer.ReverseProxyServer;


class HttpRequestStartFromZeroWorkerPool extends WorkerPool {

    public HttpRequestStartFromZeroWorkerPool(Config settings) {
        super(settings);
    }

    protected Worker createWorker(WorkerPool pool, Config settings) {
        Worker w = new HttpRequestStartFromZeroWorker(pool, settings);
        return w;
    }
}


class HttpRequestStartFromZeroWorker extends Worker {

    static final int BUFFER_SIZE = 2048;
    static final int EOF = -1;
    static final byte[] EOL = {(byte)'\r', (byte)'\n'};

    byte[] requestBuffer;
    int index;
    int nread;

    public HttpRequestStartFromZeroWorker(WorkerPool coworkers, Config config) {
        super(coworkers, config);
        requestBuffer = new byte[BUFFER_SIZE];
    }


    protected void handleClient() throws IOException {

        currentClient.setSoTimeout(settings.timeout);
        currentClient.setTcpNoDelay(true);

        resetBuffer();

        int httpMethod = HTTP_BAD_METHOD;
        File targ = null;
        try {
            InputStream is = new BufferedInputStream(currentClient.getInputStream());
            int totalBytesRead = readFirstLine(is);

            nread = totalBytesRead;
            index = 0;
            httpMethod = extractMethod(requestBuffer);
            if (httpMethod == HTTP_GET) {
                String fname = extractFilename(requestBuffer);
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


    protected int readFirstLine(InputStream is) throws IOException, Exception {
        int totalBytesRead = 0, bytesRead = 0;
        boolean firstLineComplete = false;

        while(!firstLineComplete) {
            bytesRead = is.read(requestBuffer, totalBytesRead, BUFFER_SIZE - totalBytesRead);
            if (bytesRead == EOF) {
                String message = "reached end of request before line complete";
                throw new Exception();
            }
            firstLineComplete = isLineComplete(totalBytesRead, totalBytesRead + bytesRead);
            totalBytesRead += bytesRead;
            if (totalBytesRead >= BUFFER_SIZE) {
                String message = "Line too long";
                throw new Exception();
            }
        }
        return totalBytesRead;
    }

    protected boolean isLineComplete(int beginSearchAt, int endSearchAt) {
        int newline = (byte)'\n';
        int creturn = (byte)'\r';
        for (int i = beginSearchAt; i < endSearchAt; ++i) {
            if ((requestBuffer[i] == creturn) || (requestBuffer[i] == newline)) {
                return true;
            }
        }
        return false;
    }

    protected void resetBuffer() {
        for (int i = 0; i < BUFFER_SIZE; i++) {
            requestBuffer[i] = 0;
        }
    }

    protected int extractMethod(byte[] buf) {
        int method = HTTP_BAD_METHOD;

        if (buf[0] == (byte)'G' &&
            buf[1] == (byte)'E' &&
            buf[2] == (byte)'T' &&
            buf[3] == (byte)' ') {
            method = HTTP_GET;
            index = 4;
        } else if (buf[0] == (byte)'H' &&
                   buf[1] == (byte)'E' &&
                   buf[2] == (byte)'A' &&
                   buf[3] == (byte)'D' &&
                   buf[4] == (byte)' ') {
            method = HTTP_HEAD;
            index = 5;
        }
        return method;
    }

    protected String extractFilename(byte[] buf) {
        /* find the file name, from:
         * GET /foo/bar.html HTTP/1.0
         * extract "/foo/bar.html"
         */
        int fnameBegin = index;
        int fnameLen = 0;
        for (int i = index; i < nread; i++) {
            if (buf[i] == (byte)' ') {
                fnameLen = i - fnameBegin;
                break;
            }
        }

        String fname = (new String(buf, 0, fnameBegin,
                  fnameLen)).replace('/', File.separatorChar);
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

