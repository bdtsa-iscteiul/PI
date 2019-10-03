package pt.iul.ista.poo.field.objects;

import pt.iul.ista.poo.field.Field;
import pt.iul.ista.poo.utils.Point2D;

public class Explosion extends FireFightObject implements Updatable {

	private int timer = 0;
	
	public Explosion(Point2D p) {
		super(p);
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	@Override
	public int getLayer() {

		return 3;
	}

	@Override
	public String getName() {

		return "explosion";
	}

	@Override
	public void update() {
	
		timer++;
		if (timer > 1)
			Field.getInstance().addToRemove(this);
	}
	
	@Override
	public String toFile() {

		return getClass().getSimpleName().toLowerCase() + " " + getPosition().getX() + " " + getPosition().getY()
				+ " " + timer;
	}

}
