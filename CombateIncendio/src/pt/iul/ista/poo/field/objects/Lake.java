package pt.iul.ista.poo.field.objects;

import pt.iul.ista.poo.field.Field;
import pt.iul.ista.poo.utils.Point2D;

public class Lake extends FireFightObject implements Interactable {

	public Lake(Point2D p) {
		super(p);
		
	}


	@Override
	public int getLayer() {

		return 1;
	}


	@Override
	public String getName() {

		return "lake";
	}


	@Override
	public void interact() {
		
		Field.getInstance().getF().addWater();
	}
	
	public static int lakesNumber(int dim)
	{
	return (int)(Math.cbrt(dim/25*dim/25));
	}


}
