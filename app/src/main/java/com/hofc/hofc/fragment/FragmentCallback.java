package com.hofc.hofc.fragment;

public interface FragmentCallback {
	public void onTaskDone();
	public void onError();
    public void onError(int messageId);
}
