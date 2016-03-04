package c.examen2t.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import c.examen2t.R;
import c.examen2t.adaptadores.ProductosAdapter;
import c.examen2t.bdd.BddContract;
import c.examen2t.bdd.MyContentProvider;
import c.examen2t.bdd.SQLiteHelper;
import c.examen2t.fragmentos.FragmentoLista;
import c.examen2t.modelo.Producto;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int CARGADOR = 1;
    private LoaderManager mLoaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mLoaderManager = getSupportLoaderManager();
        mLoaderManager.initLoader(CARGADOR, null, this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues valores = new ContentValues();
                valores.put(BddContract.Producto.ID, 1);
                valores.put(BddContract.Producto.NOMBRE, "patatas");
                valores.put(BddContract.Producto.CANTIDAD, 2);
                valores.put(BddContract.Producto.UNIDAD, "kg");
                getContentResolver().insert(MyContentProvider.CONTENT_URI_PRODUCTOS, valores);
            }
        });

        cargarFragmentoLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void cargarFragmentoLista(){
        getSupportFragmentManager().beginTransaction().replace(R.id.flHueco, FragmentoLista.newInstance()).commit();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MyContentProvider.CONTENT_URI_PRODUCTOS, BddContract.Producto.TODOS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<Producto> productos = new ArrayList<>();
        Producto p = new Producto();
        if (data != null) {
            data.moveToFirst();
            while (!data.isAfterLast()) {
                p.setId(data.getInt(data.getColumnIndexOrThrow(BddContract.Producto.ID)));
                p.setNombre(data.getString(data.getColumnIndexOrThrow(BddContract.Producto.NOMBRE)));
                p.setCantidad(data.getFloat(data.getColumnIndexOrThrow(BddContract.Producto.CANTIDAD)));
                p.setUnidad(data.getString(data.getColumnIndexOrThrow(BddContract.Producto.UNIDAD)));
                productos.add(p);
                data.moveToNext();
            }
        }
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.flHueco);
        if (f instanceof FragmentoLista)
            ((FragmentoLista) f).getAdaptador().setProductos(productos);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.flHueco);
        if (f instanceof FragmentoLista)
            ((FragmentoLista) f).getAdaptador().setProductos(null);
    }
}
