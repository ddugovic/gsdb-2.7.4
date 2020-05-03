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
//////         Help    class                            ////////
////////////////////////////////////////////////////////////////

public class Help extends Frame
{
	public Button hide;
	public Panel north, south;
	public TextArea help;
	public Color col;

	public Help(Gsdb gsdb)
	{
		super("Help window");

		setFont(new Font("Times New Roman",Font.BOLD,12));
		col=Const.COLOR_BACKGR;

		setLayout(new BorderLayout());
		help=new TextArea( 30, 80 );
		help.setEditable(false);

		hide = new Button("Hide window");
		hide.setBackground(Const.COLOR_HIDE);
		hide.setFont(new Font("Helvetica",Font.BOLD, 12));
		add("North",help);
		add("South",hide);

		help.setText( gsdb.main_gsdb.getFile("gsdb.hlp") );

		pack();

		resize(610,480);
	}

	public boolean action(Event e, Object arg)
        {
		if (e.target==hide || e.id==Event.WINDOW_DESTROY)
		{
			hide();
		}
		return true;
	}
}

