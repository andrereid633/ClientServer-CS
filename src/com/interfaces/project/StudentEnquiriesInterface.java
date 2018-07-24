package com.interfaces.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface StudentEnquiriesInterface extends Remote {
	public ResultSet getEnquiriesStudent(int studentID) throws RemoteException;
	public String viewResponses(int enquiryID) throws RemoteException;
	public void addResponse(int enquiryID, String response) throws RemoteException;
	public void addEnquiry(String enquiry, String service) throws RemoteException;
	public void uploadDocument(File file, String enquiry) throws FileNotFoundException, SQLException , RemoteException;
	public void getDocuments() throws IOException, RemoteException;
}
