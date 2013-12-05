package librerias;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {

    public CustomTextView(Context context) {
        super(context);
        setTypeface(CustomTypeFace.getInstance(context).getTypeFace());
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(CustomTypeFace.getInstance(context).getTypeFace());
    }

    public CustomTextView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(CustomTypeFace.getInstance(context).getTypeFace());
    }

}