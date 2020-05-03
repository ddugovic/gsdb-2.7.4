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

////////////////////////////////////////////////////////////////
//////         StreamAnalyser class                     ////////
////////////////////////////////////////////////////////////////

public class StreamAnalyser
{
	String Buf;
	int pos;
	int len;
	int begin;
	int count;
	public char cc;

	public StreamAnalyser(String b)
	{
		Buf=new String(b+"  ");
		pos=0;
		len=Buf.length();
	}

	public StreamAnalyser(StringBuffer b)
	{
		Buf=new String(b.toString()+"  ");
		pos=0;
		len=Buf.length();
	}

	public String getWord()
	{
		if (!skipSpaces()) return null;
		char c=Char();
		begin=pos;
		while (!End() && c!=' ' && c!='\n' && c!='\r' && c!='\t' && c!='}' && c!=')' ) c=nextChar();
		//if (End() || begin==pos) return null; else return Buf.substring(begin,pos-1);
		if (begin==pos) return null; else return Buf.substring(begin,pos-1);
	}

	public String getNumber()
	{
		if (!skipSpaces()) return null;
		char c=Char();
		begin=pos;
		while (!End() && c!=' ' && c!='\n' && c!='\r' && c!='\t' && (( c<='9' && c>='0') || c=='-')) c=nextChar();
		//if (End() || begin==pos) return null; else return Buf.substring(begin,pos-1);
		if (begin==pos) return null; else return Buf.substring(begin,pos-1);
	}

	public String getPair(char b, char e)
	{
		if (!skipSpaces()) return null;
		char c=Char();
		if (c!=b) return null; // allways needs to start with b char
		begin=pos;
		count=0;
		while (!End() && (c!=e || count!=0))
		{
			c=nextChar();
			if (c==b) count++;
			if (c==e) count--;
		}
		if ((End() || begin==pos) && c!=e) return null; else return Buf.substring(begin+1,pos-1).trim();
	}

	public String getPair(char be)
	{
		if (!skipSpaces()) return null;
		char c=Char();
		if (c!=be) return null; // allways needs to start with be char
		begin=pos;
		advancePosition();
		c=Char();
		while (!End() && c!=be) c=nextChar();
		if ((End() || begin==pos) && c!=be) return null; else return Buf.substring(begin+1,pos-1).trim();
	}

	public String getUntil(char u)
	{
		if (!skipSpaces()) return null;
		char c=Char();
		begin=pos;
		while (!End() && c!=u) c=nextChar();
		if (End()) pos=begin; // return to from where we came, no next u char in Stream !
		if (begin==pos) return null; else return Buf.substring(begin,pos-1).trim();
	}

	public int advancePosition()
	{
		pos++;
		return pos;
	}

	public int advancePosition(int adv)
	{
		pos+=adv;
		return pos;
	}

	public boolean skipSpaces()
	{
		if (End()) return false;
		while (!End() && ( Char()==' ' || Char()=='\n' || Char()=='\r' || Char()=='\t' )) pos++;
		if (End()) return false; else return true;
	}

	public char Char()
	{
		return Buf.charAt(pos);
	}

	public char nextChar()
	{
		cc=Buf.charAt(pos);
		pos++;
		return cc;
	}

	public boolean End()
	{
		if (pos<len) return false; else return true;
	}
}
