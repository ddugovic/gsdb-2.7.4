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
import java.util.Vector;

////////////////////////////////////////////////////////////////
//////         chooseFile class                         ////////
////////////////////////////////////////////////////////////////

public class chooseFile extends Dialog
{
	public Gsdb gsdb;
	public List files;
	public Button select, cancel;
	public Label header;
	public Color col;
	public Font fnt;
	public Panel south;
	public String buf=new String();
	public String selected_string=null;

	public chooseFile(Gsdb g, String title, String directory, String index)
	{
		super(g,title,true);

		gsdb=g;

		setLayout(new BorderLayout());
		fnt=new Font("Helvetica",Font.BOLD,14);
		col=Const.COLOR_BACKGR;
		setBackground(col);

		files = new List(10,false);
		files.setFont(fnt);
		files.setBackground(col);
		south=new Panel();
		south.setLayout(new BorderLayout());
		select = new Button("Select");
		select.setBackground(Const.COLOR_OK);
		select.setFont(fnt);
		cancel = new Button("Cancel");
		cancel.setBackground(Const.COLOR_CANCEL);
		cancel.setFont(fnt);
		south.add("East",select);
		south.add("West",cancel);

		header=new Label("Chose a file :");
		header.setFont(fnt);

                add("North",header    );
                add("Center",files);
                add("South",south     );

		pack();
		resize(300,280);

		setFiles(directory+index);
	}

	public void setFiles(String ind)
	{
		String list=gsdb.getFile(ind).replace('\r',' ');
		String file;

		int i=0;
		int t=0;
		while (i<list.length() && t>=0)
		{
			t=list.indexOf("\n",i);
			if (t>=0)
			{
				file=list.substring(i,t).trim();
				//System.out.println("Filename found:"+file);
				files.addItem(file);
				i=t+1;
			}
		}
	}

	public boolean action(Event e, Object arg)
        {
		if (e.target==cancel || e.id==Event.WINDOW_DESTROY)
		{
			hide();
		}
		if (e.target==select)
		{
			selected_string=files.getSelectedItem();
			hide();
		}
		return true;
	}

	public String getFile()
	{
		return selected_string;
	}
}




