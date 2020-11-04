package app.mordred.differ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import app.mordred.diffgenerator.DiffGenerator;
import app.mordred.diffgenerator.util.DiffToHtmlParameters;
import app.mordred.diffgenerator.view.DiffView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    public static final int REQCODE= 1232;

    public static String workingDirPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "DiffWorkplace";
    public static String exampleLeftFilePath = workingDirPath + File.separator + "a1.txt";
    public static String exampleRightFilePath = workingDirPath + File.separator + "a2.txt";
    public static String exampleOutputFilePath = workingDirPath + File.separator + "diff.html";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        Button btn1 = findViewById(R.id.button);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareDirs();

                DiffToHtmlParameters parameters = DiffToHtmlParameters.builder()
                        .withInputLeftPath(exampleLeftFilePath)
                        .withInputRightPath(exampleRightFilePath)
                        .withOutputPath(exampleOutputFilePath)
                        .build();

                try {
                    int result = DiffGenerator.generateAndSaveDiff(parameters);
                    Toast.makeText(getApplicationContext(), (result != 0 ? "SUCCESS" : "ERROR"),
                            Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        DiffView diffView = findViewById(R.id.customDiffView);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareDirs();

                DiffToHtmlParameters parameters = DiffToHtmlParameters.builder()
                        .withInputLeftPath(exampleLeftFilePath)
                        .withInputRightPath(exampleRightFilePath)
                        .withOutputPath(exampleOutputFilePath) //TODO remove this, since its senseless
                        .build();

                try {
                    String resultHtml = DiffGenerator.generateDiff(parameters);
                    diffView.loadHtml(resultHtml);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void prepareDirs() {
        File fileWorkspace = new File(workingDirPath);
        if (!fileWorkspace.exists()) {
            fileWorkspace.mkdir();
        }

        copyFromAssetToSD(getApplicationContext(), "a1.txt", exampleLeftFilePath);
        copyFromAssetToSD(getApplicationContext(), "a2.txt", exampleRightFilePath);
    }

    private void copyFromAssetToSD(Context context, String assetFileName, String outputPath) {
        if (new File(outputPath).isFile()) {
            return;
        }
        InputStream instream = null;
        OutputStream outstream = null;
        try {
            //location we want the file to be at

            //get access to AssetManager
            final AssetManager assetManager = context.getAssets();

            //open byte streams for reading/writing
            instream = assetManager.open(assetFileName);
            outstream = new FileOutputStream(outputPath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outstream != null) {
                    outstream.flush();
                    outstream.close();
                }

                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQCODE);
        }
    }
}
