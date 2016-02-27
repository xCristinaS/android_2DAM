package c.trabajo_fct.clases_bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cristina on 01/02/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static SQLiteHelper helper;

    private static final String SQL_CREATE_TABLE_PROFESOR = String.format("create table %s (%s integer primary key autoincrement, %s varchar (50) not null, %s varchar(20) not null);",
            BddContract.Profesor.TABLA, BddContract.Profesor.ID, BddContract.Profesor.NOMBRE, BddContract.Profesor.CONTRA);

    private static final String SQL_CREATE_TABLE_ALUMNO = String.format("create table %s (%s integer primary key autoincrement, %s varchar (50) not null, %s varchar (300) not null, %s varchar(9) not null,\n" +
            "%s varchar (10), %s integer, %s varchar(200), %s integer, FOREIGN KEY (%s) REFERENCES %s (%s), FOREIGN KEY (%s) REFERENCES %s (%s));", BddContract.Alumno.TABLA, BddContract.Alumno.ID, BddContract.Alumno.NOMBRE,
            BddContract.Alumno.DIRECCION, BddContract.Alumno.TELEFONO, BddContract.Alumno.CURSO, BddContract.Alumno.EDAD, BddContract.Alumno.FOTO, BddContract.Alumno.PROFESOR, BddContract.Alumno.PROFESOR,
            BddContract.Profesor.TABLA, BddContract.Profesor.ID, BddContract.Alumno.EMPRESA, BddContract.Empresa.TABLA, BddContract.Empresa.ID);

    private static final String SQL_CREATE_TABLE_EMPRESA = String.format("create table %s (%s integer primary key autoincrement, %s varchar (50) not null, %s varchar (300) not null, %s varchar(9) not null,\n" +
                    "%s varchar(200));", BddContract.Empresa.TABLA, BddContract.Empresa.ID, BddContract.Empresa.NOMBRE, BddContract.Empresa.DIRECCION, BddContract.Empresa.TELEFONO, BddContract.Alumno.FOTO);

    private static final String SQL_CREATE_TABLE_VISITAS = String.format("create table %s (%s integer, %s integer, %s date, %s varchar (500), Foreign key (%s) References %s (%s), " +
            "Foreign key (%s) References %s (%s), Primary key (%s, %s, %s));",
            BddContract.Visitas.TABLA, BddContract.Visitas.ID_PROFESOR, BddContract.Visitas.ID_ALUMNO, BddContract.Visitas.FECHA, BddContract.Visitas.COMENTARIO, BddContract.Visitas.ID_PROFESOR, BddContract.Profesor.TABLA,
            BddContract.Profesor.ID, BddContract.Visitas.ID_ALUMNO, BddContract.Alumno.TABLA, BddContract.Alumno.ID, BddContract.Visitas.ID_PROFESOR, BddContract.Visitas.ID_ALUMNO, BddContract.Visitas.FECHA);


    private SQLiteHelper(Context contexto, String nombreBD, CursorFactory factory, int versionBD){
        super(contexto, nombreBD, factory, versionBD);
    }

    public static SQLiteHelper getInstance(Context contexto, String nombreBD, CursorFactory factory, int versionBD){
        if (helper == null)
            helper = new SQLiteHelper(contexto, nombreBD, factory, versionBD);

        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PROFESOR);
        db.execSQL(SQL_CREATE_TABLE_EMPRESA);
        db.execSQL(SQL_CREATE_TABLE_ALUMNO);
        db.execSQL(SQL_CREATE_TABLE_VISITAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BddContract.Profesor.TABLA);
        db.execSQL(SQL_CREATE_TABLE_PROFESOR);
        db.execSQL("DROP TABLE IF EXISTS " + BddContract.Empresa.TABLA);
        db.execSQL(SQL_CREATE_TABLE_EMPRESA);
        db.execSQL("DROP TABLE IF EXISTS " + BddContract.Alumno.TABLA);
        db.execSQL(SQL_CREATE_TABLE_ALUMNO);
        db.execSQL("DROP TABLE IF EXISTS " + BddContract.Visitas.TABLA);
        db.execSQL(SQL_CREATE_TABLE_VISITAS);
    }
}
