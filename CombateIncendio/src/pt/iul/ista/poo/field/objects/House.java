package pt.iul.ista.poo.field.objects;

import pt.iul.ista.poo.utils.Point2D;

public class House extends FireFightObject {

	private static final double burningProb = 0.2;

	public House(Point2D p) {
		super(p);
		
	}

	@Override
	public int getLayer() {

		return 1;
	}

	@Override
	public String getName() {

		return "house";
	}

	public static double burningProb() {
		
		return burningProb;
	}

	
}
