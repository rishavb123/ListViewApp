package io.github.rishavb123.listviewapp;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class AppItem implements Serializable {

    private String packageName;
    private boolean seperator;
    private transient Drawable image;
    private int imageResource;
    private boolean downloaded;
    private String appName;
    private String features;
    private double price;
    private String category;

    public AppItem(int imageResource, String appName, String category, String features, double price, String packageName) {

        this.imageResource = imageResource;
        this.appName = appName;
        this.features = features;
        this.price = price;
        this.category = category;
        this.downloaded = false;
        this.packageName = packageName;

    }

    public AppItem(Drawable image, String appName, String category, String features, String packageName) {

        this.image = image;
        this.appName = appName;
        this.features = features;
        this.price = 0;
        this.category = category;
        this.downloaded = true;
        this.packageName = packageName;

    }

    public AppItem(String appName) {
        seperator = true;
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isSeperator() {
        return seperator;
    }

    public String getCategory() {
        return (category == null)? "Unknown": category;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public Drawable getImage() {
        return image;
    }

    public String getFeatures() {
        return (features == null)? "None" : features;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getAppName() {
        return appName;
    }

    public double getPrice() {
        return price;
    }

}
