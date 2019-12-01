package com.study.server;

public class ControllerImpl implements Controller {
    private String path;

    public ControllerImpl(String path) {
        this.path = path;
    }

    @Override
    public void getFile(String path) {

    }
}
