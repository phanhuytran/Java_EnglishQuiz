import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class UserManager {

	private List<User> ds;

	public UserManager() {
		this.ds = new ArrayList<>();
	}

	/**
	 * phuong thuc dang nhap, dang ky
	 * phuong thuc lay du lieu User tu CSDL
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws ParseException
	 */

	public User signIn(Scanner s) throws SQLException, ClassNotFoundException, ParseException {

		SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");
		boolean b = true;
		do {
			s.nextLine();
			System.out.print("Username: ");
			String userNameC1 = s.nextLine();
			System.out.print("Password: ");
			String passwordC1 = s.nextLine();
			Connection c1 = Tester.getConnection();
			Statement stm1 = c1.createStatement();
			ResultSet rs1 = stm1.executeQuery("select * from user");
			while (rs1.next()) {
				String checkUserName = rs1.getString("user_Name");
				String checkPassword = rs1.getString("password");
				String fullName = rs1.getString("FullName");
				String sex = rs1.getString("Gender");
				String addr = rs1.getString("Address");

				if (userNameC1.equals(checkUserName) && passwordC1.equals(checkPassword)) {
					User u = new User(userNameC1, passwordC1);

					System.out.printf("\n====================\n"
							+ "Username: %s\nFullname: %s\nDate of Birth: %s\nGender: %s\nAddress: %s\nRegistration Day: %s\n",
							checkUserName, fullName, d.format(u.getDateOfBirth()), sex, addr,
							d.format(u.getRegistrationDay()));
					b = false;
					return u;
				}
			}
			rs1.close();
			stm1.close();
			c1.close();
		} while (b);
		return null;
	}

	public void signUp(Scanner s) {

		boolean q = true;
		while (q) {
			try {
				s.nextLine();
				System.out.print("Full Name: ");
				String fullname = s.nextLine();

				System.out.print("DateOfBirth: ");
				String dOb = s.nextLine();

				System.out.print("Address: ");
				String address = s.nextLine();
				System.out.print("Sex: ");
				String sex = s.nextLine();

				String userNameC2;
				Connection cnn = Tester.getConnection();
				Statement stm = cnn.createStatement();
				Boolean flag;
				do {
					flag = true;
					System.out.print("Username: ");
					userNameC2 = s.nextLine();
					String sql = "select user.user_Name from user";
					ResultSet rs = stm.executeQuery(sql);
					while (rs.next()) {
						if (userNameC2.equals(rs.getString("user_Name"))) {
							System.out.println("User name already exit");
							flag = false;
						}
					}
					rs.close();
				} while (!flag);
				stm.close();
				cnn.close();

				String passwordC2 = null;
				String reInputPassword = null;
				System.out.print("Password: ");
				passwordC2 = s.nextLine();
				do {

					System.out.print("Confirm password: ");
					reInputPassword = s.nextLine();
					if (!(reInputPassword.equals(passwordC2)))
						System.out.print("Password incorrect!");

				} while (!(reInputPassword.equals(passwordC2)));
				System.out.println("*****");
				System.out.println("1. Create Account");
				System.out.println("2. Exit");
				System.out.print("Your choice: ");
				int signIn = s.nextInt();
				switch (signIn) {
				case 1:
					Connection c = Tester.getConnection();
					String sql = "insert into user(user_Name, password, FullName,"
							+ "DateOfBirth, Gender, Address, RegistrationDay)" + "values(?, ?, ?, ?, ?, ?, ?)";
					PreparedStatement p = c.prepareStatement(sql);
					p.setString(1, userNameC2);
					p.setString(2, passwordC2);
					p.setString(3, fullname);
					p.setString(5, sex);
					p.setString(6, address);
					SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");
					SimpleDateFormat d2 = new SimpleDateFormat("yyyy-MM-dd");
					p.setString(4, d2.format(d.parse(dOb)));
					p.setString(7, d2.format(new Date()));
					p.executeUpdate();
					p.close();
					c.close();
					break;
				case 2:
					break;
				}
				q = false;
			} catch (Exception e) {
				System.out.println("Connection unsuccessful\n");

			}
		}
	}

	public void takeUserFromDB() throws SQLException, ClassNotFoundException, ParseException {
		String sql = "select * from user";
		Connection cnn = Tester.getConnection();
		Statement stm = cnn.createStatement();
		ResultSet rs = stm.executeQuery(sql);
		while (rs.next()) {
			User u = new User(rs.getString("user_Name"), rs.getString("password"));
			this.ds.add(u);
		}
		rs.close();
		stm.close();
		cnn.close();
	}

	/**
	 * @parama phuong thuc xoa, tim kiem user
	 * @param u ten user muon xoa
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public boolean removeUser(String u) throws SQLException, ClassNotFoundException {

		try (Connection conn = Tester.getConnection()) {
			String sql = "delete from user where user_Name = '" + u + "'";
			Statement stm = conn.createStatement();

			return stm.executeUpdate(sql) != 0 ? true : false;
		} catch (SQLException ex) {
			return false;
		}
	}

	/**
	 * Cac phuong thuc tim kiem User
	 * @param keyword tu khoa nguoi dung nhap de tim kiem
	 * @return
	 */

	public List<User> searchUserByName(String keyword) {
		System.out.println("\nUsername\t\tFullname\t\tDate of Birth\t\tGender\t\tAddress\t\tRegistration Day\n");
		List<User> ds = new ArrayList<>();
		int count = 0;
		for (User u : this.ds) {
			if (u.getFullName().toUpperCase().contains(keyword.toUpperCase()))
				ds.add(u);
				count++;
		}
		if (count == 0)
			System.out.println("Student list not found");
		return ds;
	}

	public List<User> searchUserByAddress(String keyword) {
		System.out.println("\nUsername\t\tFullname\t\tDate of Birth\t\tGender\t\tAddress\t\tRegistration Day\n");
		List<User> ds = new ArrayList<>();
		int count = 0;
		for (User u : this.ds) {
			if (u.getAddress().toUpperCase().contains(keyword.toUpperCase()))
				ds.add(u);
			count++;
		}
		if (count == 0)
			System.out.println("Student list not found");
		return ds;
	}

	public List<User> searchUserByDateOfBirth(String keyword) {
		System.out.println("\nUsername\t\tFullname\t\tDate of Birth\t\tGender\t\tAddress\t\tRegistration Day\n");
		List<User> ds = new ArrayList<>();
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
		int count = 0;
		for (User u : this.ds) {
			if (d.format(u.getDateOfBirth()).contains(keyword))
				ds.add(u);
			count++;
		}
		if (count == 0)
			System.out.println("Student list not found");
		return ds;
	}

	public List<User> searchUserByGender(String keyword) {
		System.out.println("\nUsername\t\tFullname\t\tDate of Birth\t\tGender\t\tAddress\t\tRegistration Day\n");
		List<User> ds = new ArrayList<User>();
		int count = 0;
		for (User u : this.ds) {
			if (u.getGender().toUpperCase().contains(keyword.toUpperCase()))
				ds.add(u);
			count++;
		}
		if (count == 0)
			System.out.println("Student list not found");
		return ds;
	}

	public void showListUser() {
		for (User u : this.ds)
			System.out.println(u);
	}

	public List<User> getDs() {
		return ds;
	}

	public void setDs(List<User> ds) {
		this.ds = ds;
	}
}
