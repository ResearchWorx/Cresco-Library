package com.researchworx.cresco.library.messaging;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@XmlRootElement
public class MsgEvent {
    private static CAddr myAddress = new CAddr("my_region", "my_agent", "my_plugin");

    public enum Type {
        CONFIG, DISCOVER, ERROR, EXEC, GC, INFO, KPI, LOG, WATCHDOG
    }

    private Type msgType;
    private CAddr source;
    private CAddr destination;
    private Map<String, String> params;

    private String msgRegion;
    private String msgAgent;
    private String msgPlugin;

    public MsgEvent() {

    }

    public MsgEvent(Type msgType, String msgRegion, String msgAgent, String msgPlugin, String msgBody) {
        this.msgType = msgType;
        this.msgRegion = msgRegion;
        this.msgAgent = msgAgent;
        this.msgPlugin = msgPlugin;
        this.params = new HashMap<>();
        this.source = new CAddr("source_region", "source_agent", "source_plugin");
        this.destination = new CAddr("destination_region", "destination_agent", "destination_plugin");
        params.put("msg", msgBody);
    }

    public MsgEvent(Type msgType, String msgRegion, String msgAgent, String msgPlugin, Map<String, String> params) {
        this.msgType = msgType;
        this.msgRegion = msgRegion;
        this.msgAgent = msgAgent;
        this.msgPlugin = msgPlugin;
        this.params = params;
        this.params = new HashMap<>(params);
    }

    @XmlJavaTypeAdapter(CAddrAdapter.class)
    public CAddr getSource() {
        return source;
    }
    public void setSource(String region) {
        setParam("src_region", region);
        this.source = new CAddr(region);
    }
    public void setSource(String region, String agent) {
        setSource(region);
        setParam("src_agent", agent);
        this.source = new CAddr(region, agent);
    }
    public void setSource(String region, String agent, String plugin) {
        setSource(region, agent);
        setParam("src_plugin", plugin);
        this.source = new CAddr(region, agent, plugin);
    }

    @XmlJavaTypeAdapter(CAddrAdapter.class)
    public CAddr getDestination() {
        return destination;
    }
    public void setDestination(String region) {
        setParam("dst_region", region);
        this.destination = new CAddr(region);
    }
    public void setDestination(String region, String agent) {
        setDestination(region);
        setParam("dst_agent", agent);
        this.destination = new CAddr(region, agent);
    }
    public void setDestination(String region, String agent, String plugin) {
        setDestination(region, agent);
        setParam("dst_plugin", plugin);
        this.destination = new CAddr(region, agent, plugin);
    }

    public void setReturn() {
        String src_region = getParam("src_region");
        String src_agent = getParam("src_agent");
        String src_plugin = getParam("src_plugin");

        removeParam("src_region");
        removeParam("src_agent");
        removeParam("src_plugin");

        if (getParam("dst_region") != null) {
            setParam("src_region", getParam("dst_region"));
        }
        if (getParam("dst_agent") != null) {
            setParam("src_agent", getParam("dst_agent"));
        }
        if (getParam("dst_plugin") != null) {
            setParam("src_plugin", getParam("dst_plugin"));
        }

        if (src_region != null) {
            setParam("dst_region", src_region);
        } else {
            removeParam("dst_region");
        }
        //setMsgRegion(src_region);
        if (src_agent != null) {
            setParam("dst_agent", src_agent);
        } else {
            removeParam("dst_agent");
        }
        //setMsgAgent(src_agent);
        if (src_plugin != null) {
            setParam("dst_plugin", src_plugin);
        } else {
            removeParam("dst_plugin");
        }
        //setMsgPlugin(src_plugin);
    }

    /*public String getMsgBody() {
        return params.get("msg");
    }

    public void setMsgBody(String msgBody) {
        params.put("msg", msgBody);
    }*/

    @XmlJavaTypeAdapter(MsgEventTypesAdapter.class)
    public Type getMsgType() {
        return msgType;
    }

    public void setMsgType(Type msgType) {
        this.msgType = msgType;
    }

    /*public String getMsgRegion() {
        return msgRegion;
    }

    public void setMsgRegion(String msgRegion) {
        this.msgRegion = msgRegion;
    }

    public String getMsgAgent() {
        return msgAgent;
    }

    public void setMsgAgent(String msgAgent) {
        this.msgAgent = msgAgent;
    }

    public String getMsgPlugin() {
        return msgPlugin;
    }

    public void setMsgPlugin(String msgPlugin) {
        this.msgPlugin = msgPlugin;
    }*/

    @XmlJavaTypeAdapter(MsgEventParamsAdapter.class)
    public Map<String, String> getParams() {
        Map<String, String> uncompressedParams = new HashMap<>();
        for (String key : params.keySet()) {
            uncompressedParams.put(key, stringUncompress(params.get(key)));
        }
        return uncompressedParams;
    }

    public void setParams(Map<String, String> params) {
        this.params = new HashMap<>();
        for (String key : params.keySet()) {
            this.params.put(key, stringCompress(params.get(key)));
        }
        //this.params = params;
    }

    public String getParam(String key) {
        //return params.get(key);
        return stringUncompress(params.get(key));
    }

    public void setParam(String key, String value) {
        params.put(key, stringCompress(value));
    }

    public void removeParam(String key) {
        params.remove(key);
    }

    @Deprecated
    public void setCompressedParam(String key, String value) {
        setParam(key, value);
        //params.put(key, DatatypeConverter.printBase64Binary(stringCompress(value)));
    }

    @Deprecated
    public String getCompressedParam(String key) {
        return getParam(key);
        /*String value = params.get(key);
        if (value == null)
            return null;
        try {
            byte[] exportDataRawCompressed = DatatypeConverter.parseBase64Binary(value);
            InputStream iss = new ByteArrayInputStream(exportDataRawCompressed);
            InputStream is = new GZIPInputStream(iss);
            return new Scanner(is,"UTF-8").useDelimiter("\\A").next();
        } catch (IOException e) {
            return null;
        }*/
    }

    private String stringUncompress(String compressed) {
        if (compressed == null)
            return null;
        try {
            byte[] exportDataRawCompressed = DatatypeConverter.parseBase64Binary(compressed);
            InputStream iss = new ByteArrayInputStream(exportDataRawCompressed);
            InputStream is = new GZIPInputStream(iss);
            return new Scanner(is,"UTF-8").useDelimiter("\\A").next();
        } catch (IOException e) {
            return null;
        }
    }

    private String stringCompress(String str) {
        byte[] dataToCompress = str.getBytes(StandardCharsets.UTF_8);
        byte[] compressedData;
        try {
            ByteArrayOutputStream byteStream =
                    new ByteArrayOutputStream(dataToCompress.length);
            try {
                GZIPOutputStream zipStream =
                        new GZIPOutputStream(byteStream);
                try {
                    zipStream.write(dataToCompress);
                }
                finally {
                    zipStream.close();
                }
            } finally {
                byteStream.close();
            }
            compressedData = byteStream.toByteArray();
        } catch(Exception e) {
            return null;
        }
        return DatatypeConverter.printBase64Binary(compressedData);
    }

    /*private byte[] stringCompress(String str) {
        byte[] dataToCompress = str.getBytes(StandardCharsets.UTF_8);
        byte[] compressedData;
        try {
            ByteArrayOutputStream byteStream =
                    new ByteArrayOutputStream(dataToCompress.length);
            try {
                GZIPOutputStream zipStream =
                        new GZIPOutputStream(byteStream);
                try {
                    zipStream.write(dataToCompress);
                }
                finally {
                    zipStream.close();
                }
            } finally {
                byteStream.close();
            }
            compressedData = byteStream.toByteArray();
        } catch(Exception e) {
            return null;
        }
        return compressedData;
    }*/

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MsgEvent))
            return false;

        MsgEvent msgEvent = (MsgEvent)o;

        return getMsgType().equals(msgEvent.getMsgType()) &&
                //getMsgRegion().equals(msgEvent.getMsgRegion()) &&
                //getMsgAgent().equals(msgEvent.getMsgAgent()) &&
                //getMsgPlugin().equals(msgEvent.getMsgPlugin()) &&
                getParams().equals(msgEvent.getParams());
    }
}