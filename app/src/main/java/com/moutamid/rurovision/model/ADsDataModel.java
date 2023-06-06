package com.moutamid.rurovision.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ADsDataModel {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("banner_id")
    @Expose
    private String bannerid;
    @SerializedName("interstitial_id")
    @Expose
    private String interstitialid;
    @SerializedName("native_id")
    @Expose
    private String nativeid;
    @SerializedName("open_app_id")
    @Expose
    private String openadid;
    @SerializedName("is_show_ads")
    @Expose
    private Integer isAdEnable;
    @SerializedName("show_banner")
    @Expose
    private Integer banner;
    @SerializedName("show_interstitial")
    @Expose
    private Integer interstitial;
    @SerializedName("show_interstitial_exit")
    @Expose
    private Integer exit_ad_enable;
    @SerializedName("interstitial_count")
    @Expose
    private Integer interstitial_count;
    @SerializedName("show_native")
    @Expose
    private Integer _native;
    @SerializedName("show_open_app")
    @Expose
    private Integer openad;
    @SerializedName("show_open_app_splash")
    @Expose
    private Integer is_splash_on;
    @SerializedName("ads_per_seassion")
    @Expose
    private Integer ads_per_session;
    @SerializedName("relocation")
    @Expose
    private String transfer_link;
    @SerializedName("adtype")
    @Expose
    private String adtype;
    @SerializedName("splash_time")
    @Expose
    private Integer splash_time;


    public Integer getIs_splash_on() {
        if (is_splash_on == null) {
            return 1;
        }
        return is_splash_on;
    }

    public void setIs_splash_on(Integer is_splash_on) {
        this.is_splash_on = is_splash_on;
    }

    public Integer getSplash_time() {
        if (splash_time == null) {
            return 10;
        }
        return splash_time;
    }

    public void setSplash_time(Integer splash_time) {
        this.splash_time = splash_time;
    }

    public String getTransfer_link() {
        return transfer_link;
    }

    public void setTransfer_link(String transfer_link) {
        this.transfer_link = transfer_link;
    }

    public Integer get_native() {
        return _native;
    }

    public void set_native(Integer _native) {
        this._native = _native;
    }

    public Integer getExit_ad_enable() {
        return exit_ad_enable;
    }

    public void setExit_ad_enable(Integer exit_ad_enable) {
        this.exit_ad_enable = exit_ad_enable;
    }

    public Integer getAds_per_session() {
        return ads_per_session;
    }

    public void setAds_per_session(Integer ads_per_session) {
        this.ads_per_session = ads_per_session;
    }

    public Integer getAds_per_click() {
        return interstitial_count;
    }

    public void setAds_per_click(Integer ads_per_click) {
        this.interstitial_count = interstitial_count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBannerid() {
        return bannerid;
    }

    public void setBannerid(String bannerid) {
        this.bannerid = bannerid;
    }

    public Integer getBanner() {
        return banner;
    }

    public void setBanner(Integer banner) {
        this.banner = banner;
    }

    public String getNativeid() {
        return nativeid;
    }

    public void setNativeid(String nativeid) {
        this.nativeid = nativeid;
    }

    public Integer getNative() {
        return _native;
    }

    public void setNative(Integer _native) {
        this._native = _native;
    }

    public String getInterstitialid() {
        return interstitialid;
    }

    public void setInterstitialid(String interstitialid) {
        this.interstitialid = interstitialid;
    }

    public Integer getInterstitial() {
        return interstitial;
    }

    public void setInterstitial(Integer interstitial) {
        this.interstitial = interstitial;
    }

    public String getOpenadid() {
        return openadid;
    }

    public void setOpenadid(String openadid) {
        this.openadid = openadid;
    }

    public Integer getOpenad() {
        return openad;
    }

    public void setOpenad(Integer openad) {
        this.openad = openad;
    }

    public String getAdtype() {
        return adtype;
    }

    public void setAdtype(String adtype) {
        this.adtype = adtype;
    }

    public Integer getIsAdEnable() {
        return isAdEnable;
    }

    public void setIsAdEnable(Integer isAdEnable) {
        this.isAdEnable = isAdEnable;
    }

}
