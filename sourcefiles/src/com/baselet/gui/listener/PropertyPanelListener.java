package com.baselet.gui.listener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.SwingUtilities;

import com.baselet.control.Main;
import com.baselet.diagram.CustomPreviewHandler;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.command.ChangeState;
import com.baselet.diagram.command.CustomCodePropertyChanged;
import com.baselet.diagram.command.HelpPanelChanged;
import com.baselet.element.GridElement;

public class PropertyPanelListener implements KeyListener, FocusListener {

	@Override public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == '\u001b') { // ESC Key: Leaves the Property Panel
			Main.getInstance().getGUI().requestFocus();
		} else if (!e.isActionKey()) {
			final Runnable beeper = new Runnable() {
				@Override
				public void run() {updateGridElement();}
			};
			SwingUtilities.invokeLater(beeper);
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		updateGridElement(); // Workaround which is needed to make selection of a autocompletion element via mouse work
	}

	@Override public void keyReleased(KeyEvent e) {}
	@Override public void focusLost(FocusEvent e) {}
	@Override public void keyPressed(KeyEvent e) {}

	protected void updateGridElement() {
		GridElement gridElement = Main.getInstance().getEditedGridElement();
		String s = Main.getInstance().getGUI().getPropertyPane().getText();
		DiagramHandler handler = Main.getInstance().getDiagramHandler();

		if (gridElement != null) {
			//only create command if changes were made
			if (!s.equals(gridElement.getPanelAttributes())) {
				int newCaretPos = Main.getInstance().getGUI().getPropertyPane().getCaretPosition();
				int oldCaretPos = newCaretPos - (s.length()-gridElement.getPanelAttributes().length());		

				if (gridElement.getHandler() instanceof CustomPreviewHandler) {
					gridElement.getHandler().getController().executeCommand(new CustomCodePropertyChanged(gridElement.getPanelAttributes(), s, oldCaretPos, newCaretPos));
				} else {
					gridElement.getHandler().getController().executeCommand(new ChangeState(gridElement, gridElement.getPanelAttributes(), s, oldCaretPos, newCaretPos));
				}
			}
		}
		else if (handler != null && !s.equals(handler.getHelpText())) { // help panel has been edited
			handler.getController().executeCommand(new HelpPanelChanged(s));
		}

		// Scrollbars must be updated cause some entities can grow out of screen border by typing text inside (eg: autoresize custom elements)
		if (handler != null) handler.getDrawPanel().updatePanelAndScrollbars();
	}
}
