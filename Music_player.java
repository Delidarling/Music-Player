import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import org.jsoup.Jsoup;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

 
import java.util.List;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;


import javazoom.jl.player.Player;




public class Music_player {

		JFrame jf;//jf为主框架
		JPanel jp;// 创建内容面板，指定布局为null，使用绝对布局，jp类似画布，可往上面放东西
		GridBagLayout gbl;//创建网格包布局管理器
		GridBagConstraints gbc;//GridBagConstraints对象来给出每个组件的大小和摆放位置
		int window_h;//软件界面的高度
		int window_w;//软件界面的宽度
		int main_menu_comp_h;//主页主菜单的控件高度
		int main_menu_comp_w;//主页主菜单的控件宽度
		int fontsize;//控件字体大小
		int play_btn_clicknum;//播放按钮点击次数
		int volume_btn_clicknum;//音量按钮点击次数
		int playmode_clicknum;//播放模式按钮点击次数
		int music_switch;//歌曲切换次数
		JTextArea search_bar;
		String search_str;//搜索框的输入内容
		//数据库相关
		Connection connection = null;//用于连接数据库
        Statement statement = null;//用于sql语句的执行
        int song_id;//歌曲表主键
        int singer_id;//歌手表主键
        int album_id;//专辑表主键
        int playlist_id;//歌单表主键
        //播放音乐相关；
        Player []player=new Player[2];//相当于建立两个player变量，两个变量随着切歌而互相切换防止点不同的歌时多首歌同时播放
        
		
		Music_player()//构造函数，进行各个参数的初始化
		{
			
			jf=new JFrame("Music Player");//jf为主框架
			jp = new JPanel(null);// 创建内容面板，指定布局为 null，则使用绝对布局 
			gbl=new GridBagLayout();
			gbc=new GridBagConstraints();
			jf.setLayout(gbl);//设置容器布局为网格包布局类型
			window_h=700;//软件界面的高度
			window_w=1200;//软件界面的宽度
			main_menu_comp_h=50;//主页主菜单的控件高度
			main_menu_comp_w=100;//主页主菜单的控件宽度
			fontsize=16;//控件上的字体大小
			play_btn_clicknum=0;//播放按钮点击次数初始化为0
			volume_btn_clicknum=0;//音量按钮初始化次数为0
			playmode_clicknum=0;//播放模式按钮初始化次数为0
			music_switch=0;//歌曲切换次数置为0，通过该变量来控制每次只播放一首歌曲，而不会多首歌同时播放
					
			gbc.weightx=1;
			gbc.weighty=1;
			gbc.gridheight=0;
			gbc.gridwidth=0;
			gbl.setConstraints(jp, gbc);
			jf.add(jp);
			jf.setSize(window_w, window_h);//设置窗口的长宽
	        jf.setLocationRelativeTo(null);//窗口将置于屏幕的中央
	        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//设置关闭，用户单击窗口的关闭按钮时关闭就可以窗口
	        connect_sql();//连接数据库
	        String songPath = ".\\local_music";//指定项目中存放歌曲的文件夹目录，在该文件夹下进行歌曲搜索
			File songFile = new File(songPath);
			findMP3(jp,songFile,songPath);//一打开软件就把本地文件夹的音乐导入到数据库中
	        jf.setVisible(true);//设置界面为显示状态，否则无法显示
		}
		
		//主页顶部界面函数
		public void top_main_page(JPanel jp)
		{
			search_bar=new JTextArea();//搜索框
			search_bar.setMargin(new Insets(main_menu_comp_h/4+4,10,main_menu_comp_h/4,10));//设置搜索框内的文字和框的边距(上左下右)
			Add_Component(jp,search_bar,Color.LIGHT_GRAY,fontsize,main_menu_comp_w+2,0,main_menu_comp_w*10,main_menu_comp_h);
			
			JButton search_btn=new JButton("搜索");//搜索按钮
			Add_Component(jp,search_btn,Color.white,fontsize,main_menu_comp_w*11+3,0,main_menu_comp_w*3/4,main_menu_comp_h);
			btn_click(search_btn,jp);//点击搜索按钮后，开始搜索在线歌曲
			
		}
		
		//主页左部界面函数
		public void left_main_page(JPanel jp)
	    {
	    	JButton main_page_btn=new JButton("主页");//主页按钮，按下后返回播放器主页
	    	Add_Component(jp,main_page_btn,Color.white,fontsize,0,0,main_menu_comp_w,main_menu_comp_h);
	    	btn_click(main_page_btn,jp);//点击主页按钮后，跳转页面
	    	
			JLabel my_music=new JLabel("我的音乐",JLabel.CENTER);//控件上的文字居中
			Add_Component(jp,my_music,Color.white,fontsize*3/4,0,main_menu_comp_h,main_menu_comp_w,main_menu_comp_h);
			
			JButton my_love=new JButton("我喜欢");//我喜欢的音乐，点击该按钮跳转到用户收藏的音乐页面
			Add_Component(jp,my_love,Color.white,fontsize,0,main_menu_comp_h*2,main_menu_comp_w,main_menu_comp_h);
			btn_click(my_love,jp);//点击我喜欢按钮后，跳转页面
			
			JButton play_history=new JButton("播放历史");//点击该按钮跳转到播放历史
			Add_Component(jp,play_history,Color.white,fontsize,0,main_menu_comp_h*3,main_menu_comp_w,main_menu_comp_h);
			btn_click(play_history,jp);//点击播放历史按钮后，跳转页面
			
			JButton local_music=new JButton("本地音乐");//点击该按钮跳转到本地音乐
			Add_Component(jp,local_music,Color.white,fontsize,0,main_menu_comp_h*4,main_menu_comp_w,main_menu_comp_h);
			btn_click(local_music,jp);//点击本地音乐按钮后，跳转页面
			
			JLabel blank=new JLabel();//一段空白区域
			Add_Component(jp,blank,Color.white,fontsize,0,main_menu_comp_h*5,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel my_playlist=new JLabel("我的歌单",JLabel.CENTER);
			my_playlist.setBorder(BorderFactory.createLineBorder(Color.black,1,true));//设置边框
			Add_Component(jp,my_playlist,Color.white,fontsize*3/4,0,main_menu_comp_h*6,main_menu_comp_w,main_menu_comp_h);
			
			String[] songlist = { "歌单一", "歌单二", "歌单三","歌单四","歌单五"};//存放所有歌单名的字符串数组，可点击进入相应的歌单
			JComboBox<String> playlist= new JComboBox<String>(songlist);//可下拉列表框
			Add_Component(jp,playlist,Color.white,fontsize,0,main_menu_comp_h*7,main_menu_comp_w,main_menu_comp_h);
			playlist_click(playlist,jp);//歌单下拉框点击事件触发函数
			
			JList playlist_content=new JList(songlist);//列表框，框里有所有歌单名，可点击进入相应的歌单
			playlist_content.setBorder(BorderFactory.createLineBorder(Color.black,1,true));
			Add_Component(jp,playlist_content,Color.white,fontsize,0,main_menu_comp_h*8,main_menu_comp_w,main_menu_comp_h*songlist.length/2);
			
			jf.setContentPane(jp);//向框架内添加组件
	    }
		
		//子界面全歌曲显示按钮，按歌手划分按钮，按专辑划分按钮
		public void subpage_btn(JPanel jp)
		{
			JButton allsong_btn=new JButton("全歌曲显示");//全歌曲显示按钮
			Add_Component(jp,allsong_btn,Color.white,fontsize,main_menu_comp_w+2,main_menu_comp_h*2,main_menu_comp_w*2,main_menu_comp_h);
			
			JButton singer_div_btn=new JButton("按歌手划分");//按歌手划分按钮
			Add_Component(jp,singer_div_btn,Color.white,fontsize,main_menu_comp_w*3,main_menu_comp_h*2,main_menu_comp_w*2,main_menu_comp_h);
			
			JButton album_div_btn=new JButton("按专辑划分");//按专辑划分按钮
			Add_Component(jp,album_div_btn,Color.white,fontsize,main_menu_comp_w*5,main_menu_comp_h*2,main_menu_comp_w*2,main_menu_comp_h);
			
			jf.setContentPane(jp);//向框架内添加组件
		}
		
		//我喜欢-子页面
	    public void mylove_page()
	    {
	    	// 创建内容面板，指定布局为 null，则使用绝对布局
	        JPanel jp = new JPanel(null);
	    	
	        left_main_page(jp);//左部界面
	        play_tool(jp);//底部音乐控制器
	    	
	        top_main_page(jp);//界面顶部
					
			JLabel playlist_topbar=new JLabel(" 我喜欢的音乐");
			playlist_topbar.setBorder(BorderFactory.createLineBorder(Color.black,1,true));//设置边框
			Add_Component(jp,playlist_topbar,Color.white,fontsize,main_menu_comp_w+2,main_menu_comp_h,main_menu_comp_w*10,main_menu_comp_h);
			
			subpage_btn(jp);//子界面的功能按钮界面，全歌曲显示按钮，按歌手划分按钮，按专辑划分按钮
			
			JLabel songname=new JLabel("歌名");//歌名标签
			Add_Component(jp,songname,Color.white,fontsize,main_menu_comp_w*2,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel singername=new JLabel("歌手");//歌手标签
			Add_Component(jp,singername,Color.white,fontsize,main_menu_comp_w*5,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel albumname=new JLabel("专辑");//专辑标签
			Add_Component(jp,albumname,Color.white,fontsize,main_menu_comp_w*8,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel songtime=new JLabel("时长");//时长标签
			Add_Component(jp,songtime,Color.white,fontsize,main_menu_comp_w*11,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			jf.setContentPane(jp);//向框架内添加组件
	    }
	    
	  //播放历史-子页面
	    public void playhistory_page()
	    {
	    	// 创建内容面板，指定布局为 null，则使用绝对布局
	        JPanel jp = new JPanel(null);
	    	
	        left_main_page(jp);//左部界面
	        play_tool(jp);//底部音乐控制器
	    	
	        top_main_page(jp);//界面顶部
					
			JLabel playlist_topbar=new JLabel(" 播放历史");
			playlist_topbar.setBorder(BorderFactory.createLineBorder(Color.black,1,true));//设置边框
			Add_Component(jp,playlist_topbar,Color.white,fontsize,main_menu_comp_w+2,main_menu_comp_h,main_menu_comp_w*10,main_menu_comp_h);
			
			subpage_btn(jp);//子界面功能按钮
			
			JButton playtime_sort_btn=new JButton("按时间排序");//按时间排序按钮
			Add_Component(jp,playtime_sort_btn,Color.white,fontsize,main_menu_comp_w*7,main_menu_comp_h*2,main_menu_comp_w*2,main_menu_comp_h);
			
			JLabel songname=new JLabel("歌名");//歌名标签
			Add_Component(jp,songname,Color.white,fontsize,main_menu_comp_w*2,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel singername=new JLabel("歌手");//歌手标签
			Add_Component(jp,singername,Color.white,fontsize,main_menu_comp_w*4,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel albumname=new JLabel("专辑");//专辑标签
			Add_Component(jp,albumname,Color.white,fontsize,main_menu_comp_w*6,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel songtime=new JLabel("时长");//时长标签
			Add_Component(jp,songtime,Color.white,fontsize,main_menu_comp_w*8,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel playdate=new JLabel("播放时间");//播放时间标签
			Add_Component(jp,playdate,Color.white,fontsize,main_menu_comp_w*10,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			jf.setContentPane(jp);//向框架内添加组件
	    }
	    
	  //本地音乐-子页面
	    public void localmusic_page()
	    {
	    	int index;
	    	// 创建内容面板，指定布局为 null，则使用绝对布局
	        JPanel jp = new JPanel(null);
	    	
	        left_main_page(jp);//左部界面
	        play_tool(jp);//底部音乐控制器
	    	
	        top_main_page(jp);//界面顶部
					
			JLabel playlist_topbar=new JLabel(" 本地音乐");
			playlist_topbar.setBorder(BorderFactory.createLineBorder(Color.black,1,true));//设置边框
			Add_Component(jp,playlist_topbar,Color.white,fontsize,main_menu_comp_w+2,main_menu_comp_h,main_menu_comp_w*10,main_menu_comp_h);
			
			subpage_btn(jp);//子界面功能按钮，全歌曲显示按钮，按歌手划分按钮，按专辑划分按钮
			
			JButton localsearch_btn=new JButton("搜索本地");//搜索本地音乐按钮
			Add_Component(jp,localsearch_btn,Color.white,fontsize,main_menu_comp_w*7,main_menu_comp_h*2,main_menu_comp_w*2,main_menu_comp_h);
			btn_click(localsearch_btn,jp);
			
			String songPath = ".\\local_music";//指定项目中存放歌曲的文件夹目录，在该文件夹下进行歌曲搜索
			File songFile = new File(songPath);
			ArrayList<ArrayList<String> > data=findMP3(jp,songFile,songPath);//得到项目中歌曲文件夹的所有歌曲信息
			String[][]songinfo=new String[data.size()][data.get(0).size()];//创建一个二维字符串数组
			for(index=0;index<data.size();index++)
			{
				songinfo[index]=(String[])(data.get(index).toArray(new String[0]));
			}
			System.out.println(songinfo[0][0]);
			String []colname= {"歌名","歌手","专辑","时长","大小"};//表格列名
			JTable table = new JTable(songinfo,colname){
				public boolean isCellEditable(int row, int column)//设置表格为不可编辑状态
				{
				return false;
				}
				};
			//表格按键监听
			table.addMouseListener(new MouseAdapter() {//为表格添加右键菜单
			      public void mouseClicked(MouseEvent e){
			        if (e.getButton() == MouseEvent.BUTTON3){//Button3为右键，Button1为左键
			          //在table显示
			          JPopupMenu jpm = new JPopupMenu();//右键弹出的菜单的变量
			          //表格 的rowAtPoint方法返回坐标所在的行号，参数为坐标类型，
			          int click_row = table.rowAtPoint(e.getPoint());//click_row接收用户点击处的行号，行号从0开始
			          JMenuItem PlayItem = new JMenuItem("播放");
			          JMenuItem LovedItem = new JMenuItem("收藏");
			          
			          // 添加 一级菜单 到 弹出菜单
			          jpm.add(PlayItem);
			          jpm.add(LovedItem);
			          // 添加菜单项的点击监听器
			          jpm.show(e.getComponent(), e.getX(), e.getY());//在哪右键，就在那弹出菜单
			          //右键菜单点击播放的监听触发函数
			          PlayItem.addActionListener(new ActionListener() {
			              @Override
			              public void actionPerformed(ActionEvent e) {
			                  System.out.println(songinfo[click_row][0]);//得到点击的歌名
			                  String []attribute= {"resource_url"};//要查找的信息
			                  //根据点击的歌名，往数据库里查找该音乐文件存在的路径
			                  String []res=sql_query("Select resource_url from song where song_name="+"'"+songinfo[click_row][0]
			                  +"'"+" and album_id="+"(Select album_id from album where album_name="+"'"+songinfo[click_row][2]+"')",attribute);
			                  System.out.println(res[0]);//控制台输出本地音乐文件路径
			                  File file = new File(res[0]);
			                  
			      			  //调用播放方法进行播放
			      			  try {
			      				  	FileInputStream fis = new FileInputStream(file);//输入流
			      				  	BufferedInputStream stream = new BufferedInputStream(fis);//缓冲流
			      				  	
			      				  	//如果第一次点播本地歌曲
			      				    if(music_switch==0)//这里设置if是为了解决第一次点播本地歌曲出现播放不出的情况
			      				    {
			      				    	music_switch++;
				                		player[0]=new Player(stream);//用player[0]变量
				                		music_play(player[0]);
			      				    }
			      				    else//不是第一次播放本地歌曲
			      				    {
			      				    	if(music_switch%2==0)
					                	{
					                		music_switch++;
					                		player[0]=new Player(stream);
					                		player[1].close();//关闭其中一个player实现每次只播放一首歌
					                		music_play(player[0]);
					                	}
					                	else
					                	{
					                		music_switch++;
					                		player[1]=new Player(stream);
					                		player[0].close();
					                		music_play(player[1]);
					                	}
			      				    }
				                	
			      				} catch (Exception err) {
			      				// TODO: handle exception
			      				}
			          		  
			              }
			          });
			        //右键菜单点击收藏的监听触发函数
			          LovedItem.addActionListener(new ActionListener() {
			              @Override
			              public void actionPerformed(ActionEvent e) {
			                  
			              }
			          });
			          
			        }
			      }
			    });
		    table.setFont(new Font("微软雅黑", Font.PLAIN, fontsize));//设置表格中的字体大小
		    table.setRowHeight(2*fontsize);//设置表格的行高
	        JScrollPane jsp = new JScrollPane(table);//可滚动页面
			Add_Component(jp,jsp,Color.white,16,main_menu_comp_w+2,main_menu_comp_h*310/100,main_menu_comp_w*10,main_menu_comp_h*9);
			
			jf.setContentPane(jp);//向框架内添加组件
	    }
	    
	    
	    public void music_play(Player player)//歌曲播放函数
	    {
	    	//开启一个线程，防止点播本地歌曲时页面完全被歌曲播放占用而卡住
			  new Thread(()->{
			  //调用播放方法进行播放
			  try {
              		player.play();
              	}
				 catch (Exception err) {
				}
			  }).start();
	    }
	    
	    
	  //我的歌单-子页面
	    public void songlist_page(String listname)
	    {
	    	// 创建内容面板，指定布局为 null，则使用绝对布局
	        JPanel jp = new JPanel(null);
	    	
	        left_main_page(jp);//左部界面
	        play_tool(jp);//底部音乐控制器
	    	
	        top_main_page(jp);//界面顶部
					
			JLabel playlist_topbar=new JLabel(" 我的歌单: "+listname);
			playlist_topbar.setBorder(BorderFactory.createLineBorder(Color.black,1,true));//设置边框
			Add_Component(jp,playlist_topbar,Color.white,fontsize,main_menu_comp_w+2,main_menu_comp_h,main_menu_comp_w*10,main_menu_comp_h);
			
			subpage_btn(jp);//子界面功能按钮
			
			JLabel songname=new JLabel("歌名");//歌名标签
			Add_Component(jp,songname,Color.white,fontsize,main_menu_comp_w*2,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel singername=new JLabel("歌手");//歌手标签
			Add_Component(jp,singername,Color.white,fontsize,main_menu_comp_w*5,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel albumname=new JLabel("专辑");//专辑标签
			Add_Component(jp,albumname,Color.white,fontsize,main_menu_comp_w*8,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel songtime=new JLabel("时长");//时长标签
			Add_Component(jp,songtime,Color.white,fontsize,main_menu_comp_w*11,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			jf.setContentPane(jp);//向框架内添加组件
	    }
	    
	    //播放进度条和播放控制键
	    public void play_tool(JPanel jp)
	    {
	    	JPanel jpr=new JPanel(null);
	    	JSlider song_slider=new JSlider(0,100);  //指定最小值，最大值，这是个可拖动的进度条，显示或控制歌曲的播放进度
			song_slider.setValue(0);//设置进度条的初始值为0
			Add_Component(jp,song_slider,Color.white,0,main_menu_comp_w+2,window_h*17/20,main_menu_comp_w*10,main_menu_comp_h/2);
			
			JButton play_mode = new JButton();//播放模式
			play_mode=changeIconSize("顺序播放",play_mode,"./img/顺序播放.png",main_menu_comp_w/2,main_menu_comp_h);
			Add_Component(jp,play_mode,Color.white,0,window_w*25/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
			btn_click(play_mode,jp);
			
			JButton last_song = new JButton();//上一首按钮
			last_song=changeIconSize("上一首",last_song,"./img/上一首.png",main_menu_comp_w/2,main_menu_comp_h);
			Add_Component(jp,last_song,Color.white,0,window_w*36/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
			
			JButton play_btn = new JButton();//暂停/播放按钮
			play_btn=changeIconSize("暂停",play_btn,"./img/暂停.png",main_menu_comp_w/2,main_menu_comp_h);
			Add_Component(jp,play_btn,Color.white,0,window_w*45/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
			btn_click(play_btn,jp);
			
			JButton next_song = new JButton();//下一首按钮
			next_song=changeIconSize("下一首",next_song,"./img/下一首.png",main_menu_comp_w/2,main_menu_comp_h);
			Add_Component(jp,next_song,Color.white,0,window_w*54/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
			
			JButton volume_btn = new JButton();//音量按钮
			volume_btn=changeIconSize("音量",volume_btn,"./img/音量.png",main_menu_comp_w/2,main_menu_comp_h);
			Add_Component(jp,volume_btn,Color.white,0,window_w*70/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
			btn_click(volume_btn,jp);
			
			JSlider volume_slider=new JSlider(0,100);  //指定最小值，最大值，这是个可拖动的进度条，显示或控制歌曲的播放进度
			volume_slider.setValue(50);//设置进度条的初始值为50
			Add_Component(jp,volume_slider,Color.white,0,window_w*75/100,window_h*91/100,main_menu_comp_w*2,main_menu_comp_h/2);
	    }
	    
	    
	  //按钮事件监听
	  /*按钮点击后切换按钮图片的原理为隐藏原按钮，并在面板jp中清除该按钮，然后新建一个按钮并设置相关的按钮图片，根据当前点击次数来设置不同的按钮图片
	    按钮点击后切换页面的原理为将原面板jp隐藏，然后在不同的子页面函数中重新渲染页面  
	  * */
	    public void btn_click(JButton btn,JPanel jp)
	    {
	    	//为按钮btn添加ActionEvent事件的处理程序
	    	btn.addActionListener(new ActionListener()
	    	{
				@Override
				public void actionPerformed(ActionEvent e) 
				{	
					
					if(btn.getActionCommand()=="我喜欢")//获取点击按钮的名字，并根据不同按钮执行不同的函数
					{
						jp.hide();//隐藏跳转前的面板
						mylove_page();
					}
					else if(btn.getActionCommand()=="播放历史")//获取点击按钮的名字，并根据不同按钮执行不同的函数
					{
						jp.hide();//隐藏跳转前的面板
						playhistory_page();
					}	
					else if(btn.getActionCommand()=="本地音乐")//获取点击按钮的名字，并根据不同按钮执行不同的函数
					{
						jp.hide();//隐藏跳转前的面板
						localmusic_page();
					}
						
					else if(btn.getActionCommand()=="主页")//获取点击按钮的名字，并根据不同按钮执行不同的函数
					{
						jp.hide();//隐藏跳转前的面板
						JPanel jpr=new JPanel(null);
						top_main_page(jpr);//主页顶部界面函数
				        left_main_page(jpr);//主页左部界面函数
				        play_tool(jpr);//下方的音乐播放工具
				        JLabel playlist_topbar=new JLabel(" 歌单推荐");
						playlist_topbar.setBorder(BorderFactory.createLineBorder(Color.black,1,true));//设置边框
						Add_Component(jpr,playlist_topbar,Color.white,fontsize,main_menu_comp_w+2,main_menu_comp_h,main_menu_comp_w*10,main_menu_comp_h);
					}
					else if(btn.getActionCommand()=="暂停"||btn.getActionCommand()=="播放")//获取点击按钮的名字，并根据不同按钮执行不同的函数
					{
						btn.hide();//隐藏原按钮，使得点击后能流畅切换按钮图片
						jp.remove(btn);//从面板中移除原按钮
						play_btn_clicknum++;//点击次数自增
						JButton play_btn = new JButton();//播放按钮
						if(play_btn_clicknum%2==1)//根据点击次数来切换按钮图片
						{
							play_btn=changeIconSize("播放",play_btn,"./img/播放.png",main_menu_comp_w/2,main_menu_comp_h);
							Add_Component(jp,play_btn,Color.white,0,window_w*45/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
							btn_click(play_btn,jp);
						}
						else
						{
							play_btn=changeIconSize("暂停",play_btn,"./img/暂停.png",main_menu_comp_w/2,main_menu_comp_h);
							Add_Component(jp,play_btn,Color.white,0,window_w*45/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
							btn_click(play_btn,jp);
						}
					}
					else if(btn.getActionCommand()=="音量"||btn.getActionCommand()=="静音")//获取点击按钮的名字，并根据不同按钮执行不同的函数
					{
						btn.hide();//隐藏原按钮，使得点击后能流畅切换按钮图片
						jp.remove(btn);//从面板中移除原按钮
						volume_btn_clicknum++;//点击次数自增
						JButton volume_btn = new JButton();//音量按钮
						if(volume_btn_clicknum%2==1)//根据点击次数来切换按钮图片
						{
							volume_btn=changeIconSize("静音",volume_btn,"./img/静音.png",main_menu_comp_w/2,main_menu_comp_h);
							Add_Component(jp,volume_btn,Color.white,0,window_w*70/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
							btn_click(volume_btn,jp);
						}
						else
						{
							volume_btn=changeIconSize("音量",volume_btn,"./img/音量.png",main_menu_comp_w/2,main_menu_comp_h);
							Add_Component(jp,volume_btn,Color.white,0,window_w*70/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
							btn_click(volume_btn,jp);
						}
					}
					else if(btn.getActionCommand()=="顺序播放"||btn.getActionCommand()=="单曲循环"||btn.getActionCommand()=="随机播放")//获取点击按钮的名字，并根据不同按钮执行不同的函数
					{
						btn.hide();//隐藏原按钮，使得点击后能流畅切换按钮图片
						jp.remove(btn);//从面板中移除原按钮
						playmode_clicknum++;//点击次数自增
						JButton playmode_btn = new JButton();//音量按钮
						if(playmode_clicknum%3==0)//根据点击次数来切换按钮图片
						{
							playmode_btn=changeIconSize("顺序播放",playmode_btn,"./img/顺序播放.png",main_menu_comp_w/2,main_menu_comp_h);
							Add_Component(jp,playmode_btn,Color.white,0,window_w*25/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
							btn_click(playmode_btn,jp);
						}
						else if(playmode_clicknum%3==1)//根据点击次数来切换按钮图片
						{
							playmode_btn=changeIconSize("单曲循环",playmode_btn,"./img/单曲循环.png",main_menu_comp_w/2,main_menu_comp_h);
							Add_Component(jp,playmode_btn,Color.white,0,window_w*25/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
							btn_click(playmode_btn,jp);
						}
						else if(playmode_clicknum%3==2)//根据点击次数来切换按钮图片
						{
							playmode_btn=changeIconSize("随机播放",playmode_btn,"./img/随机播放.png",main_menu_comp_w/2,main_menu_comp_h);
							Add_Component(jp,playmode_btn,Color.white,0,window_w*25/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
							btn_click(playmode_btn,jp);
						}
					}
					
					else if(btn.getActionCommand()=="搜索本地")//获取点击按钮的名字，并根据不同按钮执行不同的函数
					{
											
						String songPath = "C:\\Users\\apple\\Desktop\\Music";//指定文件夹目录，在该文件夹下进行歌曲搜索
						File songFile = new File(songPath);
						System.out.println("\nFind the MP3 files in the specified directory("
								+ songPath + "): ");
						//findMP3(jp,songFile, songPath);

					}
					
					else if(btn.getActionCommand()=="搜索")//获取点击按钮的名字，并根据不同按钮执行不同的函数
					{
						search_str=search_bar.getText();//获取搜索框的内容
						System.out.println(search_str);
						String search_urlencode=new String();
						try {
							search_urlencode=URLEncoder.encode(search_str,"UTF-8");//对搜索内容进行转码
						}
						catch (Exception err) {
				            // TODO Auto-generated catch block
				            err.printStackTrace();
				        }
						//要访问的音乐的对应网址
//						http://tool.liumingye.cn/music/?page=audioPage&type=migu&name="+search_urlencode
						String strurl="http://tool.liumingye.cn/music/?page=audioPage&type=migu&name="+search_urlencode;
						System.out.println(strurl);
						
						
						
  
					}
				}
			});
	    }
	    
	    //歌单列表监听
    public void playlist_click(JComboBox<String> jcb,JPanel jp) {
    	jcb.addActionListener(new ActionListener()
    	{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				jp.hide();//隐藏跳转前上一个界面的panel
				jf.getContentPane().removeAll();//将jf里的所有组件都清空
				if(jcb.getSelectedItem()=="歌单一")//获取点击列表的名字，并根据不同按钮执行不同的函数
					songlist_page("歌单一");
				else if(jcb.getSelectedItem()=="歌单二")//获取点击列表的名字，并根据不同按钮执行不同的函数
					songlist_page("歌单二");
				else if(jcb.getSelectedItem()=="歌单三")//获取点击列表的名字，并根据不同按钮执行不同的函数
					songlist_page("歌单三");
				else if(jcb.getSelectedItem()=="歌单四")//获取点击列表的名字，并根据不同按钮执行不同的函数
					songlist_page("歌单四");
				else if(jcb.getSelectedItem()=="歌单五")//获取点击列表的名字，并根据不同按钮执行不同的函数
					songlist_page("歌单五");
			}
		});
    }

	    
    //主函数
    public static void main(String[] args) 
    {
    	//主页初始化
    	Music_player mp=new Music_player();
    	mp.top_main_page(mp.jp);//主页顶部界面函数
    	mp.play_tool(mp.jp);//下方的音乐播放工具
        mp.left_main_page(mp.jp);//主页左部界面函数
        JLabel playlist_topbar=new JLabel(" 歌单推荐");
		playlist_topbar.setBorder(BorderFactory.createLineBorder(Color.black,1,true));//设置边框
		Add_Component(mp.jp,playlist_topbar,Color.white,mp.fontsize,mp.main_menu_comp_w+2,mp.main_menu_comp_h,mp.main_menu_comp_w*10,mp.main_menu_comp_h);
        
    }
    
    //设置按钮所带图片大小自适应控件，让按钮的图片和按钮大小保持一致
    public static JButton changeIconSize(String btn_name,JButton button,String url,int width,int height)
    {//btn_name为按钮名称，button为相关要设置图片的按钮，url为按钮图片在本地文件夹的路径，width为按钮宽度，height为按钮高度
        button.setBounds(0,0,width,height);//设置按钮大小
        ImageIcon buttonImg=new ImageIcon(url);//设置按钮图片
        //改变图片的大小
        Image temp=buttonImg.getImage().getScaledInstance(button.getWidth(), button.getHeight(), buttonImg.getImage().SCALE_DEFAULT);
        button=new JButton(new ImageIcon(temp));
        button.setBorderPainted(false);//去按钮边框
		button.setOpaque(false);//将按钮设置为透明
		button.setText(btn_name);
        return button;
    }
    
    
    //组件添加函数
  	public static void Add_Component(JPanel jp,Component comp,Color color,int fontsize,int x,int y,int width,int height)
  	{
  		comp.setLocation(x, y);//设置组件的位置
  		comp.setSize(width, height);//设置组件的大小
  		comp.setBackground(color);//设置组件的背景颜色
  		comp.setFont(new Font("宋体",Font.BOLD,fontsize));//设置组件上的标签字体样式
  		jp.add(comp);//往容器内添加组件
  	}

  	//查找某本地文件夹目录下的MP3文件
  	public ArrayList <ArrayList<String> > findMP3(JPanel jp,File songFile, String songPath)//用ArrayList<String>方便将字符串添加到字符串列表
  	{
  		ArrayList <ArrayList<String> > all_songinfo=new ArrayList <ArrayList<String> >();//模拟一个二维数组，保存所有找到的歌的信息
  		Map<String, String> map = new HashMap<String,String>();//采用哈希表结构，以键值对来存放歌曲的所有信息
  		String songname=new String();//存放歌曲名
  		String singername=new String();//存放歌手名
  		String albumname=new String();//存放专辑名
  		String songtime=new String();//存放歌曲时长
  		String songsize=new String();//存放歌曲文件大小
  		String songurl=new String();//存放歌曲路径
  		String release_year=new String();//存放发行时间
  		DecimalFormat df = new DecimalFormat("0.00");//用于文件大小保留两位小数
		File[] songList = songFile.listFiles();

		if (songList != null) 
		{
			for (File s : songList) 
			{
				// 用递归法来查找各个子目录
				if (s.isDirectory() == true) 
				{
					findMP3(jp,s,s.getAbsolutePath());
				} 
				else 
				{
					// String类下的endsWith来判断文件后缀
					if (s.getAbsolutePath().endsWith(".mp3") == true|| s.getAbsolutePath().endsWith("MP3")== true) 
					{		
						try 
						{
							//String str=s.getAbsolutePath().replaceAll("\\\\","\\\\\\\\");
							Mp3File mp3file=new Mp3File(s.getAbsolutePath());
							if (mp3file.hasId3v2Tag())
							{
								//以下代码为进入本地音乐界面的歌曲表初始化做准备
					            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
					            //songname=s.getAbsolutePath().substring(s.getAbsolutePath().lastIndexOf("\\")+1,s.getAbsolutePath().lastIndexOf("."));//歌名
					            songname=id3v2Tag.getTitle();//歌曲名
					            singername=id3v2Tag.getArtist();//歌手名
					            albumname=id3v2Tag.getAlbum();//专辑名
					            songtime=mp3file.getLengthInSeconds()/60+":"+mp3file.getLengthInSeconds()%60;//歌曲时长
					            songsize=df.format(s.length()/1024/1024.0)+"MB";//歌曲大小
					            //将每首歌的所有信息存入一个字符串数组里，每个一维数组存放的字符串信息分别为歌名、歌手名、专辑名、歌曲时长、歌曲大小
					            ArrayList<String> songinfo = new ArrayList<String>();//存放各首歌的歌曲信息
					            songinfo.add(songname);
					            songinfo.add(singername);
					            songinfo.add(albumname);
					            songinfo.add(songtime);
					            songinfo.add(songsize);
					            all_songinfo.add(songinfo);
					            
					            
					            //以下代码块用来操作数据库，将歌曲信息填入各个表
					            songurl=s.getAbsolutePath();//得到歌曲路径
					            release_year=id3v2Tag.getYear();//获得发行时间
					            //将以上所有数据以键值对的形式存储，方便后续为数据库中的表添加信息时能在一个函数中进行对所有表的操作，不必为每个表都设置一个添加函数
					            map.put("songurl",songurl);
					            map.put("release_year", release_year);
					            map.put("albumname",albumname);
					            map.put("songname",songname);
					            map.put("singername",singername);
					            map.put("songtime",songtime);
					            map.put("songsize",songsize);

					            //本地音乐将is_local统一置为true，is_loved统一置为false
					            //为专辑表插入信息
					            sql_insert("album",map,true,false);//album为表名，map包含要插入的元组信息
					            //为歌手表插入信息
					            sql_insert("singer",map,true,false);
					            //为歌曲表插入信息
					            sql_insert("song",map,true,false);
					            //为属于关系表插入信息
					            sql_insert("belong_relation",map,true,false);
					            
					            
					            
					            
//					            String[] splited =songinfo_tmp.split("\\s+");
//					            for(String str:splited)
//					            {
//					            	System.out.println(str);
//					            }
					            
					           
					        }
						}
						catch (Exception err) 
						{
				            err.printStackTrace();
				        }
						//System.out.println(s.getAbsolutePath()+" "+df.format(s.length()/1024/1024.0)+"MB");//得到歌曲文件大小
						
					}
				}
			}
		}
	return all_songinfo;//返回一个二维字符串数组	
	}

  	
  	public void connect_sql()//连接数据库
  	{
  		
        try {
            String url = "jdbc:postgresql://localhost:5432/Music";//换成自己PostgreSQL数据库实例所在的ip地址，并设置自己的端口
            String user = "postgres";//设置用户名
            String password = "123456";  //设置密码
            Class.forName("org.postgresql.Driver"); //JDBC
            connection= DriverManager.getConnection(url, user, password);
            System.out.println("数据库连接成功"+connection);
            
            //statement.close();
            //connection.close();
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(null, "您还未连接本地数据库，请连接后再打开本软件", "提示",JOptionPane.INFORMATION_MESSAGE);
        	System.exit(0);//退出程序
            //e.printStackTrace();
        }
  	}
  	
  	public void sql_insert(String tablename,Map<String, String> map,boolean is_local,boolean is_loved)//数据库数据插入函数
  	{
  		String sql=new String();
  		//connection和statement已被设置为全局公共变量，且数据库一开始就连接到位，无需再连接
		try {
			//connection.setAutoCommit(false);//不自动提交
			statement = connection.createStatement();
			//为专辑表新增数据
			if(tablename=="album"&&statement.executeQuery("Select album_name from album where album_name="+"'"+map.get("albumname")+"'").next()==false)//若专辑表中找不到该专辑名则插入专辑表
			{
				sql = "INSERT INTO "+tablename+" (album_name,publish_time) "//sql语句
				+ "VALUES ("+"'"+map.get("albumname")+"'"+","+"'"+map.get("release_year")+"'"+");";//注意value里的字符串要用‘’括起来，否则sql语句无法运行
				statement.executeUpdate(sql);//执行sql语句
			}
			//为歌手表新增数据
			else if(tablename=="singer"&&statement.executeQuery("Select singer_name from singer where singer_name="+"'"+map.get("singername")+"'").next()==false)//若歌手表中找不到该歌手名则插入歌手表
			{
				sql = "INSERT INTO "+tablename+" (singer_name,singer_pic_url) "//sql语句
				+ "VALUES ("+"'"+map.get("singername")+"'"+","+"'"+map.get("singer_pic_url")+"'"+");";//注意value里的字符串要用‘’括起来，否则sql语句无法运行
				statement.executeUpdate(sql);
			}
			
			//为歌曲表新增数据,用歌曲名和专辑名来区分这首歌曲之前是否被录入，若这首歌之前已经录入，则不再添加进数据库
			else if(tablename=="song")
			{
				//若待插入歌名已经在song表中
				if(statement.executeQuery("Select song_name from song where song_name="+"'"+map.get("songname")+"'").next()==true)
				{
					ResultSet rs=statement.executeQuery("Select album_id from song where song_name="+"'"+map.get("songname")+"'");
					rs.next();//使得游标后移，否则读不出我们要的数据
					String albumid = rs.getString("album_id");//得到song表中该首歌的外键album_id
					
					rs=statement.executeQuery("Select album_name from album where album_id="+"'"+albumid+"'");
					rs.next();//使得游标后移，否则读不出我们要的数据
					String albumname = rs.getString("album_name");//得到album表中该专辑的名字
					if(albumname!=map.get("albumname"))//若待插入歌曲的歌名和song表中的同名歌曲的专辑不一样，则允许该首同名歌插入song表
					{
						sql = "INSERT INTO "+tablename+" (song_name,total_time,song_size,resource_url,is_local,is_loved,album_id) "//sql语句
								+ "VALUES ("+"'"+map.get("songname")+"'"+","+"'"+map.get("songtime")+"'"+","+"'"+map.get("songsize")+"'"
								+","+"'"+map.get("songurl")+"'"+","+is_local+","+is_loved+",(Select album_id from album where album_name="+"'"+map.get("albumname")+"')"+");";//注意value里的字符串要用‘’括起来，否则sql语句无法运行
						statement.executeUpdate(sql);
					}
				}
				else//若待插入歌名不在song表中，则直接插入
				{
					sql = "INSERT INTO "+tablename+" (song_name,total_time,song_size,resource_url,is_local,is_loved,album_id) "//sql语句
							+ "VALUES ("+"'"+map.get("songname")+"'"+","+"'"+map.get("songtime")+"'"+","+"'"+map.get("songsize")+"'"
							+","+"'"+map.get("songurl")+"'"+","+is_local+","+is_loved+",(Select album_id from album where album_name="+"'"+map.get("albumname")+"')"+");";//注意value里的字符串要用‘’括起来，否则sql语句无法运行
					statement.executeUpdate(sql);
				}
			}
			
			else if(tablename=="belong_relation")
			{
				ResultSet rs=statement.executeQuery("Select song_id from song where song_name="+"'"+map.get("songname")+"'");
				rs.next();//使得游标后移，否则读不出我们要的数据
				String songid = rs.getString("song_id");//得到song表中该首歌的主键
				
				rs=statement.executeQuery("Select singer_id from singer where singer_name="+"'"+map.get("singername")+"'");
				rs.next();//使得游标后移，否则读不出我们要的数据
				String singerid = rs.getString("singer_id");//得到singer表中该歌手的id
				
				sql = "INSERT INTO "+tablename+" (song_id,singer_id) "//sql语句
						+ "VALUES ("+"'"+songid+"'"+","+"'"+singerid+"'"+");";//注意value里的字符串要用‘’括起来，否则sql语句无法运行
				statement.executeUpdate(sql);
			}
			
			System.out.println(sql);
			
			//connection.commit();
 
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("新增数据失败！");
			//System.exit(0);
		}
		System.out.println("新增数据成功！");
  	}
  	
  	
  	
  	public void sql_delete(String sql)//数据库数据删除函数
  	{
  		try
  		{
  			statement.executeUpdate(sql);
  		}
  		catch (Exception e) 
  		{
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("删除数据失败！");
		}
  	}
  	
  	public String[] sql_query(String sql,String [] attribute)//数据库数据查询函数，attribute存放要查询的某个表的属性组
  	{
  		int index=0;
  		String [] result=new String[attribute.length];//result存放查询后返回每行记录的的各个属性值组成的字符串数组
		try
  		{
  			ResultSet rs=statement.executeQuery(sql);
  			while(rs.next())//使得游标后移，否则读不出我们要的数据
  			{
  				result[index]=rs.getString(attribute[index]);
  				index++;
  			}
  		}
  		catch (Exception e) 
  		{
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("修改数据失败！");
		}
		return result;
  	}
  	
  	public void sql_modify(String sql)//数据库数据修改函数
  	{
  		try
  		{
  			statement.executeUpdate(sql);
  		}
  		catch (Exception e) 
  		{
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("修改数据失败！");
		}
  	}
  	
}