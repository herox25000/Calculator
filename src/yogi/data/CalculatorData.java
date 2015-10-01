package yogi.data;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;

import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.RequestingUserName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import yogi.data.CalculatorInfo.CalculatorType;

public final class CalculatorData
{
	public final String PATH = "res/CalculatorConfig.xml";
	public final String SPLIT = ",";
	
	private static CalculatorData s_Instance;
	public static CalculatorData GetInstance()
	{
		if (null == s_Instance)
		{
			s_Instance = new CalculatorData();
		}
		
		return s_Instance;
	}
	
	public Document document;
	private HashMap<CalculatorType, CalculatorInfo> m_DataMap;

	private CalculatorData()
	{
		m_DataMap = new HashMap<CalculatorType, CalculatorInfo>();
		
		File file = new File(PATH);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try
		{
			db = dbf.newDocumentBuilder();
			document = db.parse(file);
			parser(document.getDocumentElement());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public CalculatorInfo query(CalculatorType type)
	{
		if (!m_DataMap.containsKey(type))
		{
			return null;
		}
		
		return m_DataMap.get(type);
	}
	
	public void parser(Element root)
	{
		NodeList calculatorNode = root.getElementsByTagName("calculator");
		for (int i = calculatorNode.getLength(); --i >= 0; )
		{
			Element calculatorElement = (Element)calculatorNode.item(i);

			CalculatorInfo calculatorInfo = new CalculatorInfo();
			calculatorInfo.type = CalculatorType.values()[Integer.valueOf(calculatorElement.getAttribute("type"))];
			calculatorInfo.row = Integer.valueOf(calculatorElement.getAttribute("row"));
			calculatorInfo.col = Integer.valueOf(calculatorElement.getAttribute("col"));
			
			NodeList buttonNode = calculatorElement.getElementsByTagName("button");
			for (int j = buttonNode.getLength(); --j >= 0; )
			{
				Element buttonElement = (Element)buttonNode.item(j);
				String label = buttonElement.getAttribute("label");
				Color color = parserColor(buttonElement.getAttribute("color"));
				int[] grids = parserIntArray(buttonElement.getAttribute("grids"));
				char[] keys = buttonElement.getAttribute("keys").toCharArray();
				
				calculatorInfo.AddButton(label, color, grids, keys);
			}
			
			if (m_DataMap.containsKey(calculatorInfo.type))
			{
				m_DataMap.remove(calculatorInfo.type);
			}
			
			m_DataMap.put(calculatorInfo.type, calculatorInfo);
		}
	}
	
	private int[] parserIntArray(String value)
	{
		String[] splits = value.split(SPLIT);
		int[] array = new int[splits.length];
		for (int i = array.length; --i >= 0; )
		{
			array[i] = Integer.valueOf(splits[i]);
		}
		
		return array;
	}
	
	private Color parserColor(String value)
	{
		String[] splits = value.split(SPLIT);
		if (splits.length != 3)
		{
			return Color.white;
		}

		int r = Integer.valueOf(splits[0]);
		int g = Integer.valueOf(splits[1]);
		int b = Integer.valueOf(splits[2]);
		Color color = new Color(r, g, b);
		
		return color;
	}
}