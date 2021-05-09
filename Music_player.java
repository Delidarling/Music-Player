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

		JFrame jf;//jfΪ�����
		JPanel jp;// ����������壬ָ������Ϊnull��ʹ�þ��Բ��֣�jp���ƻ�������������Ŷ���
		GridBagLayout gbl;//������������ֹ�����
		GridBagConstraints gbc;//GridBagConstraints����������ÿ������Ĵ�С�Ͱڷ�λ��
		int window_h;//�������ĸ߶�
		int window_w;//�������Ŀ��
		int main_menu_comp_h;//��ҳ���˵��Ŀؼ��߶�
		int main_menu_comp_w;//��ҳ���˵��Ŀؼ����
		int fontsize;//�ؼ������С
		int play_btn_clicknum;//���Ű�ť�������
		int volume_btn_clicknum;//������ť�������
		int playmode_clicknum;//����ģʽ��ť�������
		int music_switch;//�����л�����
		JTextArea search_bar;
		String search_str;//���������������
		//���ݿ����
		Connection connection = null;//�����������ݿ�
        Statement statement = null;//����sql����ִ��
        int song_id;//����������
        int singer_id;//���ֱ�����
        int album_id;//ר��������
        int playlist_id;//�赥������
        //����������أ�
        Player []player=new Player[2];//�൱�ڽ�������player�������������������и�������л���ֹ�㲻ͬ�ĸ�ʱ���׸�ͬʱ����
        
		
		Music_player()//���캯�������и��������ĳ�ʼ��
		{
			
			jf=new JFrame("Music Player");//jfΪ�����
			jp = new JPanel(null);// ����������壬ָ������Ϊ null����ʹ�þ��Բ��� 
			gbl=new GridBagLayout();
			gbc=new GridBagConstraints();
			jf.setLayout(gbl);//������������Ϊ�������������
			window_h=700;//�������ĸ߶�
			window_w=1200;//�������Ŀ��
			main_menu_comp_h=50;//��ҳ���˵��Ŀؼ��߶�
			main_menu_comp_w=100;//��ҳ���˵��Ŀؼ����
			fontsize=16;//�ؼ��ϵ������С
			play_btn_clicknum=0;//���Ű�ť���������ʼ��Ϊ0
			volume_btn_clicknum=0;//������ť��ʼ������Ϊ0
			playmode_clicknum=0;//����ģʽ��ť��ʼ������Ϊ0
			music_switch=0;//�����л�������Ϊ0��ͨ���ñ���������ÿ��ֻ����һ�׸�������������׸�ͬʱ����
					
			gbc.weightx=1;
			gbc.weighty=1;
			gbc.gridheight=0;
			gbc.gridwidth=0;
			gbl.setConstraints(jp, gbc);
			jf.add(jp);
			jf.setSize(window_w, window_h);//���ô��ڵĳ���
	        jf.setLocationRelativeTo(null);//���ڽ�������Ļ������
	        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//���ùرգ��û��������ڵĹرհ�ťʱ�رվͿ��Դ���
	        connect_sql();//�������ݿ�
	        String songPath = ".\\local_music";//ָ����Ŀ�д�Ÿ������ļ���Ŀ¼���ڸ��ļ����½��и�������
			File songFile = new File(songPath);
			findMP3(jp,songFile,songPath);//һ������Ͱѱ����ļ��е����ֵ��뵽���ݿ���
	        jf.setVisible(true);//���ý���Ϊ��ʾ״̬�������޷���ʾ
		}
		
		//��ҳ�������溯��
		public void top_main_page(JPanel jp)
		{
			search_bar=new JTextArea();//������
			search_bar.setMargin(new Insets(main_menu_comp_h/4+4,10,main_menu_comp_h/4,10));//�����������ڵ����ֺͿ�ı߾�(��������)
			Add_Component(jp,search_bar,Color.LIGHT_GRAY,fontsize,main_menu_comp_w+2,0,main_menu_comp_w*10,main_menu_comp_h);
			
			JButton search_btn=new JButton("����");//������ť
			Add_Component(jp,search_btn,Color.white,fontsize,main_menu_comp_w*11+3,0,main_menu_comp_w*3/4,main_menu_comp_h);
			btn_click(search_btn,jp);//���������ť�󣬿�ʼ�������߸���
			
		}
		
		//��ҳ�󲿽��溯��
		public void left_main_page(JPanel jp)
	    {
	    	JButton main_page_btn=new JButton("��ҳ");//��ҳ��ť�����º󷵻ز�������ҳ
	    	Add_Component(jp,main_page_btn,Color.white,fontsize,0,0,main_menu_comp_w,main_menu_comp_h);
	    	btn_click(main_page_btn,jp);//�����ҳ��ť����תҳ��
	    	
			JLabel my_music=new JLabel("�ҵ�����",JLabel.CENTER);//�ؼ��ϵ����־���
			Add_Component(jp,my_music,Color.white,fontsize*3/4,0,main_menu_comp_h,main_menu_comp_w,main_menu_comp_h);
			
			JButton my_love=new JButton("��ϲ��");//��ϲ�������֣�����ð�ť��ת���û��ղص�����ҳ��
			Add_Component(jp,my_love,Color.white,fontsize,0,main_menu_comp_h*2,main_menu_comp_w,main_menu_comp_h);
			btn_click(my_love,jp);//�����ϲ����ť����תҳ��
			
			JButton play_history=new JButton("������ʷ");//����ð�ť��ת��������ʷ
			Add_Component(jp,play_history,Color.white,fontsize,0,main_menu_comp_h*3,main_menu_comp_w,main_menu_comp_h);
			btn_click(play_history,jp);//���������ʷ��ť����תҳ��
			
			JButton local_music=new JButton("��������");//����ð�ť��ת����������
			Add_Component(jp,local_music,Color.white,fontsize,0,main_menu_comp_h*4,main_menu_comp_w,main_menu_comp_h);
			btn_click(local_music,jp);//����������ְ�ť����תҳ��
			
			JLabel blank=new JLabel();//һ�οհ�����
			Add_Component(jp,blank,Color.white,fontsize,0,main_menu_comp_h*5,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel my_playlist=new JLabel("�ҵĸ赥",JLabel.CENTER);
			my_playlist.setBorder(BorderFactory.createLineBorder(Color.black,1,true));//���ñ߿�
			Add_Component(jp,my_playlist,Color.white,fontsize*3/4,0,main_menu_comp_h*6,main_menu_comp_w,main_menu_comp_h);
			
			String[] songlist = { "�赥һ", "�赥��", "�赥��","�赥��","�赥��"};//������и赥�����ַ������飬�ɵ��������Ӧ�ĸ赥
			JComboBox<String> playlist= new JComboBox<String>(songlist);//�������б��
			Add_Component(jp,playlist,Color.white,fontsize,0,main_menu_comp_h*7,main_menu_comp_w,main_menu_comp_h);
			playlist_click(playlist,jp);//�赥���������¼���������
			
			JList playlist_content=new JList(songlist);//�б�򣬿��������и赥�����ɵ��������Ӧ�ĸ赥
			playlist_content.setBorder(BorderFactory.createLineBorder(Color.black,1,true));
			Add_Component(jp,playlist_content,Color.white,fontsize,0,main_menu_comp_h*8,main_menu_comp_w,main_menu_comp_h*songlist.length/2);
			
			jf.setContentPane(jp);//������������
	    }
		
		//�ӽ���ȫ������ʾ��ť�������ֻ��ְ�ť����ר�����ְ�ť
		public void subpage_btn(JPanel jp)
		{
			JButton allsong_btn=new JButton("ȫ������ʾ");//ȫ������ʾ��ť
			Add_Component(jp,allsong_btn,Color.white,fontsize,main_menu_comp_w+2,main_menu_comp_h*2,main_menu_comp_w*2,main_menu_comp_h);
			
			JButton singer_div_btn=new JButton("�����ֻ���");//�����ֻ��ְ�ť
			Add_Component(jp,singer_div_btn,Color.white,fontsize,main_menu_comp_w*3,main_menu_comp_h*2,main_menu_comp_w*2,main_menu_comp_h);
			
			JButton album_div_btn=new JButton("��ר������");//��ר�����ְ�ť
			Add_Component(jp,album_div_btn,Color.white,fontsize,main_menu_comp_w*5,main_menu_comp_h*2,main_menu_comp_w*2,main_menu_comp_h);
			
			jf.setContentPane(jp);//������������
		}
		
		//��ϲ��-��ҳ��
	    public void mylove_page()
	    {
	    	// ����������壬ָ������Ϊ null����ʹ�þ��Բ���
	        JPanel jp = new JPanel(null);
	    	
	        left_main_page(jp);//�󲿽���
	        play_tool(jp);//�ײ����ֿ�����
	    	
	        top_main_page(jp);//���涥��
					
			JLabel playlist_topbar=new JLabel(" ��ϲ��������");
			playlist_topbar.setBorder(BorderFactory.createLineBorder(Color.black,1,true));//���ñ߿�
			Add_Component(jp,playlist_topbar,Color.white,fontsize,main_menu_comp_w+2,main_menu_comp_h,main_menu_comp_w*10,main_menu_comp_h);
			
			subpage_btn(jp);//�ӽ���Ĺ��ܰ�ť���棬ȫ������ʾ��ť�������ֻ��ְ�ť����ר�����ְ�ť
			
			JLabel songname=new JLabel("����");//������ǩ
			Add_Component(jp,songname,Color.white,fontsize,main_menu_comp_w*2,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel singername=new JLabel("����");//���ֱ�ǩ
			Add_Component(jp,singername,Color.white,fontsize,main_menu_comp_w*5,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel albumname=new JLabel("ר��");//ר����ǩ
			Add_Component(jp,albumname,Color.white,fontsize,main_menu_comp_w*8,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel songtime=new JLabel("ʱ��");//ʱ����ǩ
			Add_Component(jp,songtime,Color.white,fontsize,main_menu_comp_w*11,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			jf.setContentPane(jp);//������������
	    }
	    
	  //������ʷ-��ҳ��
	    public void playhistory_page()
	    {
	    	// ����������壬ָ������Ϊ null����ʹ�þ��Բ���
	        JPanel jp = new JPanel(null);
	    	
	        left_main_page(jp);//�󲿽���
	        play_tool(jp);//�ײ����ֿ�����
	    	
	        top_main_page(jp);//���涥��
					
			JLabel playlist_topbar=new JLabel(" ������ʷ");
			playlist_topbar.setBorder(BorderFactory.createLineBorder(Color.black,1,true));//���ñ߿�
			Add_Component(jp,playlist_topbar,Color.white,fontsize,main_menu_comp_w+2,main_menu_comp_h,main_menu_comp_w*10,main_menu_comp_h);
			
			subpage_btn(jp);//�ӽ��湦�ܰ�ť
			
			JButton playtime_sort_btn=new JButton("��ʱ������");//��ʱ������ť
			Add_Component(jp,playtime_sort_btn,Color.white,fontsize,main_menu_comp_w*7,main_menu_comp_h*2,main_menu_comp_w*2,main_menu_comp_h);
			
			JLabel songname=new JLabel("����");//������ǩ
			Add_Component(jp,songname,Color.white,fontsize,main_menu_comp_w*2,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel singername=new JLabel("����");//���ֱ�ǩ
			Add_Component(jp,singername,Color.white,fontsize,main_menu_comp_w*4,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel albumname=new JLabel("ר��");//ר����ǩ
			Add_Component(jp,albumname,Color.white,fontsize,main_menu_comp_w*6,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel songtime=new JLabel("ʱ��");//ʱ����ǩ
			Add_Component(jp,songtime,Color.white,fontsize,main_menu_comp_w*8,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel playdate=new JLabel("����ʱ��");//����ʱ���ǩ
			Add_Component(jp,playdate,Color.white,fontsize,main_menu_comp_w*10,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			jf.setContentPane(jp);//������������
	    }
	    
	  //��������-��ҳ��
	    public void localmusic_page()
	    {
	    	int index;
	    	// ����������壬ָ������Ϊ null����ʹ�þ��Բ���
	        JPanel jp = new JPanel(null);
	    	
	        left_main_page(jp);//�󲿽���
	        play_tool(jp);//�ײ����ֿ�����
	    	
	        top_main_page(jp);//���涥��
					
			JLabel playlist_topbar=new JLabel(" ��������");
			playlist_topbar.setBorder(BorderFactory.createLineBorder(Color.black,1,true));//���ñ߿�
			Add_Component(jp,playlist_topbar,Color.white,fontsize,main_menu_comp_w+2,main_menu_comp_h,main_menu_comp_w*10,main_menu_comp_h);
			
			subpage_btn(jp);//�ӽ��湦�ܰ�ť��ȫ������ʾ��ť�������ֻ��ְ�ť����ר�����ְ�ť
			
			JButton localsearch_btn=new JButton("��������");//�����������ְ�ť
			Add_Component(jp,localsearch_btn,Color.white,fontsize,main_menu_comp_w*7,main_menu_comp_h*2,main_menu_comp_w*2,main_menu_comp_h);
			btn_click(localsearch_btn,jp);
			
			String songPath = ".\\local_music";//ָ����Ŀ�д�Ÿ������ļ���Ŀ¼���ڸ��ļ����½��и�������
			File songFile = new File(songPath);
			ArrayList<ArrayList<String> > data=findMP3(jp,songFile,songPath);//�õ���Ŀ�и����ļ��е����и�����Ϣ
			String[][]songinfo=new String[data.size()][data.get(0).size()];//����һ����ά�ַ�������
			for(index=0;index<data.size();index++)
			{
				songinfo[index]=(String[])(data.get(index).toArray(new String[0]));
			}
			System.out.println(songinfo[0][0]);
			String []colname= {"����","����","ר��","ʱ��","��С"};//�������
			JTable table = new JTable(songinfo,colname){
				public boolean isCellEditable(int row, int column)//���ñ��Ϊ���ɱ༭״̬
				{
				return false;
				}
				};
			//��񰴼�����
			table.addMouseListener(new MouseAdapter() {//Ϊ�������Ҽ��˵�
			      public void mouseClicked(MouseEvent e){
			        if (e.getButton() == MouseEvent.BUTTON3){//Button3Ϊ�Ҽ���Button1Ϊ���
			          //��table��ʾ
			          JPopupMenu jpm = new JPopupMenu();//�Ҽ������Ĳ˵��ı���
			          //��� ��rowAtPoint���������������ڵ��кţ�����Ϊ�������ͣ�
			          int click_row = table.rowAtPoint(e.getPoint());//click_row�����û���������кţ��кŴ�0��ʼ
			          JMenuItem PlayItem = new JMenuItem("����");
			          JMenuItem LovedItem = new JMenuItem("�ղ�");
			          
			          // ��� һ���˵� �� �����˵�
			          jpm.add(PlayItem);
			          jpm.add(LovedItem);
			          // ��Ӳ˵���ĵ��������
			          jpm.show(e.getComponent(), e.getX(), e.getY());//�����Ҽ��������ǵ����˵�
			          //�Ҽ��˵�������ŵļ�����������
			          PlayItem.addActionListener(new ActionListener() {
			              @Override
			              public void actionPerformed(ActionEvent e) {
			                  System.out.println(songinfo[click_row][0]);//�õ�����ĸ���
			                  String []attribute= {"resource_url"};//Ҫ���ҵ���Ϣ
			                  //���ݵ���ĸ����������ݿ�����Ҹ������ļ����ڵ�·��
			                  String []res=sql_query("Select resource_url from song where song_name="+"'"+songinfo[click_row][0]
			                  +"'"+" and album_id="+"(Select album_id from album where album_name="+"'"+songinfo[click_row][2]+"')",attribute);
			                  System.out.println(res[0]);//����̨������������ļ�·��
			                  File file = new File(res[0]);
			                  
			      			  //���ò��ŷ������в���
			      			  try {
			      				  	FileInputStream fis = new FileInputStream(file);//������
			      				  	BufferedInputStream stream = new BufferedInputStream(fis);//������
			      				  	
			      				  	//�����һ�ε㲥���ظ���
			      				    if(music_switch==0)//��������if��Ϊ�˽����һ�ε㲥���ظ������ֲ��Ų��������
			      				    {
			      				    	music_switch++;
				                		player[0]=new Player(stream);//��player[0]����
				                		music_play(player[0]);
			      				    }
			      				    else//���ǵ�һ�β��ű��ظ���
			      				    {
			      				    	if(music_switch%2==0)
					                	{
					                		music_switch++;
					                		player[0]=new Player(stream);
					                		player[1].close();//�ر�����һ��playerʵ��ÿ��ֻ����һ�׸�
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
			        //�Ҽ��˵�����ղصļ�����������
			          LovedItem.addActionListener(new ActionListener() {
			              @Override
			              public void actionPerformed(ActionEvent e) {
			                  
			              }
			          });
			          
			        }
			      }
			    });
		    table.setFont(new Font("΢���ź�", Font.PLAIN, fontsize));//���ñ���е������С
		    table.setRowHeight(2*fontsize);//���ñ����и�
	        JScrollPane jsp = new JScrollPane(table);//�ɹ���ҳ��
			Add_Component(jp,jsp,Color.white,16,main_menu_comp_w+2,main_menu_comp_h*310/100,main_menu_comp_w*10,main_menu_comp_h*9);
			
			jf.setContentPane(jp);//������������
	    }
	    
	    
	    public void music_play(Player player)//�������ź���
	    {
	    	//����һ���̣߳���ֹ�㲥���ظ���ʱҳ����ȫ����������ռ�ö���ס
			  new Thread(()->{
			  //���ò��ŷ������в���
			  try {
              		player.play();
              	}
				 catch (Exception err) {
				}
			  }).start();
	    }
	    
	    
	  //�ҵĸ赥-��ҳ��
	    public void songlist_page(String listname)
	    {
	    	// ����������壬ָ������Ϊ null����ʹ�þ��Բ���
	        JPanel jp = new JPanel(null);
	    	
	        left_main_page(jp);//�󲿽���
	        play_tool(jp);//�ײ����ֿ�����
	    	
	        top_main_page(jp);//���涥��
					
			JLabel playlist_topbar=new JLabel(" �ҵĸ赥: "+listname);
			playlist_topbar.setBorder(BorderFactory.createLineBorder(Color.black,1,true));//���ñ߿�
			Add_Component(jp,playlist_topbar,Color.white,fontsize,main_menu_comp_w+2,main_menu_comp_h,main_menu_comp_w*10,main_menu_comp_h);
			
			subpage_btn(jp);//�ӽ��湦�ܰ�ť
			
			JLabel songname=new JLabel("����");//������ǩ
			Add_Component(jp,songname,Color.white,fontsize,main_menu_comp_w*2,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel singername=new JLabel("����");//���ֱ�ǩ
			Add_Component(jp,singername,Color.white,fontsize,main_menu_comp_w*5,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel albumname=new JLabel("ר��");//ר����ǩ
			Add_Component(jp,albumname,Color.white,fontsize,main_menu_comp_w*8,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			JLabel songtime=new JLabel("ʱ��");//ʱ����ǩ
			Add_Component(jp,songtime,Color.white,fontsize,main_menu_comp_w*11,main_menu_comp_h*322/100,main_menu_comp_w,main_menu_comp_h/3);
			
			jf.setContentPane(jp);//������������
	    }
	    
	    //���Ž������Ͳ��ſ��Ƽ�
	    public void play_tool(JPanel jp)
	    {
	    	JPanel jpr=new JPanel(null);
	    	JSlider song_slider=new JSlider(0,100);  //ָ����Сֵ�����ֵ�����Ǹ����϶��Ľ���������ʾ����Ƹ����Ĳ��Ž���
			song_slider.setValue(0);//���ý������ĳ�ʼֵΪ0
			Add_Component(jp,song_slider,Color.white,0,main_menu_comp_w+2,window_h*17/20,main_menu_comp_w*10,main_menu_comp_h/2);
			
			JButton play_mode = new JButton();//����ģʽ
			play_mode=changeIconSize("˳�򲥷�",play_mode,"./img/˳�򲥷�.png",main_menu_comp_w/2,main_menu_comp_h);
			Add_Component(jp,play_mode,Color.white,0,window_w*25/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
			btn_click(play_mode,jp);
			
			JButton last_song = new JButton();//��һ�װ�ť
			last_song=changeIconSize("��һ��",last_song,"./img/��һ��.png",main_menu_comp_w/2,main_menu_comp_h);
			Add_Component(jp,last_song,Color.white,0,window_w*36/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
			
			JButton play_btn = new JButton();//��ͣ/���Ű�ť
			play_btn=changeIconSize("��ͣ",play_btn,"./img/��ͣ.png",main_menu_comp_w/2,main_menu_comp_h);
			Add_Component(jp,play_btn,Color.white,0,window_w*45/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
			btn_click(play_btn,jp);
			
			JButton next_song = new JButton();//��һ�װ�ť
			next_song=changeIconSize("��һ��",next_song,"./img/��һ��.png",main_menu_comp_w/2,main_menu_comp_h);
			Add_Component(jp,next_song,Color.white,0,window_w*54/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
			
			JButton volume_btn = new JButton();//������ť
			volume_btn=changeIconSize("����",volume_btn,"./img/����.png",main_menu_comp_w/2,main_menu_comp_h);
			Add_Component(jp,volume_btn,Color.white,0,window_w*70/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
			btn_click(volume_btn,jp);
			
			JSlider volume_slider=new JSlider(0,100);  //ָ����Сֵ�����ֵ�����Ǹ����϶��Ľ���������ʾ����Ƹ����Ĳ��Ž���
			volume_slider.setValue(50);//���ý������ĳ�ʼֵΪ50
			Add_Component(jp,volume_slider,Color.white,0,window_w*75/100,window_h*91/100,main_menu_comp_w*2,main_menu_comp_h/2);
	    }
	    
	    
	  //��ť�¼�����
	  /*��ť������л���ťͼƬ��ԭ��Ϊ����ԭ��ť���������jp������ð�ť��Ȼ���½�һ����ť��������صİ�ťͼƬ�����ݵ�ǰ������������ò�ͬ�İ�ťͼƬ
	    ��ť������л�ҳ���ԭ��Ϊ��ԭ���jp���أ�Ȼ���ڲ�ͬ����ҳ�溯����������Ⱦҳ��  
	  * */
	    public void btn_click(JButton btn,JPanel jp)
	    {
	    	//Ϊ��ťbtn���ActionEvent�¼��Ĵ������
	    	btn.addActionListener(new ActionListener()
	    	{
				@Override
				public void actionPerformed(ActionEvent e) 
				{	
					
					if(btn.getActionCommand()=="��ϲ��")//��ȡ�����ť�����֣������ݲ�ͬ��ťִ�в�ͬ�ĺ���
					{
						jp.hide();//������תǰ�����
						mylove_page();
					}
					else if(btn.getActionCommand()=="������ʷ")//��ȡ�����ť�����֣������ݲ�ͬ��ťִ�в�ͬ�ĺ���
					{
						jp.hide();//������תǰ�����
						playhistory_page();
					}	
					else if(btn.getActionCommand()=="��������")//��ȡ�����ť�����֣������ݲ�ͬ��ťִ�в�ͬ�ĺ���
					{
						jp.hide();//������תǰ�����
						localmusic_page();
					}
						
					else if(btn.getActionCommand()=="��ҳ")//��ȡ�����ť�����֣������ݲ�ͬ��ťִ�в�ͬ�ĺ���
					{
						jp.hide();//������תǰ�����
						JPanel jpr=new JPanel(null);
						top_main_page(jpr);//��ҳ�������溯��
				        left_main_page(jpr);//��ҳ�󲿽��溯��
				        play_tool(jpr);//�·������ֲ��Ź���
				        JLabel playlist_topbar=new JLabel(" �赥�Ƽ�");
						playlist_topbar.setBorder(BorderFactory.createLineBorder(Color.black,1,true));//���ñ߿�
						Add_Component(jpr,playlist_topbar,Color.white,fontsize,main_menu_comp_w+2,main_menu_comp_h,main_menu_comp_w*10,main_menu_comp_h);
					}
					else if(btn.getActionCommand()=="��ͣ"||btn.getActionCommand()=="����")//��ȡ�����ť�����֣������ݲ�ͬ��ťִ�в�ͬ�ĺ���
					{
						btn.hide();//����ԭ��ť��ʹ�õ�����������л���ťͼƬ
						jp.remove(btn);//��������Ƴ�ԭ��ť
						play_btn_clicknum++;//�����������
						JButton play_btn = new JButton();//���Ű�ť
						if(play_btn_clicknum%2==1)//���ݵ���������л���ťͼƬ
						{
							play_btn=changeIconSize("����",play_btn,"./img/����.png",main_menu_comp_w/2,main_menu_comp_h);
							Add_Component(jp,play_btn,Color.white,0,window_w*45/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
							btn_click(play_btn,jp);
						}
						else
						{
							play_btn=changeIconSize("��ͣ",play_btn,"./img/��ͣ.png",main_menu_comp_w/2,main_menu_comp_h);
							Add_Component(jp,play_btn,Color.white,0,window_w*45/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
							btn_click(play_btn,jp);
						}
					}
					else if(btn.getActionCommand()=="����"||btn.getActionCommand()=="����")//��ȡ�����ť�����֣������ݲ�ͬ��ťִ�в�ͬ�ĺ���
					{
						btn.hide();//����ԭ��ť��ʹ�õ�����������л���ťͼƬ
						jp.remove(btn);//��������Ƴ�ԭ��ť
						volume_btn_clicknum++;//�����������
						JButton volume_btn = new JButton();//������ť
						if(volume_btn_clicknum%2==1)//���ݵ���������л���ťͼƬ
						{
							volume_btn=changeIconSize("����",volume_btn,"./img/����.png",main_menu_comp_w/2,main_menu_comp_h);
							Add_Component(jp,volume_btn,Color.white,0,window_w*70/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
							btn_click(volume_btn,jp);
						}
						else
						{
							volume_btn=changeIconSize("����",volume_btn,"./img/����.png",main_menu_comp_w/2,main_menu_comp_h);
							Add_Component(jp,volume_btn,Color.white,0,window_w*70/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
							btn_click(volume_btn,jp);
						}
					}
					else if(btn.getActionCommand()=="˳�򲥷�"||btn.getActionCommand()=="����ѭ��"||btn.getActionCommand()=="�������")//��ȡ�����ť�����֣������ݲ�ͬ��ťִ�в�ͬ�ĺ���
					{
						btn.hide();//����ԭ��ť��ʹ�õ�����������л���ťͼƬ
						jp.remove(btn);//��������Ƴ�ԭ��ť
						playmode_clicknum++;//�����������
						JButton playmode_btn = new JButton();//������ť
						if(playmode_clicknum%3==0)//���ݵ���������л���ťͼƬ
						{
							playmode_btn=changeIconSize("˳�򲥷�",playmode_btn,"./img/˳�򲥷�.png",main_menu_comp_w/2,main_menu_comp_h);
							Add_Component(jp,playmode_btn,Color.white,0,window_w*25/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
							btn_click(playmode_btn,jp);
						}
						else if(playmode_clicknum%3==1)//���ݵ���������л���ťͼƬ
						{
							playmode_btn=changeIconSize("����ѭ��",playmode_btn,"./img/����ѭ��.png",main_menu_comp_w/2,main_menu_comp_h);
							Add_Component(jp,playmode_btn,Color.white,0,window_w*25/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
							btn_click(playmode_btn,jp);
						}
						else if(playmode_clicknum%3==2)//���ݵ���������л���ťͼƬ
						{
							playmode_btn=changeIconSize("�������",playmode_btn,"./img/�������.png",main_menu_comp_w/2,main_menu_comp_h);
							Add_Component(jp,playmode_btn,Color.white,0,window_w*25/100,window_h*8/9,main_menu_comp_w*3/5,main_menu_comp_h);
							btn_click(playmode_btn,jp);
						}
					}
					
					else if(btn.getActionCommand()=="��������")//��ȡ�����ť�����֣������ݲ�ͬ��ťִ�в�ͬ�ĺ���
					{
											
						String songPath = "C:\\Users\\apple\\Desktop\\Music";//ָ���ļ���Ŀ¼���ڸ��ļ����½��и�������
						File songFile = new File(songPath);
						System.out.println("\nFind the MP3 files in the specified directory("
								+ songPath + "): ");
						//findMP3(jp,songFile, songPath);

					}
					
					else if(btn.getActionCommand()=="����")//��ȡ�����ť�����֣������ݲ�ͬ��ťִ�в�ͬ�ĺ���
					{
						search_str=search_bar.getText();//��ȡ�����������
						System.out.println(search_str);
						String search_urlencode=new String();
						try {
							search_urlencode=URLEncoder.encode(search_str,"UTF-8");//���������ݽ���ת��
						}
						catch (Exception err) {
				            // TODO Auto-generated catch block
				            err.printStackTrace();
				        }
						//Ҫ���ʵ����ֵĶ�Ӧ��ַ
//						http://tool.liumingye.cn/music/?page=audioPage&type=migu&name="+search_urlencode
						String strurl="http://tool.liumingye.cn/music/?page=audioPage&type=migu&name="+search_urlencode;
						System.out.println(strurl);
						
						
						
  
					}
				}
			});
	    }
	    
	    //�赥�б����
    public void playlist_click(JComboBox<String> jcb,JPanel jp) {
    	jcb.addActionListener(new ActionListener()
    	{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				jp.hide();//������תǰ��һ�������panel
				jf.getContentPane().removeAll();//��jf���������������
				if(jcb.getSelectedItem()=="�赥һ")//��ȡ����б�����֣������ݲ�ͬ��ťִ�в�ͬ�ĺ���
					songlist_page("�赥һ");
				else if(jcb.getSelectedItem()=="�赥��")//��ȡ����б�����֣������ݲ�ͬ��ťִ�в�ͬ�ĺ���
					songlist_page("�赥��");
				else if(jcb.getSelectedItem()=="�赥��")//��ȡ����б�����֣������ݲ�ͬ��ťִ�в�ͬ�ĺ���
					songlist_page("�赥��");
				else if(jcb.getSelectedItem()=="�赥��")//��ȡ����б�����֣������ݲ�ͬ��ťִ�в�ͬ�ĺ���
					songlist_page("�赥��");
				else if(jcb.getSelectedItem()=="�赥��")//��ȡ����б�����֣������ݲ�ͬ��ťִ�в�ͬ�ĺ���
					songlist_page("�赥��");
			}
		});
    }

	    
    //������
    public static void main(String[] args) 
    {
    	//��ҳ��ʼ��
    	Music_player mp=new Music_player();
    	mp.top_main_page(mp.jp);//��ҳ�������溯��
    	mp.play_tool(mp.jp);//�·������ֲ��Ź���
        mp.left_main_page(mp.jp);//��ҳ�󲿽��溯��
        JLabel playlist_topbar=new JLabel(" �赥�Ƽ�");
		playlist_topbar.setBorder(BorderFactory.createLineBorder(Color.black,1,true));//���ñ߿�
		Add_Component(mp.jp,playlist_topbar,Color.white,mp.fontsize,mp.main_menu_comp_w+2,mp.main_menu_comp_h,mp.main_menu_comp_w*10,mp.main_menu_comp_h);
        
    }
    
    //���ð�ť����ͼƬ��С����Ӧ�ؼ����ð�ť��ͼƬ�Ͱ�ť��С����һ��
    public static JButton changeIconSize(String btn_name,JButton button,String url,int width,int height)
    {//btn_nameΪ��ť���ƣ�buttonΪ���Ҫ����ͼƬ�İ�ť��urlΪ��ťͼƬ�ڱ����ļ��е�·����widthΪ��ť��ȣ�heightΪ��ť�߶�
        button.setBounds(0,0,width,height);//���ð�ť��С
        ImageIcon buttonImg=new ImageIcon(url);//���ð�ťͼƬ
        //�ı�ͼƬ�Ĵ�С
        Image temp=buttonImg.getImage().getScaledInstance(button.getWidth(), button.getHeight(), buttonImg.getImage().SCALE_DEFAULT);
        button=new JButton(new ImageIcon(temp));
        button.setBorderPainted(false);//ȥ��ť�߿�
		button.setOpaque(false);//����ť����Ϊ͸��
		button.setText(btn_name);
        return button;
    }
    
    
    //�����Ӻ���
  	public static void Add_Component(JPanel jp,Component comp,Color color,int fontsize,int x,int y,int width,int height)
  	{
  		comp.setLocation(x, y);//���������λ��
  		comp.setSize(width, height);//��������Ĵ�С
  		comp.setBackground(color);//��������ı�����ɫ
  		comp.setFont(new Font("����",Font.BOLD,fontsize));//��������ϵı�ǩ������ʽ
  		jp.add(comp);//��������������
  	}

  	//����ĳ�����ļ���Ŀ¼�µ�MP3�ļ�
  	public ArrayList <ArrayList<String> > findMP3(JPanel jp,File songFile, String songPath)//��ArrayList<String>���㽫�ַ�����ӵ��ַ����б�
  	{
  		ArrayList <ArrayList<String> > all_songinfo=new ArrayList <ArrayList<String> >();//ģ��һ����ά���飬���������ҵ��ĸ����Ϣ
  		Map<String, String> map = new HashMap<String,String>();//���ù�ϣ��ṹ���Լ�ֵ������Ÿ�����������Ϣ
  		String songname=new String();//��Ÿ�����
  		String singername=new String();//��Ÿ�����
  		String albumname=new String();//���ר����
  		String songtime=new String();//��Ÿ���ʱ��
  		String songsize=new String();//��Ÿ����ļ���С
  		String songurl=new String();//��Ÿ���·��
  		String release_year=new String();//��ŷ���ʱ��
  		DecimalFormat df = new DecimalFormat("0.00");//�����ļ���С������λС��
		File[] songList = songFile.listFiles();

		if (songList != null) 
		{
			for (File s : songList) 
			{
				// �õݹ鷨�����Ҹ�����Ŀ¼
				if (s.isDirectory() == true) 
				{
					findMP3(jp,s,s.getAbsolutePath());
				} 
				else 
				{
					// String���µ�endsWith���ж��ļ���׺
					if (s.getAbsolutePath().endsWith(".mp3") == true|| s.getAbsolutePath().endsWith("MP3")== true) 
					{		
						try 
						{
							//String str=s.getAbsolutePath().replaceAll("\\\\","\\\\\\\\");
							Mp3File mp3file=new Mp3File(s.getAbsolutePath());
							if (mp3file.hasId3v2Tag())
							{
								//���´���Ϊ���뱾�����ֽ���ĸ������ʼ����׼��
					            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
					            //songname=s.getAbsolutePath().substring(s.getAbsolutePath().lastIndexOf("\\")+1,s.getAbsolutePath().lastIndexOf("."));//����
					            songname=id3v2Tag.getTitle();//������
					            singername=id3v2Tag.getArtist();//������
					            albumname=id3v2Tag.getAlbum();//ר����
					            songtime=mp3file.getLengthInSeconds()/60+":"+mp3file.getLengthInSeconds()%60;//����ʱ��
					            songsize=df.format(s.length()/1024/1024.0)+"MB";//������С
					            //��ÿ�׸��������Ϣ����һ���ַ��������ÿ��һά�����ŵ��ַ�����Ϣ�ֱ�Ϊ��������������ר����������ʱ����������С
					            ArrayList<String> songinfo = new ArrayList<String>();//��Ÿ��׸�ĸ�����Ϣ
					            songinfo.add(songname);
					            songinfo.add(singername);
					            songinfo.add(albumname);
					            songinfo.add(songtime);
					            songinfo.add(songsize);
					            all_songinfo.add(songinfo);
					            
					            
					            //���´���������������ݿ⣬��������Ϣ���������
					            songurl=s.getAbsolutePath();//�õ�����·��
					            release_year=id3v2Tag.getYear();//��÷���ʱ��
					            //���������������Լ�ֵ�Ե���ʽ�洢���������Ϊ���ݿ��еı������Ϣʱ����һ�������н��ж����б�Ĳ���������Ϊÿ��������һ����Ӻ���
					            map.put("songurl",songurl);
					            map.put("release_year", release_year);
					            map.put("albumname",albumname);
					            map.put("songname",songname);
					            map.put("singername",singername);
					            map.put("songtime",songtime);
					            map.put("songsize",songsize);

					            //�������ֽ�is_localͳһ��Ϊtrue��is_lovedͳһ��Ϊfalse
					            //Ϊר���������Ϣ
					            sql_insert("album",map,true,false);//albumΪ������map����Ҫ�����Ԫ����Ϣ
					            //Ϊ���ֱ������Ϣ
					            sql_insert("singer",map,true,false);
					            //Ϊ�����������Ϣ
					            sql_insert("song",map,true,false);
					            //Ϊ���ڹ�ϵ�������Ϣ
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
						//System.out.println(s.getAbsolutePath()+" "+df.format(s.length()/1024/1024.0)+"MB");//�õ������ļ���С
						
					}
				}
			}
		}
	return all_songinfo;//����һ����ά�ַ�������	
	}

  	
  	public void connect_sql()//�������ݿ�
  	{
  		
        try {
            String url = "jdbc:postgresql://localhost:5432/Music";//�����Լ�PostgreSQL���ݿ�ʵ�����ڵ�ip��ַ���������Լ��Ķ˿�
            String user = "postgres";//�����û���
            String password = "123456";  //��������
            Class.forName("org.postgresql.Driver"); //JDBC
            connection= DriverManager.getConnection(url, user, password);
            System.out.println("���ݿ����ӳɹ�"+connection);
            
            //statement.close();
            //connection.close();
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(null, "����δ���ӱ������ݿ⣬�����Ӻ��ٴ򿪱����", "��ʾ",JOptionPane.INFORMATION_MESSAGE);
        	System.exit(0);//�˳�����
            //e.printStackTrace();
        }
  	}
  	
  	public void sql_insert(String tablename,Map<String, String> map,boolean is_local,boolean is_loved)//���ݿ����ݲ��뺯��
  	{
  		String sql=new String();
  		//connection��statement�ѱ�����Ϊȫ�ֹ��������������ݿ�һ��ʼ�����ӵ�λ������������
		try {
			//connection.setAutoCommit(false);//���Զ��ύ
			statement = connection.createStatement();
			//Ϊר������������
			if(tablename=="album"&&statement.executeQuery("Select album_name from album where album_name="+"'"+map.get("albumname")+"'").next()==false)//��ר�������Ҳ�����ר���������ר����
			{
				sql = "INSERT INTO "+tablename+" (album_name,publish_time) "//sql���
				+ "VALUES ("+"'"+map.get("albumname")+"'"+","+"'"+map.get("release_year")+"'"+");";//ע��value����ַ���Ҫ�á���������������sql����޷�����
				statement.executeUpdate(sql);//ִ��sql���
			}
			//Ϊ���ֱ���������
			else if(tablename=="singer"&&statement.executeQuery("Select singer_name from singer where singer_name="+"'"+map.get("singername")+"'").next()==false)//�����ֱ����Ҳ����ø������������ֱ�
			{
				sql = "INSERT INTO "+tablename+" (singer_name,singer_pic_url) "//sql���
				+ "VALUES ("+"'"+map.get("singername")+"'"+","+"'"+map.get("singer_pic_url")+"'"+");";//ע��value����ַ���Ҫ�á���������������sql����޷�����
				statement.executeUpdate(sql);
			}
			
			//Ϊ��������������,�ø�������ר�������������׸���֮ǰ�Ƿ�¼�룬�����׸�֮ǰ�Ѿ�¼�룬������ӽ����ݿ�
			else if(tablename=="song")
			{
				//������������Ѿ���song����
				if(statement.executeQuery("Select song_name from song where song_name="+"'"+map.get("songname")+"'").next()==true)
				{
					ResultSet rs=statement.executeQuery("Select album_id from song where song_name="+"'"+map.get("songname")+"'");
					rs.next();//ʹ���α���ƣ��������������Ҫ������
					String albumid = rs.getString("album_id");//�õ�song���и��׸�����album_id
					
					rs=statement.executeQuery("Select album_name from album where album_id="+"'"+albumid+"'");
					rs.next();//ʹ���α���ƣ��������������Ҫ������
					String albumname = rs.getString("album_name");//�õ�album���и�ר��������
					if(albumname!=map.get("albumname"))//������������ĸ�����song���е�ͬ��������ר����һ�������������ͬ�������song��
					{
						sql = "INSERT INTO "+tablename+" (song_name,total_time,song_size,resource_url,is_local,is_loved,album_id) "//sql���
								+ "VALUES ("+"'"+map.get("songname")+"'"+","+"'"+map.get("songtime")+"'"+","+"'"+map.get("songsize")+"'"
								+","+"'"+map.get("songurl")+"'"+","+is_local+","+is_loved+",(Select album_id from album where album_name="+"'"+map.get("albumname")+"')"+");";//ע��value����ַ���Ҫ�á���������������sql����޷�����
						statement.executeUpdate(sql);
					}
				}
				else//���������������song���У���ֱ�Ӳ���
				{
					sql = "INSERT INTO "+tablename+" (song_name,total_time,song_size,resource_url,is_local,is_loved,album_id) "//sql���
							+ "VALUES ("+"'"+map.get("songname")+"'"+","+"'"+map.get("songtime")+"'"+","+"'"+map.get("songsize")+"'"
							+","+"'"+map.get("songurl")+"'"+","+is_local+","+is_loved+",(Select album_id from album where album_name="+"'"+map.get("albumname")+"')"+");";//ע��value����ַ���Ҫ�á���������������sql����޷�����
					statement.executeUpdate(sql);
				}
			}
			
			else if(tablename=="belong_relation")
			{
				ResultSet rs=statement.executeQuery("Select song_id from song where song_name="+"'"+map.get("songname")+"'");
				rs.next();//ʹ���α���ƣ��������������Ҫ������
				String songid = rs.getString("song_id");//�õ�song���и��׸������
				
				rs=statement.executeQuery("Select singer_id from singer where singer_name="+"'"+map.get("singername")+"'");
				rs.next();//ʹ���α���ƣ��������������Ҫ������
				String singerid = rs.getString("singer_id");//�õ�singer���иø��ֵ�id
				
				sql = "INSERT INTO "+tablename+" (song_id,singer_id) "//sql���
						+ "VALUES ("+"'"+songid+"'"+","+"'"+singerid+"'"+");";//ע��value����ַ���Ҫ�á���������������sql����޷�����
				statement.executeUpdate(sql);
			}
			
			System.out.println(sql);
			
			//connection.commit();
 
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("��������ʧ�ܣ�");
			//System.exit(0);
		}
		System.out.println("�������ݳɹ���");
  	}
  	
  	
  	
  	public void sql_delete(String sql)//���ݿ�����ɾ������
  	{
  		try
  		{
  			statement.executeUpdate(sql);
  		}
  		catch (Exception e) 
  		{
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("ɾ������ʧ�ܣ�");
		}
  	}
  	
  	public String[] sql_query(String sql,String [] attribute)//���ݿ����ݲ�ѯ������attribute���Ҫ��ѯ��ĳ�����������
  	{
  		int index=0;
  		String [] result=new String[attribute.length];//result��Ų�ѯ�󷵻�ÿ�м�¼�ĵĸ�������ֵ��ɵ��ַ�������
		try
  		{
  			ResultSet rs=statement.executeQuery(sql);
  			while(rs.next())//ʹ���α���ƣ��������������Ҫ������
  			{
  				result[index]=rs.getString(attribute[index]);
  				index++;
  			}
  		}
  		catch (Exception e) 
  		{
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("�޸�����ʧ�ܣ�");
		}
		return result;
  	}
  	
  	public void sql_modify(String sql)//���ݿ������޸ĺ���
  	{
  		try
  		{
  			statement.executeUpdate(sql);
  		}
  		catch (Exception e) 
  		{
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("�޸�����ʧ�ܣ�");
		}
  	}
  	
}