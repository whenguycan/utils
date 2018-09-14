package com.lepus.utils;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpGUI {

	public static void main(String[] args){
		go();
	}

	public static final String DATA_KEY_SIGN = "sign";
	public static final String DATA_KEY_TOKEN = "token";
	public static final String COOKIE_KEY = "Cookie";
	public static final String COOKIE_HOLDER_TOKEN = "[token]";
	public static final String COOKIE_HOLDER_SERVER = "[server]";
	public static final String COOKIE_VAL = COOKIE_HOLDER_TOKEN + "; Path=/" + COOKIE_HOLDER_SERVER + "/; HttpOnly";
	public static final String URL_LOGIN = "/mobile/login.do";
	public static final String URL_AUTHENTICATION = "/mobile/authentication.do";
	public static final String USERNAME_DEFAULT = "jim";
	public static final String PASSWORD_DEFAULT = "000000";
	public static final String USERNAME_STU_DEFAULT = "G123456794";
	public static final String PASSWORD_STU_DEFAULT = "000000";
	
	public static final String HOST_DEFAULT_8080 = "http://192.168.1.105:8080";
	public static final String HOST_DEFAULT_28080 = "http://192.168.1.105:28080";
	public static final String HOST_DEFAULT_38080 = "http://192.168.1.105:38080";
	public static final String HOST_DEFAULT_58080 = "http://192.168.1.105:58080";

	public static class JsonResponse {
		String result_code;
		String result_message;
		Data data;
	}

	public static class Data {
		String login_result;
		String authentication_result;
		String sign;
		String token;
	}

	public static class Server {
		String name = "";
		String serverUrl = "";
		String authenticationUrl = "";
		String token = "";

		private Server(String name, String serverUrl){
			this.name = name;
			this.serverUrl = serverUrl;
			this.authenticationUrl = serverUrl + "/" + name + URL_AUTHENTICATION;
		}
	}

	public static Gson gson = 
			new GsonBuilder()
			.disableHtmlEscaping()
			.addSerializationExclusionStrategy(new ExclusionStrategy() {
				public boolean shouldSkipField(FieldAttributes f){
					return false;
				}
				public boolean shouldSkipClass(Class<?> clazz){
					return clazz.getName().indexOf("List") != -1;
				}
			})
			.create();

	public static String format(final String str, final String fix){
		StringBuilder out = new StringBuilder();
		int level = 0;
		int i, j;
		boolean start = false, newLine = true;
		for (i = 0; i < str.length(); ++i) {
			char ch = str.charAt(i);
			switch (ch) {
				case '[':
				case '{':
					newLine = true;
					level++;
					out.append("\n");// 换行
					for (j = 0; j < level - 1; ++j) {// 缩进
						out.append(fix);
					}
					break;
				case ']':
				case '}':
					out.append("\n");// 换行
					for (j = 0; j < level - 1; ++j) {// 缩进
						out.append(fix);
					}
					level--;
					break;
				case ',':
					newLine = true;
					break;
				case ':':
					newLine = false;
					break;
				case '\"':
				case '\'':
					start = !start;
					if (start && newLine) {
						out.append("\n");// 换行
						for (j = 0; j < level; ++j) {// 缩进
							out.append(fix);
						}
					}
					break;
			}
			out.append(ch);
		}
		return out.toString();
	}

	public static String formatJson(String json){
		return format(json, "    ");
	}

	public static String getFromJsonResponse(String json, String key){
		JsonResponse jsonResponse = gson.fromJson(json, JsonResponse.class);
		if ("1".equals(jsonResponse.result_code)) {
			if (DATA_KEY_SIGN.equals(key))
				return jsonResponse.data.sign;
			else if (DATA_KEY_TOKEN.equals(key))
				return jsonResponse.data.token;
			else {
				System.out.println("key not found : " + key);
				return "";
			}
		}
		return "";
	}

	public static class JButton extends javax.swing.JButton {
		private static final long serialVersionUID = 7882488740033295281L;
		protected String serverName;

		public JButton(String name){
			super();
			this.serverName = name;
		}

		public JButton(){
			super();
		}
	}

	public static class LoginManager {

		Map<String, Server> serverMap = new HashMap<String, Server>();
		List<String> templateUrlList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();

		String username = "";
		String password = "";
		String sign = "";

		public LoginManager(){
			initServers();
			initTemplateUrlList();
			initUserList();
		}

		void loadCookie(){
			login();
			authentication();
		}

		//TODO
		void initServers(){
			
			initLocalhostServerMap();
			
		}
		
		void initLocalhostServerMap(){
			
			serverMap.put("jcsj", new Server("jcsj", HOST_DEFAULT_8080));
			
//			serverMap.put("performance", new Server("performance", HOST_DEFAULT_28080));
//			serverMap.put("evaluate", new Server("evaluate", HOST_DEFAULT_28080));
//			serverMap.put("exam", new Server("exam", HOST_DEFAULT_28080));
//
//			serverMap.put("schooloa", new Server("schooloa", HOST_DEFAULT_38080));
//			serverMap.put("student", new Server("student", HOST_DEFAULT_38080));
//			
			serverMap.put("educationoa", new Server("educationoa", HOST_DEFAULT_58080));
//			serverMap.put("security", new Server("security", HOST_DEFAULT_58080));
			
			
			
		}
		
		void init233ServerMap(){
			
			serverMap.put("jcsj", new Server("jcsj", "http://192.168.1.233:8080"));
			
//			serverMap.put("performance", new Server("performance", "http://192.168.1.233:8080"));
//			serverMap.put("evaluate", new Server("evaluate", "http://192.168.1.231:8081"));
//			serverMap.put("exam", new Server("exam", "http://192.168.1.231:8081"));
//
//			serverMap.put("schooloa", new Server("schooloa", "http://192.168.1.233:8081"));
//			serverMap.put("student", new Server("student", "http://192.168.1.233:8080"));
//			
//			serverMap.put("educationoa", new Server("educationoa", "http://192.168.1.233:8081"));
//			serverMap.put("security", new Server("security", "http://192.168.1.233:8080"));
			
		}

		void login(){
			String url = serverMap.get("jcsj").serverUrl + "/jcsj" + URL_LOGIN + "?username=" + username + "&password=" + password;
			System.out.println(url);
			String json = HttpUtils.post(url);
			sign = getFromJsonResponse(json, DATA_KEY_SIGN);
		}

		void authentication(){
			for (Server server : serverMap.values()) {
				try {
					String url = server.authenticationUrl + "?username=" + username + "&sign=" + sign;
					String json = HttpUtils.post(url);
					System.out.println("authentication " + server.name + " " + json);
					String token = getFromJsonResponse(json, DATA_KEY_TOKEN);
					server.token = token;
				} catch (Exception e) {
					
				}
			}
		}
		
		void initUserList(){
			userList.add("alice");
			userList.add("tom");
			userList.add("jim");
		}
		
		//TODO
		void initTemplateUrlList(){
			
//			templateUrlList.add("/mobile/authentication.do?username=" + USERNAME_DEFAULT + "&sign=");
//			templateUrlList.add("/mobile/appInfo.do");
			
			//educationoa
			templateUrlList.add("/mobile/docdispose/dispatch/browseDispatchWithCondition.do");
			
		}

	}
	
	public static class JComponentHolder{
		
		final LoginManager loginManager = new LoginManager();
		final List<JComponent> jComponentList = new ArrayList<JComponent>();
		
		public JComponentHolder(){
			
			Dimension dimButton = new Dimension(100, 25);
			Dimension dimButtonSmall = new Dimension(100, 25);
			Dimension dimTextField = new Dimension(100, 25);
			Dimension dimTextFieldLarge = new Dimension(800, 25);
			Dimension dimTextArea = new Dimension(500, 600);
			Dimension dimTextAreaLarge = new Dimension(800, 600);
			
			final JTextField textUsername = new JTextField();
			textUsername.setSize(dimTextField);
			textUsername.setLocation(100, 0);
			textUsername.setText(USERNAME_DEFAULT);

			final JTextField textPassword = new JTextField();
			textPassword.setSize(dimTextField);
			textPassword.setLocation(200, 0);
			textPassword.setText(PASSWORD_DEFAULT);
			
			final JComboBox<String> comboxUser = new JComboBox<String>();
			comboxUser.setSize(dimTextField);
			comboxUser.setLocation(0, 0);
			for(String user : loginManager.userList){
				comboxUser.addItem(user);
			}
			comboxUser.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					String text = (String)comboxUser.getSelectedItem();
					textUsername.setText(text);
				}
			});

			final JTextArea textAreaLogin = new JTextArea();
			textAreaLogin.setSize(dimTextArea);
			textAreaLogin.setLineWrap(true);
			textAreaLogin.setWrapStyleWord(true);
			
			JScrollPane scrollLogin = new JScrollPane(textAreaLogin);
			scrollLogin.setSize(dimTextArea);

			JPanel panelLogin = new JPanel();
			panelLogin.setLayout(null);
			panelLogin.setBounds(0, 25, (int) dimTextArea.getWidth(), (int) dimTextArea.getHeight());
			panelLogin.add(scrollLogin);
			
			final JTextField textTestUrl = new JTextField();
			textTestUrl.setSize(dimTextFieldLarge);
			textTestUrl.setLocation((int)dimTextArea.getWidth() + 5, 25);

			JButton buttonLogin = new JButton();
			buttonLogin.setSize(dimButton);
			buttonLogin.setLocation(300, 0);
			buttonLogin.setText("Login");
			buttonLogin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					loginManager.username = textUsername.getText();
					loginManager.password = textPassword.getText();
					loginManager.loadCookie();
					textAreaLogin.setText(System.currentTimeMillis() + formatJson(gson.toJson(loginManager)));
				}
			});
			buttonLogin.addKeyListener(new KeyListener() {
				public void keyTyped(KeyEvent e){
					
				}
				public void keyReleased(KeyEvent e){
					loginManager.username = textUsername.getText();
					loginManager.password = textPassword.getText();
					loginManager.loadCookie();
					textAreaLogin.setText(System.currentTimeMillis() + formatJson(gson.toJson(loginManager)));
				}
				public void keyPressed(KeyEvent e){
					
				}
			});

			final JComboBox<String> comboxUrl = new JComboBox<String>();
			comboxUrl.setSize(dimTextFieldLarge);
			comboxUrl.setLocation((int)dimTextArea.getWidth() + 5, 0);
			for(String url : loginManager.templateUrlList){
				comboxUrl.addItem(url);
			}
			comboxUrl.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					String text = (String)comboxUrl.getSelectedItem();
					textTestUrl.setText(text);
				}
			});
			
			final JTextArea textAreaTest = new JTextArea();
			textAreaTest.setSize(dimTextArea);
			textAreaTest.setLineWrap(true);
			textAreaTest.setWrapStyleWord(true);

			JScrollPane scroll = new JScrollPane(textAreaTest);
			scroll.setSize(dimTextAreaLarge);

			JPanel panel = new JPanel();
			panel.setLayout(null);
			panel.setBounds((int)dimTextArea.getWidth() + 5, 75, (int) dimTextAreaLarge.getWidth(), (int) dimTextAreaLarge.getHeight());
			panel.add(scroll);

			int offset = (int)dimTextArea.getWidth() + 5;
			int yIndex = 50;
			for (final Server server : loginManager.serverMap.values()) {
				final JButton button = new JButton(server.name);
				button.setSize(dimButtonSmall);
				button.setLocation(offset, yIndex);
				button.setText(server.name);
				offset += 100;
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){
						String relative = textTestUrl.getText();
						String url = server.serverUrl + "/" + server.name + relative;
						Map<String, Object> headers = new HashMap<String, Object>();
						String cookie = COOKIE_VAL.replace(COOKIE_HOLDER_TOKEN, server.token).replace(COOKIE_HOLDER_SERVER, server.name);
						headers.put(COOKIE_KEY, cookie);
						Map<String, Object> params_ = new HashMap<String, Object>();
						String url_ = url;
						if(url.contains("?")){
							url_ = url.substring(0, url.indexOf("?"));
							String params = url.substring(url.indexOf("?") + 1);
							String[] arr = params.split("&");
							Arrays.asList(arr).stream().forEach(par -> {
								if(par.contains("=") && par.indexOf("=") != par.length() - 1){
									String key = par.substring(0, par.indexOf("="));
									String val = par.substring(par.indexOf("=") + 1);
									params_.put(key, val);
								}
							});
						}
						String json = HttpUtils.post(url_, headers, params_);
						textAreaTest.setText(System.currentTimeMillis() + formatJson(json));
					}
				});
				jComponentList.add(button);
			}

			jComponentList.add(textUsername);
			jComponentList.add(textPassword);
			jComponentList.add(buttonLogin);
			jComponentList.add(comboxUrl);
			jComponentList.add(comboxUser);
			jComponentList.add(textTestUrl);
			jComponentList.add(panelLogin);
			jComponentList.add(panel);
			
		}
		
	}
	
	public static void go(){

		final boolean demoMode = true;
		
		JComponentHolder jComponentHolder = new JComponentHolder();

		final JFrame mainFrame = new JFrame();
		mainFrame.setLayout(null);
		mainFrame.setSize(1366, 768);
		mainFrame.setResizable(true);
		mainFrame.setLocation(200, 100);
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e){
				super.windowOpened(e);
			}

			public void windowClosing(WindowEvent e){
				if (demoMode) {
					mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} else {
					int res = JOptionPane.showConfirmDialog(mainFrame, "Ready To Exit?", "Closing",
							JOptionPane.YES_NO_OPTION);
					if (res == JOptionPane.YES_OPTION) {
						mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						super.windowClosing(e);
					} else {
						mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					}
				}
			}
		});
		
		for(JComponent comp : jComponentHolder.jComponentList){
			mainFrame.add(comp);
		}

		mainFrame.setVisible(true);

	}

}
