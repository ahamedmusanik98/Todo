package com.example.todo.presentation.Home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todo.presentation.Home_screen.components.AlertDialog_HomeSc
import com.example.todo.presentation.Home_screen.components.EmptyTaskScreen
import com.example.todo.presentation.Home_screen.components.TodoCard
import com.example.todo.presentation.MainViewModel
import com.example.todo.presentation.common.mySnackbar
import com.example.todo.presentation.common.topAppBarTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    onUpdate: (id:Int) -> Unit

){
val todos by
        mainViewModel.getAllTodos.collectAsStateWithLifecycle(initialValue = emptyList())
    var openDialog by rememberSaveable {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val snackerHostState = remember {SnackbarHostState()}
    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackerHostState)},
        topBar = {
            TopAppBar(title =
            { Text(text = "Todos",
                style = topAppBarTextStyle)})
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {openDialog=true}) {
             Icon(
                 imageVector = Icons.Rounded.Add,
                 contentDescription = null
             )

        }
        }
    ){paddingValues: PaddingValues ->
AlertDialog_HomeSc(
    openDialog=openDialog,
    onClose = {openDialog=false},
    mainViewModel =mainViewModel
)
        if (todos.isEmpty()){
            EmptyTaskScreen(paddingValues = paddingValues)
        }else{
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(
                    items =todos,
                    key = {it.id}
                    ){todo ->
TodoCard(
    todo =todo ,
    onDone = {mainViewModel.deleteTodo(todo = todo)
             mySnackbar(
                 scope=scope,
                 snackbarHostState = snackerHostState,
                 msg = "DONE! ->\"${todo.task}\"",
                 actionLabel = "UNDO",
                 onAction = {mainViewModel.undoDeletedTodo()}
             )
             },
    onUpdate =onUpdate
)
                }
            }
        }
    }
}