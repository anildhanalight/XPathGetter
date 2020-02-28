package mainPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

public class generateXPaths {
	String tagname;
	String xpath = null;
	String name_attrib;
	String class_attrib;
	String text;

	boolean browser = false;

	private WebElement lastElem = null;
	private String lastBorder = null;

	private static final String SCRIPT_GET_ELEMENT_BORDER = "arguments[0].style.border = '3px solid red'";
	private static final String SCRIPT_UNHIGHLIGHT_ELEMENT = "arguments[0].style.border = 'none'";

	public static String systempath = System.getProperty("user.home") + "/Desktop";
	String LINE_SEPARATOR = "\r\n";

	public WebElement highlightElement(String abc, WebDriver driver) {
		WebElement elem = driver.findElement(By.xpath(abc));
		unhighlightLast(driver);

		// remember the new element
		lastElem = elem;
		lastBorder = (String) (((JavascriptExecutor) driver).executeScript(SCRIPT_GET_ELEMENT_BORDER, elem));
		// if(driver instanceof JavascriptExecutor)
		// {
		// ((JavascriptExecutor)driver).executeScript("arguments[0].style.border
		// = '3px solid red'", elem);
		// }
		return elem;
	}

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

	public WebDriver gotoURL(WebDriver driver, String url,Combo iframe) {
		iframe.removeAll();
		// try{
		//
		// driver.get(url);
		// }
		// catch(Exception e)
		// {
		// driver = new ChromeDriver();
		// driver.get(url);
		// }
		if (browser == false) {
			driver = new ChromeDriver();
			driver.get(url);
			browser = true;
		} else {
			driver.get(url);
		}
		
		List<WebElement> list = driver.findElements(By.xpath("//iframe"));
		
		if (list.size() > 0)
		{
			for(int i=0;i<list.size();i++)
			iframe.add("iframe "+i+1);
		}
		
		return driver;

	}

	public ArrayList<String> generate(WebDriver mainDriver, ProgressBar progressBar, Label status,Combo iframe) throws Exception {
		status.setText("In Progress...");
		iframe.removeAll();
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
		
		List<WebElement> list = mainDriver.findElements(By.xpath("//iframe"));
		
		if (list.size() > 0)
		{
			for(int i=0;i<list.size();i++)
			iframe.add("iframe "+i+1);
		}
		
		return (arr);

		// for (int j=0; j<arr.size();j++)
		// {
		// System.out.println(arr.get(j)+"\n");
		// }

	}

	public void exportToKatalon(WebDriver driver, Text xpathtraverse, String elementXpath, int filenumber)
			throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String elementName = null;
		String elementType = null;
		String tagName = driver.findElement(By.xpath(elementXpath)).getTagName();
		String typeAttrib = null;
		if (tagName.equals("input")) {
			typeAttrib = driver.findElement(By.xpath(elementXpath)).getAttribute("type");
			if(typeAttrib.equals("submit")) 
			{
				//Btn
			}
			else if(typeAttrib.equals("text"))
			{
				//TxtBox
			}
			else if(typeAttrib.equals("checkbox"))
			{
				//Chk
			}
			else if(typeAttrib.equals("checkbox"))
			{
				//Rad
			}
		} else if (tagName.equals("select")) {
			elementType = "Drpdn";
		}
		else
		{
			//Txt
			//Link
		}
		// input[@type="button"]

		File folder = new File(systempath + "/ObjectRepo/" + xpathtraverse.getText());
		folder.mkdir();

		// if (!folder.exists()) {
		// try {
		// folder.createNewFile();
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// }

		PrintWriter writer = new PrintWriter(
				systempath + "/ObjectRepo/" + xpathtraverse.getText() + elementName + "_" + elementType + ".rs",
				"UTF-8");
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
