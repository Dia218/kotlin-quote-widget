package view;

import java.util.List;
import java.util.Scanner;

import enums.Command;

public class QuoteView {
	private final String TITLE = "== 명언 앱 ==\n";
	private final String COMMAND = "\n명령) ";
	private final String ID_PROMPT = "%s할 id를 입력해주세요: ";
	private final String CONTENT = "명언%s : ";
	private final String AUTHOR = "작가%s : ";
	private final String PREVIOUS = "(기존)";
	private final String COMPLETE = "%d번 명언이 %s되었습니다.\n";
	private final String BUILD_NOTIFICATION = "빌드가 완료되었습니다.\n";
	private final String LIST_HEAD = "번호 / 작가 / 명언\n" + "----------------------\n";

	private final Scanner scanner;

	public QuoteView() {
		scanner = new Scanner(System.in);
	}

	public void showTitle() {
		printMessage(TITLE);
	}

	public String requestCommand() {
		printMessage(COMMAND);
		return scanner.nextLine();
	}

	public String requestTargetId(Command command) {
		printMessage(COMMAND + String.format(ID_PROMPT, command.getValue()));
		return scanner.nextLine();
	}

	public String[] requestRegister() {
		return new String[] {requestNewContent(),
			requestNewAuthor()};
	}

	public String[] requestUpdate(String[] previousContentAndAuthor) {
		return new String[] {requestNewContent(previousContentAndAuthor[0]),
			requestNewAuthor(previousContentAndAuthor[1])};
	}

	public void alertSuccess(int id, Command command) {
		printMessage(String.format(COMPLETE, id, command.getValue()));
	}

	public void alertSuccess() {
		printMessage(String.format(BUILD_NOTIFICATION));
	}

	public void displayQuotes(List<String> quotesInfo) {
		printMessage(LIST_HEAD);
		quotesInfo.stream()
			.sorted((a, b) -> quotesInfo.indexOf(b) - quotesInfo.indexOf(a))
			.forEach(quote -> printMessage(quote + "\n"));
	}

	public void displayErrorMessage(String message) {
		System.out.println(message);
	}

	public void closeScanner() {
		scanner.close();
	}

	private String requestNewContent() {
		printMessage(String.format(CONTENT, ""));
		return scanner.nextLine();
	}

	private String requestNewContent(String previousContent) {
		printMessage(String.format(CONTENT, PREVIOUS) + previousContent + "\n");
		return requestNewContent();
	}

	private String requestNewAuthor() {
		printMessage(String.format(AUTHOR, ""));
		return scanner.nextLine();
	}

	private String requestNewAuthor(String previousAuthor) {
		printMessage(String.format(AUTHOR, PREVIOUS) + previousAuthor + "\n");
		return requestNewAuthor();
	}

	private void printMessage(String message) {
		System.out.print(message);
	}
}
