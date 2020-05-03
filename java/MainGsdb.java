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
import java.util.StringTokenizer;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;



////////////////////////////////////////////////////////////////
//////         Gsdb class                               ////////
////////////////////////////////////////////////////////////////

public class MainGsdb extends java.applet.Applet
{
	public Button client;
	public Image database;
	public Frame browser;

	// Initialise our GSDB applet
	//
	public void init()
	{
		database=getImage("pictures/database.gif");
		showStatus("Click to start a new GSDB client.");

		Object ap=getParent();
		while (! (ap instanceof Frame))
			ap= ((Component)ap).getParent();
		browser=(Frame)ap;

		MyDialog md=new MyDialog(browser,"Welcome to the GSDB project.  Click on database to open a client.",MyDialog.OK,MyDialog.NOTHING);
		md.show();
	}

	public void paint(Graphics g)
	{
		int w=size().width;
		int h=size().height;

		g.drawImage(database,0,0, w-2, h-2,this);
		g.setColor(Color.gray);
		g.drawLine(1,h-1,w,h-1);
		g.drawLine(w-1,1,w-1,h-1);
	}

	public boolean mouseMove(Event evt, int x, int y)
	{
		showStatus("Click to start a new GSDB client.");
		return true;
	}

	public boolean mouseDown(Event evt, int x, int y)
	{
		MyDialog md=new MyDialog(browser,"The GSDB client is loading now...",MyDialog.OK,MyDialog.NOTHING, false);
		md.show();
		if (evt.metaDown())
		{
			// this, Gametype, SmallBoard?, Connect?
			Gsdb gsdb_session=new Gsdb(this, "Even game", true, true);
			gsdb_session.show();
		}
		else
		{
			// this, Gametype, SmallBoard?, Connect?
			Gsdb gsdb_session=new Gsdb(this, "Even game", false, true);
			gsdb_session.show();
		}

		md.hide(); md.dispose();

		return true;
	}

	public Image getImage( String file )
	{
		return getImage(getDocumentBase(), "java/"+file);
	}

        public String getFile(String filename)
        {
                InputStream is=null;
                DataInputStream dis=null;
                URL psntext;
                StringBuffer buffer;
                int i=0;
                buffer=new StringBuffer("");

                try
                {
                        psntext=new URL(getDocumentBase(),"java/"+filename);
                        is=psntext.openStream();

                        i=is.read();
                        while (i!=-1)
                        {
                                buffer.append((char)i);
                                i=is.read();
                        }

                        is.close();
                }
                catch (Exception e) { System.out.println("ERROR : "+e.toString()); }

                return (buffer.toString());
        }


}

