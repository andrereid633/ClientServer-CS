package com.finaltry.ap;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.interfaces.project.EmployeeServiceInterface;

public class EmployeeService extends UnicastRemoteObject implements EmployeeServiceInterface {
	
	private DatabaseConnection connection = new DatabaseConnection();
	Statement stmt;
	
	private static final long serialVersionUID = 1l;
	
	protected EmployeeService() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String countResolvedService(String service) throws RemoteException {
		try {
			if (connection == null) {
				System.out.println("ggg");
			}
			stmt = connection.getConnection().createStatement();
			String query = ("SELECT count(status) as count FROM `enquiries` WHERE service = '"
					+ service + "' and status = 'Resolved'");
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			String count = Integer.toString(rs.getInt("count"));
			// System.out.print(count);
			return count;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return service;
	}

	@Override
	public String countUnresolvedService(String service) throws RemoteException {
		try {

			stmt = connection.getConnection().createStatement();
			String query = ("SELECT count(status) as count FROM `enquiries` WHERE service = '"
					+ service + "' and status = 'Unresolved'");
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			String count = Integer.toString(rs.getInt("count"));
			// System.out.print(count);
			return count;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return service;
	}

	@Override
	public String[] countServices() throws RemoteException {
		String[] serviceCount = new String[10];
		serviceCount[0] = countResolvedService("Applying for Refunds");
		serviceCount[1] = countUnresolvedService("Applying for Refunds");
		serviceCount[2] = countResolvedService("Applying for Financial Clearance");
		serviceCount[3] = countUnresolvedService("Applying for Financial Clearance");
		serviceCount[4] = countResolvedService("Generation of Semester Fee Statement");
		serviceCount[5] = countUnresolvedService("Generation of Semester Fee Statement");
		serviceCount[6] = countResolvedService("Monies owed");
		serviceCount[7] = countUnresolvedService("Monies owed");
		serviceCount[8] = countResolvedService("Account balance");
		serviceCount[9] = countUnresolvedService("Account balance");
		return serviceCount;
	}

	@Override
	public ResultSet viewService(String service) throws RemoteException {
		try {
			stmt = connection.getConnection().createStatement();

			String query = "SELECT student.id as studentid, enquiries.Enquiry, enquiries.id, student.FirstName, student.LastName, enquiries.status FROM `enquiries`left outer join student on enquiries.Stu_ID = student.ID  WHERE Service = '"
					+ service + "' ";
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("working");
			return rs;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("null here");
		}
		return null;
	}

	@Override
	public ResultSet viewEnquiry(int enquiryID) throws RemoteException {
		try {
			stmt = connection.getConnection().createStatement();
			String query = "SELECT student.ID, student.FirstName, student.LastName, student.Email,student.Cell,account.ACC_BAL,account.ACC_BAL_DESCRIPTION, Enquiry from enquiries left outer join student on student.ID = Stu_ID left outer join account on account.ID = Stu_ID where enquiries.id = "
					+ enquiryID + " ";
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}


