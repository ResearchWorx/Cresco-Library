package com.researchworx.cresco.library.messaging;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MsgEventScopesAdapter extends XmlAdapter<String, MsgEvent.Scope> {
    @Override
    public MsgEvent.Scope unmarshal(String v) throws IllegalArgumentException {
        return Enum.valueOf(MsgEvent.Scope.class, v);
    }

    @Override
    public String marshal(MsgEvent.Scope v) throws IllegalArgumentException {
        return v.toString();
    }
}
