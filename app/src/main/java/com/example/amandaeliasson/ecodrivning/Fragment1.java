package com.example.amandaeliasson.ecodrivning;



import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by amandaeliasson on 2018-01-23.
 */

public class Fragment1 extends Fragment implements
        OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private GoogleMap mMap;
    private DataProviderMockup dataprovider;
    private Marker mark1;
    private Marker mark2;
    private Context thiscontext;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataprovider = new DataProviderMockup();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment1, container, false);
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        thiscontext = container.getContext();
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 13.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.612808, 13.003448), zoomLevel));
        // Add a marker
        List<Measurement> list = dataprovider.getData();
        for(Measurement m: list){
            LatLng coordinate = new LatLng(m.getCoordinate1(), m.getCoordinate2());
            String s = Double.toString(m.getCoordinate1());
            String s2 = Double.toString(m.getCoordinate2());
            Marker mark = mMap.addMarker(new MarkerOptions().position(coordinate).title(s + ", " + " " + s2));
            mark.setTag(0);
            mMap.setOnMapLongClickListener(this);
            markerColor(mark, m.goodValue());
        }
    }

    public void markerColor(Marker mark, boolean b){
        if(b){
            mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }else{
            mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
    }
    public boolean onMarkerClick(final Marker marker) {
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            String s = marker.getTitle() +" has been clicked " + clickCount + " times.";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(thiscontext,s, duration).show();
            return true;
        }
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}
