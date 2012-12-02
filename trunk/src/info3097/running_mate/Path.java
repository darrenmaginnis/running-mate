/**
 * 
 */
package info3097.running_mate;

import java.util.ArrayList;
import java.util.Date;

import android.location.Location;

/**
 * 
 *
 */
public class Path {
	
	private ArrayList<Location> path;
	private Date Start;
	
	public Path(Location l){
		this.path = new ArrayList<Location>();
		this.path.add(l);
		this.Start = new Date(l.getTime());
	}
	
	public Path(ArrayList<Location> p){
		this.path = p;
		this.Start = new Date(p.get(0).getTime());
	}
	
	/**
	 * Adds a way point to the path
	 * @param l
	 */
	public void AddWayPoint(Location l) {
		this.path.add(l);
	}

	/**
	 * @return the path
	 */
	public ArrayList<Location> getLocations() {
		return path;
	}

	/**
	 * @return the start time of this path
	 */
	public Date getStart() {
		return Start;
	}
}
