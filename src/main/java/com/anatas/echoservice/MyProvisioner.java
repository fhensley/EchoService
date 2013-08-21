/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anatas.echoservice;

import cf.service.BindRequest;
import cf.service.CreateRequest;
import cf.service.Provisioner;
import cf.service.ServiceBinding;
import cf.service.ServiceInstance;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Francois Hensley <francois.hensley@prododgenous.com>
 */
public class MyProvisioner implements Provisioner {

    private final AtomicInteger createInvocationCount = new AtomicInteger();
    private final AtomicInteger deleteInvocationCount = new AtomicInteger();
    private final AtomicInteger bindInvocationCount = new AtomicInteger();
    private volatile int lastCreateId;
    private volatile int lastDeleteId;

    @Override
    public ServiceInstance create(CreateRequest request) {
        System.out.println("Let's create a service!");
        final Integer id = createInvocationCount.getAndIncrement();
        lastCreateId = id;
        final ServiceInstance serviceInstance = new ServiceInstance(id.toString());
        serviceInstance.addGatewayDataField("config", "value");
        serviceInstance.addCredential("user", "yourmom");
        return serviceInstance;
    }

    @Override
    public void delete(String instanceId) {
        System.out.println("Deleting service! " + instanceId);
        lastDeleteId = Integer.valueOf(instanceId);
        deleteInvocationCount.getAndIncrement();
    }

    @Override
    public ServiceBinding bind(BindRequest request) {
        System.out.println("Binding a service!");
        Integer id = bindInvocationCount.getAndIncrement();
        return new ServiceBinding(request.getServiceInstanceId(), id.toString());
    }

    @Override
    public void unbind(String instanceId, String handleId) {
        System.out.println("Unbinding service! " + handleId);
    }

    @Override
    public Iterable<String> serviceInstanceIds() {
        return null;
    }

    @Override
    public Iterable<String> bindingIds(String instanceId) {
        return null;
    }

    @Override
    public void removeOrphanedBinding(String instanceId, String handleId) {
    }

    @Override
    public void removeOrphanedServiceInstance(String instanceId) {
    }

    public int getCreateInvocationCount() {
        return createInvocationCount.get();
    }

    public int getDeleteInvocationCount() {
        return deleteInvocationCount.get();
    }

    public int getBindInvocationCount() {
        return bindInvocationCount.get();
    }

    public int getLastCreateId() {
        return lastCreateId;
    }

    public int getLastDeleteId() {
        return lastDeleteId;
    }
}
