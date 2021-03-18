public class Choice {
	private String content;
	private boolean correct;

	public Choice(String content, boolean correct) {
		this.content = content;
		this.setCorrect(correct);
	}

	@Override
	public String toString() {
		return this.content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
}
