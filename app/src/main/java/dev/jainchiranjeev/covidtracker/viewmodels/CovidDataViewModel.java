package dev.jainchiranjeev.covidtracker.viewmodels;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import dev.jainchiranjeev.covidtracker.utils.Constants;

public class CovidDataViewModel extends AndroidViewModel {
    CovidLiveData covidLiveData;
    public CovidDataViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getCovidLiveData(Context context) {
        covidLiveData = new CovidLiveData(context);
        return covidLiveData;
    }
}

class CovidLiveData extends LiveData<String> {
    private final Context context;

    CovidLiveData(Context context) {
        this.context = context;
        loadData();
    }

    private void loadData() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String fakeResponse = Constants.fakeResponse;
                String urlString = "https://api.covid19api.com/summary";
                StringBuffer response = new StringBuffer("");
                BufferedReader in = null;
                try {
                    URL url = new URL(urlString);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                        String line;
                        while((line = in.readLine()) != null) {
                            response.append(line);
                        }
                    }
//                    return response.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return fakeResponse;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                setValue(s);
            }
        }.execute();
    }
}
