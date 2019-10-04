package pt.iul.ista.poo.field.objects;

import java.util.Random;

import pt.iul.ista.poo.utils.Point2D;

public class Fireman extends FireFightObject  {

	private static final int max_water = 50;
	private static final int  max_wood = 25;
	private double water = 10;
	private int wood = 0;

	public Fireman(Point2D p) {
		super(p);
	
	}
	

	public double getWater() {
		return water;
	}

	public void addWater() {
		
		Random r = new Random();
		water = water + 15 +  r.nextGaussian() * Math.sqrt(8);  // VA Continua , curva de gauss com media de 15 litros
		
		if (water > max_water)
		{
			water = max_water;
		}
	}
	
	public void subWater(){
		water--;
	}


	public void setWater(double water) {
		this.water = water;
	}

	@Override
	public String getName() {
		
		return "fireman";
	}

	@Override
	public int getLayer() {

		return 2;
	}

	@Override
	public String toFile() {

		return getClass().getSimpleName().toLowerCase() + " " + getPosition().getX() + " " + getPosition().getY()
				+ " " + getWater() + " " + getWood()   ;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}
	
	public void addWood() {
		if (wood < max_wood)
		wood++;
	}
}
