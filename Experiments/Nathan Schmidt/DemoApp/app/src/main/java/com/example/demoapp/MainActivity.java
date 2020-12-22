package com.example.demoapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button b = findViewById (R.id.epic_button);
        b.setText ("LOLZ");
        b.setOnClickListener(new MyButtonClickListener ());

        //Correcting a small problem with the template
        //Hello world is extremely lame
        TextView tv = findViewById (R.id.lame_text);
        tv.setText ("Behold, the ever-growing button!\n It cannot(?) be shrunk!");
        tv.setOnClickListener (new MyTextClickListener ());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Taking over this method here, sorry!

        ((Button)(findViewById (R.id.epic_button))).setText ("You found the options menu.");

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyButtonClickListener implements View.OnClickListener {

        public MyButtonClickListener () {
            super ();
        }

        @Override
        public void onClick (View v) {
            //System.out.println ("Button has been pressed");
            Button b = findViewById (R.id.epic_button);
            CharSequence t = b.getText ();
            char c = (char)(t.charAt (t.length () - 1) + 1);
            b.setText (t.toString () + c);
        }
    }

    public class MyTextClickListener implements View.OnClickListener {

        public MyTextClickListener () {
            super ();
        }

        @Override
        public void onClick (View v) {
            //System.out.println ("Button has been pressed");
            Button b = findViewById (R.id.epic_button);
            CharSequence t = b.getText ();
            if (t.length () <= 1) {
                b.setText ("I can't be defeated that easily!");
            } else {
                b.setText(t.toString().substring(0, t.length() - 1));
            }
        }
    }
}
