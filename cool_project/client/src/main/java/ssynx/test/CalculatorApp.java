package ssynx.test;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class CalculatorApp extends Application {
	private TextArea resultTextArea;
	private Button[] numberButton;
	private Button[] operationButton;
	
	private static final int WIDTH = 200;
	private static final int HEIGHT = 250;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void initWidget() {
		final String[] ops = { "+", "*", "-", "/", "C", "=" };
		
		resultTextArea = new TextArea();
		numberButton = new Button[10];
		operationButton = new Button[ops.length];
		
		for(int i = 0; i < 10; ++i) {
			numberButton[i] = new Button();
			numberButton[i].setText(Integer.toString(i));
		}
		
		for(int i = 0; i < ops.length; ++i) {
			operationButton[i] = new Button();
			operationButton[i].setText(ops[i]);
		}
		
		resultTextArea.setFont(Font.font("Consolas", FontWeight.BOLD, 15));
		resultTextArea.setPrefRowCount(1);
		resultTextArea.setEditable(false);
	}
	
	private Scene populateScene() {
		HBox hbox1 = new HBox(resultTextArea);
		HBox hbox2 = new HBox(32, numberButton[7], numberButton[8], numberButton[9], operationButton[0]);
		HBox hbox3 = new HBox(32, numberButton[4], numberButton[5], numberButton[6], operationButton[1]);
		HBox hbox4 = new HBox(32, numberButton[1], numberButton[2], numberButton[3], operationButton[2]);
		HBox hbox5 = new HBox(32, numberButton[0], operationButton[5], operationButton[4], operationButton[3]);
		
		VBox mainVbox = new VBox(hbox1, hbox2, hbox3, hbox4, hbox5);
		
		mainVbox.setSpacing(13);
		
		return new Scene(mainVbox);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		initWidget();
		
		for(final Button btn : numberButton)
			btn.setOnMouseClicked(handler);
		
		for(final Button btn : operationButton)
			btn.setOnMouseClicked(handler);
		
		emptyNumberState();
		
		primaryStage.setTitle("Calculator");
		primaryStage.setWidth(WIDTH);
		primaryStage.setHeight(HEIGHT);
		primaryStage.setScene(populateScene());
		primaryStage.show();
	}

	private class OperationResult {
		private final int res;
		private final boolean divByZero;

		public OperationResult(int res, boolean divByZero) {
			this.res = res;
			this.divByZero = divByZero;
		}

		public int getRes() {
			return res;
		}

		public boolean isDivByZero() {
			return divByZero;
		}
	}
	
	// Calculator states

	//Initial state -- empty: can only press numbers
	private void emptyNumberState() {
		for(final Button btn : operationButton)
			btn.setDisable(true);
	}

	private EventHandler<MouseEvent> handler = new EventHandler<MouseEvent> () {
		private String totalOpStr = "";
		private boolean divByZero = false;

		//At least one number was pressed, enable "C" (like backspace)
		private void leastNumberState() {
			int i = 0;

			for(i = 0; i < operationButton.length - 1; ++i)
				operationButton[i].setDisable(false);

			operationButton[i].setDisable(true);
		}

		//One number and one binary operation (+, *, -, /) 
		//was correctly specified, enable "="
		private void binaryOpSpecifiedState(boolean disableEquals) {
			for(int i = 0; i < 4; ++i)
				operationButton[i].setDisable(true);

			operationButton[4].setDisable(false);
			operationButton[5].setDisable(disableEquals);
		}

		//If valid operation then keep results and go to leastNumber state
		//otherwise (if division by zero) clear results and 
		//go back to emptyNumber state

		private OperationResult binaryOperator(char sym, int a, int b) {
			if(sym == '+')
				return new OperationResult(a + b, false);

			if(sym == '-')
				return new OperationResult(a - b, false);
				
			if(sym == '*')
				return new OperationResult(a * b, false);
				
			if(b == 0)
				return new OperationResult(0, true);
				
			return new OperationResult(a / b, false);
		}
		
		private String removeLastChar(String str) {
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < str.length() - 1; ++i)
				builder.append(Character.toString(str.charAt(i)));
			
			return builder.toString();
		}
		
		private int opSymLookup(String str) {
			final char[] symTable = { '*', '+', '-', '/' };
			for(int i = 0; i < str.length(); ++i) {
				for(final char sym : symTable) {
					if(sym == str.charAt(i))
						return i;
				}
			}
			
			return 0;
		}
		
		private Pair<String[], Character> split() {
			int symAt = opSymLookup(totalOpStr);
			if(symAt == 0)
				return new Pair<String[], Character>(new String[]{ totalOpStr },  ' ');
			
			char sym = totalOpStr.charAt(symAt);
			String[] ops = totalOpStr.split("\\" + sym);
			
			return new Pair<String[], Character>(ops, sym);
		}
		
		private void display() {
			if(divByZero) {
				resultTextArea.setText("DivByZero");
				divByZero = false;
			} else {
				Pair<String[], Character> paired = split();
				String[] ops = paired.getKey();
				char sym = paired.getValue();
			
				if(sym == ' ')
					resultTextArea.setText(ops[0]);
				else {
					if(ops.length > 1) {
						resultTextArea.setText(ops[0] + " " + sym + " " + ops[1]);
					} else {
						resultTextArea.setText(ops[0] + " " + sym);
					}
				}
			}
		}
		
		@Override
		public void handle(MouseEvent arg0) {
			Button b = (Button) arg0.getSource();
			String evText = b.getText();
			
			if(totalOpStr.equals("")) {
				totalOpStr += evText;
				leastNumberState();
			} else {
				if(evText.equals("+") || evText.equals("*") || 
						evText.equals("/") || evText.equals("-")) {
					totalOpStr += evText;
					binaryOpSpecifiedState(true);
				} else if(evText == "C") {
					totalOpStr = removeLastChar(totalOpStr);
					int len = totalOpStr.length();
					if(len == 0) {
						emptyNumberState();
					} else {
						char lastChar = totalOpStr.charAt(len - 1);
						if(lastChar == '*' || lastChar == '/' || 
								lastChar == '+' || lastChar == '-')
							binaryOpSpecifiedState(true);
						else {
							if(opSymLookup(totalOpStr) > 0)
								binaryOpSpecifiedState(false);
							else
								leastNumberState();
						}
					}
				} else if(evText.equals("=")) {
					Pair<String[], Character> paired = split();
					String[] ops = paired.getKey();
					char sym = paired.getValue();
					
					OperationResult res = 
							binaryOperator(sym, 
									Integer.parseInt(ops[0]), 
									Integer.parseInt(ops[1]));
					
					if(res.isDivByZero()) {
						totalOpStr = "";
						divByZero = true;
						emptyNumberState();
					} else {
						totalOpStr = Integer.toString(res.getRes());
						leastNumberState();
					}
				} else {
					totalOpStr += evText;
					if(opSymLookup(totalOpStr) > 0)
						binaryOpSpecifiedState(false);
				}
			}
			display();
		}
	};
}
