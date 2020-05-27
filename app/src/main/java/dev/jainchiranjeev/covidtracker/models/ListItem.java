package dev.jainchiranjeev.covidtracker.models;

public abstract class ListItem {
    public final static int TYPE_USER = 0;
    public final static int TYPE_OTHER = 1;

    abstract public int getType();
}
