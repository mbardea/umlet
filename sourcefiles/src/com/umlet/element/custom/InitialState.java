package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class InitialState extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.fillOval(0, 0, this.getSize().width, this.getSize().height);
	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes
}
