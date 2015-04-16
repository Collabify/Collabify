package space.collabify.android.fragments;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by andrew on 4/16/15.
 * Found at http://stackoverflow.com/questions/8533394/icons-in-a-list-dialog
 */
public class ArrayAdapterWithIcon extends ArrayAdapter<String> {

  private List<Integer> images;

  public ArrayAdapterWithIcon(Context context, List<String> items, List<Integer> images) {
    super(context, android.R.layout.select_dialog_item, items);
    this.images = images;
  }

  public ArrayAdapterWithIcon(Context context, String[] items, Integer[] images) {
    super(context, android.R.layout.select_dialog_item, items);
    this.images = Arrays.asList(images);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = super.getView(position, convertView, parent);
    TextView textView = (TextView) view.findViewById(android.R.id.text1);
    textView.setCompoundDrawablesWithIntrinsicBounds(images.get(position), 0, 0, 0);
    textView.setCompoundDrawablePadding(
      (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getContext().getResources().getDisplayMetrics()));
    return view;
  }

}