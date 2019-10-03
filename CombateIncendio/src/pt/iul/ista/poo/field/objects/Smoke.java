package pt.iul.ista.poo.field.objects;

import pt.iul.ista.poo.field.Field;
import pt.iul.ista.poo.utils.Point2D;

public class Smoke extends FireFightObject implements Interactable {

	public Smoke(Point2D p) {
		super(p);
	
	}

	@Override
	public int getLayer() {

		return 3;
	}
	
	@Override
	public String getName() {
		
		return "smoke";
	}

	@Override
	public void interact() {

		if (Field.getInstance().getF().getWater()>0)
		{
			Field.getInstance().getF().subWater();
			Field.getInstance().addToRemove(this);
		}
	}

}
