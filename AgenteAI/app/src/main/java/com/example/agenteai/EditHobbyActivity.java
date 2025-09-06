package com.example.agenteai;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class EditHobbyActivity extends AppCompatActivity {

    private TextInputEditText editNombre;
    private RadioGroup radioGroupNivel;
    private RadioButton radioFacil, radioMedio, radioDificil;
    private Button btnActualizar, btnCancelar;
    private DatabaseHelper databaseHelper;
    private Hobby hobbyActual;
    private int hobbyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hobby);

        initViews();
        initDatabase();
        obtenerHobbyId();
        cargarDatosHobby();
        setupClickListeners();
    }

    private void initViews() {
        editNombre = findViewById(R.id.edit_nombre);
        radioGroupNivel = findViewById(R.id.radio_group_nivel);
        radioFacil = findViewById(R.id.radio_facil);
        radioMedio = findViewById(R.id.radio_medio);
        radioDificil = findViewById(R.id.radio_dificil);
        btnActualizar = findViewById(R.id.btn_actualizar);
        btnCancelar = findViewById(R.id.btn_cancelar);
    }

    private void initDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }

    private void obtenerHobbyId() {
        hobbyId = getIntent().getIntExtra("HOBBY_ID", -1);
        if (hobbyId == -1) {
            Toast.makeText(this, "Error: No se pudo cargar el hobby", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void cargarDatosHobby() {
        hobbyActual = databaseHelper.getHobby(hobbyId);
        if (hobbyActual != null) {
            // Cargar nombre
            editNombre.setText(hobbyActual.getNombre());

            // Cargar nivel de dificultad
            String nivel = hobbyActual.getNivelDificultad();
            switch (nivel) {
                case "Fácil":
                    radioFacil.setChecked(true);
                    break;
                case "Medio":
                    radioMedio.setChecked(true);
                    break;
                case "Difícil":
                    radioDificil.setChecked(true);
                    break;
            }
        } else {
            Toast.makeText(this, "Error: No se pudo cargar el hobby", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupClickListeners() {
        btnActualizar.setOnClickListener(v -> actualizarHobby());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void actualizarHobby() {
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

        // Actualizar hobby actual
        hobbyActual.setNombre(nombre);
        hobbyActual.setNivelDificultad(nivelDificultad);
        // Mantener la fecha de registro original

        // Actualizar en base de datos
        int resultado = databaseHelper.updateHobby(hobbyActual);

        if (resultado > 0) {
            Toast.makeText(this, "Hobby actualizado correctamente", Toast.LENGTH_SHORT).show();
            finish(); // Cerrar activity y volver a MainActivity
        } else {
            Toast.makeText(this, "Error al actualizar el hobby", Toast.LENGTH_SHORT).show();
        }
    }
}
