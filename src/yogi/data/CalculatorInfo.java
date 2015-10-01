package yogi.data;

import java.awt.Color;
import java.util.HashMap;

public class CalculatorInfo
{
	public enum CalculatorType
	{
		NORMAL,
	}

	public class ButtonInfo
	{
		public String label;
		public Color color;
		public int[] grids;
		public char[] keys;
	}
	
	public CalculatorType type;
	public int row;
	public int col;
	public HashMap<String, ButtonInfo> buttonMap;
	
	public CalculatorInfo()
	{
		buttonMap = new HashMap<String, ButtonInfo>();
	}
	
	public void AddButton(String label, Color color, int[] grids, char[] keys)
	{
		ButtonInfo buttonInfo;
		if (buttonMap.containsKey(label))
		{
			buttonInfo = buttonMap.get(label);
		}
		else
		{
			buttonInfo = new ButtonInfo();
			buttonMap.put(label, buttonInfo);
		}
		
		buttonInfo.label = label;
		buttonInfo.color = color;
		buttonInfo.grids = grids;
		buttonInfo.keys = keys;
	}
}