package yogi.ui;

import java.awt.Frame;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import yogi.Calculator.OperaType;
import yogi.data.CalculatorData;
import yogi.data.CalculatorInfo;
import yogi.data.CalculatorInfo.ButtonInfo;
import yogi.data.CalculatorInfo.CalculatorType;
import yogi.ui.UICalculatorButton.ButtonEvent;
import yogi.ui.UICalculatorButton.ButtonListener;

public class UICalculator implements ComponentListener, KeyListener, ButtonListener
{
	public class OperaEvent
	{
		public OperaType operaType;
		public String arg;
	}
	
	public interface OperaListener extends EventListener
	{
		public void operaHandle(OperaEvent e);
	}
	
	private OperaListener m_OperaListener;
	
	private CalculatorInfo m_CalculatorInfo;

	private Frame m_Frame;
	private Panel m_ScreenPanel;
	private Panel m_ButtonPanel;
	private UICalculatorScreen m_Screen;

	private HashMap<UICalculatorButton, ButtonInfo> m_ButtonInfoMap;
	private HashMap<Integer, UICalculatorButton> m_ButtonKeyMap;

	public UICalculator(CalculatorType type)
	{
		m_Frame = new java.awt.Frame("Calculator");
		m_Frame.setVisible(true);
		m_Frame.addComponentListener(this);
		
		m_ScreenPanel = new java.awt.Panel();
		m_Frame.add(m_ScreenPanel);
		
		m_ButtonPanel = new java.awt.Panel();
		m_Frame.add(m_ButtonPanel);
		
		m_Screen = new UICalculatorScreen(m_ScreenPanel);
		
		m_ButtonInfoMap = new HashMap<UICalculatorButton, ButtonInfo>();
		m_ButtonKeyMap = new HashMap<Integer, UICalculatorButton>();
		
		setType(type);
	}
	
	public void addOperaListener(OperaListener listener)
	{
		m_OperaListener = listener;
	}
	
	public void setScreenText(String text)
	{
		m_Screen.setText(text);
	}
	
	public String getScreenText()
	{
		return m_Screen.getText();
	}
	
	public void setType(CalculatorType type)
	{
		if (null != m_CalculatorInfo && m_CalculatorInfo.type == type)
		{
			return;
		}
		
		m_CalculatorInfo = CalculatorData.getInstance().query(type);
		
		m_ButtonPanel.removeAll();
		m_ButtonInfoMap.clear();
		m_ButtonKeyMap.clear();

		Iterator<Entry<String, ButtonInfo>> iterator = m_CalculatorInfo.buttonMap.entrySet().iterator();
		while (iterator.hasNext())
		{
			Entry<String, ButtonInfo> entry = iterator.next();
			ButtonInfo buttonInfo = entry.getValue();
			
			UICalculatorButton button = new UICalculatorButton(m_ButtonPanel);
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

		m_ScreenPanel.setLocation(0, 0);
		m_ScreenPanel.setSize(m_Frame.getWidth(), (int)(m_Frame.getHeight() * m_CalculatorInfo.screen));

		m_ButtonPanel.setLocation(0, m_ScreenPanel.getHeight());
		m_ButtonPanel.setSize(m_Frame.getWidth(), m_Frame.getHeight() - m_ScreenPanel.getHeight());
		
		m_Screen.setBounds(0, 0, m_Frame.getWidth(), m_Frame.getHeight() - m_ButtonPanel.getHeight());

		int w = m_ButtonPanel.getWidth() / m_CalculatorInfo.col;
		int h = m_ButtonPanel.getHeight() / m_CalculatorInfo.row;
		Iterator<Entry<UICalculatorButton, ButtonInfo>> iterator = m_ButtonInfoMap.entrySet().iterator();
		while (iterator.hasNext())
		{
			Entry<UICalculatorButton, ButtonInfo> entry = iterator.next();
			UICalculatorButton button = entry.getKey();
			ButtonInfo buttonInfo = entry.getValue();
			Rectangle rect = getBoundsInGrids(w, h, m_CalculatorInfo.col, m_CalculatorInfo.row, buttonInfo.grids);
			
			button.setBounds(rect.x, rect.y, rect.width, rect.height);
		}
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
		if(null == m_OperaListener)
		{
			return;
		}
		
		int keyCode = e.getExtendedKeyCode();
		if (!m_ButtonKeyMap.containsKey(keyCode))
		{
			return;
		}
		
		UICalculatorButton button = m_ButtonKeyMap.get(keyCode);
		if (!m_ButtonInfoMap.containsKey(button))
		{
			return;
		}
		
		ButtonInfo buttonInfo = m_ButtonInfoMap.get(button);
		
		OperaEvent operaEvent = new OperaEvent();
		operaEvent.operaType = buttonInfo.operaType;
		operaEvent.arg = buttonInfo.label;
		
		m_OperaListener.operaHandle(operaEvent);
	}

	public void keyReleased(KeyEvent e)
	{
	}

	public void buttonPressed(ButtonEvent e)
	{
		if(null == m_OperaListener)
		{
			return;
		}
		
		UICalculatorButton button = e.button;
		if (!m_ButtonInfoMap.containsKey(button))
		{
			return;
		}
		
		ButtonInfo buttonInfo = m_ButtonInfoMap.get(button);
		
		OperaEvent operaEvent = new OperaEvent();
		operaEvent.operaType = buttonInfo.operaType;
		operaEvent.arg = buttonInfo.label;
		
		m_OperaListener.operaHandle(operaEvent);
	}
}