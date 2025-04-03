package com.example.registeruser.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.registeruser.components.ErrorDialog
import com.example.registeruser.components.MyPasswordField
import com.example.registeruser.components.MyTextField
import com.example.registeruser.database.AppDatabase
import com.example.registeruser.ui.theme.RegisterUserTheme

@Composable
fun RegisterUserMainScreen() {
    val ctx = LocalContext.current
    val userDao = AppDatabase.getDatabase(ctx).userDao()
    val registerUserViewModel : RegisterUserViewModel =
        viewModel(
            factory = RegisterUserViewModelFactory(userDao)
        )

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            RegisterUserFields(registerUserViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserFields(registerUserViewModel: RegisterUserViewModel) {
    var registerUser = registerUserViewModel.uiState.collectAsState()
    val ctx = LocalContext.current

    MyTextField(
        label = "User",
        value = registerUser.value.user,
        onValueChange = {
            registerUserViewModel.onUserChange(it)
        },
    )
    MyTextField(
        label = "E-mail",
        value = registerUser.value.email,
        onValueChange = {
            registerUserViewModel.onEmailChange(it)
        }
    )
    MyPasswordField(
        label = "Password",
        value = registerUser.value.password,
        errorMessage = registerUser.value.validatePassord(),
        onValueChange = {
            registerUserViewModel.onPasswordChange(it)
        })
    MyPasswordField(
        label = "Confirm password",
        value = registerUser.value.confirmPassword,
        errorMessage = registerUser.value.validateConfirmPassword() ,
        onValueChange = {
            registerUserViewModel.onConfirmPassword(it)
        })

    Button(
        modifier = Modifier.padding(top = 16.dp),
        onClick = {
             registerUserViewModel.register()
        }
    ) {
        Text(text = "Register user")
    }

    if (registerUser.value.errorMessage.isNotBlank()) {
        ErrorDialog(
            error = registerUser.value.errorMessage,
            onDismissRequest =  {
                registerUserViewModel.cleanDisplayValues()
            }
        )
    }

    LaunchedEffect(registerUser.value.isSaved){
        if (registerUser.value.isSaved){
            Toast.makeText(ctx, "User registered",
                Toast.LENGTH_SHORT).show()
            registerUserViewModel.cleanDisplayValues()
        }
    }

}







@Composable
@Preview(showSystemUi = true, showBackground = true, device = "id:Galaxy Nexus")
fun RegisterUserPreview() {
    RegisterUserTheme {
        RegisterUserMainScreen()
    }
}