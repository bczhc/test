package filepicker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.*;
import com.zhc.codec.Main;
import com.zhc.codec.R;

import java.io.File;
import java.io.IOException;

public class Picker extends AppCompatActivity {
    private Toast notHavePermissionAccessToast = null;
    @SuppressWarnings("unused")
    public static int PICK_FILE = 1;
    @SuppressWarnings("unused")
    public static int PICK_FOLDER = 2;
    private String resultString = "";
    private TextView pathView;
    private File currentPath;
    private LinearLayout ll;
    private LinearLayout.LayoutParams lp;
    private int grey = Color.parseColor("#DCDCDC");
    private int white = Color.WHITE;
    private final int[] justPicked = new int[]{-1};
    private int option = 1;
    private Main main_o = new Main();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        } else {
            D();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == -1) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1);
            } else {
                D();
            }
        }
    }

    private void D() {
        Intent intent = getIntent();
        this.option = intent.getIntExtra("option", 0);
        setContentView(R.layout.picker);
        this.lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.currentPath = Environment.getExternalStorageDirectory();
//        this.currentPath = new File("/storage/emulated/0");
        lp.setMargins(2, 10, 10, 0);
        Button cancel = findViewById(R.id.cancel);
        Button ok = findViewById(R.id.pick);
        cancel.setOnClickListener(v -> {
            setResult(3, null);
            finish();
        });
        ok.setOnClickListener(v -> {
            Intent r = new Intent();
            switch (option) {
                case 1:
                    r.putExtra("result", resultString);
                    break;
                case 2:
                    String dir = null;
                    try {
                        dir = currentPath.getCanonicalPath();
                    } catch (IOException e) {
                        main_o.showException(e, this);
                    }
                    r.putExtra("result", dir);
                    break;
            }
            this.setResult(3, r);
            finish();
        });
        this.pathView = findViewById(R.id.textView);
        this.pathView.setOnClickListener((v) -> {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            EditText et = new EditText(this);
            et.setText(String.format(getResources().getString(R.string.tv), pathView.getText().toString()));
            et.setLayoutParams(lp);
            ad.setTitle("输入路径")
                    .setPositiveButton("确定", (dialog, which) -> {
                        File f = new File(et.getText().toString());
                        if (f.isFile() && option == 1) {
                            Intent r = new Intent();
                            try {
                                r.putExtra("result", f.getCanonicalFile());
                            } catch (IOException e) {
                                main_o.showException(e, this);
                            }
                            this.setResult(3, r);
                            finish();
                        } else {
                            File[] listFiles = f.listFiles();
                            this.currentPath = f;
                            fillViews(listFiles, lp, grey, justPicked, ll);
                        }
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                    })
                    .setView(et).show();
        });
        this.ll = findViewById(R.id.ll);
        new Thread(() -> {
            File[] listFiles = currentPath.listFiles();
            fillViews(listFiles, lp, grey, justPicked, ll);
        }).start();
    }

    @Override
    public void onBackPressed() {
        previous();
    }

    private void fillViews(File[] listFiles, LinearLayout.LayoutParams lp, int unselectedColor, int[] justPicked, LinearLayout ll) {
        new Thread(() -> {
            runOnUiThread(() -> {
                ll.removeAllViews();
                try {
                    pathView.setText(String.format("%s", currentPath.getCanonicalFile()));
                } catch (IOException e) {
                    main_o.showException(e, this);
                }
            });
            TextView[] textViews;
            int length = 0;
            try {
                length = listFiles.length;
            } catch (Exception e) {
                runOnUiThread(() -> {
                    if (notHavePermissionAccessToast != null) notHavePermissionAccessToast.cancel();
                    notHavePermissionAccessToast = Toast.makeText(this, "此处无权访问", Toast.LENGTH_SHORT);
                    notHavePermissionAccessToast.show();
                });
                e.printStackTrace();
            }
            textViews = new TextView[length];
            switch (option) {
                case 1:
                    for (int i = 0; i < length; i++) {
                        final int finalI = i;
                        extractM1(listFiles, lp, unselectedColor, textViews, i);
                        textViews[i].setOnClickListener(v -> {
                            File currentFile = listFiles[finalI];
                            if (currentFile.isFile()) {
                                Drawable background = textViews[finalI].getBackground();
                                ColorDrawable colorDrawable = (ColorDrawable) background;
                                runOnUiThread(() -> {
                                    if (colorDrawable.getColor() == Color.GREEN) {
                                        textViews[finalI].setBackgroundColor(Color.WHITE);
                                    } else {
                                        if (justPicked[0] != -1)
                                            textViews[justPicked[0]].setBackgroundColor(Color.WHITE);
                                        textViews[finalI].setBackgroundColor(Color.GREEN);
                                        justPicked[0] = finalI;
                                    }
                                });
                                try {
                                    resultString = listFiles[justPicked[0]].getCanonicalPath();
                                } catch (IOException e) {
                                    main_o.showException(e, Picker.this);
                                }
                            } else {
                                runOnUiThread(() -> {
                                    this.currentPath = currentFile;
                                    File[] listFiles1 = currentFile.listFiles();
                                    fillViews(listFiles1, lp, unselectedColor, justPicked, ll);
                                });
                            }
                        });
                        runOnUiThread(() -> ll.addView(textViews[finalI]));
                    }
                    break;
                case 2:
                    for (int i = 0; i < length; i++) {
                        extractM1(listFiles, lp, unselectedColor, textViews, i);
                        final int finalI = i;
                        textViews[i].setOnClickListener(v -> {
                            File currentFile = listFiles[finalI];
                            if (currentFile.isDirectory()) {
                                runOnUiThread(() -> {
                                    this.currentPath = currentFile;
                                    File[] listFiles1 = currentFile.listFiles();
                                    fillViews(listFiles1, lp, unselectedColor, justPicked, ll);
                                });
                            }
                        });
                        runOnUiThread(() -> ll.addView(textViews[finalI]));
                    }
                    break;
            }
        }).start();
    }

    private void extractM1(File[] listFiles, LinearLayout.LayoutParams lp, int unselectedColor, TextView[] textViews, int i) {
        textViews[i] = new TextView(this);
        textViews[i].setTextSize(25);
        textViews[i].setText(listFiles[i].isFile() ? listFiles[i].getName() : (listFiles[i].getName() + "/"));
        textViews[i].setLayoutParams(lp);
        switch (option) {
            case 1:
                runOnUiThread(() -> {
                    if (listFiles[i].isFile()) {
                        textViews[i].setBackgroundColor(white);
                    } else textViews[i].setBackgroundColor(unselectedColor);
                });
                break;
            case 2:
                runOnUiThread(() -> {
                    if (listFiles[i].isFile()) {
                        textViews[i].setBackgroundColor(unselectedColor);
                    } else textViews[i].setBackgroundColor(white);
                });
                break;
        }
    }

    private void previous() {
        File parentFile = this.currentPath.getParentFile();
        File[] listFiles = parentFile.listFiles();
        fillViews(listFiles, lp, grey, justPicked, ll);
        this.currentPath = parentFile;
    }
}