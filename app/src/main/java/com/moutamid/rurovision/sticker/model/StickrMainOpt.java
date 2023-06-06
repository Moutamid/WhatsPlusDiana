package com.moutamid.rurovision.sticker.model;

import android.graphics.drawable.Drawable;

import com.moutamid.rurovision.sticker.CallBack_ParentMain;

import java.io.Serializable;
import java.util.List;

public class StickrMainOpt implements CallBack_ParentMain<Stickr>, Serializable {

    Drawable parentIcon;
    private List<Stickr> mChildItemList;
    private String mParentText;
    private Boolean isVisibleProgress = false;
    private int visibility = 0;

    public Boolean getVisibleProgress() {
        return isVisibleProgress;
    }

    public void setVisibleProgress(Boolean visibleProgress) {
        isVisibleProgress = visibleProgress;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    @Override
    public List<Stickr> getChildList() {
        return mChildItemList;
    }

    public void setChildItemList(List<Stickr> childItemList) {
        mChildItemList = childItemList;
    }

    public String getParentText() {
        return mParentText;
    }

    public void setParentText(String parentText) {
        mParentText = parentText;
    }

    public Drawable getParentIcon() {
        return parentIcon;
    }

    public void setParentIcon(Drawable parentIcon) {
        this.parentIcon = parentIcon;
    }

}
