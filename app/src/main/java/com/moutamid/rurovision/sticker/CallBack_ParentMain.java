package com.moutamid.rurovision.sticker;

import java.util.List;


public interface CallBack_ParentMain<C> {

    List<C> getChildList();
    boolean isInitiallyExpanded();
}