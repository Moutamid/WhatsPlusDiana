package com.moutamid.rurovision.ascii_face;

public interface EmojiListener {
    void onWpShare(String emojiUnicode);
    void onShare(String emojiUnicode);
    void onCopy(String emojiUnicode);
}
