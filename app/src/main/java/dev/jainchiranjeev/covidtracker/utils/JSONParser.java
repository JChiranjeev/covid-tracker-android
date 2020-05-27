package dev.jainchiranjeev.covidtracker.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.jainchiranjeev.covidtracker.models.GlobalModel;
import dev.jainchiranjeev.covidtracker.models.ListItem;
import dev.jainchiranjeev.covidtracker.models.OtherCountryModel;
import dev.jainchiranjeev.covidtracker.models.UserCountryModel;

public class JSONParser {
    Context context;
    String jsonData;
    String userCountryCode;

    public JSONParser(Context context, String jsonData, String userCountryCode) {
        this.context = context;
        this.jsonData = jsonData;
        this.userCountryCode = userCountryCode;
    }

    public List<ListItem> getCountriesList() {
        List<ListItem> countriesList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(jsonData);
            JSONArray countriesArray = object.getJSONArray("Countries");
            for (int i=0; i<countriesArray.length(); i++) {
                JSONObject country = countriesArray.getJSONObject(i);
                ListItem countryModel;
                if(country.getString("CountryCode").equalsIgnoreCase(userCountryCode)) {
                    countryModel = new UserCountryModel();
                    ((UserCountryModel) countryModel).setCountry(country.getString("Country"));
                    ((UserCountryModel) countryModel).setCountryCode(country.getString("CountryCode"));
                    ((UserCountryModel) countryModel).setNewConfirmed(Integer.parseInt(country.getString("NewConfirmed")));
                    ((UserCountryModel) countryModel).setTotalConfirmed(Integer.parseInt(country.getString("TotalConfirmed")));
                    ((UserCountryModel) countryModel).setNewDeaths(Integer.parseInt(country.getString("NewDeaths")));
                    ((UserCountryModel) countryModel).setTotalDeaths(Integer.parseInt(country.getString("TotalDeaths")));
                    ((UserCountryModel) countryModel).setNewRecovered(Integer.parseInt(country.getString("NewRecovered")));
                    ((UserCountryModel) countryModel).setTotalRecovered(Integer.parseInt(country.getString("TotalRecovered")));
                } else {
                    countryModel = new OtherCountryModel();
                    ((OtherCountryModel) countryModel).setCountry(country.getString("Country"));
                    ((OtherCountryModel) countryModel).setCountryCode(country.getString("CountryCode"));
                    ((OtherCountryModel) countryModel).setNewConfirmed(Integer.parseInt(country.getString("NewConfirmed")));
                    ((OtherCountryModel) countryModel).setTotalConfirmed(Integer.parseInt(country.getString("TotalConfirmed")));
                    ((OtherCountryModel) countryModel).setNewDeaths(Integer.parseInt(country.getString("NewDeaths")));
                    ((OtherCountryModel) countryModel).setTotalDeaths(Integer.parseInt(country.getString("TotalDeaths")));
                    ((OtherCountryModel) countryModel).setNewRecovered(Integer.parseInt(country.getString("NewRecovered")));
                    ((OtherCountryModel) countryModel).setTotalRecovered(Integer.parseInt(country.getString("TotalRecovered")));
                }
                countriesList.add(countryModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countriesList;
    }

    public GlobalModel getGlobalData() {
        GlobalModel model = new GlobalModel();
        try {
            JSONObject obj = new JSONObject(jsonData);
            JSONObject global = obj.getJSONObject("Global");
            model.setNewConfirmed(global.getInt("NewConfirmed"));
            model.setTotalConfirmed(global.getInt("TotalConfirmed"));
            model.setNewDeaths(global.getInt("NewDeaths"));
            model.setTotalDeaths(global.getInt("TotalDeaths"));
            model.setNewRecovered(global.getInt("NewRecovered"));
            model.setTotalRecovered(global.getInt("TotalRecovered"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }
}
