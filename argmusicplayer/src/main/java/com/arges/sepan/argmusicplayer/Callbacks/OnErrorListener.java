package com.arges.sepan.argmusicplayer.Callbacks;

import com.arges.sepan.argmusicplayer.Enums.ErrorType;

public interface OnErrorListener {
    void onError(ErrorType errorType, String description);
}
