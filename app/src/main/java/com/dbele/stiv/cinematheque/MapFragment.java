package com.dbele.stiv.cinematheque;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dbele.stiv.model.Cinema;
import com.dbele.stiv.persistence.CinemaRepository;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private MapView mapView;
    private ArrayList<Cinema> cinemas;
    private Map<Marker, Cinema> markerImagesMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActivity().getActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(Boolean.TRUE);
        }
        initCinemas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        markerImagesMap = new HashMap<>();
        MapsInitializer.initialize(getActivity());
        if (checkGooglePlayServices(savedInstanceState, v)) {
            handleMap(savedInstanceState, v);
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
           mapView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    private void initCinemas() {
        cinemas = CinemaRepository.getCinemas(getActivity());
    }

    private boolean checkGooglePlayServices(Bundle savedInstanceState, View v) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode == ConnectionResult.SUCCESS) {
            return true;
        }
        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
            GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_RESOLUTION_REQUEST).show();
        } else {
            Toast.makeText(getActivity(), R.string.no_googleplayservices, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void handleMap(Bundle savedInstanceState, View v) {
        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        if(mapView!=null) {
            GoogleMap map = mapView.getMap();
            map.setMyLocationEnabled(true);
            for(Cinema cinema : cinemas) {
                Marker marker =  map.addMarker(new MarkerOptions()
                    .position(new LatLng(cinema.getLat(), cinema.getLng()))
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.map_cinema)));
                markerImagesMap.put(marker, cinema);
            }

            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v  = getActivity().getLayoutInflater().inflate(R.layout.cinema_infowindow, null);
                    Cinema cinema = markerImagesMap.get(marker);
                    ImageView cinemaMarkerIcon = (ImageView) v.findViewById(R.id.cinema_marker_icon);
                    TextView cinemaMarkerName = (TextView)v.findViewById(R.id.cinema_marker_name);
                    TextView cinemaMarkerAddress = (TextView)v.findViewById(R.id.cinema_marker_address);
                    TextView cinemaMarkerProperties = (TextView)v.findViewById(R.id.cinema_marker_properties);
                    cinemaMarkerIcon.setImageResource(getResources().getIdentifier(cinema.getPicture(), "drawable", getActivity().getPackageName()));
                    cinemaMarkerName.setText(cinema.getName());
                    cinemaMarkerAddress.setText(cinema.getAddress());
                    cinemaMarkerProperties.setText(cinema.getProperties());
                    return v;
                }
            });
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(45.81, 15.96), 12);
            map.animateCamera(cameraUpdate);
        }
    }

}
