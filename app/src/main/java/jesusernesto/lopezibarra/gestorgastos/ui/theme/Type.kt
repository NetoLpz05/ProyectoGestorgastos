package jesusernesto.lopezibarra.gestorgastos.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import jesusernesto.lopezibarra.gestorgastos.R
import androidx.compose.ui.text.font.FontStyle


val PlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal,   FontStyle.Normal),
    Font(R.font.plus_jakarta_sans, FontWeight.Medium,   FontStyle.Normal),
    Font(R.font.plus_jakarta_sans, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.plus_jakarta_sans, FontWeight.Bold,     FontStyle.Normal),
    Font(R.font.plus_jakarta_sans_italic, FontWeight.Normal, FontStyle.Italic),
)

val AppTypography = Typography(
    bodyLarge   = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.Normal),
    bodyMedium  = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.Normal),
    bodySmall   = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.Normal),
    titleLarge  = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.SemiBold),
    titleSmall  = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.Medium),
    labelLarge  = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.SemiBold),
    labelMedium = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.Medium),
    labelSmall  = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.Normal),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)