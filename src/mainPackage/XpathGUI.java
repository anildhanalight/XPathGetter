package mainPackage;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;



public class XpathGUI {
	private static Text url;
	private static Text xpathtraverse;
	static WebDriver driver = null;
	static ArrayList<String> arr = null;
	static HashMap<Integer, WebElement> iFrameElements = new HashMap<Integer, WebElement>();
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println(System.getProperty("user.dir")+"\\Drivers\\chromedriver.exe");
		System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\Drivers\\chromedriver.exe");
		
		generateXPaths generateXPathsUtil = new generateXPaths();
		Display display = Display.getDefault();
		Shell shlXpath = new Shell();
		shlXpath.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {
				driver.quit();
			}
		});
		shlXpath.setSize(640, 570);
		shlXpath.setText("XPath Getter");
		shlXpath.setLayout(new FormLayout());
		
		Label lblEnterUrl = new Label(shlXpath, SWT.NONE);
		FormData fd_lblEnterUrl = new FormData();
		fd_lblEnterUrl.top = new FormAttachment(0, 30);
		fd_lblEnterUrl.left = new FormAttachment(0, 10);
		lblEnterUrl.setLayoutData(fd_lblEnterUrl);
		lblEnterUrl.setText("Enter URL:");
		
		Label lblXpathCurrentPath = new Label(shlXpath, SWT.NONE);
		FormData fd_lblXpathCurrentPath = new FormData();
		fd_lblXpathCurrentPath.top = new FormAttachment(lblEnterUrl, 15);
		fd_lblXpathCurrentPath.left = new FormAttachment(lblEnterUrl, 0, SWT.LEFT);
		lblXpathCurrentPath.setLayoutData(fd_lblXpathCurrentPath);
		lblXpathCurrentPath.setText("XPath Traverse Path:");
		
		url = new Text(shlXpath, SWT.BORDER);
		FormData fd_url = new FormData();
		fd_url.left = new FormAttachment(lblEnterUrl, 70);
		url.setLayoutData(fd_url);
		
		Button btnOpenUrl = new Button(shlXpath, SWT.NONE);
		fd_url.right = new FormAttachment(btnOpenUrl, -6);
	
	
		FormData fd_btnOpenUrl = new FormData();
		fd_btnOpenUrl.top = new FormAttachment(lblEnterUrl, -5, SWT.TOP);
		fd_btnOpenUrl.left = new FormAttachment(0, 406);
		btnOpenUrl.setLayoutData(fd_btnOpenUrl);
		btnOpenUrl.setText("Navigate to URL");
		
		xpathtraverse = new Text(shlXpath, SWT.BORDER);
		fd_url.bottom = new FormAttachment(xpathtraverse, -7);
		FormData fd_xpathtraverse = new FormData();
		fd_xpathtraverse.left = new FormAttachment(lblXpathCurrentPath, 17);
		xpathtraverse.setLayoutData(fd_xpathtraverse);
		
		Button btnGetAllXpaths = new Button(shlXpath, SWT.NONE);
		fd_xpathtraverse.bottom = new FormAttachment(btnGetAllXpaths, -6);
		
		
		FormData fd_btnGetAllXpaths = new FormData();
		fd_btnGetAllXpaths.top = new FormAttachment(0, 84);
		btnGetAllXpaths.setLayoutData(fd_btnGetAllXpaths);
		btnGetAllXpaths.setText("Get All XPaths");
		
		List list = new List(shlXpath, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		
		FormData fd_list = new FormData();
		fd_list.left = new FormAttachment(lblEnterUrl, 0, SWT.LEFT);
		list.setLayoutData(fd_list);
		
		ProgressBar progressBar = new ProgressBar(shlXpath, SWT.NONE);
		fd_list.bottom = new FormAttachment(progressBar, -6);
		fd_list.right = new FormAttachment(progressBar, 0, SWT.RIGHT);
		FormData fd_progressBar = new FormData();
		fd_progressBar.bottom = new FormAttachment(100, -10);
		fd_progressBar.right = new FormAttachment(100, -10);
		progressBar.setLayoutData(fd_progressBar);
		
		Label status = new Label(shlXpath, SWT.NONE);
		FormData fd_status = new FormData();
		fd_status.top = new FormAttachment(0, 506);
		fd_status.bottom = new FormAttachment(100, -10);
		fd_status.right = new FormAttachment(lblEnterUrl, 127);
		fd_status.left = new FormAttachment(0, 10);
		fd_status.width = 2;
		status.setLayoutData(fd_status);
		
		status.setText("Ready...");
		
		Button btnExportToKatalon = new Button(shlXpath, SWT.NONE);
		
		
		FormData fd_btnExportToKatalon = new FormData();
		fd_btnExportToKatalon.top = new FormAttachment(btnGetAllXpaths, 0, SWT.TOP);
		fd_btnExportToKatalon.left = new FormAttachment(btnGetAllXpaths, 6);
		btnExportToKatalon.setLayoutData(fd_btnExportToKatalon);
		btnExportToKatalon.setText("Export to Katalon");
		
		Button btnCopy = new Button(shlXpath, SWT.NONE);
		
		fd_btnGetAllXpaths.left = new FormAttachment(btnCopy, 6);
		FormData fd_btnCopy = new FormData();
		fd_btnCopy.top = new FormAttachment(lblXpathCurrentPath, 9);
		fd_btnCopy.left = new FormAttachment(lblEnterUrl, 0, SWT.LEFT);
		btnCopy.setLayoutData(fd_btnCopy);
		btnCopy.setText("Copy XPath");
		
		Button btnExportToNotepad = new Button(shlXpath, SWT.NONE);
		
		fd_xpathtraverse.right = new FormAttachment(btnExportToNotepad, 0, SWT.RIGHT);
		FormData fd_btnExportToNotepad = new FormData();
		fd_btnExportToNotepad.top = new FormAttachment(btnGetAllXpaths, 0, SWT.TOP);
		fd_btnExportToNotepad.left = new FormAttachment(btnExportToKatalon, 6);
		btnExportToNotepad.setLayoutData(fd_btnExportToNotepad);
		btnExportToNotepad.setText("Export to Notepad");
		
		Combo iframe = new Combo(shlXpath, SWT.NONE);
		
		FormData fd_iframe = new FormData();
		fd_iframe.left = new FormAttachment(xpathtraverse, 10);
		fd_iframe.right = new FormAttachment(100, -10);
		fd_iframe.top = new FormAttachment(lblXpathCurrentPath, -3, SWT.TOP);
		iframe.setLayoutData(fd_iframe);
		
		Button btnFindIframes = new Button(shlXpath, SWT.NONE);
		
		FormData fd_btnFindIframes = new FormData();
		fd_btnFindIframes.bottom = new FormAttachment(btnGetAllXpaths, 0, SWT.BOTTOM);
		fd_btnFindIframes.left = new FormAttachment(btnOpenUrl, 0, SWT.LEFT);
		btnFindIframes.setLayoutData(fd_btnFindIframes);
		btnFindIframes.setText("Find iFrames");
		
		Button btnSwitchToIframe = new Button(shlXpath, SWT.NONE);
		
		FormData fd_btnSwitchToIframe = new FormData();
		fd_btnSwitchToIframe.top = new FormAttachment(btnGetAllXpaths, 0, SWT.TOP);
		fd_btnSwitchToIframe.left = new FormAttachment(btnFindIframes, 6);
		btnSwitchToIframe.setLayoutData(fd_btnSwitchToIframe);
		btnSwitchToIframe.setText("Switch To iFrame");
		
		Button btnSwitchToDefault = new Button(shlXpath, SWT.NONE);
		
		fd_list.top = new FormAttachment(btnSwitchToDefault, 6);
		FormData fd_btnSwitchToDefault = new FormData();
		fd_btnSwitchToDefault.top = new FormAttachment(btnFindIframes, 6);
		fd_btnSwitchToDefault.right = new FormAttachment(100, -47);
		btnSwitchToDefault.setLayoutData(fd_btnSwitchToDefault);
		btnSwitchToDefault.setText("Switch To Default Content");
		
		//*******************************************************************************************
		//**************************This function is used to export to Katalon***********************
		//*******************************************************************************************
		
		btnExportToKatalon.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseDown(MouseEvent e) 
			{
				int filenumber = 0;
				/*for(all elements in list)
				if elements are
				*/ 
				String[] items= list.getItems();
				
				
				String path = "C:\\Users\\a1001362\\OneDrive - Alight Solutions\\Desktop";
				
				for (int i=0; i < items.length; i++) 
				{
			
				try {
					generateXPathsUtil.exportToKatalon(driver, xpathtraverse, items[i], filenumber,path);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				}
				System.out.println("The No. of files accessed are: "+filenumber);
				//Call Function to Export to Katalon
			}
		});
		
		//*******************************************************************************************
		//**************************This function is used to copy XPath******************************
		//*******************************************************************************************
		
		btnCopy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				String xpath = list.getItem(list.getSelectionIndex());
				System.out.println("Item selected: "+xpath+"\n");
				generateXPathsUtil.copyXPath(xpath);
			}
		});
		
		
		//*******************************************************************************************
		//*********This function is used to copy XPath contents and open New Notepad file************
		//*******************************************************************************************
		
		btnExportToNotepad.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				
				 FileDialog dialog = new FileDialog(shlXpath, SWT.SAVE);
				    dialog.setFilterNames(new String[] { "Text Files", "All Files (*.*)" });
				    dialog.setFilterExtensions(new String[] { "*.txt", "*.*" });
				    dialog.setFileName("XPaths.txt");
//				    System.out.println("Save to: " + dialog.open());
					generateXPathsUtil.exportToNotepad(list.getItems(),dialog.open());
					
				
			}
		});
		
		//*******************************************************************************************
		//**********This function is used to highlight appropriate frame on selecting it*************
		//*******************************************************************************************
		
		iframe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String iframeSelection = iframe.getItem(iframe.getSelectionIndex());
				WebElement element = iFrameElements.get(Integer.parseInt(iframeSelection));
				generateXPathsUtil.highlightElement(element, driver);
			}
		});

		//*******************************************************************************************
		//*********************This function is used to fetch all iframes****************************
		//*******************************************************************************************
		
		btnFindIframes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				iFrameElements = generateXPathsUtil.iFrameFetch(driver,iframe);
			}
		});

		//*******************************************************************************************
		//*************This function is used to switch to the appropriate iframe*********************
		//*******************************************************************************************
		
		btnSwitchToIframe.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				String iframeSelection = iframe.getItem(iframe.getSelectionIndex());
				WebElement element = iFrameElements.get(Integer.parseInt(iframeSelection));
				generateXPathsUtil.switchIFrame(driver,element);
			}
		});
		
		//*******************************************************************************************
		//**************This function is used to generate all XPath of a web page********************
		//*******************************************************************************************
		
		btnGetAllXpaths.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					arr = generateXPathsUtil.generate(driver,progressBar,status);
					list.removeAll();
					for (int j=0; j<arr.size();j++)
					{
						System.out.println(arr.get(j));
						list.add(arr.get(j));
					}
					xpathtraverse.setText(driver.getTitle()+"/");
					iFrameElements = generateXPathsUtil.iFrameFetch(driver,iframe);
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		
		//*******************************************************************************************
		//****************This function is used to navigate to the provided URL**********************
		//*******************************************************************************************
			
		btnOpenUrl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				driver = generateXPathsUtil.gotoURL(driver,url.getText());
			}
		});		
		
		
		//*******************************************************************************************
		//*************This function is used to highlight the selected XPath element*****************
		//*******************************************************************************************
			
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String xpath = list.getItem(list.getSelectionIndex());
				System.out.println("Item selected: "+xpath+"\n");
				generateXPathsUtil.highlightElement(xpath, driver);
			}
		});
		
		
		//*******************************************************************************************
		//******************This function is used to switch to default content***********************
		//*******************************************************************************************
			
		
		btnSwitchToDefault.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				driver.switchTo().defaultContent();
			}
		});
		
		shlXpath.open();
		shlXpath.layout();
		while (!shlXpath.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
