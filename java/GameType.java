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

public class GameType extends Dialog
{
	public Gsdb gsdb;
	public Label lab;
	public Button ok;
	public Button cancel;
	public Panel north,south, okcancel;
	public int funct;
	public boolean result;
	public CheckboxGroup cbg;
	public Font fnt;

	public GameType(Gsdb g)
	{
		super(g,"Select Game Type",true);
		gsdb=g;

		setLayout(new GridLayout(13,1));

		fnt=new Font("Helvetica", Font.BOLD, 12);

		ok = new Button(" OK ");
		cancel = new Button(" Cancel ");
		ok.setFont(fnt);
		ok.setBackground(Const.COLOR_OK);
		cancel.setFont(fnt);
		cancel.setBackground(Const.COLOR_CANCEL);

		setBackground(Const.COLOR_BACKGR);

		add("Center",new Label("Choose a game type ..."));

		cbg=new CheckboxGroup();
		gsdb.cbgametype=new Checkbox[11];
		for (int h=0;h<11;h++)
		{
			gsdb.cbgametype[h]=new Checkbox( gsdb.MENU_VIEW_HAND[h]);
			gsdb.cbgametype[h].setCheckboxGroup(cbg);
			gsdb.cbgametype[h].setFont(fnt);
			add(gsdb.cbgametype[h]);
		}

		south=new Panel();
		south.setLayout(new BorderLayout());
		okcancel=new Panel();
		okcancel.setLayout(new FlowLayout());
		okcancel.add(ok);
		okcancel.add(cancel);

		add(okcancel);

		pack();
	}

	public boolean action(Event e, Object arg)
        {
		result=false;
		if (e.target==ok || e.target==cancel || e.id==Event.WINDOW_DESTROY)
		{
			hide();
		}
		if (e.target==ok)
		{
			result=true;
		}
		return true;
	}
}





