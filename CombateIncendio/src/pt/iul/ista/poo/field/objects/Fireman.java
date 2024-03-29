package pt.iul.ista.poo.field.objects;

import java.util.Random;

import pt.iul.ista.poo.utils.Point2D;

public class Fireman extends FireFightObject  {

	private static final int max_water = 50;
	private static final int  max_wood = 25;
	private double water = 10;
	private int wood = 0;
	private String name;
	public Fireman(Point2D p) {
		super(p);
		name="fireman";
	}
	

	public double getWater() {
		return water;
	}

	public void addWater() {
		// VA Continua , curva de gauss com media de 15 litros
		Random r = new Random();       

		double x = 0;
		int media = 15;
		int sigma = 5;

		while (x<= 0 || x>=30) {

			x = media + sigma* Math.sqrt(-2 * Math.log(r.nextDouble()))* Math.cos(2*Math.PI*r.nextDouble());
		}
		water += x;


		if (water > max_water)
		{
			water = max_water;
		}
	}
	
	public void subWater(){
		
		//Random r = new Random();
		//water = water - 2 - r.nextGaussian() * Math.sqrt(1);  // VA Continua , curva de gauss com media de 2 litros
		water = water-cosine(0,4);
		if (water < 0)
			water = 0;
	}
	

	public void setWater(double water) {
		this.water = water;
		
		Random r = new Random();
		double a = r.nextDouble()*4;
		double x = -0.09375*(a*a)+0.375*a;
		water =- x;
	}
	public void changeImage() {
		if(getWater()!=0)
			name="fireman1";
	}
	public void changeImageDefault() {
		name="fireman";
	}
	@Override
	public String getName() {
		
		return name;
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
	
	public double cosine( double xMin, double xMax ){
		Random r= new Random();
		//U(-1,1)
		double g =r.nextDouble()*2-1;
		assert( xMin < xMax );
		double a = 0.5 * ( xMin + xMax ); // location parameter
		double b = ( xMax - xMin ) / Math.PI; // scale parameter
		return a + b * Math.asin(g);
	}
	
}
