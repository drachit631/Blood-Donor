package com.example.blooddonorapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MakeNewRequest extends Fragment {
    Spinner sp_state, sp_city, sp_hosp;
    Context context;
    ArrayList<String> state_array, city_array, hospital_array;
    ArrayAdapter<String> state_adapter;
    ArrayAdapter<String> city_adapter;
    ArrayAdapter<String> hosp_adapter;
    ArrayList<HashMap<String, String>> datalist, citydatalist, hospitaldatalist;
    final httphandler states = new httphandler();
    private String TAG = MainActivity.class.getSimpleName();
    int position=0;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_req, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar_MakeNewReq);
        context = getActivity();
        sp_state = view.findViewById(R.id.state_s);
        sp_city = view.findViewById(R.id.city_s);
        sp_hosp = view.findViewById(R.id.hosp_s);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new getstatestask().execute();
        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position=i;
                new getcitytask().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                position=i;
                new gethospitalstask().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    class getstatestask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... arg0) {
            datalist = new ArrayList<HashMap<String, String>>();
            citydatalist = new ArrayList<HashMap<String, String>>();
            hospitaldatalist = new ArrayList<HashMap<String, String>>();
            state_array = new ArrayList<>();
            city_array = new ArrayList<>();
            hospital_array = new ArrayList<>();
            String jsonStr = null;

            jsonStr = states.getstates("https://blood-donor.herokuapp.com/objects/getstates.php");
            Log.e(TAG, jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e(TAG, String.valueOf(jsonObj));
                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("states_data");
                    // looping through All Contacts

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        String name = c.getString("name");
                        String id = c.getString("id");
                        HashMap<String, String> states_data = new HashMap<>();
                        state_array.add(name);
                        // adding each child node to HashMap key => value
                        states_data.put("name", name);
                        states_data.put("stateid", "" + id);
                        // adding contact to contact list
                        datalist.add(states_data);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });

            }
            state_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, state_array);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sp_state.setAdapter(state_adapter);
                }
            });
            return null;
        }
    }

    class getcitytask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... arg0) {
            HashMap<String, String> temp = new HashMap<String, String>();
            temp = datalist.get(position);
            String sid = temp.get("stateid");
            String jsonString = "{\r\n    \"stateid\": \"" + sid + "\"}";
            String citystr = states.getcity("https://blood-donor.herokuapp.com/objects/getcity.php", jsonString);
            if (citystr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(citystr);
                    Log.e(TAG, String.valueOf(jsonObj));
                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("city_data");
                    // looping through All Contacts
                    for (int j = 0; j < data.length(); j++) {
                        JSONObject c = data.getJSONObject(j);
                        String cityid = c.getString("cityid");
                        String sid1 = c.getString("stateid");
                        String cityname = c.getString("name");
                        HashMap<String, String> city_data = new HashMap<>();
                        city_array.add(cityname);
                        // adding each child node to HashMap key => value
                        city_data.put("name", cityname);
                        city_data.put("cityid", "" + cityid);
                        city_data.put("stateid", "" + sid1);
                        // adding contact to contact list
                        citydatalist.add(city_data);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });

            }
            city_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, city_array);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sp_city.setAdapter(city_adapter);
                }
            });
            return null;
        }
    }
    class gethospitalstask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... arg0) {
            HashMap<String, String> temp = new HashMap<String, String>();
            temp = citydatalist.get(position);
            String sid = temp.get("stateid");
            String stateid = "stateid";
            String cid = temp.get("cityid");
            String cityid = "cityid";
            String jsonInputString = "{" + stateid + ": " + sid + ",\n" + cityid + ": " + cid + "}";
            String citystr = states.gethospitals("https://blood-donor.herokuapp.com/objects/gethospitals.php", jsonInputString);
            if (citystr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(citystr);
                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("");
                    // looping through All Contacts
                    for (int j = 0; j < data.length(); j++) {
                        JSONObject c = data.getJSONObject(j);
                        String hospitalid = c.getString("hospitalid");
                        String hospitalname = c.getString("name");
                        HashMap<String, String> hospital_data = new HashMap<>();
                        hospital_array.add(hospitalname);
                        // adding each child node to HashMap key => value
                        hospital_data.put("name", hospitalname);
                        hospital_data.put("cityid", "" + hospitalid);
                        hospital_data.put("stateid", "" + sid);
                        hospital_data.put("city", "" + cid);
                        // adding contact to contact list
                        hospitaldatalist.add(hospital_data);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });

            }
            hosp_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, hospital_array);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sp_hosp.setAdapter(hosp_adapter);
                }
            });
            return null;
        }
    }
}
