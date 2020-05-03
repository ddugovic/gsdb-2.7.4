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
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;

////////////////////////////////////////////////////////////////
//////         Move class                               ////////
////////////////////////////////////////////////////////////////

class Move 
{
	public int from, to, piece, taken;
	public boolean promotes;
	public boolean expanded;
	public String comment;
	public Move variation[]; // to store the variation on the move.
	public int moves;    // to restore the normal game or the previous variation.

	public Move()
	{
		clearMove();
	}

	public void clearMove()
	{
		from=0;
		to=0;
		piece=0;
		taken=0;
		promotes=false;
		expanded=false;
		comment=null;
		variation=null;
		moves=0;
	}

	public int fx() { return(from%9); }
	public int fy() { return(from/9); }
	public int tx() { return(to  %9); }
	public int ty() { return(to  /9); }

	public boolean equals(Move m)
	{
		if (from!=m.from) return false;
		if (to!=m.to) return false;
		if (piece!=m.piece) return false;
		if (taken!=m.taken) return false;
		if (promotes!=m.promotes) return false;
		return true;
	}

	public void copy(Move m)
	{
		from=m.from;
		to=m.to;
		piece=m.piece;
		taken=m.taken;
		promotes=m.promotes;
	}

	public void copyAll(Move m)
	{
		from=m.from;
		to=m.to;
		piece=m.piece;
		taken=m.taken;
		promotes=m.promotes;
		comment=m.comment;
		variation=m.variation;
		moves=m.moves;
	}

	public void setMove(int fx,int fy,int tx,int ty,int pce,int tkn,boolean pro)
	{
		from     = fx+9*fy;
		to       = tx+9*ty;
		piece    = pce;
		taken    = tkn;
		promotes = pro;
		comment  = null;
		variation= null;
		moves    = 0;
	}

	public void setMove(int f,int t,int pce,int tkn, boolean pro)
	{
		from     = f;
		to       = t;
		piece    = pce;
		taken    = tkn;
		promotes = pro;
		comment  = null;
		variation= null;
		moves    = 0;
	}

	public boolean isBlack(int piece)
	{
		if (piece>Const.BLACK_PROOK) return false; else return true;
	}

	public boolean isWhite(int piece)
	{
		if (piece>Const.BLACK_PROOK) return true; else return false;
	}

	public boolean isPromoted(int piece)
	{
		if (isBlack(piece))
		{
			if (piece>Const.BLACK_KING) return true; else return false;
		}
		else
		{
			if (piece>Const.WHITE_KING) return true; else return false;
		}
	}

	public int dePromote(int piece)
	{
		if (isBlack(piece))
		{
			if (isPromoted(piece))
			{
				if (piece>=Const.BLACK_PBISHOP) return(piece-7); else return piece-8;
			}
			else
			{
				return piece;
			}
		}
		else
		{
			if (isPromoted(piece))
			{
				if (piece>=Const.WHITE_PBISHOP) return(piece-7); else return piece-8;
			}
			else
			{
				return piece;
			}
		}
	}

	//
	// Converts from-to pair into an ASCII move representation
	//
	
	public String ASCII_move(String s)
	{
		Move move=this;

		int pc=move.piece;
		int tk=move.taken;
		int fx=move.fx();
		int fy=move.fy();
		int tx=move.tx();
		int ty=move.ty();
		StringBuffer m;

		if (move.promotes) 
		{
			pc=dePromote(pc);
		}
		switch(pc)
		{
			case Const.BLACK_PAWN   : case Const.WHITE_PAWN   : m=new StringBuffer("P"); break;
			case Const.BLACK_LANCE  : case Const.WHITE_LANCE  : m=new StringBuffer("L"); break;
			case Const.BLACK_KNIGHT : case Const.WHITE_KNIGHT : m=new StringBuffer("N"); break;
			case Const.BLACK_SILVER : case Const.WHITE_SILVER : m=new StringBuffer("S"); break;
			case Const.BLACK_GOLD   : case Const.WHITE_GOLD   : m=new StringBuffer("G"); break;
			case Const.BLACK_BISHOP : case Const.WHITE_BISHOP : m=new StringBuffer("B"); break;
			case Const.BLACK_ROOK   : case Const.WHITE_ROOK   : m=new StringBuffer("R"); break;
			case Const.BLACK_KING   : case Const.WHITE_KING   : m=new StringBuffer("K"); break;
			case Const.BLACK_PPAWN  : case Const.WHITE_PPAWN  : m=new StringBuffer("+P"); break;
			case Const.BLACK_PLANCE : case Const.WHITE_PLANCE : m=new StringBuffer("+L"); break;
			case Const.BLACK_PKNIGHT: case Const.WHITE_PKNIGHT: m=new StringBuffer("+N"); break;
			case Const.BLACK_PSILVER: case Const.WHITE_PSILVER: m=new StringBuffer("+S"); break;
			case Const.BLACK_PBISHOP: case Const.WHITE_PBISHOP: m=new StringBuffer("+B"); break;
			case Const.BLACK_PROOK  : case Const.WHITE_PROOK  : m=new StringBuffer("+R"); break;
			default: m=new StringBuffer(" ");  break;
		}
		if (move.from != Const.DROP)
		{
			m.append((char)('9'-fx));
			m.append((char)('a'+fy));
			if (tk!=Const.EMPTY) m.append('x'); else m.append('-');
			m.append((char)('9'-tx));
			m.append((char)('a'+ty));
		}
		else
		{
			m.append('\'');
			m.append((char)('9'-tx));
			m.append((char)('a'+ty));
		}
		if (move.promotes)
		{
			m.append('+');
		}

		s=m.toString();

		return(s);
	}

}

