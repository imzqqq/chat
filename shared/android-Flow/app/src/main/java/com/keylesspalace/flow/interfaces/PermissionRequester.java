package com.keylesspalace.flow.interfaces;

public interface PermissionRequester {
    void onRequestPermissionsResult(String[] permissions, int[] grantResults);
}