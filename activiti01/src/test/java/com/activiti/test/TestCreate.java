package com.activiti.test;

import org.activiti.engine.ProcessEngine;
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
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(defaultProcessEngine);
    }
}
