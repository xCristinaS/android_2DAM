package cristina.ejercicio30_reciclerview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView lstNombres;
    private ArrayList<Nombre> lista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        rellenarListaNombres();
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    private void initViews(){
        lstNombres = (RecyclerView) findViewById(R.id.lstNombres);
        lstNombres.setAdapter(new NombresAdapter(lista));
        lstNombres.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lstNombres.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void rellenarListaNombres(){
        lista.add(new Nombre("Pepe"));
        lista.add(new Nombre("Juan"));
        lista.add(new Nombre("Lolo"));
        lista.add(new Nombre("María"));
        lista.add(new Nombre("Pepe"));
        lista.add(new Nombre("Juan"));
        lista.add(new Nombre("Lolo"));
        lista.add(new Nombre("María"));
        lista.add(new Nombre("Pepe"));
        lista.add(new Nombre("Juan"));
        lista.add(new Nombre("Lolo"));
        lista.add(new Nombre("María"));
        lista.add(new Nombre("Pepe"));
        lista.add(new Nombre("Juan"));
        lista.add(new Nombre("Lolo"));
        lista.add(new Nombre("María"));
        lista.add(new Nombre("Pepe"));
        lista.add(new Nombre("Juan"));
        lista.add(new Nombre("Lolo"));
        lista.add(new Nombre("María"));
        lista.add(new Nombre("Pepe"));
        lista.add(new Nombre("Juan"));
        lista.add(new Nombre("Lolo"));
        lista.add(new Nombre("María"));
        lista.add(new Nombre("Pepe"));
        lista.add(new Nombre("Juan"));
        lista.add(new Nombre("Lolo"));
        lista.add(new Nombre("María"));
        lista.add(new Nombre("Pepe"));
        lista.add(new Nombre("Juan"));
        lista.add(new Nombre("Lolo"));
        lista.add(new Nombre("María"));
        lista.add(new Nombre("Pepe"));
        lista.add(new Nombre("Juan"));
        lista.add(new Nombre("Lolo"));
        lista.add(new Nombre("María"));
    }
}
