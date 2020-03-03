package mainPackage;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;



public class generateXPaths{
	String tagname;
	String xpath = null;
	String name_attrib;
	String class_attrib;
	String text;
	String tempURL = null;

	boolean browser = false;

	private WebElement lastElem = null;
	private String lastBorder = null;

	private static final String SCRIPT_GET_ELEMENT_BORDER = "arguments[0].style.border = '3px solid red'";
	private static final String SCRIPT_UNHIGHLIGHT_ELEMENT = "arguments[0].style.border = 'none'";

	public static String systempath = System.getProperty("user.home") + "/Desktop";
	String LINE_SEPARATOR = "\r\n";
	
//	public void run(WebDriver driver)
//	{
//		try
//		{
//			tempURL = driver.getCurrentUrl();
//			System.out.println("webpage is open");
//			
//		}
//		catch(Exception e)
//		{
//			System.out.println("webpage is closed");
//			if(browser==true)
//			{
//			driver.quit();
//			browser = false;
//			}
//		}
//	
////		while(browser == true)
//	}
	
	//*******************************************************************************************
	//*************This function is used to highlight the selected XPath element*****************
	//*******************************************************************************************
	
	public WebElement highlightElement(String abc, WebDriver driver) {
		WebElement elem = null;
		try
		{
		elem = driver.findElement(By.xpath(abc));
		unhighlightLast(driver);

		// remember the new element
		lastElem = elem;
		lastBorder = (String) (((JavascriptExecutor) driver).executeScript(SCRIPT_GET_ELEMENT_BORDER, elem));
		// if(driver instanceof JavascriptExecutor)
		// {
		// ((JavascriptExecutor)driver).executeScript("arguments[0].style.border
		// = '3px solid red'", elem);
		// }
		
		}
		catch(Exception e)
		{
			e.printStackTrace();;
		}
		return elem;
	}
	
	//*******************************************************************************************
	//**********This function is used to highlight appropriate frame on selecting it*************
	//*******************************************************************************************
	
	public WebElement highlightElement(WebElement element, WebDriver driver) {
		
		try
		{
		
		unhighlightLast(driver);

		// remember the new element
		lastElem = element;
		lastBorder = (String) (((JavascriptExecutor) driver).executeScript(SCRIPT_GET_ELEMENT_BORDER, element));
		// if(driver instanceof JavascriptExecutor)
		// {
		// ((JavascriptExecutor)driver).executeScript("arguments[0].style.border
		// = '3px solid red'", elem);
		// }
		
		}
		catch(Exception e)
		{
			e.printStackTrace();;
		}
		return element;
	}
	
	//*******************************************************************************************
	//*************This function is used to unhighlight the selected XPath element***************
	//*******************************************************************************************
	
	void unhighlightLast(WebDriver driver) {
		if (lastElem != null) {
			try {
				// if there already is a highlighted element, unhighlight it
				((JavascriptExecutor) driver).executeScript(SCRIPT_UNHIGHLIGHT_ELEMENT, lastElem, lastBorder);
			} catch (StaleElementReferenceException ignored) {
				// the page got reloaded, the element isn't there
			} finally {
				// element either restored or wasn't valid, nullify in both
				// cases
				lastElem = null;
			}
		}
	}
	
	//*******************************************************************************************
	//****************This function is used to navigate to the provided URL**********************
	//*******************************************************************************************
		
	public WebDriver gotoURL(WebDriver driver, String url) {
		
		
		if (browser == false) {
			driver = new ChromeDriver();
			driver.get(url);
			browser = true;
		} else {
			driver.get(url);
		}
		return driver;
		
		

	}

	//*******************************************************************************************
	//*********************This function is used to fetch all iframes****************************
	//*******************************************************************************************
	
	public HashMap iFrameFetch(WebDriver driver,Combo iframe)
	{
		iframe.removeAll();
		List<WebElement> list = driver.findElements(By.xpath("//iframe"));
		HashMap<Integer, WebElement> iFrameElements = new HashMap<Integer, WebElement>();
		if (list.size() > 0)
		{
			int i = 1;
		    for (WebElement element : list) 
		    {
		        iFrameElements.put(i,element);
		        iframe.add(String.valueOf(i));
		        i++;
		    }
			
		}
		
		return iFrameElements;
	}

	//*******************************************************************************************
	//*************This function is used to switch to the appropriate iframe*********************
	//*******************************************************************************************
	
	public void switchIFrame(WebDriver driver,WebElement iFrameElement)
	{
		driver.switchTo().frame(iFrameElement);
	}
	
	//*******************************************************************************************
	//**************This function is used to generate all XPath of a web page********************
	//*******************************************************************************************
	
	public ArrayList<String> generate(WebDriver mainDriver, ProgressBar progressBar, Label status) throws Exception {
		status.setText("In Progress...");
		
		List<WebElement> elements = mainDriver.findElements(By.xpath("//*"));
		ArrayList<String> arr = new ArrayList<String>();
		HashSet hs = new HashSet();
		try {
			progressBar.setMinimum(0);
			progressBar.setMaximum(elements.size());
			for (int i = 0; i < elements.size(); i++) {

				tagname = elements.get(i).getTagName();
				name_attrib = elements.get(i).getAttribute("name");
				class_attrib = elements.get(i).getAttribute("class");
				text = elements.get(i).getText();

				if (tagname.equals("input")) {
					if (name_attrib.length() > 0) {
						xpath = "//input[@name='" + name_attrib + "']";
					} else if (class_attrib.length() > 0) {
						xpath = "//input[@class='" + class_attrib + "']";
					}
				} else if (tagname.equals("option")) {

				} else if (tagname.equals("select")) {
					if (name_attrib.length() > 0) {
						xpath = "//select[@name='" + name_attrib + "']";
					} else if (class_attrib.length() > 0) {
						xpath = "//select[@class='" + class_attrib + "']";
					}
				} else {
					if (!(text.isEmpty() || text.equals(""))) {

						xpath = "//" + tagname + "[contains(text(),\"" + text.replace("'", "\\'").split("\n")[0]
								+ "\")]";
					}
				}
				if (xpath != null) {
					List<WebElement> list = mainDriver.findElements(By.xpath(xpath));
					if (list.size() > 0)
						arr.add(xpath);
				}

				progressBar.setSelection(i + 1);
			}
		} catch (Exception e) {
			status.setText(e.toString());
		}

		hs.addAll(arr);
		arr.clear();
		arr.addAll(hs);
		status.setText("Extraction Complete");
		
		
		
		return (arr);

	}
	
	
	//*******************************************************************************************
	//**************************This function is used to copy XPath******************************
	//*******************************************************************************************
	
	public void copyXPath(String xpath)
	{
		StringSelection stringSelection = new StringSelection(xpath);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}
	
	
	//*******************************************************************************************
	//*********This function is used to copy XPath contents and open New Notepad file************
	//*******************************************************************************************
	
	
	public void exportToNotepad(String[] items, String path)
	{


	
		try {
//				
					PrintWriter writer = new PrintWriter(path,"UTF-8");
					for(int i=0;i<items.length;i++)
					writer.println(items[i]+ LINE_SEPARATOR);
					writer.close();

			
		} 
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		}
		
		

	}
	public String renameString(String elementXpath,WebDriver driver)
	{
		String elementName = driver.findElement(By.xpath(elementXpath)).getText();
		elementName.replaceAll("[^a-zA-Z0-9\\s+]", "");
		String abc[] = elementName.split(" ");
		String finalData="";
		for(int i = 0;i<abc.length;i++)
		{
			if(i==0)
			{
			finalData=finalData+abc[0].toLowerCase();
			}
			else
			{
				abc[i] = abc[i].toLowerCase();
				abc[i] = abc[i].replace(abc[i].charAt(0), (char) (abc[i].charAt(0)-32));
				finalData=finalData+abc[i];
			    
			}
			
		}
		System.out.println("Final String is : "+ finalData);
		return finalData;
	}
	
	//*******************************************************************************************
	//**************************This function is used to export to Katalon***********************
	//*******************************************************************************************
	
	public void exportToKatalon(WebDriver driver, Text xpathtraverse, String elementXpath, int filenumber, String path)
			throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String elementName = null;
		String elementType = null;
		String tagName = driver.findElement(By.xpath(elementXpath)).getTagName();
		String typeAttrib = null;
		String valueAttrib = driver.findElement(By.xpath(elementXpath)).getAttribute("value");
		String nameAttrib = driver.findElement(By.xpath(elementXpath)).getAttribute("name");
		
		if (tagName.equals("input")) 
		{
			if(nameAttrib == null)
			{
				elementName = valueAttrib;
			}
			else
			{
				elementName = nameAttrib;
			}
			
			typeAttrib = driver.findElement(By.xpath(elementXpath)).getAttribute("type");
			if(typeAttrib.equals("submit")) 
			{
				elementType ="Button";
			}
			else if(typeAttrib.equals("text"))
			{
				elementType ="InputBox";
			}
			else if(typeAttrib.equals("checkbox"))
			{
				elementType ="CheckBox";
			}
			else if(typeAttrib.equals("radio"))
			{
				elementType ="RadioButton";
			}
		} 
		else if (tagName.equals("select")) 
		{
			if(nameAttrib == null)
			{
				elementName = valueAttrib;
			}
			else
			{
				elementName = nameAttrib;
			}
			elementType = "DropDown";
		}
		else if (tagName.equals("a"))
		{
			try
			{
				elementName = renameString(elementXpath,driver);
				elementName = elementName.substring(0,15);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				elementName = renameString(elementXpath,driver);
			}
			elementType = "Link";
		}
		else
		{
			try
			{
				elementName = renameString(elementXpath,driver);
				elementName = elementName.substring(0,15);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				elementName = renameString(elementXpath,driver);
			}
			elementType = "Text";
		}
		File file = new File(path+"/ObjectRepo/"+xpathtraverse.getText());
	      //Creating the directory
	      boolean bool = file.mkdir();
		PrintWriter writer = new PrintWriter(path+"/ObjectRepo/"+xpathtraverse.getText()+elementName + "_" + elementType + ".rs","UTF-8");
//		File folder = new File(systempath + "/ObjectRepo/" + xpathtraverse.getText());
//		folder.mkdir();
//
//		PrintWriter writer = new PrintWriter(
//				systempath + "/ObjectRepo/" + xpathtraverse.getText() + elementName + "_" + elementType + ".rs",
//				"UTF-8");
		
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINE_SEPARATOR + "<WebElementEntity>"
				+ LINE_SEPARATOR + "<description></description>" + LINE_SEPARATOR + "<name>" + elementName + "_"
				+ elementType + "</name>" + LINE_SEPARATOR + "<tag></tag>" + LINE_SEPARATOR
				+ "<elementGuidId></elementGuidId>" + LINE_SEPARATOR + "<selectorCollection>" + LINE_SEPARATOR
				+ "<entry>" + LINE_SEPARATOR + "<key>BASIC</key>" + LINE_SEPARATOR + "<value></value>" + LINE_SEPARATOR
				+ "</entry>" + LINE_SEPARATOR + "</selectorCollection>" + LINE_SEPARATOR
				+ "<selectorMethod>BASIC</selectorMethod>" + LINE_SEPARATOR
				+ "<useRalativeImagePath>false</useRalativeImagePath>" + LINE_SEPARATOR + "<webElementProperties>"
				+ LINE_SEPARATOR + "<isSelected>true</isSelected>" + LINE_SEPARATOR
				+ "<matchCondition>equals</matchCondition>");

		writer.println("<name>xpath</name>" + LINE_SEPARATOR + "<type>Main</type>" + LINE_SEPARATOR + "<value>"
				+ elementXpath + "</value>");

		writer.println("</webElementProperties>" + LINE_SEPARATOR + "</WebElementEntity>");

		writer.close();
		++filenumber;

	}
}
