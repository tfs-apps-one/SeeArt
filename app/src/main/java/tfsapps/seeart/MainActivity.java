package tfsapps.seeart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//public class MainActivity extends AppCompatActivity {
public class MainActivity extends AppCompatActivity implements Painter.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Painter painter = new Painter(this, 0, 0);
        painter.setCallback(this);
        setContentView(painter);
    }
}