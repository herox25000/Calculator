package yogi;

import yogi.ui.UICalculator;

public final class Calculator
{
	private static Calculator s_Instance;
	public static Calculator GetInstance()
	{
		if (null == s_Instance)
		{
			s_Instance = new Calculator();
		}
		
		return s_Instance;
	}
	
	private UICalculator m_UICalculator;
	
	private Calculator()
	{
		m_UICalculator = new UICalculator(320, 480);
	}
}