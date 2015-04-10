package space.collabify.collabify.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import space.collabify.collabify.R;


/**
 * This file was born on March 29 at 18:15
 */
public class ImageToggleButton extends ToggleButton {
    private Drawable mCheckedSrc;
    private Drawable mUncheckedSrc;
    private Drawable mDisabledSrc;

    private boolean isEnabled;

    public ImageToggleButton(Context context) {
        super(context);
    }

    public ImageToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public ImageToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        TypedArray array = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.ImageToggleButton);

        //get image references
        try {
            mCheckedSrc = array.getDrawable(R.styleable.ImageToggleButton_checkedSrc);
            mUncheckedSrc = array.getDrawable(R.styleable.ImageToggleButton_uncheckedSrc);
            mDisabledSrc = array.getDrawable(R.styleable.ImageToggleButton_disabledSrc);
        } finally{
            array.recycle();
        }
        //set unchecked as default background
        setBackgroundDrawable(mUncheckedSrc);
        // default enabled
        isEnabled = true;
    }

    public void disable() {
        isEnabled = false;
        setBackgroundDrawable(mDisabledSrc);
    }

    public void enable() {
        isEnabled = true;

        if (isChecked()) {
            setBackgroundDrawable(mCheckedSrc);
        }
        else {
            setBackgroundDrawable(mUncheckedSrc);
        }
    }

    @Override
    public void setChecked(boolean checked) {

        if (isEnabled) {
            super.setChecked(checked);
            if(checked){
                setBackgroundDrawable(mCheckedSrc);
            }else {
                setBackgroundDrawable(mUncheckedSrc);
            }
        }
    }


}
