package yogi.ui;

import java.awt.Frame;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import yogi.data.CalculatorData;
import yogi.data.CalculatorInfo;
import yogi.data.CalculatorInfo.ButtonInfo;
import yogi.data.CalculatorInfo.CalculatorType;
import yogi.ui.UIButton.ButtonEvent;
import yogi.ui.UIButton.ButtonListener;

public class UICalculator implements ComponentListener, KeyListener, ButtonListener
{
	private Frame m_Frame;
	private Panel m_DisplayPanel;
	private Panel m_ButtonPanel;
	private UIDisplay m_Display;

	private CalculatorInfo m_CalculatorInfo;

	private HashMap<UIButton, ButtonInfo> m_ButtonInfoMap;
	private HashMap<Integer, UIButton> m_ButtonKeyMap;

	public UICalculator(CalculatorType type)
	{
		m_Frame = new java.awt.Frame("Calculator");
		m_Frame.setVisible(true);
		m_Frame.addComponentListener(this);
		
		m_DisplayPanel = new java.awt.Panel();
		m_Frame.add(m_DisplayPanel);
		
		m_ButtonPanel = new java.awt.Panel();
		m_Frame.add(m_ButtonPanel);
		
		m_Display = new UIDisplay(m_DisplayPanel);
		
		m_CalculatorInfo = CalculatorData.GetInstance().query(type);
		
		m_ButtonInfoMap = new HashMap<UIButton, ButtonInfo>();
		m_ButtonKeyMap = new HashMap<Integer, UIButton>();
		
		Iterator<Entry<String, ButtonInfo>> iterator = m_CalculatorInfo.buttonMap.entrySet().iterator();
		while (iterator.hasNext())
		{
			Entry<String, ButtonInfo> entry = iterator.next();
			ButtonInfo buttonInfo = entry.getValue();
			
			UIButton button = new UIButton(m_ButtonPanel);
			button.setLabel(buttonInfo.label);
			button.setColor(buttonInfo.color);
			button.addKeyListener(this);
			button.addButtonListener(this);
			
			m_ButtonInfoMap.put(button, buttonInfo);

			for (int i = buttonInfo.keys.length; --i >= 0; )
			{
				int key = buttonInfo.keys[i];
				if (m_ButtonKeyMap.containsKey(key))
				{
					m_ButtonKeyMap.replace(key, button);
				}
				else
				{
					m_ButtonKeyMap.put(key, button);
				}
			}
		}
		
		setSize(m_CalculatorInfo.width, m_CalculatorInfo.height);
	}

	public void setSize(int width, int height)
	{
		m_Frame.setSize(width, height);

		m_DisplayPanel.setLocation(0, 0);
		m_DisplayPanel.setSize(m_Frame.getWidth(), (int)(m_Frame.getHeight() * 0.2f));

		m_ButtonPanel.setLocation(0, m_DisplayPanel.getHeight());
		m_ButtonPanel.setSize(m_Frame.getWidth(), m_Frame.getHeight() - m_DisplayPanel.getHeight());
		
		m_Display.setBounds(0, 0, m_Frame.getWidth(), m_Frame.getHeight() - m_ButtonPanel.getHeight());

		int w = m_ButtonPanel.getWidth() / m_CalculatorInfo.col;
		int h = m_ButtonPanel.getHeight() / m_CalculatorInfo.row;
		Iterator<Entry<UIButton, ButtonInfo>> iterator = m_ButtonInfoMap.entrySet().iterator();
		while (iterator.hasNext())
		{
			Entry<UIButton, ButtonInfo> entry = iterator.next();
			UIButton button = entry.getKey();
			ButtonInfo buttonInfo = entry.getValue();
			Rectangle rect = getBoundsInGrids(w, h, m_CalculatorInfo.col, m_CalculatorInfo.row, buttonInfo.grids);
			
			button.setBounds(rect.x, rect.y, rect.width, rect.height);
		}
		
		m_DisplayPanel.repaint();
		m_ButtonPanel.repaint();
	}
	
	private Rectangle getBoundsInGrids(int w, int h, int col, int row, int[] grids)
	{
		Rectangle rect = new Rectangle();
		int minCol = Integer.MAX_VALUE,
				maxCol = Integer.MIN_VALUE,
				minRow = Integer.MAX_VALUE,
				maxRow = Integer.MIN_VALUE;
		for (int i = grids.length; --i >= 0; )
		{
			int grid = grids[i];
			int c = grid % col;
			int r = grid / col;
			
			if (c < minCol)
			{
				minCol = c;
			}
			if (c > maxCol)
			{
				maxCol = c;
			}
			
			if (r < minRow)
			{
				minRow = r;
			}
			if (r > maxRow)
			{
				maxRow = r;
			}
		}
		
		rect.x = w * minCol;
		rect.y = h * minRow;
		rect.width = w * (maxCol - minCol + 1);
		rect.height = h * (maxRow - minRow + 1);
		
		return rect;
	}
	
	public void componentResized(ComponentEvent e)
	{
		setSize(m_Frame.getWidth(), m_Frame.getHeight());
	}

	public void componentMoved(ComponentEvent e)
	{
	}

	public void componentShown(ComponentEvent e)
	{
	}

	public void componentHidden(ComponentEvent e)
	{
	}

	public void keyTyped(KeyEvent e)
	{
	}

	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getExtendedKeyCode();
		if (!m_ButtonKeyMap.containsKey(keyCode))
		{
			return;
		}
		
		UIButton button = m_ButtonKeyMap.get(keyCode);
		
		m_Display.setText(m_Display.getText() + button.getLabel());
	}

	public void keyReleased(KeyEvent e)
	{
	}

	public void buttonPressed(ButtonEvent e)
	{
		UIButton button = e.button;
		
		m_Display.setText(m_Display.getText() + button.getLabel());
	}
}