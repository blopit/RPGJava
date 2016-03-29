package core;

import java.util.ArrayList;

public class Room {
	public static ArrayList<Entity> objects; 
	
	public Room() {
		objects = new ArrayList<Entity>();
	}
	
	public void addEntity(Entity x) {
		objects.add(x);
		x.room = this;
	}
	
	public void removeEntity(Entity x) {
		if (objects.contains(x))
			objects.remove(x);
	}
	
}
