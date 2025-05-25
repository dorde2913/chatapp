package com.example.chatapp.ui.screens.ProfileScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfoRow(
    modifier: Modifier = Modifier,
    icon: @Composable()(modifier: Modifier, tint: Color)->Unit,
    label: String,
    value: String,
    editable: Boolean,
    updateData: (String)->Unit = {}
){
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth()
            .clickable(
                enabled = editable
            ) {
                showDialog = true
            },
        verticalAlignment = Alignment.CenterVertically
    ){
        icon(Modifier.padding(horizontal = 20.dp).size(30.dp), Color.Gray)

        Column{
            Text(
                text = label,
                fontSize = 19.sp,
                color = Color.White
            )

            Text(
                text = value,
                fontSize = 18.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        if (editable)
            Icon(Icons.Outlined.Edit,null, modifier = Modifier.padding(end = 20.dp))
    }


    if (showDialog){
        BasicAlertDialog(
            onDismissRequest = {showDialog = false},
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(15.dp)
            )
        ){
            var textFieldValue by remember{ mutableStateOf("") }
            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        label = {Text(label)},
                        value = textFieldValue,
                        onValueChange = {textFieldValue = it}
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        Button(onClick = { showDialog = false },
                        ) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            updateData(textFieldValue)
                            showDialog = false
                        }, enabled = textFieldValue.isNotEmpty()) {
                            Text("Confirm Change")
                        }
                    }

                }
            }
        }
    }
}