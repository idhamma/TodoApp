package com.kiezudev.todoapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.launch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import com.google.firebase.auth.FirebaseAuth


@Preview()
@Composable
fun ToDoPagePreview(){
    Column {
        val navController = rememberNavController()
        TodoApp(navController = navController)
    }
}


// To Do App

@Composable
fun TodoApp(navController: NavHostController) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF81248A),
            Color(0xFFE57373),
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        ToDoHeader(navController = navController)

        Spacer(modifier = Modifier.height(35.dp))

        ToDoBody()
    }
}

@Composable
fun ToDoHeader(navController: NavHostController){
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    var userEmail: String = ""

    if (currentUser != null) {
        userEmail = currentUser.email.toString()
    }

    Box(modifier = Modifier
        .fillMaxWidth(),
        Alignment.BottomStart
    ){
        //transparent bg
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .alpha(0.3f)
            .background(Color.White),
            Alignment.BottomStart
        ){
        }

        //user info
        Row (modifier = Modifier
            .padding(bottom = 10.dp)
            .alpha(0.8f)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){

            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Logout",
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 16.dp)

            )

            Text(text = "Hello, $userEmail",
                modifier = Modifier.padding(5.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Box(modifier = Modifier
                .fillMaxWidth(),
                Alignment.CenterEnd){
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(end = 20.dp)
                        .clickable {
                            auth.signOut()
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                )
            }
        }
    }
}




@Composable
fun ToDoBody() {
    val coroutineScope = rememberCoroutineScope()
    var todoList by remember { mutableStateOf(listOf<Task>()) }
    var taskName by remember { mutableStateOf("") }
    val maxVisibleItems = 8
    val visibleItems = min(todoList.size, maxVisibleItems)
    val lazyListState = rememberLazyListState()

    var userEmail: String = ""
    var userId: String = ""
    val mContext = LocalContext.current

    val database = Firebase.database
    val auth = Firebase.auth

    // Fetch tasks from Firebase
    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userEmail = currentUser.email.toString()
            userId = currentUser.uid

            database.getReference("notes").child(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tasks = mutableListOf<Task>()
                    for (taskSnapshot in snapshot.children) {
                        val task = taskSnapshot.getValue(Task::class.java)
                        task?.let { tasks.add(it) }
                    }
                    todoList = tasks
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }


    fun saveTask() {
        val currentUser = auth.currentUser
        if (currentUser != null && taskName.isNotBlank()) {
            val myRef = database.getReference("notes").child(currentUser.uid).push()
            val newTask = Task(id = myRef.key, name = taskName, isCompleted = false)
            myRef.setValue(newTask).addOnSuccessListener {
                taskName = ""
                Toast.makeText(mContext, "New task added successfully", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Fungsi untuk menghapus task
    fun deleteTask(task: Task) {
        val currentUser = auth.currentUser
        if (currentUser != null && task.id != null) {
            database.getReference("notes").child(currentUser.uid).child(task.id).removeValue()
        }
    }


    // Fungsi untuk mengupdate status completion task
    fun updateTaskCompletion(task: Task, isCompleted: Boolean) {
        val currentUser = auth.currentUser
        if (currentUser != null && task.id != null) {
            database.getReference("notes").child(currentUser.uid).child(task.id).child("isCompleted").setValue(isCompleted)
        }
    }


    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(id = R.drawable.notebook_header2),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
            )

            if (todoList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height((48 * visibleItems).dp)
                        .background(Color.White)
                ) {
                    items(todoList) { task ->
                        ToDoItem(
                            task = task,
                            onDelete = {
                                deleteTask(task)
                            },
                            onToggleComplete = { isCompleted ->
                                updateTaskCompletion(task, isCompleted)
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier,
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painterResource(id = R.drawable.notebook_body),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.90.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = taskName,
                            onValueChange = { taskName = it },
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp),
                            keyboardOptions = KeyboardOptions.Default,
                            visualTransformation = VisualTransformation.None
                        )

                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Task",
                            tint = Color.Gray,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable {
                                    if (taskName.isNotBlank()) {
                                        saveTask()
                                        coroutineScope.launch {
                                            lazyListState.animateScrollToItem(todoList.size)
                                        }
                                    }
                                }
                        )
                    }

                    if (taskName.isEmpty()) {
                        Text(
                            text = "Add Task",
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterStart)
                        )
                    }
                }
            }

            Image(
                painterResource(id = R.drawable.notebook_bottom),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(35.dp)
            )
        }
    }
}

@Composable
fun ToDoItem(task: Task, onDelete: () -> Unit, onToggleComplete: (Boolean) -> Unit) {
    var isChecked by remember { mutableStateOf(task.isCompleted) }
    var showDialog by remember { mutableStateOf(false) }
    var editedTaskName by remember { mutableStateOf(task.name) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { newValue ->
                isChecked = newValue
                onToggleComplete(newValue)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .padding(end = 10.dp)
                .weight(1f),
            text = task.name,
            style = if (isChecked) {
                TextStyle(
                    textDecoration = TextDecoration.LineThrough,
                    color = Color.Gray
                )
            } else {
                TextStyle(textDecoration = TextDecoration.None)
            }
        )

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit Task",
            tint = Color.Gray,
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable {
                    editedTaskName = task.name
                    showDialog = true
                }
        )

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Task",
            tint = Color.Gray,
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable {
                    onDelete()
                }
        )
    }

    // Dialog for editing task
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Edit Task") },
            text = {
                BasicTextField(
                    value = editedTaskName,
                    onValueChange = { editedTaskName = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 18.sp)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        updateTaskName(task, editedTaskName)
                        showDialog = false
                    }
                ) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Function to update task name in Firebase
fun updateTaskName(task: Task, newName: String) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null && task.id != null) {
        Firebase.database.getReference("notes").child(currentUser.uid).child(task.id).child("name").setValue(newName)
    }
}
