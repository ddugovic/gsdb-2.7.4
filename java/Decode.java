//
// GNU SHOGI DATABASE PROJECT
// GN                      CT
// GN       G.S.D.B.       CT
// GN                      CT
// GNU SHOGI DATABASE PROJECT
//
// Matt Casters : 24/11/1996, first attempt
// Matt Casters : 25/11/1996, added promotion
// Matt Casters : 26/11/1996, added promotion question, some checks, back/forward
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
import java.util.StringTokenizer;

////////////////////////////////////////////////////////////////
//////         Decode class                             ////////
////////////////////////////////////////////////////////////////

public class Decode extends Frame
{
	public Gsdb gsdb;
	public TextArea game;
	public Button clear;
	public Button hide;
	public Panel south;
	public Color col;
	public Font fnt;

	public boolean Stop;

	public String dummy;

	public StringBuffer sbuf;

	public MenuBar menubar;
	public Menu menu;

	Menu menufile;
	Menu menudecode, menudecodetype;
	Menu menuencode, menuencodetype;

	CheckboxMenuItem cmi_decpsn ;
	CheckboxMenuItem cmi_deckifu;
	CheckboxMenuItem cmi_decjava;
	CheckboxMenuItem cmi_encpsn ;
	CheckboxMenuItem cmi_encesn ;

        public String MENU_FILE          = new String("File");
        public String MENU_FILE_CLOSE    = new String("Close");

        public String MENU_DECODE        = new String("Decode");
        public String MENU_DECODE_START  = new String("Start decoding");
        public String MENU_DECODE_TYPE   = new String("Decoding type...");
        public String MENU_DECODE_PSN    = new String("PSN/ESN (normal game score)");
        public String MENU_DECODE_KIFU   = new String("Kifu (Reijer Grimbergen's Kifu format)");
        public String MENU_DECODE_JAVA   = new String("JavaShogi (From JavaShogi history window)");

        public String MENU_ENCODE        = new String("Encode");
        public String MENU_ENCODE_START  = new String("Start encoding");
        public String MENU_ENCODE_TYPE   = new String("Encoding type...");
        public String MENU_ENCODE_PSN    = new String("PSN (Portable Shogi Notation)");
        public String MENU_ENCODE_ESN    = new String("ESN (Enhanced Shogi Notation)");

	public Decode(Gsdb g)
	{
		super("Decoding window");

		gsdb=g;

		fnt=new Font("Helvetica",Font.BOLD, 14);
		col=Const.COLOR_BACKGR;
		setLayout(new BorderLayout());
		setBackground(col);

		menubar=new MenuBar(); setMenuBar(menubar);
		menufile=new Menu(MENU_FILE); menubar.add(menufile);
			menufile.add(new MenuItem( MENU_FILE_CLOSE     ));
		menudecode=new Menu(MENU_DECODE); menubar.add(menudecode);
			menudecode.add(new MenuItem( MENU_DECODE_START  ));
			menudecodetype=new Menu( MENU_DECODE_TYPE   );
			menudecodetype.add(cmi_decpsn =new CheckboxMenuItem( MENU_DECODE_PSN    ));
			menudecodetype.add(cmi_deckifu=new CheckboxMenuItem( MENU_DECODE_KIFU   ));
			menudecodetype.add(cmi_decjava=new CheckboxMenuItem( MENU_DECODE_JAVA   ));
			menudecode.add(menudecodetype);

		menuencode=new Menu(MENU_ENCODE); menubar.add(menuencode);
			menuencode.add(new MenuItem( MENU_ENCODE_START  ));
			menuencodetype=new Menu( MENU_ENCODE_TYPE   );
			menuencodetype.add(cmi_encpsn =new CheckboxMenuItem( MENU_ENCODE_PSN    ));
			menuencodetype.add(cmi_encesn =new CheckboxMenuItem( MENU_ENCODE_ESN    ));
			menuencode.add(menuencodetype);

		game = new TextArea(20,80);
		game.setFont(new Font("Courier", Font.BOLD, 12));
		clear = new Button("Clear");
		clear.setBackground(Const.COLOR_CLEAR);
		clear.setFont(fnt);
		hide = new Button("Hide window");
		hide.setBackground(Const.COLOR_HIDE);
		hide.setFont(fnt);

		south=new Panel();
		south.setLayout(new FlowLayout());
		south.add(clear);
		south.add(hide);

                add("North",game);
		add("South",south);

		pack();

		dummy=new String();
		sbuf=new StringBuffer();
		setEncodePSN();
		setDecodePSN();
	}

	public void setEncodePSN()
	{
		cmi_encpsn.setState(true);
		cmi_encesn.setState(false);
	}

	public void setEncodeESN()
	{
		cmi_encpsn.setState(false);
		cmi_encesn.setState(true);
	}

	public void setDecodePSN()
	{
		cmi_decpsn .setState(true);
		cmi_deckifu.setState(false);
		cmi_decjava.setState(false);
	}

	public void setDecodeKifu()
	{
		cmi_decpsn .setState(false);
		cmi_deckifu.setState(true );
		cmi_decjava.setState(false);
	}

	public void setDecodeJava()
	{
		cmi_decpsn .setState(false);
		cmi_deckifu.setState(false);
		cmi_decjava.setState(true );
	}

	public boolean action(Event e, Object arg)
        {
		String label=null;
		try { label=(String)arg; } catch(ClassCastException cce) {}
		if (label==null) label="Dummy for argument";

		if (e.target==hide || label.equals(MENU_FILE_CLOSE))
		{
			hideWindow();
		}
		if (e.target==clear)
		{
			game.setText("");
		}
		if (label.equals(MENU_ENCODE_PSN))
		{
			setEncodePSN();
		}
		if (label.equals(MENU_ENCODE_ESN))
		{
			setEncodeESN();
		}
		if (label.equals(MENU_DECODE_PSN))
		{
			setDecodePSN();
		}
		if (label.equals(MENU_DECODE_KIFU))
		{
			setDecodeKifu();
		}
		if (label.equals(MENU_DECODE_JAVA))
		{
			setDecodeJava();
		}

		if (label.equals(MENU_DECODE_START))
		{
			MyDialog md=new MyDialog(gsdb,"Decoding text will destroy your current game !",MyDialog.OKCANCEL,MyDialog.DECODE);
			md.show();
		}
		if (label.equals(MENU_ENCODE_START))
		{
			gsdb.encodeGame(true);
		}
		return true;
	}

	public void decodeNow()
	{
		boolean dm=gsdb.draw_moves;
		gsdb.draw_moves=false;
		hide();
		gsdb.newGame();
		decodeGame(game.getText());
		gsdb.game.begin();
		gsdb.draw_moves=dm;
		gsdb.repaint();
	}

	public void hideWindow()
	{
		hide();
	}

	public void decodeGame(String b)
	{
		int i,j;
		int max;

		//System.out.println("Decoding game ["+b+"]");

		StringBuffer mve=new StringBuffer();

		if (b.indexOf("1-0")!=-1) gsdb.w_result.setText("1-0");
		if (b.indexOf("0-1")!=-1) gsdb.w_result.setText("0-1"); 
		if (b.indexOf("Sennichite")!=-1) gsdb.w_result.setText("1/2-1/2"); 

		int f,t;
		int f2,t2;

		if (cmi_decjava.getState()) // Java shogi history format
		{
			StreamAnalyser sa=new StreamAnalyser(b);
			String blackp, whitep;
			String num, date, yy, mm, dd, spiece, word, sfrom, sto;
			int color, piece, from, to, last;
			boolean promo;
			Move m=new Move();
			char c1,c2;

			StringBuffer game=new StringBuffer();
			game.setLength(0);

			last=0;
			
			sa.getUntil('\n'); // vaShogi version 2.0.3. HistoryFile
			sa.getUntil('\n'); // # Comment
			sa.getUntil('\n'); // #
			sa.getUntil('\n'); // #
			sa.getWord(); // StartDate: 
			date=sa.getWord(); // the date : dd-mmm-yy
			dd=date.substring(0,2);
			yy=date.substring(7);
			mm="01";
			if (date.substring(3,6).equals("Jan")) mm="01";
			if (date.substring(3,6).equals("Feb")) mm="02";
			if (date.substring(3,6).equals("Mar")) mm="03";
			if (date.substring(3,6).equals("Apr")) mm="04";
			if (date.substring(3,6).equals("May")) mm="05";
			if (date.substring(3,6).equals("Jun")) mm="06";
			if (date.substring(3,6).equals("Jul")) mm="07";
			if (date.substring(3,6).equals("Aug")) mm="08";
			if (date.substring(3,6).equals("Sep")) mm="09";
			if (date.substring(3,6).equals("Oct")) mm="10";
			if (date.substring(3,6).equals("Nov")) mm="11";
			if (date.substring(3,6).equals("Dec")) mm="12";
			
			gsdb.data_frame.setDate("19"+yy+mm+dd);
			game.append("[Date \"19"+yy+"/"+mm+"/"+dd+"\"]\n");

			sa.getUntil('\n'); // StartDate end of line
			sa.getUntil('\n'); // Stop: ....
			sa.getUntil('\n'); // TheaiWari: Hirate

			sa.getWord(); // Word BLACK:
			gsdb.w_black_player.setText( blackp=sa.getUntil('\n') ); // Black player
			sa.getWord(); // Word WHITE:
			gsdb.w_white_player.setText( whitep=sa.getUntil('\n') ); // White player

			game.append("[Black \""+blackp+"\"]\n");
			game.append("[White \""+whitep+"\"]\n");
			game.append("[Country \"Japan\"]\n");
			game.append("[Comment \"Played on Ryo Neyama's Java Shogi Server\"]\n");

			sa.getUntil('\n'); // Count----History---------Spent Time--

			// Here comes the game !
			//
			to=0;
			f=0;

			while (!sa.End())
			{
				f++;
				num=sa.getNumber(); // The movenumber : skip it.

				c1='1'; c2='1';
				if (!sa.End()) c1=sa.nextChar();
				if (!sa.End()) c2=sa.nextChar();
				if (sa.End()) break;

				sto=""+c1+c2; // Pos or Do (== same)
				spiece=sa.getUntil('(');
				if ( (""+c1+c2+spiece).equals("Resign") )
				{
					if ((Integer.parseInt( num )%2)==0)
					{
						game.append(" 1-0");
					}
					else
					{
						game.append(" 0-1");
					}
					break;
				}
				if (spiece.endsWith("Utsu"))
				{
					sfrom="DROP";
					spiece=spiece.substring(0,spiece.indexOf("Utsu"));
				}
				else
				{
					sfrom=sa.getUntil(')');  // between brackets.
				}
				if (!sa.End()) sa.nextChar();

				sa.getUntil('\n'); // skip ( 0:00/00:00:00)

				if (sa.End()) break;
				if (sfrom.equals("DROP"))
				{
					from=Const.DROP;
				}
				else
				{
					from=((int)'9'-(int)sfrom.charAt(0))+((int)sfrom.charAt(1)-(int)'1')*9;
				}

				if (!sto.equals("Do"))
				{
					to=((int)'9'-(int)sto.charAt(0))+((int)sto.charAt(1)-(int)'1')*9;
				}
				else
				{
					to=last;
				}
				last=to;

				piece=Const.EMPTY;
				promo=false;
				if (spiece.equals("Fu"       )) { piece=Const.PAWN; }
				if (spiece.equals("To"       )) { piece=Const.PPAWN; }
				if (spiece.equals("FuNaru"   )) { piece=Const.PAWN; promo=true; }
				if (spiece.equals("Kyo"      )) { piece=Const.LANCE; }
				if (spiece.equals("NariKyo"  )) { piece=Const.PLANCE; }
				if (spiece.equals("KyoNaru"  )) { piece=Const.LANCE; promo=true; }
				if (spiece.equals("Kei"      )) { piece=Const.KNIGHT; }
				if (spiece.equals("NariKei"  )) { piece=Const.PKNIGHT; }
				if (spiece.equals("KeiNaru"  )) { piece=Const.KNIGHT; promo=true; }
				if (spiece.equals("Gin"      )) { piece=Const.SILVER; }
				if (spiece.equals("NariGin"  )) { piece=Const.PSILVER; }
				if (spiece.equals("GinNaru"  )) { piece=Const.SILVER; promo=true; }
				if (spiece.equals("Kin"      )) { piece=Const.GOLD; }
				if (spiece.equals("Kaku"     )) { piece=Const.BISHOP; }
				if (spiece.equals("Uma"      )) { piece=Const.PBISHOP; }
				if (spiece.equals("KakuNaru" )) { piece=Const.BISHOP; promo=true; }
				if (spiece.equals("Hi"       )) { piece=Const.ROOK; }
				if (spiece.equals("Ryu"      )) { piece=Const.PROOK; }
				if (spiece.equals("HiNaru"   )) { piece=Const.ROOK; promo=true; }
				if (spiece.equals("Gyoku"    )) { piece=Const.KING; }

				//System.out.print("move : from="+sfrom+"("+from+") piece="+spiece+"("+piece+") to="+sto+"("+to+")");

				m.piece   =piece;
				m.from    =from;
				m.to      =to;
				m.promotes=promo;
				m.taken   =Const.EMPTY;

				//System.out.println(m.ASCII_move(dummy)+" ");
				if (f%6==0) game.append("\n");
				game.append(m.ASCII_move(dummy)+" ");
			}
			cmi_decjava.setState(false);
			cmi_decpsn.setState(true);
			System.out.println("Decoding game : ");
			System.out.println(game.toString());
			decodeGame(game.toString());
			cmi_decjava.setState(true);
			cmi_decpsn.setState(false);
			return;
		}
		if (cmi_deckifu.getState())
		{
			/*
			 The kifu format comes from Reijer Grimbergens Kifu program :
			
			   Black: Habu Yoshiharu, Oi
			   White: Sato Yasumitsu, Challenger
			   38th Oi-sen, Game 4, August 18th and 19th 1997
			   1.P7g-7f           1/1          0/0
			   2.P8c-8d           0/1          4/4
			  
			   An interesting choice by challenger Sato. He is probably an expert on the
			   Yagura opening and has written several good books on the opening. However,
			   lately the results by white in the Yagura have not been very good.
			  
			   3.S7i-6h           4/5          0/4
			   4.P3c-3d           0/5          1/5
			   etc...


			Problem 1  (1/9/1997)
			---------

			If we use a StringTokenizer on this, we get a list of Strings with all the lines
			seperated. Comments are allways seperated from the game-score by blank lines.
			StringTokenizer has a disadvantage : P7f\n\nComment does not seperate into
			"P7f", "" and "Comment" but into "P7f" and "Comment".  It ignores double seperators.
			That's why we first need to find all \n\n and insert a space behind the first \n

			Problem 2 (7/10/1997)
			---------

			If we copy games from Reijers home-page, somethimes we take multiple blank
			lines. (some kind of Netscape copy feature, who knows)
			Now, we should replace multiple blank lines (>\n\n\n) into a single blank line !

			*/

			StringBuffer kifbuf=new StringBuffer(b);
			for (j=0;j<kifbuf.length();j++)
			if (kifbuf.charAt(j)=='\r') // remove all /r remainders from MS-DOS text files
			{
				String two=kifbuf.toString().substring(j+1);
				kifbuf.setLength(j);
				kifbuf.append(two);
			}

			// 
			// Take care of Problem 2        \n\n\n\n\n\n ---> \n\n
			//

			String blanks=kifbuf.toString();
			StringBuffer noblanks=new StringBuffer("");
			noblanks.setLength(0);
			int counter=0;
			for (j=0;j<blanks.length();j++)
			{
				if (blanks.charAt(j)!='\n') counter=0;
				if (blanks.charAt(j)=='\n') counter++;
				if (counter<3)
				{
					noblanks.append(blanks.charAt(j));
				}
			}

			// System.out.println("After problem 2 :\n"+noblanks.toString());

			for (j=0;j<noblanks.length()-1;j++)
			{
				if (noblanks.charAt(j)=='\n' && noblanks.charAt(j+1)=='\n')
				{
					noblanks.insert(j+1,' ');
				}
			}

			/* Now we can seperate noblanks into seperate Strings */

			StringTokenizer kb=new StringTokenizer(noblanks.toString(),"\n");
			int linenr=1;
			int pos;
			boolean comm=false;
			StringBuffer comment=new StringBuffer("");
			StringBuffer game=new StringBuffer("");
			String black="";
			String white="";
			String tournament="";

			while(kb.hasMoreElements())
			{
				String line=(String)kb.nextElement();
				if (!kb.hasMoreElements() && line.length()<5) break; // ignore last \n 
				if (line.indexOf("Black:")!=-1) 
				{
					black=line.substring(6); 
				}
				else
				if (line.indexOf("White:")!=-1) 
				{
					white=line.substring(6); 
					linenr=2; 
				}
				else
                                if (line.indexOf("Resings")!=-1)
                                {
                                }
				else
				if (line.indexOf("Time:")!=-1)
				{
				}
				else
				if (linenr==3) // The line afther White: contains the tournament information, normaly the third line.
				{
					tournament=line;
				}
				else
				{
                                        if (line.equals(" "))
                                        {
                                                comm=!comm;
                                        }
                                        else
                                        {
                                                if (comm)
                                                {
                                                        comment.append(line+"\n");
                                                }
                                                else
                                                {
                                                        pos=line.indexOf('.');
                                                        if (pos!=-1 && line.charAt(0)>='0' && line.charAt(0)<='9')
                                                        {
                                                                if (comment.length()!=0) game.append(" {"+comment.toString().replace('\n','~')+"} ");
                                                                comment.setLength(0);
                                                                game.append( line.substring( pos+1,pos+10 ) );
                                                        }
                                                        else
                                                        {
                                                                // We seemed to have missed the boat here !
                                                                //
                                                                comm=!comm;
                                                                comment.append(line+"\n");
                                                        }
                                                }
                                        }
				}
				linenr++;
			}
			cmi_deckifu.setState(false);
			gsdb.convertGame(game.toString().replace('?',' ').replace('!',' '));
			cmi_deckifu.setState(true);
			gsdb.w_black_player.setText(black);
			gsdb.w_white_player.setText(white);
			gsdb.w_tournament.setText(tournament);
			gsdb.game.begin();
			gsdb.setMessage("game decoded succesfully !");
			return;
		}

		StreamAnalyser sa=new StreamAnalyser(b);
		String argument2;
		String comm;
		String move;
		String number;
		String variation;
		int io;

		gsdb.w_name.setText("Matt Casters");
		gsdb.w_email.setText("matt@innet.be");
		gsdb.data_frame.setDate("19700101");

		while (!sa.End())
		{
			if (gsdb.game.thisLevel().variation.length-1<=gsdb.game.movenr) 
			        gsdb.game.reAllocateMoves(10);

			String h=sa.getPair('[',']'); // header entries available... ?
			while (h!=null)
			{
				// ...yes !
				StreamAnalyser hsa=new StreamAnalyser(h);
				String he      =hsa.getWord().toUpperCase();
				String argument=hsa.getPair('\"');

				if (he.equals("RESULT"))
				{
					gsdb.w_result.setText(argument);
				}
				else
				if (he.equals("COUNTRY"))
				{
					gsdb.w_country.setText(argument);
				}
				else
				if (he.equals("EVENT") || he.equals("TOURNAMENT"))
				{
					gsdb.w_tournament.setText(argument);
				}
				else
				if (he.equals("PROAM"))
				{
					gsdb.w_proam.select(argument);
					if (argument.equals("Professional"))
					{
						gsdb.w_country.setText("Japan");
					}
				}
				else
				if (he.equals("ROUND"))
				{
					gsdb.w_round.setText(argument);
				}
				else
				if (he.equals("DATE"))
				{
					// The date is rather difficult.
					// Patrick seems to prefer 1997 Februari 8th instead of 19970208
					// Somethimes we also get 1997/02/08 
					// Let's see if we can convert it !
					//
					if (argument.trim().length()==10)
					{
						// format would be something like 1997/02/08
						String year=argument.substring(0,4); // 1997
						String month=argument.substring(5,7);
						String day=argument.substring(8);

						gsdb.data_frame.setDate(year+month+day);
					}
					else
					if (argument.trim().length()!=8)
					{
						String year=argument.substring(0,4); // 1997
						String month=argument.substring(5,argument.indexOf(' ',5));
						String da=argument.substring(argument.lastIndexOf(' ')+1);

						//System.out.println("Date detected : year=["+year+"] month=["+month+"] day=["+da+"]");
						for (i=0;i<da.length();i++) if (da.charAt(i)<'0' || da.charAt(i)>'9') break;
						String day=da.substring(0,i);

						if (month.equals("January"))   month="01"; if (month.equals("February"))  month="02";
						if (month.equals("March"))     month="03"; if (month.equals("April"))     month="04";
						if (month.equals("May"))       month="05"; if (month.equals("June"))      month="06";
						if (month.equals("July"))      month="07"; if (month.equals("August"))    month="08";
						if (month.equals("September")) month="09"; if (month.equals("October"))   month="10";
						if (month.equals("November"))  month="11"; if (month.equals("December"))  month="12";

						gsdb.data_frame.setDate(year+month+(day.length()==0?"01":(day.length()==1?"0":""))+day);
					}
					else
					{
						gsdb.data_frame.setDate(argument);
					}
				}
				else
				if (he.equals("SITE"))
				{
					if (argument.indexOf(",")<0) gsdb.w_venue.setText(argument.trim());
					else
					{
						gsdb.w_venue.setText(argument.substring(0,argument.indexOf(",")).trim());
						gsdb.w_country.setText(argument.substring(argument.indexOf(",")+1).trim());
					}
				}
				else
				if (he.equals("SENTE") || he.equals("Const.BLACK"))
				{
					gsdb.w_black_player.setText(argument);
					io=argument.indexOf(",");
					if (io!=-1)
					{
						gsdb.w_black_player.setText(argument.substring(0,io));
						argument2=argument.substring(io+2);
						gsdb.w_black_grade.select(argument2);
					}
				}
				else
				if (he.equals("GOTE") || he.equals("Const.WHITE"))
				{
					gsdb.w_white_player.setText(argument);
					io=argument.indexOf(",");
					if (io!=-1)
					{
						gsdb.w_white_player.setText(argument.substring(0,io));
						argument2=argument.substring(io+2);
						gsdb.w_white_grade.select(argument2);
					}
				}
				else
				if (he.equals("COMMENT"))
				{
					gsdb.w_comment.setText(argument);
				}
				else
				if (he.equals("OPENING"))
				{
					gsdb.w_comment.setText("Opening: "+argument);
				}
				else
				if (he.equals("RESULT"))
				{
					gsdb.w_result.setText(argument);
				}
				else
				if (he.equals("SOURCE"))
				{
					gsdb.w_source.setText(argument);
				}
				else
				if (he.equals("NAME"))
				{
					gsdb.w_name.setText(argument);
				}
				else
				if (he.equals("EMAIL"))
				{
					gsdb.w_email.setText(argument);
				}
				else
				if (he.equals("BLACK_GRADE"))
				{
					gsdb.w_black_grade.select(argument);
				}
				else
				if (he.equals("WHITE_GRADE"))
				{
					gsdb.w_white_grade.select(argument);
				}
				else
				if (he.equals("VENUE"))
				{
					gsdb.w_venue.setText(argument);
				}

				h=sa.getPair('[',']'); // another header entry available... ?
			}
			if (gsdb.PSN_onlyHeaders) break;

			// 1.P7f P3d 2.P2f P4d 3.G45h R4b 4.S4h ...

			comm     =sa.getPair('{','}'); // let's see if there is a comment available 
			if (comm!=null) gsdb.game.thisMove().comment=comm.replace('~','\n');

			variation=sa.getPair('(',')'); // let's see if there is a variation available 
			if (variation!=null)
			{
				Stop=false;
				if (variation.length()!=1)
				{
					gsdb.side=gsdb.side^1;
					gsdb.undoMove(gsdb.game.prevMove()); // a variation can never be on move 0 !!
					//System.out.println("undoMove("+gsdb.game.prevMove().ASCII_move(dummy)+");"+(gsdb.side==Const.BLACK?"Black":"White"));

					checkVariation(variation, gsdb.game.prevMove());

					//System.out.println("doMove("+gsdb.game.prevMove().ASCII_move(dummy)+");"+(gsdb.side==Const.BLACK?"Black":"White"));
					gsdb.doMove(gsdb.game.prevMove()); // a variation can never be on move 0 !!
					gsdb.side=gsdb.side^1;
				}
				// we store the variation in Move[] : Move.variation;
			}

			number=sa.getNumber(); // usually we get a movenumber of some sort. If not, it's cool because we skip it anyway.
			move=sa.getWord(); // now we get a move like P7f...
			if (move==null) break;
			if (move.indexOf("1-0")!=-1) ;
			if (move.indexOf("0-1")!=-1) ;
			if (!findMove(move,gsdb.game.thisMove()))
			{
				gsdb.setMessage("decode halted : move ["+move+"] not recognised !");
				return;
			}
			else
			{
				gsdb.game.forward();
			}
		}
		gsdb.setMessage("game decoded succesfully !");
	}

	public void checkVariation(String s, Move m)
	{
		//System.out.println("checkVariation("+s+");");
		m.moves=countWords(s);
		m.variation=new Move[m.moves+1];
		for (int i=0;i<m.moves+1;i++) m.variation[i]=new Move();
		int whatside;

		StreamAnalyser sa=new StreamAnalyser(s);

		int count=0;
		while (!sa.End() && !Stop)
		{
			//System.out.println("Run nr. "+count+"/"+m.moves);
			String comm=sa.getPair('{','}'); // a comment
			if (comm!=null)
				m.variation[count].comment=comm.replace('~','\n');
			String var =sa.getPair('(',')'); // another variation
			if (var!=null)
			{
				gsdb.side=gsdb.side^1;
				gsdb.undoMove(m.variation[count-1]);
				//System.out.println("undoMove("+m.variation[count-1].ASCII_move(dummy)+");"+(gsdb.side==Const.BLACK?"Black":"White"));
				whatside=gsdb.side;
				checkVariation(var,m.variation[count-1]);
				gsdb.side=whatside;
				//System.out.println("doMove("+m.variation[count-1].ASCII_move(dummy)+");"+(gsdb.side==Const.BLACK?"Black":"White"));
				gsdb.doMove(m.variation[count-1]);
				gsdb.side=gsdb.side^1;
			}
			String num =sa.getNumber();      // a (move-)number, allways ignore
			String mve =sa.getWord();        // a word
			if (mve!=null)
			{
				//System.out.println("found move : ["+mve+"]");
				if (findMove(mve.toUpperCase(), m.variation[count]))
				{
					//System.out.println("doMove("+mve+");"+(gsdb.side==Const.BLACK?"Black":"White"));
					gsdb.doMove(m.variation[count]);
					gsdb.side=gsdb.side^1;
					count++;
				}
				else
				{
					gsdb.setMessage("Couldn't find move : ["+mve+"]");
					//System.out.println("Couldn't find move : ["+mve+"]");
					Stop=true;
				}
			}
		}
		for (int i=count-1;i>=0;i--) // since it's a variation, we need to hide all these nice moves.
		{
			gsdb.side=gsdb.side^1;
			gsdb.undoMove(m.variation[i]);
			//System.out.println("undoMove("+m.variation[i].ASCII_move(dummy)+");"+(gsdb.side==Const.BLACK?"Black":"White"));
		}
		//System.out.println("End of checkVariations()");
	}

	public int countWords(String s)
	{
		StreamAnalyser sa=new StreamAnalyser(s);
		int count=0;
		while (!sa.End())
		{
			String comm=sa.getPair('{','}'); // a comment          ignore this
			String var =sa.getPair('(',')'); // another variation  ignore this
			String num =sa.getNumber();      // a (move-)number    ignore this
			String word=sa.getWord();        // a word             ignore this
			if (word!=null) count++;
		}
		return count;
	}

	public boolean findMove(String opft, Move mve)
	{
		int i;
		int len;
		char pce;
		int piece;
		int piece_nodrop;
		boolean promoted;
		boolean promotes;
		boolean drop;
		Move m=new Move();
		Move m_nodrop=new Move();
		int hits=0;
		int hits_nodrop=0;
		boolean found=false;
		boolean found_nodrop=false;
		String pft;

		// First move to uppercase characters
		opft=opft.toUpperCase();

		sbuf.setLength(0);

		//
		// Remove all X - = ? and ! from the move !!
		//
		for (i=0;i<opft.length();i++)
		{
			switch (opft.charAt(i))
			{
			case 'X': case '-': case '=': case '?': case '!': break;
			default : sbuf.append(opft.charAt(i)); break;
			}
		}
		pft=sbuf.toString();
	/*
		if (gsdb.side==Const.BLACK)
			System.out.println("Const.BLACK to move, checking move ["+pft+"]");
		else
			System.out.println("Const.WHITE to move, checking move ["+pft+"]");
	*/
	

		i=0;
		len=pft.length();

		if (len<2) return true; // probably is not a move, if it's only 1 byte long

		promotes=pft.charAt(len-1)=='+';
		drop=pft.charAt(1)=='\'' || pft.charAt(1)=='*';

		promoted=(pft.charAt(i)=='+');
		if (promoted) i++; 
		if (i>=len)
		{
			gsdb.setMessage("Something's wrong with move : ["+pft+"]");
			//System.out.println("Something's wrong with move : ["+pft+"]");
			return false;
		}
		pce=pft.charAt(i);
		switch (pce)
		{
		case 'P': if (!promoted && !promotes) piece=Const.PAWN   ; else piece=Const.PPAWN   ; break;
		case 'L': if (!promoted && !promotes) piece=Const.LANCE  ; else piece=Const.PLANCE  ; break;
		case 'N': if (!promoted && !promotes) piece=Const.KNIGHT ; else piece=Const.PKNIGHT ; break;
		case 'S': if (!promoted && !promotes) piece=Const.SILVER ; else piece=Const.PSILVER ; break;
		case 'G': piece=Const.GOLD; break;
		case 'B': if (!promoted && !promotes) piece=Const.BISHOP ; else piece=Const.PBISHOP ; break;
		case 'R': if (!promoted && !promotes) piece=Const.ROOK   ; else piece=Const.PROOK   ; break;
		case 'K': piece=Const.KING; break;
		default: piece=0; gsdb.setMessage("Error in game-string : "+pft.substring(i)); return false;
		}
		//System.out.println("Piece = "+piece);
		piece_nodrop=piece;
		if (!drop) piece=piece+gsdb.side*14;

		int start=1;
		int stop=len;
		if (promoted) start++;
		if (drop    ) start++;
		if (promotes) stop --;

		String ft=pft.substring(start,stop); // From-To
		//System.out.println("ft="+ft);

		gsdb.gen.moveptr=0;
		gsdb.gen.no_checking=true;
		gsdb.gen.generateMoves();
		gsdb.gen.no_checking=false;


		if (ft.length()==2)
		{
			int to=((int)'9' - (int)ft.charAt(0))+((int)ft.charAt(1)-(int)'A')*9;
			//System.out.println("  to="+to);
			hits=0;
			for (i=0;i<gsdb.gen.moveptr;i++)
			{
				//if (gsdb.gen.Piece[i]==1) System.out.println("From[i]="+gsdb.gen.From[i]+" To[i]="+gsdb.gen.To[i]);
				if (piece   ==gsdb.gen.Piece[i])
				if (to      ==gsdb.gen.To[i])
				if (promotes==gsdb.gen.Promotes[i])
				if ((!drop && gsdb.gen.From[i]!=Const.DROP) || (drop && gsdb.gen.From[i]==Const.DROP ))
				{
					hits++;
					found=true;
					m.setMove(gsdb.gen.From[i],gsdb.gen.To[i],gsdb.gen.Piece[i],gsdb.gen.Taken[i],gsdb.gen.Promotes[i]);
				}
				if (piece_nodrop==gsdb.gen.Piece[i]) // just to capture Patrick Davin's notation P2c, when he
				if (to          ==gsdb.gen.To[i])    // really means : P'2c (possible only if it's the only move)
				if (gsdb.gen.Promotes[i]==false) //a drop NEVER promotes.
				{
					hits_nodrop++;
					found_nodrop=true;
					m_nodrop.setMove(gsdb.gen.From[i],gsdb.gen.To[i],gsdb.gen.Piece[i],gsdb.gen.Taken[i],gsdb.gen.Promotes[i]);
				}
			}
			if (hits==0 && hits_nodrop==1)
			{
				found=true;
				hits=1;
				m.copy(m_nodrop);
			}
			if (hits>1)
			{
				gsdb.setMessage("More then 1 possible move for ["+pft+"] !");
				System.out.println("More then 1 possible move for ["+pft+"] !");
				return false;
			}
		}

		if (ft.length()==3)
		{
			// Format : S 56g
			//
			if (    ft.charAt(0)>='1' && ft.charAt(0)<='9'
			     && ft.charAt(1)>='1' && ft.charAt(1)<='9'
			     && ft.charAt(2)>='A' && ft.charAt(2)<='I')
			{
				// Format is probably : 45B
				int col = (int)'9' - (int)ft.charAt(0);
				int to  =((int)'9' - (int)ft.charAt(1))+((int)ft.charAt(2)-(int)'A')*9;
				for (i=0;i<gsdb.gen.moveptr;i++)
				{
					if (piece   ==gsdb.gen.Piece[i])
					if (to      ==gsdb.gen.To[i])
					if (promotes==gsdb.gen.Promotes[i])
					if (col     ==gsdb.gen.From[i]%9)
					{
						found=true;
						m.setMove(gsdb.gen.From[i],gsdb.gen.To[i],gsdb.gen.Piece[i],gsdb.gen.Taken[i],gsdb.gen.Promotes[i]);
						break;
					}
				}
			}
			// Format : G 6IH
			//
			if (    ft.charAt(0)>='1' && ft.charAt(0)<='9'
			     && ft.charAt(1)>='A' && ft.charAt(1)<='I'
			     && ft.charAt(2)>='A' && ft.charAt(2)<='I')
			{
				// Format should be : 6IH
				int row = (int)ft.charAt(1)-(int)'A';
				int to  =((int)'9' - (int)ft.charAt(0))+((int)ft.charAt(2)-(int)'A')*9;
				for (i=0;i<gsdb.gen.moveptr;i++)
				{
					if (piece   ==gsdb.gen.Piece[i])
					if (to      ==gsdb.gen.To[i])
					if (promotes==gsdb.gen.Promotes[i])
					if (row     ==gsdb.gen.From[i]/9)
					{
						found=true;
						m.setMove(gsdb.gen.From[i],gsdb.gen.To[i],gsdb.gen.Piece[i],gsdb.gen.Taken[i],gsdb.gen.Promotes[i]);
						break;
					}
				}
			}
			// Format : G g7h
			//
			if (    ft.charAt(0)>='A' && ft.charAt(0)<='I'
			     && ft.charAt(1)>='1' && ft.charAt(1)<='9'
			     && ft.charAt(2)>='A' && ft.charAt(2)<='I')
			{
				// Format is probably : C5B
				int row = (int)ft.charAt(0) - (int)'A';
				int to  =((int)'9' - (int)ft.charAt(1))+((int)ft.charAt(2)-(int)'A')*9;
				for (i=0;i<gsdb.gen.moveptr;i++)
				{
					if (piece   ==gsdb.gen.Piece[i])
					if (to      ==gsdb.gen.To[i])
					if (promotes==gsdb.gen.Promotes[i])
					if (row     ==gsdb.gen.From[i]/9)
					{
						found=true;
						m.setMove(gsdb.gen.From[i],gsdb.gen.To[i],gsdb.gen.Piece[i],gsdb.gen.Taken[i],gsdb.gen.Promotes[i]);
						break;
					}
				}
			}
		}
		if (ft.length()==4)
		{
			// Format is probably : 4A5B
			int from =((int)'9' - (int)ft.charAt(0))+((int)ft.charAt(1)-(int)'A')*9;
			int to   =((int)'9' - (int)ft.charAt(2))+((int)ft.charAt(3)-(int)'A')*9;
			//System.out.println("piece="+piece+" from="+from+" to="+to+" promotes="+promotes);
			for (i=0;i<gsdb.gen.moveptr;i++)
			{
				if (piece   ==gsdb.gen.Piece[i])
				if (from    ==gsdb.gen.From[i])
				if (to      ==gsdb.gen.To[i])
				if (promotes==gsdb.gen.Promotes[i])
				{
					found=true;
					m.setMove(gsdb.gen.From[i],gsdb.gen.To[i],gsdb.gen.Piece[i],gsdb.gen.Taken[i],gsdb.gen.Promotes[i]);
					break;
				}
			}
		}
		if (found==false)
		{
			gsdb.setMessage("Can't find ["+(gsdb.side==Const.BLACK?"Black":"White")+" ["+pft+"] as a legal move !");
			System.out.println("Can't find ["+(gsdb.side==Const.BLACK?"Black":"White")+" ["+pft+"] as a legal move !");
		}
		else
		{
			mve.copy(m);
		}

		return found;
	}
}





