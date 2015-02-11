package datalink.checkered.colors;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.graphics.PorterDuff;

public class ColorSeeker extends SeekBar 
{
    public ColorSeeker(Context context, AttributeSet attrs) 
    {
        super(context, attrs);
    }
    
    public void attachSquares(View root)
    {
        mTopLeftSquare = (ColoredSquare)root.findViewById(R.id.topLeftSquare);
        mTopRightSquare = (ColoredSquare)root.findViewById(R.id.topRightSquare);
        mBotLeftSquare = (ColoredSquare)root.findViewById(R.id.botLeftSquare);
        mBotRightSquare = (ColoredSquare)root.findViewById(R.id.botRightSquare);

        syncWithTopLeftSquare();
        
        setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {   
            int initial;
            @Override       
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                int delta = progress - initial;
                adjustHue(mTopLeftSquare, delta);
                adjustHue(mTopRightSquare, delta);
                adjustHue(mBotLeftSquare, delta);
                adjustHue(mBotRightSquare, delta);
                setProgressBarColor(mTopLeftSquare.getColor());
                initial = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0)
            {
                initial = getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar arg0)
            {
            }
        });
    }
    
    public void syncWithTopLeftSquare()
    {
        setProgress(hueAsProgress(mTopLeftSquare.getHue()));
        setProgressBarColor(mTopLeftSquare.getColor());
    }
    
    private int hueAsProgress(float hue)
    {
        return (int)(getMax() * (hue / 360.0f));
    }
    
    private void adjustHue(ColoredSquare square, int delta)
    {
        square.setHue(adjustHue(square.getHue(), delta));
    }
    
    private float adjustHue(float hue, int delta)
    {
        int value = hueAsProgress(hue);
        value += delta;
        
        if (value < 0)
            value += getMax();
        
        return (float)(value % getMax()) * 360.0f / getMax();
    }
    
    public void setProgressBarColor(int color) 
    {
        getProgressDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }
    
    ColoredSquare mTopLeftSquare;
    ColoredSquare mTopRightSquare;
    ColoredSquare mBotLeftSquare;
    ColoredSquare mBotRightSquare;
}
