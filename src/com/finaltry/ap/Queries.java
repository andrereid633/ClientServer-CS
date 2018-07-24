package com.finaltry.ap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Queries {
	private DatabaseConnection connection = new DatabaseConnection();
	Statement stmt;
	
	/**
	 * NOTE: This method is used to determine if the user has been successfully validated
	 * */
	public boolean signInStudentUser(String Username, String password){
		int username = Integer.parseInt(Username);
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.getConnection().createStatement();
			
			String dbUsername = "";
			String dbPassword = "";
			String query = "SELECT * FROM Student WHERE ID ='" + username + "'";
			
			result = statement.executeQuery(query);
			
			while(result.next()){
	
				dbUsername = result.getString(1);
				dbPassword = result.getString(6);
								
				//value from DB converted so that fair comparison could be made
				int dbID = Integer.parseInt(dbUsername);
				
				if((dbID == username) && dbPassword.equals(password)){ 
					return true;
				}
			}
		} catch (SQLException e) {
			System.err.println("SIGN-IN-USER: " + e.getMessage());
		}
		return false;
	}
	
	public boolean signInEmployeeUser(String Username, String password){
		int username = Integer.parseInt(Username);
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.getConnection().createStatement();
			
			String dbUsername = "";
			String dbPassword = "";
			String query = "SELECT * FROM Employee WHERE ID ='" + username + "'";
			
			result = statement.executeQuery(query);
			
			while(result.next()){
				System.out.println(result.getString(1) + " " + result.getString(2));
	
				dbUsername = result.getString(1);
				dbPassword = result.getString(2);
				
								
				//value from DB converted so that fair comparison could be made
				int dbID = Integer.parseInt(dbUsername);
				
				//GET USER TYPE FROM GUI COMPONENT
				if((dbID == username) && dbPassword.equals(password)){ 
					return true;
				}
			}
		} catch (SQLException e) {
			System.err.println("Queries/SIGN-IN-USER: " + e.getMessage());
		}
		return false;
	}
	
	// /THESE ARE FOR THE STUDENTS INTERACTION WITH THE DATABASE/////////////

		public ResultSet getEnquiriesStudent(int studentID) {

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

		public String viewResponses(int enquiryID) {
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

		public void addResponse(int enquiryID, String response) {
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

		public void addEnquiry(String enquiry, String service) {
			try {
				stmt = connection.getConnection().createStatement();

				String query = "INSERT INTO `enquiries` (`Stu_ID`, `Date`, `Service`, `Enquiry`, `Status`) VALUES ('3', CURRENT_DATE(), '"
						+ service + "', '" + enquiry + "','Unresolved')";
				stmt.executeUpdate(query);
			} catch (SQLException e) {
				System.err.println("addEntry: " + e.getMessage());
			}
		}

		public void uploadDocument(File file, String enquiry)
				throws FileNotFoundException, SQLException {

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

		public void getDocuments() throws IOException {
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

		// /THESE ARE FOR THE STUDENTS INTERACTION WITH THE DATABASE/////////////

		public String countResolvedService(String service) {

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

		public String countUnresolvedService(String service) {

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

		public String[] countServices() {
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

		public ResultSet viewService(String service) {

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

		public ResultSet viewEnquiry(int enquiryID) {

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


