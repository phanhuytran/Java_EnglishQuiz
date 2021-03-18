import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mutiple_Choice extends Question {
	private List<Choice> choices;
	private static final String[] LABELS = { "A", "B", "C", "D" };

	public Mutiple_Choice(String content, Category cat, Level lv) {
		super(content, cat, lv);
		this.choices = new ArrayList<>();
	}

	public void addChoice(Choice choice) {
		this.choices.add(choice);
	}

	public boolean checkAnswer(String ans) {
		for (int i = 0; i < this.choices.size(); i++)
			if (this.choices.get(i).isCorrect() == true && LABELS[i].equals(ans.toUpperCase())) {
				return true;
			}
		return false;
	}

	@Override
	public String toString() {
		String s = super.toString();
		if (this.choices.size() < LABELS.length)
			for (int i = 0; i < this.choices.size(); i++)
				s += String.format("%s. %s\n", LABELS[i], this.choices.get(i));
		else
			for (int i = 0; i < LABELS.length; i++)
				s += String.format("%s. %s\n", LABELS[i], this.choices.get(i));
		return s;
	}

	public List<Choice> getChoices() {
		return choices;
	}

	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}
}
