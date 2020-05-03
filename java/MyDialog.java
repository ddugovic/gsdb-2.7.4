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
//////         Comment class                            ////////
////////////////////////////////////////////////////////////////

public class MyDialog extends Dialog
{
	public Gsdb gsdb;
	public Label lab;
	public Button ok;
	public Button cancel;
	public Panel north,south, okcancel;
	public int funct;
	public boolean result;

	public static final int OK        =1;
	public static final int CANCEL    =2;
	public static final int OKCANCEL  =3;
	public static final int YESNO     =4;

	public static final int NOTHING   =0;
	public static final int SEND_GAME =1;
	public static final int OVERWRITE =2;
	public static final int NEW_GAME  =3;
	public static final int LOAD_PSN  =4;
	public static final int QUIT      =5;
	public static final int DECODE    =6;


	public MyDialog(Frame f, String text, int type, int function, boolean modal)
	{
		super(f,"Message",modal);
		gsdb=null;
		initDialog(text, function, type);
	}

	public MyDialog(Frame f, String text, int type, int function)
	{
		super(f,"Message",true);
		gsdb=null;
		initDialog(text, function, type);
	}

	public MyDialog(Gsdb g, String text, int type, int function, boolean modal)
	{
		super(g,"Message",modal);
		gsdb=g;
		initDialog(text, function, type);
	}
	public MyDialog(Gsdb g, String text, int type, int function)
	{
		super(g,"Message",true);
		gsdb=g;
		initDialog(text, function, type);
	}

	public void initDialog(String text, int function, int type)
	{
		funct=function;
		result=false;

		setLayout(new BorderLayout());

		if (type!=YESNO)
		{
			ok = new Button(" OK ");
			cancel = new Button(" Cancel ");
		}
		else
		{
			ok = new Button(" YES ");
			cancel = new Button(" NO ");
		}
		ok.setFont(new Font("Helvetica", Font.BOLD, 14));
		ok.setBackground(Const.COLOR_OK);
		cancel.setFont(new Font("Helvetica", Font.BOLD, 14));
		cancel.setBackground(Const.COLOR_CANCEL);

		lab = new Label("  "+text+"  ", Label.CENTER);
		lab.setFont(new Font("Helvetica", Font.BOLD, 16));

		setBackground(Const.COLOR_BACKGR);

		north=new Panel();
		north.setLayout(new BorderLayout());
		north.add("Center",lab );

		south=new Panel();
		south.setLayout(new BorderLayout());
		okcancel=new Panel();
		okcancel.setLayout(new FlowLayout());

		switch (type)
		{
		case OK    	: okcancel.add(ok); south.add("Center",okcancel); break;
		case CANCEL	: okcancel.add(cancel); south.add("Center",cancel); break;
		case OKCANCEL	:
		case YESNO      : okcancel.add(ok); okcancel.add(cancel);
				  south.add("Center",okcancel); 
		default: break;
		}

                add("North",north);
		add("Center",new Label(" "));
                add("South",south);

		pack();
	}

	public boolean action(Event e, Object arg)
        {
		if (e.target==ok || e.target==cancel || e.id==Event.WINDOW_DESTROY)
		{
			hide();
		}
		if (e.target==ok)
		{
			result=true;
			switch(funct)
			{
			case NOTHING	: break;
			case SEND_GAME  : gsdb.sendGame(); break;
			case OVERWRITE  : gsdb.sendGame(); break;
			case NEW_GAME   : gsdb.game.begin(); gsdb.newGame(); gsdb.repaint();
					  gsdb.setMessage("game cleared !"); break;
			case LOAD_PSN   : gsdb.readPSNFile(gsdb.PSNFileName); dispose(); return true;
			case QUIT       : gsdb.stop(); dispose(); return true;
			case DECODE     : gsdb.dec_frame.decodeNow(); dispose(); return true;
			}
		}
		dispose();
		return true;
	}
}





