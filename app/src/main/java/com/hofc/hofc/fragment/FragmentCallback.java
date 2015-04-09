package com.hofc.hofc.fragment;

public interface FragmentCallback {
	void onTaskDone();
	void onError();
    void onError(int messageId);
}
