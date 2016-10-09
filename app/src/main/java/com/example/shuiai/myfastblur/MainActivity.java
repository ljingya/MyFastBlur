package com.example.shuiai.myfastblur;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screen = (Button) findViewById(R.id.screen);
        screen.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Bitmap img = blur();
        showDialoge(img);
    }

    private void showDialoge(Bitmap img) {
        Dialog dialoge = new Dialog(this);
        View view = View.inflate(this, R.layout.dialoge, null);
        dialoge.addContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dialoge.show();
        ImageView imageView = (ImageView) dialoge.findViewById(R.id.img);
        imageView.setImageBitmap(img);
    }

    private Bitmap overlay = null;

    /**
     * 对拿到的背景图片进行模糊处理
     *
     * @return
     */
    private Bitmap blur() {
        if (null != overlay) {
            return overlay;
        }
        //获取当前的屏幕视图
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();
        View view = getWindow().getDecorView();//截取当前view界面的视图
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap mBitmap = view.getDrawingCache();

        float radius = 20;
        overlay = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);
        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setDrawingCacheEnabled(false);
        return overlay;
    }
}
