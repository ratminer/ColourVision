package com.example.home.colourvision;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.View;

/**
 * Created by robertfernandes on 10/28/2016.
 */

public class FocusBox extends View {

    public FocusBox(Context context) {
        super(context);
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        canvas.drawRect(100,100,200,200, p);
        canvas.drawRect(400,400,600,600,p);
    }
}
