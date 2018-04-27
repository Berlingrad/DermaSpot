package com.example.samx.dermaspot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//parse
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CaptureDetectActivity extends AppCompatActivity {

    ImageView imageView3;
    ImageButton capture, detect, gallery, upload;
    Boolean img;
    TextView result;
    Bitmap bitmap;
    Classifier classifier;
    String pictureDirectoryPaht;
    int SIZE = 299;
    int MEAN = 117;
    float STD = 1;
    String INPUT = "Mul";
    String OUTPUT = "final_result";
    String MODEL = "file:///android_asset/moles_quantized_graph.pb";
    String LABEL = "file:///android_asset/labels.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_detect);
        gallery = findViewById(R.id.gallery);
        img = false;
        imageView3 = findViewById(R.id.imageView3);
        result = findViewById(R.id.result);
        capture = findViewById(R.id.capture);
        detect= findViewById(R.id.detect);
        gallery = findViewById(R.id.gallery);
        upload = findViewById(R.id.upload);

        classifier = TensorFlowImageClassifier.create(
                getAssets(),MODEL,LABEL, SIZE,MEAN,STD,INPUT,OUTPUT);

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText("");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);
                img = true;
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText("");
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 2);
                img = true;

            }
        });

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (img) {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    float sW = (float) SIZE / (float) width;
                    float sH = (float) SIZE / (float) height;
                    Matrix matrix = new Matrix();
                    matrix.postScale(sW,sH);
                    Bitmap cropped = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);

                    List<Classifier.Recognition> results = classifier.recognizeImage(cropped);
                    String resultStr = results.get(0).toString().substring(3);
                    result.setText(resultStr);
                    if (resultStr.toLowerCase().contains("malignant".toLowerCase())) {
                        result.setTextColor(Color.RED);
                    } else if (resultStr.toLowerCase().contains("benign".toLowerCase())) {
                        result.setTextColor((Color.GREEN));
                    }
                    //System.out.print(results);
                    Toast.makeText(getApplicationContext(),resultStr, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Choose a Image", Toast.LENGTH_LONG).show();
                }

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (img) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    ParseFile file = new ParseFile("image.png", byteArray);
                    ParseUser user = ParseUser.getCurrentUser();

                    file.saveInBackground();
                    ParseObject image = new ParseObject("Image");
                    image.put("filename", file);
                    image.put("userId", user.getObjectId());
                    image.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(),"Succuess", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Upload Fail", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(),"Choose a Image", Toast.LENGTH_LONG).show();
                }

            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode==1) {
                bitmap = (Bitmap) data.getExtras().get("data");
                imageView3.setImageBitmap(bitmap);
            } else if (requestCode==2) {
                Uri selectedImageUri = data.getData();
                imageView3.setImageURI(selectedImageUri);
                BitmapDrawable drawable = (BitmapDrawable) imageView3.getDrawable();
                bitmap = drawable.getBitmap();

                //temperory compress
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                float sW = (float) SIZE / (float) width;
                float sH = (float) SIZE / (float) height;
                Matrix matrix = new Matrix();
                matrix.postScale(sW,sH);
                bitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
            }


        } else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}
