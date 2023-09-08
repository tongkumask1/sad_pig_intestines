package com.activiti.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

public class TestCreate {
    /**
     * 使用activiti提供的默认方式来创建mysql的表
     */
    @Test
    public void testCreateDbTable(){
        //需要使用activiti提供的工具类 ProcessEngines 使用方法 getDefaultProcessEngine
        //getDefaultProcessEngine方法会默认从resources下读取名字为activiti.cfg.xml的文件
        //创建processEnginc的时候就会创建mysql的表
        //创建25张表
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
    }
    /*
    * 使用activiti自定义方式创建
    * */
    @Test
    public void testCreateZdy(){
        //使用自定义方式创建
        //配置文件的名字可以自定义
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        //创建 processEngine (流程引擎)对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println(processEngine);
    }

}
