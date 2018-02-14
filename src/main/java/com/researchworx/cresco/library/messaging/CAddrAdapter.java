package com.researchworx.cresco.library.messaging;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;

public class CAddrAdapter extends XmlAdapter<CAddrAdapter.AdaptedMap, CAddr> {
    static class AdaptedMap {
        List<Entry> entry = new ArrayList<>();
    }

    private static class Entry {
        String key;
        String value;
    }

    @Override
    public CAddr unmarshal(AdaptedMap adaptedMap) {
        String region = null;
        String agent = null;
        String plugin = null;
        for (Entry entry : adaptedMap.entry) {
            switch (entry.key) {
                case "region":
                    region = entry.value;
                    break;
                case "agent":
                    agent = entry.value;
                    break;
                case "plugin":
                    plugin = entry.value;
                    break;
                default:
                    break;
            }
        }
        return new CAddr(region, agent, plugin);
    }

    @Override
    public AdaptedMap marshal(CAddr cAddr) {
        AdaptedMap adaptedMap = new AdaptedMap();
        Entry region = new Entry();
        region.key = "region";
        region.value = cAddr.getRegion();
        adaptedMap.entry.add(region);
        Entry agent = new Entry();
        region.key = "agent";
        region.value = cAddr.getAgent();
        adaptedMap.entry.add(agent);
        Entry plugin = new Entry();
        region.key = "plugin";
        region.value = cAddr.getPlugin();
        adaptedMap.entry.add(plugin);
        return adaptedMap;
    }
}
