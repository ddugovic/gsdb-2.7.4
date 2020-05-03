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
import java.util.Date;
import java.util.Vector;
import java.util.StringTokenizer;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;



////////////////////////////////////////////////////////////////
//////         Gsdb class                               ////////
////////////////////////////////////////////////////////////////


public class Gsdb extends Frame implements Runnable
{
	MainGsdb main_gsdb;

	Menu menufile;
	Menu menuview;
	Menu menumove, menumovedelay;
	Menu menuwindow;
	Menu menusearch, menusrcama, menusrcpro, menusrcdat, menusrcnew;
	Menu menuhelp;

	MenuItem menufileconnect;
	MenuItem menufilesend;
	MenuItem menufilebitmap;
	MenuItem menufilecalc;
	MenuItem menuviewtype;
	MenuItem menuwindowsearch;
	MenuItem menuviewedit;
	MenuItem menuviewdone;

	CheckboxMenuItem cmenuviewcolor;
	CheckboxMenuItem cmenuviewbw;
	CheckboxMenuItem cmenuviewie;
	CheckboxMenuItem cmenuviewnames;

	Checkbox cbgametype[];

	Thread runner=null;
	int sleep=1000;  /* wait 1 second between moves */

	public boolean from_clicked;
	public boolean firstmovedone;
	public boolean from_clicked_drop;
	public boolean set_position;
	public boolean explorer=false;
	public int x_clicked;
	public int y_clicked;
	public int x_moved;
	public int y_moved;
	public int piece_to_drop;
	public boolean translated=false;
	public boolean black_white;
	public boolean piece_promoted;
	public boolean space_down;
	public boolean promote;
	public boolean draw_moves;
	public boolean connected=false;
	public boolean keep_selection;
	public boolean flipped=false;
	public int search_depth;
	public int piece,taken;
	public boolean bold[];
	public int last_moved;
	public int prev_moved;
	public int pixels[];      // buffer for binary output to image file.
	public Image screenImg;   // buffer for faster screen redraw, like moving text etc.
	public boolean painting;
	public boolean shownames;  // toggle for showing of names.
	public int sx, sy;  // global, otherwise it get's allocated over and over again
	public int px, py;  // global, otherwise it get's allocated over and over again
	public int i, j;  // global, otherwise it get's allocated over and over again

	public int MESSAGE_LINES;

	public Gen gen;

	public int side;  // Which side is playing ?

	public Image logo=null; // GSDB logo
	public Image pieces[][];
	public MediaTracker LoadImages=null;

	public StringBuffer message;
	public StringBuffer pmessage;

	public int RX=-1 ;  // Bounding Rectangle X
	public int RY=-1 ;  // Bounding Rectangle Y
	public int MX    ;  // Left margin
	public int MY    ;  // Upper margin
	public int SX    ;  // Square size X
	public int SY    ;  // Square size Y
	public int PM    ;  // Margin between piece & Board-lines
	public int BR    ;  // Ball radius
	public int BX    ;  // Background X
	public int BY    ;  // Background Y
	public int IHX   ;  // In Hand X
	public int IHYW  ;  // In Hand Y White
	public int IHYB  ;  // In Hand Y Black
	public int HX    ;  // Space between pieces in hand X
	public int HY    ;  // Space between pieces in hand Y

	public final static int INTRO_SIZE_X       = 350;
	public final static int INTRO_SIZE_Y       = 250;
	public final static int INTRO_FONT_SIZE    =  12;
	public final static int INTRO_FONT_WIDTH   =   7;

	public int pieces_in_hand[][];
	public int pihand[][];

	public int board[];
	public static int begin_board[] = {
		16,17,18,19,22,19,18,17,16,
		 0,21, 0, 0, 0, 0, 0,20, 0,
		15,15,15,15,15,15,15,15,15,
		 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 0, 0, 0, 0, 0, 0, 0, 0, 0,
		 1, 1, 1, 1, 1, 1, 1, 1, 1,
		 0, 6, 0, 0, 0, 0, 0, 7, 0,
		 2, 3, 4, 5, 8, 5, 4, 3, 2};
	
	public static String months[] = { "Months", "January", "February", "March", "April", "May", "June", 
	                           "July", "August", "September", "October", "November", "December" };

	public String gametype;
	public String oldtype;
	public Label sidelabel=null;
	public Button back;
	public Button forward;
	public Button begingame;
	public Button endgame;
	public Button leave;
	public Button newvar;
	public Button autostart;
	public Button autostop;
	public Label interval;

	public TextField w_name;
	public TextField w_email;
	public TextField w_country;
	public TextField w_black_player;
	public TextField w_white_player;
	public Choice    w_black_grade;
	public Choice    w_white_grade;
	public TextField w_black_elo;
	public TextField w_white_elo;
	public TextField w_result;
	public TextField w_comment;
	public TextField w_source;
	public TextField w_tournament;
	public TextField w_date;
	public TextField w_round;
	public TextField w_venue;
	public Choice    w_proam;
	public Button w_hide;

	public Panel main_panel;
	public Panel txt_panel;
	public Panel inp_panel;

	public Game game;
	public Graphics gg=null;
	public Graphics igg=null;
	public Graphics tmpgg;

	public Data data_frame;
	public GameSelect select_frame;
	public Comment comm_frame;
	public Decode dec_frame;
	public varFrame var_frame;
	public varChoice choice_frame;
	public Search search_frame;
	public Client client;
	public Help help;
	public GameType gt_frame;

	public String PSNFileName;
	public String PSNFile;
	public boolean PSN_source;
	public boolean PSN_format;
	public String PSN_games[];
	public String PSN_gametype[];
	public int PSN_gameNumber;
	public int PSN_gameCount;
	public boolean PSN_onlyHeaders;
	public boolean game_loaded;
	public boolean game_saved;
	public boolean silent_save;

	public String buf;

	public MenuBar menubar;
	public Menu menu;

	public String MENU_SEPERATOR     = new String("-");

	public String MENU_FILE          = new String("File");
	public String MENU_FILE_OPEN     = new String("Open game file");
	public String MENU_FILE_PSN      = new String("Open PSN file");
	public String MENU_FILE_NEW      = new String("New game");
	public String MENU_FILE_PRINT    = new String("Print diagram");
	public String MENU_FILE_SEND     = new String("Save game in database");
	public String MENU_FILE_CONNECT  = new String("Connect to server");
	public String MENU_FILE_DISCONN  = new String("Disconnect from server");
	public String MENU_FILE_BITMAP   = new String("Save bitmap (d)");
	public String MENU_FILE_CALC     = new String("Calculate tsume");
	public String MENU_FILE_QUIT     = new String("Quit");

	public String MENU_VIEW          = new String("View");
	public String MENU_VIEW_FLIP     = new String("turn board (F9)");
	public String MENU_VIEW_COLOR    = new String("Colored board");
	public String MENU_VIEW_BW       = new String("Black and White board");
	public String MENU_VIEW_TYPE     = new String("Game type...");
	public String MENU_VIEW_HAND[];
	public String MENU_VIEW_EDIT     = new String("Edit position");
	public String MENU_VIEW_DONE     = new String("Done editing");
	public String MENU_VIEW_NAMES    = new String("Show names of players");
	public String MENU_VIEW_PREVGAME = new String("Load the previous game in query (UP)");
	public String MENU_VIEW_NEXTGAME = new String("Load the next game in query (DOWN)");
	public String MENU_VIEW_IE       = new String("Correct board position (MSIE)");

	public String MENU_MOVE          = new String("Move");
	public String MENU_MOVE_FORWARD  = new String("Forward (RIGHT)");
	public String MENU_MOVE_BACK     = new String("Back (LEFT)");
	public String MENU_MOVE_BEGIN    = new String("Start of game (HOME)");
	public String MENU_MOVE_END      = new String("End of game (END)");
	public String MENU_MOVE_NEWVAR   = new String("New variation");
	public String MENU_MOVE_LEAVEVAR = new String("Leave variation");
	public String MENU_MOVE_ASTART   = new String("Auto replay start");
	public String MENU_MOVE_ASTOP    = new String("Auto replay stop");
	public String MENU_MOVE_ADELAY   = new String("Auto replay delay...");
	public String MENU_MOVE_A0       = new String("delay  100 ms");
	public String MENU_MOVE_A1       = new String("delay  250 ms");
	public String MENU_MOVE_A2       = new String("delay  500 ms");
	public String MENU_MOVE_A3       = new String("delay  750 ms");
	public String MENU_MOVE_A4       = new String("delay 1000 ms");
	public String MENU_MOVE_A5       = new String("delay 2000 ms");
	public String MENU_MOVE_A6       = new String("delay 3000 ms");
	public String MENU_MOVE_A7       = new String("delay 4000 ms");
	public String MENU_MOVE_A8       = new String("delay 5000 ms");
	public String MENU_MOVE_A9       = new String("delay 6000 ms");

	public String MENU_SEARCH        = new String("Search");
	public String MENU_SEARCH_SAMA   = new String("Amature events...");
	public String MENU_SEARCH_SPRO   = new String("Professional events...");
	public String MENU_SEARCH_COLMAR = new String("Colmar");
	public String MENU_SEARCH_DENHAAG= new String("Den Haag");
	public String MENU_SEARCH_EURO   = new String("European Championships");
	public String MENU_SEARCH_FIRST  = new String("First 40 moves examples");
	public String MENU_SEARCH_GERMAN = new String("German Open");
	public String MENU_SEARCH_GINGA  = new String("Ginga");
	public String MENU_SEARCH_JUNISEN= new String("Junisen");
	public String MENU_SEARCH_BELGIAN= new String("BK, Belgian Championships");
	public String MENU_SEARCH_KIO    = new String("Kio");
	public String MENU_SEARCH_KISEI  = new String("Kisei");
	public String MENU_SEARCH_MEIJIN = new String("Meijin");
	public String MENU_SEARCH_MEMOR  = new String("Memorial Richard Verkouille");
	public String MENU_SEARCH_NAKA   = new String("Nakabisha examples");
	public String MENU_SEARCH_NIHONPR= new String("Nihon Pro");
	public String MENU_SEARCH_NIJMEG = new String("Nijmegen");
	public String MENU_SEARCH_NK     = new String("NK, Dutch Championships");
	public String MENU_SEARCH_OI     = new String("Oi");
	public String MENU_SEARCH_OZA    = new String("Oza");
	public String MENU_SEARCH_RIKAI  = new String("Rikai");
	public String MENU_SEARCH_RYUO   = new String("Ryu-O");
	public String MENU_SEARCH_TSUME  = new String("Tsume problems");
	public String MENU_SEARCH_AMA    = new String("All amature games");
	public String MENU_SEARCH_PRO    = new String("All professional games");
	public String MENU_SEARCH_VARTEST= new String("Variation tests");
	public String MENU_SEARCH_DATE   = new String("Games played in ...");
	public String MENU_SEARCH_1992   = new String("1992");
	public String MENU_SEARCH_1993   = new String("1993");
	public String MENU_SEARCH_1994   = new String("1994");
	public String MENU_SEARCH_1995   = new String("1995");
	public String MENU_SEARCH_1996   = new String("1996");
	public String MENU_SEARCH_1997   = new String("1997");
	public String MENU_SEARCH_1998   = new String("1998");
	public String MENU_SEARCH_NEW    = new String("What new ? ...");
	public String MENU_SEARCH_NEW_D  = new String("What new today ?");
	public String MENU_SEARCH_NEW_M  = new String("What new this month ?");
	public String MENU_SEARCH_NEW_Y  = new String("What new this year ?");

	public String MENU_WINDOW        = new String("Window");
	public String MENU_WINDOW_DATA   = new String("Data (F2)");
	public String MENU_WINDOW_VARIA  = new String("Game score (F3)");
	public String MENU_WINDOW_COMMENT= new String("Comment (F4)");
	public String MENU_WINDOW_DECODE = new String("Decode/Encode (F5)");
	public String MENU_WINDOW_SEARCH = new String("Search (F6)");
	public String MENU_WINDOW_SELECT = new String("Select from search result (F7)");
	public String MENU_WINDOW_CHOICE = new String("Choose a variation (F8)");
	public String MENU_WINDOW_CLOSE  = new String("Close all windows");

	public String MENU_HELP          = new String("Help (F1)");
	public String MENU_HELP_GENERAL  = new String("General");
	public String MENU_HELP_ABOUT    = new String("About");

	public void start()
	{
		setMessage(message.toString());
	}

	public void stop()
	{
		hide();
		client.disconnect();
		data_frame.dispose();
		select_frame.dispose();
		choice_frame.dispose();
		comm_frame.dispose();
		dec_frame.dispose();
		var_frame.dispose();
		search_frame.dispose();
		help.dispose();
		dispose();
	}

	//
	// Initialise our GSDB applet
	//
	public Gsdb(MainGsdb mgsdb, String gtype, boolean smallboard, boolean connect)
	{
		super("GNU Shogi Database");

		//setResizable(false);
		main_gsdb=mgsdb;
		gametype=new String( gtype );

		pieces = new Image[2][];
		pieces[0]=new Image[29];
		pieces[1]=new Image[29];

		loadBoard(smallboard, false);
		
		screenImg=main_gsdb.createImage( 620, 530 );
		if (screenImg==null)
		{
			System.out.println("Oops, screenImg has not been allocated !");
		}

		igg=screenImg.getGraphics();

		if (black_white) setFont(new Font("Courier",Font.PLAIN,11)); else setFont(new Font("Courier",Font.BOLD,12));

		pieces_in_hand=new int[2][9];
		pihand=new int[2][9];
		board=new int[81];
		bold=new boolean[81];
		search_depth=3;
		firstmovedone=false;
		last_moved=-1;
		prev_moved=-1;
		painting=false;
		shownames=true;
		keep_selection=false;  // allows multiple queries
		PSN_source=false;
		PSN_format=false;
		PSNFile=null;
		PSNFileName=null;
		PSN_games=null;
		PSN_gametype=null;
		PSN_onlyHeaders=false;
		game_loaded=false;
		game_saved=false;
		silent_save=false;
		buf=new String();

		game=new Game(this); game.allocateMoves(10);
		gen=new Gen(this);

		message=new StringBuffer("Welcome to Java G.S.D.B. version "+Const.VERSION);
		pmessage=new StringBuffer();

		MENU_VIEW_HAND     = new String[11];
		MENU_VIEW_HAND[ 0] = "Even game";
		MENU_VIEW_HAND[ 1] = "Lance";
		MENU_VIEW_HAND[ 2] = "Bishop";    
		MENU_VIEW_HAND[ 3] = "Rook";
		MENU_VIEW_HAND[ 4] = "2 pieces";  
		MENU_VIEW_HAND[ 5] = "4 pieces";
		MENU_VIEW_HAND[ 6] = "6 pieces";  
		MENU_VIEW_HAND[ 7] = "8 pieces";
		MENU_VIEW_HAND[ 8] = "Tsume";
		MENU_VIEW_HAND[ 9] = "Hishi";
		MENU_VIEW_HAND[10] = "Position";

		menubar=new MenuBar(); setMenuBar(menubar);
		menufile=new Menu(MENU_FILE); menubar.add(menufile);
			menufile.add(new MenuItem( MENU_FILE_NEW      ));
			menufile.add(new MenuItem( MENU_FILE_OPEN     ));
			menufile.add(new MenuItem( MENU_FILE_PSN      ));
			//menufile.add(new MenuItem( MENU_FILE_PRINT    ));
			menufile.add(menufileconnect=new MenuItem( MENU_FILE_CONNECT  ));
			menufile.add(menufilesend=new MenuItem( MENU_FILE_SEND     ));
			menufile.add(menufilebitmap=new MenuItem( MENU_FILE_BITMAP   ));
			menufile.add(new MenuItem( MENU_SEPERATOR     ));
			menufile.add(menufilecalc=new MenuItem( MENU_FILE_CALC     ));
			menufile.add(new MenuItem( MENU_SEPERATOR     ));
			menufile.add(new MenuItem( MENU_FILE_QUIT     ));
		menuview=new Menu(MENU_VIEW); menubar.add(menuview);
			menuview.add(new MenuItem( MENU_VIEW_FLIP     ));
			menuview.add(new MenuItem( MENU_SEPERATOR     ));
			menuview.add(cmenuviewcolor=new CheckboxMenuItem( MENU_VIEW_COLOR    ));
			menuview.add(cmenuviewbw   =new CheckboxMenuItem( MENU_VIEW_BW       ));
			menuview.add(menuviewtype=new MenuItem( MENU_VIEW_TYPE ));
			menuview.add(new MenuItem( MENU_SEPERATOR     ));
			menuview.add(menuviewedit=new MenuItem( MENU_VIEW_EDIT ));
			menuview.add(menuviewdone=new MenuItem( MENU_VIEW_DONE ));
			menuview.add(new MenuItem( MENU_SEPERATOR     ));
			menuview.add(cmenuviewnames=new CheckboxMenuItem( MENU_VIEW_NAMES    ));
			cmenuviewnames.setState(true);
			menuview.add(new MenuItem( MENU_SEPERATOR     ));
			menuview.add(new MenuItem( MENU_VIEW_PREVGAME ));
			menuview.add(new MenuItem( MENU_VIEW_NEXTGAME ));
			menuview.add(new MenuItem( MENU_SEPERATOR     ));
			menuview.add(cmenuviewie=new CheckboxMenuItem( MENU_VIEW_IE       ));
			menuviewdone.disable();
			menumove=new Menu(MENU_MOVE); menubar.add(menumove);
			menumove.add(new MenuItem( MENU_MOVE_FORWARD  ));
			menumove.add(new MenuItem( MENU_MOVE_BACK     ));
			menumove.add(new MenuItem( MENU_MOVE_BEGIN    ));
			menumove.add(new MenuItem( MENU_MOVE_END      ));
			menumove.add(new MenuItem( MENU_SEPERATOR     ));
			menumove.add(new MenuItem( MENU_MOVE_ASTART   ));
			menumove.add(new MenuItem( MENU_MOVE_ASTOP    ));
			menumovedelay=new Menu( MENU_MOVE_ADELAY );
			menumovedelay.add(new MenuItem( MENU_MOVE_A0       ));
			menumovedelay.add(new MenuItem( MENU_MOVE_A1       ));
			menumovedelay.add(new MenuItem( MENU_MOVE_A2       ));
			menumovedelay.add(new MenuItem( MENU_MOVE_A3       ));
			menumovedelay.add(new MenuItem( MENU_MOVE_A4       ));
			menumovedelay.add(new MenuItem( MENU_MOVE_A5       ));
			menumovedelay.add(new MenuItem( MENU_MOVE_A6       ));
			menumovedelay.add(new MenuItem( MENU_MOVE_A7       ));
			menumovedelay.add(new MenuItem( MENU_MOVE_A8       ));
			menumovedelay.add(new MenuItem( MENU_MOVE_A9       ));
			menumove.add(menumovedelay);
			menumove.add(new MenuItem( MENU_SEPERATOR     ));
			menumove.add(new MenuItem( MENU_MOVE_NEWVAR   ));
			menumove.add(new MenuItem( MENU_MOVE_LEAVEVAR ));
		menusearch=new Menu(MENU_SEARCH); menubar.add(menusearch);
			menusrcama=new Menu(MENU_SEARCH_SAMA);
			menusrcama.add(new MenuItem( MENU_SEARCH_COLMAR  ));
			menusrcama.add(new MenuItem( MENU_SEARCH_DENHAAG ));
			menusrcama.add(new MenuItem( MENU_SEARCH_EURO    ));
			menusrcama.add(new MenuItem( MENU_SEARCH_GERMAN  ));
			menusrcama.add(new MenuItem( MENU_SEARCH_BELGIAN ));
			menusrcama.add(new MenuItem( MENU_SEARCH_MEMOR   ));
			menusrcama.add(new MenuItem( MENU_SEARCH_NIJMEG  ));
			menusrcama.add(new MenuItem( MENU_SEARCH_NK      ));
			menusrcama.add(new MenuItem( MENU_SEARCH_RIKAI   ));
			menusearch.add(menusrcama);
			menusrcpro=new Menu(MENU_SEARCH_SPRO);
			menusrcpro.add(new MenuItem( MENU_SEARCH_GINGA   ));
			menusrcpro.add(new MenuItem( MENU_SEARCH_JUNISEN ));
			menusrcpro.add(new MenuItem( MENU_SEARCH_KIO     ));
			menusrcpro.add(new MenuItem( MENU_SEARCH_KISEI   ));
			menusrcpro.add(new MenuItem( MENU_SEARCH_MEIJIN  ));
			menusrcpro.add(new MenuItem( MENU_SEARCH_NIHONPR ));
			menusrcpro.add(new MenuItem( MENU_SEARCH_OI      ));
			menusrcpro.add(new MenuItem( MENU_SEARCH_OZA     ));
			menusrcpro.add(new MenuItem( MENU_SEARCH_RYUO    ));
			menusearch.add(menusrcpro);
			menusearch.add(new MenuItem( MENU_SEPERATOR      ));
			menusearch.add(new MenuItem( MENU_SEARCH_NAKA    ));
			menusearch.add(new MenuItem( MENU_SEARCH_FIRST   ));
			menusearch.add(new MenuItem( MENU_SEARCH_TSUME   ));
			menusearch.add(new MenuItem( MENU_SEPERATOR      ));
			menusearch.add(new MenuItem( MENU_SEARCH_AMA     ));
			menusearch.add(new MenuItem( MENU_SEARCH_PRO     ));
			menusearch.add(new MenuItem( MENU_SEARCH_VARTEST ));
			menusearch.add(new MenuItem( MENU_SEPERATOR      ));
			menusrcdat=new Menu(MENU_SEARCH_DATE);
			menusrcdat.add(new MenuItem(MENU_SEARCH_1992));
			menusrcdat.add(new MenuItem(MENU_SEARCH_1993));
			menusrcdat.add(new MenuItem(MENU_SEARCH_1994));
			menusrcdat.add(new MenuItem(MENU_SEARCH_1995));
			menusrcdat.add(new MenuItem(MENU_SEARCH_1996));
			menusrcdat.add(new MenuItem(MENU_SEARCH_1997));
			menusrcdat.add(new MenuItem(MENU_SEARCH_1998));
			menusearch.add(menusrcdat);
			menusrcnew=new Menu(MENU_SEARCH_NEW);
			menusrcnew.add(new MenuItem(MENU_SEARCH_NEW_D  ));
			menusrcnew.add(new MenuItem(MENU_SEARCH_NEW_M  ));
			menusrcnew.add(new MenuItem(MENU_SEARCH_NEW_Y  ));
			menusearch.add(menusrcnew);
		menuwindow=new Menu(MENU_WINDOW); menubar.add(menuwindow);
			menuwindow.add(new MenuItem( MENU_WINDOW_DATA   ));
			menuwindow.add(new MenuItem( MENU_WINDOW_VARIA  ));
			menuwindow.add(new MenuItem( MENU_WINDOW_COMMENT));
			menuwindow.add(new MenuItem( MENU_WINDOW_DECODE ));
			menuwindow.add(menuwindowsearch=new MenuItem( MENU_WINDOW_SEARCH ));
			menuwindow.add(new MenuItem( MENU_WINDOW_SELECT ));
			menuwindow.add(new MenuItem( MENU_WINDOW_CHOICE ));
			menuwindow.add(new MenuItem( MENU_SEPERATOR     ));
			menuwindow.add(new MenuItem( MENU_WINDOW_CLOSE  ));
		menuhelp=new Menu(MENU_HELP); menubar.add(menuhelp);
			menuhelp.add(new MenuItem( MENU_HELP_GENERAL  ));
			menuhelp.add(new MenuItem( MENU_HELP_ABOUT    ));
		menubar.setHelpMenu(menuhelp);

		back         = new Button(Const.BACK_BUTTON);
		forward      = new Button(Const.FORW_BUTTON);
		begingame    = new Button(Const.BEGN_BUTTON);
		endgame      = new Button(Const. END_BUTTON);
		leave        = new Button(Const.LEAV_BUTTON);
		newvar       = new Button(Const.NEWV_BUTTON);
		autostart    = new Button(Const.AUTO_START);
		autostop     = new Button(Const.AUTO_STOP);
		sidelabel    = new Label("Black to play");
		autostop.disable();

		/* let's make a gridBagLayout() */

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		setLayout(gridbag);

		// <<
		buildConstraints(constraints, 0, 0, 1, 1, 1, 2); constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.EAST; gridbag.setConstraints(begingame, constraints);
		add(begingame);
		// <
		buildConstraints(constraints, 1, 0, 1, 1, 1, 2); constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.EAST; gridbag.setConstraints(back, constraints);
		add(back);
		// >
		buildConstraints(constraints, 2, 0, 1, 1, 1, 2); constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.EAST; gridbag.setConstraints(forward, constraints);
		add(forward);
		// >>
		buildConstraints(constraints, 3, 0, 1, 1, 1, 2); constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.EAST; gridbag.setConstraints(endgame, constraints);
		add(endgame);
		// newvar-button
		buildConstraints(constraints, 0, 1, 1, 1, 1, 1); constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.EAST; gridbag.setConstraints(newvar, constraints);
		add(newvar);
		// Leave variation
		buildConstraints(constraints, 1, 1, 1, 1, 1, 1); constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.EAST; gridbag.setConstraints(leave, constraints);
		add(leave);
		// autostart
		buildConstraints(constraints, 2, 1, 1, 1, 1, 1); constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.EAST; gridbag.setConstraints(autostart, constraints);
		add(autostart);
		// autostop 
		buildConstraints(constraints, 3, 1, 1, 1, 1, 1); constraints.fill = GridBagConstraints.BOTH; 
		constraints.anchor = GridBagConstraints.EAST; gridbag.setConstraints(autostop, constraints);
		add(autostop);
		// side-label
		buildConstraints(constraints, 0, 2, 4, 1, 1, 1); constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.EAST; gridbag.setConstraints(sidelabel, constraints);
		add(sidelabel);

		data_frame   = new Data(this);       // This window contains the data of the game
		select_frame = new GameSelect(this); // This is the result of the last search operation (list of games)
		comm_frame   = new Comment(this);    // This window contains a comment on the current move.
		dec_frame    = new Decode(this);     // This window takes care of decoding a text score
		var_frame    = new varFrame(this);   // This window handles variations on the curent move.
		choice_frame = new varChoice(this);  // Here you can choose a variation
		search_frame = new Search(this);     // This window allows you to make a search in the database
		help         = new Help(this);       // The help window
		gt_frame     = new GameType(this);   // Dialog to select the gametype

		this         .setIconImage(pieces[0][Const.KING]); // Minimized.
		data_frame   .setIconImage(pieces[0][Const.KING]);
		select_frame .setIconImage(pieces[0][Const.KING]);
		comm_frame   .setIconImage(pieces[0][Const.KING]);
		dec_frame    .setIconImage(pieces[0][Const.KING]);
		var_frame    .setIconImage(pieces[0][Const.KING]);
		choice_frame .setIconImage(pieces[0][Const.KING]);
		search_frame .setIconImage(pieces[0][Const.KING]);
		help         .setIconImage(pieces[0][Const.KING]);

		newGame();
		draw_moves=true;

		// Now we try to connect to the game-server
		client=new Client(this);
		if ( connect )
		{
			connect();
		}
		else
		{
			connected=false;

			menufileconnect.enable();
			menufilesend.disable();
			menufilebitmap.disable();
			menuwindowsearch.disable();
			menusearch.disable();
		}
	}

	public void connect()
	{
		if (!client.connect())
		{
			System.out.println("ERROR CONNECTING TO SERVER");
			setMessage("ERROR: Couldn't connect to game server !");
			connected=false;

			menufileconnect.enable();
			menufilesend.disable();
			menufilebitmap.disable();
			menuwindowsearch.disable();
			menusearch.disable();
		}
		else
		{
			setMessage("Connected to game server !");
			connected=true;

			menufileconnect.disable();
			menufilesend.enable();
			menufilebitmap.enable();
			menuwindowsearch.enable();
			menusearch.enable();
		}
	}

	public void  disconnect()
	{
		if (!client.disconnect())
		{
			setMessage("ERROR: Couldn't disconnect from server !");
		}
		else
		{
			setMessage("Disconnected from server !");
			connected=false;
		}
	}

	public void loadBoard(boolean smallboard, boolean only_sizes)
	{
		boolean first=false;
		translated=false;
		if (!only_sizes && LoadImages==null)
		{
			LoadImages = new MediaTracker(this);

			first=true;
			for (int i=1;i<29;i++) 
			{
				pieces[0][i]=main_gsdb.getImage("pictures/piece"+i+".gif");
				pieces[1][i]=main_gsdb.getImage("pictures/bw"+i+".gif");
				LoadImages.addImage(pieces[0][i],i);
				LoadImages.addImage(pieces[1][i],i);
			}
			try
			{
				LoadImages.waitForAll();  // wait until pieces are loaded, otherwise the pieces keep refreshing.
			} 
			catch (InterruptedException e)
			{
				System.out.println("Image Loading Failed !");
			}
		}
		if (!smallboard)
		{
			black_white= false;
			MX         = 15;  // Left margin
			MY         = 20;  // Upper margin
			SX         = 40;  // Square size X
			SY         = 40;  // Square size Y
			PM         =  1;  // Margin between piece & Board-lines
			BR         =  3;  // Ball radius
			BX         = 96;  // Background X
			BY         = 96;  // Background Y
			IHX        =400;  // In Hand X
			IHYW       = MY;  // In Hand Y White
			IHYB       = MY+9*SY-BY; // 350;  // In Hand Y Black
			HX         =SX+10;// Space between pieces in hand X
			HY         =SY+10;// Space between pieces in hand Y

			if (!only_sizes)
			{
				//cmenuviewcolor.setState(true);
				//cmenuviewbw.setState(false);

				setBackground(Const.BACK_COLOR);
				setForeground(Const.FRNT_COLOR);
				if (sidelabel!=null) 
					sidelabel.setBackground(Const.BACK_COLOR);

				resize(620,530);
			}
		}
		else
		{
			black_white= true;
			MX         = 45;  // Left margin
			MY         = 20;  // Upper margin
			SX         = 32;  // Square size X
			SY         = 35;  // Square size Y
			PM         =  1;  // Margin between piece & Board-lines
			BR         =  3;  // Ball radius
			BX         = 75;  // Background X
			BY         = 75;  // Background Y
			IHX        = MX+9*SX+20; // In Hand X MX+9*SX
			IHYW       = 30;  // In Hand Y White
			IHYB       =280;  // In Hand Y Black
			HX         =SX+10;// Space between pieces in hand X
			HY         =SY+10;// Space between pieces in hand Y
			if (!only_sizes)
			{
				//cmenuviewcolor.setState(false);
				//cmenuviewbw.setState(true);

				setBackground(Color.white); 
				setForeground(Color.black); 
				if (sidelabel!=null) 
					sidelabel.setBackground(Color.white);

				resize(560,470);
			}
		}

		if (!first && !only_sizes) 
		{
			hide();
			show();
		}
	}

	public void changeColor()
	{
		side=side^1;
	}

	public Insets insets()
	{
		int w=size().width;
		int h=size().height;

		if (!translated) checkClipRect(getGraphics());
		if (!black_white)
		{
			return new Insets(h/2-40,IHX-10,h/2-60,5);
		}
		else
		{
			return new Insets(h/2-40,IHX+HX,h/2-60,5);
		}
	}

	void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy)
	{
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.weighty = wy;
	}

	public void newGame()
	{
		for (int i=0;i<81;i++) board[i]=begin_board[i];
		for (int i=0;i<2;i++) for (int j=0;j<9;j++) pieces_in_hand[i][j]=0;
		for (int i=0;i<2;i++) for (int j=0;j<9;j++) pihand[i][j]=0;
		for (int i=0;i<game.root.variation.length;i++) game.root.variation[i].clearMove();
		for (int i=0;i<11;i++)
		{
			if (gametype.equals(MENU_VIEW_HAND[i]))
				cbgametype[i].setState(true);
			else    cbgametype[i].setState(false);
		}

		data_frame.clearData();
		game.movenr=0;
		game.level=0;
		comm_frame.hide();
		var_frame.hide();
		setVariation(game.thisMove());
		setComment(game.thisMove());
		from_clicked=false;
		from_clicked_drop=false;
		piece_promoted=false;
		space_down=false;
		set_position=false;
		data_frame.century.select("19");
		data_frame.year   .select("97");
		data_frame.month  .select("  ");
		data_frame.day    .select("  ");
		w_name           .setText("");  w_email          .setText("");  w_country        .setText("");
		w_black_player   .setText("");  w_white_player   .setText("");  w_black_grade    .select("Grade");
		w_white_grade    .select("Grade"); w_black_elo   .setText("");  w_white_elo      .setText("");
		w_result         .setText("");  w_comment        .setText("");  w_source         .setText("");
		w_tournament     .setText("");  w_date           .setText("");  w_round          .setText("");
		w_venue          .setText("");  w_proam          .select("Amateur"); 

		oldtype=null;
		oldtype=new String(gametype);
		char first=oldtype.charAt(0);

		side=Const.WHITE;
		menufilecalc.disable();
		flipped=true;
		switch(first)
		{
			case 'E':	side=Const.BLACK; flipped=false; break; // Even game
			case 'L':	board[ 8]=0; break;              // Lance handicap
			case 'B':	board[16]=0; break;              // Bishop handicap
			case 'R':	board[10]=0; break;              // Rook handicap
			case '+':	board[10]=0; board[8]=0; break;  // Rook + Lance handicap
			case '2':	                                 // 2 pieces
					board[10]=0; board[16]=0; break; 
			case '4':	                                 // 4 pieces
					board[ 0]=0; board[ 8]=0; 
					board[10]=0; board[16]=0; break; 
			case '6':	                                 // 6 pieces
					board[ 0]=0; board[ 1]=0; 
					board[ 7]=0; board[ 8]=0;
					board[10]=0; board[16]=0; break; 
			case '8':	                                 // 8 pieces
					board[ 0]=0; board[ 1]=0; 
					board[ 2]=0; board[ 6]=0; 
					board[ 7]=0; board[ 8]=0; 
					board[10]=0; board[16]=0; break; 
			case 'T': 	menufilecalc.enable();
			case 'H': 
			case 'P':       // Tsume, Hishi, Position
					flipped=false;
					for (int i=0;i<81;i++) board[i]=0;
					set_position=true;
					setMessage("Type PLSGRBG/plsgrbg, 2x to promote, <CR> for side, d for search depth");
					menuviewdone.enable();
					side=Const.BLACK;
					repaint();
					break;
			default:	break;
		}
	}
	
	public void readPSNFile(String filename)
	{
			PSNFile=getFile(filename);
			PSN_source=true;

			if (PSN_format)
			{
				StringTokenizer st;
				StringBuffer PSN_temp=new StringBuffer(PSNFile);

				setMessage("Marking seperate games.");
				for (i=0;i<PSN_temp.length();i++)
				{
					i=PSN_temp.toString().indexOf("\n\n[",i+1); 
					if (i<0) break;
					PSN_temp.setCharAt(i+1,(char)1); 
				}

				setMessage("Running StringTokenizer ..."); System.out.flush();
				st=new StringTokenizer(PSN_temp.toString(),"\u0001");
				setMessage("StringTokenizer done.");

				PSN_games   =null;
				PSN_games   =new String[st.countTokens()];
				PSN_gametype=null;
				PSN_gametype=new String[st.countTokens()];
				i=0;
				while (st.hasMoreTokens())
				{
					PSN_games[i]=st.nextToken();
					PSN_gametype[i]=MENU_VIEW_HAND[0];
					setMessage("Game "+i+" found");
					i++;
				}

				PSN_gameCount=i;
				PSN_gameNumber=0;

				PSN_onlyHeaders=true;
				StringBuffer fdate = new StringBuffer("");
				StringBuffer fblack_name = new StringBuffer("");
				StringBuffer fwhite_name = new StringBuffer("");
				StringBuffer fresult = new StringBuffer("");
				StringBuffer ftournament = new StringBuffer("");
				StringBuffer fround = new StringBuffer("");
				StringBuffer fgametype = new StringBuffer("");

				select_frame.newSelection();

				for (i=0;i<PSN_gameCount;i++)
				{
					setMessage("Scanning game "+i+"/"+PSN_gameCount);
					dec_frame.decodeGame( PSN_games[i] );

					fdate       .setLength(0); fdate       .append( data_frame.getDate() );
					fblack_name .setLength(0); fblack_name .append( w_black_player .getText() );
					fwhite_name .setLength(0); fwhite_name .append( w_white_player .getText() );
					fresult     .setLength(0); fresult     .append( w_result       .getText() );
					ftournament .setLength(0); ftournament .append( w_tournament   .getText() );
					fround      .setLength(0); fround      .append( w_round        .getText() );

					if (fdate.length()==8) { fdate.insert(4,'/'); fdate.insert(7,'/'); } else { fdate.setLength(0); fdate.append("unknown   "); }
					while (fblack_name.length()<17) fblack_name.append(" "); fblack_name.setLength(17);
					while (fwhite_name.length()<17) fwhite_name.append(" "); fwhite_name.setLength(17);
					while (fresult    .length()< 6) fresult    .append(" "); fresult    .setLength( 6);
					while (ftournament.length()<30) ftournament.append(" "); ftournament.setLength(30);
					
					select_frame.addGame((i<9?"   ":i<99?"  ":i<999?" ":"")+(i+1)+" | "+fdate+" | "+fblack_name+" | "+fwhite_name+" | "+fresult+" | "+ftournament+" | "+fround);
				}
				PSN_onlyHeaders=false;
		}
		else
		{
			/* simple ASCII-file, tab-delimeted :

                                key        name       email      country    
                                black_name white_name black_grade white_grade
                                black_elo  white_elo  result     comment    
                                source     tournament date       round      
                                venue      proam      gametype   game       

			   a \n after each line

			*/
			StringBuffer 
				key       ,name      ,email     ,country    
                               ,black_name,white_name,black_grade,white_grade
                               ,black_elo ,white_elo ,result     ,comment    
                               ,source    ,tournament,date       ,round      
                               ,venue     ,proam     ,gametype   ,game       ;

			StreamAnalyser sa=new StreamAnalyser(PSNFile); // treat it as a stream.

			String size=sa.getNumber();
			int number=Integer.parseInt( size );

			PSN_games=new String[ number ];
			PSN_gameCount=number;
			PSN_gameNumber=0;

			select_frame.newSelection();

			for (int i=0;i<number && !sa.End();i++)
			{
				if (i%100==0 && i>0) System.out.println("parsed "+i+" lines");
				key        =new StringBuffer(sa.getUntil('\t').trim());
				name       =new StringBuffer(sa.getUntil('\t').trim());
				email      =new StringBuffer(sa.getUntil('\t').trim());
				country    =new StringBuffer(sa.getUntil('\t').trim());
				black_name =new StringBuffer(sa.getUntil('\t').trim());
				white_name =new StringBuffer(sa.getUntil('\t').trim());
				black_grade=new StringBuffer(sa.getUntil('\t').trim());
				white_grade=new StringBuffer(sa.getUntil('\t').trim());
				black_elo  =new StringBuffer(sa.getUntil('\t').trim());
				white_elo  =new StringBuffer(sa.getUntil('\t').trim());
				result     =new StringBuffer(sa.getUntil('\t').trim());
				comment    =new StringBuffer(sa.getUntil('\t').trim());
				source     =new StringBuffer(sa.getUntil('\t').trim());
				tournament =new StringBuffer(sa.getUntil('\t').trim());
				date       =new StringBuffer(sa.getUntil('\t').trim());
				round      =new StringBuffer(sa.getUntil('\t').trim());
				venue      =new StringBuffer(sa.getUntil('\t').trim());
				proam      =new StringBuffer(sa.getUntil('\t').trim());
				gametype   =new StringBuffer(sa.getUntil('\t').trim());
				game       =new StringBuffer(sa.getUntil('\t').trim());

				PSN_games[i]=new String(game);
				PSN_gametype[i]=new String(gametype);

				if (date.length()==8) { date.insert(4,'/'); date.insert(7,'/'); } else { date.setLength(0); date.append("unknown   "); }
				while (black_name.length()<17) black_name.append(" "); black_name.setLength(17);
				while (white_name.length()<17) white_name.append(" "); white_name.setLength(17);
				while (result    .length()< 6) result    .append(" "); result.setLength( 6);
				while (tournament.length()<30) tournament.append(" "); tournament.setLength(30);
				
				select_frame.addGame((i<9?"   ":i<99?"  ":i<999?" ":"")+(i+1)+" | "+date+" | "+black_name+" | "+white_name+" | "+result+" | "+tournament+" | "+round);

				tournament=null; round     =null; date      =null; // collect garbage
				black_name=null; white_name=null; result    =null;
			}
			if (i<number) PSN_gameCount=i;
		}
	}

	public String getFile(String filename)
	{
		return (main_gsdb.getFile(filename));
	}


	public void convertGame(String s)
	{
		char c=' ';
		char p;
		char f1, f2;
		char t1, t2;
		int piece=0;
		int from;
		int to;
		int taken;
		boolean promotes;
		boolean is_promoted;
		boolean is_drop;
		boolean dm=draw_moves;
		int num;
		int i=0;

		newGame();
		draw_moves=false;

		//System.out.println("Converting game: ["+s+"]");

		if (oldtype.equals("Tsume") || oldtype.equals("Position") || oldtype.equals("Hishi"))
		{
			//
			// NOT a normal game, but a list of positions : +bP7i bB5e wK3d ...
			//

			for (i=0;i<s.length() && c!='$';i++)
			{
				c=s.charAt(i);
				while (c==' ' && i<s.length()) { i++; c=s.charAt(i); }
				if (i>=s.length()) break;
				if (c=='$') break;

				if (c=='b') side=Const.BLACK;
				if (c=='w') side=Const.WHITE;
				
				i++; if (i>=s.length()) break; c=s.charAt(i);
				if (c=='+')
				{
					is_promoted=true;
					i++; if (i>=s.length()) break; c=s.charAt(i);
				}
				else is_promoted=false;
				switch( c )
				{
					case 'P' : if (!is_promoted) piece=Const.PAWN;   else piece=Const.PPAWN; break;
					case 'L' : if (!is_promoted) piece=Const.LANCE;  else piece=Const.PLANCE; break;
					case 'N' : if (!is_promoted) piece=Const.KNIGHT; else piece=Const.PKNIGHT; break;
					case 'S' : if (!is_promoted) piece=Const.SILVER; else piece=Const.PSILVER; break;
					case 'G' : if (!is_promoted) piece=Const.GOLD;   else piece=Const.GOLD; break;
					case 'B' : if (!is_promoted) piece=Const.BISHOP; else piece=Const.PBISHOP; break;
					case 'R' : if (!is_promoted) piece=Const.ROOK;   else piece=Const.PROOK; break;
					case 'K' : if (!is_promoted) piece=Const.KING;   else piece=Const.KING; break;
					default  : piece=Const.EMPTY;  System.out.println("ERROR white reading position !!!"); break;
				}
				i++; if (i>=s.length()) break; t1=s.charAt(i);
				t2=' ';
				i++; if (i<s.length()) t2=s.charAt(i);

				// <param name="game" value="wK1c wP2b wB5f b+B4d bR3d bS3b wPH17 wLH4 wNH4 wSH3 wGH4 wRH1 $">

				if (t1=='H')
				{
					if (t2!=' ')
					{
						num=t2-'0';
						c=' ';
						i++; if (i<s.length()) c=s.charAt(i);
						if (c!=' ' && c!='$')
						{
							num*=10;
							num+=c-'0';
						}
					}
					else
					{
						num=1;
					}
					pieces_in_hand[side][piece]+=num;
				}
				else
				{
					to  = (int)('9'-t1) + ((int)(t2-'a'))*9;
					piece=piece+(side==Const.BLACK?0:14);
					board[to]=piece;
				}
			}
			draw_moves=dm;
			set_position=false;
			repaint();
			side=Const.BLACK;
		}
		if (c=='$') i++;

		// 
		// Now that this is finished, we can do the rest with decodeGame();
		// ---> in Convert.java <---
		//
		// First we make sure that the ESN/PSN flag in the decode window is set
		//
		boolean backPSN   =dec_frame.cmi_decpsn .getState();
		boolean backKifu  =dec_frame.cmi_deckifu.getState();
		boolean backJShogi=dec_frame.cmi_decjava.getState();

		dec_frame.setDecodePSN();

		dec_frame.decodeGame( s.substring(i) );

		dec_frame.cmi_decpsn .setState( backPSN    );
		dec_frame.cmi_deckifu.setState( backKifu   );
		dec_frame.cmi_decjava.setState( backJShogi );

		setComment( game.thisMove() );

		draw_moves=dm;
	}

	//
        // Make a bitmap !
        //

        public void sendBitmap()
        {
                int w=408;
                int h=shownames ? 355 : 340;
                
                setMessage("Allocating memory for image...");
		if (pixels==null) pixels=new int[w*h];

                Graphics tmpg=screenImg.getGraphics();

                boolean bw=black_white;
		loadBoard(true, true);

                setMessage("Drawing image in memory...");
		paintScreen(tmpg);

                setMessage("Converting image to bitmap...");
                PixelGrabber pg=new PixelGrabber(screenImg,MX-HX-10,MY-15,w,h,pixels,0,w);
                try { pg.grabPixels(); }
                catch (InterruptedException e) { System.out.println("pixel grab Failed !"); }

		loadBoard(bw, true);

                setMessage("Sending bitmap image to server...");
                client.sendBitmap(w,h);
		repaint();
        }

/*  

	//
        // Print a bitmap, only works in JDK 1.1, so we don't want it !
        //
*/
        public void printBitmap()
	{
                int w=408;
                int h=shownames ? 355 : 340;
                
                boolean bw=black_white;
		loadBoard(true, true);


		PrintJob pj = getToolkit().getPrintJob(this, "test", null);
		Graphics pg = pj.getGraphics();
		paintScreen(pg);
		printAll(pg);
		pg.dispose();
		pj.end();

		loadBoard(bw, true);

                setMessage("Sending bitmap image to server...");
                client.sendBitmap(w,h);
        }
/*   */

	//
	// Paint the screen
	//

	public void paint(Graphics g)
	{
		if (gg==null) gg=getGraphics();
		update(g);
	}

	public void update(Graphics g)
	{
		if (!translated) checkClipRect(g);
		painting=true; paintScreen(igg); painting=false;
		g.drawImage(screenImg,0,0,this);
		setMessage(message.toString());
	}

	public void checkClipRect(Graphics g)
	{
		Rectangle r=g.getClipRect();
		if (r!=null)
		{
			MX+=r.x;
			MY+=r.y;
			if (!black_white)
			{
				IHYW+=r.y;
				IHYB+=r.y;
			}
			translated=true;
			//System.out.println("Clipping origin is : ("+r.x+","+r.y+") width="+r.width+" height="+r.height);
		}
	}

	public void paintScreen(Graphics g)
	{
		//System.out.println("paintScreen : MX="+MX+" MY="+MY);
		if (black_white)
		{
			g.setColor(Color.white);
			g.fillRect(0,0,size().width,size().height);
			g.setColor(Color.black);
		}
		else
		{
			g.setColor(Const.BACK_COLOR);
			g.fillRect(0,0,size().width,size().height);
			g.setColor(Const.FRNT_COLOR);
		}

		for (i=0;i<9;i++) for (j=0;j<9;j++) drawPieceBackground(g,i,j);
		g.drawRect(MX-1,MY-1,9*SX+2,9*SY+2);
		g.drawRect(MX-2,MY-2,9*SX+4,9*SY+4);
		g.drawLine(MX,MY+9*SY,MX+9*SX,MY+9*SY);
		g.drawLine(MX+9*SX,MY,MX+9*SX,MY+9*SY);

		if (black_white) g.setFont(Const.BW_PIECE_COUNT); else g.setFont(Const.COL_PIECE_COUNT);
		for (i=1;i<10;i++)
		{
			if (!flipped)
			{
                                g.drawString(""+(10-i),MX+SX*i-SX/2-4,MY-5);
                                if (!black_white)
                                        g.drawString(""+(char)('a'+i-1),MX-10,MY+SY*i-SY/2);
                                else
                                        g.drawString(""+(char)('a'+i-1),MX+9*SX+5,MY+SY*i-SY/2+4);
			}
			else
			{
                                g.drawString(""+i,MX+SX*i-SX/2-4,MY-5);
                                if (!black_white)
                                        g.drawString(""+(char)('j'-i),MX-10,MY+SY*i-SY/2);
                                else
                                        g.drawString(""+(char)('j'-i),MX+9*SX+5,MY+SY*i-SY/2+4);
			}
		}

		for (i=0;i<9;i++) for (j=0;j<9;j++)
		if (board[i+j*9] != Const.EMPTY)
		{
			if (!flipped)
			{
				g.drawImage(pieces[black_white?1:0][board[i+j*9]],MX+PM+i*SX,MY+PM+j*SY,this);
				if (bold[i+j*9] || last_moved==i+j*9)
				{
					g.drawImage(pieces[black_white?1:0][board[i+j*9]],MX+PM+i*SX+1,MY+PM+j*SY,this);
				}
			}
			else
			{
				if ( board[i+j*9] > 14 ) /* white piece turns black */
				{
					g.drawImage(pieces[black_white?1:0][board[i+j*9]-14],MX+PM+(8-i)*SX,MY+PM+(8-j)*SY,this);
					if (bold[i+j*9] || last_moved==i+j*9)
					{
						g.drawImage(pieces[black_white?1:0][board[i+j*9]-14],MX+PM+(8-i)*SX+1,MY+PM+(8-j)*SY,this);
					}
				}
				else /* black piece turns white */
				{
					g.drawImage(pieces[black_white?1:0][board[i+j*9]+14],MX+PM+(8-i)*SX,MY+PM+(8-j)*SY,this);
					if (bold[i+j*9] || last_moved==i+j*9)
					{
						g.drawImage(pieces[black_white?1:0][board[i+j*9]+14],MX+PM+(8-i)*SX+1,MY+PM+(8-j)*SY,this);
					}
				}
			}
		}

		drawCircles(g);
		drawPiecesInHand(g);
		drawSide(g);
		drawLastMove(g);
		if (shownames) drawPlayers(g);
	}


	public void drawPlayers(Graphics g)
	{
		if (w_black_player.getText().trim().length()>0)
		if (w_white_player.getText().trim().length()>0)
		{
			String players=new String(w_black_player.getText().trim()+"  vs  "+w_white_player.getText().trim());
			if (black_white)
			{
				g.setFont(new Font("Helvetica",Font.PLAIN,12));
				px=MX+((SX*9)/2)-((players.length()/2)*6);
				if (px<5) px=5;
				g.drawString(players,px,MY+9*SY+15);
			}
			else
			{
				g.setFont(new Font("Helvetica",Font.BOLD,14));
				px=MX+((SX*9)/2)-((players.length()/2)*7);
				if (px<5) px=5;
				g.drawString(players,px,MY+9*SY+18);
			}
			if (black_white) g.setFont(new Font("Courier",Font.PLAIN,11)); else g.setFont(new Font("Courier",Font.BOLD,12));
		}
	}

	public void drawMarkers(Graphics g)
	{
		// Left Upper corner
		g.drawLine(2,2,10,2);
		g.drawLine(2,2,2,10);

		// Right Upper corner
		g.drawLine(MX+9*SX+HX+7,2,MX+9*SX+HX+15,2);
		g.drawLine(MX+9*SX+HX+15,2,MX+9*SX+HX+15,10);

		// Right Lower corner
		g.drawLine(MX+9*SX+HX+7,MY+9*SY+5,MX+9*SX+HX+15,MY+9*SY+5);
		g.drawLine(MX+9*SX+HX+15,MY+9*SY+5,MX+9*SX+HX+15,MY+9*SY-3);

		// Left Lower corner
		g.drawLine(2,MY+9*SY+5,10,MY+9*SY+5);
		g.drawLine(2,MY+9*SY+5,2,MY+9*SY-3);
	}

	public void setMessage(String s)
	{
		message.setLength(0);
		message.append(s);

		if (gg==null) gg=getGraphics();
		if (gg==null) return; // in case the window is not yet created.

                gg.setColor(Const.MESS_COLOR); 
		gg.fillRect(0,MY+9*SY+SY,640,20); 
		gg.setColor(Color.black);
		if (black_white) 
			gg.setFont(new Font("Courier",Font.PLAIN,11)); 
		else    gg.setFont(new Font("Courier",Font.BOLD,12));
		gg.drawString(message.toString(),10,MY+9*SY+SY-1+15);
	}

	public void drawSide(Graphics g)
	{
		if (draw_moves)
		sidelabel.setText( ((side==Const.BLACK)?"Black to play":"White to play") + " / "+gametype);
	}

	public void drawLastMove(Graphics g)
	{
		if (black_white && draw_moves)
		{
                        Color oldcol=g.getColor();
                        g.setColor(Color.white); g.fillRect(MX+10*SX+HX,MY+9*SY+7,100,20); g.setColor(oldcol);
			g.setFont(new Font("Helvetica",Font.BOLD,14));

                        if (firstmovedone && game.thisMove().piece>0)
                        {
                                String disp=game.thisMove().ASCII_move(new String());
                                int mnr;
                                if ((game.movenr % 2) == 0)
                                {
                                        mnr = ((game.movenr+1)/2)+1;
                                        g.drawString(""+(mnr)+"."+disp,MX+10*SX+HX+5,MY+9*SY+20);
                                }
                                else
                                {
                                        mnr = ((game.movenr+1)/2);
                                        g.drawString(""+(mnr)+"..."+disp,MX+10*SX+HX+5,MY+9*SY+20);
                                }
                        }
			g.setFont(new Font("Courier",Font.PLAIN,11));
		}
	}

	public void drawCircles(Graphics g)
	{
		if (!black_white)
		{
			g.fillOval(MX-BR+3*SX,MY-BR+3*SY, 2*BR, 2*BR);
			g.fillOval(MX-BR+6*SX,MY-BR+3*SY, 2*BR, 2*BR);
			g.fillOval(MX-BR+3*SX,MY-BR+6*SY, 2*BR, 2*BR);
			g.fillOval(MX-BR+6*SX,MY-BR+6*SY, 2*BR, 2*BR);
		}
	}

	public void drawPiecesInHand(Graphics g)
	{
		int i,j;
		Color oldcolor=g.getColor();
		Color brown=new Color(139,69,19);

		// NOW DRAW THE PIECES IN HAND:

		Color oldcol=g.getColor();
		if (black_white)
		{
			g.setColor(Color.white); 
			g.fillRect(MX-HX-10,MY,HX,7*HX+25); // for White
			g.fillRect(IHX,MY,HX,7*HX+30);         // for Black
		}
		else
		{
			g.setColor(new Color(255,165,0));
			g.fillRect(IHX-1,IHYW+2,2*BX,BY);
			g.fillRect(IHX-2,IHYB-6,2*BX,BY);
		}
		g.setColor(oldcol);

		if (!black_white)
		{
			for (i=1;i<5;i++)
			{
				if (pieces_in_hand[ Const.WHITE ^ (flipped?1:0)][i] != 0) // Do we have a Const.WHITE piece ?
				{
					g.drawImage(pieces[black_white?1:0][i+14],IHX+HX*(i-1)+PM,IHYW+PM,this);
					if (pieces_in_hand[Const.WHITE ^ (flipped?1:0)][i] > 1)
					g.drawString(""+pieces_in_hand[Const.WHITE ^ (flipped?1:0)][i],IHX+HX*i-16,IHYW+HY-7);

				}
				if (pieces_in_hand[Const.BLACK ^ (flipped?1:0)][i] != 0) // Do we have a BLACK piece ?
				{
					g.drawImage(pieces[black_white?1:0][i   ],IHX+HX*(i-1)+PM,IHYB+PM,this);
					if (pieces_in_hand[Const.BLACK ^ (flipped?1:0)][i] > 1)
					g.drawString(""+pieces_in_hand[Const.BLACK ^ (flipped?1:0)][i],IHX+HX*i-18,IHYB+6);
				}
			}
			for (i=5;i<8;i++)
			{
				if (pieces_in_hand[Const.WHITE ^ (flipped?1:0)][i] != 0) // Do we have a Const.WHITE piece ?
				{
					g.drawImage(pieces[black_white?1:0][i+14],IHX+HX*(i-5)+PM,IHYW+PM+HY,this);
					if (pieces_in_hand[Const.WHITE ^ (flipped?1:0)][i] > 1)
					g.drawString(""+pieces_in_hand[Const.WHITE ^ (flipped?1:0)][i],IHX+HX*(i-4)-16,IHYW+2*HY-2);
				}
				if (pieces_in_hand[Const.BLACK ^ (flipped?1:0)][i] != 0) // Do we have a BLACK piece ?
				{
					g.drawImage(pieces[black_white?1:0][i   ],IHX+HX*(i-5)+PM,IHYB+PM+HY,this);
					if (pieces_in_hand[Const.BLACK ^ (flipped?1:0)][i] > 1)
					g.drawString(""+pieces_in_hand[Const.BLACK ^ (flipped?1:0)][i],IHX+HX*(i-4)-18,IHYB+6+HY);
				}
			}
		}
		else
		{
			int countb=0;
			int countw=0;

			for (i=0;i<9;i++) pihand[Const.BLACK][i]=0;
			for (i=0;i<9;i++) pihand[Const.WHITE][i]=0;

			for (i=Const.PAWN;i<Const.KING;i++)
			{
				if (pieces_in_hand[Const.WHITE ^ (flipped?1:0)][i] != 0) // Do we have a Const.WHITE piece ?
				{
					pihand[side][countw]=i;
                                        g.drawImage(pieces[black_white?1:0][i+14],MX-HX-0,HY*(countw)+MY,this);
                                        if (pieces_in_hand[Const.WHITE ^ (flipped?1:0)][i] > 1)
                                                g.drawString(""+pieces_in_hand[Const.WHITE ^ (flipped?1:0)][i],MX-HX-0+SX-6,HY*(countw)+MY+HY-6);
                                        countw++;

				}
			}
			for (i=Const.PAWN;i<Const.KING;i++)
			{
				if (pieces_in_hand[Const.BLACK ^ (flipped?1:0)][i] != 0) // Do we have a BLACK piece ?
				{
					pihand[side][countb]=i;
					g.drawImage(pieces[black_white?1:0][i   ],IHX,MY+9*SY-HY-HY*countb,this);
					if (pieces_in_hand[Const.BLACK ^ (flipped?1:0)][i] > 1)
					g.drawString(""+pieces_in_hand[Const.BLACK ^ (flipped?1:0)][i],IHX+SX-3,MY+9*SY-HY*countb+6-HY);
					countb++;
				}
			}
		}
	}

	public void fill_pihand()
	{
		int i;
		int countb=0;
		int countw=0;

		for (i=0;i<9;i++) pihand[Const.BLACK][i]=0;
		for (i=0;i<9;i++) pihand[Const.WHITE][i]=0;

		for (i=Const.PAWN;i<Const.KING;i++)
		{
			if (pieces_in_hand[Const.WHITE ^ (flipped?1:0)][i] != 0) // Do we have a Const.WHITE piece ?
			{
				pihand[Const.WHITE][countw]=i;
				//System.out.println("pihand[side]["+countw+"]="+pihand[side][countw]);
				countw++;
			}
		}
		for (i=Const.PAWN;i<Const.KING;i++)
		{
			if (pieces_in_hand[Const.BLACK ^ (flipped?1:0)][i] != 0) // Do we have a BLACK piece ?
			{
				pihand[Const.BLACK][countb]=i;
				countb++;
			}
		}
	}


	public boolean keyUp(Event evt, int key)
	{
                if (key==(int)' ')
                {
                        space_down=false;
                }

		return true;
	}

	public void drawPieceBackground(Graphics g, int x, int y)
	{
		Color oldcol=g.getColor();
		if (black_white) g.setColor(Color.white); else g.setColor(new Color(255,165,0));
		g.fillRect(MX+x*SX,MY+y*SY,SX,SY);
		g.setColor(oldcol);
		g.drawRect(MX+x*SX,MY+y*SY,SX,SY);
	}

	public boolean onBoard(int x, int y)
	{
		
		return ((x>MX) && (x<MX+9*SX) && (y>MY) && (y<MY+9*SY));
	}

	public boolean inHandBlack(int x, int y)
	{
		if (!black_white)
		{
			return (x>IHX && x<(IHX+4*HX) && y>IHYB && y<(IHYB+2*HY));
		}
		else
		{
			return (x>IHX && x<(IHX+HX));
		}
	}

	public boolean inHandWhite(int x, int y)
	{
		if (!black_white)
		{
			return (x>IHX && x<(IHX+4*HX) && y>IHYW && y<(IHYW+2*HY));
		}
		else
		{
			return (x>(MX-HX-10) && x<MX);
		}
	}

	//
	// What to do if the user presses the mouse button ?
	//
	public boolean mouseDown(Event evt, int x, int y)
	{
		int piece;

		space_down|=evt.metaDown();

		from_clicked=false;
		from_clicked_drop=false;

		if ( onBoard(x,y) )
		{ 
			// Now we know the mouse was clicked ON THE BOARD : FROM

			x_clicked=(x-MX)/SX;
			y_clicked=(y-MY)/SY;

			if (flipped)
			{
				x_clicked=8-x_clicked;
				y_clicked=8-y_clicked;
			}

			piece=board[x_clicked+9*y_clicked];
			if (piece == Const.EMPTY)
			{
				return true; // We can't move an empty field
			}
			if ( side == Const.BLACK && piece>14) 
			{
				setMessage("Wrong color to play with");
				return true; // It's not White's turn
			}
			if ( side == Const.WHITE && piece<15) 
			{
				setMessage("Wrong color to play with");
				return true; // It's not Black's turn
			}
			setMessage(" ");
			from_clicked=true;
			return true;
		}

		if (flipped) changeColor();
		if (!black_white)
		{
			if ((side==Const.BLACK) && inHandBlack(x,y))
			{ 
				// We clicked on Black's pieces in hand

				piece_to_drop=(x-IHX)/HX+((y-IHYB)/HY)*4 +1;
				from_clicked_drop=true;
			}
			if ((side==Const.WHITE) && inHandWhite(x,y))
			{ 
				// We clicked on White's pieces in hand

				piece_to_drop=(x-IHX)/HX+((y-IHYW)/HY)*4 +1;
				from_clicked_drop=true;
			}
		}
		else
		{
			fill_pihand();
			if (side==Const.BLACK)
			{
				if (x>IHX && x<(IHX+HX))
				{
					int ptd=((MY+9*SY)-y)/HY;
					if (ptd<0 || ptd>7) return true;

					piece_to_drop=pihand[side][ ptd ];
					//System.out.println("BLACK clicked on "+piece_to_drop+"   "+ptd);
					if (piece_to_drop<Const.KING) from_clicked_drop=true;
				}
			}
			else // side==Const.WHITE
			{
				if ( x>(MX-HX-10) && x<MX)
				{
					int ptd=(y-MY)/HY;
					if (ptd<0 || ptd>7) return true;

					piece_to_drop=pihand[side][ ptd ];
					//System.out.println("Const.WHITE clicked on "+piece_to_drop+"   "+ptd);
					if (piece_to_drop<Const.KING) from_clicked_drop=true;
				}
			}
		}
		if (flipped) changeColor();

		return true;
	}


	//
	// What to do if the user releases the mouse button ?
	//
	public boolean mouseUp(Event evt, int x, int y)
	{
		int xx,yy;
		int piece;
		int taken;
		String s=new String();
		String b=new String();

		if (from_clicked)
		{
			from_clicked=false;
			if ( onBoard(x,y) )
			{ // Now we know the mouse was clicked ON THE BOARD : TO
				xx=(x-MX)/SX;
				yy=(y-MY)/SY;

				if (flipped)
				{
					xx=8-xx;
					yy=8-yy;
				}

				piece=board[x_clicked+9*y_clicked];
				taken=board[xx+9*yy];
				if (xx!=x_clicked || yy!=y_clicked)
				{
					//
					// Better check if this is a vallid move !!!!!!!
					// You never know who's entering this game... :-)
					//
					if (checkMove(piece, x_clicked, y_clicked, xx, yy)==false) return true; 

					if (taken!=Const.EMPTY)  // check if we do a legal move
					{ 
						// we can't capture our own pieces !!!
						if (side==Const.BLACK && taken<15) 
						{
							space_down=false;
							setMessage("takes own piece"); 
							return true; 
						}
						if (side==Const.WHITE && taken>14) 
						{
							space_down=false;
							setMessage("takes own piece"); 
							return true; 
						}
					}
					// CHECK FOR PROMOTION !!!!!!!

					piece_promoted=false;
					promote=false;
					if ( (side==Const.BLACK) && (y_clicked<3 || yy<3) &&
					     (piece!=Const.BLACK_GOLD&&piece<Const.BLACK_KING))
					{
						if ( (piece==Const.BLACK_PAWN && yy==0) || (piece==Const.BLACK_LANCE && yy==0) || (piece==Const.BLACK_KNIGHT && yy<=1))
						{
							promote=true;
						}
						else
						{
							promote=!space_down;
						}
						if (promote)
						{
							if (piece<Const.BLACK_BISHOP) piece+=8; else piece+=7;
							piece_promoted=true;
						}
					}
					if ( (side==Const.WHITE) && (y_clicked>5 || yy>5) &&
					     (piece!=Const.WHITE_GOLD&&piece<Const.WHITE_KING))
					{
						if ( (piece==Const.WHITE_PAWN && yy==8) ||
						     (piece==Const.WHITE_LANCE && yy==8) ||
						     (piece==Const.WHITE_KNIGHT && yy>=7)) promote=true;
						else
						{
							promote=!space_down;
						}
						if (promote) 
						{
							if (piece<Const.WHITE_BISHOP) piece+=8; else piece+=7;
							piece_promoted=true;
						}
					}
					
					if (game.thisLevel().variation.length-2<=game.movenr) 
						game.reAllocateMoves(10);
					game.thisMove().setMove(x_clicked,y_clicked,xx,yy,piece,taken,piece_promoted);
					drawLastMove(gg);
					setComment( game.thisMove() );
					game.nextMove().clearMove();
					game.forward();
					game.thisMove().moves=game.lsize();

					piece_promoted=false;
					
					drawCircles(gg);
					drawSide(gg);
				}
			}
		}
		else
		if (from_clicked_drop)
		{
			space_down=false;
			from_clicked_drop=false;
			if ((x>MX && x<(MX+9*SX)) && (y>MY && y<(MY+9*SY)))
			{ // Now we know the mouse was clicked ON THE BOARD : TO
				xx=(x-MX)/SX;
				yy=(y-MY)/SY;

				if (flipped)
				{
					xx=8-xx;
					yy=8-yy;
				}

				taken=board[xx+9*yy];

				// CHECK THE VALIDITY OF THE MOVE !!
				
				if (taken != Const.EMPTY)
				{
					return true; // Can't drop on top of another piece !
				}
				if (pieces_in_hand[side][piece_to_drop]==0) 
				{
					return true; // You don't have this piece in hand !
				}
				if (piece_to_drop==Const.BLACK_PAWN || piece_to_drop==Const.WHITE_PAWN)
				{
					if (PawnOnColumn(xx)) return true; // Can't drop 2 pawns on a column !
				}
				if (piece_to_drop==Const.BLACK_PAWN || piece_to_drop==Const.BLACK_LANCE)
				{
					// Can't drop a lance/pawn on the last line !
					if (side==Const.BLACK && yy==0) return true;
					if (side==Const.WHITE && yy==8) return true;
				}
				if (piece_to_drop==Const.BLACK_KNIGHT)
				{
					// Can't drop a knight on the last 2 lines !
					if (side==Const.BLACK && yy<=1) return true;
					if (side==Const.WHITE && yy>=7) return true;
				}

				if (game.thisLevel().variation.length-2<=game.movenr) 
					game.reAllocateMoves(10); // 10 more moves.
				game.thisLevel().variation[game.movenr].setMove(Const.DROP,xx+9*yy,piece_to_drop,taken,false);
				doMove( game.thisLevel().variation[game.movenr] );
				drawLastMove(gg);
				setComment( game.thisLevel().variation[game.movenr] );
				game.movenr++;
				game.thisLevel().variation[game.movenr].clearMove();
				game.thisMove().moves=game.lsize();

				drawPiecesInHand(gg);
				drawCircles(gg);

				side = side^1; drawSide(gg);
			}
		}
		space_down=false;
		//if (piece_promoted) setMessage("press SPACE if you don't want to promote the piece !");

		return true;
	}

	//
	// Returns true if there is a pawn on a column
	//
	public boolean PawnOnColumn(int col)  
	{
		for (int i=0;i<9;i++)
		{
			if (side==Const.BLACK)
			{
				if (board[col+i*9]==Const.BLACK_PAWN) return true;
			}
			else
			{
				if (board[col+i*9]==Const.WHITE_PAWN) return true;
			}
		}
		return false;
	}

	public boolean checkMove(int piece, int fromx,int fromy,int tox,int toy)
	{
		int from, to;

		from=fromx+fromy*9;
		to  =tox  +toy  *9;
		
		int d=to-from;
	
		switch(piece)
		{
			case Const.BLACK_PAWN :
			{
				if (d!=Const.UP) return false;
			} break;
			case Const.BLACK_LANCE :
			{
				if (d%Const.UP != 0) return false; // NOT COMPLETE
			} break;
			case Const.BLACK_KNIGHT :
			{
				if (d!=Const.LEFTUPL  && d!=Const.RIGHTUPL) return false;
			} break;
			case Const.BLACK_SILVER :
			{
				if (d!=Const.LEFTUP && d!=Const.UP && d!=Const.RIGHTUP && d!=Const.LEFTDOWN && d!=Const.RIGHTDOWN) return false;
			} break;
			case Const.BLACK_GOLD : case Const.BLACK_PPAWN : case Const.BLACK_PLANCE : case Const.BLACK_PKNIGHT : case Const.BLACK_PSILVER :
			{
				if (d!=Const.LEFTUP && d!=Const.UP && d!=Const.RIGHTUP && d!=Const.DOWN && d!=Const.LEFT && d!=Const.RIGHT) return false;
			} break;
			case Const.BLACK_BISHOP :
			{
				if ( (d%Const.LEFTDOWN!=0) && (d%Const.RIGHTDOWN!=0) && 
				     (d%Const.LEFTUP!=0) && (d%Const.RIGHTUP!=0)) return false; // NOT COMPLETE !
			} break;
			case Const.BLACK_ROOK :
			{
				if ( fromx!=tox && fromy!=toy) return false; // NOT COMPLETE
			} break;
			case Const.BLACK_KING :
			{
				if (d!=Const.LEFTUP && d!=Const.UP && d!=Const.RIGHTUP && d!=Const.LEFT && d!=Const.LEFTDOWN && 
				     d!=Const.DOWN && d!=Const.RIGHTDOWN && d!=Const.RIGHT) return false;
			} break;
			case Const.BLACK_PBISHOP :
			{
				if ( (d%Const.LEFTDOWN!=0) && (d%Const.RIGHTDOWN!=0) && (d%Const.LEFTUP!=0) && (d%Const.RIGHTUP!=0) 
				      && d!=Const.UP && d!=Const.LEFT && d!=Const.DOWN && d!=Const.RIGHT) return false; // NOT COMPLETE !
			} break;
			case Const.BLACK_PROOK :
			{
				if ( fromx!=tox && fromy!=toy && 
				     d!=Const.LEFTUP && d!=Const.RIGHTUP && d!=Const.LEFTDOWN && d!=Const.RIGHTDOWN) return false; // NOT COMPLETE
			} break;
			
			
			
			case Const.WHITE_PAWN :
			{
				if (d!=Const.DOWN) return false;
			} break;
			case Const.WHITE_LANCE :
			{
				if (d%Const.DOWN != 0) return false; // NOT COMPLETE
			} break;
			case Const.WHITE_KNIGHT :
			{
				if (d!=Const.LEFTDOWNL  && d!=Const.RIGHTDOWNL) return false;
			} break;
			case Const.WHITE_SILVER :
			{
				if (d!=Const.LEFTUP && d!=Const.DOWN && d!=Const.RIGHTUP && d!=Const.LEFTDOWN && d!=Const.RIGHTDOWN) return false;
			} break;
			case Const.WHITE_GOLD : case Const.WHITE_PPAWN : case Const.WHITE_PLANCE : case Const.WHITE_PKNIGHT : case Const.WHITE_PSILVER :
			{
				if (d!=Const.LEFTDOWN && d!=Const.UP && d!=Const.RIGHTDOWN && d!=Const.DOWN && d!=Const.LEFT && d!=Const.RIGHT) return false;
			} break;
			case Const.WHITE_BISHOP :
			{
				if ( (d%Const.LEFTDOWN!=0) && (d%Const.RIGHTDOWN!=0) && 
				     (d%Const.LEFTUP!=0) && (d%Const.RIGHTUP!=0)) return false; // NOT COMPLETE !
			} break;
			case Const.WHITE_ROOK :
			{
				if ( fromx!=tox && fromy!=toy) return false; // NOT COMPLETE
			} break;
			case Const.WHITE_KING :
			{
				if (d!=Const.LEFTUP && d!=Const.UP && d!=Const.RIGHTUP && d!=Const.LEFT && d!=Const.LEFTDOWN && 
				     d!=Const.DOWN && d!=Const.RIGHTDOWN && d!=Const.RIGHT) return false;
			} break;
			case Const.WHITE_PBISHOP :
			{
				if ( (d%Const.LEFTDOWN!=0) && (d%Const.RIGHTDOWN!=0) && (d%Const.LEFTUP!=0) && (d%Const.RIGHTUP!=0) 
				      && d!=Const.UP && d!=Const.LEFT && d!=Const.DOWN && d!=Const.RIGHT) return false; // NOT COMPLETE !
			} break;
			case Const.WHITE_PROOK :
			{
				if ( fromx!=tox && fromy!=toy && 
				     d!=Const.LEFTUP && d!=Const.RIGHTUP && d!=Const.LEFTDOWN && d!=Const.RIGHTDOWN) return false; // NOT COMPLETE
			} break;
		}
	
		return true;
	}

	public void setComment(Move m)
	{
		if (m==null) return;
		if (m.comment!=null)
		{
			comm_frame.comment.setText(m.comment); 
			if (!comm_frame.isShowing() && comm_frame.auto.getState() && draw_moves)
			{
				setMessage("There is a remark in the comment window");
				comm_frame.show();
			}
		}
		else 
		{
			comm_frame.comment.setText(" ");
			if (comm_frame.isShowing() && comm_frame.auto.getState() || !draw_moves)
			{
				comm_frame.hide();
			}
		}
	}

	public void setVariation(Move m)
	{
		if (!draw_moves)
		{
			choice_frame.hide();
			return;
		}

		Vector var=game.getVariations();
		if (var==null)
		{
			choice_frame.hide();
			return;
		}
		
		choice_frame.setChoices(var);
		choice_frame.show();
		
		var=null; // cleanup
	}

	public void doMove(Move m)
	{
		taken=m.taken;
		if (game.movenr==0) firstmovedone=true;

		prev_moved=last_moved;
		last_moved=m.to;

		if (m.from != Const.DROP)
		{
			board[ m.to   ] = m.piece;
			board[ m.from ] = Const.EMPTY;
			if (draw_moves)
			{
				if (!flipped)
				{
					drawPieceBackground(gg,m.fx(),m.fy());
					if (taken!=0) drawPieceBackground(gg,m.tx(),m.ty());
					gg.drawImage(pieces[black_white?1:0][m.piece],MX+m.tx()*SX+PM  ,MY+m.ty()*SY+PM,this);
				}
				else
				{
					drawPieceBackground(gg,8-m.fx(),8-m.fy());
					if (taken!=0) drawPieceBackground(gg,8-m.tx(),8-m.ty());
					if ( m.piece > 14 ) /* white piece turns black */
					{
						gg.drawImage(pieces[black_white?1:0][m.piece-14],MX+(8-m.tx())*SX+PM,MY+(8-m.ty())*SY+PM,this);
					}
					else
					{
						gg.drawImage(pieces[black_white?1:0][m.piece+14],MX+(8-m.tx())*SX+PM,MY+(8-m.ty())*SY+PM,this);
					}
				}
			}
		}
		else
		{
			board[ m.to   ] = m.piece+side*14;
			pieces_in_hand[side][m.piece   ]--;
			if (draw_moves)
			{
				if (!flipped)
				{
					gg.drawImage(pieces[black_white?1:0][m.piece+side*14],MX+m.tx()*SX+PM,MY+m.ty()*SY+PM,this);
				}
				else
				{
					if ( m.piece+side*14 > 14 ) /* white piece turns black */
					{
						gg.drawImage(pieces[black_white?1:0][m.piece+side*14-14],MX+(8-m.tx())*SX+PM,MY+(8-m.ty())*SY+PM,this);
					}
					else
					{
						gg.drawImage(pieces[black_white?1:0][m.piece+side*14+14],MX+(8-m.tx())*SX+PM,MY+(8-m.ty())*SY+PM,this);
					}
				}
				drawPiecesInHand(gg);
			}
		}
		if (prev_moved>=0 && prev_moved<81 && draw_moves)
		{
			int prev=board[prev_moved];
			int tx=prev_moved%9;
			int ty=prev_moved/9;


			if (!flipped)
			{
				if (black_white)
				{
					drawPieceBackground(gg,tx,ty);
					gg.drawImage(pieces[black_white?1:0][prev],MX+tx*SX+PM,MY+ty*SY+PM,this);
				}
				else
				{
					gg.drawRect(MX+tx*SX,MY+ty*SY,SX,SY);
				}
			}
			else
			{
				if (black_white)
				{
					drawPieceBackground(gg,8-tx,8-ty);
					if ( prev > 14 ) /* white piece turns black */
					{
						gg.drawImage(pieces[black_white?1:0][prev-14],MX+(8-tx)*SX+PM,MY+(8-ty)*SY+PM,this);
					}
					else
					{
						gg.drawImage(pieces[black_white?1:0][prev+14],MX+(8-tx)*SX+PM,MY+(8-ty)*SY+PM,this);
					}
				}
				else
				{
					gg.drawRect(MX+(8-tx)*SX,MY+(8-ty)*SY,SX,SY);
				}
			}
		}
		if (last_moved>=0 && last_moved<81 && draw_moves)
		{
			int last=board[last_moved];
			int tx=last_moved%9;
			int ty=last_moved/9;

			if (!flipped)
			{
				if (black_white)
				{
					gg.drawImage(pieces[black_white?1:0][last],MX+tx*SX+PM+1,MY+ty*SY+PM,this);
				}
				else
				{
					gg.setColor(Color.red);
					gg.drawRect(MX+tx*SX,MY+ty*SY,SX,SY);
					gg.setColor(Color.black);
				}
			}
			else
			{
				if (black_white)
				{
					if ( last > 14 ) /* white piece turns black */
					{
						gg.drawImage(pieces[black_white?1:0][last-14],MX+(8-tx)*SX+PM+1,MY+(8-ty)*SY+PM,this);
					}
					else
					{
						gg.drawImage(pieces[black_white?1:0][last+14],MX+(8-tx)*SX+PM+1,MY+(8-ty)*SY+PM,this);
					}
				}
				else
				{
					gg.setColor(Color.red);
					gg.drawRect(MX+(8-tx)*SX,MY+(8-ty)*SY,SX,SY);
					gg.setColor(Color.black);
				}
			}
		}
		if (taken != 0)
		{
			if (side==Const.BLACK)
			{
				if (taken<15) System.out.println("Can't take own BLACK piece !!!");
				if (taken>26) pieces_in_hand[Const.BLACK][taken-21]++; else
				if (taken>22) pieces_in_hand[Const.BLACK][taken-22]++;
				else          pieces_in_hand[Const.BLACK][taken-14]++;
			}
			else
			{
				if (taken>14) System.out.println("Can't take own Const.WHITE piece !!!");
				if (taken>12) pieces_in_hand[Const.WHITE][taken- 7]++; else
				if (taken> 8) pieces_in_hand[Const.WHITE][taken- 8]++;
				else          pieces_in_hand[Const.WHITE][taken   ]++;
			}
			if (draw_moves) drawPiecesInHand(gg);
		}
		if (draw_moves) drawCircles(gg);
	}

	public void undoMove(Move m)
	{
		piece=m.piece;
		taken=m.taken;
		if (m.comment!=null) comm_frame.comment.setText(m.comment); else comm_frame.comment.setText(" ");

		last_moved=prev_moved;
		prev_moved=-1;

		if (m.promotes)
		{
			if (side==Const.BLACK)
			{
				if (piece<Const.BLACK_PBISHOP) piece-=8; else piece-=7;
			}
			else
			{
				if (piece<Const.WHITE_PBISHOP) piece-=8; else piece-=7;
			}
		}

		if (m.from != Const.DROP)
		{
			board[ m.to   ] = taken;
			board[ m.from ] = piece;

			if (draw_moves)
			{
				if (!flipped)
				{
					drawPieceBackground(gg,m.fx(),m.fy());
					drawPieceBackground(gg,m.tx(),m.ty());
					gg.drawImage(pieces[black_white?1:0][piece],MX+m.fx()*SX+PM,MY+m.fy()*SY+PM,this);
					if (taken != 0)
					{
						gg.drawImage(pieces[black_white?1:0][taken],MX+m.tx()*SX+PM,MY+m.ty()*SY+PM,this);
					}
				}
				else
				{
					drawPieceBackground(gg,8-m.fx(),8-m.fy());
					drawPieceBackground(gg,8-m.tx(),8-m.ty());
					if (piece > 14)
						gg.drawImage(pieces[black_white?1:0][piece-14],MX+(8-m.fx())*SX+PM,MY+(8-m.fy())*SY+PM,this);
					else
						gg.drawImage(pieces[black_white?1:0][piece+14],MX+(8-m.fx())*SX+PM,MY+(8-m.fy())*SY+PM,this);

					if (taken != 0)
					{
						if (taken > 14)
							gg.drawImage(pieces[black_white?1:0][taken-14],MX+(8-m.tx())*SX+PM,MY+(8-m.ty())*SY+PM,this);
						else
							gg.drawImage(pieces[black_white?1:0][taken+14],MX+(8-m.tx())*SX+PM,MY+(8-m.ty())*SY+PM,this);
					}
				}
			}
		}
		else
		{
			board[ m.to   ] = Const.EMPTY;
			pieces_in_hand[side][m.piece   ]++;
			if (draw_moves)
			{
				if (!flipped)
					drawPieceBackground(gg,m.tx(),m.ty());
				else
					drawPieceBackground(gg,(8-m.tx()),(8-m.ty()));
				drawPiecesInHand(gg);
			}
		}
		if (taken != 0)
		{
			if (side==Const.BLACK)
			{
				if (taken>26) pieces_in_hand[Const.BLACK][taken-21]--; else
				if (taken>22) pieces_in_hand[Const.BLACK][taken-22]--;
				else          pieces_in_hand[Const.BLACK][taken-14]--;
			}
			else
			{
				if (taken>12) pieces_in_hand[Const.WHITE][taken- 7]--; else
				if (taken> 8) pieces_in_hand[Const.WHITE][taken- 8]--;
				else          pieces_in_hand[Const.WHITE][taken   ]--;
			}
			if (draw_moves) drawPiecesInHand(gg);
		}
		if (draw_moves) drawCircles(gg);
	}
	
	public boolean mouseEnter(Event e, int x, int y)
	{
		pmessage=message;
		     if (e.target==back       ) setMessage("Click here to go one move back");  
		else if (e.target==forward    ) setMessage("Click here to go one move forward");  
		else if (e.target==begingame  ) setMessage("Click here to start of the game");  
		else if (e.target==endgame    ) setMessage("Click here to go to the end of the game");  
		return true;
	}

	public boolean mouseLeave(Event e, int x, int y)
	{
		message=pmessage;
		return true;
	}

	public boolean mouseMove(Event e, int x, int y)
	{
		x_moved=x;
		y_moved=y;

		if (onBoard(x,y) || inHandBlack(x,y) || inHandWhite(x,y) )
		{
			if (getCursorType()!=Frame.HAND_CURSOR) setCursor(Frame.HAND_CURSOR);
		}
		else
		{
			if (getCursorType()!=Frame.DEFAULT_CURSOR) setCursor(Frame.DEFAULT_CURSOR);
		}
		return true;
	}

	public boolean keyDown(Event e, int key)
	{
		if (set_position)
		{
			int xx,yy,pos,b,p;

			if (onBoard(x_moved, y_moved))
			{
				xx=(x_moved-MX)/SX;
				yy=(y_moved-MY)/SY;

				if (flipped)
				{
					xx=8-xx;
					yy=8-yy;
				}
				
				pos=xx+9*yy;
				b=board[pos];

				switch( key )
				{
				case 'p' : if (b==Const.BLACK_PAWN)  p=Const.BLACK_PPAWN; else p=Const.BLACK_PAWN; break;
				case 'P' : if (b==Const.WHITE_PAWN)  p=Const.WHITE_PPAWN; else p=Const.WHITE_PAWN; break;
				case 'l' : if (b==Const.BLACK_LANCE)  p=Const.BLACK_PLANCE; else p=Const.BLACK_LANCE; break;
				case 'L' : if (b==Const.WHITE_LANCE)  p=Const.WHITE_PLANCE; else p=Const.WHITE_LANCE; break;
				case 'n' : if (b==Const.BLACK_KNIGHT)  p=Const.BLACK_PKNIGHT; else p=Const.BLACK_KNIGHT; break;
				case 'N' : if (b==Const.WHITE_KNIGHT)  p=Const.WHITE_PKNIGHT; else p=Const.WHITE_KNIGHT; break;
				case 's' : if (b==Const.BLACK_SILVER)  p=Const.BLACK_PSILVER; else p=Const.BLACK_SILVER; break;
				case 'S' : if (b==Const.WHITE_SILVER)  p=Const.WHITE_PSILVER; else p=Const.WHITE_SILVER; break;
				case 'g' : p=Const.BLACK_GOLD; break;
				case 'G' : p=Const.WHITE_GOLD; break;
				case 'b' : if (b==Const.BLACK_BISHOP)  p=Const.BLACK_PBISHOP; else p=Const.BLACK_BISHOP; break;
				case 'B' : if (b==Const.WHITE_BISHOP)  p=Const.WHITE_PBISHOP; else p=Const.WHITE_BISHOP; break;
				case 'r' : if (b==Const.BLACK_ROOK)  p=Const.BLACK_PROOK; else p=Const.BLACK_ROOK; break;
				case 'R' : if (b==Const.WHITE_ROOK)  p=Const.WHITE_PROOK; else p=Const.WHITE_ROOK; break;
				case 'k' : p=Const.BLACK_KING; break;
				case 'K' : p=Const.WHITE_KING; break;
				case 'd' : search_depth+=2;
					   if (search_depth>Const.MAXPLIES) search_depth=1;
					   setMessage("calc will now search "+search_depth+" plies");
					   return true;
				case 13  : case 10: changeColor(); drawSide(gg); return true;
				
				default: p=Const.EMPTY ; break;
				}
				board[pos]=p;

				if (!flipped)
				{
					drawPieceBackground(gg,xx,yy);
					if (p!=Const.EMPTY) gg.drawImage(pieces[black_white?1:0][p],MX+xx*SX+PM,MY+yy*SY+PM,this);
				}
				else
				{
					drawPieceBackground(gg,8-xx,8-yy);
					if (p!=Const.EMPTY)
					{
						if ( p > 14 ) // white piece turns black 
							gg.drawImage(pieces[black_white?1:0][p-14],MX+(8-xx)*SX+PM,MY+(8-yy)*SY+PM,this);
						else
							gg.drawImage(pieces[black_white?1:0][p+14],MX+(8-xx)*SX+PM,MY+(8-yy)*SY+PM,this);
					}
				}
			}
			else
			if (inHandBlack(x_moved,y_moved))
			{
				switch(key)
				{
					case 'p' : case 'P' : p=Const.PAWN;   break;
					case 'l' : case 'L' : p=Const.LANCE;  break;
					case 'n' : case 'N' : p=Const.KNIGHT; break;
					case 's' : case 'S' : p=Const.SILVER; break;
					case 'g' : case 'G' : p=Const.GOLD;   break;
					case 'b' : case 'B' : p=Const.BISHOP; break;
					case 'r' : case 'R' : p=Const.ROOK;   break;
					default: p=Const.EMPTY; break;
				}
				if (!flipped)
				{
					pieces_in_hand[Const.BLACK][p]++;
					if (p==Const.PAWN)
					{
						if (pieces_in_hand[Const.BLACK][p]>18) pieces_in_hand[Const.BLACK][p]=0;
					}
					else
					if (p==Const.BISHOP || p==Const.ROOK)
					{
						if (pieces_in_hand[Const.BLACK][p]>2) pieces_in_hand[Const.BLACK][p]=0;
					}
					else
					{
						if (pieces_in_hand[Const.BLACK][p]>4) pieces_in_hand[Const.BLACK][p]=0;
					}
				}
				else
				{
					pieces_in_hand[Const.WHITE][p]++;
					if (p==Const.PAWN)
					{
						if (pieces_in_hand[Const.WHITE][p]>18) pieces_in_hand[Const.WHITE][p]=0;
					}
					else
					if (p==Const.BISHOP || p==Const.ROOK)
					{
						if (pieces_in_hand[Const.WHITE][p]>2) pieces_in_hand[Const.WHITE][p]=0;
					}
					else
					{
						if (pieces_in_hand[Const.WHITE][p]>4) pieces_in_hand[Const.WHITE][p]=0;
					}
				}
				drawPiecesInHand(gg);
			}
			else
			if (inHandWhite(x_moved,y_moved))
			{
				switch(key)
				{
					case 'p' : case 'P' : p=Const.PAWN;   break;
					case 'l' : case 'L' : p=Const.LANCE;  break;
					case 'n' : case 'N' : p=Const.KNIGHT; break;
					case 's' : case 'S' : p=Const.SILVER; break;
					case 'g' : case 'G' : p=Const.GOLD;   break;
					case 'b' : case 'B' : p=Const.BISHOP; break;
					case 'r' : case 'R' : p=Const.ROOK;   break;
					default: p=Const.EMPTY; break;
				}
				if (flipped)
				{
					pieces_in_hand[Const.BLACK][p]++;
					if (p==Const.PAWN)
					{
						if (pieces_in_hand[Const.BLACK][p]>18) pieces_in_hand[Const.BLACK][p]=0;
					}
					else
					if (p==Const.BISHOP || p==Const.ROOK)
					{
						if (pieces_in_hand[Const.BLACK][p]>2) pieces_in_hand[Const.BLACK][p]=0;
					}
					else
					{
						if (pieces_in_hand[Const.BLACK][p]>4) pieces_in_hand[Const.BLACK][p]=0;
					}
				}
				else
				{
					pieces_in_hand[Const.WHITE][p]++;
					if (p==Const.PAWN)
					{
						if (pieces_in_hand[Const.WHITE][p]>18) pieces_in_hand[Const.WHITE][p]=0;
					}
					else
					if (p==Const.BISHOP || p==Const.ROOK)
					{
						if (pieces_in_hand[Const.WHITE][p]>2) pieces_in_hand[Const.WHITE][p]=0;
					}
					else
					{
						if (pieces_in_hand[Const.WHITE][p]>4) pieces_in_hand[Const.WHITE][p]=0;
					}
				}
				drawPiecesInHand(gg);
			}
		}
		else
		{
			switch(key)
			{
			case (int)' ' : space_down=true; break; // We don't want to promote a piece !
			case Event.HOME : 
					game.begin(); break;
			case Event.END  : 
					game.end(); break;
			case Event.PGUP : break;
			case Event.PGDN : break;
			case Event.UP   : prevGame(); break;
			case Event.DOWN : nextGame(); break;
			case Event.LEFT : 
					game.back(); break;                  
			case Event.RIGHT: 
					game.forward(); break;
			case Event.F1   : if ( !help.isShowing()) help.show(); else help.hide();
					break;
			case Event.F2   : if ( !data_frame.isShowing()) data_frame.show(); else data_frame.hide();
					break;
			case Event.F3   : if ( !var_frame.isShowing()) var_frame.show(); else var_frame.hide();
					break;
			case Event.F4   : if ( !comm_frame.isShowing()) comm_frame.show(); else comm_frame.hide();
					break;
			case Event.F5   : if ( !dec_frame.isShowing()) dec_frame.show(); else dec_frame.hide();
					break;
			case Event.F6   : if (connected) if ( !search_frame.isShowing()) search_frame.show(); else search_frame.hide();
					break;
			case Event.F7   : if ( !select_frame.isShowing()) select_frame.show(); else select_frame.hide();
					break;
			case Event.F8   : if ( !choice_frame.isShowing()) choice_frame.show(); else choice_frame.hide();
					break;
			case Event.F9   : flipped=!flipped;
					last_moved=80-last_moved;
					repaint();
					break;
			case Event.F10  : break;
			case Event.F11  : break;
			case Event.F12  : break;
			case (int)'!' : // Print the PSN game to the java-console & the decode window
					encodeGame(true);
					break;
			case (int)'@' : // Print the PSN games of all the games in the selection !
					boolean dm=draw_moves;
					draw_moves=false;
					encodeGame(true);  // start from the one selected.
					game_loaded=false;
					boolean stop=!select_frame.nextGame();
					while (!stop)
					{
						while (!game_loaded) // changed in the StreamListener.java
						{
							try {Thread.sleep(200);} catch(InterruptedException iex) {}
						}
						encodeGame(true);
						game_loaded=false;
						stop=!select_frame.nextGame();
					}
					draw_moves=dm;
					repaint();
					break;
			case (int)'b' : // 
			case (int)'B' : // Put a piece in Bold print !
					if (onBoard(x_moved,y_moved))
					{
						int xx=(x_moved-MX)/SX;
						int yy=(y_moved-MY)/SY;

						if (flipped)
						{
							xx=8-xx;
							yy=8-yy;
						}
						
						int pos=xx+9*yy;

						bold[pos]=!bold[pos];
						repaint();
					}
					break;
			case (int)'c' : // 
			case (int)'C' : // Clear all bold pieces except last moved.
					for (int i=0;i<81;i++) bold[i]=false;
					repaint();
					break;
			case (int)'d' : // 
			case (int)'D' : // Dump bitmap to the server and save it on disk
					sendBitmap();
					break;
			case (int)'n' : // 
			case (int)'N' : // show / don't show named of players
					shownames=!shownames;
					repaint();
					break;
			case (int)'I' : // Decode a large number of games from "import.shogi" file
					// importGames();
					break;
			default  : break;
			}
		}

		return true;
	}

	public void importGames()
	{
		if (!yesnoDialog("Do you want to start the import of file-* ?")) return;

		int count=1;
		silent_save=true;

		String file=getFile("split/file-"+count+".sho");
		while(file!=null)
		{
			System.out.println("Loaded [file-"+count+".sho]");
			boolean dm=draw_moves;
			draw_moves=false;
			game.begin();
			newGame();
			dec_frame.decodeGame(file);
			System.out.println("decoded [file-"+count+".sho]");
			game.begin();
			draw_moves=dm;
			repaint();
			copyInfo(game);
			game_saved=false;
			client.sendGame(game);
			
			while (!game_saved) // changed in the StreamListener.java
			{
				try {Thread.sleep(200);} catch(InterruptedException iex) {}
			}
			count++;
			file=null;
			file=getFile("split/file-"+count+".sho");
		}
		silent_save=false;
	}

	public boolean yesnoDialog(String s)
	{
		MyDialog md=new MyDialog(this,s,MyDialog.YESNO,MyDialog.NOTHING,true);
		md.show();
		return md.result;
	}

	public void prevGame()
	{
		if (!PSN_source) select_frame.previousGame();
		else
		{
			if (PSN_gameNumber>0)
			{
				boolean dm=draw_moves;
				draw_moves=false;
				PSN_gameNumber--;
				game.begin();
				newGame();
				dec_frame.decodeGame( PSN_games[PSN_gameNumber] );
				game.begin();
				draw_moves=dm;
				repaint();
			}
		}
	}

	public void nextGame()
	{
		if (!PSN_source) select_frame.nextGame();
		else
		{
			if (PSN_gameNumber<PSN_gameCount-1)
			{
				boolean dm=draw_moves;
				draw_moves=false;
				PSN_gameNumber++;
				game.begin();
				newGame();
				dec_frame.decodeGame( PSN_games[PSN_gameNumber] );
				game.begin();
				draw_moves=dm;
				repaint();
			}
		}
	}

	public void encodeGame(boolean clear)
	{
		StringBuffer out=new StringBuffer("");
		boolean dp=draw_moves;
		int mnr=game.movenr;
		List ll=new List();

		draw_moves=false;
		game.begin();
		if (!clear) out.append( dec_frame.game.getText() );
		out.append("\n");
		if (w_black_player.getText().trim().length()!=0)
		{
			out.append("[Sente \""+w_black_player.getText());
			if (w_black_grade.getSelectedItem().equals("Grade"))
				out.append("\"]\n");
			else
				out.append(", "+w_black_grade.getSelectedItem()+"\"]\n");
		}
		if (w_white_player.getText().trim().length()!=0)
		{
			out.append("[Gote \""+w_white_player.getText());
			if (w_white_grade.getSelectedItem().equals("Grade"))
				out.append("\"]\n");
			else
				out.append(", "+w_white_grade.getSelectedItem()+"\"]\n");
		}
		if (data_frame.day.getSelectedItem().trim().length()!=0 && data_frame.month.getSelectedItem().trim().length()!=0)
		{
			if (dec_frame.cmi_encesn.getState())
			{
				String one  = data_frame.century.getSelectedItem()+data_frame.year.getSelectedItem();
				String two  = months[ Integer.parseInt( data_frame.month.getSelectedItem() ) ];
				String three= data_frame.day.getSelectedItem();
				out.append("[Date \""+one+" "+two+" "+three+"\"]\n");
			}
			else
			{
				String one  = data_frame.century.getSelectedItem()+data_frame.year.getSelectedItem();
				String two  = data_frame.month.getSelectedItem();
				String three= data_frame.day.getSelectedItem();
				out.append("[Date \""+one+"/"+two+"/"+three+"\"]\n");
			}
		}
		if (w_country.getText().trim().length()!=0) out.append("[Country \""+w_country.getText()+"\"]\n");
		if (w_tournament.getText().trim().length()!=0) out.append("[Event \""+w_tournament.getText()+"\"]\n");
		if (w_round.getText().trim().length()!=0) out.append("[Round \""+w_round.getText()+"\"]\n");
		if (w_result.getText().trim().length()!=0) out.append("[Result \""+w_result.getText()+"\"]\n");
		if (w_source.getText().trim().length()!=0) out.append("[Source \""+w_source.getText()+"\"]\n");

		char ch='A';
		int comments=0;
		int i,j;
		boolean first=true;

		return_counter=0;

		for (i=0;i<game.size();i++)
		{
			if (game.root.variation[i].comment!=null)
			{
				if (dec_frame.cmi_encesn.getState())
				{
					out.append("{"+game.root.variation[i].comment.replace('\n','~')+"}");
				}
				else
				{
					out.append("("+ch+")");
					ll.addItem(game.root.variation[i].comment);
					comments++;
					ch=(char)( (int)ch + 1 );
				}
			}
			if (!first) out.append(" "); else first=false;
			if (return_counter%12==0 && return_counter>0) out.append("\n");
			if (i%2==0)
			{
				if (i/2+1<10) out.append(" ");
				out.append(""+(i/2+1));
				if (!dec_frame.cmi_encesn.getState()) out.append("."); else out.append(" ");
			}
			out.append(gen.PSN(buf,game.root.variation[i]));
			if (game.root.variation[i].variation!=null)
			{
				printVariation(out, i);
			}
			game.forward();
			return_counter++;
		}
		if (game.root.variation[i].comment!=null)
		{
			if (dec_frame.cmi_encesn.getState())
			{
				out.append("{"+game.root.variation[i].comment.replace('\n','~')+"}");
			}
			else
			{
				out.append("("+ch+")");
				ll.addItem(game.root.variation[i].comment);
				comments++;
			}
		}
		if (w_result.getText().trim().length()!=0) out.append("  "+w_result.getText());
		out.append("\n");
		ch='A';
		for (i=0;i<comments;i++)
		{
			ll.select(i);
			out.append("("+ch+") "+ll.getSelectedItem()+"\n");
			ch=(char)( (int)ch + 1 );
		}
		if (clear) System.out.print(out.toString());
		dec_frame.game.setText(out.toString());
		for (i=0;i<comments;i++) ll.delItem(0);
		game.gotoMove(mnr);
		draw_moves=dp;
	}

	public int return_counter;

	public void printVariation(StringBuffer s, int movenr)
	{
		printVariation(game.root.variation[i],s,movenr);
	}

	public void printVariation(Move m, StringBuffer s, int movenr)
	{
		int i;

                s.append(" (");
		return_counter++;
                for (i=0;i<m.moves;i++)
                {
			if ((movenr+i)%2==0)
			{
				if ((movenr+i)/2+1<10) s.append(" ");
				s.append(""+((movenr+i)/2+1));
				if (!dec_frame.cmi_encesn.getState()) s.append("."); else s.append(" ");
			}
			else
			{
				if (i==0)
				{
					s.append(""+((movenr+i)/2+1)+"...");
				}
			}
                        s.append(" "+gen.PSN(buf, m.variation[i]));
                        if (m.variation[i].variation!=null)
                        {
                                printVariation( m.variation[i], s, movenr+i );
                        }
			doMove(m.variation[i]);
			changeColor();

			if (return_counter%12==0 && return_counter>0) s.append("\n");
			return_counter++;
                }
                for (i=m.moves-1;i>=0;i--)
                {
			changeColor();
			undoMove(m.variation[i]);
		}
                s.append(" )");
		return_counter++;
	}

	public boolean action(Event e, Object arg) 
	{
		String label=null;
		try { label=(String)arg; } catch(ClassCastException cce) {}

		if (label==null) label="Dummy for argument";

		if (label.equals(Const.BACK_BUTTON) || label.equals(MENU_MOVE_BACK)) 
		{
			game.back();
			if (game.thisLevel().variation[game.movenr].comment==null) setMessage(" ");
		}
		if (label.equals(Const.FORW_BUTTON) || label.equals(MENU_MOVE_FORWARD)) 
		{
			game.forward();
			if (game.thisLevel().variation[game.movenr].comment==null) setMessage(" ");
		}
		if (label.equals(Const.BEGN_BUTTON) || label.equals(MENU_MOVE_BEGIN)) 
		{
			game.begin();
			if (game.thisLevel().variation[game.movenr].comment==null) setMessage("Startposition");
		}
		if (label.equals( Const.END_BUTTON) || label.equals(MENU_MOVE_END)) 
		{
			game.end();
			if (game.thisLevel().variation[game.movenr].comment==null) setMessage("End of game");
		}
		if (label.equals( Const.LEAV_BUTTON) || label.equals(MENU_MOVE_LEAVEVAR)) 
		{
			game.leaveVariation();
			setVariation(game.thisMove());
		}
		if (label.equals( Const.NEWV_BUTTON) || label.equals(MENU_MOVE_NEWVAR)) 
		{
			if (game.newVariation())
			{
				setMessage("New variation created. Enter moves. (Leave when done)");
			}
			else
			{
				setMessage("Please enter a move first to make a variation upon.");
			}
		}
		if (label.equals(MENU_FILE_CONNECT)) 
		{
			connect();
			setMessage("Connected to server");
		}
		if (label.equals(MENU_FILE_DISCONN)) 
		{
			disconnect();
			setMessage("Disconnected from server");
		}
		if (label.equals(MENU_FILE_NEW))
		{
			MyDialog md=new MyDialog(this,"Are you sure you want to start a new game ?",MyDialog.YESNO,MyDialog.NEW_GAME);
			md.show();
		}
		if (label.equals(MENU_FILE_PSN))
		{
			chooseFile fd=new chooseFile(this,"Open game file","psn/","index");
			fd.show();

			String file=fd.getFile();
			if (file==null) return super.action(e,arg);

			PSN_format=true;
			readPSNFile("psn/"+file);

			select_frame.showGames();
		}
		if (label.equals(MENU_FILE_OPEN))
		{
			chooseFile fd=new chooseFile(this,"Open game file","txtgames/","index");
			fd.show();

			String file=fd.getFile();
			if (file==null) return super.action(e,arg);

			PSN_format=false;
			readPSNFile("txtgames/"+file);

			select_frame.showGames();
		}
		if (label.equals(MENU_VIEW_EDIT)) 
		{
			if (gametype.equals("Tsume") || gametype.equals("Hishi") || gametype.equals("Position"))
			{
				menuviewdone.enable();
				menuviewedit.disable();
				set_position=true;
				setMessage("Editing board and select 'Done editing'.");
			}
			else
			{
				MyDialog md=new MyDialog(this,"You can only edit Tsume, Hishi or generic Positions !",MyDialog.OK,MyDialog.NOTHING);
				md.show();
			}
		}
		if (label.equals(MENU_VIEW_DONE)) 
		{
			menuviewdone.disable();
			menuviewedit.enable();
			checkPieces();
			drawPiecesInHand(gg);
			set_position=false;
			setMessage("Board editing complete");
		}
		if (label.equals(MENU_VIEW_TYPE)) 
		{
			Checkbox cb_now=gt_frame.cbg.getCurrent();
			gt_frame.show();
			if (!gt_frame.result)
			{
				gt_frame.cbg.setCurrent(cb_now);
			}
			else
			{
				cb_now=gt_frame.cbg.getCurrent();
				gametype=cb_now.getLabel();
				if (gametype.equals(oldtype)) return true;
				if (gametype.equals("Edit position"))
				{
					set_position=true;
					menuviewdone.enable();
					gametype=oldtype;
					return true;
				}

				newGame();
				draw_moves=true;
				repaint();
			}
		}
		if (label.equals(MENU_FILE_CALC)) 
		{
			String buffer=new String();
			int size;
			long ev=0;
			int d;

			gen.moveptr=0;
			gen.positions=0;
			gen.getKings();
			setMessage("Starting calculations ...");
			draw_moves=false;
			Date start=new Date();
			d=1;
			// while (ev<50000 && d<=search_depth)
			{
				ev=gen.calc(-200000, 200000,search_depth);
				//d+=2;
			}
			Date stop=new Date();
			draw_moves=true;
			setMessage("Best path found ("+d+"p) : ["+ev+"] pos="+gen.positions+" secs="+(float)((stop.getTime()-start.getTime())/1000.0)+" "+gen.printBestPath(new String()));
			System.out.println("Best path found ("+d+"p) : ["+ev+"] pos="+gen.positions+" secs="+(float)((stop.getTime()-start.getTime())/1000.0)+" "+gen.printBestPath(new String()));

			if (gen.best_path[0][0].piece==0) return super.action(e,arg);

			game.root.variation[game.movenr].copy( gen.best_path[0][0] );
			doMove( game.root.variation[game.movenr] );
			setComment( game.root.variation[game.movenr] );
			game.movenr++;
			game.root.variation[game.movenr].clearMove();

			drawPiecesInHand(gg);
			drawCircles(gg);
			drawLastMove(gg);

			side = side^1; drawSide(gg);
		}
		if (label.equals(MENU_FILE_SEND)) 
		{
			if (silent_save)
			{
				sendGame();
			}
			else
			{
				MyDialog md=new MyDialog(this,"You are about to send a game to the server !",MyDialog.OKCANCEL,MyDialog.SEND_GAME);
				md.show();
			}
		}
		if (label.equals(Const.AUTO_START) || label.equals(MENU_MOVE_ASTART)) 
		{
			StopAuto();
			StartAuto();
		}
		if (label.equals(Const.AUTO_STOP) || label.equals(MENU_MOVE_ASTOP)) 
		{
			StopAuto();
		}
		if (label.equals(MENU_VIEW_PREVGAME)) 
		{
			prevGame();
		}
		if (label.equals(MENU_VIEW_NEXTGAME)) 
		{
			nextGame();
		}
		if (e.target==cmenuviewie) /* label.equals(MENU_VIEW_IE      ))  */
		{
			if (!explorer)
			{
				explorer=true;
				MX+=10;
				MY+=90;

				if (!black_white)
				{
					IHYB+=90;
					IHYW+=90;
				}
			}
			else
			{
				explorer=false;
				MX-=10;
				MY-=90;

				if (!black_white)
				{
					IHYB-=90;
					IHYW-=90;
				}
			}
			repaint();
		}
		if (e.target==cmenuviewnames) /*label.equals(MENU_VIEW_NAMES)) */
		{
			shownames=!shownames;
			repaint();
		}
		if (label.startsWith("delay"))
		{
			sleep=Integer.parseInt( label.substring(6,11).trim() );
			setMessage("AutoReplay delay between moves changed to "+sleep+" ms");
		}
		if (label.equals(MENU_WINDOW_DATA))
		{	
			if (!data_frame.isShowing()) data_frame.show(); else data_frame.hide();
		}
		if (label.equals(MENU_WINDOW_SELECT))
		{	
			if (!select_frame.isShowing()) select_frame.show(); else select_frame.hide();
		}
		if (label.equals(MENU_WINDOW_CHOICE))
		{	
			if (!choice_frame.isShowing()) choice_frame.show(); else choice_frame.hide();
		}
		if (label.equals(MENU_WINDOW_COMMENT))
		{	
			if (!comm_frame.isShowing()) comm_frame.show(); else comm_frame.hide();
		}
		if (label.equals(MENU_WINDOW_DECODE))
		{	
			if (!dec_frame.isShowing()) dec_frame.show(); else dec_frame.hide();
		}
		if (label.equals(MENU_WINDOW_VARIA))
		{	
			if (!var_frame.isShowing()) var_frame.show(); else var_frame.hide();
		}
		if (label.equals(MENU_WINDOW_SEARCH))
		{	
			if (!search_frame.isShowing()) search_frame.show(); else search_frame.hide();
		}
		if (e.target == cmenuviewbw)
		{
			System.out.println("Black and white !");
			if (!black_white) loadBoard(true, false);
			cmenuviewcolor.setState(false);
			cmenuviewbw.setState(true);
		}
		if (e.target == cmenuviewcolor)
		{
			System.out.println("Color !");
			if (black_white) loadBoard(false, false);
			cmenuviewcolor.setState(true);
			cmenuviewbw.setState(false);
		}
		if (label.equals(MENU_VIEW_FLIP))
		{	
			flipped=!flipped;
			last_moved=80-last_moved;
			repaint();
		}
		if (label.equals(MENU_WINDOW_CLOSE))
		{	
			data_frame.hide();
			select_frame.hide();
			comm_frame.hide();
			dec_frame.hide();
			var_frame.hide();
			search_frame.hide();
			help.hide();
		}
		if (label.equals(MENU_HELP_GENERAL))
		{	
			if (!help.isShowing()) help.show(); else help.hide();
		}
		if (label.equals(MENU_HELP_ABOUT))
		{	
			MyDialog md=new MyDialog(this,"Gnu Shogi Database version "+Const.VERSION+",  15 Mar 1998",MyDialog.OK,MyDialog.NOTHING);
			md.show();
		}
		if (label.equals(MENU_FILE_QUIT))
		{	
			MyDialog md=new MyDialog(this,"Are you sure you want to quit GSDB ?",MyDialog.YESNO,MyDialog.QUIT);
			md.show();
		}
/* Printing only in JDK 1.1 --> not wanted ! 
		if (label.equals(MENU_FILE_PRINT   ))
		{
			printBitmap();
		}
 */

		if (label.equals(MENU_FILE_BITMAP   ))
		{
			sendBitmap();
		}

		String KSEP="\u0003";

		if (label.equals(MENU_SEARCH_COLMAR )) client.searchGame("tournament"+KSEP+"*Colmar*");
		if (label.equals(MENU_SEARCH_DENHAAG)) client.searchGame("tournament"+KSEP+"*Den Haag*");
		if (label.equals(MENU_SEARCH_EURO   )) client.searchGame("tournament"+KSEP+"*Euro*");
		if (label.equals(MENU_SEARCH_FIRST  )) client.searchGame("tournament"+KSEP+"First*");
		if (label.equals(MENU_SEARCH_GERMAN )) client.searchGame("tournament"+KSEP+"*German*");
		if (label.equals(MENU_SEARCH_GINGA  )) client.searchGame("tournament"+KSEP+"*inga*");
		if (label.equals(MENU_SEARCH_JUNISEN)) client.searchGame("tournament"+KSEP+"*unisen*");
		if (label.equals(MENU_SEARCH_BELGIAN)) client.searchGame("tournament"+KSEP+"*Belgian*");
		if (label.equals(MENU_SEARCH_KIO    )) client.searchGame("tournament"+KSEP+"*Kio*");
		if (label.equals(MENU_SEARCH_KISEI  )) client.searchGame("tournament"+KSEP+"*Kisei*");
		if (label.equals(MENU_SEARCH_MEIJIN )) client.searchGame("tournament"+KSEP+"*Meijin*");
		if (label.equals(MENU_SEARCH_MEMOR  )) client.searchGame("tournament"+KSEP+"*Verkouille*");
		if (label.equals(MENU_SEARCH_NAKA   )) client.searchGame("tournament"+KSEP+"*Nakabisha*");
		if (label.equals(MENU_SEARCH_NIHONPR)) client.searchGame("tournament"+KSEP+"*Zen?Nihon*");
		if (label.equals(MENU_SEARCH_NIJMEG )) client.searchGame("tournament"+KSEP+"*Nijmegen*");
		if (label.equals(MENU_SEARCH_NK     )) client.searchGame("tournament"+KSEP+"*Dutch*");
		if (label.equals(MENU_SEARCH_OI     )) client.searchGame("tournament"+KSEP+"*Oi*");
		if (label.equals(MENU_SEARCH_OZA    )) client.searchGame("tournament"+KSEP+"*Oza*");
		if (label.equals(MENU_SEARCH_RIKAI  )) client.searchGame("tournament"+KSEP+"*Rikai*");
		if (label.equals(MENU_SEARCH_RYUO   )) client.searchGame("tournament"+KSEP+"*Ryu?O*");
		if (label.equals(MENU_SEARCH_TSUME  )) client.searchGame("gametype"+KSEP+"Tsume");
		if (label.equals(MENU_SEARCH_AMA    )) client.searchGame("proam"+KSEP+"Amateur");
		if (label.equals(MENU_SEARCH_PRO    )) client.searchGame("proam"+KSEP+"Professional");
		if (label.equals(MENU_SEARCH_VARTEST)) client.searchGame("tournament"+KSEP+"4th file *");
		if (label.equals(MENU_SEARCH_1992   )) client.searchGame("date"+KSEP+"1992*");
		if (label.equals(MENU_SEARCH_1993   )) client.searchGame("date"+KSEP+"1993*");
		if (label.equals(MENU_SEARCH_1994   )) client.searchGame("date"+KSEP+"1994*");
		if (label.equals(MENU_SEARCH_1995   )) client.searchGame("date"+KSEP+"1995*");
		if (label.equals(MENU_SEARCH_1996   )) client.searchGame("date"+KSEP+"1996*");
		if (label.equals(MENU_SEARCH_1997   )) client.searchGame("date"+KSEP+"1997*");
		if (label.equals(MENU_SEARCH_1998   )) client.searchGame("date"+KSEP+"1998*");
		if (label.equals(MENU_SEARCH_NEW_D  )) client.searchGame("updated"+KSEP+today());
		if (label.equals(MENU_SEARCH_NEW_M  )) client.searchGame("updated"+KSEP+today().substring(0,6)+"??");
		if (label.equals(MENU_SEARCH_NEW_Y  )) client.searchGame("updated"+KSEP+today().substring(0,4)+"????");

		return super.action(e,arg);
	}

	public String today()
	{
		Date d=new Date();
		int day=d.getDate();
		int month=d.getMonth()+1;
		int year=1900+d.getYear();

		return(""+year+(month<10?"0":"")+month+(day<10?"0":"")+day);
	}

	public void checkPieces()
	{
		// Here we check the number of pieces in hand. 
		// We assume that on the board and in blacks hand everything is OK.

		//PAWN
		int pawns=numPieces(Const.BLACK_PAWN)+numPieces(Const.WHITE_PAWN)+numPieces(Const.BLACK_PPAWN)+numPieces(Const.WHITE_PPAWN)+pieces_in_hand[Const.BLACK][Const.PAWN];
		pieces_in_hand[Const.WHITE][Const.PAWN]=(pawns>18)?0:(18-pawns);
		//LANCE
		int lances=numPieces(Const.BLACK_LANCE)+numPieces(Const.WHITE_LANCE)+numPieces(Const.BLACK_PLANCE)+numPieces(Const.WHITE_PLANCE)+pieces_in_hand[Const.BLACK][Const.LANCE];
		pieces_in_hand[Const.WHITE][Const.LANCE]=(lances>4)?0:(4-lances);
		//KNIGHT
		int knights=numPieces(Const.BLACK_KNIGHT)+numPieces(Const.WHITE_KNIGHT)+numPieces(Const.BLACK_PKNIGHT)+numPieces(Const.WHITE_PKNIGHT)+pieces_in_hand[Const.BLACK][Const.KNIGHT];
		pieces_in_hand[Const.WHITE][Const.KNIGHT]=(knights>4)?0:(4-knights);
		//SILVER
		int silvers=numPieces(Const.BLACK_SILVER)+numPieces(Const.WHITE_SILVER)+numPieces(Const.BLACK_PSILVER)+numPieces(Const.WHITE_PSILVER)+pieces_in_hand[Const.BLACK][Const.SILVER];
		pieces_in_hand[Const.WHITE][Const.SILVER]=(silvers>4)?0:(4-silvers);
		//GOLD
		int golds=numPieces(Const.BLACK_GOLD)+numPieces(Const.WHITE_GOLD)+pieces_in_hand[Const.BLACK][Const.GOLD];
		pieces_in_hand[Const.WHITE][Const.GOLD]=(golds>4)?0:(4-golds);
		//BISHOP
		int bishops=numPieces(Const.BLACK_BISHOP)+numPieces(Const.WHITE_BISHOP)+numPieces(Const.BLACK_PBISHOP)+numPieces(Const.WHITE_PBISHOP)+pieces_in_hand[Const.BLACK][Const.BISHOP];
		pieces_in_hand[Const.WHITE][Const.BISHOP]=(bishops>2)?0:(2-bishops);
		//ROOK
		int rooks=numPieces(Const.BLACK_ROOK)+numPieces(Const.WHITE_ROOK)+numPieces(Const.BLACK_PROOK)+numPieces(Const.WHITE_PROOK)+pieces_in_hand[Const.BLACK][Const.ROOK];
		pieces_in_hand[Const.WHITE][Const.ROOK]=(rooks>2)?0:(2-rooks);
	}

	public int numPieces(int piece)
	{
		int retval=0;
		for (int j=0;j<81;j++) if (board[j]==piece) retval++;
		return retval;
	}

	public void copyInfo(Game g)
	{
		g.name        .setLength(0); g.email       .setLength(0); g.country     .setLength(0);
		g.black_name  .setLength(0); g.white_name  .setLength(0); g.black_grade .setLength(0);
		g.white_grade .setLength(0); g.black_elo   .setLength(0); g.white_elo   .setLength(0);
		g.result      .setLength(0); g.comment     .setLength(0); g.source      .setLength(0);
		g.tournament  .setLength(0); g.date        .setLength(0); g.round       .setLength(0);
		g.venue       .setLength(0); g.proam       .setLength(0); g.gametype    .setLength(0);

		g.name        .append( w_name.getText() );
		g.email       .append( w_email.getText() );
		g.country     .append( w_country.getText() );
		g.black_name  .append( w_black_player.getText() );
		g.white_name  .append( w_white_player.getText() );
		g.black_grade .append( w_black_grade.getSelectedItem() );
		g.white_grade .append( w_white_grade.getSelectedItem() );
		g.black_elo   .append( w_black_elo.getText() );
		g.white_elo   .append( w_white_elo.getText() );
		g.result      .append( w_result.getText() );
		g.comment     .append( w_comment.getText() );
		g.source      .append( w_source.getText() );
		g.tournament  .append( w_tournament.getText() );
		//g.date        .append( w_date.getText() );
		g.date.append( data_frame.century.getSelectedItem()+data_frame.year.getSelectedItem()+data_frame.month.getSelectedItem()+data_frame.day.getSelectedItem() );
		g.round       .append( w_round.getText() );
		g.venue       .append( w_venue.getText() );
		g.proam       .append( w_proam.getSelectedItem() );
		g.gametype    .append( gametype );

		g.name        = new StringBuffer( g.name        .toString().trim());
		g.email       = new StringBuffer( g.email       .toString().trim());
		g.country     = new StringBuffer( g.country     .toString().trim());
		g.black_name  = new StringBuffer( g.black_name  .toString().trim());
		g.white_name  = new StringBuffer( g.white_name  .toString().trim());
		g.black_grade = new StringBuffer( g.black_grade .toString().trim());
		g.white_grade = new StringBuffer( g.white_grade .toString().trim());
		g.black_elo   = new StringBuffer( g.black_elo   .toString().trim());
		g.white_elo   = new StringBuffer( g.white_elo   .toString().trim());
		g.result      = new StringBuffer( g.result      .toString().trim());
		g.comment     = new StringBuffer( g.comment     .toString().trim());
		g.source      = new StringBuffer( g.source      .toString().trim());
		g.tournament  = new StringBuffer( g.tournament  .toString().trim());
		g.date        = new StringBuffer( g.date        .toString().trim());
		g.round       = new StringBuffer( g.round       .toString().trim());
		g.venue       = new StringBuffer( g.venue       .toString().trim());
		g.proam       = new StringBuffer( g.proam       .toString().trim());
		g.gametype    = new StringBuffer( g.gametype    .toString().trim());
	}

	public void sendGame()
	{
		if (w_name.getText().trim().length()==0)
		{
			MyDialog md=new MyDialog(this,"Please fill in your name in the data-window !",MyDialog.OK,MyDialog.NOTHING,true);
			md.show();
			return;
		}
		if (w_black_player.getText().trim().length()==0 || w_white_player.getText().trim().length()==0)
		{
			MyDialog md=new MyDialog(this,"Please fill in the names of the players !",MyDialog.OK,MyDialog.NOTHING,true);
			md.show();
			return;
		}
		if (data_frame.day.getSelectedItem().trim().length()==0 || data_frame.month.getSelectedItem().trim().length()==0 || data_frame.year.getSelectedItem().trim().length()==0)
		{
			MyDialog md=new MyDialog(this,"Please fill in the date of gameplay !",MyDialog.OK,MyDialog.NOTHING,true);
			md.show();
			return;
		}
		if (w_country.getText().trim().length()==0)
		{
			MyDialog md=new MyDialog(this,"Please fill in the country where the game was played !",MyDialog.OK,MyDialog.NOTHING,true);
			md.show();
			return;
		}
		copyInfo(game);
		
		client.sendGame(game);
	}

	public void StartAuto() 
	{
		autostop.enable();
		if (runner==null)
		{
			runner=new Thread(this);
			runner.start();
		}
	}

	public void StopAuto()
	{
		autostop.disable();
		if (runner!=null)
		{
			runner.stop();
			runner=null;
		}
	}

	public void run()
	{
		while (true)
		{
			if (game.thisLevel().variation[game.movenr].piece != 0)
			{
				doMove(game.thisLevel().variation[game.movenr]);
				drawLastMove(gg);
				setComment( game.thisLevel().variation[game.movenr+1] );
				game.movenr++;
				side = side^1;
				drawSide(gg);
			}
			else
			{
				StopAuto();
			}
			if (game.thisLevel().variation[game.movenr].comment==null) setMessage(" ");

			try { Thread.sleep(sleep); }
			catch( InterruptedException e) { }
		}
	}
}

