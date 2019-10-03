package pt.iul.ista.poo.field.objects;

import java.util.Random;

import pt.iul.ista.poo.field.Field;
import pt.iul.ista.poo.field.objects.Land.LandState;
import pt.iul.ista.poo.utils.Point2D;

public abstract class Terrain extends FireFightObject implements Updatable, Interactable{

	private Land l;
	private int timer = 0;
	
	public Terrain(Point2D p, Land l) {
		super(p);
		this.l = l;
	}
    
	public void setL(Land l) {
		this.l = l;
	}

	public Land getL() {
		return l;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public int getTimer() {
		return timer;
	}

	@Override
	public abstract void interact(); 
	
	
	@Override
	public void update() {
	
	}
	
	@Override
	public int getLayer() {

		return 1;
	}
	
	public abstract boolean canBeSetOnFire ();
	
	public void spark (){

		Random r = new Random();
		if (r.nextDouble()<igniteProbability()) {

			if (getL().getState().equals(LandState.WATERED))
				getL().setState(LandState.HALFWATERED);
			else
			{

				if (getL().getState().equals(LandState.HALFWATERED))
					getL().setState(LandState.DRY);

				else
				{
					Fire f = new Fire (getPosition());
					Field.getInstance().addToInsert(f);
				}
			}
		}
	}

	public abstract double igniteProbability ();

	public void destroy() {

			timer++;
			if (timer > timeToBurn()) {
				Burnt b = new Burnt (getPosition(),getL());
				Field.getInstance().addToInsert(b);
				Field.getInstance().addToRemove(this);
			}
		}

	public abstract int timeToBurn();
	
	@Override
	public String toFile() {

		return getClass().getSimpleName().toLowerCase() + " " + getPosition().getX() + " " + getPosition().getY()
				+ " " + getTimer();
	}


}
