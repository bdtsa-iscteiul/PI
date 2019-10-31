package pt.iul.ista.poo.field.objects;

import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import pt.iul.ista.poo.field.Field;
import pt.iul.ista.poo.utils.Point2D;

public class Fire extends FireFightObject implements Updatable , Interactable {

	private int smokeTimer = 0;
	private int n = 0;
	
	public Fire (Point2D p) {

		super(p);
	}


	@Override
	public String getName() {

		return "fire";
	}


	@Override
	public void update() {

		List <Terrain> terras = Field.getInstance().burnableTerrainsAroundPosition(getPosition());

		for (Terrain t : terras)
			t.spark();

		Terrain thisT = Field.getInstance().terrainAt(getPosition());
		thisT.destroy();

		if (Field.getInstance().houseNear(getPosition()))
		{
			Random r = new Random();
			if (r.nextDouble() < House.burningProb()) {
				JOptionPane.showMessageDialog(null,"A casa ardeu! GAME OVER!");
				//throw new IllegalStateException ("A casa ardeu");
				Field.getInstance().quit();
			}
		}

	}


	@Override
	public int getLayer() {

		return 2;
	}


	@Override
	public void interact() {

		if (Field.getInstance().getF().getWater()>0)
		{
			Field.getInstance().getF().subWater();
			Field.getInstance().addToRemove(this);
		}
	}


	public int getSmokeTimer() {
		return smokeTimer;
	}


	public void setSmokeTimer() {
		
		System.out.println("smokeTimer = " + smokeTimer);
		setN(getN() + 1);
		smokeTimer = smokeTimer + getN();

	}


	public double smokeProb() {               

		return smokeTimer/55.0;
	}


	public int getN() {
		return n;
	}


	public void setN(int n) {
		this.n = n;
	}

}
