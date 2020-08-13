package team.air.mathsoluter.Activities.CustomMathView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.core.util.TimeUtils;
import android.util.AttributeSet;


import katex.hourglass.in.mathlib.MathView;

public class CustomMathView extends MathView {
    private Paint paint;
    long buffer;
    boolean draw = false;
    int textSize;
    int lines = 0;
    public String text;
    String []textLines;
    int currentLine=0;
    public CustomMathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        buffer = System.currentTimeMillis();
    }

    public CustomMathView(Context context) {
        super(context);paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        buffer = System.currentTimeMillis();
    }

    @Override
    public void setTextSize(int size) {
        super.setTextSize(size);
        textSize = size;
    }

    @Override
    public void setDisplayText(String formula_text) {
        super.setDisplayText(formula_text);
        text = formula_text;
        textLines = formula_text.split("\n");
    }



    @Override
    protected void onDraw(Canvas canvas) {

        if(System.currentTimeMillis()-buffer>1000)
        {
            draw=!draw;
            buffer=System.currentTimeMillis();
        }
        if(draw)
            canvas.drawRect(getWidth()-10, currentLine*textSize-textSize/2, getWidth(), currentLine*textSize+textSize/2,paint);
        super.onDraw(canvas);
        //reload();
    }
}
