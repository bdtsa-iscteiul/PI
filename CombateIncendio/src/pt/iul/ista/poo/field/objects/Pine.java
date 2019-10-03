package pt.iul.ista.poo.field.objects;

import pt.iul.ista.poo.field.Field;
import pt.iul.ista.poo.field.objects.Land.LandState;
import pt.iul.ista.poo.utils.Point2D;

public class Pine extends Terrain{
	
	private static final int timeToBurn = 5;

	public Pine(Point2D p, Land l) {
		super(p,l);
	}

	@Override
	public String getName() {
		
		return "pine";
	}

	@Override
	public void interact() {

		if (getL().getState().equals(LandState.WATERED)  ){
		}
		else
		{
			if (Field.getInstance().getF().getWater()>0)
			{
				if (getTimer()!=0)
					setTimer(0);
				else
				{
					Field.getInstance().getF().subWater();
					getL().setState(LandState.WATERED);

				}
			}
		}
	}

	@Override
	public boolean canBeSetOnFire() {
		
		return Field.getInstance().fireAt(getPosition()) == null;
	}

	@Override
	public double igniteProbability() {
	
		int f = Field.getInstance().nFires();
		if (f < 5)
			return 0.055;
		if (f > 5 && f<10)
			return 0.040;
		if (f > 10 && f<20)
			return 0.03;
		else
		return 0.025;
	}

	@Override
	public int timeToBurn() {
		
		return timeToBurn;
	}
	
}
