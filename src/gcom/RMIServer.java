/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 *
 * @author ens13pps
 */
public class RMIServer {

    private int port = 1099;

    public RMIServer(int port) {
        this.port = port;
    }

    public void start() throws RemoteException {
        LocateRegistry.createRegistry(port);
    }
}
