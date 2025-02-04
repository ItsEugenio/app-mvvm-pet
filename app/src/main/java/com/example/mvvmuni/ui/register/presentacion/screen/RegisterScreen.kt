package com.example.mvvmuni.ui.register.presentacion.screen

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvvmuni.R
import com.example.mvvmuni.MainActivity

import com.example.mvvmuni.ui.register.presentacion.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(viewModel: RegisterViewModel) {
    val loginError by viewModel.loginError.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) navigateToHome(context)
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Login(Modifier.align(Alignment.Center), viewModel)

        loginError?.let { errorMessage ->
            LaunchedEffect(errorMessage) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(errorMessage)
                    viewModel.clearLoginError()
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

private fun navigateToHome(context: Context) {
    context.startActivity(Intent(context, MainActivity::class.java))
}

@Composable
fun Login(modifier: Modifier, viewModel: RegisterViewModel) {
    val username by viewModel.username.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val email by viewModel.email.observeAsState("")
    val registerEnable by viewModel.registerEnable.observeAsState(false)

    Column(modifier = modifier) {

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "REGISTRARSE",
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF003566),
            modifier = Modifier.fillMaxWidth()
        )
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(16.dp))
        UserField(username) { viewModel.onRegisterChanged(it, password,email) }
        Spacer(modifier = Modifier.height(4.dp))
        PasswordField(password) { viewModel.onRegisterChanged(username, it,email) }
        Spacer(modifier = Modifier.height(4.dp))
        EmailField(email) { viewModel.onRegisterChanged(username,password,it)}
        Spacer(modifier = Modifier.height(8.dp))
        Login(Modifier.align(Alignment.End))
        Spacer(modifier = Modifier.height(16.dp))
        RegisterButton(registerEnable) { viewModel.registerSelected()}
    }
}

@Composable
fun RegisterButton(registerEnable: Boolean, registerSelected: () -> Unit) {
    Button(
        onClick = registerSelected,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color(0xFF003566),
            disabledContainerColor = Color.Gray
        ),
        enabled = registerEnable
    ) {
        Text(text = "Registrarse")
    }
}

@Composable
fun Login(modifier: Modifier) {
    val context = LocalContext.current
    Text(
        text = "Iniciar Sesion",
        modifier = modifier.clickable {
            val intent = Intent(context, MainActivity::class.java )
            context.startActivity(intent)
        },
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Blue
    )
}

@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onTextFieldChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Contraseña") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                )
            }
        }
    )
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.petlify),
        contentDescription = "Logo de Petlify",
        modifier = modifier
    )
}

@Composable
fun UserField(username: String, onTextFieldChanged: (String) -> Unit) {
    OutlinedTextField(
        value = username,
        onValueChange = onTextFieldChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Usuario") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true
    )
}

@Composable
fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = onTextFieldChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true
    )
}