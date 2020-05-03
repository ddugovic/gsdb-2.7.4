//
// GNU SHOGI DATABASE PROJECT
// GN                      CT
// GN       G.S.D.B.       CT
// GN                      CT
// GNU SHOGI DATABASE PROJECT
//
//   This source falls under the    
// 
//              GNU GENERAL PUBLIC LICENSE
//
//   please check the file COPYING in this directory
//
//

import java.awt.*;
import java.applet.*;
import java.net.URL;
import java.net.Socket;
import java.io.*;



////////////////////////////////////////////////////////////////
//////         Const class                               ////////
////////////////////////////////////////////////////////////////


public class Const
{
	public static final String VERSION = new String("2.7.3");

	public static final int BLACK           =  0;
	public static final int WHITE           =  1;

	public static final int UP              = - 9;
	public static final int DOWN            =   9;
	public static final int LEFT            = - 1;
	public static final int RIGHT           =   1;
	public static final int LEFTUP          = -10;
	public static final int RIGHTUP         = - 8;
	public static final int LEFTDOWN        =   8;
	public static final int RIGHTDOWN       =  10;
	public static final int LEFTUPL         = -19;
	public static final int RIGHTUPL        = -17;
	public static final int LEFTDOWNL       =  17;
	public static final int RIGHTDOWNL      =  19;

	public static final int DROP            = 999;

	public static final int EMPTY           = 0;
	public static final int PAWN            = 1;
	public static final int LANCE           = 2;
	public static final int KNIGHT          = 3;
	public static final int SILVER          = 4;
	public static final int GOLD            = 5;
	public static final int BISHOP          = 6;
	public static final int ROOK            = 7;
	public static final int KING            = 8;
	public static final int PPAWN           = 9;
	public static final int PLANCE          =10;
	public static final int PKNIGHT         =11;
	public static final int PSILVER         =12;
	public static final int PBISHOP         =13;
	public static final int PROOK           =14;

	public static final int BLACK_PAWN      = 1;
	public static final int BLACK_LANCE     = 2;
	public static final int BLACK_KNIGHT    = 3;
	public static final int BLACK_SILVER    = 4;
	public static final int BLACK_GOLD      = 5;
	public static final int BLACK_BISHOP    = 6;
	public static final int BLACK_ROOK      = 7;
	public static final int BLACK_KING      = 8;
	public static final int BLACK_PPAWN     = 9;
	public static final int BLACK_PLANCE    =10;
	public static final int BLACK_PKNIGHT   =11;
	public static final int BLACK_PSILVER   =12;
	public static final int BLACK_PBISHOP   =13;
	public static final int BLACK_PROOK     =14;

	public static final int WHITE_PAWN      =15;
	public static final int WHITE_LANCE     =16;
	public static final int WHITE_KNIGHT    =17;
	public static final int WHITE_SILVER    =18;
	public static final int WHITE_GOLD      =19;
	public static final int WHITE_BISHOP    =20;
	public static final int WHITE_ROOK      =21;
	public static final int WHITE_KING      =22;
	public static final int WHITE_PPAWN     =23;
	public static final int WHITE_PLANCE    =24;
	public static final int WHITE_PKNIGHT   =25;
	public static final int WHITE_PSILVER   =26;
	public static final int WHITE_PBISHOP   =27;
	public static final int WHITE_PROOK     =28;

	public static final int MAX_VARIATIONS  =100;

        //public static final char FSEP    =      (char)1;
        //public static final char KSEP    =      (char)3;

        public static final String    FSEP    =       "\u0001";
	public static final String    KSEP    =       "\u0003";

        public static final String BACK_BUTTON = new String("<");
        public static final String FORW_BUTTON = new String(">");
        public static final String BEGN_BUTTON = new String("<<");
        public static final String  END_BUTTON = new String(">>");
        public static final String LEAV_BUTTON = new String("<--");
        public static final String SEND_BUTTON = new String("Send");
        public static final String RECO_BUTTON = new String("Connect");
        public static final String DONE_BUTTON = new String("done");
        public static final String NEWV_BUTTON = new String("[+]");
        public static final String CALC_BUTTON = new String("Calc");
        public static final String SHOW_PANEL  = new String("Data");
        public static final String SHOW_GAME   = new String("Game");
        public static final String SHOW_SELECT = new String("Sel");
        public static final String SHOW_FLIP   = new String("Flip");
        public static final String SHOW_COMM   = new String("Comm");
        public static final String SHOW_DECODE = new String("Dec");
        public static final String SHOW_VARIA  = new String("Var");
        public static final String SHOW_SEARCH = new String("Srch");
        public static final String AUTO_START  = new String("Auto");
        public static final String AUTO_STOP   = new String("Stop");

        public final static int INTRO_SIZE_X       = 350;
        public final static int INTRO_SIZE_Y       = 250;
        public final static int INTRO_FONT_SIZE    =  12;
        public final static int INTRO_FONT_WIDTH   =   7;

        public static final int MAXPLIES  =   9;  // we have to do some buffering for speed advantage
        public static final int MAXMOVES  = 100;

	public static final Color BACK_COLOR  = new Color( 200, 140,   0 );
	public static final Color FRNT_COLOR  = new Color(   0,   0,   0 );
	public static final Color MESS_COLOR  = new Color( 150, 150, 150 );

	public static final Color COLOR_BACKGR= new Color( 175, 175, 175 );
	public static final Color COLOR_FOREGR= new Color( 255, 255, 255 );
	public static final Color COLOR_SHADOW= new Color(  75,  75,  75 );
	public static final Color COLOR_OK    = new Color( 125, 125, 125 );
	public static final Color COLOR_CANCEL= new Color( 125, 125, 125 );
	public static final Color COLOR_HEADER= new Color( 200, 200, 200 );
	public static final Color COLOR_HIDE  = new Color( 125, 125, 125 );
	public static final Color COLOR_CLEAR = new Color( 125, 125, 125 );
	public static final Color COLOR_PLMIN = new Color(   0,   0, 180 );

	public static final Color COLOR_VARS  = new Color( 255,   0,   0 );

	public static final Font BW_PIECE_COUNT  = new Font("Courier",Font.PLAIN,11);
	public static final Font COL_PIECE_COUNT = new Font("Courier",Font.BOLD ,12);

	public Const()
	{
	}
}
