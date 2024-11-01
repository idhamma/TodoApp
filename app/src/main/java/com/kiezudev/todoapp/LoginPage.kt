package com.kiezudev.todoapp

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

private lateinit var auth: FirebaseAuth;
private lateinit var googleSingInClient: GoogleSignInClient

@Composable
fun LogoSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo (use Image or Text as placeholder)
        Image(painter = painterResource(id = R.drawable.group_1),
            contentDescription = null,
            modifier = Modifier
                .width(150.dp)
                .height(100.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))
        // Subtitle
        Text(
            text = "Learn Graphic and UI/UX designing in Hindi\nfor free with live projects.",
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.White
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

//input form section
@Composable
fun FormLoginSection(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val mContext = LocalContext.current

    fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(mContext, "Email and password must not be empty.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(mContext, "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun signin(email: String, password: String) {
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "signInWithEmail:success")

                    val user = auth.currentUser?.email
                    Toast.makeText(
                        mContext,
                        "Welcome, $user!",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigate(route = "home")
                } else {
                    task.exception?.let { exception ->
                        Log.w(ContentValues.TAG, "signInWithEmail:failure", exception)
                        Toast.makeText(
                            mContext,
                            "Authentication failed: ${exception.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Login failed", exception)
                Toast.makeText(mContext, "Error: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    Column {
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email Address",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            icon = Icons.Default.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            isPassword = true,
            icon = Icons.Default.Lock
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (validateInput(email, password)) {
                    signin(email, password)
                } else {
                    Toast.makeText(
                        mContext,
                        "Please fill all the text fields above",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("LOGIN", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Forgot Password?",
                color = Color(0xFFFF9800),
                modifier = Modifier
                    .clickable { /* Handle forgot password */ }
                    .padding(horizontal = 40.dp),
            )
        }
    }
}


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    icon: ImageVector
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .background(Color.White, RoundedCornerShape(25.dp))
            .clip(RoundedCornerShape(25.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                keyboardOptions = keyboardOptions,
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
            )

            Icon(
                imageVector = icon,
                contentDescription = placeholder,
                tint = Color.Gray,
                modifier = Modifier.padding(end = 16.dp)
            )
        }

        if (value.isEmpty()) {
            Text(
                text = placeholder,
                color = Color.Gray,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterStart)
            )
        }
    }
}


@Composable
fun AltLogin(navController: NavHostController) {
    val context = LocalContext.current
    val auth: FirebaseAuth = Firebase.auth

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task, auth) { success ->
                    if (success) {
                        Log.d("LoginSuccess", "Google Sign-In successful.")
                        navController.navigate("home")
                    } else {
                        Log.d("LoginFailed", "Google Sign-In failed.")
                    }
                }
            } catch (e: ApiException) {
                Log.e("ApiException", "Sign-in failed with status code: ${e.statusCode}")
            }
        } else {
            Log.e("SignInError", "Result code: ${result.resultCode}")
            Toast.makeText(context, "Google Sign-In failed or canceled", Toast.LENGTH_SHORT).show()
        }
    }



    fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }



    //Alt login
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(WindowInsets.navigationBars.asPaddingValues()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("or login with", color = Color.White)

        Spacer(modifier = Modifier.height(35.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Google login button
            IconButton(
                onClick = { signInWithGoogle() },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gmail),
                    contentDescription = "Google",
                    modifier = Modifier.size(40.dp)
                )
            }

            // Facebook button
            IconButton(
                onClick = { /* Handle Facebook login */ },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.facebook),
                    contentDescription = "Facebook",
                    modifier = Modifier.size(40.dp)
                )
            }

            // Twitter button
            IconButton(
                onClick = { /* Handle Twitter login */ },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.twitter),
                    contentDescription = "Twitter",
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // Registration prompt
        Spacer(modifier = Modifier.height(35.dp))
        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Text("Don't have an account? ", color = Color.White)
            Text(
                text = "Register now",
                color = Color(0xFFFF9800),
                fontWeight = FontWeight.Bold,
                style = TextStyle(textDecoration = TextDecoration.Underline),
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }
    }
}

fun handleSignInResult(
    task: Task<GoogleSignInAccount>,
    auth: FirebaseAuth,
    onComplete: (Boolean) -> Unit
) {
    try {
        val account = task.getResult(ApiException::class.java)
        val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        Log.d("FirebaseAuth", "User is signed in: ${user.email}")
                        onComplete(true)
                    } else {
                        Log.d("FirebaseAuth", "Sign-in failed: User not found.")
                        onComplete(false)
                    }
                } else {
                    Log.w("FirebaseAuth", "signInWithCredential:failure", authTask.exception)
                    onComplete(false)
                }
            }
    } catch (e: ApiException) {
        Log.w("FirebaseAuth", "Google sign-in failed", e)
        onComplete(false)
    }
}


fun handleSignInResult(task: Task<GoogleSignInAccount>, auth: FirebaseAuth, any: Any) {

}


//overall section
@Composable
fun LoginPage(navController: NavHostController){


    Box(modifier = Modifier.fillMaxSize()){

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color(0xFF81248A)),
        ) {
            LogoSection()

            Spacer(modifier = Modifier.height(40.dp))

            FormLoginSection(navController = navController)

            Spacer(modifier = Modifier.height(40.dp))
            AltLogin(navController = navController)

        }
    }
}


@Preview()
@Composable
fun loginPagePreview(){
    val navController = rememberNavController()
    LoginPage(navController = navController)
}
