package pt.iul.ista.poo.field.objects;

import pt.iul.ista.poo.field.Field;
import pt.iul.ista.poo.utils.Point2D;

public class Burnt extends Terrain {

	public Burnt(Point2D p, Land l) {
		super(p,l);
		
	}
	
	@Override
	public String getName() {
		
		return "burnt";
	}

	@Override
	public void interact() {

		if (Field.getInstance().fireAt(getPosition()) == null)
			Field.getInstance().getF().addWood();	

	}

	@Override
	public boolean canBeSetOnFire() {
		
		return false;
	}

	@Override
	public double igniteProbability() {
		return 0;
	}

	@Override
	public int timeToBurn() {
		
		return 0;
	}
	
	@Override
	public void update() {

		setTimer(getTimer()+1);
		if (getTimer() == 10)
		{
			Pine p = new Pine (getPosition(), getL());
			Field.getInstance().addToInsert(p);
			Field.getInstance().addToRemove(this);
		}
	}

	
}
