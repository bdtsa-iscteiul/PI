package pt.iul.ista.poo.field;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import pt.iul.ista.poo.field.objects.Updatable;
import pt.iul.ista.poo.field.objects.Fire;
import pt.iul.ista.poo.field.objects.FireFightObject;
import pt.iul.ista.poo.field.objects.Fireman;
import pt.iul.ista.poo.field.objects.House;
import pt.iul.ista.poo.field.objects.Interactable;
import pt.iul.ista.poo.field.objects.Lake;
import pt.iul.ista.poo.field.objects.Land;
import pt.iul.ista.poo.field.objects.Land.LandState;
import pt.iul.ista.poo.field.objects.Pine;
import pt.iul.ista.poo.field.objects.Plane;
import pt.iul.ista.poo.field.objects.Fountain;
import pt.iul.ista.poo.field.objects.Smoke;
import pt.iul.ista.poo.field.objects.Terrain;
import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.gui.ImageTile;
import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;
import pt.iul.ista.poo.utils.Vector2D;

public class Field implements Observer {

	private static final int MIN_X = 5;
	private static final int MIN_Y = 5;
	private static final int key_space = 32;
	private static final int initial_fires = 5;
	private static final int timeLim = 1200;
	private static final int KEY_SAVE = 83;
	private static final int KEY_LOAD = 76;

	private static Field instance;
	private int max_x;
	private int max_y;
	private Fireman f;
	private List <FireFightObject> allObjects = new ArrayList <>();
	private List <FireFightObject> isToRemove = new ArrayList <> ();
	private List <FireFightObject> isToAdd = new ArrayList <> ();
	private int time;
	private int lastkey;
	private Set <Point2D> fires = new HashSet<>();
    private Set <Point2D> lakes = new HashSet<>();
	
	public Field (int x, int y)
	{
		if (x < MIN_X || x < MIN_Y)
			throw new IllegalArgumentException ("Dimensoes demasiado pequenas");
		
		max_x = x;
		max_y = y;
		
		instance = this;
		ImageMatrixGUI.setSize(max_x, max_y);
		loadscenario();
	}
	
	private void loadscenario() {
		
		for (int i = 0 ; i < max_y ; i++){
			for (int j = 0; j < max_x ; j++){
              allObjects.add(new Land(new Point2D (j,i)));
			}
		}
		Random r = new Random();
		for (FireFightObject l : allObjects)
		{
			if (l instanceof Land)
			{
				if (r.nextDouble()<Land.getTerrainProb()){
					addToInsert(new Pine(l.getPosition(),(Land)l));
					((Land)l).setPlanted(true);
				}
			}
		}
		f = new Fireman (new Point2D (0,0));
		allObjects.add(f);
		
		insertWithoutUpdate ();
		createFires();
		createLakes();
		createHouse();
		removeTrees();
		remove();
	
		List <ImageTile> images = new ArrayList <>();
		images.addAll(allObjects);
		ImageMatrixGUI.getInstance().addImages(images);
		ImageMatrixGUI.getInstance().update();
	}

	
	private void createHouse() {
		
		Random r = new Random ();
		Point2D p = null;
		
		while (p == null)
		{
			Point2D test = new Point2D (r.nextInt(max_x),r.nextInt(max_y));
			if (!lakeAt(test) && fireAt(test) == null)
			{
				boolean noNeighbourFire = true;
				List <Point2D> points = neighboursOf(test);
				for (Point2D point : points)
				{
					if (fireAt(point) != null)
						noNeighbourFire = false;
				}
				if (noNeighbourFire)
					p = test;
			}
		}
		allObjects.add(new House (p));
	}

	private void createLakes() {
	
		Random r = new Random ();

		while (lakes.size()!= Lake.lakesNumber(max_x*max_y)) {

			Point2D p = new Point2D (r.nextInt(max_x),r.nextInt(max_y));

				if (fireAt(p) == null && !p.equals(new Point2D(0,0)))
					lakes.add(p);
			}
	
		for (Point2D p1 : lakes)
		{
			Lake l = new Lake(p1);
			allObjects.add(l);
		}
	}
		
	

	public Fireman getF() {
		return f;
	}

	private void removeTrees() {
		
		for (FireFightObject f : allObjects)
		{
			if (f instanceof Terrain && (lakeAt(f.getPosition()) || houseAt(f.getPosition())))
					addToRemove(f);
		}
	}
	
	public boolean lakeAt(Point2D position) {

		for (FireFightObject f : allObjects)
			if (f instanceof Lake && f.getPosition().equals(position))
				return true;

		return false;
	}

	public boolean houseAt(Point2D position) {

		for (FireFightObject f : allObjects)
			if (f instanceof House && f.getPosition().equals(position))
				return true;

		return false;
	}
	
	private void createFires() {

		Random r = new Random ();

		while (fires.size()!= initial_fires) {

			Point2D p = new Point2D (r.nextInt(max_x),r.nextInt(max_y));

			for (FireFightObject fo : allObjects)
				if (fo instanceof Terrain && ((Terrain)fo).getPosition().equals(p) && !p.equals(new Point2D(0,0)))
					fires.add(p);
	
		}
		for (Point2D p1 : fires)
		{
			Fire f = new Fire(p1);
			allObjects.add(f);
		}
	}
	
	
	public int getMax_x() {
		return max_x;
	}
	public int getMax_y() {
		return max_y;
	}

	public void addToInsert (FireFightObject f)
	{
		isToAdd.add(f);
	}
	
	public void insertWithoutUpdate ()
	{
		allObjects.addAll(isToAdd);
		isToAdd.clear();
	}
	
	public void insert ()
	{
		allObjects.addAll(isToAdd);
		for (FireFightObject f : isToAdd)
		ImageMatrixGUI.getInstance().addImage((ImageTile)f);
			
		isToAdd.clear();
	}


	public void addToRemove (FireFightObject f)
	{
		isToRemove.add(f);
	}
	
	public void remove ()
	{
		allObjects.removeAll(isToRemove);
		for (FireFightObject f : isToRemove)
		ImageMatrixGUI.getInstance().removeImage((ImageTile)f);
			
		isToRemove.clear();
	}
	
	
	private void save ()
	{
			Scanner s = new Scanner(System.in);
			
			int i = 0;
			while (i < 1 || i > 10)
			{
				System.out.println("Insira o slot em que pretende gravar,de 1 a 10");
				i = s.nextInt();
			}
			String l = String.valueOf(i);
			
			try {
			PrintWriter pw = new PrintWriter (new File (l));

			pw.println(max_x + " " + max_y + " " + time);
			
			for (FireFightObject fo : allObjects)
			{
				pw.println(fo.toFile());
			} 
			pw.close();
		} 
		catch (FileNotFoundException e) {

		}
	}
	
	private void load ()
	{

		Scanner s = new Scanner(System.in);

		int i = 0;
		while (i < 1 || i > 10)
		{
			System.out.println("Insira o slot que pretende abrir, de 1 a 10");
			i = s.nextInt();
		}
		String l = String.valueOf(i);

		try {
			s = new Scanner (new File (l));
			
		} catch (FileNotFoundException e) {
	
			e.printStackTrace();
		}
		
		String line = s.nextLine();
		String [] tokens = line.split(" ");
		

			time = (Integer.valueOf(tokens[2]));  
			
			for (FireFightObject fo: allObjects)                        
			{
				addToRemove(fo);
			}
			remove();

			while(s.hasNextLine())
			{
				line = s.nextLine();
			    tokens = line.split(" ");
				FireFightObject fo = FireFightObject.newFireFightObject (tokens);
				isToAdd.add(fo);  
			}
			for (FireFightObject fo: isToAdd)
			{
						if (fo instanceof Terrain)                      
						assignLand((Terrain)fo);

				if (fo instanceof Fireman)                        
					f = (Fireman)fo;
			}
			
			insert();
	}
	
	public void assignLand(Terrain t)
	{
		for (FireFightObject f: isToAdd)
		{
			if (f.getPosition().equals(t.getPosition()) && f instanceof Land)
			{
				t.setL((Land)f);
			}
		}
	}
	

	@Override
	public void update(Observable arg0, Object a) {

		if (a instanceof Integer)
		{
			int key = (Integer)a;
			if (key == KEY_SAVE)                          
				save();
	
			if (key == KEY_LOAD)  
				load();
			if (Direction.isDirection(key)) {

				time++;
				Direction d = Direction.directionFor (key);
				Vector2D v = d.asVector();
				Point2D p = f.getPosition().plus(v);

				if (lastkey != key_space)
				{
					if (inBounds(p) && fireAt(p) == null && !lakeAt(p) && !houseAt(p) && fountainAt(p) == null)
						f.setPosition(p);
					for (FireFightObject obj : allObjects)

						if (obj instanceof Updatable)
							((Updatable)obj).update();
				}
				else
				{
				List <FireFightObject> interact = listaSpot (p);
				for (FireFightObject fo : interact)
					((Interactable)fo).interact();

					for (FireFightObject obj : allObjects)

						if (obj instanceof Updatable && obj.getPosition() != p)
							((Updatable)obj).update();
				}

				randomPlane();
				createSmokes();
				removeSmokes();
				randomFire();
			}
            
			lastkey = key;
			insert ();         
			remove();
		}
		ImageMatrixGUI.getInstance().setStatusMessage( "  Water: " + f.getWater() + " : 15           "
				+ "       " +  "Wood : " + f.getWood() + "                      " +       "Time: " + time + " : " + timeLim +
				"                                 Fires: " + nFires() + " : " + (int)(max_x*max_y*0.3));
		ImageMatrixGUI.getInstance().update();
		
		checkStatus();
	}
		
	


	private void checkStatus() {
		
		if (nFires() >= (int)(max_x*max_y*0.3))
			throw new IllegalStateException ("Perdeu! Limite de fogos excedidos!");
		
		if (time >= timeLim)
			throw new IllegalStateException ("Perdeu! Limite de tempo excedido!");
		
		if (nFires() == 0)
			throw new IllegalStateException ("Parabéns, ganhou!");
	
	}
	

	public Fountain fountainAt (Point2D p)
	{
		for (FireFightObject f : allObjects)
		{
			if (f instanceof Fountain && f.getPosition().equals(p))
				return (Fountain)f;
		}
		return null;
	}
	
	
	private void randomFire() {

		if (time % 40 == 0)
		{
			Random r = new Random();
			Fire f = null;
			while (f == null)
			{
				Point2D p = new Point2D (r.nextInt(max_x),r.nextInt(max_y));
				if (terrainAt(p)!= null && fireAt(p)==null && terrainAt(p).getL().getState().equals(LandState.DRY))
					f = new Fire (p);
			}
			addToInsert(f);
		}
	}


	private void createSmokes() {

		List <Fire> fires = fireList();
		for (Fire f : fires)
		{
			Point2D p = f.getPosition();
			if (smokeAtPosition(p)==null)
			{
				List <Point2D> points = neighboursOf(p);
				if (points.size() >= 3)
				{
					int c = 0;
					for (Point2D p1 : points)
					{
						if (fireAt(p1)!= null)
							c++;
					}
					if (c >= 3)
					{
						Smoke s = new Smoke (p);
						addToInsert(s);
					}
				}
			}
		}
	}

	private void removeSmokes() {

		List <Smoke> smokes = new ArrayList <>();
		for (FireFightObject f : allObjects)
		{
			if (f instanceof Smoke)
				smokes.add((Smoke)f);
		}

		for (Smoke s : smokes)
		{
			if (fireAt(s.getPosition()) == null)
				addToRemove(s);

			List <Point2D> points = neighboursOf(s.getPosition());
			if (points.size() >= 3)
			{
				int c = 0;
				for (Point2D p1 : points)
				{
					if (fireAt(p1)!= null)
						c++;
				}
				if (c < 3)
				{
					addToRemove(s);
				}
			}
		}
	}

	
	private List<Fire> fireList() {
	
		List <Fire> fires = new ArrayList <>();
		for (FireFightObject f : allObjects)
		{
			if (f instanceof Fire)
				fires.add((Fire)f);
		}
		return fires;
	}

	public Smoke smokeAtPosition(Point2D p) {

		for (FireFightObject f : allObjects)
		{
			if (f instanceof Smoke && f.getPosition().equals(p))
			{
				return (Smoke)f;
			}
		}
		return null;
	}

	
	private void randomPlane() {
		
		Random r = new Random ();

		for (int i = 0; i < max_x; i++)
		{
			if (planeAtRow (i) == null)
			{
				if (r.nextDouble()<Plane.planeProb(nFires()))
				{
					Plane p = new Plane (new Point2D(i,max_y-r.nextInt(3)));
					addToInsert(p);
				}
			}
		}
	}

	
	public int nFires() {
		
		int c = 0;
		for (FireFightObject f : allObjects)
		{
			if (f instanceof Fire)
			{
				c++;
			}
		}
		return c;
	}


	private Plane planeAtRow (int x)
	{
		for (FireFightObject f : allObjects)
		{
			if ( f instanceof Plane && f.getPosition().getX() == x)
				return (Plane)f;
		}
		return null;
	}


	public boolean inBounds (Point2D p)
	{
		return ImageMatrixGUI.getInstance().isWithinBounds(p);
	}
	
	
	private List <FireFightObject> listaSpot (Point2D p)     
	{
		List <FireFightObject> objects = new ArrayList <> ();
		for (FireFightObject f: allObjects)
		{
			if (f instanceof Interactable && f.getPosition().equals(p))
			{
				objects.add(f);
			}
		}
		return objects;
	}
	
	private List <Point2D> neighboursOf (Point2D p) {
		
		List <Point2D> result = new ArrayList <>();
		List <Point2D> points = Direction.getNeighbourhoodPoints_2 (p);
		for (Point2D p1 : points)
		{
			if (inBounds(p1))
				result.add(p1);
		}
		return result;
	}
	
	public Terrain terrainAt (Point2D p) {
		
		for (FireFightObject f : allObjects) {
			if (f instanceof Terrain && f.getPosition().equals(p))
				return (Terrain)f;
		}
			return null;
	}
	
	public List<Terrain> burnableTerrainsAroundPosition (Point2D p) {
		
		List <Terrain> burnables = new ArrayList <> ();
		List <Point2D> points = neighboursOf (p);
		
		for (Point2D p1 : points)
		{
			Terrain t = terrainAt(p1);
			if (t != null)
			{
				if (t.canBeSetOnFire() && !t.getPosition().equals(f.getPosition()))
					burnables.add(t);
			}
		}
		return burnables;
	}
	
	public Fire fireAt (Point2D p){
		
		for (FireFightObject f : allObjects)
		{
		if (f instanceof Fire && f.getPosition().equals(p))
			return (Fire)f;
		}
		return null;
	}
	
	public void extinguish (Point2D p)
	{
		Fire f = fireAt (p);
		
		if (f != null)
		{
			addToRemove(f);
		}
	}
	
	public boolean houseNear (Point2D p)
	{
		List <Point2D> points = neighboursOf (p);
		for (Point2D p1 : points)
		{
			if (houseAt(p1))
				return true;
		}
		return false;
	}
	

	private void play() {
		ImageMatrixGUI.getInstance().addObserver(this);
		ImageMatrixGUI.getInstance().go();
	}

	public static Field getInstance() {
		assert (instance != null);
		return instance;
	}
	
	public static void main(String[] args) {
		Field f = new Field(22,12);
		f.play();
	}
	
}
