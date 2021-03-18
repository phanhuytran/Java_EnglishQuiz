import java.sql.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Tester {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {

		Scanner s = new Scanner(System.in);
		System.out.print("\n**** ENGLISH! NEW FUTURE FOR YOU ****");
		int signInUp;
		User u1;
		UserManager ds = new UserManager();
		System.out.println();

		do {
			System.out.println("\n1. Sign in\n2. Sign up\n3. Quit");
			System.out.print("Your choice: ");
			signInUp = s.nextInt();
			if (signInUp <= 0 || signInUp >= 3) {
				System.out.println("\nGOOD BYE. SEE YOU AGAIN!!!");
				break;
			}
			if (signInUp == 2) {
				System.out.println();
				ds.signUp(s);
			}
			System.out.println();
			u1 = ds.signIn(s);

			Question_Management ql = new Question_Management();
			int quickChoice;
			do {

				System.out.println();
				System.out.println("1. Practice\n2. Study Document\n3. Student List\n4. Edit Account\n5. Sign out");
				System.out.print("Your choice: ");
				quickChoice = s.nextInt();
				switch (quickChoice) {
				case 1:
					System.out.println(
							"\n*** MENU ***\n1. Practice with Multiple Choice Question\n2. Practice with Incomplete Question\n3. Practice with Conversation Question\n4. Test score\n");
					System.out.print("Your choice: ");
					int choice = s.nextInt();

					switch (choice) {
					case 1:
						System.out.print("Enter number of questions what you want to practice: ");
						int numberOfQuestion = s.nextInt();
						ql.practiceMutipleChoice(s, numberOfQuestion, u1, getConnection());
						break;
					case 2:
						System.out.println("**** Level ****\n1. EASY\n2. MEDIUM\n3. DIFFICULT\n");
						System.out.print("Choose level: ");
						int lvl = s.nextInt();
						switch (lvl) {
						case 1:
							System.out.println("**** Level EASY ****");
							ql.practiceIncompleteQuestion(s, "easy", u1, getConnection());
							break;
						case 2:
							System.out.println("**** Level MEDIUM ****");
							ql.practiceIncompleteQuestion(s, "medium", u1, getConnection());
							break;
						case 3:
							System.out.println("**** Level DIFFICULT ****");
							ql.practiceIncompleteQuestion(s, "difficult", u1, getConnection());
							break;
						}
						break;
					case 3:
						System.out.println("**** Level ****\n1. EASY\n2. MEDIUM\n3. DIFFICULT\n");
						System.out.print("Choose level: ");
						lvl = s.nextInt();
						switch (lvl) {
						case 1:
							System.out.println("**** Level EASY ****");
							ql.practiceConversationQuestion(s, "easy", u1, getConnection());
							break;
						case 2:
							System.out.println("**** Level MEDIUM ****");
							ql.practiceConversationQuestion(s, "medium", u1, getConnection());
							break;
						case 3:
							System.out.println("**** Level DIFFICULT ****");
							ql.practiceConversationQuestion(s, "difficult", u1, getConnection());
							break;
						}
						break;
					case 4:
						System.out.println("\nTEST SCORE\n");
						System.out.print("Enter the month: ");
						int month = s.nextInt();
						System.out.print("The exam results for month " + month + " are: ");
						ql.thongKe(month, u1, getConnection());
						System.out.println();
					}
					break;
				case 2:
					System.out.println();
					System.out.println(
							"1. Search question by content\n2. Search question by category\n3. Search question by level");
					System.out.print("Your choice: ");
					int sQuestion = s.nextInt();

					switch (sQuestion) {
					case 1:
						ql.searchQuestionByContent();
						break;
					case 2:
						System.out.println("\n** Category **\n1. Adjective\n2. Adverb\n3. Noun\n4. Verb");
						ql.searchQuestionByCategory();
						break;
					case 3:
						System.out.println("\n** Level **\n1. Easy\n2. Medium\n3. Difficult");
						ql.searchQuestionByLevel();
						break;
					}
					break;
				case 3:
					System.out.println("\nUsername\t\tFullname\t\tDate of Birth\t\tGender\t\tAddress\t\tRegistration Day\n");
					ds.takeUserFromDB();
					ds.showListUser();
					break;
				case 4:
					System.out.println();
					System.out.println(
							"**** Edit Account ****\n\n1. Edit password\n2. Edit your full name\n3. Edit Gender\n4. Edit Address\n5. Edit Date Of Birth\n6. Remove Account\n7. Search Account\n8. Exit");
					System.out.print("Your choice: ");
					int edit = s.nextInt();
					switch (edit) {
					case 1:
						System.out.print("Enter your new password: ");
						s.nextLine();
						String newPass = s.nextLine();
						u1.updatePassword(newPass);
						break;
					case 2:
						System.out.print("Enter your new full name: ");
						s.nextLine();
						String newFullName = s.next();
						u1.updateFullName(newFullName);
						break;
					case 3:
						System.out.print("Enter your new gender: ");
						s.nextLine();
						String newGender = s.nextLine();
						u1.updateGender(newGender);
						break;
					case 4:
						System.out.print("Enter your new address: ");
						s.nextLine();
						String newAddress = s.nextLine();
						u1.updateAddress(newAddress);
						break;
					case 5:
						System.out.print("Enter your new date fo birth: ");
						s.nextLine();
						String newDOB = s.next();
						SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");
						u1.updateDateOfBirth(d.parse(newDOB));
						break;
					case 6:
						if (u1.getUserName().equals("admin")) {
							System.out.print("Enter username you are going to remove: ");
							String removeUser = s.next();
							if (ds.removeUser(removeUser))
								System.out.println("Remove successfully");
						} else
							System.out.println("\nOnly admin can remove!!!");
						break;
					case 7:
						System.out.println();
						System.out.println(
								"1. Search account by fullname\n2. Search account by gender\n3. Search account by address\n4. Search account by date of birth");
						System.out.print("Your choice: ");
						int search = s.nextInt();
						switch (search) {
						case 1:
							System.out.print("Enter fullname you are going to search: ");
							String searchFullName = s.next();
							ds.searchUserByName(searchFullName).forEach(p -> System.out.println(p));
							break;
						case 2:
							System.out.print("Enter gender you are going to search: ");
							String searchGender = s.next();
							ds.searchUserByGender(searchGender).forEach(p -> System.out.println(p));
							break;
						case 3:
							System.out.print("Enter address you are going to search: ");
							String searchAddress = s.next();
							ds.searchUserByAddress(searchAddress).forEach(p -> System.out.println(p));
							break;
						case 4:
							System.out.print("Enter date of birth you are going to search: ");
							String searchDOB = s.next();
							ds.searchUserByDateOfBirth(searchDOB).forEach(p -> System.out.println(p));
							break;
						}
						break;
					}

					break;
				case 5:
					u1 = null;
					break;
				}
			} while (quickChoice > 0 && quickChoice < 5);

		} while (signInUp > 0 && signInUp < 3);
	}

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		String DB_URL = "jdbc:mysql://localhost/english";
		String USER_NAME = "root";
		String PASSWORD = "13161710Sq";

		// System.out.println("Driver loading");
		Class.forName("com.mysql.cj.jdbc.Driver");
		// System.out.println("Driver loaded");

		Connection cnn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
		// System.out.println("Connected");
		return cnn;
	}
}
