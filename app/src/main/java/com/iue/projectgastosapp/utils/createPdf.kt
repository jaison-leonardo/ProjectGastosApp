package com.iue.projectgastosapp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.core.content.FileProvider
import com.iue.projectgastosapp.firebase.dataobjects.DataMonth
import java.io.File
import java.io.FileOutputStream


fun generatePDF(context: Context, title: String, logo: Bitmap, data: List<DataMonth>) {
    val pageHeight = 1120
    val pageWidth = 792

    val pdfDocument = PdfDocument()

    val paint = Paint()
    paint.textSize = 12F

    val myPageInfo: PdfDocument.PageInfo? =
        PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)
    val canvas: Canvas = myPage.canvas

    drawTitleAndLogo(canvas, title, logo)

    drawTable(
        canvas,
        40F,
        170F,
        listOf("Mes", "Entretenimiento", "Transporte", "Alimentación", "Otros"),
        data,
        paint
    )

    pdfDocument.finishPage(myPage)

    val mpath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val file = File(mpath, "Report_SE.pdf")
    Log.i("File_pdf", file.path)

    try {
        pdfDocument.writeTo(FileOutputStream(file))
        openPDFFile(context, file)
        Toast.makeText(context, "Archivo PDF generado..", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Log.i("ExceptionFile", e.message.toString())
        Toast.makeText(context, "Fallo al generar el archivo PDF..", Toast.LENGTH_SHORT).show()
    }

    pdfDocument.close()
}

fun drawTable(
    canvas: Canvas,
    startX: Float,
    startY: Float,
    headers: List<String>,
    data: List<DataMonth>,
    paint: Paint
) {
    val cellWidth = 120F
    val cellHeight = 40F
    val tableWidth = cellWidth * (headers.size + 1)

    var x = startX
    var y = startY
    paint.color = Color(0xFF4472C3).hashCode()
    canvas.drawRect(x, y, x + tableWidth, y + cellHeight, paint)
    paint.color = Color(0xFFFEFEFE).hashCode()
    paint.textAlign = Paint.Align.CENTER
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    headers.forEachIndexed { index, header ->
        canvas.drawText(header, x + cellWidth * (index + 1 / 2F), y + cellHeight / 2, paint)
    }

    // Draw table data
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    data.forEachIndexed { rowIndex, rowData ->
        x = startX
        y = startY + cellHeight * (rowIndex + 1)
        if (rowIndex % 2 == 0) paint.color = Color(0xFFE9EDF7).hashCode()
        else paint.color = Color(0xFFFEFEFE).hashCode()
        canvas.drawRect(x, y, x + tableWidth, y + cellHeight, paint)
        paint.color = Color.Black.hashCode()
        if (rowData.month == "Total") {
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(rowData.month.replaceFirstChar { it.uppercase() }, x + cellWidth / 2, y + cellHeight / 2, paint)
        canvas.drawText(
            formatToCurrencyCop(rowData.entertainment),
            x + cellWidth * 1.5F,
            y + cellHeight / 2,
            paint
        )
        canvas.drawText(
            formatToCurrencyCop(rowData.transportation),
            x + cellWidth * 2.5F,
            y + cellHeight / 2,
            paint
        )
        canvas.drawText(
            formatToCurrencyCop(rowData.food),
            x + cellWidth * 3.5F,
            y + cellHeight / 2,
            paint
        )
        canvas.drawText(
            formatToCurrencyCop(rowData.others),
            x + cellWidth * 4.5F,
            y + cellHeight / 2,
            paint
        )
    }
}


fun drawTitleAndLogo(canvas: Canvas, title: String, logo: Bitmap) {
    val paint = Paint()
    paint.textSize = 20f
    paint.color = Color.Black.hashCode()
    paint.textAlign = Paint.Align.CENTER
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

    // Draw title
    val titleX = canvas.width / 2F
    val titleY = 100F
    canvas.drawText(title, titleX, titleY, paint)
    val logoWidth = 100 // Tamañp del logo
    val logoHeight = (logo.height.toFloat() / logo.width * logoWidth).toInt()

    val logoX = canvas.width - logoWidth - 20F
    val logoY = 20F
    canvas.drawBitmap(logo, null, RectF(logoX, logoY, logoX + logoWidth, logoY + logoHeight), null)
}

fun openPDFFile(context: Context, file: File) {
    val pdfUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(pdfUri, "application/pdf")
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION

    context.startActivity(intent)
}