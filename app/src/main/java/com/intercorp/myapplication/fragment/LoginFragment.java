package com.intercorp.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.intercorp.myapplication.R;
import com.intercorp.myapplication.databinding.FragmentLoginBinding;

import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment {

    private String mVerificationId;
    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider .OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        mAuth = FirebaseAuth.getInstance();
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                binding.loading.setVisibility(View.GONE);
                binding.buttonLogin.setVisibility(View.VISIBLE);
                Snackbar.make(getView(), e.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                binding.loading.setVisibility(View.GONE);
                binding.phone.setEnabled(false);
                binding.code.setVisibility(View.VISIBLE);
                binding.buttonLogin.setVisibility(View.GONE);
                binding.buttonVerify.setVisibility(View.VISIBLE);
                mVerificationId = verificationId;
                Snackbar.make(getView(), "Codigo enviado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        };

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.phone.getText().toString().trim().isEmpty()) {
                    Snackbar.make(getView(), "Ingrese un número", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (binding.phone.getText().toString().trim().length() !=9) {
                    Snackbar.make(getView(), "Ingrese un número válido", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    binding.buttonLogin.setVisibility(View.GONE);
                    binding.loading.setVisibility(View.VISIBLE);
                    smsSend();
                }
            }
        });

        binding.buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.code.getText().toString().trim().isEmpty()) {
                    Snackbar.make(getView(), "Ingrese un código", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (binding.code.getText().toString().trim().length() !=6) {
                    Snackbar.make(getView(), "Ingrese un código válido", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    binding.buttonVerify.setVisibility(View.GONE);
                    binding.loading.setVisibility(View.VISIBLE);
                    verifyPhoneNumberWithCode(mVerificationId, binding.code.getText().toString().trim());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void smsSend() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+51" + binding.phone.getText().toString().trim())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(getActivity())
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void verifyPhoneNumberWithCode(String verifitacionId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifitacionId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        binding.loading.setVisibility(View.GONE);
                        binding.buttonVerify.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Verificado correctamente",
                                Toast.LENGTH_LONG).show();
                        NavHostFragment.findNavController(LoginFragment.this)
                                .navigate(R.id.action_FirstFragment_to_SecondFragment);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.loading.setVisibility(View.GONE);
                        binding.buttonVerify.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), e.getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

}