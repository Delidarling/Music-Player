# Music-Player
A music player which can play the local music.
## 使用说明
### 1. 运行前先导入压缩包内的数据库文件Music.sql，并启动postgre数据库服务，注意postgre数据库的用户名为postgres，密码为123456。
### 若sql导入失败，则用下列语句自行创建数据库。
--数据库创建语句
create table singer
(
	singer_id SERIAL  not null  PRIMARY KEY,
	singer_name varchar(255),
	singer_pic_url varchar(255)
	
)

create table album
(
	album_id SERIAL  not null  PRIMARY KEY,
	album_name varchar(255),
	publish_time varchar(255)
)


create table song
(
	song_id SERIAL  not null  PRIMARY KEY,
	song_name varchar(255),
	total_time varchar(50),
	song_size varchar(50),
	play_time timestamp,
	resource_url varchar(255),
	lyric_url varchar(255),
	album_id bigint REFERENCES album (album_id),
	is_local boolean,
	is_loved boolean
)

create table playlist
(
	playlist_id SERIAL  not null  PRIMARY KEY,
	playlist_name varchar(255),
	create_time varchar(255)
	
)

create table add_relation
(
	playlist_id bigint references playlist(playlist_id) ,
	song_id bigint  references song(song_id) ,
	add_time timestamp,
	PRIMARY KEY (playlist_id, song_id)
	
)

create table belong_relation
(
	singer_id bigint references singer(singer_id) ,
	song_id bigint references song(song_id),
	PRIMARY KEY (singer_id, song_id)
	
)
### 2. img文件夹内存有软件播放栏界面中所用到的各个按钮的图片
### lib文件夹内存有本软件所需要导入的外部包，运行之前需要全部导入到Java项目中
### local_music为本地音乐文件夹，软件将从该文件夹内读取本地音乐。
### 3. 成功启动软件后，点击本地音乐按钮，进入本地音乐子界面，在显示的音乐信息表中右键点击您想播放的歌曲，在右键点击后弹出的菜单栏中选	择“播放”即可播放歌曲。
