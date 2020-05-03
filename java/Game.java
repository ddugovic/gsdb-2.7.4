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
import java.util.Vector;

////////////////////////////////////////////////////////////////
//////         Game class                               ////////
////////////////////////////////////////////////////////////////

public class Game
{
	Gsdb gsdb=null;
	public Move root;
	public int movenr;
	public int level;

	public Move pmove=null;
	public Move nmove=null;

	public Move tlevel[];
	public int  tmoves[];

	public int selVariation[];

	public StringBuffer name=new StringBuffer();
	public StringBuffer email=new StringBuffer();
	public StringBuffer country=new StringBuffer();
	public StringBuffer black_name=new StringBuffer();
	public StringBuffer white_name=new StringBuffer();
	public StringBuffer black_grade=new StringBuffer();
	public StringBuffer white_grade=new StringBuffer();
	public StringBuffer black_elo=new StringBuffer();
	public StringBuffer white_elo=new StringBuffer();
	public StringBuffer result=new StringBuffer();
	public StringBuffer comment=new StringBuffer();
	public StringBuffer source=new StringBuffer();
	public StringBuffer tournament=new StringBuffer();
	public StringBuffer date=new StringBuffer();
	public StringBuffer round=new StringBuffer();
	public StringBuffer venue=new StringBuffer();
	public StringBuffer proam=new StringBuffer();
	public StringBuffer gametype=new StringBuffer();

	String buf;

	public Game(Gsdb g)
	{
		gsdb=g;
		level=0;
		buf=new String();
		root=new Move();
		tlevel=new Move[100]; // TODO: replace it with a vector someday !!!!
		tmoves=new int [100]; // TODO: replace it with a vector someday !!!!
		for (int i=0;i<100;i++) tlevel[i]=null;
		for (int i=0;i<100;i++) tmoves[i]=0;
		tlevel[0]=root;
		selVariation=new int[Const.MAX_VARIATIONS];
		for(int i=0;i<Const.MAX_VARIATIONS;i++) selVariation[i]=-1;
	}

	public void allocateMoves(int max)
	{
		thisLevel().variation=new Move[max];
		for(int i=0;i<max;i++) thisLevel().variation[i]=new Move();
	}

	public void reAllocateMoves(int plus)
	{
		int size=thisLevel().variation.length;
		int newsize=size+10;
		Move dummies[]=new Move[newsize];

		for (int i=0;i<size;i++)
		{
			dummies[i]=thisLevel().variation[i];
		}
		for (int i=size;i<newsize;i++)
		{
			dummies[i]=new Move();
		}
		thisLevel().variation=null; // cleanup variation
		thisLevel().variation=dummies;
	}

	public String gameString(StringBuffer s)
	{
		int i;

		s.setLength(0);

		if (gsdb.oldtype.equals("Tsume") || gsdb.oldtype.equals("Position") || gsdb.oldtype.equals("Hishi"))
		{
			begin();

			for (i=0;i<81;i++)
			{
				switch(gsdb.board[i])
				{
					case Const.BLACK_PAWN      : s.append("bP"); break;
					case Const.BLACK_LANCE     : s.append("bL"); break;
					case Const.BLACK_KNIGHT    : s.append("bN"); break;
					case Const.BLACK_SILVER    : s.append("bS"); break;
					case Const.BLACK_GOLD      : s.append("bG"); break;
					case Const.BLACK_BISHOP    : s.append("bB"); break;
					case Const.BLACK_ROOK      : s.append("bR"); break;
					case Const.BLACK_KING      : s.append("bK"); break;
					case Const.BLACK_PPAWN     : s.append("b+P"); break;
					case Const.BLACK_PLANCE    : s.append("b+L"); break;
					case Const.BLACK_PKNIGHT   : s.append("b+N"); break;
					case Const.BLACK_PSILVER   : s.append("b+S"); break;
					case Const.BLACK_PBISHOP   : s.append("b+B"); break;
					case Const.BLACK_PROOK     : s.append("b+R"); break;

					case Const.WHITE_PAWN      : s.append("wP"); break;
					case Const.WHITE_LANCE     : s.append("wL"); break;
					case Const.WHITE_KNIGHT    : s.append("wN"); break;
					case Const.WHITE_SILVER    : s.append("wS"); break;
					case Const.WHITE_GOLD      : s.append("wG"); break;
					case Const.WHITE_BISHOP    : s.append("wB"); break;
					case Const.WHITE_ROOK      : s.append("wR"); break;
					case Const.WHITE_KING      : s.append("wK"); break;
					case Const.WHITE_PPAWN     : s.append("w+P"); break;
					case Const.WHITE_PLANCE    : s.append("w+L"); break;
					case Const.WHITE_PKNIGHT   : s.append("w+N"); break;
					case Const.WHITE_PSILVER   : s.append("w+S"); break;
					case Const.WHITE_PBISHOP   : s.append("w+B"); break;
					case Const.WHITE_PROOK     : s.append("w+R"); break;
					default : break;
				}
				if (gsdb.board[i]!=Const.EMPTY)
				{
					int col=i%9;
					int row=i/9;
					char c=(char)('9'-col);
					char r=(char)('a'+row);
					s.append(c);
					s.append(r);
					s.append(" ");
				}
			}
			if (gsdb.pieces_in_hand[Const.BLACK][Const.PAWN]>0)   s.append("bPH"+gsdb.pieces_in_hand[Const.BLACK][Const.PAWN]+" ");
			if (gsdb.pieces_in_hand[Const.BLACK][Const.LANCE]>0)  s.append("bLH"+gsdb.pieces_in_hand[Const.BLACK][Const.LANCE]+" ");
			if (gsdb.pieces_in_hand[Const.BLACK][Const.KNIGHT]>0) s.append("bNH"+gsdb.pieces_in_hand[Const.BLACK][Const.KNIGHT]+" ");
			if (gsdb.pieces_in_hand[Const.BLACK][Const.SILVER]>0) s.append("bSH"+gsdb.pieces_in_hand[Const.BLACK][Const.SILVER]+" ");
			if (gsdb.pieces_in_hand[Const.BLACK][Const.GOLD]>0)   s.append("bGH"+gsdb.pieces_in_hand[Const.BLACK][Const.GOLD]+" ");
			if (gsdb.pieces_in_hand[Const.BLACK][Const.BISHOP]>0) s.append("bBH"+gsdb.pieces_in_hand[Const.BLACK][Const.BISHOP]+" ");
			if (gsdb.pieces_in_hand[Const.BLACK][Const.ROOK]>0)   s.append("bRH"+gsdb.pieces_in_hand[Const.BLACK][Const.ROOK]+" ");

			if (gsdb.pieces_in_hand[Const.WHITE][Const.PAWN]>0)   s.append("wPH"+gsdb.pieces_in_hand[Const.WHITE][Const.PAWN]+" ");
			if (gsdb.pieces_in_hand[Const.WHITE][Const.LANCE]>0)  s.append("wLH"+gsdb.pieces_in_hand[Const.WHITE][Const.LANCE]+" ");
			if (gsdb.pieces_in_hand[Const.WHITE][Const.KNIGHT]>0) s.append("wNH"+gsdb.pieces_in_hand[Const.WHITE][Const.KNIGHT]+" ");
			if (gsdb.pieces_in_hand[Const.WHITE][Const.SILVER]>0) s.append("wSH"+gsdb.pieces_in_hand[Const.WHITE][Const.SILVER]+" ");
			if (gsdb.pieces_in_hand[Const.WHITE][Const.GOLD]>0)   s.append("wGH"+gsdb.pieces_in_hand[Const.WHITE][Const.GOLD]+" ");
			if (gsdb.pieces_in_hand[Const.WHITE][Const.BISHOP]>0) s.append("wBH"+gsdb.pieces_in_hand[Const.WHITE][Const.BISHOP]+" ");
			if (gsdb.pieces_in_hand[Const.WHITE][Const.ROOK]>0)   s.append("wRH"+gsdb.pieces_in_hand[Const.WHITE][Const.ROOK]+" ");
			s.append("$ ");
		}

		
		for (i=0;root.variation[i].piece!=0;i++)
		{
			if (root.variation[i].comment != null) s.append( "{"+root.variation[i].comment.replace('\n','~')+"} ");
			s.append( root.variation[i].ASCII_move(buf) );
			if (root.variation[i].variation!=null)
			{
				printVariation( root.variation[i], s );
			}
			if (root.variation[i+1].piece!=0) s.append(" ");
		}
		if (root.variation[i].comment != null) s.append( " {"+root.variation[i].comment.replace('\n','~')+"}");
		
		//System.out.println("Game sent ["+s.toString()+"]");

		return(s.toString());
	}

	void printVariation( Move m, StringBuffer s)
	{
		s.append(" (");
		for (int i=0;m.variation[i].piece!=0;i++)
		{
			s.append(" "+m.variation[i].ASCII_move(buf));
			if (m.variation[i].variation!=null)
			{
				printVariation( m.variation[i], s );
			}
		}
		s.append(" )");
	}

	public int size()
	{
		int i;
		for (i=0;root.variation[i].piece!=0;i++);

		return i;
	}

	public int lsize()
	{
		int i;
		for (i=0;thisLevel().variation[i].piece!=0;i++);

		return i;
	}

        public StringBuffer makeKey(StringBuffer key)
        {
                key.setLength(0);
		StringBuffer dt=new StringBuffer(date.toString().trim());       dt.setLength(8);
		StringBuffer bn=new StringBuffer(black_name.toString().trim()); bn.setLength(3);
		StringBuffer wn=new StringBuffer(white_name.toString().trim()); wn.setLength(3);
		StringBuffer rd=new StringBuffer(round.toString().trim());      rd.setLength(2);
		StringBuffer ct=new StringBuffer(country.toString().trim());    ct.setLength(4);

                key.append(dt.toString().trim());  //  Date       0-7
                key.append(bn.charAt(0));          //  Black Name 8-10
                key.append(bn.charAt(1));          //
                key.append(bn.charAt(2));          //
                key.append(wn.charAt(0));          //  White Name 11-13
                key.append(wn.charAt(1));          //
                key.append(wn.charAt(2));          //
                key.append(rd.charAt(0));          //  Round 14-15
                key.append(rd.charAt(1));          //
                key.append(ct.charAt(0));          //  Country 16-19
                key.append(ct.charAt(1));          //
                key.append(ct.charAt(2));          //
                key.append(ct.charAt(3));          //

		for (int i=0;i<key.length();i++)
		{
			if (key.charAt(i)<=' ')
				key.setCharAt(i,'_');
		}
                return key;
	}

	public int advanceMove()
	{
		movenr++;
		return movenr;
	}

	public int retractMove()
	{
		movenr--;
		return movenr;
	}

	public Move prevMove()
	{
		if (movenr>0) return thisLevel().variation[movenr-1];
		return null;
	}

	public Move thisMove()
	{
		return thisLevel().variation[movenr];
	}

	public Move nextMove()
	{
		return thisLevel().variation[movenr+1];
	}

	public Move prevLevel()
	{
		if (level>0) return tlevel[level-1]; else return null;
	}

	public Move thisLevel()
	{
		return tlevel[level];
	}

	public Move nextLevel()
	{
		if (thisMove().variation!=null) 
			return thisMove().variation[0]; 
		else    return null;
	}

	public void begin()
	{
		boolean dm=gsdb.draw_moves;
		gsdb.draw_moves=false;
		while (back());
		gsdb.draw_moves=dm;
		if (gsdb.draw_moves) gsdb.repaint();
		gsdb.firstmovedone=false;
	}

	public void end()
	{
		boolean dm=gsdb.draw_moves;
		gsdb.draw_moves=false;
		while (forward());
		gsdb.draw_moves=dm;
		if (gsdb.draw_moves) gsdb.repaint();
	}

	public boolean back()
	{
		if (movenr > 0)
		{
			if (retractMove()==0) gsdb.firstmovedone=false;
			gsdb.drawLastMove(gsdb.getGraphics());
			gsdb.changeColor();
			gsdb.undoMove(thisMove());
			gsdb.setComment(thisMove());
			gsdb.setVariation(thisMove());
			gsdb.drawSide(gsdb.getGraphics());
			return true;
		}
		else
		return false;
	}

	public boolean forward()
	{
		if (thisMove().piece!=0)
		{
			gsdb.doMove(thisMove());
			gsdb.drawLastMove(gsdb.getGraphics());
			advanceMove();
			gsdb.setComment( thisMove() );
			gsdb.setVariation( thisMove() );
			gsdb.changeColor();
			gsdb.drawSide(gsdb.getGraphics());
			return true;
		}
		else
		return false;
	}

	public void gotoMove(int mnr)
	{
		begin();
		while (movenr<mnr)
		{
			gsdb.doMove(thisLevel().variation[movenr]);
			movenr++;
			gsdb.changeColor();
		}
		gsdb.setComment( thisLevel().variation[movenr] );
		gsdb.setVariation( thisLevel().variation[movenr] );
		gsdb.drawSide(gsdb.getGraphics());
		gsdb.drawLastMove(gsdb.getGraphics());
	}

        public boolean newVariation()
        {
		if (prevMove()==null) return false;
		back();
		boolean dm=gsdb.draw_moves;
		gsdb.draw_moves=false;
		while( thisMove().variation!=null) enterVariation();
		thisMove().variation=new Move[5];
		for (int i=0;i<5;i++) thisMove().variation[i]=new Move();
		enterVariation();
		leaveVariation();
		enterVariation();
		gsdb.draw_moves=dm;
		return true;
        }
        public void enterVariation()
        {
		if (thisMove().variation==null) return;

		tlevel[level+1]=thisMove();
		tmoves[level]=movenr;
                level++;
                movenr=0;
        }

        public void leaveVariation()
        {
		if (prevLevel()==null) return;

                begin(); // go to beginning of variation
		thisLevel().moves=lsize();
                tlevel[level]=null;
		level--;

		movenr=tmoves[level];
        }

	public Vector getVariations()
	{
		Move look=thisMove();
		if (look==null) return null;
		if (look.variation==null) return null;

		Vector vv=new Vector(5,5);
			
		vv.addElement(look);
		while (look.variation!=null)
		{
			look=look.variation[0];
			vv.addElement(look);
		}
		return vv;
	}
}

