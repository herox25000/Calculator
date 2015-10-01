package yogi;

import yogi.data.CalculatorInfo.CalculatorType;
import yogi.ui.UICalculator;
import yogi.ui.UICalculator.OperaEvent;
import yogi.ui.UICalculator.OperaListener;

public final class Calculator implements OperaListener
{
	//操作类型
	public enum OperaType
	{
		NONE,		//0 无
		AC,			//1 归位
		NUMBER,		//2 数字
		RESULT,		//3 结果
		OPP,		//4 相反
		ADD,		//5 加法
		DEC,		//6 减法
		MUL,		//7 乘法
		DIV,		//8 除法
		MOD,		//9 取余
	}
	
	public enum OperaState
	{
		NUMBER1,
		NUMBER2,
		OPERA,
		RESULT,
	}
	
	private static Calculator s_Instance;
	public static Calculator getInstance()
	{
		if (null == s_Instance)
		{
			s_Instance = new Calculator();
		}
		
		return s_Instance;
	}
	
	private UICalculator m_UICalculator;

	private OperaState m_OperaState;
	private float m_Number1;
	private float m_Number2;
	private OperaType m_OperaType;
	
	private Calculator()
	{
		m_UICalculator = new UICalculator(CalculatorType.NORMAL);
		m_UICalculator.addOperaListener(this);

		m_OperaState = OperaState.NUMBER1;
		m_Number1 = 0;
		m_Number2 = 0;
		m_OperaType = OperaType.NONE;
	}
	
	public void setType(CalculatorType type)
	{
		m_UICalculator.setType(type);
	}

	public void operaHandle(OperaEvent e)
	{
		switch (e.operaType)
		{
		case AC:
			m_OperaState = OperaState.NUMBER1;
			m_Number1 = 0;
			m_Number2 = 0;
			m_OperaType = OperaType.NONE;
			m_UICalculator.setScreenText(String.valueOf(m_Number1));
			
			break;
		case NUMBER:
			if (OperaState.NUMBER1 == m_OperaState
				|| OperaState.NUMBER2 == m_OperaState)
			{
				if (e.arg.equals(".")
						&& m_UICalculator.getScreenText().contains("."))
				{
					break;
				}
				
				m_UICalculator.setScreenText(m_UICalculator.getScreenText() + e.arg);
			}
			else
			{
				m_UICalculator.setScreenText(e.arg);
				m_OperaState = OperaState.OPERA == m_OperaState ? OperaState.NUMBER2 : OperaState.NUMBER1;
			}
			
			break;
		case OPP:
			String text = m_UICalculator.getScreenText();
			if (text.contains("-"))
			{
				text = m_UICalculator.getScreenText().substring(1);
			}
			else
			{
				text = "-" + m_UICalculator.getScreenText();
			}
			m_UICalculator.setScreenText(text);
			
			break;
		case RESULT:
			if (OperaState.RESULT != m_OperaState
				&& OperaState.NUMBER2 != m_OperaState)
			{
				break;
			}
			
			if (OperaState.NUMBER2 == m_OperaState)
			{
				m_Number2 = Float.valueOf(m_UICalculator.getScreenText());
			}
			m_Number1 = Calculate(m_Number1, m_Number2, m_OperaType);
			m_UICalculator.setScreenText(String.valueOf(m_Number1));
			m_OperaState = OperaState.RESULT;
			
			break;
		default:
			if (OperaState.RESULT == m_OperaState
				|| OperaState.NUMBER1 == m_OperaState)
			{
				m_Number1 = Float.valueOf(m_UICalculator.getScreenText());
				m_OperaType = e.operaType;
				m_OperaState = OperaState.OPERA;
			}
			
			break;
		}
	}
	
	private float Calculate(float number1, float number2, OperaType operaType)
	{
		System.out.println(number1 + "," + number2 + "," + operaType);
		
		switch (operaType)
		{
		case ADD:
			return number1 + number2;
		case DEC:
			return number1 - number2;
		case MUL:
			return number1 * number2;
		case DIV:
			return number1 / number2;
		case MOD:
			return number1 % number2;
		default:
			break;
		}
		
		return number1;
	}
}