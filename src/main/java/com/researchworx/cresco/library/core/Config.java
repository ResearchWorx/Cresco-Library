package com.researchworx.cresco.library.core;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

public class Config {
    private INIConfiguration iniConfObj;
    private SubnodeConfiguration subConfObj;

    public Config(String configFile) throws ConfigurationException {
        Parameters params = new Parameters();
        File iniFile = new File(configFile);
        FileBasedConfigurationBuilder<INIConfiguration> builder = new FileBasedConfigurationBuilder<>(INIConfiguration.class).configure(params.hierarchical().setFile(iniFile));
        builder.setAutoSave(true);
        this.iniConfObj = builder.getConfiguration();
    }

    public Config (SubnodeConfiguration config) {
        this.subConfObj = config;
    }

    public Boolean getBooleanParam(String param) {
        if (this.iniConfObj != null)
            return this.iniConfObj.getBoolean(param);
        return this.subConfObj.getBoolean(param);
    }
    public Boolean getBooleanParam(String param, Boolean ifNull) {
        Boolean ret = getBooleanParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    public Boolean getBooleanParam(String group, String param) {
        return this.iniConfObj.getSection(group).getBoolean(param);
    }
    public Boolean getBooleanParam(String group, String param, Boolean ifNull) {
        Boolean ret = getBooleanParam(group, param);
        if (ret != null)
            return ret;
        return ifNull;
    }

    public Double getDoubleParam(String param) {
        if (this.iniConfObj != null)
            return this.iniConfObj.getDouble(param);
        return this.subConfObj.getDouble(param);
    }
    public Double getDoubleParam(String param, Double ifNull) {
        Double ret = getDoubleParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    public Double getDoubleParam(String group, String param) {
        return iniConfObj.getSection(group).getDouble(param);
    }
    public Double getDoubleParam(String group, String param, Double ifNull) {
        Double ret = getDoubleParam(group, param);
        if (ret != null)
            return ret;
        return ifNull;
    }

    public Integer getIntegerParam(String param) {
        if (this.iniConfObj != null)
            return this.iniConfObj.getInt(param);
        return this.subConfObj.getInt(param);
    }
    public Integer getIntegerParam(String param, Integer ifNull) {
        Integer ret = getIntegerParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    public Integer getIntegerParam(String group, String param) {
        return iniConfObj.getSection(group).getInt(param);
    }
    public Integer getIntegerParam(String group, String param, Integer ifNull) {
        Integer ret = getIntegerParam(group, param);
        if (ret != null)
            return ret;
        return ifNull;
    }

    public Long getLongParam(String param) {
        if (this.iniConfObj != null)
            return this.iniConfObj.getLong(param);
        return this.subConfObj.getLong(param);
    }
    public Long getLongParam(String param, Long ifNull) {
        Long ret = getLongParam(param);
        if (ret != null)
            return ret;
        return ifNull;
    }
    public Long getLongParam(String group, String param) {
        return iniConfObj.getSection(group).getLong(param);
    }
    public Long getLongParam(String group, String param, Long ifNull) {
        Long ret = getLongParam(group, param);
        if (ret != null)
            return ret;
        return ifNull;
    }

    public String getStringParam(String param) {
        if (this.iniConfObj != null)
            return this.iniConfObj.getString(param);
        return this.subConfObj.getString(param);
    }
    public String getStringParam(String group, String param) {
        return iniConfObj.getSection(group).getString(param);
    }
}