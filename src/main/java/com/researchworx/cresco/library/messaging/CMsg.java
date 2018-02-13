package com.researchworx.cresco.library.messaging;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class CMsg {
    public enum Scope {
        GLOBAL, REGION, AGENT, PLUGIN
    }

    public enum Type {
        CONFIG, DISCOVER, ERROR, EXEC, GC, INFO, KPI, LOG, WATCHDOG
    }

    private CAddr destination;
    private CAddr source;

    private Scope scope;
    private Type type;

    private Map<String, String> params = new HashMap<>();

    @XmlJavaTypeAdapter(CMsgParamsAdapter.class)
    public Map<String, String> getParams() {
        return params;
    }
    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    class CAddr {
        private String region;
        private String agent;
        private String plugin;

        public CAddr() {
            this.region = null;
            this.agent = null;
            this.plugin = null;
        }

        public CAddr(String region) {
            this();
            this.region = region;
        }

        public CAddr(String region, String agent) {
            this(region);
            this.agent = agent;
        }

        public CAddr(String region, String agent, String plugin) {
            this(region, agent);
            this.plugin = plugin;
        }

        // ToDo Getters & Setters
    }

    // ToDo Create CMsgCAddrTypeAdapter for Marshalling/Unmarshalling

    class CMsgTypeAdapter extends XmlAdapter<String, CMsg.Type> {
        @Override
        public CMsg.Type unmarshal(String v) throws IllegalArgumentException {
            return Enum.valueOf(CMsg.Type.class, v);
        }

        @Override
        public String marshal(CMsg.Type v) throws IllegalArgumentException {
            return v.toString();
        }
    }

    class CMsgScopeAdapter extends XmlAdapter<String, CMsg.Scope> {
        @Override
        public CMsg.Scope unmarshal(String v) throws IllegalArgumentException {
            return Enum.valueOf(CMsg.Scope.class, v);
        }

        @Override
        public String marshal(CMsg.Scope v) throws IllegalArgumentException {
            return v.toString();
        }
    }

    class CMsgParamsAdapter extends XmlAdapter<CMsgParamsAdapter.AdaptedMap, Map<String, String>> {

        class AdaptedMap {
            List<Entry> entry = new ArrayList<>();
        }

        private class Entry {
            String key;
            String value;
        }

        @Override
        public Map<String, String> unmarshal(AdaptedMap adaptedMap) {
            Map<String, String> map = new HashMap<>();
            for(Entry entry : adaptedMap.entry) {
                map.put(entry.key, entry.value);
            }
            return map;
        }

        @Override
        public AdaptedMap marshal(Map<String, String> map) {
            AdaptedMap adaptedMap = new AdaptedMap();
            for(Map.Entry<String, String> mapEntry : map.entrySet()) {
                Entry entry = new Entry();
                entry.key = mapEntry.getKey();
                entry.value = mapEntry.getValue();
                adaptedMap.entry.add(entry);
            }
            return adaptedMap;
        }
    }
}
