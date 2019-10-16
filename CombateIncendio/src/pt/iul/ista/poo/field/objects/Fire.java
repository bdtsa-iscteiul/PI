package pt.iul.ista.poo.field.objects;

import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import pt.iul.ista.poo.field.Field;
import pt.iul.ista.poo.utils.Point2D;

public class Fire extends FireFightObject implements Updatable , Interactable {

	private int smokeTimer = 0;

	public Fire (Point2D p) {

		super(p);
	}


	@Override
	public String getName() {

		return "fire";
	}


	@Override
	public void update() {
		Object[] options = {"Restart", "Quit"};

		List <Terrain> terras = Field.getInstance().burnableTerrainsAroundPosition(getPosition());

		for (Terrain t : terras)
			t.spark();

		Terrain thisT = Field.getInstance().terrainAt(getPosition());
		thisT.destroy();

		if (Field.getInstance().houseNear(getPosition()))
		{
			Random r = new Random();
			if (r.nextDouble() < House.burningProb()) {
				int i = JOptionPane.showOptionDialog(null, "A casa ardeu", "InfoBox: " + "Game OVER", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options);
				//throw new IllegalStateException ("A casa ardeu");
				if(i == 0)
					Field.getInstance().restart();
				else
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


	public void setSmokeTimer(int smokeTimer) {
		this.smokeTimer = smokeTimer;
	}


	public double smokeProb() {               

		return 50.0*smokeTimer/4550.0;
	}

}
