package info3097.running_mate;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MainActivity extends MapActivity implements LocationListener {
	
	private LocationManager lm;
	private MapView mapView;
	private MapController mc;
	private ArrayList<GeoPoint> track;
	private TextView Lat;
	private TextView Lon;
	private TextView Spd;
	private TextView Alt;
	private TextView Brng;
	private TextView Dst;
	private TextView CB;
	private EditText weight;
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
        mapView = (MapView)findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(true);
        mapView.setTraffic(true);
        mc = mapView.getController();        
        Lat = (TextView)findViewById(R.id.textViewLat);
    	Lon = (TextView)findViewById(R.id.textViewLon);
    	Spd = (TextView)findViewById(R.id.textViewSpeed);
    	Alt = (TextView)findViewById(R.id.textViewAltitude);
    	Brng = (TextView)findViewById(R.id.textViewBarring);
    	Dst = (TextView)findViewById(R.id.textViewDistance);
    	CB = (TextView)findViewById(R.id.textCalories);
    	weight = (EditText)findViewById(R.id.editTextWeight);
    	
        //TODO: have the following in preserved in preferences
        distanceTraveled = 0.0F;
        calorieBurned = 0.0F;
    }
    
//	/* (non-Javadoc)
//	 * 
//	 * @see android.app.Activity#onCreateDialog(int)
//	 */
//	@Override
//	@Deprecated
//	protected Dialog onCreateDialog(int id) {
//		// TODO Auto-generated method stub
//		switch (id) {
//		// create About Dialog
//		case 0:
//			return new AlertDialog.Builder(this)
//					.setIcon(R.drawable.ic_launcher)
//					.setTitle("About")
//					.setMessage(
//							"Programmed by:\n Ryan Johnston \n Darren Maginnis \n "
//									+ " An App that tracks how far you run, updates every 30 seconds with new location.\n©2012")
//					.setPositiveButton("OK",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//
//								}
//							}).create();
//		}
//		return null;
//	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		return true;
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	public void onLocationChanged(Location location) {
		if(lastKnownLocation != null){
			distanceTraveled += lastKnownLocation.distanceTo(location);
		}
		//TODO: Calc caleries, animate to
		lastKnownLocation = location;	
		GeoPoint p = new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
		track.add(p);		
		mc.animateTo(p);
	    mc.setZoom(16); 
	    MapOverlay mo = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mo);
        
	    Lat.setText(R.string.Latitude);
	    Lat.setText(((String)Lat.getText()) + ": " + Location.convert(lastKnownLocation.getLatitude(),Location.FORMAT_SECONDS));
    	
	    Lon.setText(R.string.Longitude);
    	Lon.setText(((String)Lon.getText()) + ": " + Location.convert(lastKnownLocation.getLongitude(),Location.FORMAT_SECONDS));
    	
    	Spd.setText(R.string.Speed);
    	Spd.setText(((String)Spd.getText()) + ": " + String.valueOf(lastKnownLocation.getSpeed() + "m/s"));
    	
    	Alt.setText(R.string.Altitude);
    	Alt.setText(((String)Alt.getText()) + ": " + String.valueOf(lastKnownLocation.getAltitude() + "m"));
    	
    	Brng.setText(R.string.Baring);
    	Brng.setText(((String)Brng.getText()) + ": " + String.valueOf(lastKnownLocation.getBearing() + "°N"));
    	
    	Dst.setText(R.string.Distance);
    	Dst.setText(((String)Dst.getText()) + ": " + String.valueOf(distanceTraveled) + "m");
    	
    	CB.setText(R.string.CaloriesBurned);
    	Dst.setText(((String)CB.getText()) + ": " 
    	+ String.valueOf((distanceTraveled * Double.parseDouble(String.valueOf(weight.getText()))* 0.653))); //(yourDistance * yourWeight) * .653
    	
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
		
	    SharedPreferences appPrefs =
	    	getSharedPreferences("appPreferences", MODE_PRIVATE);
	    SharedPreferences.Editor editPrefs = appPrefs.edit();
	    	
	    editPrefs.putFloat("distance", distanceTraveled);
	    editPrefs.putFloat("calories", calorieBurned);
	    editPrefs.commit();		
		
		// Stop updates from GPS
		lm.removeUpdates(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		boolean enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// Check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to 
		// go to the settings
		if (!enabled) {
		  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		  startActivity(intent);
		} 
		
	    SharedPreferences appPrefs =		
	    getSharedPreferences("appPreferences", MODE_PRIVATE);
	    appPrefs.getFloat("distance", distanceTraveled);
	    appPrefs.getFloat("calories", calorieBurned);
		
		//Resume updates from GPS
		if(lastKnownLocation == null) {
			lastKnownLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		if(track == null){
			track = new ArrayList<GeoPoint>();
		}
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, this); //every 30 seconds
		// Add overlay over your google maps image
		MapOverlay mo = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mo);
        if(lastKnownLocation != null){
        	GeoPoint p = new GeoPoint((int)(lastKnownLocation.getLatitude()*1E6), (int)(lastKnownLocation.getLongitude()*1E6));
			mc.animateTo(p);
		    mc.setZoom(16); 
		    
		    Lat.setText(R.string.Latitude);
		    Lat.setText(((String)Lat.getText()) + ": " + Location.convert(lastKnownLocation.getLatitude(),Location.FORMAT_SECONDS));
	    	
		    Lon.setText(R.string.Longitude);
	    	Lon.setText(((String)Lon.getText()) + ": " + Location.convert(lastKnownLocation.getLongitude(),Location.FORMAT_SECONDS));
	    	
	    	Spd.setText(R.string.Speed);
	    	Spd.setText(((String)Spd.getText()) + ": " + String.valueOf(lastKnownLocation.getSpeed() + "m/s"));
	    	
	    	Alt.setText(R.string.Altitude);
	    	Alt.setText(((String)Alt.getText()) + ": " + String.valueOf(lastKnownLocation.getAltitude() + "m"));
	    	
	    	Brng.setText(R.string.Baring);
	    	Brng.setText(((String)Brng.getText()) + ": " + String.valueOf(lastKnownLocation.getBearing() + "°N"));
	    	
	    	Dst.setText(R.string.Distance);
	    	Dst.setText(((String)Dst.getText()) + ": " + String.valueOf(distanceTraveled) + "m");		    
        }
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	
	private class MapOverlay extends Overlay {

		/* (non-Javadoc)
		 * @see com.google.android.maps.Overlay#draw(android.graphics.Canvas, com.google.android.maps.MapView, boolean, long)
		 */
		@Override
		public boolean draw(Canvas canvas, MapView mapView,boolean shadow, long when) {
			super.draw(canvas,mapView,shadow);
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			Point lastPoint = null;
			// translate GeoPoints to screen points and draw to the overlay
			for (GeoPoint geoPoint : track) {
				Point p = new Point();
				mapView.getProjection().toPixels(geoPoint, p);
				if (lastPoint != null) {
					canvas.drawLine(lastPoint.x,lastPoint.y, p.x,p.y, paint);
				}
				lastPoint = p;
			}		
			return true;
	    	
		}	
		
	}
    
}
