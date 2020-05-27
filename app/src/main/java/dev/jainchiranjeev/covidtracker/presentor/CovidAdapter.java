package dev.jainchiranjeev.covidtracker.presentor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dev.jainchiranjeev.covidtracker.R;
import dev.jainchiranjeev.covidtracker.models.ListItem;
import dev.jainchiranjeev.covidtracker.models.OtherCountryModel;
import dev.jainchiranjeev.covidtracker.models.UserCountryModel;
import dev.jainchiranjeev.covidtracker.utils.Constants;
import dev.jainchiranjeev.covidtracker.utils.PersistantStorage;

public class CovidAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ListItem> countriesList;
    private List<ListItem> backupList;
    private Context context;
    private String countryCode;
    private String sortKey = "ASC";

    public CovidAdapter(List<ListItem> countriesList, Context context) {
        this.context = context;
        this.countryCode = PersistantStorage.getInstance(context).getStringValue(Constants.USER_COUNTRY);
        this.countriesList = getConsolidatedList(countriesList);
        backupList = countriesList;
    }

    public void sortList(String sortKey, List<ListItem> countriesList) {
        List<ListItem> sortedList = new ArrayList<>();
        for (ListItem item : countriesList) {
            if(item.getType() == ListItem.TYPE_USER) {
                sortedList.add(item);
            }
        }

        List<ListItem> tempList = new ArrayList<>();
        for (ListItem item : countriesList) {
            if(item.getType() == ListItem.TYPE_OTHER) {
                if(((OtherCountryModel) item).getNewConfirmed() > 0) {
                    tempList.add(item);
                }
            }
        }
        switch(sortKey) {
            case "ASC":
                Collections.sort(tempList, new Comparator<ListItem>() {
                    @Override
                    public int compare(ListItem t1, ListItem t2) {
                        return ((Integer)((OtherCountryModel) t2).getTotalConfirmed()).compareTo((Integer)((OtherCountryModel) t1).getTotalConfirmed());
                    }
                });
                break;
            case "DESC":
                Collections.sort(tempList, new Comparator<ListItem>() {
                    @Override
                    public int compare(ListItem t1, ListItem t2) {
                        return ((Integer)((OtherCountryModel) t1).getTotalConfirmed()).compareTo((Integer)((OtherCountryModel) t2).getTotalConfirmed());
                    }
                });
                break;
        }
        sortedList.addAll(tempList);
        this.countriesList = sortedList;
        this.backupList = countriesList;
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class OtherViewHolder extends RecyclerView.ViewHolder {
        public OtherViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private List<ListItem> getConsolidatedList(List<ListItem> countriesList) {
        List<ListItem> consolidatedList = new ArrayList<>();
        for (ListItem item : countriesList) {
            if (item.getType() == ListItem.TYPE_USER) {
            }
        }
//        List<ListItem> tempList = getSortedList(sortKey, countriesList);
        for (ListItem item : countriesList) {
            if (item.getType() == ListItem.TYPE_OTHER) {
                consolidatedList.add(item);
            }
        }

        return consolidatedList;
    }

    public void search(String searchString) {
        this.countriesList = backupList;
        List<ListItem> tempList = new ArrayList<>(backupList);
        List<ListItem> searchedList = new ArrayList<>();
        for(ListItem item : tempList) {
            if(item.getType() == ListItem.TYPE_USER) {
                searchedList.add(item);
            }
        }
        if(searchString != null) {
            for(ListItem item : tempList) {
                if(item.getType() == ListItem.TYPE_OTHER) {
                    if(((OtherCountryModel) item).containsString(searchString)) {
                        searchedList.add(item);
                    }
                }
            }
            this.countriesList = searchedList;
        } else {
            this.countriesList = backupList;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return this.countriesList.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case ListItem.TYPE_USER:
                View userView = inflater.inflate(R.layout.list_item_user, parent, false);
                viewHolder = new UserViewHolder(userView);
                break;
            case ListItem.TYPE_OTHER:
                View otherView = inflater.inflate(R.layout.list_item_other, parent, false);
                viewHolder = new OtherViewHolder(otherView);
                break;
        }
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListItem item = countriesList.get(position);
        switch(holder.getItemViewType()) {
            case ListItem.TYPE_USER:
                if(item != null) {
                    UserViewHolder userViewHolder = (UserViewHolder) holder;
                    MaterialTextView tvCountry,tvTotalDeaths,tvTotalConfirmed,tvTotalRecovered;
                    tvCountry = userViewHolder.itemView.findViewById(R.id.tv_country);
                    tvTotalConfirmed = userViewHolder.itemView.findViewById(R.id.tv_total_confirmed_count);
                    tvTotalDeaths = userViewHolder.itemView.findViewById(R.id.tv_total_deaths_count);
                    tvTotalRecovered = userViewHolder.itemView.findViewById(R.id.tv_total_recovered_count);
                    tvCountry.setText(((UserCountryModel)item).getCountry());
                    tvTotalDeaths.setText(String.valueOf(((UserCountryModel)item).getTotalDeaths()));
                    tvTotalConfirmed.setText(String.valueOf(((UserCountryModel)item).getTotalConfirmed()));
                    tvTotalRecovered.setText(String.valueOf(((UserCountryModel)item).getTotalRecovered()));
                }
                break;
            case ListItem.TYPE_OTHER:
                if(item != null) {
                    OtherViewHolder otherViewHolder = (OtherViewHolder) holder;
                    MaterialTextView tvCountry,tvTotalDeaths,tvTotalConfirmed,tvTotalRecovered;
                    tvCountry = otherViewHolder.itemView.findViewById(R.id.tv_country);
                    tvTotalConfirmed = otherViewHolder.itemView.findViewById(R.id.tv_total_confirmed_count);
                    tvTotalDeaths = otherViewHolder.itemView.findViewById(R.id.tv_total_deaths_count);
                    tvTotalRecovered = otherViewHolder.itemView.findViewById(R.id.tv_total_recovered_count);
                    tvCountry.setText(((OtherCountryModel) item).getCountry());
                    tvTotalDeaths.setText(String.valueOf(((OtherCountryModel) item).getTotalDeaths()));
                    tvTotalConfirmed.setText(String.valueOf(((OtherCountryModel) item).getTotalConfirmed()));
                    tvTotalRecovered.setText(String.valueOf(((OtherCountryModel) item).getTotalRecovered()));
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.countriesList.size();
    }
}
