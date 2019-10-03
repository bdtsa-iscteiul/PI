package pt.iul.ista.poo.field.objects;

import pt.iul.ista.poo.field.Field;
import pt.iul.ista.poo.utils.Point2D;

public class Land extends FireFightObject implements Interactable{

	public enum LandState {
		
		DRY , WATERED, HALFWATERED
	}
	
	private static final double terrain_prob = 0.9;
	private boolean planted = false;
	private LandState state = LandState.DRY;
	
	public Land(Point2D p) {
		super(p);
		
	}

	public void setState(LandState state) {
		this.state = state;
	}

	public LandState getState() {
		return state;
	}

	public static double getTerrainProb() {
		return terrain_prob;
	}

	public void setPlanted(boolean planted) {
		this.planted = planted;
	}

	public boolean isPlanted() {
		return planted;
	}
	

	@Override
	public void interact() {

		if (Field.getInstance().getF().getWood() >= Fountain.getWoodneeded() )
		{
			Fountain p = Field.getInstance().fountainAt(getPosition());
            Boolean l = Field.getInstance().lakeAt(getPosition());
            Boolean h = Field.getInstance().houseAt(getPosition());
			
			if (!isPlanted() && p == null && !l && !h)
			{
				Fountain f = new Fountain (getPosition());
				Field.getInstance().addToInsert(f);
				Field.getInstance().getF().setWood(0);
			}
		}
	}

	@Override
	public String getName() {

		switch (state)
		{
		case DRY : return "land";
		case WATERED : return "blueland1";
		case HALFWATERED : return "blueland";
		default: return null;
		}
		
	}
	
	@Override
	public String toFile() {

		return getClass().getSimpleName().toLowerCase() + " " + getPosition().getX() + " " + getPosition().getY()
				+ " " + isPlanted() + " " + getState() ;
	}
}
