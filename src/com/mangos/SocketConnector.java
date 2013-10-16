/* 
 * Copyright (C) 2007-2012 Caledonia Computers <http://www.caledoniacomputers.com>  
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * MODIFIED BY FLINTMAN
 */
package com.mangos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 *
 * @author bannor MODIFIED by Flintman
 */
public class SocketConnector {

    private Socket ourSocket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private boolean boolConnected = false;
    private long lngWait = 30000;
    private String strLineTerm = "\r";
    private String strPrompt = "\r";

    public String connect(String ahost, int aport) throws Exception {
        ourSocket = new Socket(ahost, aport);
        in = new BufferedReader(new InputStreamReader(ourSocket.getInputStream()));
        out = new PrintWriter(ourSocket.getOutputStream(), true);
        boolConnected = true;
        return read(lngWait);
    }

    public void setLineTerm(String text) {
        strLineTerm = text;
    }

    public void setWaitPeriod(long millis) {
        lngWait = millis;
    }

    public long getWaitPeriod() {
        return lngWait;
    }

    public boolean ready() {
        return boolConnected;
    }

    public String send(String text) {
        return send(text, lngWait);
    }

    public String send(String text, long waitfor) {

        // Ensure we are connected before doing any output
        if (!boolConnected) {
            return null;
        }
        String result;
        // Send our text output
        try {
            text += strLineTerm;
            // Lets clear the input buffer prior to sending
            while (in.ready()) {
                in.read();
            }
            out.print(text);
            out.flush();

            // Get any return text
            result = read(lngWait);
            return result;
        } catch (Exception ex) {
        }
        return null;
    }

    private String read(long waitfor) {
        String strResult = "";
        long lngTimeout;

        if (!boolConnected) {
            return null;
        }

        try {
            lngTimeout = System.currentTimeMillis() + waitfor;
            while (!in.ready()) {
                if (strPrompt.length() == 0) {
                    break;
                }
                if (System.currentTimeMillis() >= lngTimeout) {
                    return null;
                }
            }
            // Get any return text
            char c;
            String line = "";
            while (in.ready()) {
                c = (char) in.read();
                line += String.valueOf(c);
                if (c == '\n') {
                    strResult += line;
                    line = "";
                }
                if (strResult.contains(strPrompt)) {
                    while (in.ready()) {
                        in.read();
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            strResult = null;
        }
        return strResult;
    }

    public void setPrompt(String text) {
        strPrompt = text;
    }

    public void disconnect() throws IOException {

        if (boolConnected) {
            ourSocket.close();
        }
        ourSocket = null;
        in = null;
        out = null;
        boolConnected = false;
    }
}
