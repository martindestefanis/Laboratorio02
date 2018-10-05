package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class RestoFirebaseInstanceIdService extends FirebaseInstanceIdService {
    public RestoFirebaseInstanceIdService() {
    }
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        guardarToken(token);
    }

    private void guardarToken(String _token) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("registration_id", _token);
        editor.apply();
    }
    private String leerToken() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("registration_id", null);
    }
}