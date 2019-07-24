package com.application.bookclub;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView bookIconImageView;
    private TextView bookITextView;
    private ProgressBar loadingProgressBar;
    private RelativeLayout rootView, afterAnimationView;
    private TextView signuptextview;
    private Button loginbtn;
    private FirebaseAuth mAuth;
    EditText emailEditText,passwordEditText;
    boolean loginisactive=true;
    public static String username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initViews();
        mAuth = FirebaseAuth.getInstance();
        new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                bookITextView.setVisibility(GONE);
                loadingProgressBar.setVisibility(GONE);
                rootView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorSplashText));
                bookIconImageView.setImageResource(R.drawable.background_color_book);
                startAnimation();

            }
        }.start();


        signuptextview.setOnClickListener( MainActivity.this );
    }

    private void initViews() {
        bookIconImageView = findViewById(R.id.bookIconImageView);
        bookITextView = findViewById(R.id.bookITextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        rootView = findViewById(R.id.rootView);
        afterAnimationView = findViewById(R.id.afterAnimationView);
        signuptextview=findViewById( R.id.signuptext );
        loginbtn=findViewById( R.id.loginButton );
        emailEditText=findViewById( R.id.emailEditText );
        passwordEditText=findViewById( R.id.passwordEditText );

    }

    private void startAnimation() {
        ViewPropertyAnimator viewPropertyAnimator = bookIconImageView.animate();
        viewPropertyAnimator.x(50f);
        viewPropertyAnimator.y(100f);
        viewPropertyAnimator.setDuration(1000);
        viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //rootView.setVisibility( VISIBLE );
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                afterAnimationView.setVisibility(VISIBLE);
                //rootView.setVisibility( View.INVISIBLE );
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(loginisactive)
        {
            loginisactive=false;
            signuptextview.setText( "Login" );
            loginbtn.setText( "Sign Up" );


        }
        else
        {
            loginisactive=true;
            signuptextview.setText( "Sign Up" );
            loginbtn.setText( "Login" );
        }
    }


        public void loginclicked(View view) {
        if (emailEditText.getText().toString().equals( "" ) || passwordEditText.getText().toString().equals( "" )) {
            Toast.makeText( getApplicationContext(), "Both Email and password cannot be empty", Toast.LENGTH_SHORT ).show();
        } else {

            if (loginisactive) {
                mAuth.signInWithEmailAndPassword( emailEditText.getText().toString().trim(), passwordEditText.getText().toString().trim() ).addOnCompleteListener( new OnCompleteListener< AuthResult >() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText( getApplicationContext(), "Logged In", Toast.LENGTH_SHORT ).show();
                            username=emailEditText.getText().toString();
                            Intent intent = new Intent( getApplicationContext(),HomeActivity.class );
                            startActivity( intent );

                        } else {
                            Toast.makeText( getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT ).show();
                        }
                    }
                } );
            } else {
                mAuth.createUserWithEmailAndPassword( emailEditText.getText().toString(), passwordEditText.getText().toString() ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText( getApplicationContext(), "Signed Up", Toast.LENGTH_SHORT ).show();
                            Toast.makeText( getApplicationContext(), "Logged In", Toast.LENGTH_SHORT ).show();
                            username=emailEditText.getText().toString();
                            Intent intent = new Intent( getApplicationContext(),HomeActivity.class );
                            startActivity( intent );
                        } else {
                            Toast.makeText( getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT ).show();

                        }
                    }
                } );
            }
        }
    }
    }
