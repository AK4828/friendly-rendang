package id.merv.cdp.book.adapter;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.meruvian.dnabook.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.Bind;

/**
 * Created by akm on 23/03/16.
 */
public class PrintAdapter extends PrintDocumentAdapter {

    Context context;
    public PdfDocument pdfDocument;
    public int totalPages;
    private String filePath;

    public PrintAdapter(Context context, String filePath) {
        this.context = context;
        this.filePath = filePath;
    }

    @Override
    public void onLayout(PrintAttributes printAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
        pdfDocument = new PrintedPdfDocument(context, newAttributes);

        if (cancellationSignal.isCanceled()) {
            layoutResultCallback.onLayoutCancelled();
            return;
        }

        try {
            PdfReader pdfReader = new PdfReader(filePath);
            totalPages = pdfReader.getNumberOfPages();
            if (totalPages > 0) {
                PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                        .Builder("print_output.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(totalPages);

                PrintDocumentInfo info = builder.build();
                layoutResultCallback.onLayoutFinished(info, true);
            } else {
                layoutResultCallback.onLayoutFailed("No document page found");
                Toast.makeText(context, "No document page found", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = new FileInputStream(new File(filePath));
            outputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());

            byte[] buf = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "No document page found", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Choose document first", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                Toast.makeText(context, "Choose document first", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}
