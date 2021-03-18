import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Question_Management {
	private static final String[] LABELS = { "A", "B", "C", "D" };
	private List<Question> questions;
	Scanner s = new Scanner(System.in);

	public Question_Management() {
		this.questions = new ArrayList<>();
	}

	public void addQuestion(Question q) {
		this.questions.add(q);
	}

	private List<Question> getMultiplechoice() {
		List<Question> kq = new ArrayList<>();
		for (Question q : this.questions)
			if (q instanceof Mutiple_Choice)
				kq.add(q);
		return kq;
	}

	private List<Question> getIncomplete() {
		List<Question> kq = new ArrayList<>();
		for (Question q : this.questions)
			if (q instanceof Incomplete)
				kq.add(q);
		return kq;
	}

	private List<Question> getConversation() {
		List<Question> kq = new ArrayList<>();
		for (Question q : this.questions)
			if (q instanceof Conversation)
				kq.add(q);
		return kq;
	}

	/**
	 * Phuong thuc luyen tap va tinh diem
	 * @param num so cau hoi nguoi dung muon luyen tap
	 * @param u tai khoan dang luyen tap
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	public void practiceMutipleChoice(Scanner scanner, int num, User u) throws ClassNotFoundException, SQLException {
		int point = 0;
		List<Question> kq = this.getMultiplechoice();
		int n = kq.size() > num ? num : kq.size();


		for (int i = 0; i < n; i++) {
			System.out.println(this.questions.get(i));
			System.out.print("Answer: ");
			String s = scanner.next();
			Mutiple_Choice q = (Mutiple_Choice) kq.get(i);
			if (q.checkAnswer(s)) {
				System.out.println("TRUE");
				point++;
			} else
				System.out.println("FALSE");
		}
		double diem = point * 10 / num;
		Connection c = Tester.getConnection();
		String sql = "insert into result(userName, number, testDay, score) values(?,?,?,?)";
		PreparedStatement p = c.prepareStatement(sql);
		p.setString(1, u.getUserName());
		p.setInt(2, num);
		SimpleDateFormat d2 = new SimpleDateFormat("yyyy-MM-dd");
		p.setString(3, d2.format(new Date()));
		p.setDouble(4, diem);
		p.executeUpdate();
		System.out.println("Your score: " + point + "/" + num);
	}

	public void practiceMutipleChoice(Scanner scanner, int num, User u, Connection con)
			throws SQLException, ClassNotFoundException {
		Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery("select * from question where question.Type = 1 order by rand() limit " + num);
		while (rs.next()) {
			Question m = new Mutiple_Choice(rs.getString("Content"), new Category(rs.getString("category")),
					new Level(rs.getString("level")));
			Statement stm1 = con.createStatement();
			ResultSet rs1 = stm1
					.executeQuery("select * from choice where choice.idquestion = "
							+ rs.getInt("idquestion"));
			while (rs1.next()) {
				Choice c = new Choice(rs1.getString("Content"), rs1.getBoolean("CorrectChoice"));
				m.addChoice(c);
			}
			rs1.close();
			stm1.close();

			this.questions.add(m);
		}
		stm.close();
		rs.close();
		this.practiceMutipleChoice(scanner, num, u);
		con.close();
	}

	public void practiceIncompleteQuestion(Scanner scanner, String lvl, User u)
			throws SQLException, ClassNotFoundException {
		int point = 0;
		int num = 0;
		List<Question> kq = this.getIncomplete();
		if (kq.size() > 0) {
			Incomplete q = (Incomplete) kq.get(0);
			System.out.println(q.getContent());
			int dem = 0;

			for (Mutiple_Choice m : q.getQuestions()) {
				System.out.printf("Question (%d) %s - Answer: ",++dem, m);
				scanner.nextLine();
				String s = scanner.next();
				num++;
				if (m.checkAnswer(s)) {
					System.out.println("TRUE");
					point++;
				} else
					System.out.println("FALSE");
			}
		}
		double diem = point * 10 / num;
		Connection c = Tester.getConnection();
		String sql = "insert into result(userName, number, testDay, score) values(?,?,?,?)";
		PreparedStatement p = c.prepareStatement(sql);
		p.setString(1, u.getUserName());
		p.setInt(2, num);
		SimpleDateFormat d2 = new SimpleDateFormat("yyyy-MM-dd");
		p.setString(3, d2.format(new Date()));
		p.setDouble(4, diem);
		p.executeUpdate();
		System.out.println("Your score: " + point + "/" + num);
	}

	public void practiceIncompleteQuestion(Scanner s, String lvl, User u, Connection con)
			throws SQLException, ClassNotFoundException {
		Statement stm = con.createStatement();
		ResultSet rs2 = stm.executeQuery("select * from paragraph where level = '" + lvl
				+ "' and type = 1 limit 1");
		while (rs2.next()) {
			Question m1 = new Incomplete(rs2.getString("Content"), new Category(rs2.getString("category")),
					new Level(rs2.getString("level")));
			Statement stm2 = con.createStatement();
			ResultSet rs = stm2
					.executeQuery("select * from question where question.Paragraph = "
							+ rs2.getInt("idparagraph"));
			while (rs.next()) {
				Question m = new Mutiple_Choice(rs.getString("Content"), new Category(rs.getString("category")),
						new Level(rs.getString("level")));
				System.out.println(m);
				Statement stm1 = con.createStatement();
				ResultSet rs1 = stm1
						.executeQuery("select * from choice where choice.idquestion = "
								+ rs.getInt("idquestion"));
				while (rs1.next()) {
					Choice c = new Choice(rs1.getString("Content"),
							rs1.getBoolean("CorrectChoice"));
					m.addChoice(c);
				}
				rs1.close();
				stm1.close();
				m1.addQuestionInc(m);
			}
			this.questions.add(m1);

			stm2.close();
			rs.close();

		}
		rs2.close();
		this.practiceIncompleteQuestion(s, lvl, u);
		stm.close();
		con.close();
	}

	public void practiceConversationQuestion(Scanner scanner, String lvl, User u)
			throws SQLException, ClassNotFoundException {
		int point = 0;
		int num = 0;

		List<Question> kq = this.getConversation();
		if (kq.size() > 0) {
			Conversation q = (Conversation) kq.get(0);
			System.out.println(q.getContent());
			int dem = 0;

			for (Mutiple_Choice m : q.getQuestions()) {
				System.out.printf("Question %d: %s - Answer: ",++dem, m);
				scanner.nextLine();
				String s = scanner.next();
				num++;
				if (m.checkAnswer(s)) {
					System.out.println("TRUE");
					point++;
				} else
					System.out.println("FALSE");
			}
		}

		double diem = point * 10 / num;
		Connection c = Tester.getConnection();
		String sql = "insert into result(userName, number, testDay, score) values(?,?,?,?)";
		PreparedStatement p = c.prepareStatement(sql);
		p.setString(1, u.getUserName());
		p.setInt(2, num);
		SimpleDateFormat d2 = new SimpleDateFormat("yyyy-MM-dd");
		p.setString(3, d2.format(new Date()));
		p.setDouble(4, diem);
		p.executeUpdate();
		System.out.println("Your score: " + point + "/" + num);
	}

	public void practiceConversationQuestion(Scanner s, String lvl, User u, Connection con)
			throws SQLException, ClassNotFoundException {
		Statement stm = con.createStatement();
		ResultSet rs2 = stm.executeQuery("select * from paragraph where level = '" + lvl
				+ "' and type = 2 limit 1");
		while (rs2.next()) {
			Question m1 = new Conversation(rs2.getString("Content"),
					new Category(rs2.getString("category")),
					new Level(rs2.getString("level")));
			Statement stm2 = con.createStatement();
			ResultSet rs = stm2
					.executeQuery("select * from question where question.Paragraph = "
							+ rs2.getInt("idparagraph"));
			while (rs.next()) {
				Question m = new Mutiple_Choice(rs.getString("Content"),
						new Category(rs.getString("category")),
						new Level(rs.getString("level")));

				Statement stm1 = con.createStatement();
				ResultSet rs1 = stm1
						.executeQuery("select * from choice where choice.idquestion = "
								+ rs.getInt("idquestion"));
				while (rs1.next()) {
					Choice c = new Choice(rs1.getString("Content"),
							rs1.getBoolean("CorrectChoice"));
					m.addChoice(c);
				}
				rs1.close();
				stm1.close();
				m1.addQuestionConv(m);
			}
			this.questions.add(m1);

			stm2.close();
			rs.close();

		}
		rs2.close();
		this.practiceConversationQuestion(s, lvl, u);
		stm.close();
		con.close();
	}

	public void thongKe(int month, User u, Connection c) throws SQLException {
		Statement stm = c.createStatement();
		double avg = 0;
		int dem = 0;
		ResultSet rs = stm.executeQuery(
				"select * from result where userName = '" + u.getUserName() + "' and month(testDay) = " + month);
		while (rs.next()) {
			dem++;
			avg = avg + rs.getDouble("score");
		}
		avg = avg / dem;
		System.out.println(avg);
	}

	public List<Question> searchQuestionByContent() throws ClassNotFoundException, SQLException {
		List<Question> list;
		try (Connection conn = Tester.getConnection()) {

			int count_1 = 1;
			list = new ArrayList<Question>();
			System.out.print("Enter content you are going to search: ");
			String searchContent = s.nextLine();
			try {
				try (Statement stm = conn.createStatement()) {
					ResultSet rs = stm
							.executeQuery("select * from question where Content like '%" + searchContent + "%'");
					System.out.println("\n**** The list of questions you are looking for ****");

					while (rs.next()) {
						// String id = rs.getString("question.idquestion");
						String content = rs.getString("question.Content");

						System.out.print("\nQuestion " + count_1 + ": ");
						System.out.println(content);

						Connection c1 = Tester.getConnection();
						PreparedStatement stm1 = c1
								.prepareStatement("select * from choice where ? = choice.idquestion ORDER BY RAND()");
						stm1.setInt(1, rs.getInt("idquestion"));
						ResultSet rs1 = stm1.executeQuery();

						int i = 0;
						while (rs1.next()) {
							String contentA = rs1.getString("choice.Content");
							System.out.println("\t" + LABELS[i] + ". " + contentA);
							i++;
						}
						rs1.close();
						stm1.close();
						c1.close();
						count_1++;
					}
					rs.close();
					stm.close();
					conn.close();
					if (count_1 - 1 == 0)
						System.out.println("Question list not found");
				}
			} catch (SQLException ex) {
				System.err.print(ex.getMessage());
			}
			return list;
		}
	}

	public List<Question> searchQuestionByCategory() throws ClassNotFoundException, SQLException {
		List<Question> list;
		try (Connection conn = Tester.getConnection()) {

			int count_1 = 1;
			list = new ArrayList<Question>();
			System.out.print("Enter category you are going to search: ");
			String searchCategory = s.nextLine();
			try {
				try (Statement stm = conn.createStatement()) {
					ResultSet rs = stm
							.executeQuery("select * from question where category like '%" + searchCategory + "%'");
					System.out.println("\n**** The list of questions you are looking for ****");

					while (rs.next()) {
						// String id = rs.getString("question.idquestion");
						String content = rs.getString("question.Content");

						System.out.print("\nQuestion " + count_1 + ": ");
						System.out.println(content);

						Connection c1 = Tester.getConnection();
						PreparedStatement stm1 = c1
								.prepareStatement("select * from choice where ? = choice.idquestion ORDER BY RAND()");
						stm1.setInt(1, rs.getInt("idquestion"));
						ResultSet rs1 = stm1.executeQuery();

						int i = 0;
						while (rs1.next()) {
							String contentA = rs1.getString("choice.Content");
							System.out.println("\t" + LABELS[i] + ". " + contentA);
							i++;
						}
						rs1.close();
						stm1.close();
						c1.close();
						count_1++;
					}
					rs.close();
					stm.close();
					conn.close();
					if (count_1 - 1 == 0)
						System.out.println("Question list not found");
				}
			} catch (SQLException ex) {
				System.err.print(ex.getMessage());
			}
			return list;
		}
	}

	public List<Question> searchQuestionByLevel() throws ClassNotFoundException, SQLException {
		List<Question> list;
		try (Connection conn = Tester.getConnection()) {

			int count_1 = 1;
			list = new ArrayList<Question>();
			System.out.print("Enter level you are going to search: ");
			String searchLevel = s.nextLine();
			try {
				try (Statement stm = conn.createStatement()) {
					ResultSet rs = stm.executeQuery("select * from question where level = '" + searchLevel + "'");
					System.out.println("\n**** The list of questions you are looking for ****");

					while (rs.next()) {
						// String id = rs.getString("question.idquestion");
						String content = rs.getString("question.content");

						System.out.print("\nQuestion " + count_1 + ": ");
						System.out.println(content);

						Connection c1 = Tester.getConnection();
						PreparedStatement stm1 = c1
								.prepareStatement("select * from choice where ? = choice.idquestion ORDER BY RAND()");
						stm1.setInt(1, rs.getInt("idquestion"));
						ResultSet rs1 = stm1.executeQuery();

						int i = 0;
						while (rs1.next()) {
							String contentA = rs1.getString("choice.Content");
							System.out.println("\t" + LABELS[i] + ". " + contentA);
							i++;
						}
						rs1.close();
						stm1.close();
						c1.close();
						count_1++;
					}
					rs.close();
					stm.close();
					conn.close();
					if (count_1 - 1 == 0)
						System.out.println("Question list not found");
				}
			} catch (SQLException ex) {
				System.err.print(ex.getMessage());
			}
			return list;
		}
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
}
