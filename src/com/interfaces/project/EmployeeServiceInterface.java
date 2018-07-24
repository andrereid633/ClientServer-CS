package com.interfaces.project;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;

public interface EmployeeServiceInterface extends Remote{
	public String countResolvedService(String service) throws RemoteException;
	public String countUnresolvedService(String service) throws RemoteException;
	public String[] countServices() throws RemoteException;
	public ResultSet viewService(String service) throws RemoteException;
	public ResultSet viewEnquiry(int enquiryID) throws RemoteException;
}
