package com.imzqqq.app.flow.interfaces;

public interface PermissionRequester {
    void onRequestPermissionsResult(String[] permissions, int[] grantResults);
}