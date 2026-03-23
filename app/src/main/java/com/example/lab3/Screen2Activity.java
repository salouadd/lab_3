package com.example.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Screen2Activity extends AppCompatActivity {

    private TextView tvInitials, tvNomRecap, tvEmailRecap, tvPhoneRecap, tvAdresseRecap, tvVilleRecap;
    private Button btnModifier, btnPartager, btnNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);

        // Liaison des vues
        tvInitials = findViewById(R.id.tvInitials);
        tvNomRecap = findViewById(R.id.tvNomRecap);
        tvEmailRecap = findViewById(R.id.tvEmailRecap);
        tvPhoneRecap = findViewById(R.id.tvPhoneRecap);
        tvAdresseRecap = findViewById(R.id.tvAdresseRecap);
        tvVilleRecap = findViewById(R.id.tvVilleRecap);
        btnModifier = findViewById(R.id.btnModifier);
        btnPartager = findViewById(R.id.btnPartager);
        btnNew = findViewById(R.id.btnNew);

        // Récupération des données
        Intent intent = getIntent();
        String nom = intent.getStringExtra("nom");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String adresse = intent.getStringExtra("adresse");
        String ville = intent.getStringExtra("ville");

        // Affichage des données
        tvNomRecap.setText(nom);
        tvEmailRecap.setText(email);
        tvPhoneRecap.setText(phone != null && !phone.isEmpty() ? phone : "Non renseigné");
        tvAdresseRecap.setText(adresse != null && !adresse.isEmpty() ? adresse : "Non renseigné");
        tvVilleRecap.setText(ville != null && !ville.isEmpty() ? ville : "Non renseigné");
        tvInitials.setText(getInitiales(nom));

        // Bouton Modifier
        btnModifier.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // Bouton Nouveau Formulaire
        btnNew.setOnClickListener(v -> {
            Intent mainIntent = new Intent(Screen2Activity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(mainIntent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // Bouton Partager
        btnPartager.setOnClickListener(v -> {
            String shareBody = "Fiche Contact :\n" +
                    "Nom: " + nom + "\n" +
                    "Email: " + email + "\n" +
                    "Tél: " + phone + "\n" +
                    "Ville: " + ville;
            
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Partage de Contact");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(shareIntent, "Partager via"));
        });
    }

    /**
     * Génère les initiales à partir du nom complet (ex: "LACHGAR Mohamed" -> "LM")
     */
    private String getInitiales(String nom) {
        if (nom == null || nom.trim().isEmpty()) return "??";
        String[] parts = nom.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (int i = 0; i < Math.min(parts.length, 2); i++) {
            if (!parts[i].isEmpty()) {
                initials.append(parts[i].charAt(0));
            }
        }
        return initials.toString().toUpperCase();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}