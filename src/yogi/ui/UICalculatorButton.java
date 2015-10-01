package yogi.ui;

import java.awt.Container;
import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.EventListener;

public class UICalculatorButton implements ActionListener
{
	public class ButtonEvent
	{
		public UICalculatorButton button;
	}
	
	public interface ButtonListener extends EventListener
	{
	    public void buttonPressed(ButtonEvent e);
	}
	
	private Button m_Button;
	private ButtonListener m_Listener;
	
	public UICalculatorButton(Container container)
	{
		m_Button = new java.awt.Button();
		m_Button.addActionListener(this);
		
		container.add(m_Button);
	}
	
	public String getLabel()
	{
		return m_Button.getLabel();
	}
	
	public void setLabel(String label)
	{
		m_Button.setLabel(label);
	}
	
	public void setBounds(int x, int y, int width, int height)
	{
		m_Button.setBounds(x, y, width, height);
	}
	
	public Color getColor()
	{
		return m_Button.getBackground();
	}
	
	public void setColor(Color color)
	{
		m_Button.setBackground(color);
	}
	
	public void addKeyListener(KeyListener listener)
	{
		m_Button.addKeyListener(listener);
	}
	
	public void addButtonListener(ButtonListener listener)
	{
		m_Listener = listener;
	}

	public void actionPerformed(ActionEvent e)
	{
		ButtonEvent event = new ButtonEvent();
		event.button = this;
		
		m_Listener.buttonPressed(event);
	}
}