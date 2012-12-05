package info3097.running_mate;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity implements LocationListener {
	
	private LocationManager lm;
	/* 
	  	lastKnownLocation
	  	.distanceTo(Location dest) Returns the approximate distance in meters between this location and the given location.
		.getAltitude() Get the altitude if available, in meters above sea level.
		.getBearing() Get the bearing, in degrees.
		.getLatitude() Get the latitude, in degrees.
		.getLongitude() Get the longitude, in degrees.
		.getSpeed() Get the speed if it is available, in meters/second over ground.
		
	 */
	private Location lastKnownLocation;
	private float distanceTraveled;
	private float calorieBurned; // http://www.healthstatus.com/calculate/cbc

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setup and get the GPS started
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //TODO: have the following in preserved in preferences
        distanceTraveled = 0.0F;
        calorieBurned = 0.0F;
//        lastKnownLocation
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	public void onLocationChanged(Location location) {
		distanceTraveled += lastKnownLocation.distanceTo(location);
		//TODO: Calc caleries
		lastKnownLocation = location;
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	public void onProviderDisabled(String provider) {
		Log.e("GPS", "provider disabled " + provider);
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	public void onProviderEnabled(String provider) {
		Log.e("GPS", "provider enabled " + provider);
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 */
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.e("GPS", "status changed to " + provider + " [" + status + "]");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		// Stop updates from GPS
		lm.removeUpdates(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		//Resume updates from GPS
		if(lastKnownLocation == null) {
			lastKnownLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}        
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, this); //every 30 seconds
	}
    
}
