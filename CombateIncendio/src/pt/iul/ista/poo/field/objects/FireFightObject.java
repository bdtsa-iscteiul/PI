package pt.iul.ista.poo.field.objects;

import pt.iul.ista.poo.field.objects.Land.LandState;
import pt.iul.ista.poo.gui.ImageTile;
import pt.iul.ista.poo.utils.Point2D;

public class FireFightObject implements ImageTile {

	private Point2D position;
	
public FireFightObject (Point2D p)	{
	
	position = p;
}

public void setPosition(Point2D position) {
	this.position = position;
}


@Override
public String getName() {
	
	return null;
}

@Override
public Point2D getPosition() {

	return position;
}

@Override
public int getLayer() {

	return 0;
}

public String toFile() {

	return getClass().getSimpleName().toLowerCase() + " " + getPosition().getX() + " " + getPosition().getY();
}


public static FireFightObject newFireFightObject (String [] tokens)
{
	switch (tokens[0])
	{
	case "fireman":;Fireman f = new Fireman (new Point2D (Integer.valueOf(tokens[1]),Integer.valueOf(tokens[2])));
	f.setWater(Integer.valueOf(tokens[3]));
	f.setWood(Integer.valueOf(tokens[4]));
	return f;
	case "land": Land l = new Land(new Point2D (Integer.valueOf(tokens[1]),Integer.valueOf(tokens[2])));
	l.setPlanted(Boolean.valueOf(tokens[3]));
	l.setState(LandState.valueOf(tokens[4]));
	return l;
	case "explosion": Explosion e = new Explosion (new Point2D (Integer.valueOf(tokens[1]),Integer.valueOf(tokens[2])));
	e.setTimer(Integer.valueOf(tokens[3]));
	return e;
	case "lake": return new Lake (new Point2D (Integer.valueOf(tokens[1]),Integer.valueOf(tokens[2])));
	case "pine": Pine p = new Pine ((new Point2D (Integer.valueOf(tokens[1]),Integer.valueOf(tokens[2]))),
	new Land (new Point2D (Integer.valueOf(tokens[1]),Integer.valueOf(tokens[2]))));
	p.setTimer(Integer.valueOf(tokens[3]));
	return p;
	case "burnt": Burnt b = new Burnt ((new Point2D (Integer.valueOf(tokens[1]),Integer.valueOf(tokens[2]))),
			new Land (new Point2D (Integer.valueOf(tokens[1]),Integer.valueOf(tokens[2]))));
	b.setTimer(Integer.valueOf(tokens[3]));
	return b;
	case "fire": return new Fire (new Point2D (Integer.valueOf(tokens[1]),Integer.valueOf(tokens[2])));
	case "plane": Plane pl = new Plane (new Point2D (Integer.valueOf(tokens[1]),Integer.valueOf(tokens[2])));
	pl.setTimer(Integer.valueOf(tokens[3]));
	return pl;
	case "smoke": return new Smoke (new Point2D (Integer.valueOf(tokens[1]),Integer.valueOf(tokens[2])));
	case "house": return new House (new Point2D (Integer.valueOf(tokens[1]),Integer.valueOf(tokens[2])));
	default: throw new IllegalStateException();
	}
}
}

