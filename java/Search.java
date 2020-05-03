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
//////         Search class                             ////////
////////////////////////////////////////////////////////////////

public class Search extends Frame
{
	public Gsdb gsdb;
	public Choice field1,field2;
	public Choice andor;
	public TextField search1, search2;
	public Checkbox keepselection;
	public Label remark;
	public Button look;
	public Button hide;
	public Panel center, south;
	public Font fnt;
	public Color col;

        public static final char FSEP    =      (char)1;
	public static final char KSEP    =      (char)3;


	public Search(Gsdb g)
	{
		super("Do a search in the database");
		gsdb=g;

		fnt=new Font("Helvetica",Font.BOLD, 12);
		col=Const.COLOR_BACKGR;

		setLayout(new BorderLayout());
		setBackground(col);
		center=new Panel();
		center.setLayout(new GridLayout(3,2));
		field1 = new Choice();
		  field1.setBackground(col);
		  field1.setFont(new Font("Courier",Font.BOLD,12));
		  field1.addItem("black_elo"); field1.addItem("black_grade"); field1.addItem("black_name");  field1.addItem("comment");
		  field1.addItem("country");   field1.addItem("date");        field1.addItem("email");       field1.addItem("game");
	 	  field1.addItem("gametype");  field1.addItem("key");         field1.addItem("name");        field1.addItem("proam");
		  field1.addItem("result");    field1.addItem("round");       field1.addItem("source");      field1.addItem("tournament");
		  field1.addItem("venue");     field1.addItem("white_elo");   field1.addItem("white_grade"); field1.addItem("white_name");
		  field1.addItem("updated");     
		  field1.select("tournament");
		field2 = new Choice();
		  field2.setBackground(col);
		  field2.setFont(new Font("Courier",Font.BOLD,12));
		  field2.addItem("black_elo"); field2.addItem("black_grade"); field2.addItem("black_name");  field2.addItem("comment");
		  field2.addItem("country");   field2.addItem("date");        field2.addItem("email");       field2.addItem("game");
	 	  field2.addItem("gametype");  field2.addItem("key");         field2.addItem("name");        field2.addItem("proam");
		  field2.addItem("result");    field2.addItem("round");       field2.addItem("source");      field2.addItem("tournament");
		  field2.addItem("venue");     field2.addItem("white_elo");   field2.addItem("white_grade"); field2.addItem("white_name");
		  field2.addItem("updated");
		  field2.select("tournament");
		andor=new Choice();
		  andor.setBackground(col);
		  andor.setFont(new Font("Courier",Font.BOLD,12));
		  andor.addItem("AND"); andor.addItem("OR");
		remark=new Label("Type in your search string. (* and ? are allowed)");
		remark.setFont(fnt);
		remark.setBackground(Const.COLOR_HEADER);

		search1=new TextField(40);  search2=new TextField(40);
		search1.setFont(fnt);       search2.setFont(fnt);
		search1.setBackground(col); search2.setBackground(col);

		center.add(field1);
		center.add(search1);
		center.add(andor);
		center.add(new Label("AND/OR"));
		center.add(field2);
		center.add(search2);

		south=new Panel();
		south.setLayout(new BorderLayout());
		south.setBackground(col);

		look = new Button("Search games");
		look.setBackground(Const.COLOR_OK);
		look.setFont(fnt);
		keepselection=new Checkbox("keep old selection");
		keepselection.setBackground(col);
		keepselection.setFont(fnt);
		hide = new Button("Hide window");
		hide.setBackground(Const.COLOR_HIDE);
		hide.setFont(fnt);
		south.add("West",look);
		south.add("Center",keepselection);
		south.add("East",hide);

                add("North", remark);
                add("Center",center);
                add("South" ,south );

		pack();
	}

	public boolean action(Event e, Object arg)
        {
		if (e.target==hide || e.id==Event.WINDOW_DESTROY)
		{
			hide();
		}
		if (e.target==keepselection)
		{
			gsdb.keep_selection=keepselection.getState();
		}
		if (e.target==look)
		{
			String one  =new String();
			String two  =new String();
			String getit=new String("");

			if (andor.getSelectedItem().compareTo("AND")==0)
			{
				one=getit+field1.getSelectedItem()+Const.KSEP+search1.getText().trim().replace('=','?').replace(',','?');
				two=getit+field2.getSelectedItem()+Const.KSEP+search2.getText().trim().replace('=','?').replace(',','?');

				if (search1.getText().trim().length()!=0) getit=getit+one;
				if (search1.getText().trim().length()!=0 && search2.getText().trim().length()!=0) getit=getit+Const.FSEP;
				if (search2.getText().trim().length()!=0) getit=getit+two;

				gsdb.client.searchGame( getit );
				hide();
				gsdb.setMessage("searching in the database... ("+getit.replace(KSEP,'=').replace(FSEP,',')+")");
			}
			else // OR is selected : we have to do 2 queries and combine the result : overloaded searchGame(str1, str2);
			{
				one=getit+field1.getSelectedItem()+Const.KSEP+search1.getText().replace('=','?').replace(',','?');
				two=getit+field2.getSelectedItem()+Const.KSEP+search2.getText().replace('=','?').replace(',','?');

				if (search2.getText().trim().length()==0)
				{
					gsdb.client.searchGame( one );
					gsdb.setMessage("searching in the database... ("+field1.getSelectedItem()+"="+search1.getText()+")");
				}
				else
				{
					gsdb.client.searchGame( one, two );
					gsdb.setMessage("searching in the database... ("+field1.getSelectedItem()+"="+search1.getText()+") OR ("+field2.getSelectedItem()+"="+search2.getText()+")");
				}
				hide();
			}
		}
		return true;
	}
}




