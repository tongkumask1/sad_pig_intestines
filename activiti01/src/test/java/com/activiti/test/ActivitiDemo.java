package com.activiti.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ActivitiDemo {

    /**
     * 测试流程部署
     */
    @Test
    public void testDeployment(){
        //1.创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3.使用service进行流程部署，把bpmn和png都部署在数据库中，并且定义流程名称
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程")
                .addClasspathResource("bpmn/evection.bpmn")
                .addClasspathResource("bpmn/evection.png")
                .deploy();
        //4.输出部署信息
        System.out.println("流程部署id:"+deploy.getId());
        System.out.println("流程部署name:"+deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess(){
        //1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3.根据流程定义的id启动流程
        ProcessInstance evection = runtimeService.startProcessInstanceByKey("evection");
        //4.输出内容
        System.out.println("流程定义id："+evection.getProcessDefinitionId());
        System.out.println("流程实例id："+evection.getId());
        System.out.println("当前活动id："+evection.getActivityId());
    }
    /**
     * 查询个人待执行任务
     */
    @Test
    public void testFindDonalTaskList(){
        //1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取任务相关的service taskService
        TaskService taskService = processEngine.getTaskService();
        //3.根据流程key和任务的负责人进行查询
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("evection") //流程的key
                .taskAssignee("liuliu") //查询的负责人
                .list();
        //4.输出
        taskList.forEach(task->{
            System.out.println("流程实例id："+task.getProcessInstanceId());
            System.out.println("任务id："+task.getId());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("任务名称："+task.getName());
        });
    }
    /**
     * 完成个人任务
     */
    @Test
    public void completeTask(){
        //获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取能够处理完成任务的service taskService
        TaskService taskService = processEngine.getTaskService();
        //根据查出来的任务id来完成id
        taskService.complete("5002");
    }
    /**
     * 查询和完成任务整合
     */
    @Test
    public void queryAndComplete(){
        //1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取任务相关的service taskService
        TaskService taskService = processEngine.getTaskService();
        //3.根据流程key和任务的负责人进行查询
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("evection")
                .taskAssignee("liuliu")
                .singleResult();
        //4.根据查出来的任务id来完成id
        taskService.complete(task.getId());
    }
    /**
     * 流程定义信息查询
     */
    @Test
    public void queryProcessDefinition(){
        //1获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3得到ProcessDefinitionQuery对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //4查询出当前所有的流程定义
        //条件 processDefinitionKey = evection
        // order by desc
        // list 返回集合
        List<ProcessDefinition> list = processDefinitionQuery.processDefinitionKey("evection")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        //5输出流程信息
        list.forEach(p->{
            System.out.println("流程定义 id："+p.getId());
            System.out.println("流程定义 name："+p.getName());
            System.out.println("流程定义 key："+p.getKey());
            System.out.println("流程定义 Version："+p.getVersion());
            System.out.println("流程部署 ID："+p.getDeploymentId());
        });
    }

    /**
     * 流程删除
     */
    @Test
    public void deleteDeployment() {
        // 流程部署id
        String deploymentId = "1";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 通过流程引擎获取repositoryService
        RepositoryService repositoryService = processEngine
                .getRepositoryService();
        //删除流程定义，如果该流程定义已有流程实例启动则删除时出错
        repositoryService.deleteDeployment(deploymentId);
        //设置true 级联删除流程定义，即使该流程有流程实例启动也可以删除，设置为false非级别删除方式，如果流程
        //repositoryService.deleteDeployment(deploymentId,  );
    }
    /**
     * 使用zip包上传资源
     */
    @Test
    public void deployProcessByZip(){
        //1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取RepositoryService部署服务
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3.流程部署
        //1）读取资源包文件，构造成inputStream
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("bpmn/bpmn,zip");
        //2）判断inputStream是否为空
        assert inputStream != null;
        //3）使用inputStream构造zipInputStream
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        //4)使用zip压缩包流进行流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        //4.输出
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
     }
}
