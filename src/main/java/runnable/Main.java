package runnable;

import com.formdev.flatlaf.FlatLightLaf;
import layouts.LoginForm;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        FlatLightLaf.setup();
        LoginForm loginForm = new LoginForm();

    }

}