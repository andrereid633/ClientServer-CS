package com.finaltry.ap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.interfaces.project.StudentEnquiriesInterface;

public class StudentService implements StudentEnquiriesInterface{
	
	private DatabaseConnection connection = new DatabaseConnection();
	Statement stmt;

	@Override
	public ResultSet getEnquiriesStudent(int studentID) throws RemoteException {
		try {
			stmt = connection.getConnection().createStatement();

			String query = "SELECT Service, enquiries.id, Enquiry,enquiry_response.person, enquiry_response.date, enquiry_response.response, Enquiry from enquiries left OUTER JOIN (select * from enquiry_response where (date) in(select max(date) as date from enquiry_response group by id) )enquiry_response on enquiries.id = enquiry_response.id where enquiries.Stu_ID="
					+ studentID + " group by id";
			ResultSet rs = stmt.executeQuery(query);

			return rs;

		} catch (SQLException e) {
			System.err.println("getEnquireies: " + e.getMessage());
		}
		return null;
	}

	@Override
	public String viewResponses(int enquiryID) throws RemoteException {
		String person;
		String response;
		StringBuilder result = new StringBuilder();
		String query = "SELECT response, person FROM `enquiry_response` WHERE `id` = "
				+ enquiryID + " order by date asc";
		try {
			stmt = connection.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				person = rs.getString("person");
				response = rs.getString("response");
				// System.out.println(person +"   "+response);
				result.append(person);
				result.append("       ");
				result.append(response);
				result.append(System.getProperty("line.separator"));

			}

		} catch (SQLException e) {
			System.err.println("viewResponses: " + e.getMessage());
		}
		System.out.print(result.toString());
		return result.toString();
	}

	@Override
	public void addResponse(int enquiryID, String response)
			throws RemoteException {
		try {
			stmt = connection.getConnection().createStatement();

			String query = "INSERT INTO `enquiry_response` (`id`, `response`, `person`, `date`) VALUES ('"
					+ enquiryID
					+ "', '"
					+ response
					+ "', 'You', CURRENT_TIMESTAMP);";
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			System.err.println("addResponses: " + e.getMessage());
		}
	}

	@Override
	public void addEnquiry(String enquiry, String service)
			throws RemoteException {
		try {
			stmt = connection.getConnection().createStatement();

			String query = "INSERT INTO `enquiries` (`Stu_ID`, `Date`, `Service`, `Enquiry`, `Status`) VALUES ('3', CURRENT_DATE(), '"
					+ service + "', '" + enquiry + "','Unresolved')";
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			System.err.println("addEntry: " + e.getMessage());
		}
	}

	@Override
	public void uploadDocument(File file, String enquiry)
			throws FileNotFoundException, SQLException, RemoteException {
		String query = "insert into enquiry_doc (Stu_ID,Enquiry, Document, Filename) values (3, '"
				+ enquiry + "', ?, '" + file.getName() + "')";

		FileInputStream fis = null;
		PreparedStatement ps = null;
		fis = new FileInputStream(file);
		ps = connection.getConnection().prepareStatement(query);
		ps.setBinaryStream(1, fis);

		ps.executeUpdate();

		System.out.println("Record inserted successfully");
	}

	@Override
	public void getDocuments() throws IOException, RemoteException {
		OutputStream out[] = new OutputStream[10];
		int i = 0;
		try {
			stmt = connection.getConnection().createStatement();

			String query = "select * from enquiry_doc where Stu_ID = 3";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Blob blob = rs.getBlob("Document");
				String Filename = rs.getString("Filename");
				System.out.println(Filename);
				InputStream in = blob.getBinaryStream();
				out[i] = new FileOutputStream("img\\" + Filename);

				byte[] buff = new byte[4096]; // how much of the blob to
												// read/write at a time
				int len = 0;

				while ((len = in.read(buff)) != -1) {
					out[i].write(buff, 0, len);
				}
			}
			System.out.println("Record gotten successfully");
		} catch (SQLException e) {
			System.err.println("getDocuments: " + e.getMessage());
		}
	}

}
