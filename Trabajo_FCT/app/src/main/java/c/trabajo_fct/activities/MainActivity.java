package c.trabajo_fct.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;

import c.trabajo_fct.R;
import c.trabajo_fct.fragments.FragmentoPrincipal;
import c.trabajo_fct.fragments.Fragmento_Alumno_Visita;
import c.trabajo_fct.fragments.Fragmento_Detalle_Empresa;
import c.trabajo_fct.fragments.Fragmento_Detalle_Visita;
import c.trabajo_fct.fragments.Fragmento_Insert_UpdateVisita;
import c.trabajo_fct.fragments.Fragmento_Insert_UpdateAlumno;
import c.trabajo_fct.fragments.Fragmento_Insert_UpdateEmpresa;
import c.trabajo_fct.dialogs_fragments.SeleccionDirectaDialogFragment;
import c.trabajo_fct.interfaces.AdapterAllowMultiDeletion;
import c.trabajo_fct.interfaces.Callback_MainActivity;
import c.trabajo_fct.interfaces.GestionFabDesdeFragmento;
import c.trabajo_fct.interfaces.MyDateTimePickerCallBack;
import c.trabajo_fct.interfaces.OnAdapterItemClick;
import c.trabajo_fct.interfaces.OnAdapterItemLongClick;
import c.trabajo_fct.modelos.Alumno;
import c.trabajo_fct.modelos.Empresa;
import c.trabajo_fct.modelos.Visita;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Callback_MainActivity, SeleccionDirectaDialogFragment.SeleccionDirectaDialogListener
        , MyDateTimePickerCallBack, OnAdapterItemClick, OnAdapterItemLongClick {

    public static final String FRAGMENTO_INSERT_UPDATE_ALUMNO = "nuevo_alumno";
    public static final String FRAGMENTO_INSERT_UPDATE_EMPRESA = "nueva_empresa";
    public static final String FRAGMENTO_INSERT_UPDATE_VISITA = "fragmento_insert_update_visita";
    public static final String FRAGMENTO_DETALLES_EMPRESA = "fragmento_detalles_empresa";
    public static final String FRAGMENTO_DETALLE_VISITA = "fragmento_detalle_visita";

    public static final String FRAGMENTO_ALUMNO_VISITAS = "fragmento_detalles_de_alumno_y_sus_visitas";

    private static final String FRAGMENTO_PRINCIPAL = "principal";
    private static final String FRAGMENTO_PREFERENCIAS = "fragmento_preferencias";
    public static final String ALUMNO_ELIMINADO_REFRESCAR_VISITAS_ACTION = "c.trabajo_fct.activities.alumno_eliminado_refrescar_visitas_action";

    private Toolbar toolbar;
    private static FragmentManager gestor;
    private FloatingActionButton fab;
    private SharedPreferences preferencias;
    private AdapterAllowMultiDeletion adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        if (gestor.findFragmentById(R.id.flHueco) == null)
            cargarFragmentoPrincipal();

        preferencias = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void initViews() {
        gestor = getSupportFragmentManager();
        configDrawerLayout();
        configFab();
    }

    public static void cargarFragmentoPrincipal() {
        FragmentTransaction transaccion = gestor.beginTransaction();
        transaccion.replace(R.id.flHueco, FragmentoPrincipal.newInstance(), FRAGMENTO_PRINCIPAL);
        transaccion.commit();
    }

    @Override
    public void cargarFragmentoSecundario(String id_fragmento, Object o) {
        FragmentTransaction transaccion = gestor.beginTransaction();
        switch (id_fragmento) {
            case FRAGMENTO_INSERT_UPDATE_ALUMNO:
                transaccion.replace(R.id.flHueco, Fragmento_Insert_UpdateAlumno.newInstance((Alumno) o), FRAGMENTO_INSERT_UPDATE_ALUMNO);
                transaccion.addToBackStack(FRAGMENTO_INSERT_UPDATE_ALUMNO);
                break;

            case FRAGMENTO_INSERT_UPDATE_EMPRESA:
                transaccion.replace(R.id.flHueco, Fragmento_Insert_UpdateEmpresa.newInstance((Empresa) o), FRAGMENTO_INSERT_UPDATE_EMPRESA);
                transaccion.addToBackStack(FRAGMENTO_INSERT_UPDATE_EMPRESA);
                break;

            case FRAGMENTO_INSERT_UPDATE_VISITA:
                transaccion.replace(R.id.flHueco, Fragmento_Insert_UpdateVisita.newInstance((Visita) o), FRAGMENTO_INSERT_UPDATE_VISITA);
                transaccion.addToBackStack(FRAGMENTO_INSERT_UPDATE_VISITA);
                break;

            case FRAGMENTO_DETALLES_EMPRESA:
                transaccion.replace(R.id.flHueco, Fragmento_Detalle_Empresa.newInstance((Empresa) o), FRAGMENTO_DETALLES_EMPRESA);
                transaccion.addToBackStack(FRAGMENTO_DETALLES_EMPRESA);
                break;

            case FRAGMENTO_ALUMNO_VISITAS:
                transaccion.replace(R.id.flHueco, Fragmento_Alumno_Visita.newInstance((Alumno) o), FRAGMENTO_ALUMNO_VISITAS);
                transaccion.addToBackStack(FRAGMENTO_ALUMNO_VISITAS);
                break;

            case FRAGMENTO_DETALLE_VISITA:
                transaccion.replace(R.id.flHueco, Fragmento_Detalle_Visita.newInstance((Visita) o), FRAGMENTO_DETALLE_VISITA);
                transaccion.addToBackStack(FRAGMENTO_DETALLE_VISITA);
                break;

            case FRAGMENTO_PREFERENCIAS:
                startActivity(new Intent(this, ActividadPreferencias.class));
                break;
        }
        transaccion.commit();
    }

    private void configFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = gestor.findFragmentById(R.id.flHueco);
                if (fragment instanceof GestionFabDesdeFragmento)
                    ((GestionFabDesdeFragmento) fragment).onFabPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            // Cuando vuelvo de la vista de detalles del alumno, el viewPager de la ventana principal me carga solo las visitas del alumno del fragmento de detalles.
            // Para que vuelva a cargar la lista de todas las visitas, tengo que volver a cargar el fragmento principal.
            // Entonces:
        } else if (gestor.findFragmentById(R.id.flHueco) instanceof Fragmento_Alumno_Visita) { // si doy al botón de atras desde la vista de detalles y visitas de alumno
            gestor.popBackStack(); // limpio la pila
            cargarFragmentoPrincipal(); // cargo el fragmento principal
        } else {
            gestor.popBackStack();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.limpiar:
                adaptador.clearAllSelections();
                adaptador.disableMultiDeletionMode();
                toolbar.getMenu().findItem(R.id.eliminar).setVisible(false);
                toolbar.getMenu().findItem(R.id.limpiar).setVisible(false);
                return true;
            case R.id.eliminar:
                if (!adaptador.removeSelections()) // todos los adaptadores devuelven true, excepto el de alumnos q si elimina algún alumno y ese alumno tenía visitas asociadas devuelve false. En ese caso
                    enviarBroadcastVisitasAlumnoBorradas(); //envío un broadcast para notificar al adaptador de visitas que debe actualizarse.
                adaptador.clearAllSelections();
                toolbar.getMenu().findItem(R.id.eliminar).setVisible(false);
                toolbar.getMenu().findItem(R.id.limpiar).setVisible(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void enviarBroadcastVisitasAlumnoBorradas() {
        Intent intent = new Intent(ALUMNO_ELIMINADO_REFRESCAR_VISITAS_ACTION);
        LocalBroadcastManager gestor = LocalBroadcastManager.getInstance(this);
        gestor.sendBroadcast(intent);
    }

    private void configDrawerLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                vaciarPila();
                break;
            case R.id.nuevoAlumno:
                cargarFragmentoSecundario(FRAGMENTO_INSERT_UPDATE_ALUMNO, null);
                break;
            case R.id.nuevaEmpresa:
                cargarFragmentoSecundario(FRAGMENTO_INSERT_UPDATE_EMPRESA, null);
                break;
            case R.id.nuevaVisita:
                cargarFragmentoSecundario(FRAGMENTO_INSERT_UPDATE_VISITA, null);
                break;
            case R.id.configuracion:
                vaciarPila();
                cargarFragmentoSecundario(FRAGMENTO_PREFERENCIAS, null);
                break;
            case R.id.acercaDe:
                mostrarAcercaDe();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void vaciarPila(){
        if (!(gestor.findFragmentById(R.id.flHueco) instanceof FragmentoPrincipal)) {
            for (Fragment f : gestor.getFragments())
                gestor.beginTransaction().detach(f).commit();
            cargarFragmentoPrincipal();
        }
    }

    @Override
    public void setFabImage(int id) {
        fab.setImageResource(id);
    }

    @Override
    public void onItemClick(DialogFragment dialog, int which) {
        Fragment fragment = gestor.findFragmentById(R.id.flHueco);
        if (fragment instanceof Fragmento_Insert_UpdateVisita)
            ((Fragmento_Insert_UpdateVisita) fragment).setLblAlumno(which);
        else if (fragment instanceof Fragmento_Insert_UpdateAlumno)
            ((Fragmento_Insert_UpdateAlumno) fragment).setLblEmpresa(which);
    }

    @Override
    public void obtenerDate(Date date) {
        ((Fragmento_Insert_UpdateVisita) gestor.findFragmentByTag(FRAGMENTO_INSERT_UPDATE_VISITA)).setLblFecha(date);
    }

    @Override
    public void onItemClick(Object o) {
        if (o instanceof Empresa)
            cargarFragmentoSecundario(FRAGMENTO_DETALLES_EMPRESA, o);
        else if (o instanceof Alumno)
            cargarFragmentoSecundario(FRAGMENTO_ALUMNO_VISITAS, o);
        else if (o instanceof Visita)
            cargarFragmentoSecundario(FRAGMENTO_DETALLE_VISITA, o);
    }

    @Override
    public void setAdapterAllowMultiDeletion(AdapterAllowMultiDeletion adaptador) {
        this.adaptador = adaptador;
    }

    @Override
    public void onItemLongClick() {
        toolbar.getMenu().findItem(R.id.eliminar).setVisible(true);
        toolbar.getMenu().findItem(R.id.limpiar).setVisible(true);
    }

    @Override
    public void desactivarMultiseleccion() {
        toolbar.getMenu().findItem(R.id.eliminar).setVisible(false);
        toolbar.getMenu().findItem(R.id.limpiar).setVisible(false);
    }

    private void mostrarAcercaDe(){
        new AlertDialog.Builder(this).setTitle(getString(R.string.info)).setMessage(getString(R.string.desarrollado_por))
                .setPositiveButton(getString(R.string.aceptar_btn_acercade), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
