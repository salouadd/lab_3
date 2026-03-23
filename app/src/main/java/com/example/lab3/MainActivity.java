package com.example.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etNom, etEmail, etPhone, etAdresse, etVille;
    private View validNom, validEmail, validPhone, validAdresse, validVille;
    private TextView errNom, errEmail, errPhone;
    private ProgressBar progressBar;
    private TextView tvProgressLabel;
    private Button btnEnvoyer;

    private boolean touchedNom = false;
    private boolean touchedEmail = false;
    private boolean touchedPhone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues
        etNom = findViewById(R.id.nom);
        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.phone);
        etAdresse = findViewById(R.id.adresse);
        etVille = findViewById(R.id.ville);

        validNom = findViewById(R.id.validNom);
        validEmail = findViewById(R.id.validEmail);
        validPhone = findViewById(R.id.validPhone);
        validAdresse = findViewById(R.id.validAdresse);
        validVille = findViewById(R.id.validVille);

        errNom = findViewById(R.id.errNom);
        errEmail = findViewById(R.id.errEmail);
        errPhone = findViewById(R.id.errPhone);

        progressBar = findViewById(R.id.progressBar);
        tvProgressLabel = findViewById(R.id.tvProgressLabel);
        btnEnvoyer = findViewById(R.id.btnEnvoyer);

        // Configuration des TextWatchers
        setupWatchers();

        // Configuration du FocusChangeListener
        setupFocusListeners();

        // Action du bouton Envoyer
        btnEnvoyer.setOnClickListener(v -> {
            if (isFormValid()) {
                Intent intent = new Intent(MainActivity.this, Screen2Activity.class);
                intent.putExtra("nom", etNom.getText().toString().trim());
                intent.putExtra("email", etEmail.getText().toString().trim());
                intent.putExtra("phone", etPhone.getText().toString().trim());
                intent.putExtra("adresse", etAdresse.getText().toString().trim());
                intent.putExtra("ville", etVille.getText().toString().trim());

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // resetForm(); // Décommenter si vous voulez vider au retour
    }

    private void setupWatchers() {
        TextWatcher commonWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                validateForm();
            }
        };

        etNom.addTextChangedListener(commonWatcher);
        etEmail.addTextChangedListener(commonWatcher);
        etPhone.addTextChangedListener(commonWatcher);
        etAdresse.addTextChangedListener(commonWatcher);
        etVille.addTextChangedListener(commonWatcher);
    }

    private void setupFocusListeners() {
        etNom.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) { touchedNom = true; validateForm(); }
        });
        etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) { touchedEmail = true; validateForm(); }
        });
        etPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) { touchedPhone = true; validateForm(); }
        });
    }

    private void validateForm() {
        String nom = etNom.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String adresse = etAdresse.getText().toString().trim();
        String ville = etVille.getText().toString().trim();

        // Validation des champs
        boolean nomValid = nom.length() >= 3;
        boolean emailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean phoneValid = phone.isEmpty() || phone.matches("^\\+?[\\d\\s\\-()]{6,15}$");

        // Affichage des erreurs au blur
        if (touchedNom) errNom.setVisibility(nomValid ? View.GONE : View.VISIBLE);
        if (touchedEmail) errEmail.setVisibility(emailValid ? View.GONE : View.VISIBLE);
        if (touchedPhone && !phone.isEmpty()) errPhone.setVisibility(phoneValid ? View.GONE : View.VISIBLE);

        // Badges de validation
        validNom.setVisibility(nomValid ? View.VISIBLE : View.GONE);
        validEmail.setVisibility(emailValid ? View.VISIBLE : View.GONE);
        validPhone.setVisibility(!phone.isEmpty() && phoneValid ? View.VISIBLE : View.GONE);
        validAdresse.setVisibility(!adresse.isEmpty() ? View.VISIBLE : View.GONE);
        validVille.setVisibility(!ville.isEmpty() ? View.VISIBLE : View.GONE);

        // Barre de progression
        int filledCount = 0;
        if (!nom.isEmpty()) filledCount++;
        if (!email.isEmpty()) filledCount++;
        if (!phone.isEmpty()) filledCount++;
        if (!adresse.isEmpty()) filledCount++;
        if (!ville.isEmpty()) filledCount++;

        progressBar.setProgress(filledCount);
        tvProgressLabel.setText(filledCount + " / 5");

        // État du bouton
        btnEnvoyer.setEnabled(nomValid && emailValid && phoneValid);
    }

    private boolean isFormValid() {
        String nom = etNom.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        return nom.length() >= 3 && 
               Patterns.EMAIL_ADDRESS.matcher(email).matches() && 
               (phone.isEmpty() || phone.matches("^\\+?[\\d\\s\\-()]{6,15}$"));
    }

    private void resetForm() {
        etNom.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etAdresse.setText("");
        etVille.setText("");
        touchedNom = touchedEmail = touchedPhone = false;
        validateForm();
    }
}