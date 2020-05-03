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
//////         varChoice class                          ////////
////////////////////////////////////////////////////////////////

public class varChoice extends Frame
{
	public Gsdb gsdb;
	public List varchoice;
	public Button select, delete, cancel;
	public Label header;
	public Color col;
	public Font fnt;
	public Panel south;
	public String buf=new String();

	public varChoice(Gsdb g)
	{
		super("Variations List");

		gsdb=g;

		setLayout(new BorderLayout());
		fnt=new Font("Helvetica",Font.BOLD,14);
		col=Const.COLOR_BACKGR;
		setBackground(col);

		varchoice = new List(5,false);
		varchoice.setFont(fnt);
		varchoice.setBackground(col);
		south=new Panel();
		south.setLayout(new BorderLayout());
		select = new Button("Select");
		select.setBackground(Const.COLOR_OK);
		select.setFont(fnt);
		delete = new Button("Delete");
		delete.setBackground(Const.COLOR_CLEAR);
		delete.setFont(fnt);
		cancel = new Button("Cancel");
		cancel.setBackground(Const.COLOR_CANCEL);
		cancel.setFont(fnt);
		south.add("East",select);
		south.add("Center",delete);
		south.add("West",cancel);

		header=new Label("Chose a variation :");
		header.setFont(fnt);

                add("North",header    );
                add("Center",varchoice);
                add("South",south     );

		pack();
		resize(150,280);
	}

	public boolean action(Event e, Object arg)
        {
		if (e.target==cancel || e.id==Event.WINDOW_DESTROY)
		{
			hide();
		}
		if (e.target==select || e.target==delete)
		{
			String sel=varchoice.getSelectedItem();
			int vari=0;

			if (sel==null) return true;
			for (int i=0;i<varchoice.countItems();i++)
			{
				if (sel.equals(varchoice.getItem(i)))
				{
					vari=i;
					break;
				}
			}
			for (int i=0;i<vari;i++)
			{
				gsdb.game.enterVariation();
			}
			//System.out.println("Entered variation: level="+gsdb.game.level+" thisMove()="+gsdb.game.thisMove().ASCII_move(buf));
			//System.out.println("tlevel[level]="+gsdb.game.tlevel[gsdb.game.level].ASCII_move(buf));
			//if (gsdb.game.level>0) System.out.println("tlevel[level-1]="+gsdb.game.tlevel[gsdb.game.level-1].ASCII_move(buf));
			if (e.target==delete)
			{
				if (vari==0)
				{
					MyDialog md=new MyDialog(null,"You can't delete the main variation !",MyDialog.OK,MyDialog.NOTHING);
					md.show();
				}
				else
				{
					Move thisl=gsdb.game.thisLevel();
					Move link[]=null;
					if (thisl.variation!=null)
					{
						link=thisl.variation[0].variation;
					}
					gsdb.game.leaveVariation();
					thisl.variation=link;
				}
			}
			else
			{
				gsdb.game.forward();
			}
			hide();
		}
		return true;
	}

	public void setChoices(Vector v)
	{
		if (v==null) return;

		if (varchoice.countItems()!=0) varchoice.clear();
		for (int i=0;i<v.size();i++)
		{       
			varchoice.addItem(((Move)v.elementAt(i)).ASCII_move(buf));
		}
		varchoice.select(0);
	}
}




