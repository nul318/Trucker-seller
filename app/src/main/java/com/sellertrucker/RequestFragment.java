package com.sellertrucker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RequestFragment extends Fragment {
    private GoogleMap googleMap;
    MapView mMapView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_request, container, false);

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                // For dropping a marker at a point on the Map
                LatLng sk_telecoms = new LatLng(37.566456, 126.985045);
                LatLng 을지로입구역 = new LatLng(37.566056, 126.982980);

//                googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

                googleMap.addMarker(new MarkerOptions()
                        .position(sk_telecoms)
//                        .title("신전 떡볶이")
//                        .snippet("서울시 서초구 양재동 언남중학교 사거리")
                        .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("request_icon", 250, 250)))
                );

                googleMap.addMarker(new MarkerOptions()
                        .position(을지로입구역)
//                        .title("치킨 트럭")
//                        .snippet("서울시 서초구 양재동 언남중학교 사거리")
                        .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("request_icon2", 250, 250)))
                );

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(을지로입구역).zoom(15).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                cameraPosition = new CameraPosition.Builder().target(sk_telecoms).zoom(16).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return view;
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", "com.sellertrucker"));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }




}

