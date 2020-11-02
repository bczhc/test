package pers.zhc.tools.document;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import pers.zhc.tools.BaseActivity;
import pers.zhc.tools.R;
import pers.zhc.tools.filepicker.FilePicker;
import pers.zhc.tools.utils.Common;
import pers.zhc.tools.utils.DialogUtil;
import pers.zhc.tools.utils.DisplayUtil;
import pers.zhc.tools.utils.ToastUtils;
import pers.zhc.u.FileU;
import static pers.zhc.tools.utils.DialogUtil.setDialogAttr;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.text.TextWatcher;
import android.text.Editable;

/**
 * @author bczhc
 */
public class Document extends BaseActivity {
    private ScrollView sv;
    private SQLiteDatabase db;
    private File dbFile = null;
    private String state="normal";

    private static class LinearLayoutWithTimestamp extends LinearLayout {

        public LinearLayoutWithTimestamp(Context context) {
            super(context);
        }

        private long timestamp;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_activity);
        Button insertBtn = findViewById(R.id.note_take);
        Button importBtn = findViewById(R.id.import_btn);
        Button exportBtn = findViewById(R.id.export_btn);
        insertBtn.setOnClickListener(v -> {
            Intent takingIntent = new Intent(this, NoteTakingActivity.class);
            startActivityForResult(takingIntent, RequestCode.START_ACTIVITY_1);
            overridePendingTransition(R.anim.in_left_and_bottom, 0);
        });
        Button deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(v -> {
                    if(state.equals("del"))
                    {
                        try{
                            for( int i = 0; i < ((LinearLayout) sv.getChildAt(0)).getChildCount(); i++){
                                LinearLayout childLL = (LinearLayout) ((LinearLayout) sv.getChildAt(0)).getChildAt(i);
                                if (((TextView) (((LinearLayout) childLL.getChildAt(0))).getChildAt(0)).getCurrentTextColor() == 0xFF0000) {
                                   //TODO delete record
                                   db.delete("doc", "id=?", new String[]{String.valueOf(((LinearLayoutWithTimestamp) childLL).timestamp)});
                               }
                        }
                    } catch (Exception e) {
                        Common.showException(e, this);
                    }
                    state = "normal";
                    }
                    else {
                    state = "del";
                    }
        });
        sv = findViewById(R.id.sv);
        importBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this, FilePicker.class);
            intent.putExtra("option", FilePicker.PICK_FILE);
            startActivityForResult(intent, RequestCode.START_ACTIVITY_2);
        });
        exportBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this, FilePicker.class);
            intent.putExtra("option", FilePicker.PICK_FOLDER);
            startActivityForResult(intent, RequestCode.START_ACTIVITY_3);
        });
        db = getDB(this);
        setSVViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCode.START_ACTIVITY_1:
                setSVViews();
                break;
            case RequestCode.START_ACTIVITY_2:
                if (data != null) {
                    File file = new File(Objects.requireNonNull(data.getStringExtra("result")));
                    try {
                        FileU.FileCopy(file, dbFile, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Common.showException(e, this);
                        return;
                    }
                       final AlertDialog confirmationAlertDialog = DialogUtil.createConfirmationAlertDialog(this, (dialog, which) -> {
                    setSVViews();
                       ToastUtils.show(this, R.string.importing_success);
                    }, (dialog, which) -> {
                        ToastUtils.show(this, R.string.importing_canceled);
                    }, R.string.whether_to_import_notes
   
                    , ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
                    if (file.exists()) {
                        if(((LinearLayout) sv.getChildAt(0)).getChildCount() != 0) {
                        confirmationAlertDialog.show();
                        } else {
                            setSVViews();
                            ToastUtils.show(this, R.string.importing_success);
                        }
                    } else {
                        ToastUtils.show(this, R.string.copying_failure);
                    }
                }
                break;
            case RequestCode.START_ACTIVITY_3:
                if (data != null) {
                    final String destFileDir = data.getStringExtra("result");
                    String dbPath = db.getPath();
                    File file = new File(dbPath);
                    AlertDialog.Builder adb=new AlertDialog.Builder(this);
                    View inflate = View.inflate(this, R.layout.export_notes, null);
                    final EditText filename=inflate.findViewById(R.id.filename);
                    filename.setText("doc");
                    final TextView tv=inflate.findViewById(R.id.export_notesTextview2);
                    if(!(new File(destFileDir + File.separator + "doc.db")).exists())
                    {
                        tv.setVisibility(View.INVISIBLE);
                    }
                    filename.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if((new File(destFileDir + File.separator + filename.getText()+".db")).exists())
                            {
                                tv.setVisibility(View.VISIBLE);
                                } else {
                                    tv.setVisibility(View.INVISIBLE);
                                    }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });
                    adb.setView(inflate);
                    adb.setPositiveButton(R.string.confirm, (dialog, which) -> {
                        try {
                            File destFile = new File(destFileDir + File.separator + filename.getText()+".db");
                            FileU.FileCopy(file, destFile);
                            if (destFile.exists()) {
                                ToastUtils.show(this, getString(R.string.exporting_success) + "\n" + destFile.getCanonicalPath());
                            }
                        } catch (IOException e) {
                            Common.showException(e, this);
                        }
                    } ).setNegativeButton(R.string.cancel, (dialog, which) -> {
                        
                    });
                    Dialog ad=adb.create();
                    setDialogAttr(ad, false, ViewGroup.LayoutParams.WRAP_CONTENT
                                  , ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    ad.show();
                  }
                break;
            default:
                break;
        }
    }

    private void setSVViews() {
        db = getDB(this);
        sv.removeAllViews();
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        sv.addView(linearLayout);
        Cursor cursor = db.rawQuery("SELECT * FROM doc", null);
        LinearLayout.LayoutParams ll_lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams smallLL_LP4 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 4F);
        int margin = DisplayUtil.px2sp(this, 10);
        ExecutorService es = Executors.newCachedThreadPool();
        String[] sqliteOptions = getResources().getStringArray(R.array.sqlite_options);
        if (cursor.moveToFirst()) {
            es.execute(() -> {
                do {
                    LinearLayoutWithTimestamp llWithTimestamp = new LinearLayoutWithTimestamp(this);
                    llWithTimestamp = new LinearLayoutWithTimestamp(this);
                    llWithTimestamp.setOrientation(LinearLayout.HORIZONTAL);
                    ll_lp.setMargins(margin, margin, margin, margin);
                    llWithTimestamp.setLayoutParams(ll_lp);
                    {//i = 0
                        LinearLayout smallLL = new LinearLayout(this);
                        long millisecond = cursor.getLong(0);
                        llWithTimestamp.timestamp = millisecond;
                        LinearLayoutWithTimestamp finalLlWithTimestamp = llWithTimestamp;
                        llWithTimestamp.setOnClickListener(v -> {
                            if(state.equals("del")){
                finalLlWithTimestamp.setBackground(getDrawable(R.drawable.view_stroke_red));
                for (int i = 0; i < finalLlWithTimestamp.getChildCount(); i++) {
               ((TextView) ((LinearLayout) finalLlWithTimestamp.getChildAt(i)).getChildAt(0)).setTextColor(0xFFFF0000);
                }
                } else {
                            Dialog dialog = new Dialog(this);
                            LinearLayout linearLayout1 = new LinearLayout(this);
                            linearLayout1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            linearLayout1.setOrientation(LinearLayout.VERTICAL);
                            DialogUtil.setDialogAttr(dialog, false, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
                            Button[] buttons = new Button[2];
                            View.OnClickListener[] onClickListeners = new View.OnClickListener[]{
                                    v1 -> {
                                        try {
                                            Intent intent = new Intent(this, NoteTakingActivity.class);
                                            intent.putExtra("origin", false);
                                            Cursor c = db.rawQuery("SELECT * FROM doc WHERE t=" + millisecond, null);
                                            c.moveToFirst();
                                            NoteTakingActivity.title = c.getString(1);
                                            NoteTakingActivity.content = c.getString(2);
                                            c.close();
                                            intent.putExtra("bottom_btn_string", getString(R.string.modification_record));
                                            intent.putExtra("millisecond", millisecond);
                                            startActivityForResult(intent, RequestCode.START_ACTIVITY_1);
                                            dialog.dismiss();
                                            overridePendingTransition(R.anim.in_left_and_bottom, 0);
                                        } catch (IndexOutOfBoundsException e) {
                                            e.printStackTrace();
                                            ToastUtils.show(this, e.toString());
                                        }
                                    },
                                    v1 -> {
                                        AlertDialog confirmationAD = DialogUtil.createConfirmationAlertDialog(this, (dialog1, which) -> {
                                            try {
                                                db.execSQL("DELETE FROM doc WHERE t=" + millisecond);
                                            } catch (Exception e) {
                                                Common.showException(e, this);
                                            }
                                            setSVViews();
                                            dialog.dismiss();
                                        }, (dialog1, which) -> {
                                        }, R.string.whether_to_delete, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
                                        confirmationAD.show();
                                    }
                            };
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i] = new Button(this);
                                buttons[i].setText(String.format(getString(R.string.str), sqliteOptions[i]));
                                buttons[i].setOnClickListener(onClickListeners[i]);
                                linearLayout1.addView(buttons[i]);
                            }
                            dialog.setContentView(linearLayout1);
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.show();
                            }
                        });
                        Date date = new Date(millisecond);
                        String formatDate = SimpleDateFormat.getDateTimeInstance().format(date);
                        setSmallTVExtracted(smallLL_LP4, llWithTimestamp, smallLL, formatDate);
                    }
                    for (int i = 1; i < 3; i++) {
                        LinearLayout smallLL = new LinearLayout(this);
                        String s = cursor.getString(i);
                        int length = s.length();
                        setSmallTVExtracted(smallLL_LP4, llWithTimestamp, smallLL, length > 100 ? (s.substring(0, 100) + "\n...") : s);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        llWithTimestamp.setBackground(getDrawable(R.drawable.view_stroke));
                    }
                    LinearLayoutWithTimestamp finalLlWithTimestamp1 = llWithTimestamp;
                    runOnUiThread(() -> linearLayout.addView(finalLlWithTimestamp1));
                } while (cursor.moveToNext());
                cursor.close();
            });
        }
    }

    private void setSmallTVExtracted(LinearLayout.LayoutParams smallLL_LP4, LinearLayout ll, LinearLayout smallLL, String s) {
        smallLL.setLayoutParams(smallLL_LP4);
        TextView tv = new TextView(this);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setText(String.format(getString(R.string.str), s));
        runOnUiThread(() -> {
            smallLL.addView(tv);
            tv.setTextSize(15F);
            ll.addView(smallLL);
        });
    }

    SQLiteDatabase getDB(Activity ctx) {
        /*DocDB db = new DocDB(ctx, "a", null, 1);
        return db.getWritableDatabase();*/
            SQLiteDatabase database = null;
            File dbPath = Common.getInternalDatabaseDir(this);
            if (!dbPath.exists()) {
                System.out.println("dbPath.mkdirs() = " + dbPath.mkdirs());
            }
            try {
                dbFile = new File(dbPath.getPath() + File.separator + "doc.db");
                database = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                database.execSQL("CREATE TABLE IF NOT EXISTS doc(\n" +
                                 "    t long,\n" +
                                 "    title text not null,\n" +
                                 "    content text not null\n" +
                                 ");");
            } catch (Exception e) {
                e.printStackTrace();
                Common.showException(e, ctx);
            }
            if (database != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    database.disableWriteAheadLogging();
                }
            }
            return database;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }
}
