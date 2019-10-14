package pt.iul.ista.poo.field.objects;

import pt.iul.ista.poo.field.Field;
import pt.iul.ista.poo.utils.Point2D;

public class Plane extends FireFightObject implements Updatable {

	private int timer = 0;
	
	public Plane(Point2D p) {
		super(p);

	}
	
	public void setTimer(int timer) {
		this.timer = timer;
	}

	@Override
	public int getLayer() {

		return 3;
	}
	
	@Override
	public String getName() {
		
		return "plane";
	}

	@Override
	public void update() {
		
		timer++;
		if (timer%2 == 0)
		{
			if (!Field.getInstance().inBounds(getPosition()))
				Field.getInstance().addToRemove(this);
			else
				Field.getInstance().extinguish(getPosition());
			if (Field.getInstance().smokeAtPosition(getPosition())!=null)
			{
				Smoke s = Field.getInstance().smokeAtPosition(getPosition());
				Field.getInstance().addToRemove(s);
			}
		/*	Point2D p = new Point2D (getPosition().getX(),getPosition().getY()-1);
			if (!Field.getInstance().inBounds(p))
				Field.getInstance().addToRemove(this);
			else
				Field.getInstance().extinguish(p);  */

			move();
		}
	}

	private void move () {
		
		setPosition(new Point2D (getPosition().getX(),getPosition().getY()-2));

		if (getPosition().getY()<0)
			Field.getInstance().addToRemove(this);

		if (Field.getInstance().smokeAtPosition(getPosition())!= null)
		{
			Field.getInstance().addToRemove(this);
			Explosion e = new Explosion (getPosition());
			Field.getInstance().addToInsert(e);
		}
	}

	public static double planeProb(int fogos)
	{
		double n = fogos*Math.sqrt(fogos)/1000.0 - fogos/1000.0; //2000
		return n;
	}
	
	@Override
	public String toFile() {

		return getClass().getSimpleName().toLowerCase() + " " + getPosition().getX() + " " + getPosition().getY()
				+ " " + timer;
	}
}
