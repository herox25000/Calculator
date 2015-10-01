package yogi.ui;

import java.awt.Container;
import java.awt.Label;

public class UIDisplay
{
	private Label m_Label;
	
	public UIDisplay(Container container)
	{
		m_Label = new java.awt.Label();
		container.add(m_Label);
	}
	
	public String getText()
	{
		return m_Label.getText();
	}
	
	public void setText(String text)
	{
		m_Label.setText(text);
	}

	public void setBounds(int x, int y, int width, int height)
	{
		m_Label.setBounds(x, y, width, height);
	}
}