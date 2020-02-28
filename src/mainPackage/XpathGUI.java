package mainPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openqa.selenium.WebDriver;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Combo;

public class XpathGUI {
	private static Text url;
	private static Text xpathtraverse;
	static WebDriver driver = null;
	static ArrayList<String> arr = null;
	
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println(System.getProperty("user.dir")+"\\Drivers\\chromedriver.exe");
		System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\Drivers\\chromedriver.exe");
		
		generateXPaths gen = new generateXPaths();
		Display display = Display.getDefault();
		Shell shlXpath = new Shell();
		shlXpath.setSize(586, 345);
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
		url.setLayoutData(fd_url);
		
		Button btnOpenUrl = new Button(shlXpath, SWT.NONE);
	
	
		FormData fd_btnOpenUrl = new FormData();
		fd_btnOpenUrl.left = new FormAttachment(url, 6);
		fd_btnOpenUrl.top = new FormAttachment(lblEnterUrl, -5, SWT.TOP);
		btnOpenUrl.setLayoutData(fd_btnOpenUrl);
		btnOpenUrl.setText("Navigate to URL");
		
		xpathtraverse = new Text(shlXpath, SWT.BORDER);
		fd_url.left = new FormAttachment(xpathtraverse, 0, SWT.LEFT);
		fd_url.right = new FormAttachment(xpathtraverse, 0, SWT.RIGHT);
		fd_url.bottom = new FormAttachment(xpathtraverse, -7);
		FormData fd_xpathtraverse = new FormData();
		fd_xpathtraverse.left = new FormAttachment(lblXpathCurrentPath, 17);
		fd_xpathtraverse.right = new FormAttachment(100, -246);
		xpathtraverse.setLayoutData(fd_xpathtraverse);
		
		Button btnGetAllXpaths = new Button(shlXpath, SWT.NONE);
		fd_xpathtraverse.bottom = new FormAttachment(btnGetAllXpaths, -6);
		
		
		FormData fd_btnGetAllXpaths = new FormData();
		fd_btnGetAllXpaths.top = new FormAttachment(0, 84);
		btnGetAllXpaths.setLayoutData(fd_btnGetAllXpaths);
		btnGetAllXpaths.setText("Get All XPaths");
		
		List list = new List(shlXpath, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String xpath = list.getItem(list.getSelectionIndex());
				System.out.println("Item selected: "+xpath+"\n");
				gen.highlightElement(xpath, driver);
			}
		});
		FormData fd_list = new FormData();
		fd_list.right = new FormAttachment(100, -50);
		fd_list.left = new FormAttachment(0, 10);
		fd_list.top = new FormAttachment(btnGetAllXpaths, 6);
		list.setLayoutData(fd_list);
		
		ProgressBar progressBar = new ProgressBar(shlXpath, SWT.NONE);
		fd_list.bottom = new FormAttachment(progressBar, -11);
		FormData fd_progressBar = new FormData();
		fd_progressBar.right = new FormAttachment(100, -50);
		fd_progressBar.top = new FormAttachment(0, 262);
		progressBar.setLayoutData(fd_progressBar);
		
		Label status = new Label(shlXpath, SWT.NONE);
		FormData fd_status = new FormData();
		fd_status.width = 2;
		fd_status.right = new FormAttachment(lblEnterUrl, 127);
		fd_status.bottom = new FormAttachment(progressBar, 0, SWT.BOTTOM);
		fd_status.left = new FormAttachment(lblEnterUrl, 0, SWT.LEFT);
		fd_status.height = 50;
		fd_status.top = new FormAttachment(progressBar, 0, SWT.TOP);
		status.setLayoutData(fd_status);
		
		status.setText("Ready...");
		
		Button btnExportToKatalon = new Button(shlXpath, SWT.NONE);
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

				for (int i=0; i < items.length; i++) 
				{
			
				try {
					gen.exportToKatalon(driver, xpathtraverse, items[i], filenumber);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				}
				System.out.println("The No. of files accessed are: "+filenumber);
				//Call Function to Export to Katalon
			}
		});
		
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
		FormData fd_btnExportToNotepad = new FormData();
		fd_btnExportToNotepad.top = new FormAttachment(btnGetAllXpaths, 0, SWT.TOP);
		fd_btnExportToNotepad.left = new FormAttachment(btnExportToKatalon, 6);
		btnExportToNotepad.setLayoutData(fd_btnExportToNotepad);
		btnExportToNotepad.setText("Export to Notepad");
		
		Combo iframe = new Combo(shlXpath, SWT.NONE);
		FormData fd_iframe = new FormData();
		fd_iframe.bottom = new FormAttachment(list, -21);
		fd_iframe.right = new FormAttachment(list, 0, SWT.RIGHT);
		iframe.setLayoutData(fd_iframe);
		
		btnGetAllXpaths.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					arr = gen.generate(driver,progressBar,status,iframe);
					list.removeAll();
					for (int j=0; j<arr.size();j++)
					{
						System.out.println(arr.get(j));
						list.add(arr.get(j));
					}
					xpathtraverse.setText(driver.getTitle()+"/");
					
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		
		btnOpenUrl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				driver = gen.gotoURL(driver,url.getText(),iframe);
//				driver = new ChromeDriver();
//				driver.get(url.getText());
				
				
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
