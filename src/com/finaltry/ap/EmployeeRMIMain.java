package com.finaltry.ap;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class EmployeeRMIMain {
	public static void main(String args[]){
		try {
			EmployeeService obj = new EmployeeService();
			Registry r = LocateRegistry.createRegistry(4500);
			r.bind("localhost", obj);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

