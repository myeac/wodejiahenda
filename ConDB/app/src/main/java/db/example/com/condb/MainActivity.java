package db.example.com.condb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.DriverManager;
import java.sql.SQLException;

import ru.arturvasilov.sqlite.core.SQLite;
import ru.arturvasilov.sqlite.core.SQLiteConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void crearDB(){

        try {
            DriverManager.getConnection("http:\\test.db");
            SQLite.initialize(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}
