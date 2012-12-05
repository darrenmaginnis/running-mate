package info3097.running_mate;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


/**
 * MapOverlay
 * @see com.google.android.maps.Overlay
 */
public class MapOverlay extends Overlay {

	private ArrayList<GeoPoint> track;
	
	
	public MapOverlay(ArrayList<GeoPoint> track) {
		super();
		this.track = track;
	}


	/* (non-Javadoc)
	 * @see com.google.android.maps.Overlay#draw(android.graphics.Canvas, com.google.android.maps.MapView, boolean, long)
	 */
	@Override
	public boolean draw(Canvas canvas, MapView mapView,boolean shadow, long when) {
		super.draw(canvas,mapView,shadow);
		ArrayList<Point> points = new ArrayList<Point>();
		// translate GeoPoints to screen points
		for (GeoPoint geoPoint : track) {
			Point p = new Point();
			mapView.getProjection().toPixels(geoPoint, p);
			points.add(p);
		}
		
		// draw to the overlay
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		Point lastPoint = null;
		for (Point point : points) {
			if (lastPoint != null) {
				canvas.drawLine(lastPoint.x,lastPoint.y, point.x,point.y, paint);
			}
			lastPoint = point;
		}
		
		return true;
    	
	}	
	
}