import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
	private String userName;
	private String password;
	private String fullName;
	private Date dateOfBirth;
	private String gender;
	private String address;
	private Date registrationDay;

	public User() {
	};

	public User(String userName, String password) throws SQLException, ClassNotFoundException, ParseException {
		this.userName = userName;
		this.password = password;
		Connection c = Tester.getConnection();
		Statement stm = c.createStatement();
		ResultSet rs = stm.executeQuery("select * from user where user_Name = '" + userName + "'");
		while (rs.next()) {
			this.fullName = rs.getString("FullName");
			SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
			this.dateOfBirth = d.parse(rs.getString("DateOfBirth"));
			this.gender = rs.getString("Gender");
			this.address = rs.getString("Address");
			this.registrationDay = d.parse(rs.getString("RegistrationDay"));
		}
		rs.close();
		stm.close();
		c.close();
	}

	public void updatePassword(String password) throws SQLException, ClassNotFoundException {
		this.password = password;
		Connection c = Tester.getConnection();
		String sql = "update user set password = ? where iduser = ?";
		String sql1 = "select user.iduser from user where user_Name = '" + this.userName + "'";
		Statement stm = c.createStatement();
		ResultSet rs = stm.executeQuery(sql1);
		while (rs.next()) {
			PreparedStatement p = c.prepareStatement(sql);
			p.setString(1, this.password);
			p.setInt(2, rs.getInt("iduser"));
			p.executeUpdate();
			p.close();
		}
		rs.close();
		stm.close();
		c.close();
	}

	public void updateFullName(String fullName) throws SQLException, ClassNotFoundException {
		this.fullName = fullName;
		Connection c = Tester.getConnection();
		String sql = "update user set FullName = ? where iduser = ?";
		String sql1 = "select user.iduser from user where user_Name = '" + this.userName + "'";
		Statement stm = c.createStatement();
		ResultSet rs = stm.executeQuery(sql1);
		while (rs.next()) {
			PreparedStatement p = c.prepareStatement(sql);
			p.setString(1, this.fullName);
			p.setInt(2, rs.getInt("iduser"));
			p.executeUpdate();
			p.close();
		}
		rs.close();
		stm.close();
		c.close();
	}

	public void updateAddress(String address) throws SQLException, ClassNotFoundException {
		this.address = address;
		Connection c = Tester.getConnection();
		String sql = "update user set Address = ? where iduser = ?";
		String sql1 = "select user.iduser from user where user_Name = '" + this.userName + "'";
		Statement stm = c.createStatement();
		ResultSet rs = stm.executeQuery(sql1);
		while (rs.next()) {
			PreparedStatement p = c.prepareStatement(sql);
			p.setString(1, this.address);
			p.setInt(2, rs.getInt("iduser"));
			p.executeUpdate();
			p.close();
		}
		rs.close();
		stm.close();
		c.close();
	}

	public void updateGender(String gender) throws SQLException, ClassNotFoundException {
		this.gender = gender;
		Connection c = Tester.getConnection();
		String sql = "update user set Gender = ? where iduser = ?";
		String sql1 = "select user.iduser from user where user_Name = '" + this.userName + "'";
		Statement stm = c.createStatement();
		ResultSet rs = stm.executeQuery(sql1);
		while (rs.next()) {
			PreparedStatement p = c.prepareStatement(sql);
			p.setString(1, this.gender);
			p.setInt(2, rs.getInt("iduser"));
			p.executeUpdate();
			p.close();
		}
		rs.close();
		stm.close();
		c.close();
	}

	public void updateDateOfBirth(Date dOB) throws SQLException, ClassNotFoundException {
		this.dateOfBirth = dOB;
		Connection c = Tester.getConnection();
		String sql = "update user set DateOfBirth = ? where iduser = ?";
		String sql1 = "select user.iduser from user where user_Name = '" + this.userName + "'";
		Statement stm = c.createStatement();
		ResultSet rs = stm.executeQuery(sql1);
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
		while (rs.next()) {
			PreparedStatement p = c.prepareStatement(sql);
			p.setString(1, d.format(this.dateOfBirth));
			p.setInt(2, rs.getInt("iduser"));
			p.executeUpdate();
			p.close();
		}
		rs.close();
		stm.close();
		c.close();
	}

	@Override
	public String toString() {
		SimpleDateFormat d1 = new SimpleDateFormat("dd-MM-yyyy");
		return String.format("%s\t\t\t%s\t\t%s\t\t%s\t\t%s\t\t%s", userName, fullName, d1.format(dateOfBirth), gender,
				address, d1.format(registrationDay));
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getRegistrationDay() {
		return registrationDay;
	}

}
