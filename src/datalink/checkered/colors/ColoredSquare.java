package datalink.checkered.colors;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ColoredSquare extends View {

    public static String LOG_TAG = "ColoredSquare class message";
    
    public ColoredSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
        // b2b2b2 d3ff69 bbff17 8000b2 be17ff
        mBackground = (ColorDrawable)getBackground();
        mHSVColor = new float[3];
        Color.colorToHSV(getColor(), mHSVColor);
        
    }
    
    public float getHue()
    {
        return mHSVColor[0];
    }
    
    public void setHue(float hue)
    {
        mHSVColor[0] = hue;
        setColor(Color.HSVToColor(mHSVColor));
    }
    
    private ColorDrawable mBackground;
    private float[] mHSVColor;
    
    // there were no standard way to retrieve xml-defined background color prior to API Lvl 11
    // solution found here http://www.wenda.io/questions/4129625/get-background-color-from-textview-without-using-colordrawable-api-11.html
    // some ugly-but-working reflection code follows
    
    public int getColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return mBackground.getColor();
        }
        try {
            Field field = mBackground.getClass().getDeclaredField("mState");
            field.setAccessible(true);
            Object object = field.get(mBackground);
            field = object.getClass().getDeclaredField("mUseColor");
            field.setAccessible(true);
            return field.getInt(object);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Cannot get color from the drawable!"); 
        }
        return 0;
    }
      
    // just for completeness, View.setBackgroundColor is available even at API Lvl 1
    public void setColor(int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mBackground.setColor(color);
        } else {
            try {
                final Field stateField = mBackground.getClass().getDeclaredField(
                        "mState");
                stateField.setAccessible(true);
                final Object state = stateField.get(mBackground);

                final Field useColorField = state.getClass().getDeclaredField(
                        "mUseColor");
                useColorField.setAccessible(true);
                useColorField.setInt(state, color);

                final Field baseColorField = state.getClass().getDeclaredField(
                        "mBaseColor");
                baseColorField.setAccessible(true);
                baseColorField.setInt(state, color);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Cannot set color to the drawable!");
            }
        }
    }
}
