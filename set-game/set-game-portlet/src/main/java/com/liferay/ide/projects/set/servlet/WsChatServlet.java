//This sample is how to use websocket of Tomcat.
package com.liferay.ide.projects.set.servlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

public class WsChatServlet extends WebSocketServlet{
    private static final long serialVersionUID = 1L;
    private static ArrayList<MyMessageInbound> mmiList = new ArrayList<MyMessageInbound>();

    @Override
    public StreamInbound createWebSocketInbound(String protocol, HttpServletRequest arg1) {
        return new MyMessageInbound();
    }

    private class MyMessageInbound extends MessageInbound{
        WsOutbound myoutbound;

        @Override
        public void onOpen(WsOutbound outbound){
            try {
                System.out.println("Open Client.");
                this.myoutbound = outbound;
                mmiList.add(this);
                outbound.writeTextMessage(CharBuffer.wrap("Hello!"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClose(int status){
            System.out.println("Close Client.");
            mmiList.remove(this);
        }

        @Override
        public void onTextMessage(CharBuffer cb) throws IOException{
            System.out.println("Accept Message : "+ cb);
            for(MyMessageInbound mmib: mmiList){
                CharBuffer buffer = CharBuffer.wrap(cb);

                mmib.myoutbound.writeTextMessage(buffer);
                mmib.myoutbound.writeTextMessage(CharBuffer.wrap("response"));
                mmib.myoutbound.flush();
            }
        }

        @Override
        public void onBinaryMessage(ByteBuffer bb) throws IOException{
        }
    }
}
