package com.example.filmoteca;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class PelisListView extends AppCompatActivity {//activity que muestra la lista de peliculas, una listview wen la que podrmos seleccionar una de las peliculas y verla en el visor

    //Declaración de variables
    private ListView listview; //declaracion de listview

    private String pelipulsada=null;//variable que guardará el titulo de la pelicula que se pulse en la lista

    //variables de database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference raiz = database.getReference("PELICULAS");
    final ArrayList<String> names = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelis_list_view);

    Query query =raiz.orderByChild("Titulo");//query de la BBDD para ordenar por titulo





        query.addValueEventListener(new ValueEventListener() {//añadimos un listener para que cada vez que se modifique un elemento se actualice la lista
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//recorremos la BBDD y añadimos en el arraylist names el titulo de cada pelicula
                    String tit = snapshot.child("Titulo").getValue(String.class);
                    names.add(tit);
                }

                //creacion del adaptador para meter los datos del arraylist names en la listview
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(PelisListView.this, android.R.layout.simple_list_item_1, names);
                listview = (ListView) findViewById(R.id.listviewdepelis);
                listview.setAdapter(adapter);



                //listener para cuando se pulse una pelicula de la lista
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {//se crea el click listener de la listview
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // Obtener el elemento pulsado
                                String item = (String) parent.getItemAtPosition(position);
                                Toast.makeText(PelisListView.this,"ha pulsado "+item,Toast.LENGTH_SHORT).show();
                                pelipulsada=item;
                                //se crea el intent con la nueva actividad y se le pasa la peliplsada para rescatar esos datos de la BBDD
                                Intent intent = new Intent(PelisListView.this, visor.class);
                                intent.putExtra("MESSAGE", pelipulsada);
                                startActivity(intent);


                            }
                        });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value

            }
        });





    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//Se crea el menú que queramos para nuestra activity (inflate)
        getMenuInflater().inflate(R.menu.menu3,menu);
        return true;


    }




    //Creacion del menú de opciones
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//se hace el CASE para cada opción del menu
        switch(item.getItemId()){
            case R.id.idioma:
                AlertDialog.Builder locale = new AlertDialog.Builder(this);
                locale.setTitle(R.string.selecciondeidioma);
                locale.setMessage(R.string.textoselecciondeidioma);


                locale.setPositiveButton(R.string.nombredelidioma, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Locale locale = new Locale("es");
                        Locale.setDefault(locale);

                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config,
                                getBaseContext().getResources().getDisplayMetrics());

                        // Vuelve a cargar la interfaz de usuario para que se reflejen los cambios de idioma
                        recreate();
                    }
                });

                locale.setNegativeButton(R.string.nombredelidiomaen, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Establece el idioma inglés como el idioma por defecto
                        Locale locale = new Locale("en");
                        Locale.setDefault(locale);

                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config,
                                getBaseContext().getResources().getDisplayMetrics());

                        // Vuelve a cargar la interfaz de usuario para que se reflejen los cambios de idioma
                        recreate();
                    }
                });

                // Muestra el cuadro de diálogo
                AlertDialog dialog = locale.create();
                dialog.show();
                return true;


            case R.id.acercade:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.tituloacercade);
                builder.setMessage(R.string.mensajeacercade);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();

                return true;
            case R.id.anyadirpeli://se crea el intent para ir a la activity de añadir pelicula
                Intent intent3 = new Intent(PelisListView.this,IntroducePeli.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }




//Fin del menu de opciones
}