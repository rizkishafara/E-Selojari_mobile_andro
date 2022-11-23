package com.example.e_selojari;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.e_selojari.util.AppController;
import com.example.e_selojari.util.FileUtils;
import com.example.e_selojari.util.ServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

public class StatusActivity extends AppCompatActivity {
    int bayar, kembali;
    TextView status, tanggal, nota, username, total;
    ImageView imgStatus, gambar;
    Button btnsimpan, btncetak, btngallery;
    ProgressDialog pd;
    DecimalFormat decimalFormat;
    GalleryPhoto mGalery;
    String encode_image = null;
    private final int TAG_GALLERY = 2222;
    String selected_photo = null;
    public static final String TAG_USERNAME = "username";
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        status = (TextView) findViewById(R.id.text_status);
        tanggal = (TextView) findViewById(R.id.tanggal);
        nota = (TextView) findViewById(R.id.no_nota);
        username = (TextView) findViewById(R.id.user);

        username.setText(getIntent().getStringExtra(TAG_USERNAME));
        total = (TextView) findViewById(R.id.total_biaya);
        total.setText(getIntent().getStringExtra("total"));
        imgStatus = (ImageView) findViewById(R.id.img_status);
        gambar = (ImageView) findViewById(R.id.inp_gambar);
        btngallery = (Button) findViewById(R.id.btn_gallery);
        btncetak = (Button) findViewById(R.id.btn_cetak);
        btnsimpan = (Button) findViewById(R.id.btn_simpan);
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        decimalFormat = new DecimalFormat("#,##0.00");
        pd = new ProgressDialog(StatusActivity.this);
        mGalery = new GalleryPhoto(getApplicationContext());
        loadJson();
        btngallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(mGalery.openGalleryIntent(), TAG_GALLERY);
            }
        });
        btncetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPDF();
            }
        });
        btnsimpan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (selected_photo != null) {
                    simpanData();
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Upload bukti pembayaran terlebih dahulu", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void loadJson() {
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_DASHBOARD_JUAL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject data = new JSONObject(response);

                            tanggal.setText(data.getString("tanggal"));
                            nota.setText(data.getString("no_nota"));
                            bayar = data.getInt("pembayaran");
                            kembali = data.getInt("kembalian");
                            total.setText("Rp. " + decimalFormat.format(data.getInt("total_biaya")));
                            if (data.getInt("status") == 1) {
                                status.setText("Sudah Dibayar");
                                status.setTextColor(Color.GREEN);
                                imgStatus.setImageResource(R.drawable.berhasil);
                                btngallery.setVisibility(View.GONE);
                                btnsimpan.setVisibility(View.GONE);

                                findViewById(R.id.rekening).setVisibility(View.GONE);
                            } else if (data.getInt("status") == 0) {
                                status.setText("Belum Dibayar");
                                status.setTextColor(Color.RED);
                                imgStatus.setImageResource(R.drawable.gagal);
                                btncetak.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> map = new HashMap<>();
                        map.put("username", username.getText().toString());
                        return map;
                    }
                };
        AppController.getInstance().addToRequestQueue(sendData, "json_obj_req");
    }
    private void simpanData() {
        pd.setMessage("Mengirim Data");
        pd.setCancelable(false);
        pd.show();
        try {
            Bitmap bitmap = ImageLoader.init().from(selected_photo).requestSize(1024, 1024).getBitmap();
            encode_image = ImageBase64.encode(bitmap);
            Log.d("ENCODER", encode_image);
            StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_UPDATE_STATUS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.cancel();
                    try {
                        JSONObject res = new JSONObject(response);
                        Toast.makeText(StatusActivity.this, "pesan : " + res.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                    intent.putExtra(TAG_USERNAME, username.getText().toString());
                    startActivity(intent);
                    finish();
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.cancel();
                            Toast.makeText(StatusActivity.this, "pesan : Gagal Kirim Data", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("no_nota", nota.getText().toString());
                    map.put("gambar", encode_image);
                    return map;
                }
            };

            AppController.getInstance().addToRequestQueue(sendData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TAG_GALLERY) {
                checkPermissionREAD_EXTERNAL_STORAGE(this);
                Uri uri_path = data.getData();
                mGalery.setPhotoUri(uri_path);
                String path = mGalery.getPath();
                selected_photo = path;
                try {
                    Bitmap bitmap;
                    bitmap = ImageLoader.init().from(path).requestSize(512, 512).getBitmap();
                    gambar.setImageBitmap(bitmap);
                    Snackbar.make(findViewById(android.R.id.content), "Success Loader Image", Snackbar.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), "Something Wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,Manifest.permission.READ_EXTERNAL_STORAGE);
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new
                                    String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    public void showDialog(final String msg, final Context context, final
    String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes, new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context, new String[] { permission }, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    public void createPDF() {
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        Document doc = new Document();
        String outPath = FileUtils.getAppPath(getApplicationContext()) + "/" + format + ".pdf";
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(outPath));
            doc.open();
            // Document Settings
            doc.setPageSize(PageSize.A4);
            doc.addCreationDate();
            doc.addAuthor("Ivan");
            doc.addCreator("Ivan");
            /**
             * How to USE FONT....
             */
            BaseFont urName = BaseFont.createFont("res/font/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
            /***
             * Variables for further use....
             */
            BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            float mHeadingFontSize = 20.0f;
            float mValueFontSize = 26.0f;
            Font mTitleFont = new Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK);
            Font mHeadingFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
            Font mValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
            // Order Details...
            // Title
            Chunk mTitleChunk = new Chunk("Invoice", mTitleFont);
            Paragraph mTitleParagraph = new Paragraph(mTitleChunk);
            mTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(mTitleParagraph);
            // Adding Line Breakable Space....
            doc.add(new Paragraph(""));
            // Adding Horizontal Line...
            doc.add(new Chunk(lineSeparator));
            // Adding Line Breakable Space....
            doc.add(new Paragraph(""));
            // Tanggal
            Chunk mDateChunk = new Chunk("Order Date:", mHeadingFont);
            Paragraph mOrderDateParagraph = new Paragraph(mDateChunk);
            doc.add(mOrderDateParagraph);
            Chunk mDateValueChunk = new Chunk(date, mValueFont);
            Paragraph mDateValueParagraph = new Paragraph(mDateValueChunk);
            doc.add(mDateValueParagraph);
            doc.add(new Paragraph(""));
            doc.add(new Chunk(lineSeparator));
            doc.add(new Paragraph(""));
            // Akun
            Chunk mAcNameChunk = new Chunk("Account Name:", mHeadingFont);
            Paragraph mAcNameParagraph = new Paragraph(mAcNameChunk);
            doc.add(mAcNameParagraph);
            Chunk mAcNameValueChunk = new Chunk(username.getText().toString(), mValueFont);
            Paragraph mAcNameValueParagraph = new Paragraph(mAcNameValueChunk);
            doc.add(mAcNameValueParagraph);
            //adds paragraph and line seperator
            doc.add(new Paragraph(""));
            doc.add(new Chunk(lineSeparator));
            doc.add(new Paragraph(""));
            // Total
            Chunk mAmountChunk = new Chunk("Total Amount:", mHeadingFont);
            Paragraph mAmountParagraph = new Paragraph(mAmountChunk);
            doc.add(mAmountParagraph);
            Chunk mAmountValueChunk = new Chunk(total.getText().toString(), mValueFont);
            Paragraph mAmountValueParagraph = new Paragraph(mAmountValueChunk);
            doc.add(mAmountValueParagraph);
            //adds paragraph and line seperator
            doc.add(new Paragraph(""));
            doc.add(new Chunk(lineSeparator));
            doc.add(new Paragraph(""));
            // Pembayaran
            Chunk mCashChunk = new Chunk("Cash:", mHeadingFont);
            Paragraph mCashParagraph = new Paragraph(mCashChunk);
            doc.add(mCashParagraph);
            Chunk mCashValueChunk = new Chunk("Rp. " + decimalFormat.format(bayar), mValueFont);
            Paragraph mCashValueParagraph = new Paragraph(mCashValueChunk);
            doc.add(mCashValueParagraph);
            //adds paragraph and line seperator
            doc.add(new Paragraph(""));
            doc.add(new Chunk(lineSeparator));
            doc.add(new Paragraph(""));
            // Kembalian
            Chunk mChangeChunk = new Chunk("Change:", mHeadingFont);
            Paragraph mChangeParagraph = new Paragraph(mChangeChunk);
            doc.add(mChangeParagraph);
            Chunk mChangeValueChunk = new Chunk("Rp. " + decimalFormat.format(kembali), mValueFont);
            Paragraph mChangeValueParagraph = new Paragraph(mChangeValueChunk);
            doc.add(mChangeValueParagraph);
            //adds paragraph and line seperator
            doc.add(new Paragraph(""));
            doc.add(new Chunk(lineSeparator));
            doc.add(new Paragraph(""));
            doc.close();
//            FileUtils.openFile(getApplicationContext(), new File(outPath));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}