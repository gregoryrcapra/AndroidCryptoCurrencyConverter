package com.example.cryptocurrencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    final Map<String, Map<String,Double>> exchangeMap = new HashMap<>();
    final Map<String, Double> usdMap = new HashMap<>();
    final Map<String, Double> btcMap = new HashMap<>();
    final Map<String, Double> ethMap = new HashMap<>();
    final Map<String, Double> ltcMap = new HashMap<>();


    final DecimalFormat df = new DecimalFormat("#.#####");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText cryptoText1 = (EditText) findViewById(R.id.cryptoVal1);
        final TextView cryptoText2 = (TextView) findViewById(R.id.cryptoVal2);

        final Spinner spinner1 = (Spinner) findViewById(R.id.currency1);
        final Spinner spinner2 = (Spinner) findViewById(R.id.currency2);

        spinner1.setSelection(0);
        spinner2.setSelection(1);

        exchangeMap.put("USD",usdMap);
        exchangeMap.put("BTC",btcMap);
        exchangeMap.put("ETH",ethMap);
        exchangeMap.put("LTC",ltcMap);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://min-api.cryptocompare.com/data/pricemulti?fsyms=USD,ETH,BTC,LTC&tsyms=USD,ETH,BTC,USD,LTC&api_key=3c94b6415351c9960af381a93814121400fc6f99649852992861bd0ede06fc61";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject usdObject = jsonObject.getJSONObject("USD");
                            Iterator<String> iter = usdObject.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                try {
                                    if (key.equals("USD")){
                                        continue;
                                    }
                                    usdMap.put(key,Double.parseDouble(usdObject.getString(key)));
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }
                            JSONObject btcObject = jsonObject.getJSONObject("BTC");
                            Iterator<String> iter2 = btcObject.keys();
                            while (iter2.hasNext()) {
                                String key = iter2.next();
                                try {
                                    if (key.equals("BTC")){
                                        continue;
                                    }
                                    btcMap.put(key,Double.parseDouble(btcObject.getString(key)));
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }
                            JSONObject ethObject = jsonObject.getJSONObject("ETH");
                            Iterator<String> iter3 = ethObject.keys();
                            while (iter3.hasNext()) {
                                String key = iter3.next();
                                try {
                                    if (key.equals("ETH")){
                                        continue;
                                    }
                                    ethMap.put(key,Double.parseDouble(ethObject.getString(key)));
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }
                            JSONObject ltcObject = jsonObject.getJSONObject("LTC");
                            Iterator<String> iter4 = ltcObject.keys();
                            while (iter4.hasNext()) {
                                String key = iter4.next();
                                try {
                                    if (key.equals("LTC")){
                                        continue;
                                    }
                                    ltcMap.put(key,Double.parseDouble(ltcObject.getString(key)));
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    throw new Exception("Request to Crypto Price API failed.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        queue.add(stringRequest);

        usdMap.put("BTC",.0003);
        usdMap.put("ETH",.0096);
        usdMap.put("LTC",.031);
        usdMap.put("USD",1.0);
        btcMap.put("USD",3369.81);
        btcMap.put("ETH",32.39);
        btcMap.put("LTC",103.05);
        btcMap.put("BTC",1.0);
        ethMap.put("BTC",.031);
        ethMap.put("USD",103.99);
        ethMap.put("LTC",3.18);
        ethMap.put("ETH",1.0);
        ltcMap.put("BTC",.0097);
        ltcMap.put("ETH",.31);
        ltcMap.put("USD",32.69);
        ltcMap.put("LTC",1.0);

        df.setRoundingMode(RoundingMode.CEILING);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String otherSelectedItem = spinner2.getSelectedItem().toString();

                if (selectedItem.matches("USD|BTC|ETH|LTC")) {
                    if (otherSelectedItem.matches("USD|BTC|ETH|LTC")) {
                        double exchangeRate = exchangeMap.get(selectedItem).get(otherSelectedItem);
                        if (!cryptoText1.getText().toString().isEmpty()){
                            double newVal = Double.parseDouble(cryptoText1.getText().toString())*exchangeRate;
                            cryptoText2.setText(String.valueOf(df.format(newVal)));
                        }
                    }
                }

                ImageView img= (ImageView) findViewById(R.id.cryptoIcon1);

                switch (selectedItem){
                    case "USD":
                        img.setImageResource(R.drawable.usd);
                        break;
                    case "ETH":
                        img.setImageResource(R.drawable.eth);
                        break;
                    case "LTC":
                        img.setImageResource(R.drawable.ltc);
                        break;
                    case "BTC":
                        img.setImageResource(R.drawable.btcz);
                        break;
                }

            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String otherSelectedItem = spinner1.getSelectedItem().toString();

                if (selectedItem.matches("USD|BTC|ETH|LTC")) {
                    if (otherSelectedItem.matches("USD|BTC|ETH|LTC")) {
                        double exchangeRate = exchangeMap.get(otherSelectedItem).get(selectedItem);
                        if (!cryptoText1.getText().toString().isEmpty()){
                            double newVal = Double.parseDouble(cryptoText1.getText().toString())*exchangeRate;
                            cryptoText2.setText(String.valueOf(df.format(newVal)));
                        }
                    }
                }

                ImageView img= (ImageView) findViewById(R.id.cryptoIcon2);

                switch (selectedItem){
                    case "USD":
                        img.setImageResource(R.drawable.usd);
                        break;
                    case "ETH":
                        img.setImageResource(R.drawable.eth);
                        break;
                    case "LTC":
                        img.setImageResource(R.drawable.ltc);
                        break;
                    case "BTC":
                        img.setImageResource(R.drawable.btcz);
                        break;
                }

            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        cryptoText1.addTextChangedListener(new TextWatcher() {
            boolean _ignore = false;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (_ignore)
                    return;
                _ignore = true;
                String selectedItem = spinner1.getSelectedItem().toString();
                String otherSelectedItem = spinner2.getSelectedItem().toString();

                if (selectedItem.matches("USD|BTC|ETH|LTC")) {
                    if (otherSelectedItem.matches("USD|BTC|ETH|LTC")) {
                        double exchangeRate = exchangeMap.get(selectedItem).get(otherSelectedItem);
                        if (!cryptoText1.getText().toString().isEmpty()){
                            double newVal = Double.parseDouble(cryptoText1.getText().toString())*exchangeRate;
                            cryptoText2.setText(String.valueOf(df.format(newVal)));
                        }
                        _ignore = false;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    public void swapCryptos(View view) {
        Spinner spinner1 = (Spinner) findViewById(R.id.currency1);
        Spinner spinner2 = (Spinner) findViewById(R.id.currency2);

        int temp = spinner1.getSelectedItemPosition();
        spinner1.setSelection(spinner2.getSelectedItemPosition());
        spinner2.setSelection(temp);

        ImageView img= (ImageView) findViewById(R.id.cryptoIcon1);

        switch (spinner1.getSelectedItem().toString()){
            case "USD":
                img.setImageResource(R.drawable.usd);
                break;
            case "ETH":
                img.setImageResource(R.drawable.eth);
                break;
            case "LTC":
                img.setImageResource(R.drawable.ltc);
                break;
            case "BTC":
                img.setImageResource(R.drawable.btcz);
                break;
        }

        ImageView img2= (ImageView) findViewById(R.id.cryptoIcon2);

        switch (spinner2.getSelectedItem().toString()){
            case "USD":
                img2.setImageResource(R.drawable.usd);
                break;
            case "ETH":
                img2.setImageResource(R.drawable.eth);
                break;
            case "LTC":
                img2.setImageResource(R.drawable.ltc);
                break;
            case "BTC":
                img2.setImageResource(R.drawable.btcz);
                break;
        }

    }

    public void refreshRates(View view) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://min-api.cryptocompare.com/data/pricemulti?fsyms=USD,ETH,BTC,LTC&tsyms=USD,ETH,BTC,USD,LTC&api_key=3c94b6415351c9960af381a93814121400fc6f99649852992861bd0ede06fc61";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject usdObject = jsonObject.getJSONObject("USD");
                            Iterator<String> iter = usdObject.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                try {
                                    if (key.equals("USD")){
                                        continue;
                                    }
                                    usdMap.put(key,Double.parseDouble(usdObject.getString(key)));
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }
                            JSONObject btcObject = jsonObject.getJSONObject("BTC");
                            Iterator<String> iter2 = btcObject.keys();
                            while (iter2.hasNext()) {
                                String key = iter2.next();
                                try {
                                    if (key.equals("BTC")){
                                        continue;
                                    }
                                    btcMap.put(key,Double.parseDouble(btcObject.getString(key)));
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }
                            JSONObject ethObject = jsonObject.getJSONObject("ETH");
                            Iterator<String> iter3 = ethObject.keys();
                            while (iter3.hasNext()) {
                                String key = iter3.next();
                                try {
                                    if (key.equals("ETH")){
                                        continue;
                                    }
                                    ethMap.put(key,Double.parseDouble(ethObject.getString(key)));
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }
                            JSONObject ltcObject = jsonObject.getJSONObject("LTC");
                            Iterator<String> iter4 = ltcObject.keys();
                            while (iter4.hasNext()) {
                                String key = iter4.next();
                                try {
                                    if (key.equals("LTC")){
                                        continue;
                                    }
                                    ltcMap.put(key,Double.parseDouble(ltcObject.getString(key)));
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    throw new Exception("Request to Crypto Price API failed.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        queue.add(stringRequest);

        final EditText cryptoText1 = (EditText) findViewById(R.id.cryptoVal1);
        final TextView cryptoText2 = (TextView) findViewById(R.id.cryptoVal2);

        final Spinner spinner1 = (Spinner) findViewById(R.id.currency1);
        final Spinner spinner2 = (Spinner) findViewById(R.id.currency2);

        String selectedItem = spinner1.getSelectedItem().toString();
        String otherSelectedItem = spinner2.getSelectedItem().toString();

        if (selectedItem.matches("USD|BTC|ETH|LTC")) {
            if (otherSelectedItem.matches("USD|BTC|ETH|LTC")) {
                double exchangeRate = exchangeMap.get(selectedItem).get(otherSelectedItem);
                if (!cryptoText1.getText().toString().isEmpty()){
                    double newVal = Double.parseDouble(cryptoText1.getText().toString())*exchangeRate;
                    cryptoText2.setText(String.valueOf(df.format(newVal)));
                }
            }
        }

        ImageView img= (ImageView) findViewById(R.id.cryptoIcon1);

        switch (spinner1.getSelectedItem().toString()){
            case "USD":
                img.setImageResource(R.drawable.usd);
                break;
            case "ETH":
                img.setImageResource(R.drawable.eth);
                break;
            case "LTC":
                img.setImageResource(R.drawable.ltc);
                break;
            case "BTC":
                img.setImageResource(R.drawable.btcz);
                break;
        }

        ImageView img2= (ImageView) findViewById(R.id.cryptoIcon2);

        switch (spinner2.getSelectedItem().toString()){
            case "USD":
                img2.setImageResource(R.drawable.usd);
                break;
            case "ETH":
                img2.setImageResource(R.drawable.eth);
                break;
            case "LTC":
                img2.setImageResource(R.drawable.ltc);
                break;
            case "BTC":
                img2.setImageResource(R.drawable.btcz);
                break;
        }
    }
}
