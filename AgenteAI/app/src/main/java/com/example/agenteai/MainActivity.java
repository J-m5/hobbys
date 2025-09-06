package com.example.agenteai;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements HobbyAdapter.OnHobbyClickListener {

    private RecyclerView recyclerHobbies;
    private TextView textEmpty;
    private FloatingActionButton fabAddHobby;
    private HobbyAdapter hobbyAdapter;
    private DatabaseHelper databaseHelper;
    private List<Hobby> hobbiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        initDatabase();
        setupRecyclerView();
        loadHobbies();
        setupClickListeners();
    }

    private void initViews() {
        recyclerHobbies = findViewById(R.id.recycler_hobbies);
        textEmpty = findViewById(R.id.text_empty);
        fabAddHobby = findViewById(R.id.fab_add_hobby);
    }

    private void initDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }

    private void setupRecyclerView() {
        recyclerHobbies.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadHobbies() {
        hobbiesList = databaseHelper.getAllHobbies();

        if (hobbiesList.isEmpty()) {
            recyclerHobbies.setVisibility(View.GONE);
            textEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerHobbies.setVisibility(View.VISIBLE);
            textEmpty.setVisibility(View.GONE);

            if (hobbyAdapter == null) {
                hobbyAdapter = new HobbyAdapter(this, hobbiesList);
                hobbyAdapter.setOnHobbyClickListener(this);
                recyclerHobbies.setAdapter(hobbyAdapter);
            } else {
                hobbyAdapter.updateHobbies(hobbiesList);
            }
        }
    }

    private void setupClickListeners() {
        fabAddHobby.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddHobbyActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onEditClick(Hobby hobby) {
        Intent intent = new Intent(MainActivity.this, EditHobbyActivity.class);
        intent.putExtra("HOBBY_ID", hobby.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Hobby hobby) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar el hobby '" + hobby.getNombre() + "'?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    databaseHelper.deleteHobby(hobby.getId());
                    Toast.makeText(this, "Hobby eliminado correctamente", Toast.LENGTH_SHORT).show();
                    loadHobbies();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHobbies();
    }
}