package com.kiezudev.todoapp

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

private lateinit var auth: FirebaseAuth;

//input form section
@Composable
fun FormRegisterSection(navController: NavHostController) {
    var name by remember{ mutableStateOf("")}
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVal by remember { mutableStateOf("") }

    val mContext = LocalContext.current

    fun signup(email: String, password: String) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "signInWithEmail:success")

                    val user = auth.currentUser?.email
                    Toast.makeText(
                        mContext,
                        user.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigate(route = "home")
                } else {
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                }
                Toast.makeText(
                    mContext,
                    "failed",
                    Toast.LENGTH_SHORT
                )
            }
    }

    Column {
        Box(modifier = Modifier,
            Alignment.CenterStart
        ){
            //Nama
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .background(Color.White, RoundedCornerShape(25.dp))
                    .clip(RoundedCornerShape(25.dp)),
                Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicTextField(
                        value = name,
                        onValueChange = {name = it},
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        keyboardOptions = KeyboardOptions.Default,
                        visualTransformation = VisualTransformation.None
                    )

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "",
                        tint = Color.Gray,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
                            }
                    )
                }

                if (name.isEmpty()) {
                    Text(
                        text = "Name",
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterStart)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        //email
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .background(Color.White, RoundedCornerShape(25.dp))
                .clip(RoundedCornerShape(25.dp)),
            Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicTextField(
                    value = email,
                    onValueChange = {email = it},
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    keyboardOptions = KeyboardOptions.Default,
                    visualTransformation = VisualTransformation.None
                )

                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Addres",
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            if (email.isNotBlank()) {
                            }
                        }
                )
            }

            if (email.isEmpty()) {
                Text(
                    text = "Email Address",
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterStart)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        //password
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .background(Color.White, RoundedCornerShape(25.dp))
                .clip(RoundedCornerShape(25.dp)),
            Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicTextField(
                    value = password,
                    onValueChange = {password = it},
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    keyboardOptions = KeyboardOptions.Default,
                    visualTransformation =  PasswordVisualTransformation()
                )

                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password",
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(end = 16.dp)

                )
            }

            if (password.isEmpty()) {
                Text(
                    text = "Password",
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterStart)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        //re-enter password
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .background(Color.White, RoundedCornerShape(25.dp))
                .clip(RoundedCornerShape(25.dp)),
            Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicTextField(
                    value = passwordVal,
                    onValueChange = {passwordVal = it},
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    keyboardOptions = KeyboardOptions.Default,
                    visualTransformation =  PasswordVisualTransformation()
                )

                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password",
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(end = 16.dp)

                )
            }

            if (passwordVal.isEmpty()) {
                Text(
                    text = "Re-enter Password",
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterStart)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (name.isNotEmpty()  && email.isNotEmpty() && password.isNotEmpty() && passwordVal.isNotEmpty()) {
                    if (password == passwordVal) {
                        signup(email, password)
                    }else {
                        Toast.makeText(
                            mContext,
                            "Please fill same password",
                            Toast.LENGTH_LONG
                        ).show()
                    }
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
            Text("REGISTER", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Already have account?",
                color = Color(0xFFFF9800),
                modifier = Modifier
                    .clickable { /* Handle forgot password */ }
                    .padding(horizontal = 40.dp)
                    .clickable {
                        navController.navigate("login")
                    }
            )
        }
    }
}

@Composable
fun RegisterPage(navController: NavHostController){

    Box(modifier = Modifier.fillMaxSize()){

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color(0xFF81248A)),
        ) {
            LogoSection()

            Spacer(modifier = Modifier.height(40.dp))

            FormRegisterSection(navController = navController)

            Spacer(modifier = Modifier.height(40.dp))
//            AltLogin()

        }
    }
}



@Preview()
@Composable
fun RegsiterPagePreview(){
    val navController = rememberNavController()
    RegisterPage(navController = navController)
}
