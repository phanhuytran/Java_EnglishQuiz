public abstract class Question {
	private String content;
	private Category cat;
	private Level level;

	public Question(String content, Category cat, Level level) {
		this.content = content;
		this.cat = cat;
		this.level = level;
	}

	public void addChoice(Choice c) {

	}

	public void addQuestionInc(Question q) {

	}

	public void addQuestionConv(Question q) {

	}

	@Override
	public String toString() {
		return this.content + "\n";
	}

	public String getContent() {

		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Category getCat() {
		return cat;
	}

	public void setCat(Category cat) {
		this.cat = cat;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}
}
