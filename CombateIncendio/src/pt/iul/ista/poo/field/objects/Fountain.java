package pt.iul.ista.poo.field.objects;

import pt.iul.ista.poo.field.Field;
import pt.iul.ista.poo.utils.Point2D;

public class Fountain extends FireFightObject implements Interactable {

	private enum poolState{
		HALF,COMPLETE
	}
	
	private poolState poolstate = poolState.HALF;
	private final static int woodNeeded = 20;

	public Fountain(Point2D p) {
		super(p);
		
	}

	@Override
	public int getLayer() {

		return 1;
	}
	
	public static int getWoodneeded() {
		return woodNeeded;
	}
	
	@Override
	public String getName() {
		
		switch (poolstate)
		{
		case HALF: return "fountain";
		case COMPLETE : return "pool";
		default : return null;
		}
	}
	

	@Override
	public void interact() {

		if (poolstate == poolState.COMPLETE)
			Field.getInstance().getF().addWater();

		else
		{
			if (Field.getInstance().getF().getWater()>=10)
			{
			poolstate = poolState.COMPLETE;
			Field.getInstance().getF().setWater(Field.getInstance().getF().getWater()-10);
			}
		}
	}

}
