package me.NickP0is0n;

import java.io.Serializable;
import java.util.ArrayList;

class TaskSet extends ArrayList<Task> implements Serializable {
    private boolean passwordProtected;
    private String password;

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.passwordProtected = true;
    }
}
