package com.dbele.stiv.model;

/**
 * Created by dbele on 4/3/2015.
 */
public class NavigationItem {
    private String title;
    private int icon;

    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    private String fragmentName;


    public NavigationItem(){}

    public NavigationItem(String title, int icon, String fragmentName){
        this.title = title;
        this.icon = icon;
        this.fragmentName = fragmentName;
    }


    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

}


