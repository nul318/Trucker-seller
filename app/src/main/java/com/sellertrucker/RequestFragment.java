package com.sellertrucker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class RequestFragment extends Fragment {
    private GoogleMap googleMap;
    MapView mMapView;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_request, container, false);


        ArrayList<Request> requests = new ArrayList<>();
        requests.add(new Request("서울시 서초구 양재2동", "오늘 오후 6시에 방문 가능하신가요!"));
        requests.add(new Request("서울시 서초구 잠원동", "에어컨 판매하고싶습니다!"));


        RequestAdpater requestAdpater = new RequestAdpater(requests, getContext());

        listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(requestAdpater);



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

    class RequestAdpater extends BaseAdapter{
        ArrayList<Request> data_list;
        Context con;
        LayoutInflater layoutInflater;

        RequestAdpater(ArrayList<Request> data_list, Context con){
            this.data_list = data_list;
            this.con = con;

            layoutInflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return data_list.size();
        }

        @Override
        public Request getItem(int position) {
            return data_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.request_item, parent, false);

            TextView address = (TextView) convertView.findViewById(R.id.address);
            TextView message = (TextView) convertView.findViewById(R.id.message);

            address.setText(data_list.get(position).address);
            message.setText(data_list.get(position).message);

            return convertView;
        }
    }


}

