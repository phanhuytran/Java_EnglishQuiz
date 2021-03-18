import java.util.ArrayList;
import java.util.List;

public class Incomplete extends Question {
	private List<Mutiple_Choice> questions;

	public Incomplete(String content, Category cat, Level lvl) {
		super(content, cat, lvl);

		this.questions = new ArrayList<>();
	}

	public void addQuestionInc(Question q) {
		Mutiple_Choice m = (Mutiple_Choice) q;
		this.questions.add(m);
	}

	@Override
	public String toString() {
		String s = super.toString();
		for (Mutiple_Choice q : this.questions) {
			s += q;
		}
		return s;
	}

	public List<Mutiple_Choice> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Mutiple_Choice> questions) {
		this.questions = questions;
	}
}
