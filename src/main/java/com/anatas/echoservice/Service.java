/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anatas.echoservice;

import cf.client.CfTokens;
import cf.client.CloudController;
import cf.client.DefaultCloudController;
import cf.client.Resource;
import cf.client.RestCollection;
import cf.client.model.ServiceAuthToken;
import cf.client.model.ServicePlan;
import java.net.URI;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Francois Hensley <francois.hensley@prododgenous.com>
 */
public class Service {

    private Service() {
    }

    public Service(String host, int port) {
        final CfTokens cfTokens = new CfTokens();
        final CfTokens.CfToken target = cfTokens.getCurrentTargetToken();

        if (target == null) {
            System.err.println("It appears you haven't logged into a Cloud Foundry instance with cf.");
            return;
        }
        if (target.getVersion() == null || target.getVersion() != 2) {
            System.err.println("You must target a v2 Cloud Controller using cf.");
            return;
        }
        if (target.getSpaceGuid() == null) {
            System.err.println("You must select a space to use using cf.");
            return;
        }

        final String label = "testService" + ThreadLocalRandom.current().nextInt();
        final String provider = "core";
        //final URI url = URI.create("http://" + localIp(target.getTarget()) + ":" + Common.PORT);
        final URI url = URI.create(String.format("http://%s:%d", host, port));
        final String description = "A service used for testing the service framework.";
        final String version = "0.1";

        final String servicePlan = "ServicePlan";
        final String servicePlanDescription = "Finest service... ever.";

        final CloudController cloudControllerClient = new DefaultCloudController(new DefaultHttpClient(), target.getTarget());
        RestCollection<ServicePlan> servicePlans = cloudControllerClient.getServicePlans(target.getToken());
        for (Iterator<Resource<ServicePlan>> it = servicePlans.iterator(); it.hasNext();) {
            Resource<ServicePlan> resource = it.next();
            ServicePlan servicePlan1 = resource.getEntity();
            System.out.println("servicePlan Name " + servicePlan1.getName());
            if (servicePlan1.getName().equals(servicePlan)) {
                System.out.println("Delete servicePlan Name " + servicePlan1.getName());
                cloudControllerClient.deleteServicePlan(target.getToken(), resource.getGuid());
                System.out.println("Deleted servicePlan Name " + servicePlan1.getName());
            }
        }

        RestCollection<ServiceAuthToken> serviceAuthTokens = cloudControllerClient.getAuthTokens(target.getToken());
        for (Iterator<Resource<ServiceAuthToken>> it = serviceAuthTokens.iterator(); it.hasNext();) {
            Resource<ServiceAuthToken> resource = it.next();
            ServiceAuthToken serviceAuthToken = resource.getEntity();
            System.out.println("serviceAuthToken Label " + serviceAuthToken.getLabel());
            if (serviceAuthToken.getLabel().startsWith("testService")) {
                System.out.println("Delete serviceAuthToken Label " + serviceAuthToken.getLabel());
                cloudControllerClient.deleteServiceAuthToken(target.getToken(), resource.getGuid());
                System.out.println("Deleted serviceAuthToken Label " + serviceAuthToken.getLabel());
            }
        }

        RestCollection<cf.client.model.Service> services = cloudControllerClient.getServices(target.getToken());
        for (Iterator<Resource<cf.client.model.Service>> it = services.iterator(); it.hasNext();) {
            Resource<cf.client.model.Service> resource = it.next();
            cf.client.model.Service service = resource.getEntity();
            System.out.println("service name " + service.getLabel());
            if (service.getLabel().startsWith("testService")) {
                System.out.println("Delete service name " + service.getLabel());
                cloudControllerClient.deleteService(target.getToken(), resource.getGuid());
                System.out.println("Deleted service name " + service.getLabel());
            }
        }

        UUID serviceGuid = cloudControllerClient.createService(target.getToken(), new cf.client.model.Service(
                label, provider, url, description, version, null, true, UUID.randomUUID().toString()));
        System.out.println("serviceGuid " + serviceGuid);
        UUID servicePlanGuid = cloudControllerClient.createServicePlan(target.getToken(), new ServicePlan(servicePlan, servicePlanDescription, serviceGuid, true, UUID.randomUUID().toString()));
        System.out.println("servicePlanGuid " + servicePlanGuid);
        UUID authTokenGuid = cloudControllerClient.createAuthToken(target.getToken(), new ServiceAuthToken(label, provider, Common.AUTH_TOKEN));
        System.out.println("authTokenGuid " + authTokenGuid);
    }
}
