package com.example.basiclogins;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextEmail;
    private Button buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        wireWidgets();
        prefillUserName();
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerOnBackendless();
            }
        });

    }

    private void registerOnBackendless() {
        //verify all fields are filled out and passwords are the same
        String password = editTextPassword.getText().toString();
        String confirmpassword = editTextConfirmPassword.getText().toString();
        String username = editTextUsername.getText().toString();
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();

        if (allFieldsValid(password, confirmpassword, username, email, name)){
            //make the registration call
            BackendlessUser user = new BackendlessUser();
            user.setProperty( "email", email);
            user.setPassword( password );
            user.setProperty("name", name);
            user.setProperty("username", username);

            Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
            {
                public void handleResponse( BackendlessUser registeredUser )
                {
                    // user has been registered and now can login
                    Toast.makeText(CreateAccountActivity.this, registeredUser.getUserId() + "has registered.", Toast.LENGTH_SHORT).show();
                finish();
                }

                public void handleFault( BackendlessFault fault )
                {
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                    Toast.makeText(CreateAccountActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();

                }
            } );
        }




        //return to the login activity in the handleResponse



    }

    private boolean allFieldsValid(String password, String confirmpassword, String username, String email, String name) {
        //TODO validate all fields
        return password.equals(confirmpassword) && username.length()>0;

    }

    private void prefillUserName() {
        String username = getIntent().getStringExtra(LoginActivity.EXTRA_USERNAME);
        if(username != null) {
            editTextUsername.setText(username);
        }
    }

    private void wireWidgets() {
        editTextUsername = findViewById(R.id.edittext_create_username);
        editTextName = findViewById(R.id.edittext_create_name);
        editTextPassword = findViewById(R.id.edittext_create_password);
        editTextConfirmPassword = findViewById(R.id.edittext_create_confirmpassword);
        editTextEmail = findViewById(R.id.edittext_create_email);
        buttonCreateAccount = findViewById(R.id.button_create_account);

    }
}