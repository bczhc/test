package pers.zhc.tools.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import pers.zhc.tools.BaseActivity;
import pers.zhc.tools.R;
import pers.zhc.tools.utils.ToastUtils;

import java.util.Objects;

public class Clip extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clip_activity);
        Button btn = findViewById(R.id.copy);
        EditText et = findViewById(R.id.copy_et);
        btn.setOnClickListener(v -> {
            String s = et.getText().toString();
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ClipData cd = ClipData.newHtmlText("", getString(R.string.html_content), s);
                try {
                    Objects.requireNonNull(cm).setPrimaryClip(cd);
                    ToastUtils.show(this, getString(R.string.copying_success));
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.show(this, R.string.copying_failure);
                }
            } else {
                ToastUtils.show(this, getString(R.string.html_clipboard_unsupported));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }
}