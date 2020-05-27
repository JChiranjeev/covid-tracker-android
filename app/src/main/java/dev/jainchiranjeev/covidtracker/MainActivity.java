package dev.jainchiranjeev.covidtracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import java.util.List;

import dev.jainchiranjeev.covidtracker.databinding.ActivityMainBinding;
import dev.jainchiranjeev.covidtracker.models.GlobalModel;
import dev.jainchiranjeev.covidtracker.models.ListItem;
import dev.jainchiranjeev.covidtracker.presentor.CovidAdapter;
import dev.jainchiranjeev.covidtracker.utils.Constants;
import dev.jainchiranjeev.covidtracker.utils.FilterValidator;
import dev.jainchiranjeev.covidtracker.utils.JSONParser;
import dev.jainchiranjeev.covidtracker.utils.PersistantStorage;
import dev.jainchiranjeev.covidtracker.viewmodels.CovidDataViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    CovidDataViewModel covidDataViewModel;
    ActivityMainBinding binding;
    List<ListItem> countriesList = null;
    CovidAdapter adapter;
    Boolean orderDescending = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getWindow().setBackgroundDrawableResource(R.drawable.background_gradient_main_activity);

        context = getApplicationContext();
        loadData();
        Handler handler = new Handler();
        int delay = 120*1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                handler.postDelayed(this, delay);
            }
        }, delay);

        binding.mbFilter.setOnClickListener(this);
        binding.mbSort.setOnClickListener(this);
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.search(binding.etSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void loadData() {
        String countryCode = getAndSaveLocation();
        covidDataViewModel = ViewModelProviders.of(this).get(CovidDataViewModel.class);
        covidDataViewModel.getCovidLiveData(this).observe(this, data -> {
            binding.mbFilter.setVisibility(View.VISIBLE);
            binding.mbSort.setVisibility(View.VISIBLE);
            if(orderDescending) {
                binding.mbSort.setText("Sort Asc");
            } else {
                binding.mbSort.setText("Sort Desc");
            }
            JSONParser parser = new JSONParser(context, data, countryCode);
            countriesList = parser.getCountriesList();
            GlobalModel globalModel = parser.getGlobalData();
            adapter = new CovidAdapter(countriesList, context);
            if(orderDescending) {
                adapter.sortList("ASC",countriesList);
            } else {
                adapter.sortList("DESC",countriesList);
            }
            binding.rvCovidList.setAdapter(adapter);
            binding.rvCovidList.setLayoutManager(new LinearLayoutManager(this));
            binding.tvTotalConfirmedCount.setText(String.valueOf(globalModel.getTotalConfirmed()));
            binding.tvTotalDeathsCount.setText(String.valueOf(globalModel.getTotalDeaths()));
            binding.tvTotalRecoveredCount.setText(String.valueOf(globalModel.getTotalRecovered()));
        });
    }

    private String getAndSaveLocation() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String countryCode = telephonyManager.getNetworkCountryIso();
        PersistantStorage persistantStorage = PersistantStorage.getInstance(context);
        persistantStorage.storeStringValue(Constants.USER_COUNTRY, countryCode);
        return countryCode;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.mb_filter:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Set Filters");
                final View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_filter, null);
                builder.setView(dialogLayout);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int casesMin, casesMax, deathsMin, deathsMax, recoveredMin, recoveredMax;
                        casesMin = Integer.parseInt(((AppCompatEditText)dialogLayout.findViewById(R.id.et_cases_min_count)).getText().toString());
                        casesMax = Integer.parseInt(((AppCompatEditText)dialogLayout.findViewById(R.id.et_cases_max_count)).getText().toString());
                        deathsMax = Integer.parseInt(((AppCompatEditText)dialogLayout.findViewById(R.id.et_deaths_max_count)).getText().toString());
                        deathsMin = Integer.parseInt(((AppCompatEditText)dialogLayout.findViewById(R.id.et_deaths_min_count)).getText().toString());
                        recoveredMax = Integer.parseInt(((AppCompatEditText)dialogLayout.findViewById(R.id.et_recovered_max_count)).getText().toString());
                        recoveredMin = Integer.parseInt(((AppCompatEditText)dialogLayout.findViewById(R.id.et_recovered_min_count)).getText().toString());
                        FilterValidator filterValidator = new FilterValidator(casesMin,
                                casesMax,
                                deathsMax,
                                deathsMin,
                                recoveredMax,
                                recoveredMin,
                                countriesList,
                                context
                        );
                        countriesList = filterValidator.getFilteredList();
                        adapter = new CovidAdapter(countriesList, context);
                        if(orderDescending) {
                            adapter.sortList("DESC",countriesList);
                        } else {
                            adapter.sortList("ASC",countriesList);
                        }
                        binding.rvCovidList.setAdapter(adapter);
                        binding.rvCovidList.setLayoutManager(new LinearLayoutManager(context));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.mb_sort:
                if(orderDescending) {
                    adapter.sortList("DESC",countriesList);
                    binding.mbSort.setText("Sort Desc");
                } else {
                    adapter.sortList("ASC",countriesList);
                    binding.mbSort.setText("Sort Asc");
                }
                orderDescending = !orderDescending;
                break;
        }
    }
}
