package com.moutamid.rurovision.reply;

public interface ReplyListener {
    void onClickEdit(String s,int layoutrPosition);
    void onClickCopy(String s);
    void onClickShare(String s);
}
