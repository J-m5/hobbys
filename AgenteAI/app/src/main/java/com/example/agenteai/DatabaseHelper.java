package com.example.agenteai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "jobicat.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla hobbies
    private static final String TABLE_HOBBIES = "hobbies";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_NIVEL_DIFICULTAD = "nivel_dificultad";
    private static final String COLUMN_FECHA_REGISTRO = "fecha_registro";

    // Script para crear la tabla
    private static final String CREATE_TABLE_HOBBIES =
        "CREATE TABLE " + TABLE_HOBBIES + "(" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        COLUMN_NOMBRE + " TEXT NOT NULL," +
        COLUMN_NIVEL_DIFICULTAD + " TEXT NOT NULL," +
        COLUMN_FECHA_REGISTRO + " TEXT" +
        ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HOBBIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOBBIES);
        onCreate(db);
    }

    // Insertar hobby
    public long insertHobby(Hobby hobby) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NOMBRE, hobby.getNombre());
        values.put(COLUMN_NIVEL_DIFICULTAD, hobby.getNivelDificultad());
        values.put(COLUMN_FECHA_REGISTRO, hobby.getFechaRegistro());

        long id = db.insert(TABLE_HOBBIES, null, values);
        db.close();
        return id;
    }

    // Obtener todos los hobbies
    public List<Hobby> getAllHobbies() {
        List<Hobby> hobbies = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HOBBIES + " ORDER BY " + COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Hobby hobby = new Hobby();
                hobby.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                hobby.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)));
                hobby.setNivelDificultad(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NIVEL_DIFICULTAD)));
                hobby.setFechaRegistro(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_REGISTRO)));

                hobbies.add(hobby);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return hobbies;
    }

    // Obtener hobby por ID
    public Hobby getHobby(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HOBBIES,
                new String[]{COLUMN_ID, COLUMN_NOMBRE, COLUMN_NIVEL_DIFICULTAD, COLUMN_FECHA_REGISTRO},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Hobby hobby = new Hobby(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NIVEL_DIFICULTAD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_REGISTRO))
            );
            cursor.close();
            db.close();
            return hobby;
        }

        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    // Actualizar hobby
    public int updateHobby(Hobby hobby) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, hobby.getNombre());
        values.put(COLUMN_NIVEL_DIFICULTAD, hobby.getNivelDificultad());
        values.put(COLUMN_FECHA_REGISTRO, hobby.getFechaRegistro());

        int result = db.update(TABLE_HOBBIES, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(hobby.getId())});
        db.close();
        return result;
    }

    // Eliminar hobby
    public void deleteHobby(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOBBIES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Obtener cantidad de hobbies
    public int getHobbiesCount() {
        String countQuery = "SELECT * FROM " + TABLE_HOBBIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}
