package dev.jainchiranjeev.covidtracker.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import dev.jainchiranjeev.covidtracker.models.ListItem;
import dev.jainchiranjeev.covidtracker.models.OtherCountryModel;

public class FilterValidator {
    Integer totalCaseMin, totalCaseMax, totalDeathsMin, totalDeathsMax, totalRecoveredMin, totalRecoveredMax;
    List<ListItem> countriesList;
    Context context;

    public FilterValidator(Integer totalCaseMin,
                           Integer totalCaseMax,
                           Integer totalDeathsMin,
                           Integer totalDeathsMax,
                           Integer totalRecoveredMin,
                           Integer totalRecoveredMax,
                           List<ListItem> countriesList,
                           Context context) {
        this.totalCaseMin = totalCaseMin;
        this.totalCaseMax = totalCaseMax;
        this.totalDeathsMin = totalDeathsMin;
        this.totalDeathsMax = totalDeathsMax;
        this.totalRecoveredMin = totalRecoveredMin;
        this.totalRecoveredMax = totalRecoveredMax;
        this.countriesList = countriesList;
        this.context = context;
    }

    public List<ListItem> getFilteredList() {
        List<ListItem> filteredList = new ArrayList<>();
        for (ListItem item : countriesList) {
            if(item.getType() == ListItem.TYPE_OTHER) {
                item = (OtherCountryModel) item;
                if(totalCaseMax != null && totalCaseMax != 0 && ((OtherCountryModel) item).getTotalConfirmed() < totalCaseMax) {
                    filteredList.add(item);
                }
            }
        }
        for (ListItem item : countriesList) {
            if(item.getType() == ListItem.TYPE_OTHER) {
                item = (OtherCountryModel) item;
                if(totalCaseMin != null && totalCaseMin != 0 && ((OtherCountryModel) item).getTotalConfirmed() > totalCaseMin) {
                    filteredList.add(item);
                }
            }
        }
        for (ListItem item : countriesList) {
            if(item.getType() == ListItem.TYPE_OTHER) {
                item = (OtherCountryModel) item;
                if(totalDeathsMax != null && totalDeathsMax != 0 && ((OtherCountryModel) item).getTotalDeaths() < totalDeathsMax) {
                    filteredList.add(item);
                }
            }
        }
        for (ListItem item : countriesList) {
            if(item.getType() == ListItem.TYPE_OTHER) {
                item = (OtherCountryModel) item;
                if(totalDeathsMin != null && totalDeathsMin != 0 && ((OtherCountryModel) item).getTotalDeaths() > totalDeathsMin) {
                    filteredList.add(item);
                }
            }
        }
        for (ListItem item : countriesList) {
            if(item.getType() == ListItem.TYPE_OTHER) {
                item = (OtherCountryModel) item;
                if(totalRecoveredMax != null && totalRecoveredMax != 0 && ((OtherCountryModel) item).getTotalRecovered() < totalRecoveredMax) {
                    filteredList.add(item);
                }
            }
        }
        for (ListItem item : countriesList) {
            if(item.getType() == ListItem.TYPE_OTHER) {
                item = (OtherCountryModel) item;
                if(totalRecoveredMin != null && totalRecoveredMin != 0 && ((OtherCountryModel) item).getTotalRecovered() > totalRecoveredMin) {
                    filteredList.add(item);
                }
            }
        }

        return filteredList;
    }
}
