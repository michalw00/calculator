import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListeners {




	public static class numberListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				Calculator.numbers(e);
			}
			catch (NumberFormatException e2) {
				Calculator.operandField.setText("Error: Reenter Number.");
			}
		}
	}
	public static class operationListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Calculator.operations(e);
		}
	}




}
