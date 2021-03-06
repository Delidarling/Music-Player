PGDMP         3                y            Music    10.15    10.15 /    &           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                       false            '           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                       false            (           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                       false            )           1262    49213    Music    DATABASE     ?   CREATE DATABASE "Music" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Chinese (Simplified)_China.936' LC_CTYPE = 'Chinese (Simplified)_China.936';
    DROP DATABASE "Music";
             postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
             postgres    false            *           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                  postgres    false    3                        3079    12924    plpgsql 	   EXTENSION     ?   CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
    DROP EXTENSION plpgsql;
                  false            +           0    0    EXTENSION plpgsql    COMMENT     @   COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';
                       false    1            ?            1259    74107    add_relation    TABLE     ?   CREATE TABLE public.add_relation (
    playlist_id bigint NOT NULL,
    song_id bigint NOT NULL,
    add_time timestamp without time zone
);
     DROP TABLE public.add_relation;
       public         postgres    false    3            ?            1259    74071    album    TABLE     ?   CREATE TABLE public.album (
    album_id integer NOT NULL,
    album_name character varying(255),
    publish_time character varying(255)
);
    DROP TABLE public.album;
       public         postgres    false    3            ?            1259    74069    album_album_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.album_album_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.album_album_id_seq;
       public       postgres    false    3    199            ,           0    0    album_album_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.album_album_id_seq OWNED BY public.album.album_id;
            public       postgres    false    198            ?            1259    74122    belong_relation    TABLE     d   CREATE TABLE public.belong_relation (
    singer_id bigint NOT NULL,
    song_id bigint NOT NULL
);
 #   DROP TABLE public.belong_relation;
       public         postgres    false    3            ?            1259    74098    playlist    TABLE     ?   CREATE TABLE public.playlist (
    playlist_id integer NOT NULL,
    playlist_name character varying(255),
    create_time character varying(255)
);
    DROP TABLE public.playlist;
       public         postgres    false    3            ?            1259    74096    playlist_playlist_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.playlist_playlist_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.playlist_playlist_id_seq;
       public       postgres    false    3    203            -           0    0    playlist_playlist_id_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.playlist_playlist_id_seq OWNED BY public.playlist.playlist_id;
            public       postgres    false    202            ?            1259    74060    singer    TABLE     ?   CREATE TABLE public.singer (
    singer_id integer NOT NULL,
    singer_name character varying(255),
    singer_pic_url character varying(255)
);
    DROP TABLE public.singer;
       public         postgres    false    3            ?            1259    74058    singer_singer_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.singer_singer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.singer_singer_id_seq;
       public       postgres    false    3    197            .           0    0    singer_singer_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.singer_singer_id_seq OWNED BY public.singer.singer_id;
            public       postgres    false    196            ?            1259    74082    song    TABLE     g  CREATE TABLE public.song (
    song_id integer NOT NULL,
    song_name character varying(255),
    total_time character varying(50),
    song_size character varying(50),
    play_time timestamp without time zone,
    resource_url character varying(255),
    lyric_url character varying(255),
    album_id bigint,
    is_local boolean,
    is_loved boolean
);
    DROP TABLE public.song;
       public         postgres    false    3            ?            1259    74080    song_song_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.song_song_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.song_song_id_seq;
       public       postgres    false    3    201            /           0    0    song_song_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.song_song_id_seq OWNED BY public.song.song_id;
            public       postgres    false    200            ?
           2604    74074    album album_id    DEFAULT     p   ALTER TABLE ONLY public.album ALTER COLUMN album_id SET DEFAULT nextval('public.album_album_id_seq'::regclass);
 =   ALTER TABLE public.album ALTER COLUMN album_id DROP DEFAULT;
       public       postgres    false    198    199    199            ?
           2604    74101    playlist playlist_id    DEFAULT     |   ALTER TABLE ONLY public.playlist ALTER COLUMN playlist_id SET DEFAULT nextval('public.playlist_playlist_id_seq'::regclass);
 C   ALTER TABLE public.playlist ALTER COLUMN playlist_id DROP DEFAULT;
       public       postgres    false    203    202    203            ?
           2604    74063    singer singer_id    DEFAULT     t   ALTER TABLE ONLY public.singer ALTER COLUMN singer_id SET DEFAULT nextval('public.singer_singer_id_seq'::regclass);
 ?   ALTER TABLE public.singer ALTER COLUMN singer_id DROP DEFAULT;
       public       postgres    false    197    196    197            ?
           2604    74085    song song_id    DEFAULT     l   ALTER TABLE ONLY public.song ALTER COLUMN song_id SET DEFAULT nextval('public.song_song_id_seq'::regclass);
 ;   ALTER TABLE public.song ALTER COLUMN song_id DROP DEFAULT;
       public       postgres    false    200    201    201            "          0    74107    add_relation 
   TABLE DATA               F   COPY public.add_relation (playlist_id, song_id, add_time) FROM stdin;
    public       postgres    false    204   ?3                 0    74071    album 
   TABLE DATA               C   COPY public.album (album_id, album_name, publish_time) FROM stdin;
    public       postgres    false    199   ?3       #          0    74122    belong_relation 
   TABLE DATA               =   COPY public.belong_relation (singer_id, song_id) FROM stdin;
    public       postgres    false    205   ?3       !          0    74098    playlist 
   TABLE DATA               K   COPY public.playlist (playlist_id, playlist_name, create_time) FROM stdin;
    public       postgres    false    203   4                 0    74060    singer 
   TABLE DATA               H   COPY public.singer (singer_id, singer_name, singer_pic_url) FROM stdin;
    public       postgres    false    197   $4                 0    74082    song 
   TABLE DATA               ?   COPY public.song (song_id, song_name, total_time, song_size, play_time, resource_url, lyric_url, album_id, is_local, is_loved) FROM stdin;
    public       postgres    false    201   A4       0           0    0    album_album_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.album_album_id_seq', 1, false);
            public       postgres    false    198            1           0    0    playlist_playlist_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.playlist_playlist_id_seq', 1, false);
            public       postgres    false    202            2           0    0    singer_singer_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.singer_singer_id_seq', 1, false);
            public       postgres    false    196            3           0    0    song_song_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.song_song_id_seq', 1, false);
            public       postgres    false    200            ?
           2606    74111    add_relation add_relation_pkey 
   CONSTRAINT     n   ALTER TABLE ONLY public.add_relation
    ADD CONSTRAINT add_relation_pkey PRIMARY KEY (playlist_id, song_id);
 H   ALTER TABLE ONLY public.add_relation DROP CONSTRAINT add_relation_pkey;
       public         postgres    false    204    204            ?
           2606    74079    album album_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.album
    ADD CONSTRAINT album_pkey PRIMARY KEY (album_id);
 :   ALTER TABLE ONLY public.album DROP CONSTRAINT album_pkey;
       public         postgres    false    199            ?
           2606    74126 $   belong_relation belong_relation_pkey 
   CONSTRAINT     r   ALTER TABLE ONLY public.belong_relation
    ADD CONSTRAINT belong_relation_pkey PRIMARY KEY (singer_id, song_id);
 N   ALTER TABLE ONLY public.belong_relation DROP CONSTRAINT belong_relation_pkey;
       public         postgres    false    205    205            ?
           2606    74106    playlist playlist_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.playlist
    ADD CONSTRAINT playlist_pkey PRIMARY KEY (playlist_id);
 @   ALTER TABLE ONLY public.playlist DROP CONSTRAINT playlist_pkey;
       public         postgres    false    203            ?
           2606    74068    singer singer_pkey 
   CONSTRAINT     W   ALTER TABLE ONLY public.singer
    ADD CONSTRAINT singer_pkey PRIMARY KEY (singer_id);
 <   ALTER TABLE ONLY public.singer DROP CONSTRAINT singer_pkey;
       public         postgres    false    197            ?
           2606    74090    song song_pkey 
   CONSTRAINT     Q   ALTER TABLE ONLY public.song
    ADD CONSTRAINT song_pkey PRIMARY KEY (song_id);
 8   ALTER TABLE ONLY public.song DROP CONSTRAINT song_pkey;
       public         postgres    false    201            ?
           2606    74112 *   add_relation add_relation_playlist_id_fkey    FK CONSTRAINT     ?   ALTER TABLE ONLY public.add_relation
    ADD CONSTRAINT add_relation_playlist_id_fkey FOREIGN KEY (playlist_id) REFERENCES public.playlist(playlist_id);
 T   ALTER TABLE ONLY public.add_relation DROP CONSTRAINT add_relation_playlist_id_fkey;
       public       postgres    false    203    204    2711            ?
           2606    74117 &   add_relation add_relation_song_id_fkey    FK CONSTRAINT     ?   ALTER TABLE ONLY public.add_relation
    ADD CONSTRAINT add_relation_song_id_fkey FOREIGN KEY (song_id) REFERENCES public.song(song_id);
 P   ALTER TABLE ONLY public.add_relation DROP CONSTRAINT add_relation_song_id_fkey;
       public       postgres    false    204    201    2709            ?
           2606    74127 .   belong_relation belong_relation_singer_id_fkey    FK CONSTRAINT     ?   ALTER TABLE ONLY public.belong_relation
    ADD CONSTRAINT belong_relation_singer_id_fkey FOREIGN KEY (singer_id) REFERENCES public.singer(singer_id);
 X   ALTER TABLE ONLY public.belong_relation DROP CONSTRAINT belong_relation_singer_id_fkey;
       public       postgres    false    205    2705    197            ?
           2606    74132 ,   belong_relation belong_relation_song_id_fkey    FK CONSTRAINT     ?   ALTER TABLE ONLY public.belong_relation
    ADD CONSTRAINT belong_relation_song_id_fkey FOREIGN KEY (song_id) REFERENCES public.song(song_id);
 V   ALTER TABLE ONLY public.belong_relation DROP CONSTRAINT belong_relation_song_id_fkey;
       public       postgres    false    201    205    2709            ?
           2606    74091    song song_album_id_fkey    FK CONSTRAINT     }   ALTER TABLE ONLY public.song
    ADD CONSTRAINT song_album_id_fkey FOREIGN KEY (album_id) REFERENCES public.album(album_id);
 A   ALTER TABLE ONLY public.song DROP CONSTRAINT song_album_id_fkey;
       public       postgres    false    2707    201    199            "      x?????? ? ?            x?????? ? ?      #      x?????? ? ?      !      x?????? ? ?            x?????? ? ?            x?????? ? ?     