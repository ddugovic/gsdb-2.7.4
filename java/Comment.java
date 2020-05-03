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

public class Comment extends Frame
{
	public Gsdb gsdb;
	public TextArea comment;
	public Button set;
	public Checkbox auto;
	public Button hide;
	public Panel south;
	public Label header;
	public Font fnt;
	public Color col;

	public Comment(Gsdb g)
	{
		super("Comment on this move");

		gsdb=g;

		fnt=new Font("Helvetica",Font.BOLD, 14);
		col=Const.COLOR_BACKGR;

		setLayout(new BorderLayout());
		setBackground(col);

		comment = new TextArea(10,50);
		comment.setBackground(col);
		comment.setFont(new Font("Courier",Font.BOLD,14));
		set = new Button("Set comment");
		set.setBackground(Const.COLOR_OK);
		auto = new Checkbox("Auto show");
		hide = new Button("Hide window");
		hide.setBackground(Const.COLOR_HIDE);

		south=new Panel();
		south.setLayout(new BorderLayout());
		south.add("West",set);
		south.add("Center",auto);
		south.add("East",hide);

		header=new Label("Comments :");
		header.setBackground(Const.COLOR_HEADER);
		header.setFont(fnt);

                add("North",header);
                add("Center",comment       );
                add("South",south       );

		pack();
		resize(420,260);

		auto.setState(true);
	}



	public boolean action(Event e, Object arg)
        {
		if (e.target==hide || e.id==Event.WINDOW_DESTROY)
		{
			hide();
		}
		if (e.target==set)
		{
			if (comment.getText().replace('\n',' ').trim().length()>0)
			{
				gsdb.game.thisMove().comment=new String( comment.getText() );
				gsdb.setMessage("Comment set for this move");
			}
			else
			{
				gsdb.game.thisMove().comment=null;
				gsdb.setMessage("Comment cleared for this move");
			}
		}
		return true;
	}
}




