package com.example.chatapp.ui.screens.LoginScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.R
import com.example.chatapp.ui.screens.LoginScreen.components.LoginForm
import com.example.chatapp.ui.screens.LoginScreen.components.RegistrationForm
import com.example.chatapp.stateholders.AuthViewModel

@Composable
fun LoginScreen(
    navigateToHome: ()->Unit,
    viewModel: AuthViewModel
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){

        Text(
            modifier = Modifier.padding(vertical = 50.dp),
            text = stringResource(R.string.welcome_text),
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold
        )


        LoginRegisterMenu(
            navigateToHome = navigateToHome,
            viewModel = viewModel
        )

    }
}

@Composable
fun LoginRegisterMenu(
    navigateToHome: ()->Unit,
    viewModel: AuthViewModel
){
    var selectedLogin by rememberSaveable { mutableStateOf(true) }

    val formCardHeight by animateDpAsState(
        targetValue = if(selectedLogin) 250.dp else 350.dp
    )


    val cardColors = CardColors(
        contentColor = MaterialTheme.colorScheme.secondary,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        disabledContentColor = Color.White,
        disabledContainerColor = Color.White
    )

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){

        //ovaj row su samo 2 dugmeta za biranje login/register i odg animacija
        Row(
            modifier = Modifier
                .background(cardColors.containerColor)
                .width(250.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(modifier = Modifier.weight(0.5f)){
                LoginRegisterSelectionText(text = stringResource(R.string.login_selection),
                    modifier = Modifier.fillMaxWidth()
                        .clickable { selectedLogin = true },
                    color = cardColors.contentColor)
                AnimatedVisibility(
                    visible = selectedLogin,
                    enter =
                        slideIn(
                            initialOffset = { IntOffset(
                                it.width,
                                it.height
                            ) }),
                    exit = slideOut(

                    targetOffset = { IntOffset(x = 0, y = 0) }
                )

                ) {
                    HorizontalDivider(modifier = Modifier.width(125.dp), color = Color.White)
                }
            }

            Column(modifier = Modifier.weight(0.5f)){
                LoginRegisterSelectionText(text = stringResource(R.string.register_selection),
                    modifier = Modifier.fillMaxWidth()
                        .clickable { selectedLogin = false },
                    color = cardColors.contentColor)
                AnimatedVisibility(
                    visible = !selectedLogin,
                    enter =
                        slideIn(
                            initialOffset = { IntOffset(
                                -it.width,
                                -it.height
                            ) }),
                    exit = slideOut(

                        targetOffset = { IntOffset(x = 0, y = 0) }
                    )
                ) {
                    HorizontalDivider(modifier = Modifier.width(125.dp), color = Color.White)
                }

            }



        }

        //ovde su zapravo forme

        Card(
            modifier = Modifier.animateContentSize()
                .height(formCardHeight),
            colors = cardColors
        ) {

            Box{

                HorizontallySlidingContainer(
                    visible = selectedLogin,
                    direction = Direction.LEFT
                ){
                    LoginForm(navigateToHome, viewModel)
                }

                HorizontallySlidingContainer(
                    visible = !selectedLogin,
                    direction = Direction.RIGHT
                ){
                    RegistrationForm(navigateToHome, viewModel)
                }

            }

        }


    }

}

enum class Direction{
    RIGHT,
    LEFT
}



@Composable
fun HorizontallySlidingContainer(
    direction: Direction,
    visible: Boolean,
    content: @Composable()()->Unit
){
    androidx.compose.animation.AnimatedVisibility( //mora full name iz nekog razloga
        visible = visible,
        enter =
            slideInHorizontally(
                initialOffsetX = {if (direction == Direction.RIGHT)it else -it}
            ) + fadeIn(),
        exit = slideOutHorizontally(
            targetOffsetX = {if (direction == Direction.RIGHT)it else -it}
        ) + fadeOut(),
    ) {
        content()
    }
}

@Composable
fun LoginRegisterSelectionText(modifier: Modifier = Modifier,
                               text: String,
                               color: Color) {
    Text(text = text,
        modifier = modifier
            .padding(10.dp),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        color = color)
}



