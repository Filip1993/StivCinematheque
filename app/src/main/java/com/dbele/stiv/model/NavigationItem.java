package com.dbele.stiv.model;

public class NavigationItem {

    private String fragmentName;
    private String title;
    private int icon;

    public NavigationItem(String title, int icon, String fragmentName){
        this.title = title;
        this.icon = icon;
        this.fragmentName = fragmentName;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

}


