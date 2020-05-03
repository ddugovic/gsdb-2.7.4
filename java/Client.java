
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
//////         Client class                             ////////
////////////////////////////////////////////////////////////////

public class Client extends Thread
{
	private Socket soc;
	public DataInputStream in;
	public StreamListener listener;
	public Gsdb gsdb;
	public byte[] byteline;

	public Client(Gsdb g)
	{
		super();

		byteline=new byte[2048];
		gsdb=g;
	}

	public boolean connect()
	{
		String host=gsdb.main_gsdb.getCodeBase().getHost();
		int port = 54321;

		System.out.println("Connecting to host ["+host+"] !");
		try
		{
			soc = new Socket(host,port);
			in = new DataInputStream(soc.getInputStream());

			listener = new StreamListener(in, gsdb);
		}
		catch (Exception e) 
		{
			return false;
		}

		return true;
	}

	public boolean disconnect()
	{
		try
		{
			soc.close();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	public boolean sendGame(Game g)
	{
		StringBuffer s=new StringBuffer("");
		StringBuffer k=new StringBuffer("");
		PrintStream out;
		try
		{
			out = new PrintStream(soc.getOutputStream());

			g.makeKey(k);

			out.println("SEND GAME");
			out.println(
				    k.toString()+Const.FSEP+
				    g.name.toString()+Const.FSEP+
			            g.email.toString()+Const.FSEP+
				    g.country.toString()+Const.FSEP+
				    g.black_name.toString()+Const.FSEP+
				    g.white_name.toString()+Const.FSEP+
				    g.black_grade.toString()+Const.FSEP+
				    g.white_grade.toString()+Const.FSEP+
				    g.black_elo.toString()+Const.FSEP+
				    g.white_elo.toString()+Const.FSEP+
				    g.result.toString()+Const.FSEP+
				    g.comment.toString()+Const.FSEP+
				    g.source.toString()+Const.FSEP+
				    g.tournament.toString()+Const.FSEP+
				    g.date.toString()+Const.FSEP+
				    g.round.toString()+Const.FSEP+
				    g.venue.toString()+Const.FSEP+
				    g.proam.toString()+Const.FSEP+
				    g.gametype.toString()+Const.FSEP+
				    g.gameString(s));
			out.flush();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	public boolean askGame(String key)
	{
		PrintStream out;
		try
		{
			out = new PrintStream(soc.getOutputStream());

			out.println("GET GAME");
			out.println("key"+Const.KSEP+key);
			out.flush();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	public boolean searchGame(String search)
	{
		PrintStream out;
		try
		{
			out = new PrintStream(soc.getOutputStream());

			out.println("GET GAME");
			out.println(search);
			out.flush();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	public boolean searchGame(String search1, String search2)
	{
		PrintStream out;
		try
		{
			out = new PrintStream(soc.getOutputStream());

			out.println("GET GAMES");
			out.println(search1);
			out.println(search2);
			out.flush();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

        public boolean sendBitmap(int w, int h)
        {
                PrintStream out;
		int bitbyte;

		StringBuffer sb=new StringBuffer("");

                try
                {
                        out = new PrintStream(soc.getOutputStream());

                        out.println("BITMAP");
                        out.println(""+w);
                        out.println(""+h);
			for (int j=h-1;j>=0;j--)
			{
				sb.setLength(0);
				for (int i=0;i<w;i=i+4)
				{
                                        bitbyte=32;
                                        for (int b=0;b<4;b++)
                                        {
						if (i+b<w)
                                                if (gsdb.pixels[i+b+j*w]==-1)
                                                {
                                                        bitbyte+=(1<<(3-b));
                                                }
                                        }
					sb.append((char)bitbyte);
				}
				out.println(sb.toString());
			}
			out.flush();
			gsdb.setMessage("Bitmap sent to server");
                }
                catch(Exception e)
                {
                        return false;
                }
                return true;
        }
}



