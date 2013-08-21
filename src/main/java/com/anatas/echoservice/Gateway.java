/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anatas.echoservice;

import cf.component.http.SimpleHttpServer;
import cf.service.GatewayServer;
import java.net.InetSocketAddress;

/**
 *
 * @author Francois Hensley <francois.hensley@prododgenous.com>
 */
public class Gateway {

    private Gateway() {
    }

    public Gateway(int port) throws Exception {
        try (final SimpleHttpServer server = new SimpleHttpServer(new InetSocketAddress(port))) {

            final MyProvisioner provisioner = new MyProvisioner();
            new GatewayServer(server, provisioner, Common.AUTH_TOKEN);
        }
    }
}
