package com.researchworx.cresco.library.core;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Config {
    private INIConfiguration iniConfObj;
    private SubnodeConfiguration subConfObj;

    public Config(String configFile) throws ConfigurationException {
        Parameters params = new Parameters();
        File iniFile = new File(configFile);
        ReloadingFileBasedConfigurationBuilder<INIConfiguration> builder = new ReloadingFileBasedConfigurationBuilder<>(INIConfiguration.class).configure(params.hierarchical().setFile(iniFile));
        builder.setAutoSave(true);
        PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(builder.getReloadingController(), null, 1, TimeUnit.MINUTES);
        trigger.start();
        this.iniConfObj = builder.getConfiguration();
    }

    public Config (SubnodeConfiguration config) {
        this.subConfObj = config;
    }

    public Boolean getBooleanParam(String param) {
        if (this.iniConfObj != null) {
            return this.iniConfObj.getBoolean(param);
        }
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

    public Double getDoubleParam(String group, String param) {
        return iniConfObj.getSection(group).getDouble(param);
    }

    public Double getDoubleParam(String group, String param, Double ifNull) {
        Double ret = getDoubleParam(group, param);
        if (ret != null)
            return ret;
        return ifNull;
    }

    public Float getFloatParam(String group, String param) {
        return iniConfObj.getSection(group).getFloat(param);
    }

    public Float getDoubleParam(String group, String param, Float ifNull) {
        Float ret = getFloatParam(group, param);
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

    public Long getLongParam(String group, String param) {
        return iniConfObj.getSection(group).getLong(param);
    }

    public Long getLongParam(String group, String param, Long ifNull) {
        Long ret = getLongParam(group, param);
        if (ret != null)
            return ret;
        return ifNull;
    }

    public String getStringParam(String group, String param) {
        return iniConfObj.getSection(group).getString(param);
    }

    public String getStringParam(String group, String param, String ifNull) {
        String ret = getStringParam(group, param);
        if (ret != null)
            return ret;
        return ifNull;
    }
}