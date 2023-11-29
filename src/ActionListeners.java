import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class ActionListeners {


	public static class NumberListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				Calculator.numbers(e);
			}
			catch (NumberFormatException e2) {
				Calculator.operandField.setText("Error: Reenter Number.");
			}
		}
	}
	public static class OperationListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();

			if (actionCommand.matches("[+\\-*/]") ) {
				Calculator.operations(actionCommand);
				return;
			}

			if (actionCommand.charAt(0) == '(' || actionCommand.charAt(0) == ')') {
				Calculator.addParenthesis(actionCommand);
				return;
			}

			if (actionCommand.charAt(0) == '=' ) {
				Calculator.sum();
				return;
			}

		}
	}




}
