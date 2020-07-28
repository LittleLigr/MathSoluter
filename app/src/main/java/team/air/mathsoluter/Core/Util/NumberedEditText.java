package team.air.mathsoluter.Core.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

public class NumberedEditText extends android.support.v7.widget.AppCompatEditText {

    private Rect rect;
    private Paint paint;

    private int tabCount=1;
    boolean flag = false;

    public NumberedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        rect = new Rect();
        rect.left = 15;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if(text.length()>0&&flag==false)
        {
            if(text.subSequence(start,start+1).toString().equals("{"))
                tabCount++;
            if(text.subSequence(start,start+1).toString().equals("}"))
            {
                if(text.length()>1)
                {
                    if (start!=0)
                        if(getText().subSequence(start-1, start).toString().equals("\t"))
                        {
                            flag=true;
                            setText(getText().replace(start-1, start, ""));
                            setSelection(start);
                        }
                }
                if(tabCount>1)
                    tabCount--;
            }
            if(text.subSequence(start,start+1).toString().equals("\n"))
                for(int i = 0; i < tabCount; i++)
                {
                    flag = true;
                    setText(getText().insert(start+1, "\t"));
                    setSelection(start+i+1);
                }
            flag=false;
        }



    }

    @Override
    protected void onDraw(Canvas canvas) {
        int baseline = getBaseline();
        for (int i = 0; i < getLineCount(); i++) {
            canvas.drawText("" + (i+1), rect.left, baseline, paint);
            baseline += getLineHeight();
        }
        super.onDraw(canvas);
    }
}
