package com.example.agenteai;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddHobbyActivity extends AppCompatActivity {

    private TextInputEditText editNombre;
    private RadioGroup radioGroupNivel;
    private RadioButton radioFacil, radioMedio, radioDificil;
    private Button btnGuardar, btnCancelar;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hobby);

        initViews();
        initDatabase();
        setupClickListeners();
    }

    private void initViews() {
        editNombre = findViewById(R.id.edit_nombre);
        radioGroupNivel = findViewById(R.id.radio_group_nivel);
        radioFacil = findViewById(R.id.radio_facil);
        radioMedio = findViewById(R.id.radio_medio);
        radioDificil = findViewById(R.id.radio_dificil);
        btnGuardar = findViewById(R.id.btn_guardar);
        btnCancelar = findViewById(R.id.btn_cancelar);
    }

    private void initDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }

    private void setupClickListeners() {
        btnGuardar.setOnClickListener(v -> guardarHobby());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void guardarHobby() {
        // Validar nombre del hobby
        String nombre = editNombre.getText().toString().trim();
        if (TextUtils.isEmpty(nombre)) {
            editNombre.setError("El nombre del hobby es obligatorio");
            editNombre.requestFocus();
            return;
        }

        // Validar selección de nivel de dificultad
        int selectedRadioButtonId = radioGroupNivel.getCheckedRadioButtonId();
        if (selectedRadioButtonId == -1) {
            Toast.makeText(this, "Por favor selecciona un nivel de dificultad", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el nivel de dificultad seleccionado
        String nivelDificultad = "";
        if (selectedRadioButtonId == R.id.radio_facil) {
            nivelDificultad = "Fácil";
        } else if (selectedRadioButtonId == R.id.radio_medio) {
            nivelDificultad = "Medio";
        } else if (selectedRadioButtonId == R.id.radio_dificil) {
            nivelDificultad = "Difícil";
        }

        // Obtener fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaRegistro = dateFormat.format(new Date());

        // Crear nuevo hobby
        Hobby nuevoHobby = new Hobby(nombre, nivelDificultad, fechaRegistro);

        // Guardar en base de datos
        long resultado = databaseHelper.insertHobby(nuevoHobby);

        if (resultado != -1) {
            Toast.makeText(this, "Hobby guardado correctamente", Toast.LENGTH_SHORT).show();
            finish(); // Cerrar activity y volver a MainActivity
        } else {
            Toast.makeText(this, "Error al guardar el hobby", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarFormulario() {
        editNombre.setText("");
        radioGroupNivel.clearCheck();
        editNombre.requestFocus();
    }
}
