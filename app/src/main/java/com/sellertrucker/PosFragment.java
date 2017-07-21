package com.sellertrucker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PosFragment extends Fragment {
    ListView listView;
    static ArrayList<Menu> menus;
    PosAdapter posAdapter;
    CashDialog cashDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_pos, container, false);


        menus = new ArrayList<>();



        posAdapter = new PosAdapter(menus, getContext());

        listView = (ListView) view.findViewById(R.id.menu_list);
        listView.setAdapter(posAdapter);


        view.findViewById(R.id.menu01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=true;
                for(Menu menu : menus){
                    if(menu.title.equals("메뉴1")){
                        menu.cnt++;
                        check=false;
                    }
                }
                if(check){
                    menus.add(new Menu("메뉴1", 1));
                }

                posAdapter.notifyDataSetChanged();
            }
        });
        view.findViewById(R.id.menu02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=true;
                for(Menu menu : menus){
                    if(menu.title.equals("메뉴2")){
                        menu.cnt++;
                        check=false;
                    }
                }
                if(check){
                    menus.add(new Menu("메뉴2", 1));
                }
                posAdapter.notifyDataSetChanged();
            }
        });
        view.findViewById(R.id.menu03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=true;
                for(Menu menu : menus){
                    if(menu.title.equals("메뉴3")){
                        menu.cnt++;
                        check=false;
                    }
                }
                if(check){
                    menus.add(new Menu("메뉴3", 1));
                }
                posAdapter.notifyDataSetChanged();
            }
        });
        view.findViewById(R.id.menu04).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=true;
                for(Menu menu : menus){
                    if(menu.title.equals("메뉴4")){
                        menu.cnt++;
                        check=false;
                    }
                }
                if(check){
                    menus.add(new Menu("메뉴4", 1));
                }
                posAdapter.notifyDataSetChanged();
            }
        });

        view.findViewById(R.id.barcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CaptureActivity.class));
            }

        });
        cashDialog = new CashDialog(getContext(), getActivity());
        view.findViewById(R.id.cash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cashDialog.show();
                menus.clear();
                posAdapter.notifyDataSetChanged();
            }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        posAdapter.notifyDataSetChanged();
    }

    class PosAdapter extends BaseAdapter {
        ArrayList<Menu> data_list;
        Context con;
        LayoutInflater layoutInflater;

        PosAdapter(ArrayList<Menu> data_list, Context con){
            this.data_list = data_list;
            this.con = con;

            layoutInflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return data_list.size();
        }

        @Override
        public Menu getItem(int position) {
            return data_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.pos_item, parent, false);

            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView cnt = (TextView) convertView.findViewById(R.id.cnt);

            title.setText(data_list.get(position).title);
            cnt.setText(String.valueOf(data_list.get(position).cnt));

            return convertView;
        }
    }

}

