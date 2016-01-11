package com.example.marcos.registro;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    String url = "http://dominiodepruebas.16mb.com/registro2.php";
    String nombre,correo,password;
    ProgressDialog PD;

    EditText editTextNombre;
    EditText editTextCorreo;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        isInternetOn();

        Button buttonEnviar = (Button)findViewById(R.id.buttonEnviar);
        final CheckBox checkBoxTerminos = (CheckBox) findViewById(R.id.checkBox);
        PD = new ProgressDialog(this);
        PD.setMessage("Cargando...");
        PD.setCancelable(false);
        editTextNombre = (EditText) findViewById(R.id.editTextNombre);
        editTextCorreo = (EditText) findViewById(R.id.editTextCorreo);
        editTextPassword = (EditText) findViewById(R.id.editTextContraseña);

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxTerminos.isChecked()) {
                    PD.show();
                    nombre = editTextNombre.getText().toString();
                    correo = editTextCorreo.getText().toString();
                    password = editTextPassword.getText().toString();

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    PD.dismiss();
                                    editTextNombre.setText("");
                                    editTextCorreo.setText("");
                                    editTextPassword.setText("");
                                    if (response.equals("success")) {
                                        Toast.makeText(getApplicationContext(),
                                                "Te has registrado correctamente",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),
                                                "El usuario que intentas crear ya existe",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            PD.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "Ha habido un error al realizar el registro", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("nombre", nombre);
                            params.put("password", password);
                            params.put("correo", correo);

                            return params;
                        }
                    };

                    // Adding request to request queue
                    MyApplication.getInstance().addToReqQueue(postRequest);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Porfavor acepte los terminos y condiciones de uso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(this, " No hay conexion a internet ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}

